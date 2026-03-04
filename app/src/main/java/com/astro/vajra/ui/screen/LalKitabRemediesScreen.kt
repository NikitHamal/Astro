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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyDosha
import com.astro.vajra.data.localization.currentLanguage
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.LalKitabRemediesCalculator
import com.astro.vajra.ephemeris.LalKitabAnalysis
import com.astro.vajra.ephemeris.PlanetaryAffliction
import com.astro.vajra.ephemeris.AfflictionType
import com.astro.vajra.ephemeris.AfflictionSeverity
import com.astro.vajra.ephemeris.KarmicDebt
import com.astro.vajra.ephemeris.DebtType
import com.astro.vajra.ephemeris.LalKitabRemedy
import com.astro.vajra.ephemeris.RemedyCategory
import com.astro.vajra.ephemeris.ColorRemedy
import com.astro.vajra.ephemeris.DirectionRemedy
import com.astro.vajra.ephemeris.AnnualRemedyEntry
import com.astro.vajra.ui.theme.NeoVedicTypeScale
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
 * Lal Kitab Remedies Screen
 *
 * Comprehensive Lal Kitab remedial system display showing:
 * - Planetary afflictions per Lal Kitab principles
 * - Karmic debts (Rin) analysis
 * - Practical, everyday remedies
 * - Color therapy recommendations
 * - Direction guidance
 * - Weekly remedy schedule
 *
 * Based on: Original Lal Kitab texts, Pandit Roop Chand Joshi's interpretations
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LalKitabRemediesScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(
            title = stringResource(StringKeyDosha.LAL_KITAB_SCREEN_TITLE),
            message = stringResource(StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    val language = currentLanguage()
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var analysisResult by remember { mutableStateOf<LalKitabAnalysis?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val tabs = listOf(
        stringResource(StringKeyDosha.SCREEN_OVERVIEW),
        stringResource(StringKeyDosha.LAL_KITAB_SCREEN_KARMIC_DEBTS),
        stringResource(StringKeyDosha.LAL_KITAB_WEEKLY_SCHEDULE),
        stringResource(StringKeyDosha.LAL_KITAB_COLOR_THERAPY)
    )

    // Calculate analysis
    LaunchedEffect(chart, language) {
        if (chart != null) {
            isCalculating = true
            errorMessage = null
            delay(300)
            try {
                val result = withContext(Dispatchers.Default) {
                    LalKitabRemediesCalculator.analyzeLalKitab(chart, language)
                }
                analysisResult = result
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isCalculating = false
            }
        }
    }

    Scaffold(
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.LAL_KITAB_SCREEN_TITLE),
                subtitle = stringResource(StringKeyDosha.LAL_KITAB_SCREEN_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyDosha.LAL_KITAB_SCREEN_ABOUT),
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
                message = errorMessage ?: stringResource(StringKeyDosha.SCREEN_ERROR_CALCULATION)
            )
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Tab Row
                    TabSelector(
                        tabs = tabs,
                        selectedIndex = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )

                    // Content based on selected tab
                    when (selectedTab) {
                        0 -> OverviewTab(analysisResult!!, language)
                        1 -> KarmicDebtsTab(analysisResult!!, language)
                        2 -> WeeklyScheduleTab(analysisResult!!, language)
                        3 -> ColorDirectionTab(analysisResult!!, language)
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
                    stringResource(StringKeyDosha.LAL_KITAB_SCREEN_ABOUT),
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            text = {
                Column {
                    Text(
                        stringResource(StringKeyDosha.LAL_KITAB_SCREEN_ABOUT_DESC),
                        color = AppTheme.TextSecondary,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = AppTheme.InfoColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Outlined.Info,
                                contentDescription = null,
                                tint = AppTheme.InfoColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(StringKeyDosha.LAL_KITAB_NOTE_DISTINCT),
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.InfoColor,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text(
                        stringResource(StringKey.BTN_CLOSE),
                        color = AppTheme.AccentPrimary
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = AppTheme.ErrorColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(message, color = AppTheme.TextMuted)
        }
    }
}

@Composable
private fun TabSelector(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tabs.size) { index ->
            com.astro.vajra.ui.components.common.NeoVedicChoicePill(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                label = {
                    Text(
                        tabs[index],
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = if (selectedIndex == index) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.2f),
                    selectedLabelColor = AppTheme.AccentPrimary,
                    containerColor = AppTheme.CardBackground,
                    labelColor = AppTheme.TextSecondary
                )
            )
        }
    }
}

@Composable
private fun OverviewTab(analysis: LalKitabAnalysis, language: Language) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // System Note Banner
        item {
            Surface(
                modifier = Modifier.fillMaxWidth().vedicCornerMarkers(color = AppTheme.InfoColor, strokeWidth = 1.dp),
                color = AppTheme.InfoColor.copy(alpha = 0.1f),
                border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.InfoColor.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Outlined.MenuBook,
                        contentDescription = null,
                        tint = AppTheme.InfoColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        analysis.systemNote,
                        style = NeoVedicTypeScale.BodyCompact,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }

        // Planetary Afflictions Summary
        if (analysis.planetaryAfflictions.isNotEmpty()) {
            item {
                SectionHeader(
                    title = stringResource(StringKeyDosha.LAL_KITAB_SCREEN_PLANETARY_AFFLICTIONS),
                    icon = Icons.Filled.Warning,
                    tint = AppTheme.WarningColor
                )
            }

            items(analysis.planetaryAfflictions.take(5)) { affliction ->
                PlanetaryAfflictionCard(affliction, language)
            }
        }

        // Top Remedies
        if (analysis.remedies.isNotEmpty()) {
            item {
                SectionHeader(
                    title = stringResource(StringKeyDosha.LAL_KITAB_RECOMMENDED_REMEDIES),
                    icon = Icons.Filled.Healing,
                    tint = AppTheme.SuccessColor
                )
            }

            items(analysis.remedies.take(5)) { remedy ->
                RemedyCard(remedy, language)
            }
        }

        // General Recommendations
        item {
            GeneralRecommendationsCard(analysis.generalRecommendations)
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: ImageVector, tint: Color) {
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
            style = NeoVedicTypeScale.SectionTitle.copy(
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.SemiBold
            ),
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun PlanetaryAfflictionCard(affliction: PlanetaryAffliction, language: Language) {
    var expanded by remember { mutableStateOf(false) }

    val severityColor = when (affliction.severity) {
        AfflictionSeverity.SEVERE -> AppTheme.ErrorColor
        AfflictionSeverity.MODERATE -> AppTheme.WarningColor
        AfflictionSeverity.MILD -> AppTheme.AccentGold
        AfflictionSeverity.MINIMAL -> AppTheme.TextMuted
    }

    val afflictionTypeName = when (affliction.afflictionType) {
        AfflictionType.PITRU_DOSH -> stringResource(StringKeyDosha.LAL_KITAB_DEBT_PITRU)
        AfflictionType.MATRU_RIN -> stringResource(StringKeyDosha.LAL_KITAB_DEBT_MATRU)
        AfflictionType.STRI_RIN -> stringResource(StringKeyDosha.LAL_KITAB_DEBT_STRI)
        AfflictionType.KANYA_RIN -> stringResource(StringKeyDosha.LAL_KITAB_AFFLICTION_KANYA)
        AfflictionType.GRAHAN_DOSH -> stringResource(StringKeyDosha.LAL_KITAB_AFFLICTION_GRAHAN)
        AfflictionType.SHANI_PEEDA -> stringResource(StringKeyDosha.LAL_KITAB_AFFLICTION_SHANI)
        AfflictionType.NONE -> stringResource(StringKeyDosha.LAL_KITAB_AFFLICTION_GENERAL)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
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
                            .background(AppTheme.getPlanetColor(affliction.planet).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            affliction.planet.displayName.take(2),
                            style = NeoVedicTypeScale.DataValue,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.getPlanetColor(affliction.planet)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            affliction.planet.getLocalizedName(language),
                            style = NeoVedicTypeScale.CardTitle,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            "House ${affliction.house} \u2022 $afflictionTypeName",
                            style = NeoVedicTypeScale.DataLabel,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Surface(
                    color = severityColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
                ) {
                    Text(
                        affliction.severity.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = NeoVedicTypeScale.ChipLabel.copy(
                            fontFamily = SpaceGroteskFamily,
                            fontWeight = FontWeight.Medium
                        ),
                        color = severityColor
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (affliction.effects.isNotEmpty()) {
                        Text(
                            stringResource(StringKeyDosha.EFFECTS_LABEL),
                            style = NeoVedicTypeScale.DataLabel,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        affliction.effects.forEach { effect ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("\u2022", color = AppTheme.TextMuted, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    effect,
                                    style = NeoVedicTypeScale.BodyCompact,
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }

                    if (affliction.remedies.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(StringKeyDosha.LAL_KITAB_SECTION_REMEDIES),
                            style = NeoVedicTypeScale.DataLabel,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.SuccessColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        affliction.remedies.forEach { remedy ->
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
                                    remedy,
                                    style = NeoVedicTypeScale.BodyCompact,
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
private fun RemedyCard(remedy: LalKitabRemedy, language: Language) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.SuccessColor.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.SuccessColor.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Spa,
                    contentDescription = null,
                    tint = AppTheme.SuccessColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    remedy.remedy,
                    style = NeoVedicTypeScale.CardTitle,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (remedy.method.isNotEmpty()) {
                Text(
                    remedy.method,
                    style = NeoVedicTypeScale.BodyCompact,
                    color = AppTheme.TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (remedy.frequency.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = AppTheme.TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            remedy.frequency,
                            style = NeoVedicTypeScale.ChipLabel.copy(fontFamily = SpaceGroteskFamily),
                            color = AppTheme.TextMuted
                        )
                    }
                }
                if (remedy.effectiveness.isNotEmpty()) {
                    Surface(
                        color = AppTheme.SuccessColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
                    ) {
                        Text(
                            remedy.effectiveness,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = NeoVedicTypeScale.ChipLabel.copy(fontFamily = SpaceGroteskFamily),
                            color = AppTheme.SuccessColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GeneralRecommendationsCard(recommendations: List<String>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
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
                    stringResource(StringKeyDosha.LAL_KITAB_GENERAL_PRINCIPLES),
                    style = NeoVedicTypeScale.SectionTitle.copy(
                        fontFamily = CinzelDecorativeFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            recommendations.forEach { rec ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("\u2022", color = AppTheme.AccentGold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        rec,
                        style = NeoVedicTypeScale.BodyCompact,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun KarmicDebtsTab(analysis: LalKitabAnalysis, language: Language) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Introduction Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth().vedicCornerMarkers(color = AppTheme.AccentPrimary, strokeWidth = 1.dp),
                color = AppTheme.AccentPrimary.copy(alpha = 0.08f),
                border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.AccentPrimary.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        stringResource(StringKeyDosha.LAL_KITAB_RIN_TITLE),
                        style = NeoVedicTypeScale.SectionTitle.copy(
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        stringResource(StringKeyDosha.LAL_KITAB_RIN_DESC),
                        style = NeoVedicTypeScale.BodyCompact,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }

        // Karmic Debts List
        if (analysis.karmicDebts.isNotEmpty()) {
            items(analysis.karmicDebts) { debt ->
                KarmicDebtCard(debt, language)
            }
        } else {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = AppTheme.SuccessColor.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.SuccessColor.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
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
                                stringResource(StringKeyDosha.LAL_KITAB_RIN_NONE_TITLE),
                                style = NeoVedicTypeScale.CardTitle,
                                fontWeight = FontWeight.SemiBold,
                                color = AppTheme.TextPrimary
                            )
                            Text(
                                stringResource(StringKeyDosha.LAL_KITAB_RIN_NONE_DESC),
                                style = NeoVedicTypeScale.BodyCompact,
                                color = AppTheme.TextMuted
                            )
                        }
                    }
                }
            }
        }

        // Debt Types Reference
        item {
            DebtTypesReferenceCard()
        }
    }
}

@Composable
private fun KarmicDebtCard(debt: KarmicDebt, language: Language) {
    var expanded by remember { mutableStateOf(false) }

    val (icon, color, title) = when (debt.type) {
        DebtType.PITRU_RIN -> Triple(Icons.Filled.Elderly, AppTheme.AccentGold, stringResource(StringKeyDosha.LAL_KITAB_DEBT_PITRU))
        DebtType.MATRU_RIN -> Triple(Icons.Filled.Face, AppTheme.AccentTeal, stringResource(StringKeyDosha.LAL_KITAB_DEBT_MATRU))
        DebtType.STRI_RIN -> Triple(Icons.Filled.Favorite, AppTheme.ErrorColor.copy(alpha = 0.8f), stringResource(StringKeyDosha.LAL_KITAB_DEBT_STRI))
        DebtType.KANYA_RIN -> Triple(Icons.Filled.ChildCare, AppTheme.AccentPrimary, stringResource(StringKeyDosha.LAL_KITAB_DEBT_KANYA))
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
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
                            .background(color.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            title,
                            style = NeoVedicTypeScale.CardTitle,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            debt.description,
                            style = NeoVedicTypeScale.BodyCompact,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Icon(
                    if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (debt.indicators.isNotEmpty()) {
                        Text(
                            stringResource(StringKeyDosha.LAL_KITAB_INDICATORS),
                            style = NeoVedicTypeScale.DataLabel,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        debt.indicators.forEach { indicator ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    Icons.Outlined.Info,
                                    contentDescription = null,
                                    tint = AppTheme.InfoColor,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    indicator,
                                    style = NeoVedicTypeScale.BodyCompact,
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }

                    if (debt.effects.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(StringKeyDosha.EFFECTS_LABEL),
                            style = NeoVedicTypeScale.DataLabel,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.WarningColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        debt.effects.forEach { effect ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("\u2022", color = AppTheme.WarningColor, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    effect,
                                    style = NeoVedicTypeScale.BodyCompact,
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }

                    if (debt.remedies.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(StringKeyDosha.LAL_KITAB_REMEDIES_LABEL),
                            style = NeoVedicTypeScale.DataLabel,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.SuccessColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        debt.remedies.forEach { remedy ->
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
                                    remedy,
                                    style = NeoVedicTypeScale.BodyCompact,
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
private fun DebtTypesReferenceCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                stringResource(StringKeyDosha.LAL_KITAB_TYPES_TITLE),
                style = NeoVedicTypeScale.SectionTitle.copy(
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.Bold
                ),
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            val debtTypes = listOf(
                Triple(stringResource(StringKeyDosha.LAL_KITAB_DEBT_PITRU), stringResource(StringKeyDosha.LAL_KITAB_DESC_PITRU), AppTheme.AccentGold),
                Triple(stringResource(StringKeyDosha.LAL_KITAB_DEBT_MATRU), stringResource(StringKeyDosha.LAL_KITAB_DESC_MATRU), AppTheme.AccentTeal),
                Triple(stringResource(StringKeyDosha.LAL_KITAB_DEBT_STRI), stringResource(StringKeyDosha.LAL_KITAB_DESC_STRI), AppTheme.ErrorColor.copy(alpha = 0.8f)),
                Triple(stringResource(StringKeyDosha.LAL_KITAB_DEBT_SELF), stringResource(StringKeyDosha.LAL_KITAB_DESC_SELF), AppTheme.AccentPrimary)
            )

            debtTypes.forEach { (name, desc, color) ->
                Row(
                    modifier = Modifier.padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            name,
                            style = NeoVedicTypeScale.BodyCompact,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            desc,
                            style = NeoVedicTypeScale.ChipLabel.copy(fontFamily = SpaceGroteskFamily),
                            color = AppTheme.TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeeklyScheduleTab(analysis: LalKitabAnalysis, language: Language) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                stringResource(StringKeyDosha.LAL_KITAB_WEEKLY_SCHEDULE),
                style = NeoVedicTypeScale.SectionTitle.copy(
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.Bold
                ),
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                stringResource(StringKeyDosha.LAL_KITAB_DAILY_REMEDIES_DESC),
                style = NeoVedicTypeScale.BodyCompact,
                color = AppTheme.TextMuted,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(analysis.annualCalendar) { entry ->
            WeeklyRemedyCard(entry, language)
        }
    }
}

@Composable
private fun WeeklyRemedyCard(entry: AnnualRemedyEntry, language: Language) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AppTheme.getPlanetColor(entry.planet).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    entry.day.take(3),
                    style = NeoVedicTypeScale.DataValue,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.getPlanetColor(entry.planet)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        entry.day,
                        style = NeoVedicTypeScale.CardTitle,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Surface(
                        color = AppTheme.getPlanetColor(entry.planet).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
                    ) {
                        Text(
                            entry.planet.getLocalizedName(language),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = NeoVedicTypeScale.ChipLabel.copy(
                                fontFamily = SpaceGroteskFamily,
                                fontWeight = FontWeight.Medium
                            ),
                            color = AppTheme.getPlanetColor(entry.planet)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                entry.remedies.forEach { remedy ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Filled.Circle,
                            contentDescription = null,
                            tint = AppTheme.getPlanetColor(entry.planet),
                            modifier = Modifier.size(6.dp).padding(top = 6.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    remedy,
                                    style = NeoVedicTypeScale.BodyCompact,
                                    color = AppTheme.TextSecondary
                                )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorDirectionTab(analysis: LalKitabAnalysis, language: Language) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Color Remedies Section
        item {
            SectionHeader(
                title = stringResource(StringKeyDosha.LAL_KITAB_COLOR_THERAPY),
                icon = Icons.Filled.Palette,
                tint = AppTheme.AccentPrimary
            )
        }

        items(analysis.colorRemedies) { colorRemedy ->
            ColorRemedyCard(colorRemedy, language)
        }

        // Direction Remedies Section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(
                title = stringResource(StringKeyDosha.LAL_KITAB_DIRECTION_GUIDANCE),
                icon = Icons.Filled.Explore,
                tint = AppTheme.AccentTeal
            )
        }

        items(analysis.directionRemedies) { dirRemedy ->
            DirectionRemedyCard(dirRemedy, language)
        }
    }
}

@Composable
private fun ColorRemedyCard(remedy: ColorRemedy, language: Language) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AppTheme.getPlanetColor(remedy.planet).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Palette,
                        contentDescription = null,
                        tint = AppTheme.getPlanetColor(remedy.planet),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    remedy.planet.getLocalizedName(language),
                    style = NeoVedicTypeScale.CardTitle,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        stringResource(StringKeyDosha.LAL_KITAB_FAVORABLE),
                        style = NeoVedicTypeScale.ChipLabel.copy(
                            fontFamily = SpaceGroteskFamily,
                            fontWeight = FontWeight.Medium
                        ),
                        color = AppTheme.SuccessColor
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(remedy.favorableColors) { color ->
                            Surface(
                                color = AppTheme.SuccessColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
                            ) {
                                Text(
                                    color,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = NeoVedicTypeScale.ChipLabel.copy(fontFamily = SpaceGroteskFamily),
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        stringResource(StringKeyDosha.LAL_KITAB_AVOID),
                        style = NeoVedicTypeScale.ChipLabel.copy(
                            fontFamily = SpaceGroteskFamily,
                            fontWeight = FontWeight.Medium
                        ),
                        color = AppTheme.ErrorColor
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(remedy.avoidColors) { color ->
                            Surface(
                                color = AppTheme.ErrorColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
                            ) {
                                Text(
                                    color,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = NeoVedicTypeScale.ChipLabel.copy(fontFamily = SpaceGroteskFamily),
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            if (remedy.application.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    remedy.application,
                    style = NeoVedicTypeScale.BodyCompact.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun DirectionRemedyCard(remedy: DirectionRemedy, language: Language) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AppTheme.AccentTeal.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Explore,
                        contentDescription = null,
                        tint = AppTheme.AccentTeal,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    remedy.planet.getLocalizedName(language),
                    style = NeoVedicTypeScale.CardTitle,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        remedy.favorableDirection,
                        style = NeoVedicTypeScale.BodyCompact,
                        color = AppTheme.SuccessColor,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = null,
                        tint = AppTheme.ErrorColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        remedy.avoidDirection,
                        style = NeoVedicTypeScale.BodyCompact,
                        color = AppTheme.ErrorColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (remedy.application.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    remedy.application,
                    style = NeoVedicTypeScale.BodyCompact,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}







