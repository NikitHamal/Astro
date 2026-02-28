package com.astro.vajra.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyDosha
import com.astro.vajra.data.localization.LocalLanguage
import com.astro.vajra.data.localization.currentLanguage
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.ArudhaPadaCalculator
import com.astro.vajra.ephemeris.ArudhaPadaCalculator.ArudhaPadaAnalysis
import com.astro.vajra.ephemeris.ArudhaPadaCalculator.ArudhaPada
import com.astro.vajra.ephemeris.ArudhaPadaCalculator.ArudhaPadaDetail
import com.astro.vajra.ephemeris.ArudhaPadaCalculator.ArudhaYoga
import com.astro.vajra.ephemeris.ArudhaPadaCalculator.ArudhaRelationship
import com.astro.vajra.ephemeris.ArudhaPadaCalculator.ArudhaStrength
import com.astro.vajra.ephemeris.ArudhaPadaCalculator.YogaStrength
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.TabItem
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
 * Arudha Pada Analysis Screen
 *
 * Comprehensive Jaimini Arudha Pada analysis showing:
 * - All 12 Arudha Padas and their positions
 * - Special Arudhas (AL, UL, A7, A10, A11) with detailed analysis
 * - Arudha Yogas (Raja Yoga, Dhana Yoga from Arudhas)
 * - Arudha relationships and their effects
 * - Transit and Dasha activation
 *
 * References: Jaimini Sutras, BPHS, Sanjay Rath's works
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArudhaPadaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(
            title = stringResource(StringKeyDosha.ARUDHA_SCREEN_TITLE),
            message = stringResource(StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    val language = currentLanguage()
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedArudha by remember { mutableStateOf<ArudhaPada?>(null) }
    var isCalculating by remember { mutableStateOf(true) }
    var arudhaAnalysis by remember { mutableStateOf<ArudhaPadaAnalysis?>(null) }

    val tabs = listOf(
        stringResource(StringKeyDosha.ARUDHA_TAB_OVERVIEW),
        stringResource(StringKeyDosha.ARUDHA_TAB_ALL),
        stringResource(StringKeyDosha.ARUDHA_TAB_YOGAS),
        stringResource(StringKeyDosha.ARUDHA_TAB_RELATIONSHIPS)
    )

    // Calculate Arudha Padas
    LaunchedEffect(chart) {
        isCalculating = true
        delay(300)
        try {
            arudhaAnalysis = withContext(Dispatchers.Default) {
                ArudhaPadaCalculator.analyzeArudhaPadas(chart, language)
            }
        } catch (e: Exception) {
            // Handle error silently
        }
        isCalculating = false
    }

    if (showInfoDialog) {
        ArudhaPadaInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.ARUDHA_SCREEN_TITLE),
                subtitle = chart.birthData.name,
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyDosha.ARUDHA_ABOUT_BTN),
                            tint = AppTheme.TextPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isCalculating) {
            LoadingContent(paddingValues)
        } else if (arudhaAnalysis != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Tab row
                val tabItems = tabs.map { TabItem(title = it, accentColor = AppTheme.AccentPrimary) }
                
                ModernPillTabRow(
                    tabs = tabItems,
                    selectedIndex = selectedTab,
                    onTabSelected = { selectedTab = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // Content based on selected tab
                when (selectedTab) {
                    0 -> OverviewTab(arudhaAnalysis!!)
                    1 -> AllArudhasTab(
                        arudhaAnalysis!!,
                        selectedArudha = selectedArudha,
                        onArudhaSelected = { selectedArudha = it }
                    )
                    2 -> YogasTab(arudhaAnalysis!!)
                    3 -> RelationshipsTab(arudhaAnalysis!!)
                }
            }
        }
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = AppTheme.AccentPrimary,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = stringResource(StringKeyDosha.ARUDHA_CALCULATING),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted
            )
        }
    }
}

// ============================================
// OVERVIEW TAB
// ============================================

@Composable
private fun OverviewTab(analysis: ArudhaPadaAnalysis) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary card
        item {
            SummaryCard(analysis)
        }

        // Key Arudhas highlight
        item {
            KeyArudhasCard(analysis)
        }

        // Overall Assessment
        item {
            AssessmentCard(analysis.overallAssessment)
        }

        // Key Yogas preview
        if (analysis.arudhaYogas.isNotEmpty()) {
            item {
                KeyYogasPreview(analysis.arudhaYogas.take(3))
            }
        }

        // Recommendations
        item {
            RecommendationsCard(analysis.interpretation.recommendations)
        }
    }
}

@Composable
private fun SummaryCard(analysis: ArudhaPadaAnalysis) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.ARUDHA_ANALYSIS_TITLE),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
            }

            Text(
                text = analysis.interpretation.summary,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun KeyArudhasCard(analysis: ArudhaPadaAnalysis) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(StringKeyDosha.ARUDHA_KEY_POSITIONS),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            // Key Arudhas in a grid-like layout
            val keyArudhas = listOf(
                analysis.specialArudhas.arudhaLagna to Icons.Outlined.Person,
                analysis.specialArudhas.rajyaPada to Icons.Outlined.Work,
                analysis.specialArudhas.labhaPada to Icons.Outlined.TrendingUp,
                analysis.specialArudhas.darapada to Icons.Outlined.Handshake,
                analysis.specialArudhas.upapada to Icons.Outlined.Favorite
            )

            keyArudhas.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { (detail, icon) ->
                        KeyArudhaChip(
                            detail = detail,
                            icon = icon,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Fill empty space if odd number
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyArudhaChip(
    detail: ArudhaPadaDetail,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val strengthColor = when (detail.arudha.strength) {
        ArudhaStrength.VERY_STRONG -> AppTheme.SuccessColor
        ArudhaStrength.STRONG -> com.astro.vajra.ui.theme.SuccessDark
        ArudhaStrength.MODERATE -> AppTheme.AccentGold
        ArudhaStrength.WEAK -> AppTheme.WarningColor
        ArudhaStrength.VERY_WEAK -> AppTheme.ErrorColor
    }

    Surface(
        modifier = modifier,
        color = AppTheme.ChipBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(strengthColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = strengthColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = detail.arudha.name,
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = detail.arudha.sign.displayName,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun AssessmentCard(assessment: ArudhaPadaCalculator.ArudhaOverallAssessment) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(StringKeyDosha.ARUDHA_MANIFESTATION),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            // Strength bars
            StrengthBar(
                label = stringResource(StringKeyDosha.ARUDHA_PUBLIC_IMAGE),
                value = assessment.publicImageStrength,
                icon = Icons.Outlined.Person
            )
            StrengthBar(
                label = stringResource(StringKeyDosha.ARUDHA_CAREER),
                value = assessment.careerManifestationStrength,
                icon = Icons.Outlined.Work
            )
            StrengthBar(
                label = stringResource(StringKeyDosha.ARUDHA_GAINS),
                value = assessment.gainsAndFulfillment,
                icon = Icons.Outlined.TrendingUp
            )
            StrengthBar(
                label = stringResource(StringKeyDosha.ARUDHA_RELATIONSHIPS),
                value = assessment.relationshipIndicator,
                icon = Icons.Outlined.Favorite
            )

            HorizontalDivider(color = AppTheme.BorderColor, modifier = Modifier.padding(vertical = 4.dp))

            // Key themes
            if (assessment.keyThemes.isNotEmpty()) {
                Text(
                    text = stringResource(StringKeyDosha.ARUDHA_KEY_THEMES),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(assessment.keyThemes) { theme ->
                        Surface(
                            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = AppTheme.AccentPrimary.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = theme,
                                fontFamily = PoppinsFontFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                color = AppTheme.AccentPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StrengthBar(
    label: String,
    value: Int,
    icon: ImageVector
) {
    val color = when {
        value >= 70 -> AppTheme.SuccessColor
        value >= 50 -> AppTheme.AccentGold
        value >= 30 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.TextMuted,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            fontFamily = PoppinsFontFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            color = AppTheme.TextSecondary,
            modifier = Modifier.width(80.dp)
        )
        LinearProgressIndicator(
            progress = { value / 100f },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)),
            color = color,
            trackColor = AppTheme.ChipBackground
        )
        Text(
            text = "$value%",
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun KeyYogasPreview(yogas: List<ArudhaYoga>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Stars,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.ARUDHA_YOGAS_TITLE),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            yogas.forEach { yoga ->
                YogaPreviewItem(yoga)
            }
        }
    }
}

@Composable
private fun YogaPreviewItem(yoga: ArudhaYoga) {
    val strengthColor = when (yoga.strength) {
        YogaStrength.EXCEPTIONAL -> AppTheme.AccentGold
        YogaStrength.STRONG -> AppTheme.SuccessColor
        YogaStrength.MODERATE -> AppTheme.AccentPrimary
        YogaStrength.MILD -> AppTheme.TextMuted
        YogaStrength.WEAK -> AppTheme.TextSubtle
    }

    Surface(
        color = AppTheme.ChipBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(strengthColor)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = yoga.name,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = yoga.effects,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
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
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
                    text = stringResource(StringKeyDosha.ARUDHA_RECOMMENDATIONS),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            recommendations.take(5).forEach { rec ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "\u2022",
                        color = AppTheme.AccentPrimary
                    )
                    Text(
                        text = rec,
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

// ============================================
// ALL ARUDHAS TAB
// ============================================

@Composable
private fun AllArudhasTab(
    analysis: ArudhaPadaAnalysis,
    selectedArudha: ArudhaPada?,
    onArudhaSelected: (ArudhaPada?) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(analysis.arudhaPadas) { arudha ->
            ArudhaPadaCard(
                arudha = arudha,
                isExpanded = selectedArudha == arudha,
                onClick = {
                    onArudhaSelected(if (selectedArudha == arudha) null else arudha)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArudhaPadaCard(
    arudha: ArudhaPada,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val strengthColor = when (arudha.strength) {
        ArudhaStrength.VERY_STRONG -> AppTheme.SuccessColor
        ArudhaStrength.STRONG -> com.astro.vajra.ui.theme.SuccessDark
        ArudhaStrength.MODERATE -> AppTheme.AccentGold
        ArudhaStrength.WEAK -> AppTheme.WarningColor
        ArudhaStrength.VERY_WEAK -> AppTheme.ErrorColor
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Arudha badge
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        strengthColor.copy(alpha = 0.3f),
                                        strengthColor.copy(alpha = 0.1f)
                                    )
                                )
                            )
                            .border(1.dp, strengthColor.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = arudha.name,
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            fontWeight = FontWeight.Bold,
                            color = strengthColor
                        )
                    }

                    Column {
                        Text(
                            text = arudha.fullName,
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = arudha.sign.symbol,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14
                            )
                            Text(
                                text = arudha.sign.displayName,
                                fontFamily = PoppinsFontFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                color = AppTheme.TextMuted
                            )
                        }
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted
                )
            }

            // Strength indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(StringKeyDosha.ARUDHA_STRENGTH_LABEL),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted
                )
                Surface(
                    color = strengthColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                ) {
                    Text(
                        text = arudha.strength.name.replace("_", " "),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = strengthColor,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = "\u2022 " + stringResource(
                        StringKeyDosha.ARUDHA_LORD_IN_SIGN,
                        arudha.houseLord.displayName,
                        arudha.houseLordSign.displayName
                    ),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted
                )
            }

            // Expanded content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HorizontalDivider(color = AppTheme.BorderColor)

                    // Interpretation
                    Text(
                        text = arudha.interpretation,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )

                    // Planets in Arudha
                    if (arudha.planetsInArudha.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Circle,
                                contentDescription = null,
                                tint = AppTheme.AccentPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = stringResource(StringKeyDosha.ARUDHA_PLANETS_IN_LABEL, arudha.name),
                                fontFamily = SpaceGroteskFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.TextPrimary
                            )
                            val language = LocalLanguage.current
                            Text(
                                text = arudha.planetsInArudha.joinToString { it.planet.getLocalizedName(language) },
                                fontFamily = PoppinsFontFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                color = AppTheme.TextSecondary
                            )
                        }
                    }

                    // Significations
                    Text(
                        text = stringResource(StringKeyDosha.ARUDHA_SIGNIFICATIONS_LABEL),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(arudha.significations) { signification ->
                            Surface(
                                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                                color = AppTheme.ChipBackground
                            ) {
                                Text(
                                    text = signification,
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                    color = AppTheme.TextSecondary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
// YOGAS TAB
// ============================================

@Composable
private fun YogasTab(analysis: ArudhaPadaAnalysis) {
    if (analysis.arudhaYogas.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Stars,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.UI_NO_ARUDHA_YOGAS),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    color = AppTheme.TextMuted
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(analysis.arudhaYogas) { yoga ->
                YogaCard(yoga)
            }
        }
    }
}

@Composable
private fun YogaCard(yoga: ArudhaYoga) {
    val strengthColor = when (yoga.strength) {
        YogaStrength.EXCEPTIONAL -> AppTheme.AccentGold
        YogaStrength.STRONG -> AppTheme.SuccessColor
        YogaStrength.MODERATE -> AppTheme.AccentPrimary
        YogaStrength.MILD -> AppTheme.TextMuted
        YogaStrength.WEAK -> AppTheme.TextSubtle
    }

    val typeIcon = when (yoga.type) {
        ArudhaPadaCalculator.ArudhaYogaType.RAJA_YOGA -> Icons.Outlined.MilitaryTech
        ArudhaPadaCalculator.ArudhaYogaType.DHANA_YOGA -> Icons.Outlined.AttachMoney
        ArudhaPadaCalculator.ArudhaYogaType.PARIVARTANA -> Icons.Outlined.SwapHoriz
        ArudhaPadaCalculator.ArudhaYogaType.ARGALA_YOGA -> Icons.Outlined.Shield
        ArudhaPadaCalculator.ArudhaYogaType.BHAVA_YOGA -> Icons.Outlined.Home
        ArudhaPadaCalculator.ArudhaYogaType.GRAHA_YOGA -> Icons.Outlined.Stars
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
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
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(strengthColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = typeIcon,
                            contentDescription = null,
                            tint = strengthColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = yoga.name,
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = yoga.type.name.replace("_", " "),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Surface(
                    color = strengthColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                ) {
                    Text(
                        text = yoga.strength.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = strengthColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Involved Arudhas
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                yoga.involvedArudhas.forEach { arudha ->
                    Surface(
                        color = AppTheme.AccentPrimary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                    ) {
                        Text(
                            text = arudha,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = AppTheme.AccentPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Text(
                    text = stringResource(StringKeyDosha.ARUDHA_IN_CONNECTOR),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                yoga.involvedSigns.forEach { sign ->
                    Text(
                        text = "${sign.symbol} ${sign.displayName}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            // Effects
            Text(
                text = yoga.effects,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp
            )

            // Timing
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = yoga.timing,
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted
                )
            }

            // Recommendations
            if (yoga.recommendations.isNotEmpty()) {
                HorizontalDivider(color = AppTheme.BorderColor)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    yoga.recommendations.forEach { rec ->
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("\u2022", color = AppTheme.AccentPrimary, fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12)
                            Text(
                                text = rec,
                                fontFamily = PoppinsFontFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                color = AppTheme.TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

// ============================================
// RELATIONSHIPS TAB
// ============================================

@Composable
private fun RelationshipsTab(analysis: ArudhaPadaAnalysis) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(analysis.arudhaRelationships) { relationship ->
            RelationshipCard(relationship)
        }
    }
}

@Composable
private fun RelationshipCard(relationship: ArudhaRelationship) {
    val isPositiveColor = if (relationship.isPositive) AppTheme.SuccessColor else AppTheme.WarningColor

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Arudha 1
            Surface(
                color = AppTheme.AccentPrimary.copy(alpha = 0.1f),
                shape = CircleShape
            ) {
                Text(
                    text = relationship.arudha1,
                    modifier = Modifier.padding(12.dp),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.AccentPrimary
                )
            }

            // Relationship indicator
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.width(20.dp),
                        color = isPositiveColor
                    )
                    Icon(
                        imageVector = if (relationship.isPositive)
                            Icons.Outlined.CheckCircle else Icons.Outlined.Warning,
                        contentDescription = null,
                        tint = isPositiveColor,
                        modifier = Modifier.size(16.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.width(20.dp),
                        color = isPositiveColor
                    )
                }
                Text(
                    text = relationship.relationship.name.replace("_", " "),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = "${relationship.distanceInSigns} signs",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextSubtle
                )
            }

            // Arudha 2
            Surface(
                color = AppTheme.AccentPrimary.copy(alpha = 0.1f),
                shape = CircleShape
            ) {
                Text(
                    text = relationship.arudha2,
                    modifier = Modifier.padding(12.dp),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.AccentPrimary
                )
            }
        }

        // Effect description
        Text(
            text = relationship.effect,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontFamily = PoppinsFontFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            color = AppTheme.TextSecondary
        )
    }
}

// ============================================
// INFO DIALOG
// ============================================

@Composable
private fun ArudhaPadaInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyDosha.ARUDHA_INFO_TITLE),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = stringResource(StringKeyDosha.ARUDHA_WHAT_IS_TITLE),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = stringResource(StringKeyDosha.ARUDHA_WHAT_IS_DESC),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary
                    )
                }

                item {
                    Text(
                        text = stringResource(StringKeyDosha.ARUDHA_KEY_ARUDHAS_TITLE),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(
                            "AL (A1)" to stringResource(StringKeyDosha.ARUDHA_KEY_AL_DESC),
                            "A7" to stringResource(StringKeyDosha.ARUDHA_KEY_A7_DESC),
                            "A10" to stringResource(StringKeyDosha.ARUDHA_KEY_A10_DESC),
                            "A11" to stringResource(StringKeyDosha.ARUDHA_KEY_A11_DESC),
                            "UL (A12)" to stringResource(StringKeyDosha.ARUDHA_KEY_UL_DESC)
                        ).forEach { (name, desc) ->
                            Row {
                                Text(
                                    text = "$name: ",
                                    fontFamily = SpaceGroteskFamily,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                    fontWeight = FontWeight.Medium,
                                    color = AppTheme.AccentPrimary
                                )
                                Text(
                                    text = desc,
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource(StringKeyDosha.ARUDHA_CALC_METHOD_TITLE),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = stringResource(StringKeyDosha.ARUDHA_CALC_METHOD_DESC),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary
                    )
                }

                item {
                    Text(
                        text = stringResource(StringKeyDosha.ARUDHA_REFERENCES_TITLE),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = stringResource(StringKeyDosha.ARUDHA_REFERENCES_DESC),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(StringKeyDosha.BTN_GOT_IT), color = AppTheme.AccentPrimary, fontFamily = SpaceGroteskFamily)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}
