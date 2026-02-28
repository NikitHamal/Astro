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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Warning
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
import com.astro.vajra.core.common.StringKeyDosha
import com.astro.vajra.core.common.StringKeyUIExtra
import com.astro.vajra.core.common.StringKeyUICommon
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.BadhakaCalculator
import com.astro.vajra.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.compose.ui.text.style.TextOverflow

/**
 * Badhaka Analysis Screen
 *
 * Displays comprehensive analysis of Badhaka (obstruction) planets including:
 * - Badhaka sthana identification based on sign modality
 * - Primary and secondary Badhaka planets
 * - Obstacle types and life areas affected
 * - Critical Dasha periods with obstruction potential
 * - Remedial measures to overcome obstacles
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadhakaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var analysis by remember { mutableStateOf<BadhakaCalculator.BadhakaAnalysis?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(StringKeyDosha.UI_OVERVIEW),
        stringResource(StringKeyDosha.BADHAKA_TAB_PLANETS),
        stringResource(StringKeyDosha.BADHAKA_TAB_OBSTACLES),
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
                BadhakaCalculator.analyzeBadhaka(chart)
            }
        } catch (_: Exception) { }
        isCalculating = false
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.BADHAKA_SCREEN_TITLE),
                subtitle = stringResource(StringKeyDosha.BADHAKA_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyUICommon.INFO),
                            tint = AppTheme.TextSecondary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            isCalculating -> BadhakaLoadingContent(modifier = Modifier.padding(paddingValues))
            chart == null || analysis == null -> BadhakaEmptyContent(modifier = Modifier.padding(paddingValues))
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        BadhakaTabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                    when (selectedTab) {
                        0 -> item { BadhakaOverviewSection(analysis!!) }
                        1 -> item { BadhakaPlanetsSection(analysis!!) }
                        2 -> item { ObstaclesSection(analysis!!) }
                        3 -> item { BadhakaDashaSection(analysis!!) }
                        4 -> item { BadhakaRemediesSection(analysis!!) }
                    }
                }
            }
        }
    }

    if (showInfoDialog) {
        BadhakaInfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun BadhakaTabSelector(
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
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                        fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AppTheme.AccentTeal.copy(alpha = 0.15f),
                    selectedLabelColor = AppTheme.AccentTeal,
                    containerColor = AppTheme.CardBackground,
                    labelColor = AppTheme.TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = AppTheme.BorderColor,
                    selectedBorderColor = AppTheme.AccentTeal.copy(alpha = 0.3f),
                    enabled = true,
                    selected = selectedTab == index
                )
            )
        }
    }
}

@Composable
private fun BadhakaOverviewSection(analysis: BadhakaCalculator.BadhakaAnalysis) {
    val language = LocalLanguage.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BadhakaSthanaCard(analysis)
        BadhakaQuickStats(analysis)
        BadhakaInterpretationCard(analysis)
    }
}

@Composable
private fun BadhakaSthanaCard(analysis: BadhakaCalculator.BadhakaAnalysis) {
    val language = LocalLanguage.current
    val modalityColor = when (analysis.signModality) {
        BadhakaCalculator.SignModality.MOVABLE -> AppTheme.AccentPrimary
        BadhakaCalculator.SignModality.FIXED -> AppTheme.AccentGold
        BadhakaCalculator.SignModality.DUAL -> AppTheme.AccentTeal
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = modalityColor.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
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
                    .background(modalityColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Block,
                    contentDescription = null,
                    tint = modalityColor,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyDosha.BADHAKA_STHANA) + stringResource(StringKeyUIExtra.COLON_SPACE) + analysis.badhakaHouse + getOrdinalSuffix(analysis.badhakaHouse) + " " + stringResource(StringKeyAnalysis.EXPORT_HOUSE),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = modalityColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = modalityColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = analysis.signModality.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = modalityColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = analysis.ascendantSign.getLocalizedName(language),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = getBadhakaSthanaExplanation(analysis.signModality),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun getOrdinalSuffix(number: Int): String {
    return when {
        number in 11..13 -> stringResource(StringKeyUIExtra.ORDINAL_TH)
        number % 10 == 1 -> stringResource(StringKeyUIExtra.ORDINAL_ST)
        number % 10 == 2 -> stringResource(StringKeyUIExtra.ORDINAL_ND)
        number % 10 == 3 -> stringResource(StringKeyUIExtra.ORDINAL_RD)
        else -> stringResource(StringKeyUIExtra.ORDINAL_TH)
    }
}

@Composable
private fun getBadhakaSthanaExplanation(modality: BadhakaCalculator.SignModality): String {
    return when (modality) {
        BadhakaCalculator.SignModality.MOVABLE -> stringResource(StringKeyUIExtra.BADHAKA_EXPL_MOVABLE)
        BadhakaCalculator.SignModality.FIXED -> stringResource(StringKeyUIExtra.BADHAKA_EXPL_FIXED)
        BadhakaCalculator.SignModality.DUAL -> stringResource(StringKeyUIExtra.BADHAKA_EXPL_DUAL)
    }
}

@Composable
private fun BadhakaQuickStats(analysis: BadhakaCalculator.BadhakaAnalysis) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BadhakaStatCard(
            title = stringResource(StringKeyDosha.BADHAKA_PRIMARY),
            value = "1", // Badhakesh is always 1
            color = AppTheme.ErrorColor,
            modifier = Modifier.weight(1f)
        )
        BadhakaStatCard(
            title = stringResource(StringKeyDosha.BADHAKA_SECONDARY),
            value = "${analysis.planetsInBadhakaHouse.size}",
            color = AppTheme.WarningColor,
            modifier = Modifier.weight(1f)
        )
        BadhakaStatCard(
            title = stringResource(StringKeyDosha.BADHAKA_SEVERITY),
            value = (analysis.overallSeverity.level * 20).toString() + stringResource(StringKeyUIExtra.PERCENT),
            color = getSeverityColor(analysis.overallSeverity.level * 20),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun getSeverityColor(severity: Int): Color {
    return when {
        severity >= 70 -> AppTheme.ErrorColor
        severity >= 50 -> AppTheme.WarningColor
        severity >= 30 -> AppTheme.AccentGold
        else -> AppTheme.SuccessColor
    }
}

@Composable
private fun BadhakaStatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
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
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun BadhakaInterpretationCard(analysis: BadhakaCalculator.BadhakaAnalysis) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Psychology,
                    contentDescription = null,
                    tint = AppTheme.AccentTeal,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.UI_INTERPRETATION),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = analysis.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 24.sp
            )
            if (analysis.protectiveFactors.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyDosha.BADHAKA_MITIGATING),
                    style = MaterialTheme.typography.labelMedium,
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
private fun BadhakaPlanetsSection(analysis: BadhakaCalculator.BadhakaAnalysis) {
    val language = LocalLanguage.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Primary Badhakesh
        Text(
            text = stringResource(StringKeyDosha.BADHAKA_PRIMARY_TITLE),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.ErrorColor
        )
        BadhakaPlanetCard(badhaka = analysis.badhakeshPosition, isPrimary = true)

        // Planets in Badhaka House (Secondary)
        if (analysis.planetsInBadhakaHouse.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKeyDosha.BADHAKA_SECONDARY_TITLE),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.WarningColor
            )
            analysis.planetsInBadhakaHouse.forEach { badhaka ->
                BadhakaPlanetCard(badhaka = badhaka, isPrimary = false)
            }
        }
    }
}

@Composable
private fun BadhakaPlanetCard(
    badhaka: BadhakaCalculator.BadhakaPlanet,
    isPrimary: Boolean
) {
    val language = LocalLanguage.current
    var expanded by remember { mutableStateOf(false) }
    val severityColor = when (badhaka.severity) {
        BadhakaCalculator.BadhakaSeverity.SEVERE -> AppTheme.ErrorColor
        BadhakaCalculator.BadhakaSeverity.STRONG -> AppTheme.WarningColor
        BadhakaCalculator.BadhakaSeverity.MODERATE -> AppTheme.AccentGold
        BadhakaCalculator.BadhakaSeverity.MILD -> AppTheme.AccentTeal
        BadhakaCalculator.BadhakaSeverity.MINIMAL -> AppTheme.TextMuted
        BadhakaCalculator.BadhakaSeverity.NONE -> AppTheme.TextMuted
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
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
                            text = badhaka.planet.symbol,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S22,
                            color = severityColor
                        )
                    }
                    Column {
                        Text(
                            text = badhaka.planet.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = badhaka.position.sign.getLocalizedName(language) + stringResource(StringKeyUIExtra.BULLET_SPACE) + stringResource(StringKeyUIExtra.HOUSE_PREFIX_SHORT) + badhaka.position.house,
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
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                        color = severityColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = badhaka.severity.displayName,
                            style = MaterialTheme.typography.labelSmall,
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

                    if (isPrimary) {
                        Surface(
                            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = AppTheme.ErrorColor.copy(alpha = 0.15f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = stringResource(StringKeyDosha.BADHAKA_LORD),
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.ErrorColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = badhaka.interpretation,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )

                    if (false) { // badhaka.obstacleTypes property does not exist in BadhakaPlanet
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(StringKeyDosha.BADHAKA_OBSTACLE_TYPES),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ObstaclesSection(analysis: BadhakaCalculator.BadhakaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Affected Life Areas Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = null,
                        tint = AppTheme.WarningColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(StringKeyDosha.BADHAKA_AFFECTED_AREAS),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                if (analysis.affectedLifeAreas.isEmpty()) {
                    Text(
                        text = stringResource(StringKeyDosha.BADHAKA_NO_AREAS),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextMuted
                    )
                } else {
                    analysis.affectedLifeAreas.forEach { area ->
                        ObstacleAreaRow(area)
                    }
                }
            }
        }

        // Primary Obstacles Card (Replaces Obstacle Nature)
        if (analysis.primaryObstacles.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(StringKeyDosha.BADHAKA_NATURE),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    analysis.primaryObstacles.forEach { obstacle ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Block,
                                contentDescription = null,
                                tint = AppTheme.WarningColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = obstacle.displayName,
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

@Composable
private fun ObstacleAreaRow(area: BadhakaCalculator.AffectedLifeArea) {
    val areaColor = when {
        area.impactLevel >= 70 -> AppTheme.ErrorColor
        area.impactLevel >= 50 -> AppTheme.WarningColor
        area.impactLevel >= 30 -> AppTheme.AccentGold
        else -> AppTheme.TextMuted
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackgroundElevated
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = area.area,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
                Surface(
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = areaColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${area.impactLevel}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = areaColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { area.impactLevel / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                color = areaColor,
                trackColor = areaColor.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
            if (area.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = area.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun BadhakaDashaSection(analysis: BadhakaCalculator.BadhakaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (analysis.dashaPeriods.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.SuccessColor.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
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
                        text = stringResource(StringKeyDosha.BADHAKA_NO_CRITICAL),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            tint = AppTheme.AccentTeal,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(StringKeyDosha.BADHAKA_CRITICAL_PERIODS),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    analysis.dashaPeriods.take(8).forEach { period ->
                        BadhakaDashaPeriodRow(period)
                    }
                }
            }
        }
    }
}

@Composable
private fun BadhakaDashaPeriodRow(period: BadhakaCalculator.BadhakaDashaPeriod) {
    val riskColor = when {
        period.obstructionLevel.level >= 4 -> AppTheme.ErrorColor
        period.obstructionLevel.level >= 3 -> AppTheme.WarningColor
        period.obstructionLevel.level >= 2 -> AppTheme.AccentGold
        else -> AppTheme.TextMuted
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
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
                    text = "${period.planet.displayName} Period",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
//                Text(
//                    text = period.dateRange,
//                    style = MaterialTheme.typography.labelSmall,
//                    color = AppTheme.TextMuted
//                )
            }
            Surface(
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                color = riskColor.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "${period.obstructionLevel.level * 20}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = riskColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun BadhakaRemediesSection(analysis: BadhakaCalculator.BadhakaAnalysis) {
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
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
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
                        text = stringResource(StringKeyDosha.BADHAKA_NO_REMEDIES),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            analysis.remedies.forEach { remedy ->
                BadhakaRemedyCard(remedy)
            }
        }
    }
}

@Composable
private fun BadhakaRemedyCard(remedy: BadhakaCalculator.BadhakaRemedy) {
    val categoryColor = when (remedy.remedyType) {
        "Mantra" -> AppTheme.AccentGold
        "Worship" -> AppTheme.LifeAreaSpiritual
        "Charity" -> AppTheme.SuccessColor
        "Gemstone" -> AppTheme.AccentPrimary
        "Fasting" -> AppTheme.AccentTeal
        "Yantra" -> AppTheme.WarningColor
        "Pilgrimage" -> AppTheme.LifeAreaHealth
        "Multi-fold" -> AppTheme.ErrorColor
        "Practice" -> AppTheme.LifeAreaSpiritual
        "General" -> AppTheme.TextSecondary
        else -> AppTheme.TextSecondary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = categoryColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = remedy.remedyType,
                        style = MaterialTheme.typography.labelSmall,
                        color = categoryColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = remedy.planet?.displayName ?: "General",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = remedy.description,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            Text(
                text = (remedy.mantra ?: "").ifEmpty { remedy.charity ?: "" },
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary
            )
            if (!remedy.ritual.isNullOrEmpty()) {
                Text(
                    text = stringResource(StringKeyDosha.UI_TIMING) + stringResource(StringKeyUIExtra.COLON_SPACE) + remedy.ritual,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun BadhakaLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentTeal)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyDosha.BADHAKA_ANALYZING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun BadhakaEmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Block,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyDosha.UI_NO_CHART_DATA),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKeyDosha.BADHAKA_NO_CHART_DESC),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun BadhakaInfoDialog(onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyDosha.BADHAKA_ABOUT_TITLE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                text = stringResource(StringKeyDosha.BADHAKA_ABOUT_DESC),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(stringResource(StringKeyDosha.BTN_GOT_IT), color = AppTheme.AccentTeal)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}







