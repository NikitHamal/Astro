package com.astro.storm.ui.screen

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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAdvanced
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringKeyUICommon
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.model.VedicChart
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.ephemeris.shoola.DashaDirection
import com.astro.storm.ephemeris.shoola.HealthVulnerabilityPeriod
import com.astro.storm.ephemeris.shoola.LongevityAssessment
import com.astro.storm.ephemeris.shoola.LongevityCategory
import com.astro.storm.ephemeris.shoola.PeriodNature
import com.astro.storm.ephemeris.shoola.RemedyType
import com.astro.storm.ephemeris.shoola.ShoolaDashaResult
import com.astro.storm.ephemeris.shoola.ShoolaDashaPeriod
import com.astro.storm.ephemeris.shoola.ShoolaRemedy
import com.astro.storm.ephemeris.shoola.TriMurtiAnalysis
import com.astro.storm.ephemeris.shoola.getSeverityColor
import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.NeoVedicEmptyState
import com.astro.storm.ui.components.common.NeoVedicPageHeader
import com.astro.storm.ui.components.common.TabItem
import com.astro.storm.ui.components.common.vedicCornerMarkers
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.CinzelDecorativeFamily
import com.astro.storm.ui.theme.NeoVedicFontSizes
import com.astro.storm.ui.theme.NeoVedicTokens
import com.astro.storm.ui.theme.PoppinsFontFamily
import com.astro.storm.ui.theme.SpaceGroteskFamily
import com.astro.storm.ui.viewmodel.ShoolaDashaUiState
import com.astro.storm.ui.viewmodel.ShoolaDashaViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun shoolaLocale(language: Language): Locale =
    if (language == Language.NEPALI) Locale.forLanguageTag("ne-NP") else Locale.ENGLISH

private fun shoolaMonthYearFormatter(language: Language): DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMM yyyy", shoolaLocale(language))

/**
 * Shoola Dasha Screen
 *
 * Displays comprehensive Shoola Dasha analysis including:
 * - Tri-Murti analysis (Brahma, Rudra, Maheshwara)
 * - Current and future dasha periods with health implications
 * - Longevity assessment based on Jaimini principles
 * - Health vulnerability periods and remedies
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoolaDashaScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    viewModel: ShoolaDashaViewModel = hiltViewModel()
) {
    val language = LocalLanguage.current
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(StringKeyAdvanced.SHOOLA_TAB_OVERVIEW),
        stringResource(StringKeyAdvanced.SHOOLA_TAB_PERIODS),
        stringResource(StringKeyAdvanced.SHOOLA_TAB_HEALTH),
        stringResource(StringKeyAdvanced.SHOOLA_TAB_REMEDIES)
    )

    LaunchedEffect(chart) {
        viewModel.loadShoolaDasha(chart, language)
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            ShoolaDashaTopBar(
                chartName = chart?.birthData?.name ?: stringResource(StringKeyMatch.MISC_UNKNOWN),
                result = (uiState as? ShoolaDashaUiState.Success)?.result,
                isCalculating = uiState is ShoolaDashaUiState.Loading,
                onBack = onBack,
                onInfoClick = { showInfoDialog = true }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is ShoolaDashaUiState.Loading -> ShoolaLoadingContent(modifier = Modifier.padding(paddingValues))
            is ShoolaDashaUiState.Error -> ShoolaErrorContent(
                message = state.message,
                modifier = Modifier.padding(paddingValues)
            )
            is ShoolaDashaUiState.Idle -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    NeoVedicEmptyState(
                        title = stringResource(StringKeyAdvanced.SHOOLA_TITLE),
                        subtitle = stringResource(StringKey.NO_PROFILE_MESSAGE),
                        icon = Icons.Outlined.HealthAndSafety
                    )
                }
            }
            is ShoolaDashaUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        ShoolaTabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                    when (selectedTab) {
                        0 -> item { ShoolaOverviewSection(state.result, language) }
                        1 -> item { ShoolaPeriodsSection(state.result, language) }
                        2 -> item { ShoolaHealthSection(state.result, language) }
                        3 -> item { ShoolaRemediesSection(state.result, language) }
                    }
                }
            }
        }
    }

    if (showInfoDialog) {
        ShoolaInfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun ShoolaDashaTopBar(
    chartName: String,
    result: ShoolaDashaResult?,
    isCalculating: Boolean,
    onBack: () -> Unit,
    onInfoClick: () -> Unit
) {
    val language = LocalLanguage.current
    val subtitle = when {
        isCalculating -> stringResource(StringKey.DASHA_CALCULATING)
        result != null -> buildString {
            result.currentMahadasha?.let { md ->
                append(md.sign.getLocalizedName(language))
                append(" | ")
            }
            append(chartName)
        }
        else -> chartName
    }
    NeoVedicPageHeader(
        title = stringResource(StringKeyAdvanced.SHOOLA_TITLE),
        subtitle = subtitle,
        onBack = onBack,
        actionIcon = Icons.Outlined.Info,
        onAction = onInfoClick
    )
}

@Composable
private fun ShoolaTabSelector(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    ModernPillTabRow(
        tabs = tabs.mapIndexed { index, title ->
            TabItem(
                title = title,
                accentColor = if (selectedTab == index) AppTheme.WarningColor else Color.Unspecified
            )
        },
        selectedIndex = selectedTab,
        onTabSelected = onTabSelected,
        modifier = Modifier.padding(
            horizontal = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding,
            vertical = com.astro.storm.ui.theme.NeoVedicTokens.SpaceXS
        )
    )
}

@Composable
private fun ShoolaOverviewSection(
    result: ShoolaDashaResult,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(com.astro.storm.ui.theme.NeoVedicTokens.SpaceMD)
    ) {
        // Longevity Status Card
        LongevityStatusCard(result.longevityAssessment, language)

        // Tri-Murti Analysis
        TriMurtiCard(result.triMurti, language)

        // Current Period Card
        result.currentMahadasha?.let { current ->
            CurrentPeriodCard(current, language)
        }

        // Quick Stats
        ShoolaQuickStats(result)
    }
}

@Composable
private fun LongevityStatusCard(
    longevity: LongevityAssessment,
    language: Language
) {
    val statusColor = when (longevity.category) {
        LongevityCategory.POORNAYU,
        LongevityCategory.AMITAYU -> AppTheme.SuccessColor
        LongevityCategory.MADHYAYU -> AppTheme.AccentGold
        LongevityCategory.ALPAYU -> AppTheme.WarningColor
        LongevityCategory.BALARISHTA -> AppTheme.ErrorColor
    }

    val displayName = if (language == Language.NEPALI)
        longevity.category.displayNameNe else longevity.category.displayName

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .vedicCornerMarkers(color = statusColor),
        color = statusColor.copy(alpha = 0.05f),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, statusColor.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(NeoVedicTokens.ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(statusColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(NeoVedicTokens.SpaceMD))
            Text(
                text = stringResource(StringKeyAdvanced.SHOOLA_LONGEVITY_CATEGORY),
                fontFamily = SpaceGroteskFamily,
                fontSize = NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = displayName,
                fontFamily = CinzelDecorativeFamily,
                fontSize = NeoVedicFontSizes.S20,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(StringKeyAdvanced.SHOOLA_LONGEVITY_RANGE) + ": " + longevity.category.yearsRange,
                fontFamily = SpaceGroteskFamily,
                fontSize = NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (language == Language.NEPALI) longevity.interpretationNe else longevity.interpretation,
                fontFamily = PoppinsFontFamily,
                fontSize = NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun TriMurtiCard(
    triMurti: TriMurtiAnalysis,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(AppTheme.AccentGold.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Psychology,
                        contentDescription = null,
                        tint = AppTheme.AccentGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = stringResource(StringKeyAdvanced.SHOOLA_TRI_MURTI),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = NeoVedicFontSizes.S16,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Brahma
            triMurti.brahma?.let { brahma ->
                TriMurtiRow(
                    title = stringResource(StringKeyAdvanced.SHOOLA_BRAHMA),
                    subtitle = stringResource(StringKeyAdvanced.SHOOLA_BRAHMA_DESC),
                    planet = brahma.displayName,
                    sign = triMurti.brahmaSign?.displayName,
                    strength = triMurti.brahmaStrength,
                    color = AppTheme.SuccessColor
                )
            }

            HorizontalDivider(
                color = AppTheme.DividerColor,
                modifier = Modifier.padding(vertical = 14.dp)
            )

            // Rudra
            TriMurtiRow(
                title = stringResource(StringKeyAdvanced.SHOOLA_RUDRA),
                subtitle = stringResource(StringKeyAdvanced.SHOOLA_RUDRA_DESC),
                planet = triMurti.rudra.displayName,
                sign = triMurti.rudraSign?.displayName,
                strength = triMurti.rudraStrength,
                color = AppTheme.ErrorColor
            )

            HorizontalDivider(
                color = AppTheme.DividerColor,
                modifier = Modifier.padding(vertical = 14.dp)
            )

            // Maheshwara
            triMurti.maheshwara?.let { maheshwara ->
                TriMurtiRow(
                    title = stringResource(StringKeyAdvanced.SHOOLA_MAHESHWARA),
                    subtitle = stringResource(StringKeyAdvanced.SHOOLA_MAHESHWARA_DESC),
                    planet = maheshwara.displayName,
                    sign = triMurti.maheshwaraSign?.displayName,
                    strength = null,
                    color = AppTheme.AccentPrimary
                )
            }
        }
    }
}

@Composable
private fun TriMurtiRow(
    title: String,
    subtitle: String,
    planet: String,
    sign: String?,
    strength: Double?,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = SpaceGroteskFamily,
                fontSize = NeoVedicFontSizes.S14,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = subtitle,
                fontFamily = PoppinsFontFamily,
                fontSize = NeoVedicFontSizes.S11,
                color = AppTheme.TextMuted
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = planet,
                fontFamily = SpaceGroteskFamily,
                fontSize = NeoVedicFontSizes.S14,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )
            sign?.let {
                Text(
                    text = it,
                    fontFamily = PoppinsFontFamily,
                    fontSize = NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary
                )
            }
            strength?.let {
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { it.toFloat() },
                    modifier = Modifier
                        .width(60.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)),
                    color = color,
                    trackColor = color.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
private fun CurrentPeriodCard(
    period: ShoolaDashaPeriod,
    language: Language
) {
    val natureColor = when (period.nature) {
        PeriodNature.FAVORABLE -> AppTheme.SuccessColor
        PeriodNature.SUPPORTIVE -> AppTheme.AccentTeal
        PeriodNature.MIXED -> AppTheme.AccentGold
        PeriodNature.CHALLENGING -> AppTheme.WarningColor
        PeriodNature.VERY_CHALLENGING -> AppTheme.ErrorColor
    }

    val dateFormatter = remember(language) { shoolaMonthYearFormatter(language) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = natureColor.copy(alpha = 0.05f),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, natureColor.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(StringKeyAdvanced.SHOOLA_CURRENT_PERIOD),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${period.sign.displayName} ${stringResource(StringKeyAdvanced.SHOOLA_MAHADASHA)}",
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = NeoVedicFontSizes.S18,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
                    color = natureColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = (if (language == Language.NEPALI) period.nature.displayNameNe else period.nature.displayName).uppercase(),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S10,
                        fontWeight = FontWeight.Bold,
                        color = natureColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress bar
            Column {
                LinearProgressIndicator(
                    progress = { period.progress.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)),
                    color = natureColor,
                    trackColor = natureColor.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = period.startDate.format(dateFormatter),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = "${(period.progress * 100).toInt()}%",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S11,
                        fontWeight = FontWeight.Bold,
                        color = natureColor
                    )
                    Text(
                        text = period.endDate.format(dateFormatter),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Period details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyAdvanced.SHOOLA_SIGN_LORD),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = period.signLord.displayName,
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyAdvanced.SHOOLA_HEALTH_SEVERITY),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (language == Language.NEPALI) period.healthSeverity.displayNameNe else period.healthSeverity.displayName,
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Bold,
                        color = getSeverityColor(period.healthSeverity)
                    )
                }
            }
        }
    }
}

@Composable
private fun ShoolaQuickStats(result: ShoolaDashaResult) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ShoolaStatCard(
            title = stringResource(StringKeyAdvanced.SHOOLA_DIRECTION),
            value = if (result.dashaDirection == DashaDirection.DIRECT) "\u21BB" else "\u21BA",
            color = AppTheme.AccentPrimary,
            modifier = Modifier.weight(1f)
        )
        ShoolaStatCard(
            title = stringResource(StringKeyAdvanced.SHOOLA_STARTING_SIGN),
            value = result.startingSign.displayName.take(3),
            color = AppTheme.AccentGold,
            modifier = Modifier.weight(1f)
        )
        ShoolaStatCard(
            title = stringResource(StringKeyAdvanced.SHOOLA_UPCOMING_CRITICAL),
            value = "${result.upcomingCriticalPeriods.size}",
            color = AppTheme.WarningColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ShoolaStatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun ShoolaPeriodsSection(
    result: ShoolaDashaResult,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(StringKeyAdvanced.SHOOLA_ALL_PERIODS),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary
        )

        result.mahadashas.forEach { period ->
            ShoolaPeriodCard(period = period, language = language)
        }
    }
}

@Composable
private fun ShoolaPeriodCard(
    period: ShoolaDashaPeriod,
    language: Language
) {
    var expanded by remember { mutableStateOf(period.isCurrent) }
    val dateFormatter = remember(language) { shoolaMonthYearFormatter(language) }

    val natureColor = when (period.nature) {
        PeriodNature.FAVORABLE -> AppTheme.SuccessColor
        PeriodNature.SUPPORTIVE -> AppTheme.AccentTeal
        PeriodNature.MIXED -> AppTheme.AccentGold
        PeriodNature.CHALLENGING -> AppTheme.WarningColor
        PeriodNature.VERY_CHALLENGING -> AppTheme.ErrorColor
    }

    val statusLabel = when {
        period.isCurrent -> stringResource(StringKeyAdvanced.SHOOLA_CURRENT)
        period.progress >= 1.0 -> stringResource(StringKeyAdvanced.SHOOLA_PAST)
        else -> stringResource(StringKeyAdvanced.SHOOLA_FUTURE)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(),
        color = if (period.isCurrent) natureColor.copy(alpha = 0.05f) else AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = if (period.isCurrent) androidx.compose.foundation.BorderStroke(1.dp, natureColor.copy(alpha = 0.3f)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(natureColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = period.sign.displayName.take(2),
                            fontSize = NeoVedicFontSizes.S17,
                            fontWeight = FontWeight.Bold,
                            color = natureColor,
                            fontFamily = SpaceGroteskFamily
                        )
                    }
                    Column {
                        Text(
                            text = "${period.sign.displayName} Dasha",
                            fontSize = NeoVedicFontSizes.S16,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary,
                            fontFamily = CinzelDecorativeFamily
                        )
                        Text(
                            text = "${period.startDate.format(dateFormatter)} - ${period.endDate.format(dateFormatter)}",
                            fontSize = NeoVedicFontSizes.S12,
                            color = AppTheme.TextMuted,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
                        color = if (period.isCurrent) natureColor.copy(alpha = 0.2f)
                        else AppTheme.ChipBackground
                    ) {
                        Text(
                            text = statusLabel.uppercase(),
                            fontSize = NeoVedicFontSizes.S10,
                            fontWeight = FontWeight.Bold,
                            color = if (period.isCurrent) natureColor else AppTheme.TextMuted,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontFamily = SpaceGroteskFamily
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
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Nature and Health Severity
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(StringKeyAdvanced.SHOOLA_PERIOD_NATURE),
                                fontSize = NeoVedicFontSizes.S11,
                                color = AppTheme.TextMuted,
                                fontFamily = SpaceGroteskFamily
                            )
                            Surface(
                                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                                color = natureColor.copy(alpha = 0.15f),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = if (language == Language.NEPALI) period.nature.displayNameNe
                                    else period.nature.displayName,
                                    fontSize = NeoVedicFontSizes.S12,
                                    color = natureColor,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = PoppinsFontFamily
                                )
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(StringKeyAdvanced.SHOOLA_HEALTH_SEVERITY),
                                fontSize = NeoVedicFontSizes.S11,
                                color = AppTheme.TextMuted,
                                fontFamily = SpaceGroteskFamily
                            )
                            Surface(
                                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                                color = getSeverityColor(period.healthSeverity).copy(alpha = 0.15f),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = if (language == Language.NEPALI) period.healthSeverity.displayNameNe
                                    else period.healthSeverity.displayName,
                                    fontSize = NeoVedicFontSizes.S12,
                                    color = getSeverityColor(period.healthSeverity),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = PoppinsFontFamily
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Interpretation
                    Text(
                        text = if (language == Language.NEPALI) period.interpretationNe else period.interpretation,
                        fontSize = NeoVedicFontSizes.S13,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp,
                        fontFamily = PoppinsFontFamily
                    )

                    // Significant planets
                    if (period.significantPlanets.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(StringKeyAdvanced.SHOOLA_SIGNIFICANT_PLANETS),
                            fontSize = NeoVedicFontSizes.S11,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextMuted,
                            fontFamily = SpaceGroteskFamily
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            items(period.significantPlanets) { planet ->
                                Surface(
                                    shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
                                    color = AppTheme.ChipBackground
                                ) {
                                    Text(
                                        text = planet.displayName,
                                        fontSize = NeoVedicFontSizes.S11,
                                        color = AppTheme.TextSecondary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontFamily = PoppinsFontFamily
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
private fun ShoolaHealthSection(
    result: ShoolaDashaResult,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Current Health Status
        result.currentMahadasha?.let { current ->
            if (current.healthSeverity.level >= 2) {
                HealthConcernsCard(period = current, language = language)
            }
        }

        // Upcoming Critical Periods
        if (result.upcomingCriticalPeriods.isNotEmpty()) {
            Text(
                text = stringResource(StringKeyAdvanced.SHOOLA_UPCOMING_CRITICAL),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.WarningColor
            )
            result.upcomingCriticalPeriods.take(5).forEach { vulnerability ->
                VulnerabilityCard(vulnerability = vulnerability, language = language)
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.SuccessColor.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
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
                        text = stringResource(StringKeyAdvanced.SHOOLA_NO_CRITICAL),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Body Parts Overview
        LongevityFactorsCard(longevity = result.longevityAssessment, language = language)
    }
}

@Composable
private fun HealthConcernsCard(
    period: ShoolaDashaPeriod,
    language: Language
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.HealthAndSafety,
                    contentDescription = null,
                    tint = getSeverityColor(period.healthSeverity),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyAdvanced.SHOOLA_HEALTH_CONCERNS),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            val concerns = if (language == Language.NEPALI) period.healthConcernsNe else period.healthConcerns
            concerns.forEach { concern ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("\u2022", color = getSeverityColor(period.healthSeverity))
                    Text(
                        text = concern,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }

            if (period.precautions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyAdvanced.SHOOLA_PRECAUTIONS),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.AccentTeal
                )
                val precautions = if (language == Language.NEPALI) period.precautionsNe else period.precautions
                precautions.forEach { precaution ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("\u2713", color = AppTheme.AccentTeal)
                        Text(
                            text = precaution,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VulnerabilityCard(
    vulnerability: HealthVulnerabilityPeriod,
    language: Language
) {
    val dateFormatter = remember(language) { shoolaMonthYearFormatter(language) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = getSeverityColor(vulnerability.severity).copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${vulnerability.dashaSign.displayName} Period",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = "${vulnerability.startDate.format(dateFormatter)} - ${vulnerability.endDate.format(dateFormatter)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
                Surface(
                    shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = getSeverityColor(vulnerability.severity).copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (vulnerability.severity.level >= 4) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = getSeverityColor(vulnerability.severity),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Text(
                            text = if (language == Language.NEPALI) vulnerability.severity.displayNameNe
                            else vulnerability.severity.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = getSeverityColor(vulnerability.severity)
                        )
                    }
                }
            }

            val bodyParts = if (language == Language.NEPALI) vulnerability.bodyPartsNe else vulnerability.bodyParts
            if (bodyParts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(StringKeyAdvanced.SHOOLA_BODY_PARTS) + ": " + bodyParts.joinToString(", "),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun LongevityFactorsCard(
    longevity: LongevityAssessment,
    language: Language
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyAdvanced.SHOOLA_LONGEVITY),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))

            val supporting = if (language == Language.NEPALI) longevity.supportingFactorsNe else longevity.supportingFactors
            if (supporting.isNotEmpty()) {
                Text(
                    text = stringResource(StringKeyAdvanced.SHOOLA_SUPPORTING_FACTORS),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.SuccessColor
                )
                supporting.forEach { factor ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("\u2713", color = AppTheme.SuccessColor, fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12)
                        Text(
                            text = factor,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }

            val challenging = if (language == Language.NEPALI) longevity.challengingFactorsNe else longevity.challengingFactors
            if (challenging.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(StringKeyAdvanced.SHOOLA_CHALLENGING_FACTORS),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.WarningColor
                )
                challenging.forEach { factor ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("\u26A0", color = AppTheme.WarningColor, fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12)
                        Text(
                            text = factor,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShoolaRemediesSection(
    result: ShoolaDashaResult,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (result.remedies.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
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
                            text = stringResource(StringKeyDosha.KEMA_NO_REMEDIES),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextMuted,
                            textAlign = TextAlign.Center
                        )
                }
            }
        } else {
            result.remedies.forEach { remedy ->
                ShoolaRemedyCard(remedy = remedy, language = language)
            }
        }
    }
}

@Composable
private fun ShoolaRemedyCard(
    remedy: ShoolaRemedy,
    language: Language
) {
    val typeColor = when (remedy.remedyType) {
        RemedyType.MANTRA -> AppTheme.AccentGold
        RemedyType.PUJA -> AppTheme.LifeAreaSpiritual
        RemedyType.DONATION -> AppTheme.SuccessColor
        RemedyType.FASTING -> AppTheme.AccentTeal
        RemedyType.GEMSTONE -> AppTheme.AccentPrimary
        RemedyType.YANTRA -> AppTheme.WarningColor
        RemedyType.LIFESTYLE -> AppTheme.LifeAreaHealth
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
                    color = typeColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (language == Language.NEPALI) remedy.remedyType.displayNameNe
                        else remedy.remedyType.displayName,
                        fontSize = NeoVedicFontSizes.S10,
                        color = typeColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGroteskFamily
                    )
                }
                remedy.targetPlanet?.let { planet ->
                    Text(
                        text = planet.displayName,
                        fontSize = NeoVedicFontSizes.S13,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary,
                        fontFamily = PoppinsFontFamily
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${remedy.effectiveness}%",
                    fontSize = NeoVedicFontSizes.S10,
                    fontWeight = FontWeight.SemiBold,
                    color = typeColor,
                    fontFamily = SpaceGroteskFamily
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (language == Language.NEPALI) remedy.descriptionNe else remedy.description,
                fontSize = NeoVedicFontSizes.S13,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary,
                fontFamily = PoppinsFontFamily
            )

            remedy.mantra?.let { mantra ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = mantra,
                    fontSize = NeoVedicFontSizes.S11,
                    color = AppTheme.AccentGold,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFontFamily
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == Language.NEPALI) remedy.instructionNe else remedy.instruction,
                fontSize = NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp,
                fontFamily = PoppinsFontFamily
            )

            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceMD)
            ) {
                remedy.deity?.let { deity ->
                    Column {
                        Text(
                            text = stringResource(StringKeyAdvanced.SHOOLA_DEITY),
                            fontSize = NeoVedicFontSizes.S10,
                            color = AppTheme.TextMuted,
                            fontFamily = SpaceGroteskFamily
                        )
                        Text(
                            text = if (language == Language.NEPALI) remedy.deityNe ?: deity else deity,
                            fontSize = NeoVedicFontSizes.S11,
                            color = AppTheme.TextSecondary,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                }
                remedy.bestDay?.let { day ->
                    Column {
                        Text(
                            text = stringResource(StringKeyAdvanced.SHOOLA_BEST_DAY),
                            fontSize = NeoVedicFontSizes.S10,
                            color = AppTheme.TextMuted,
                            fontFamily = SpaceGroteskFamily
                        )
                        Text(
                            text = if (language == Language.NEPALI) remedy.bestDayNe ?: day else day,
                            fontSize = NeoVedicFontSizes.S11,
                            color = AppTheme.TextSecondary,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                }
            }
        }
    }
}

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
                    color = typeColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (language == Language.NEPALI) remedy.remedyType.displayNameNe
                        else remedy.remedyType.displayName,
                        fontSize = NeoVedicFontSizes.S10,
                        color = typeColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGroteskFamily
                    )
                }
                remedy.targetPlanet?.let { planet ->
                    Text(
                        text = planet.displayName,
                        fontSize = NeoVedicFontSizes.S13,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary,
                        fontFamily = PoppinsFontFamily
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${remedy.effectiveness}%",
                    fontSize = NeoVedicFontSizes.S10,
                    fontWeight = FontWeight.SemiBold,
                    color = typeColor,
                    fontFamily = SpaceGroteskFamily
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (language == Language.NEPALI) remedy.descriptionNe else remedy.description,
                fontSize = NeoVedicFontSizes.S13,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary,
                fontFamily = PoppinsFontFamily
            )

            remedy.mantra?.let { mantra ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = mantra,
                    fontSize = NeoVedicFontSizes.S11,
                    color = AppTheme.AccentGold,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFontFamily
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == Language.NEPALI) remedy.instructionNe else remedy.instruction,
                fontSize = NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp,
                fontFamily = PoppinsFontFamily
            )

            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceMD)
            ) {
                remedy.deity?.let { deity ->
                    Column {
                        Text(
                            text = stringResource(StringKeyAdvanced.SHOOLA_DEITY),
                            fontSize = NeoVedicFontSizes.S10,
                            color = AppTheme.TextMuted,
                            fontFamily = SpaceGroteskFamily
                        )
                        Text(
                            text = if (language == Language.NEPALI) remedy.deityNe ?: deity else deity,
                            fontSize = NeoVedicFontSizes.S11,
                            color = AppTheme.TextSecondary,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                }
                remedy.bestDay?.let { day ->
                    Column {
                        Text(
                            text = stringResource(StringKeyAdvanced.SHOOLA_BEST_DAY),
                            fontSize = NeoVedicFontSizes.S10,
                            color = AppTheme.TextMuted,
                            fontFamily = SpaceGroteskFamily
                        )
                        Text(
                            text = if (language == Language.NEPALI) remedy.bestDayNe ?: day else day,
                            fontSize = NeoVedicFontSizes.S11,
                            color = AppTheme.TextSecondary,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                }
            }
        }
    }
}

                remedy.targetPlanet?.let { planet ->
                    Text(
                        text = planet.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${remedy.effectiveness}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = typeColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == Language.NEPALI) remedy.descriptionNe else remedy.description,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )

            remedy.mantra?.let { mantra ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mantra,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.AccentGold,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(com.astro.storm.ui.theme.NeoVedicTokens.SpaceMD)
            ) {
                remedy.deity?.let { deity ->
                    Column {
                        Text(
                            text = stringResource(StringKeyAdvanced.SHOOLA_DEITY),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                        Text(
                            text = if (language == Language.NEPALI) remedy.deityNe ?: deity else deity,
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
                remedy.bestDay?.let { day ->
                    Column {
                        Text(
                            text = stringResource(StringKeyAdvanced.SHOOLA_BEST_DAY),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                        Text(
                            text = if (language == Language.NEPALI) remedy.bestDayNe ?: day else day,
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShoolaLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.WarningColor)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyAdvanced.COMMON_LOADING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun ShoolaErrorContent(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = AppTheme.ErrorColor,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyAdvanced.COMMON_ERROR),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ShoolaEmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyAdvanced.SHOOLA_UNABLE_CALCULATE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKeyAnalysis.TRANSIT_SELECT_CHART),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ShoolaInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyAdvanced.SHOOLA_INFO_TITLE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(StringKeyAdvanced.SHOOLA_INFO_DESC),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyAdvanced.SHOOLA_VEDIC_REF),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(com.astro.storm.core.common.StringKey.BTN_OK), color = AppTheme.AccentGold)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}

@Composable
private fun getSeverityColor(severity: HealthSeverity): Color {
    return when (severity) {
        HealthSeverity.CRITICAL -> AppTheme.ErrorColor
        HealthSeverity.HIGH -> AppTheme.WarningColor
        HealthSeverity.MODERATE -> AppTheme.AccentGold
        HealthSeverity.LOW -> AppTheme.AccentTeal
        HealthSeverity.MINIMAL -> AppTheme.TextMuted
        HealthSeverity.NONE -> AppTheme.TextMuted
    }
}






