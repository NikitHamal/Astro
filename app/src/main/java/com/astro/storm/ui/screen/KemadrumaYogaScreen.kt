package com.astro.storm.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Brightness2
import androidx.compose.material.icons.outlined.Healing
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.TipsAndUpdates
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
import com.astro.storm.ui.components.ScreenTopBar
import com.astro.storm.ui.components.ScreenTopBarDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.KemadrumaYogaCalculator
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.compose.ui.text.style.TextOverflow

/**
 * Kemadruma Yoga Screen - Displays Moon's isolation analysis
 *
 * Shows detailed analysis of Kemadruma Yoga (Moon without planetary support),
 * its cancellations (Bhanga), impacts, and remedies.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KemadrumaYogaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var kemadrumaAnalysis by remember { mutableStateOf<KemadrumaYogaCalculator.KemadrumaAnalysis?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(StringKeyDosha.KEMA_TAB_OVERVIEW),
        stringResource(StringKeyDosha.KEMA_MOON),
        stringResource(StringKeyDosha.KEMA_CANCELLATIONS),
        stringResource(StringKeyDosha.KEMA_IMPACTS),
        stringResource(StringKey.FEATURE_REMEDIES)
    )

    // Calculate Kemadruma analysis
    LaunchedEffect(chart) {
        if (chart == null) {
            isCalculating = false
            return@LaunchedEffect
        }
        isCalculating = true
        delay(300)
        try {
            kemadrumaAnalysis = withContext(Dispatchers.Default) {
                KemadrumaYogaCalculator.analyzeKemadruma(chart)
            }
        } catch (e: Exception) {
            // Handle calculation error
        }
        isCalculating = false
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            ScreenTopBar(
                title = stringResource(StringKeyDosha.KEMA_SCREEN_TITLE),
                subtitle = stringResource(StringKeyDosha.KEMA_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyAnalysis.UI_DETAILS),
                            tint = AppTheme.TextSecondary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            isCalculating -> {
                KemadrumaLoadingContent(modifier = Modifier.padding(paddingValues))
            }
            chart == null || kemadrumaAnalysis == null -> {
                KemadrumaEmptyContent(modifier = Modifier.padding(paddingValues))
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    // Tab selector
                    item {
                        KemadrumaTabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }

                    // Content based on selected tab
                    when (selectedTab) {
                        0 -> item { KemadrumaOverviewSection(kemadrumaAnalysis!!) }
                        1 -> item { MoonAnalysisSection(kemadrumaAnalysis!!) }
                        2 -> item { CancellationsSection(kemadrumaAnalysis!!) }
                        3 -> item { ImpactsSection(kemadrumaAnalysis!!) }
                        4 -> item { RemediesSection(kemadrumaAnalysis!!) }
                    }
                }
            }
        }
    }

    // Info Dialog
    if (showInfoDialog) {
        KemadrumaInfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun KemadrumaTabSelector(
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
private fun KemadrumaOverviewSection(analysis: KemadrumaYogaCalculator.KemadrumaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status Card
        KemadrumaStatusCard(analysis = analysis)

        // Quick Stats
        KemadrumaQuickStatsRow(analysis)

        // Interpretation
        InterpretationCard(interpretation = analysis.interpretation)

        // Formation details if present
        if (analysis.isKemadrumaFormed) {
            FormationDetailsCard(formation = analysis.formationDetails)
        }
    }
}

@Composable
private fun KemadrumaStatusCard(analysis: KemadrumaYogaCalculator.KemadrumaAnalysis) {
    val statusColor = getStatusColor(analysis.effectiveStatus)
    val isPositive = analysis.effectiveStatus == KemadrumaYogaCalculator.KemadrumaStatus.NOT_PRESENT ||
            analysis.effectiveStatus == KemadrumaYogaCalculator.KemadrumaStatus.FULLY_CANCELLED

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = statusColor.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(statusColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPositive) Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = analysis.effectiveStatus.displayName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (analysis.isKemadrumaFormed) {
                    stringResource(StringKeyDosha.KEMA_FORMED)
                } else {
                    stringResource(StringKeyDosha.KEMA_NOT_FORMED)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                textAlign = TextAlign.Center
            )

            if (analysis.isKemadrumaFormed && analysis.cancellations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AppTheme.SuccessColor.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Shield,
                            contentDescription = null,
                            tint = AppTheme.SuccessColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = stringResource(StringKeyDosha.KEMA_CANCELLATIONS_FOUND, analysis.cancellations.size),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.SuccessColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KemadrumaQuickStatsRow(analysis: KemadrumaYogaCalculator.KemadrumaAnalysis) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        KemadrumaStatCard(
            title = stringResource(StringKeyDosha.KEMA_SEVERITY_LABEL),
            value = "${analysis.effectiveStatus.severity}/5",
            color = getStatusColor(analysis.effectiveStatus),
            modifier = Modifier.weight(1f)
        )
        KemadrumaStatCard(
            title = stringResource(StringKeyDosha.KEMA_CANCELLATION_LABEL),
            value = "${analysis.totalCancellationScore}%",
            color = AppTheme.SuccessColor,
            modifier = Modifier.weight(1f)
        )
        KemadrumaStatCard(
            title = stringResource(StringKeyDosha.KEMA_PERIODS_LABEL),
            value = "${analysis.activationPeriods.size}",
            color = AppTheme.AccentPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun KemadrumaStatCard(
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
                .padding(16.dp),
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
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun InterpretationCard(interpretation: String) {
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
                    text = stringResource(StringKeyDosha.KEMA_INTERPRETATION_LABEL),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = interpretation,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun FormationDetailsCard(formation: KemadrumaYogaCalculator.KemadrumaFormation) {
    val formationDescription = buildString {
        append(stringResource(StringKeyDosha.KEMA_FORMATION_MAIN))
        if (formation.hasSecondHouseEmpty) append(" " + stringResource(StringKeyDosha.KEMA_FORMATION_2ND_EMPTY))
        if (formation.hasTwelfthHouseEmpty) append(" " + stringResource(StringKeyDosha.KEMA_FORMATION_12TH_EMPTY))
        if (formation.isMoonUnaspected) append(" " + stringResource(StringKeyDosha.KEMA_FORMATION_UNASPECTED))
    }

    val reasons = buildList {
        if (formation.hasSecondHouseEmpty) add(stringResource(StringKeyDosha.KEMA_REASON_2ND))
        if (formation.hasTwelfthHouseEmpty) add(stringResource(StringKeyDosha.KEMA_REASON_12TH))
        if (formation.isMoonUnaspected) add(stringResource(StringKeyDosha.KEMA_REASON_CONJUNCT))
        if (formation.formationStrength > 70) add(stringResource(StringKeyDosha.KEMA_FORMATION_STRENGTH, formation.formationStrength))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.WarningColor.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyDosha.KEMA_FORMATION_DETAILS),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = formationDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )

            if (reasons.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                reasons.forEach { reason ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "•",
                            color = AppTheme.WarningColor,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = reason,
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
private fun MoonAnalysisSection(analysis: KemadrumaYogaCalculator.KemadrumaAnalysis) {
    val language = LocalLanguage.current
    val moonAnalysis = analysis.moonPosition

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Moon Position Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(AppTheme.PlanetMoon.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "\u263D",
                            fontSize = 28.sp,
                            color = AppTheme.PlanetMoon
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(StringKeyDosha.KEMA_MOON_POSITION),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = "${moonAnalysis.sign.getLocalizedName(language)} in ${stringResource(StringKey.CHART_HOUSE)} ${moonAnalysis.house}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = AppTheme.DividerColor)
                Spacer(modifier = Modifier.height(16.dp))

                // Moon details
                MoonDetailRow(label = stringResource(StringKeyAnalysis.DIALOG_DEGREE), value = String.format("%.2f°", moonAnalysis.degree))
                MoonDetailRow(label = stringResource(StringKeyDosha.KEMA_NAKSHATRA), value = moonAnalysis.nakshatra)
                MoonDetailRow(label = stringResource(StringKeyDosha.KEMA_PAKSHA), value = stringResource(if (moonAnalysis.paksha == KemadrumaYogaCalculator.LunarPaksha.SHUKLA) StringKeyAnalysis.PAKSHA_SHUKLA else StringKeyAnalysis.PAKSHA_KRISHNA))
                MoonDetailRow(label = stringResource(StringKeyDosha.KEMA_BRIGHTNESS), value = moonAnalysis.brightness.getLocalizedName(language))

                Spacer(modifier = Modifier.height(16.dp))

                // Brightness indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(StringKeyDosha.KEMA_MOON_STRENGTH),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    LinearProgressIndicator(
                        progress = { getMoonBrightnessProgress(moonAnalysis.brightness) },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = getMoonBrightnessColor(moonAnalysis.brightness),
                        trackColor = AppTheme.DividerColor
                    )
                    Text(
                        text = moonAnalysis.brightness.name.replace("_", " ").lowercase()
                            .replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = getMoonBrightnessColor(moonAnalysis.brightness)
                    )
                }
            }
        }

        // Surrounding houses analysis
        SurroundingHousesCard(
            moonAnalysis = moonAnalysis,
            formationDetails = analysis.formationDetails
        )
    }
}

@Composable
private fun MoonDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextMuted
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun SurroundingHousesCard(
    moonAnalysis: KemadrumaYogaCalculator.MoonAnalysis,
    formationDetails: KemadrumaYogaCalculator.KemadrumaFormation
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyDosha.KEMA_SURROUNDING_HOUSES),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))

            val houseBefore = if (moonAnalysis.house == 1) 12 else moonAnalysis.house - 1
            val houseAfter = if (moonAnalysis.house == 12) 1 else moonAnalysis.house + 1

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HouseStatusBox(
                    house = houseBefore,
                    label = stringResource(StringKeyDosha.KEMA_BEFORE_MOON),
                    hasplanets = formationDetails.planetsInTwelfthFromMoon.isEmpty(),
                    isKendra = false
                )
                HouseStatusBox(
                    house = moonAnalysis.house,
                    label = stringResource(StringKeyDosha.KEMA_MOON_HOUSE),
                    hasplanets = true,
                    isKendra = true
                )
                HouseStatusBox(
                    house = houseAfter,
                    label = stringResource(StringKeyDosha.KEMA_AFTER_MOON),
                    hasplanets = formationDetails.planetsInSecondFromMoon.isEmpty(),
                    isKendra = false
                )
            }

            if (formationDetails.planetsInSecondFromMoon.isEmpty() && formationDetails.planetsInTwelfthFromMoon.isEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = AppTheme.WarningColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = stringResource(StringKeyDosha.KEMA_CONDITION_MET),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.WarningColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HouseStatusBox(
    house: Int,
    label: String,
    hasplanets: Boolean,
    isKendra: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isKendra) AppTheme.PlanetMoon.copy(alpha = 0.15f)
                    else if (hasplanets) AppTheme.WarningColor.copy(alpha = 0.1f)
                    else AppTheme.SuccessColor.copy(alpha = 0.1f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${house}H",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isKendra) AppTheme.PlanetMoon
                else if (hasplanets) AppTheme.WarningColor
                else AppTheme.SuccessColor
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CancellationsSection(analysis: KemadrumaYogaCalculator.KemadrumaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (analysis.cancellations.isEmpty()) {
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
                        imageVector = Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = AppTheme.TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (analysis.isKemadrumaFormed) stringResource(StringKeyDosha.KEMA_NO_CANCELLATIONS) else stringResource(StringKeyDosha.KEMA_NA),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (analysis.isKemadrumaFormed) {
                            stringResource(StringKeyDosha.KEMA_NO_CANCELLATIONS_DESC)
                        } else {
                            "Kemadruma Yoga is not formed, so cancellations are not applicable."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Total cancellation score
            TotalCancellationCard(score = analysis.totalCancellationScore)

            // Individual cancellations
            Text(
                text = stringResource(StringKeyDosha.KEMA_ACTIVE_CANCELLATIONS),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            analysis.cancellations.forEach { bhanga ->
                BhangaCard(bhanga = bhanga)
            }
        }
    }
}

@Composable
private fun TotalCancellationCard(score: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.SuccessColor.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                val progress by animateFloatAsState(
                    targetValue = score / 100f,
                    animationSpec = tween(1000),
                    label = "cancellationProgress"
                )
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = AppTheme.SuccessColor,
                    trackColor = AppTheme.DividerColor,
                    strokeWidth = 8.dp,
                    strokeCap = StrokeCap.Round
                )
                Text(
                    text = "$score%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.SuccessColor
                )
            }

            Column {
                Text(
                    text = stringResource(StringKeyDosha.KEMA_TOTAL_CANCELLATION),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(StringKeyAnalysis.UI_NET_STRENGTH_FMT, score.toString()),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun BhangaCard(bhanga: KemadrumaYogaCalculator.KemadrumaBhanga) {
    val language = LocalLanguage.current
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
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
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AppTheme.SuccessColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Shield,
                            contentDescription = null,
                            tint = AppTheme.SuccessColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = bhanga.type.name.replace("_", " "),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        bhanga.planet?.let { planet ->
                            Text(
                                text = "Via ${planet.getLocalizedName(language)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextMuted
                            )
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = AppTheme.SuccessColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "+${bhanga.strength}%",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.SuccessColor,
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
                    Text(
                        text = bhanga.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextSecondary,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ImpactsSection(analysis: KemadrumaYogaCalculator.KemadrumaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Emotional Impact
        ImpactCard(
            title = stringResource(StringKeyDosha.KEMADRUMA_EMOTIONAL),
            icon = Icons.Outlined.Psychology,
            impact = analysis.emotionalImpact,
            color = AppTheme.LifeAreaLove
        )

        // Financial Impact
        ImpactCard(
            title = stringResource(StringKeyDosha.KEMADRUMA_FINANCIAL),
            icon = Icons.Outlined.TipsAndUpdates,
            impact = analysis.financialImpact,
            color = AppTheme.LifeAreaFinance
        )

        // Social Impact
        ImpactCard(
            title = stringResource(StringKeyDosha.KEMADRUMA_SOCIAL),
            icon = Icons.Outlined.Brightness2,
            impact = analysis.socialImpact,
            color = AppTheme.AccentTeal
        )

        // Activation Periods
        if (analysis.activationPeriods.isNotEmpty()) {
            ActivationPeriodsCard(periods = analysis.activationPeriods)
        }
    }
}

@Composable
private fun ImpactCard(
    title: String,
    icon: ImageVector,
    impact: Any,
    color: Color
) {
    val level = when (impact) {
        is KemadrumaYogaCalculator.EmotionalImpact -> impact.overallLevel.name
        is KemadrumaYogaCalculator.FinancialImpact -> impact.overallLevel.name
        is KemadrumaYogaCalculator.SocialImpact -> impact.overallLevel.name
        else -> "NONE"
    }

    val recommendations = when (impact) {
        is KemadrumaYogaCalculator.EmotionalImpact -> impact.recommendations
        is KemadrumaYogaCalculator.FinancialImpact -> impact.recommendations
        is KemadrumaYogaCalculator.SocialImpact -> impact.recommendations
        else -> emptyList()
    }

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
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
                            .background(color.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = stringResource(StringKeyDosha.KEMA_IMPACT_LEVEL_LABEL, level.replace("_", " ")),
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
                        color = getImpactLevelColor(level).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = level.replace("_", " "),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = getImpactLevelColor(level),
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

                    if (recommendations.isNotEmpty()) {
                        Text(
                            text = stringResource(StringKeyDosha.KEMA_RECOMMENDATIONS_LABEL),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        recommendations.forEach { recommendation ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "•",
                                    color = color,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = recommendation,
                                    style = MaterialTheme.typography.bodySmall,
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
private fun ActivationPeriodsCard(periods: List<KemadrumaYogaCalculator.ActivationPeriod>) {
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
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.KEMADRUMA_ACTIVATION),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            periods.take(5).forEach { period ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
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
                                text = "${period.planet.displayName} ${period.periodType}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.TextPrimary
                            )
                            Text(
                                text = period.reason,
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = getIntensityColorFromInt(period.intensity).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = getIntensityLabel(period.intensity),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = getIntensityColorFromInt(period.intensity),
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
private fun RemediesSection(analysis: KemadrumaYogaCalculator.KemadrumaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (analysis.remedies.isEmpty()) {
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
                        imageVector = Icons.Outlined.Healing,
                        contentDescription = null,
                        tint = AppTheme.TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(StringKeyDosha.KEMA_NO_REMEDIES_NEEDED),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(StringKeyDosha.KEMADRUMA_FULLY_CANCELLED),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Text(
                text = stringResource(StringKeyDosha.DOSHA_REMEDIES_SECTION_TITLE),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Group by category
            val groupedRemedies = analysis.remedies.groupBy { it.category }

            groupedRemedies.forEach { (category, remedies) ->
                RemedyCategoryCard(category = category, remedies = remedies)
            }
        }
    }
}

@Composable
private fun RemedyCategoryCard(
    category: KemadrumaYogaCalculator.RemedyCategory,
    remedies: List<KemadrumaYogaCalculator.KemadrumaRemedy>
) {
    var expanded by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Healing,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = category.name.replace("_", " "),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = AppTheme.ChipBackground
                    ) {
                        Text(
                            text = "${remedies.size}",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    remedies.forEach { remedy ->
                        RemedyItem(remedy = remedy)
                    }
                }
            }
        }
    }
}

@Composable
private fun RemedyItem(remedy: KemadrumaYogaCalculator.KemadrumaRemedy) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = AppTheme.CardBackgroundElevated
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = remedy.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = getPriorityColorFromInt(remedy.priority).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = getPriorityLabelFromInt(remedy.priority),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = getPriorityColorFromInt(remedy.priority),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            if (remedy.timing.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Timing: ${remedy.timing}",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun KemadrumaLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyDosha.KEMA_ANALYZING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun KemadrumaEmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Brightness2,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyDosha.KEMA_NO_CHART_DATA),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKeyDosha.KEMA_SELECT_CHART_DESC),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun KemadrumaInfoDialog(onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyDosha.KEMA_ABOUT_TITLE_MODAL),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                text = stringResource(StringKeyDosha.KEMA_ABOUT_DESC),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(stringResource(StringKeyDosha.BTN_GOT_IT), color = AppTheme.AccentPrimary)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}

// Helper functions
@Composable
private fun getStatusColor(status: KemadrumaYogaCalculator.KemadrumaStatus): Color {
    return when (status) {
        KemadrumaYogaCalculator.KemadrumaStatus.NOT_PRESENT -> AppTheme.SuccessColor
        KemadrumaYogaCalculator.KemadrumaStatus.FULLY_CANCELLED -> AppTheme.SuccessColor
        KemadrumaYogaCalculator.KemadrumaStatus.MOSTLY_CANCELLED -> AppTheme.AccentTeal
        KemadrumaYogaCalculator.KemadrumaStatus.PARTIALLY_CANCELLED -> AppTheme.AccentGold
        KemadrumaYogaCalculator.KemadrumaStatus.WEAKLY_CANCELLED -> AppTheme.WarningColor
        KemadrumaYogaCalculator.KemadrumaStatus.ACTIVE_MODERATE -> Color(0xFFFF9800)
        KemadrumaYogaCalculator.KemadrumaStatus.ACTIVE_SEVERE -> AppTheme.ErrorColor
    }
}

@Composable
private fun getMoonBrightnessColor(brightness: KemadrumaYogaCalculator.MoonBrightness): Color {
    return when (brightness) {
        KemadrumaYogaCalculator.MoonBrightness.FULL -> AppTheme.SuccessColor
        KemadrumaYogaCalculator.MoonBrightness.BRIGHT -> AppTheme.AccentTeal
        KemadrumaYogaCalculator.MoonBrightness.AVERAGE -> AppTheme.AccentGold
        KemadrumaYogaCalculator.MoonBrightness.DIM -> AppTheme.WarningColor
        KemadrumaYogaCalculator.MoonBrightness.NEW -> AppTheme.ErrorColor
    }
}

private fun getMoonBrightnessProgress(brightness: KemadrumaYogaCalculator.MoonBrightness): Float {
    return when (brightness) {
        KemadrumaYogaCalculator.MoonBrightness.FULL -> 1f
        KemadrumaYogaCalculator.MoonBrightness.BRIGHT -> 0.8f
        KemadrumaYogaCalculator.MoonBrightness.AVERAGE -> 0.6f
        KemadrumaYogaCalculator.MoonBrightness.DIM -> 0.4f
        KemadrumaYogaCalculator.MoonBrightness.NEW -> 0.2f
    }
}

@Composable
private fun getImpactLevelColor(level: String): Color {
    return when (level.uppercase()) {
        "SEVERE", "HIGH" -> AppTheme.ErrorColor
        "MODERATE", "MEDIUM" -> AppTheme.WarningColor
        "MILD", "LOW" -> AppTheme.AccentGold
        "NONE", "MINIMAL" -> AppTheme.SuccessColor
        else -> AppTheme.TextMuted
    }
}

@Composable
private fun getIntensityColor(intensity: String): Color {
    return when (intensity.uppercase()) {
        "HIGH", "STRONG" -> AppTheme.ErrorColor
        "MODERATE", "MEDIUM" -> AppTheme.WarningColor
        "LOW", "MILD" -> AppTheme.AccentTeal
        else -> AppTheme.TextMuted
    }
}

@Composable
private fun getEffectivenessColor(effectiveness: String): Color {
    return when (effectiveness.uppercase()) {
        "HIGH", "VERY_HIGH" -> AppTheme.SuccessColor
        "MODERATE", "MEDIUM" -> AppTheme.AccentTeal
        "LOW" -> AppTheme.AccentGold
        else -> AppTheme.TextMuted
    }
}

@Composable
private fun getIntensityColorFromInt(intensity: Int): Color {
    return when (intensity) {
        5 -> AppTheme.ErrorColor
        4 -> Color(0xFFFF9800)
        3 -> AppTheme.WarningColor
        2 -> AppTheme.AccentGold
        1 -> AppTheme.AccentTeal
        else -> AppTheme.TextMuted
    }
}

@Composable
private fun getIntensityLabel(intensity: Int): String {
    return when (intensity) {
        5 -> stringResource(StringKeyDosha.KEMA_SEV_VERY_HIGH)
        4 -> stringResource(StringKeyDosha.KEMA_SEV_HIGH)
        3 -> stringResource(StringKeyDosha.KEMA_SEV_MODERATE)
        2 -> stringResource(StringKeyDosha.KEMA_SEV_LOW)
        1 -> stringResource(StringKeyDosha.KEMA_SEV_MILD)
        else -> stringResource(com.astro.storm.core.common.StringKeyDosha.LABEL_UNKNOWN)
    }
}

@Composable
private fun getPriorityLabelFromInt(priority: Int): String {
    return when (priority) {
        1 -> stringResource(StringKeyDosha.KEMA_PRIORITY_ESSENTIAL)
        2 -> stringResource(StringKeyDosha.KEMA_PRIORITY_RECOMMENDED)
        3 -> stringResource(StringKeyDosha.KEMA_PRIORITY_OPTIONAL)
        else -> stringResource(StringKeyDosha.KEMA_PRIORITY_OPTIONAL)
    }
}

@Composable
private fun getPriorityColorFromInt(priority: Int): Color {
    return when (priority) {
        1 -> AppTheme.ErrorColor // Essential
        2 -> AppTheme.WarningColor // Recommended
        3 -> AppTheme.AccentTeal // Optional
        else -> AppTheme.TextMuted
    }
}

