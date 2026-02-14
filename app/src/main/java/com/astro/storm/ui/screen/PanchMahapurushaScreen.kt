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
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.WorkspacePremium
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
import com.astro.storm.ui.components.common.NeoVedicPageHeader
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
import com.astro.storm.core.common.*
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.localization.localized
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.PanchMahapurushaYogaCalculator
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.compose.ui.text.style.TextOverflow

/**
 * Panch Mahapurusha Yoga Screen
 *
 * Displays the five great yogas formed by Mars, Mercury, Jupiter, Venus, and Saturn
 * when placed in Kendra houses in their own or exaltation signs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanchMahapurushaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var analysis by remember { mutableStateOf<PanchMahapurushaYogaCalculator.PanchMahapurushaAnalysis?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(StringKeyDosha.UI_OVERVIEW),
        stringResource(StringKeyDosha.UI_YOGAS),
        stringResource(StringKeyDosha.UI_EFFECTS),
        stringResource(StringKeyDosha.UI_TIMING),
        stringResource(StringKeyDosha.UI_REMEDIES)
    )

    // Calculate analysis
    LaunchedEffect(chart) {
        if (chart == null) {
            isCalculating = false
            return@LaunchedEffect
        }
        isCalculating = true
        delay(300)
        try {
            analysis = withContext(Dispatchers.Default) {
                PanchMahapurushaYogaCalculator.analyzePanchMahapurushaYogas(chart)
            }
        } catch (e: Exception) {
            // Handle error
        }
        isCalculating = false
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.PANCHA_SCREEN_TITLE),
                subtitle = stringResource(StringKeyDosha.PANCHA_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyAshtamangala.INFO_TITLE),
                            tint = AppTheme.TextSecondary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            isCalculating -> {
                LoadingContent(modifier = Modifier.padding(paddingValues))
            }
            chart == null || analysis == null -> {
                EmptyContent(modifier = Modifier.padding(paddingValues))
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
                        TabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }

                    // Content based on tab
                    when (selectedTab) {
                        0 -> item { OverviewSection(analysis!!) }
                        1 -> item { YogasSection(analysis!!) }
                        2 -> item { EffectsSection(analysis!!) }
                        3 -> item { TimingSection(analysis!!) }
                        4 -> item { RemediesSection(analysis!!) }
                    }
                }
            }
        }
    }

    if (showInfoDialog) {
        InfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun TabSelector(
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
            com.astro.storm.ui.components.common.NeoVedicChoicePill(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                label = {
                    Text(
                        text = tabs[index],
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
                        fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AppTheme.AccentGold.copy(alpha = 0.15f),
                    selectedLabelColor = AppTheme.AccentGold,
                    containerColor = AppTheme.CardBackground,
                    labelColor = AppTheme.TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = AppTheme.BorderColor,
                    selectedBorderColor = AppTheme.AccentGold.copy(alpha = 0.3f),
                    enabled = true,
                    selected = selectedTab == index
                )
            )
        }
    }
}

@Composable
private fun OverviewSection(analysis: PanchMahapurushaYogaCalculator.PanchMahapurushaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status Card
        StatusCard(analysis = analysis)

        // Quick Stats
        QuickStatsRow(analysis)

        // Interpretation
        InterpretationCard(interpretation = analysis.interpretation)

        // Combined Effects (if multiple yogas)
        analysis.combinedEffects?.let { combined ->
            CombinedEffectsCard(combined)
        }
    }
}

@Composable
private fun StatusCard(analysis: PanchMahapurushaYogaCalculator.PanchMahapurushaAnalysis) {
    val hasYogas = analysis.hasAnyYoga
    val statusColor = if (hasYogas) AppTheme.AccentGold else AppTheme.TextMuted

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = statusColor.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
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
                    imageVector = if (hasYogas) Icons.Filled.Stars else Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (hasYogas) {
                     // Note: Handled dynamically, but title is "Yoga(s) Found!" or "No Yogas Formed"
                     if(analysis.yogaCount > 0) stringResource(StringKeyDosha.PANCHA_STATUS_FOUND) else stringResource(StringKeyDosha.PANCHA_NO_YOGAS)
                } else stringResource(StringKeyDosha.PANCHA_NO_YOGAS),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (hasYogas) {
                    String.format(stringResource(StringKeyDosha.PANCHA_STATUS_FOUND_DESC), analysis.yogaCount)
                } else {
                    stringResource(StringKeyDosha.PANCHA_STATUS_NONE_DESC)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                textAlign = TextAlign.Center
            )

            if (hasYogas) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    analysis.yogas.forEach { yoga ->
                        Surface(
                            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = getYogaColor(yoga.type).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = yoga.type.displayName,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = getYogaColor(yoga.type),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickStatsRow(analysis: PanchMahapurushaYogaCalculator.PanchMahapurushaAnalysis) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = stringResource(StringKeyDosha.UI_YOGAS),
            value = "${analysis.yogaCount}/5",
            color = AppTheme.AccentGold,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = stringResource(StringKeyDosha.PANCHA_STRENGTH),
            value = analysis.overallYogaStrength.toString() + stringResource(StringKeyUIExtra.PERCENT),
            color = AppTheme.AccentPrimary,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = stringResource(StringKeyDosha.PANCHA_PERIODS),
            value = "${analysis.activationPeriods.size}",
            color = AppTheme.AccentTeal,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
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
private fun InterpretationCard(interpretation: PanchMahapurushaYogaCalculator.OverallInterpretation) {
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
                    imageVector = Icons.Outlined.Psychology,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
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
                text = interpretation.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 24.sp
            )

            if (interpretation.keyInsights.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                interpretation.keyInsights.forEach { insight ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "â€¢",
                            color = AppTheme.AccentGold,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = insight,
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
private fun CombinedEffectsCard(combined: PanchMahapurushaYogaCalculator.CombinedYogaEffects) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.AccentGold.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.WorkspacePremium,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.PANCHA_COMBINED),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Surface(
                    shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = AppTheme.AccentGold.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = combined.rarity,
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.AccentGold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = combined.overallInterpretation,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )

            if (combined.synergies.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyDosha.PANCHA_SYNERGIES),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                combined.synergies.forEach { synergy ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("âœ¨", fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12)
                        Text(
                            text = synergy,
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
private fun YogasSection(analysis: PanchMahapurushaYogaCalculator.PanchMahapurushaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (analysis.yogas.isEmpty()) {
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
                        imageVector = Icons.Outlined.AutoAwesome,
                        contentDescription = null,
                        tint = AppTheme.TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(StringKeyDosha.PANCHA_NO_YOGAS_DISPLAY),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(StringKeyDosha.PANCHA_NO_YOGAS_DESC),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            analysis.yogas.forEach { yoga ->
                YogaDetailCard(yoga = yoga)
            }
        }
    }
}

@Composable
private fun YogaDetailCard(yoga: PanchMahapurushaYogaCalculator.MahapurushaYoga) {
    val language = LocalLanguage.current
    var expanded by remember { mutableStateOf(true) }
    val yogaColor = getYogaColor(yoga.type)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
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
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(yogaColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = yoga.planet.symbol,
                            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S24,
                            color = yogaColor
                        )
                    }
                    Column {
                        Text(
                            text = yoga.type.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = stringResource(
                                StringKey.PLANET_IN_SIGN_ACCESSIBILITY,
                                yoga.planet.getLocalizedName(language),
                                yoga.sign.getLocalizedName(language)
                            ) + ", " + stringResource(StringKeyUICommon.HOUSE) + " " + yoga.house.localized(),
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
                        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                        color = yogaColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = yoga.strength.toString() + stringResource(StringKeyUIExtra.PERCENT),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = yogaColor,
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

                    // Dignity info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = if (yoga.isExalted) AppTheme.SuccessColor.copy(alpha = 0.15f)
                            else AppTheme.AccentTeal.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (yoga.isExalted) stringResource(StringKeyUICommon.EXALTED) else stringResource(StringKeyUICommon.OWN_SIGN),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (yoga.isExalted) AppTheme.SuccessColor else AppTheme.AccentTeal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = AppTheme.ChipBackground
                        ) {
                            Text(
                                text = yoga.strengthLevel.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = yoga.interpretation,
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
private fun EffectsSection(analysis: PanchMahapurushaYogaCalculator.PanchMahapurushaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (analysis.yogas.isEmpty()) {
            EmptyYogasMessage()
        } else {
            analysis.yogas.forEach { yoga ->
                EffectsCard(yoga = yoga)
            }
        }
    }
}

@Composable
private fun EffectsCard(yoga: PanchMahapurushaYogaCalculator.MahapurushaYoga) {
    val yogaColor = getYogaColor(yoga.type)

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
                Text(
                    text = yoga.planet.symbol,
                    fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S20,
                    color = yogaColor
                )
                Text(
                    text = stringResource(StringKeyDosha.PANCHA_EFFECTS_TITLE, yoga.type.displayName),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            EffectRow(label = stringResource(StringKeyDosha.PANCHA_PHYSICAL), value = yoga.effects.physicalTraits)
            EffectRow(label = stringResource(StringKeyDosha.PANCHA_MENTAL), value = yoga.effects.mentalTraits)
            EffectRow(label = stringResource(StringKeyDosha.PANCHA_CAREER), value = yoga.effects.careerIndications)
            EffectRow(label = stringResource(StringKeyDosha.PANCHA_SPIRITUAL), value = yoga.effects.spiritualGrowth)
            EffectRow(label = stringResource(StringKeyDosha.PANCHA_RELATIONSHIPS), value = yoga.effects.relationshipImpact)

            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                color = yogaColor.copy(alpha = 0.08f)
            ) {
                Text(
                    text = yoga.effects.houseSpecificEffect,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
private fun EffectRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label + stringResource(StringKeyUICommon.COLON),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextMuted,
            modifier = Modifier.width(90.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.TextSecondary
        )
    }
}

@Composable
private fun TimingSection(analysis: PanchMahapurushaYogaCalculator.PanchMahapurushaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (analysis.activationPeriods.isEmpty()) {
            EmptyYogasMessage()
        } else {
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
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = AppTheme.AccentPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(StringKeyDosha.PANCHA_ACTIVATION_PERIODS),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    analysis.activationPeriods.take(10).forEach { period ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
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
                                        text = period.periodType,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = AppTheme.TextPrimary
                                    )
                                    Text(
                                        text = period.description,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AppTheme.TextMuted
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                                    color = when (period.importance) {
                                        PanchMahapurushaYogaCalculator.ActivationImportance.HIGH ->
                                            AppTheme.SuccessColor.copy(alpha = 0.15f)
                                        PanchMahapurushaYogaCalculator.ActivationImportance.MEDIUM ->
                                            AppTheme.AccentGold.copy(alpha = 0.15f)
                                        else -> AppTheme.ChipBackground
                                    }
                                ) {
                                    Text(
                                        text = period.importance.name,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = when (period.importance) {
                                            PanchMahapurushaYogaCalculator.ActivationImportance.HIGH ->
                                                AppTheme.SuccessColor
                                            PanchMahapurushaYogaCalculator.ActivationImportance.MEDIUM ->
                                                AppTheme.AccentGold
                                            else -> AppTheme.TextMuted
                                        },
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
}

@Composable
private fun RemediesSection(analysis: PanchMahapurushaYogaCalculator.PanchMahapurushaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (analysis.yogas.isEmpty()) {
            EmptyYogasMessage()
        } else {
            analysis.yogas.forEach { yoga ->
                RemedyCard(yoga = yoga)
            }
        }
    }
}

@Composable
private fun RemedyCard(yoga: PanchMahapurushaYogaCalculator.MahapurushaYoga) {
    val yogaColor = getYogaColor(yoga.type)

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
                    imageVector = Icons.Outlined.Spa,
                    contentDescription = null,
                    tint = yogaColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyDosha.PANCHA_RECOMMENDATIONS_TITLE, yoga.type.displayName),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            yoga.recommendations.forEach { recommendation ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = AppTheme.CardBackgroundElevated
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                                color = yogaColor.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = recommendation.category.getLocalizedName(LocalLanguage.current),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = yogaColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = recommendation.action,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = recommendation.benefit,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                        Text(
                            text = stringResource(StringKeyDosha.PANCHA_TIMING_LABEL, recommendation.timing),
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
private fun EmptyYogasMessage() {
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
            Text(
                text = stringResource(StringKeyDosha.PANCHA_NO_YOGAS_DISPLAY),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKeyDosha.PANCHA_DETAILS_EMPTY_DESC),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentGold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyDosha.PANCHA_ANALYZING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Stars,
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
                text = stringResource(StringKeyDosha.PANCHA_NO_CHART_DESC),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun InfoDialog(onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyDosha.PANCHA_ABOUT_TITLE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                text = stringResource(StringKeyDosha.PANCHA_ABOUT_DESC),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(stringResource(StringKeyDosha.BTN_GOT_IT), color = AppTheme.AccentGold)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}

@Composable
private fun getYogaColor(type: PanchMahapurushaYogaCalculator.MahapurushaYogaType): Color {
    return when (type) {
        PanchMahapurushaYogaCalculator.MahapurushaYogaType.RUCHAKA -> AppTheme.PlanetMars
        PanchMahapurushaYogaCalculator.MahapurushaYogaType.BHADRA -> AppTheme.PlanetMercury
        PanchMahapurushaYogaCalculator.MahapurushaYogaType.HAMSA -> AppTheme.PlanetJupiter
        PanchMahapurushaYogaCalculator.MahapurushaYogaType.MALAVYA -> AppTheme.PlanetVenus
        PanchMahapurushaYogaCalculator.MahapurushaYogaType.SASHA -> AppTheme.PlanetSaturn
    }
}






