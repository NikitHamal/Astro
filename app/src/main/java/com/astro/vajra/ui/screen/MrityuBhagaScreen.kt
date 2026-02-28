package com.astro.vajra.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyDosha
import com.astro.vajra.core.common.StringKeyFinder
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.data.localization.currentLanguage
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.MrityuBhagaCalculator
import com.astro.vajra.ephemeris.SensitiveDegreesAnalysis
import com.astro.vajra.ephemeris.MrityuBhagaResult
import com.astro.vajra.ephemeris.MrityuBhagaSeverity
import com.astro.vajra.ephemeris.GandantaResult
import com.astro.vajra.ephemeris.GandantaSeverity
import com.astro.vajra.ephemeris.GandantaType
import com.astro.vajra.ephemeris.PushkaraNavamsaResult
import com.astro.vajra.ephemeris.PushkaraBhagaResult
import com.astro.vajra.ephemeris.AssessmentLevel
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
 * Mrityu Bhaga Analysis Screen
 *
 * Comprehensive sensitive degrees analysis showing:
 * - Mrityu Bhaga (death portion) degrees where planets are vulnerable
 * - Gandanta (karmic knot) junctions at water-fire sign boundaries
 * - Pushkara Navamsa (auspicious degrees) for protection
 * - Pushkara Bhaga (nourishing degrees) benefits
 *
 * Based on classical texts: Phaladeepika, BPHS, Saravali
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MrityuBhagaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(
            title = stringResource(StringKeyDosha.MRITYU_BHAGA_SCREEN_TITLE),
            message = stringResource(StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    val language = currentLanguage()
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var analysisResult by remember { mutableStateOf<SensitiveDegreesAnalysis?>(null) }

    val tabs = listOf(
        stringResource(StringKeyDosha.SCREEN_OVERVIEW),
        stringResource(StringKeyDosha.MRITYU_BHAGA_SIGN_DEGREES),
        stringResource(StringKeyDosha.MRITYU_BHAGA_REMEDIES_SECTION)
    )

    // Calculate analysis
    LaunchedEffect(chart, language) {
        isCalculating = true
        delay(300)
        withContext(Dispatchers.Default) {
            analysisResult = MrityuBhagaCalculator.analyzeSensitiveDegrees(chart, language)
        }
        isCalculating = false
    }

    Scaffold(
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.MRITYU_BHAGA_SCREEN_TITLE),
                subtitle = stringResource(StringKeyDosha.MRITYU_BHAGA_SCREEN_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyDosha.MRITYU_BHAGA_SCREEN_ABOUT),
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
                        MrityuBhagaTabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }

                    // Content based on selected tab
                    when (selectedTab) {
                        0 -> item { OverviewTab(analysisResult!!, language) }
                        1 -> item { SignDegreesTab(analysisResult!!, language) }
                        2 -> item { RemediesTab(analysisResult!!, language) }
                    }
                }
            }
        }
    }

    // Info Dialog
    if (showInfoDialog) {
        MrityuBhagaInfoDialog(onDismiss = { showInfoDialog = false })
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
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
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
            title = stringResource(StringKeyDosha.MRITYU_BHAGA_SCREEN_TITLE),
            subtitle = message,
            icon = Icons.Outlined.ErrorOutline
        )
    }
}

@Composable
private fun MrityuBhagaTabSelector(
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
private fun OverviewTab(analysis: SensitiveDegreesAnalysis, language: Language) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Overall Assessment Card
        OverallAssessmentCard(analysis)

        // Critical Placements (Mrityu Bhaga)
        val criticalMrityu = analysis.mrityuBhagaAnalysis.filter { it.isInMrityuBhaga }
        if (criticalMrityu.isNotEmpty()) {
            SectionHeader(
                title = stringResource(StringKeyDosha.MRITYU_BHAGA_PLANETS_AFFECTED),
                icon = Icons.Filled.Warning,
                tint = AppTheme.WarningColor
            )
            criticalMrityu.forEach { result ->
                MrityuBhagaPlanetCard(result, language)
            }
        }

        // Gandanta Placements
        val gandantaPlanets = analysis.gandantaAnalysis.filter { it.isInGandanta }
        if (gandantaPlanets.isNotEmpty()) {
            SectionHeader(
                title = stringResource(StringKeyDosha.MRITYU_BHAGA_GANDANTA_PLACEMENTS),
                icon = Icons.Filled.Waves,
                tint = AppTheme.ErrorColor
            )
            gandantaPlanets.forEach { result ->
                GandantaPlanetCard(result, language)
            }
        }

        // Auspicious Placements (Pushkara)
        val pushkaraNavamsa = analysis.pushkaraNavamsaAnalysis.filter { it.isInPushkaraNavamsa }
        val pushkaraBhaga = analysis.pushkaraBhagaAnalysis.filter { it.isInPushkaraBhaga }

        if (pushkaraNavamsa.isNotEmpty() || pushkaraBhaga.isNotEmpty()) {
            SectionHeader(
                title = stringResource(StringKeyDosha.MRITYU_BHAGA_AUSPICIOUS_PLACEMENTS),
                icon = Icons.Filled.Star,
                tint = AppTheme.SuccessColor
            )
            pushkaraNavamsa.forEach { result ->
                PushkaraNavamsaCard(result, language)
            }
            pushkaraBhaga.forEach { result ->
                PushkaraBhagaCard(result, language)
            }
        }

        // No Critical Placements Message
        if (criticalMrityu.isEmpty() && gandantaPlanets.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.SuccessColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.SuccessColor.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            stringResource(StringKeyDosha.MRITYU_BHAGA_NO_PLANETS),
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16
                        )
                        Text(
                            stringResource(StringKeyDosha.MRITYU_BHAGA_NO_CRITICAL),
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                            color = AppTheme.TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OverallAssessmentCard(analysis: SensitiveDegreesAnalysis) {
    val assessment = analysis.overallAssessment

    val (backgroundColor, iconColor, icon) = when (assessment.level) {
        AssessmentLevel.NEEDS_ATTENTION -> Triple(
            AppTheme.ErrorColor.copy(alpha = 0.1f),
            AppTheme.ErrorColor,
            Icons.Filled.Warning
        )
        AssessmentLevel.MODERATE_CONCERN -> Triple(
            AppTheme.WarningColor.copy(alpha = 0.1f),
            AppTheme.WarningColor,
            Icons.Outlined.Info
        )
        AssessmentLevel.BALANCED -> Triple(
            AppTheme.AccentGold.copy(alpha = 0.1f),
            AppTheme.AccentGold,
            Icons.Filled.Balance
        )
        AssessmentLevel.GENERALLY_POSITIVE -> Triple(
            AppTheme.SuccessColor.copy(alpha = 0.1f),
            AppTheme.SuccessColor,
            Icons.Filled.CheckCircle
        )
        AssessmentLevel.HIGHLY_AUSPICIOUS -> Triple(
            AppTheme.SuccessColor.copy(alpha = 0.15f),
            AppTheme.SuccessColor,
            Icons.Filled.Star
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
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        stringResource(StringKeyDosha.MRITYU_BHAGA_OVERALL_ASSESSMENT),
                        fontFamily = CinzelDecorativeFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        when (assessment.level) {
                            AssessmentLevel.NEEDS_ATTENTION -> stringResource(StringKeyDosha.MRITYU_BHAGA_LEVEL_NEEDS_ATTENTION)
                            AssessmentLevel.MODERATE_CONCERN -> stringResource(StringKeyDosha.MRITYU_BHAGA_LEVEL_MODERATE_CONCERN)
                            AssessmentLevel.BALANCED -> stringResource(StringKeyDosha.MRITYU_BHAGA_LEVEL_BALANCED)
                            AssessmentLevel.GENERALLY_POSITIVE -> stringResource(StringKeyDosha.MRITYU_BHAGA_LEVEL_GENERALLY_POSITIVE)
                            AssessmentLevel.HIGHLY_AUSPICIOUS -> stringResource(StringKeyDosha.MRITYU_BHAGA_LEVEL_HIGHLY_AUSPICIOUS)
                        },
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = iconColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                assessment.summary,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = stringResource(StringKeyDosha.MRITYU_BHAGA_CRITICAL_COUNT),
                    value = assessment.criticalPlacementCount.toString(),
                    color = if (assessment.criticalPlacementCount > 0) AppTheme.ErrorColor else AppTheme.TextMuted
                )
                StatItem(
                    label = stringResource(StringKeyDosha.MRITYU_BHAGA_AUSPICIOUS_COUNT),
                    value = assessment.auspiciousPlacementCount.toString(),
                    color = if (assessment.auspiciousPlacementCount > 0) AppTheme.SuccessColor else AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            label,
            fontFamily = PoppinsFontFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            title,
            fontFamily = CinzelDecorativeFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun MrityuBhagaPlanetCard(result: MrityuBhagaResult, language: Language) {
    var expanded by remember { mutableStateOf(false) }

    val severityColor = when (result.severity) {
        MrityuBhagaSeverity.EXACT -> AppTheme.ErrorColor
        MrityuBhagaSeverity.VERY_CLOSE -> AppTheme.ErrorColor.copy(alpha = 0.8f)
        MrityuBhagaSeverity.WITHIN_ORB -> AppTheme.WarningColor
        MrityuBhagaSeverity.APPROACHING -> AppTheme.WarningColor.copy(alpha = 0.7f)
        MrityuBhagaSeverity.SAFE -> AppTheme.SuccessColor
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        color = AppTheme.CardBackground,
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
                            .background(AppTheme.getPlanetColor(result.planet).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            result.planet.displayName.take(2),
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            color = AppTheme.getPlanetColor(result.planet)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            result.planet.getLocalizedName(language),
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            "${result.sign.getLocalizedName(language)} ${String.format("%.1f", result.actualDegree)}\u00B0",
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                // Severity Badge
                Surface(
                    color = severityColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                ) {
                    Text(
                        result.severity.getLocalizedName(language),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                        fontWeight = FontWeight.Medium,
                        color = severityColor
                    )
                }
            }

            // Mrityu Bhaga degree info
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(StringKeyDosha.MRITYU_BHAGA_SENSITIVE_DEGREE),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted
                )
                Text(
                    "${String.format("%.1f", result.mrityuBhagaDegree)}\u00B0",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(StringKeyDosha.MRITYU_BHAGA_ORB),
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted
                )
                Text(
                    "${String.format("%.2f", result.distanceFromMrityuBhaga)}\u00B0",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    fontWeight = FontWeight.Medium,
                    color = severityColor
                )
            }

            // Expandable content
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (result.effects.isNotEmpty()) {
                        Text(
                            stringResource(StringKeyDosha.EFFECTS_LABEL),
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            color = AppTheme.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        result.effects.forEach { effect ->
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

                    if (result.vulnerabilityAreas.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(StringKeyDosha.MRITYU_BHAGA_LIFE_AREAS),
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            color = AppTheme.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(result.vulnerabilityAreas) { area ->
                                Surface(
                                    color = AppTheme.ChipBackground,
                                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                                ) {
                                    Text(
                                        area,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        fontFamily = PoppinsFontFamily,
                                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
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
}

@Composable
private fun GandantaPlanetCard(result: GandantaResult, language: Language) {
    var expanded by remember { mutableStateOf(false) }

    val severityColor = when (result.severity) {
        GandantaSeverity.EXACT_JUNCTION -> AppTheme.ErrorColor
        GandantaSeverity.CRITICAL -> AppTheme.ErrorColor.copy(alpha = 0.8f)
        GandantaSeverity.SEVERE -> AppTheme.WarningColor
        GandantaSeverity.MODERATE -> AppTheme.WarningColor.copy(alpha = 0.7f)
        GandantaSeverity.MILD -> AppTheme.AccentGold
    }

    val gandantaTypeName = when (result.gandantaType) {
        GandantaType.BRAHMA_GANDANTA -> stringResource(StringKeyDosha.MRITYU_BHAGA_GANDANTA_BRAHMA)
        GandantaType.VISHNU_GANDANTA -> stringResource(StringKeyDosha.MRITYU_BHAGA_GANDANTA_VISHNU)
        GandantaType.SHIVA_GANDANTA -> stringResource(StringKeyDosha.MRITYU_BHAGA_GANDANTA_SHIVA)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        color = AppTheme.CardBackground,
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
                            .background(AppTheme.getPlanetColor(result.planet).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Waves,
                            contentDescription = null,
                            tint = AppTheme.getPlanetColor(result.planet),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            result.planet.getLocalizedName(language),
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            gandantaTypeName,
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = severityColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Surface(
                    color = severityColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                ) {
                    Text(
                        result.severity.getLocalizedName(language),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                        fontWeight = FontWeight.Medium,
                        color = severityColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                stringResource(StringKeyDosha.MRITYU_BHAGA_JUNCTION_DESC, result.waterSign.getLocalizedName(language), result.fireSign.getLocalizedName(language)),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted
            )
            Text(
                stringResource(StringKeyDosha.MRITYU_BHAGA_DISTANCE_JUNCTION, String.format("%.2f", result.distanceFromJunction) + "\u00B0"),
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = severityColor
            )

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (result.effects.isNotEmpty()) {
                        Text(
                            stringResource(StringKeyDosha.EFFECTS_LABEL),
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            color = AppTheme.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        result.effects.forEach { effect ->
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
                }
            }
        }
    }
}

@Composable
private fun PushkaraNavamsaCard(result: PushkaraNavamsaResult, language: Language) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.SuccessColor.copy(alpha = 0.08f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.SuccessColor.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Star,
                contentDescription = null,
                tint = AppTheme.SuccessColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${result.planet.getLocalizedName(language)} " + stringResource(StringKeyFinder.LABEL_PUSH_NAV_IN),
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    "${result.sign.getLocalizedName(language)} ${String.format("%.1f", result.degree)}\u00B0",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted
                )
                if (result.benefits.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        result.benefits.first(),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                        color = AppTheme.SuccessColor
                    )
                }
            }
        }
    }
}

@Composable
private fun PushkaraBhagaCard(result: PushkaraBhagaResult, language: Language) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.AccentGold.copy(alpha = 0.08f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.AccentGold.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Spa,
                contentDescription = null,
                tint = AppTheme.AccentGold,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${result.planet.getLocalizedName(language)} " + stringResource(StringKeyFinder.LABEL_PUSH_BHAGA_IN),
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    StringResources.get(StringKeyFinder.LABEL_NOURISHING_DEGREE, language, String.format("%.1f", result.pushkaraBhagaDegree), String.format("%.2f", result.distance)),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun SignDegreesTab(analysis: SensitiveDegreesAnalysis, language: Language) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                stringResource(StringKeyDosha.MRITYU_BHAGA_ALL_SIGNS),
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.Bold,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Group by planet
        val planetGroups = analysis.mrityuBhagaAnalysis.groupBy { it.planet }

        items(planetGroups.entries.toList()) { (planet, results) ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardBackground,
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(AppTheme.getPlanetColor(planet).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                planet.displayName.take(2),
                                fontFamily = CinzelDecorativeFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                color = AppTheme.getPlanetColor(planet)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            planet.getLocalizedName(language),
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    results.forEach { result ->
                        val isInMrityu = result.isInMrityuBhaga
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                result.sign.getLocalizedName(language),
                                fontFamily = PoppinsFontFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                                color = if (isInMrityu) AppTheme.WarningColor else AppTheme.TextSecondary
                            )
                            Row {
                                Text(
                                    StringResources.get(StringKeyFinder.LABEL_MB_DEGREE, language, String.format("%.0f", result.mrityuBhagaDegree)),
                                    fontFamily = SpaceGroteskFamily,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                    color = AppTheme.TextMuted
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    StringResources.get(StringKeyFinder.LABEL_ACTUAL_DEGREE, language, String.format("%.1f", result.actualDegree)),
                                    fontFamily = SpaceGroteskFamily,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                    color = if (isInMrityu) AppTheme.WarningColor else AppTheme.TextSecondary,
                                    fontWeight = if (isInMrityu) FontWeight.Medium else FontWeight.Normal
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
private fun RemediesTab(analysis: SensitiveDegreesAnalysis, language: Language) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // General Recommendations
        item {
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
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                            color = AppTheme.TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    analysis.recommendations.forEach { recommendation ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                tint = AppTheme.SuccessColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                recommendation,
                                fontFamily = PoppinsFontFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                color = AppTheme.TextSecondary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }

        // Planet-specific remedies
        val criticalPlanets = analysis.mrityuBhagaAnalysis.filter { it.isInMrityuBhaga }

        if (criticalPlanets.isNotEmpty()) {
            item {
                Text(
                    stringResource(StringKeyFinder.LABEL_PLANET_SPEC_REM),
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                    color = AppTheme.TextPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(criticalPlanets) { result ->
                if (result.remedies.isNotEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = AppTheme.CardBackground,
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(AppTheme.getPlanetColor(result.planet).copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        result.planet.displayName.take(2),
                                        fontFamily = CinzelDecorativeFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                        color = AppTheme.getPlanetColor(result.planet)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    StringResources.get(StringKeyFinder.LABEL_PLANET_REM, language, result.planet.getLocalizedName(language)),
                                    fontFamily = CinzelDecorativeFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppTheme.TextPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            result.remedies.forEach { remedy ->
                                Row(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text("\u2022", color = AppTheme.AccentPrimary, fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        remedy,
                                        fontFamily = PoppinsFontFamily,
                                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
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

        // Gandanta remedies
        val gandantaPlanets = analysis.gandantaAnalysis.filter { it.isInGandanta }

        if (gandantaPlanets.isNotEmpty()) {
            item {
                Text(
                    stringResource(StringKeyFinder.LABEL_GAND_REM),
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                    color = AppTheme.TextPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(gandantaPlanets) { result ->
                if (result.remedies.isNotEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = AppTheme.CardBackground,
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.Waves,
                                    contentDescription = null,
                                    tint = AppTheme.AccentTeal,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    "${result.planet.getLocalizedName(language)} - ${result.gandantaType.name.replace("_", " ")}",
                                    fontFamily = CinzelDecorativeFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppTheme.TextPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            result.remedies.forEach { remedy ->
                                Row(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text("\u2022", color = AppTheme.AccentTeal, fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        remedy,
                                        fontFamily = PoppinsFontFamily,
                                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
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

        // Precautionary Measures
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.InfoColor.copy(alpha = 0.08f),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.InfoColor.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Shield,
                            contentDescription = null,
                            tint = AppTheme.InfoColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            stringResource(StringKeyDosha.MRITYU_BHAGA_PRECAUTIONS),
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                            color = AppTheme.TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        stringResource(StringKeyDosha.MRITYU_BHAGA_SCREEN_ABOUT_DESC),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MrityuBhagaInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(StringKeyDosha.MRITYU_BHAGA_SCREEN_ABOUT),
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
            text = {
                Text(
                    stringResource(StringKeyDosha.MRITYU_BHAGA_SCREEN_ABOUT_DESC), // Using fallback valid string
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                )
            },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    stringResource(StringKey.BTN_CLOSE),
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.AccentPrimary
                )
            }
        },
        containerColor = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    )
}