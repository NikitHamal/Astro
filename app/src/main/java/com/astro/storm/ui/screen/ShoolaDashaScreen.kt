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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.astro.storm.core.common.Language
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKeyAdvanced
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.shoola.*
import com.astro.storm.ephemeris.shoola.ShoolaDashaCalculator
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.ShoolaDashaUiState
import com.astro.storm.ui.viewmodel.ShoolaDashaViewModel
import java.time.format.DateTimeFormatter

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
    viewModel: ShoolaDashaViewModel = viewModel()
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
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(StringKeyAdvanced.SHOOLA_TITLE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = stringResource(StringKeyAdvanced.SHOOLA_SUBTITLE),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppTheme.TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info",
                            tint = AppTheme.TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.ScreenBackground
                )
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is ShoolaDashaUiState.Loading -> ShoolaLoadingContent(modifier = Modifier.padding(paddingValues))
            is ShoolaDashaUiState.Error -> ShoolaErrorContent(
                message = state.message,
                modifier = Modifier.padding(paddingValues)
            )
            is ShoolaDashaUiState.Idle -> ShoolaEmptyContent(modifier = Modifier.padding(paddingValues))
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
private fun ShoolaTabSelector(
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
            FilterChip(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                label = {
                    Text(
                        text = tabs[index],
                        fontSize = 13.sp,
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
private fun ShoolaOverviewSection(
    result: ShoolaDashaResult,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp)
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
                text = stringResource(StringKeyAdvanced.SHOOLA_LONGEVITY_CATEGORY),
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(StringKeyAdvanced.SHOOLA_LONGEVITY_RANGE) + ": " + longevity.category.yearsRange,
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (language == Language.NEPALI) longevity.interpretationNe else longevity.interpretation,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TriMurtiCard(
    triMurti: TriMurtiAnalysis,
    language: Language
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
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
                    text = stringResource(StringKeyAdvanced.SHOOLA_TRI_MURTI),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

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
                modifier = Modifier.padding(vertical = 12.dp)
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
                modifier = Modifier.padding(vertical = 12.dp)
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
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = planet,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            sign?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
            }
            strength?.let {
                LinearProgressIndicator(
                    progress = { it.toFloat() },
                    modifier = Modifier
                        .width(60.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
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

    val dateFormatter = DateTimeFormatter.ofPattern("MMM yyyy")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = natureColor.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp)
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
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${period.sign.displayName} ${stringResource(StringKeyAdvanced.SHOOLA_MAHADASHA)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = natureColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (language == Language.NEPALI) period.nature.displayNameNe else period.nature.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = natureColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            Column {
                LinearProgressIndicator(
                    progress = { period.progress.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = natureColor,
                    trackColor = natureColor.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = period.startDate.format(dateFormatter),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = "${(period.progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = natureColor
                    )
                    Text(
                        text = period.endDate.format(dateFormatter),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Period details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyAdvanced.SHOOLA_SIGN_LORD),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = period.signLord.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyAdvanced.SHOOLA_HEALTH_SEVERITY),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = if (language == Language.NEPALI) period.healthSeverity.displayNameNe else period.healthSeverity.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
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
            value = if (result.dashaDirection == DashaDirection.DIRECT) "↻" else "↺",
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
        shape = RoundedCornerShape(12.dp)
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
    val dateFormatter = DateTimeFormatter.ofPattern("MMM yyyy")

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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = if (period.isCurrent)
                natureColor.copy(alpha = 0.08f) else AppTheme.CardBackground
        ),
        shape = RoundedCornerShape(12.dp)
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
                            .background(natureColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = period.sign.displayName.take(2),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = natureColor
                        )
                    }
                    Column {
                        Text(
                            text = "${period.sign.displayName} Dasha",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = "${period.startDate.format(dateFormatter)} - ${period.endDate.format(dateFormatter)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (period.isCurrent) natureColor.copy(alpha = 0.2f)
                        else AppTheme.ChipBackground
                    ) {
                        Text(
                            text = statusLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (period.isCurrent) natureColor else AppTheme.TextMuted,
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

                    // Nature and Health Severity
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(StringKeyAdvanced.SHOOLA_PERIOD_NATURE),
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = natureColor.copy(alpha = 0.15f),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = if (language == Language.NEPALI) period.nature.displayNameNe
                                    else period.nature.displayName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = natureColor,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(StringKeyAdvanced.SHOOLA_HEALTH_SEVERITY),
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = getSeverityColor(period.healthSeverity).copy(alpha = 0.15f),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = if (language == Language.NEPALI) period.healthSeverity.displayNameNe
                                    else period.healthSeverity.displayName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = getSeverityColor(period.healthSeverity),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Interpretation
                    Text(
                        text = if (language == Language.NEPALI) period.interpretationNe else period.interpretation,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )

                    // Significant planets
                    if (period.significantPlanets.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(StringKeyAdvanced.SHOOLA_SIGNIFICANT_PLANETS),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextMuted
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            items(period.significantPlanets) { planet ->
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = AppTheme.ChipBackground
                                ) {
                                    Text(
                                        text = planet.displayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AppTheme.TextSecondary,
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
                shape = RoundedCornerShape(16.dp)
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
        shape = RoundedCornerShape(16.dp)
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
                    Text("•", color = getSeverityColor(period.healthSeverity))
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
                        Text("✓", color = AppTheme.AccentTeal)
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
    val dateFormatter = DateTimeFormatter.ofPattern("MMM yyyy")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = getSeverityColor(vulnerability.severity).copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(12.dp)
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
                    shape = RoundedCornerShape(6.dp),
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
        shape = RoundedCornerShape(16.dp)
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
                        Text("✓", color = AppTheme.SuccessColor, fontSize = 12.sp)
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
                        Text("⚠", color = AppTheme.WarningColor, fontSize = 12.sp)
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
                shape = RoundedCornerShape(16.dp)
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
                        text = "No specific remedies needed at this time",
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = typeColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (language == Language.NEPALI) remedy.remedyType.displayNameNe
                        else remedy.remedyType.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = typeColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
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
                horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                text = "Please select a birth chart to view Shoola Dasha analysis",
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
                fontWeight = FontWeight.SemiBold
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
                Text("OK", color = AppTheme.AccentGold)
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

