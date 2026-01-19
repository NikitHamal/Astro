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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.TrendingUp
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.astro.storm.core.common.Language
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.VipareetaRajaYogaCalculator
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Vipareeta Raja Yoga Screen
 *
 * Displays comprehensive analysis of the three Vipareeta (Reverse) Raja Yogas:
 * - Harsha Yoga: 6th lord in 6th, 8th, or 12th house
 * - Sarala Yoga: 8th lord in 6th, 8th, or 12th house
 * - Vimala Yoga: 12th lord in 6th, 8th, or 12th house
 *
 * These yogas operate on the principle that "negative times negative equals positive"
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VipareetaRajaYogaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var analysis by remember { mutableStateOf<VipareetaRajaYogaCalculator.VipareetaRajaYogaAnalysis?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(StringKeyDosha.UI_OVERVIEW),
        stringResource(StringKeyDosha.UI_YOGAS),
        stringResource(StringKeyDosha.VIPAREETA_TAB_BENEFITS),
        stringResource(StringKeyDosha.UI_TIMING),
        stringResource(StringKeyDosha.VIPAREETA_TAB_FACTORS)
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
                VipareetaRajaYogaCalculator.analyzeVipareetaRajaYogas(chart)
            }
        } catch (_: Exception) { }
        isCalculating = false
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(StringKeyDosha.VIPAREETA_SCREEN_TITLE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = stringResource(StringKeyDosha.VIPAREETA_SUBTITLE),
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
        when {
            isCalculating -> VipareetaLoadingContent(modifier = Modifier.padding(paddingValues))
            chart == null || analysis == null -> VipareetaEmptyContent(modifier = Modifier.padding(paddingValues))
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        VipareetaTabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                    when (selectedTab) {
                        0 -> item { VipareetaOverviewSection(analysis!!) }
                        1 -> item { VipareetaYogasSection(analysis!!) }
                        2 -> item { BenefitsSection(analysis!!) }
                        3 -> item { VipareetaTimingSection(analysis!!) }
                        4 -> item { FactorsSection(analysis!!) }
                    }
                }
            }
        }
    }

    if (showInfoDialog) {
        VipareetaInfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun VipareetaTabSelector(
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
                    selectedContainerColor = AppTheme.LifeAreaSpiritual.copy(alpha = 0.15f),
                    selectedLabelColor = AppTheme.LifeAreaSpiritual,
                    containerColor = AppTheme.CardBackground,
                    labelColor = AppTheme.TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = AppTheme.BorderColor,
                    selectedBorderColor = AppTheme.LifeAreaSpiritual.copy(alpha = 0.3f),
                    enabled = true,
                    selected = selectedTab == index
                )
            )
        }
    }
}

@Composable
private fun VipareetaOverviewSection(analysis: VipareetaRajaYogaCalculator.VipareetaRajaYogaAnalysis) {
    val language = LocalLanguage.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        VipareetaStatusCard(analysis)
        VipareetaQuickStats(analysis)
        VipareetaSummaryCard(analysis)
    }
}

@Composable
private fun VipareetaStatusCard(analysis: VipareetaRajaYogaCalculator.VipareetaRajaYogaAnalysis) {
    val hasYogas = analysis.totalYogasFormed > 0
    val statusColor = when {
        analysis.totalYogasFormed >= 3 -> AppTheme.AccentGold
        analysis.totalYogasFormed >= 2 -> AppTheme.SuccessColor
        analysis.totalYogasFormed >= 1 -> AppTheme.AccentPrimary
        else -> AppTheme.TextMuted
    }

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
                    imageVector = if (hasYogas) Icons.Outlined.Stars else Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = when (analysis.totalYogasFormed) {
                    0 -> stringResource(StringKeyDosha.VIPAREETA_NO_YOGAS)
                    1 -> stringResource(StringKeyDosha.VIPAREETA_ONE_YOGA)
                    2 -> stringResource(StringKeyDosha.VIPAREETA_TWO_YOGAS)
                    else -> stringResource(StringKeyDosha.VIPAREETA_ALL_YOGAS)
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${stringResource(StringKeyDosha.VIPAREETA_STRENGTH)}: ${analysis.overallStrength.displayName}",
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary
            )

            if (hasYogas) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (analysis.harshaYoga.isFormed) {
                        YogaBadge(
                            name = "Harsha",
                            color = getYogaTypeColor(VipareetaRajaYogaCalculator.VipareetaYogaType.HARSHA)
                        )
                    }
                    if (analysis.saralaYoga.isFormed) {
                        YogaBadge(
                            name = "Sarala",
                            color = getYogaTypeColor(VipareetaRajaYogaCalculator.VipareetaYogaType.SARALA)
                        )
                    }
                    if (analysis.vimalaYoga.isFormed) {
                        YogaBadge(
                            name = "Vimala",
                            color = getYogaTypeColor(VipareetaRajaYogaCalculator.VipareetaYogaType.VIMALA)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun YogaBadge(name: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = color,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun getYogaTypeColor(type: VipareetaRajaYogaCalculator.VipareetaYogaType): Color {
    return when (type) {
        VipareetaRajaYogaCalculator.VipareetaYogaType.HARSHA -> AppTheme.SuccessColor
        VipareetaRajaYogaCalculator.VipareetaYogaType.SARALA -> AppTheme.AccentPrimary
        VipareetaRajaYogaCalculator.VipareetaYogaType.VIMALA -> AppTheme.LifeAreaSpiritual
    }
}

@Composable
private fun VipareetaQuickStats(analysis: VipareetaRajaYogaCalculator.VipareetaRajaYogaAnalysis) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        VipareetaStatCard(
            title = stringResource(StringKeyDosha.VIPAREETA_YOGAS_FORMED),
            value = "${analysis.totalYogasFormed}/3",
            color = AppTheme.AccentGold,
            modifier = Modifier.weight(1f)
        )
        VipareetaStatCard(
            title = stringResource(StringKeyDosha.VIPAREETA_EXCHANGES),
            value = "${analysis.dusthanaExchanges.size}",
            color = AppTheme.AccentPrimary,
            modifier = Modifier.weight(1f)
        )
        VipareetaStatCard(
            title = stringResource(StringKeyDosha.VIPAREETA_ACTIVATIONS),
            value = "${analysis.activationTimeline.size}",
            color = AppTheme.AccentTeal,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun VipareetaStatCard(
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
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun VipareetaSummaryCard(analysis: VipareetaRajaYogaCalculator.VipareetaRajaYogaAnalysis) {
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
                    tint = AppTheme.LifeAreaSpiritual,
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
        }
    }
}

@Composable
private fun VipareetaYogasSection(analysis: VipareetaRajaYogaCalculator.VipareetaRajaYogaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        VipareetaYogaCard(yoga = analysis.harshaYoga)
        VipareetaYogaCard(yoga = analysis.saralaYoga)
        VipareetaYogaCard(yoga = analysis.vimalaYoga)

        if (analysis.dusthanaExchanges.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKeyDosha.VIPAREETA_EXCHANGES_TITLE),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.AccentGold
            )
            analysis.dusthanaExchanges.forEach { exchange ->
                DusthanaExchangeCard(exchange)
            }
        }
    }
}

@Composable
private fun VipareetaYogaCard(yoga: VipareetaRajaYogaCalculator.VipareetaYoga) {
    val language = LocalLanguage.current
    var expanded by remember { mutableStateOf(yoga.isFormed) }
    val yogaColor = getYogaTypeColor(yoga.yogaType)
    val statusIcon = if (yoga.isFormed) Icons.Filled.CheckCircle else Icons.Filled.RemoveCircle

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = if (yoga.isFormed) AppTheme.CardBackground else AppTheme.CardBackground.copy(alpha = 0.6f)
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
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(yogaColor.copy(alpha = if (yoga.isFormed) 0.15f else 0.05f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = yoga.dusthanaLord.symbol,
                            fontSize = 24.sp,
                            color = if (yoga.isFormed) yogaColor else AppTheme.TextMuted
                        )
                    }
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = yoga.yogaType.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (yoga.isFormed) AppTheme.TextPrimary else AppTheme.TextMuted
                            )
                            Icon(
                                imageVector = statusIcon,
                                contentDescription = null,
                                tint = if (yoga.isFormed) AppTheme.SuccessColor else AppTheme.TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "${yoga.yogaType.houseLord}th Lord: ${yoga.dusthanaLord.displayName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (yoga.isFormed) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = yogaColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = yoga.strength.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = yogaColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
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

                    if (yoga.isFormed) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = AppTheme.ChipBackground
                            ) {
                                Text(
                                    text = "H${yoga.placedInHouse}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppTheme.TextSecondary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = AppTheme.ChipBackground
                            ) {
                                Text(
                                    text = yoga.placedInSign.getLocalizedName(language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppTheme.TextSecondary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = when {
                                    yoga.isExalted -> AppTheme.SuccessColor.copy(alpha = 0.15f)
                                    yoga.isDebilitated -> AppTheme.ErrorColor.copy(alpha = 0.15f)
                                    else -> AppTheme.ChipBackground
                                }
                            ) {
                                Text(
                                    text = when {
                                        yoga.isExalted -> stringResource(StringKey.DIGNITY_EXALTED_STATUS)
                                        yoga.isDebilitated -> stringResource(StringKey.DIGNITY_DEBILITATED_STATUS)
                                        yoga.isRetrograde -> stringResource(StringKey.PLANET_RETROGRADE)
                                        else -> stringResource(StringKey.DIGNITY_NEUTRAL_STATUS)
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = when {
                                        yoga.isExalted -> AppTheme.SuccessColor
                                        yoga.isDebilitated -> AppTheme.ErrorColor
                                        else -> AppTheme.TextSecondary
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Text(
                        text = yoga.interpretation,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )

                    if (yoga.isFormed && yoga.benefitsAreas.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(StringKeyDosha.VIPAREETA_BENEFITS),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.SuccessColor
                        )
                        yoga.benefitsAreas.take(3).forEach { benefit ->
                            Text(
                                text = "• $benefit",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DusthanaExchangeCard(exchange: VipareetaRajaYogaCalculator.DusthanaExchange) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.AccentGold.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${exchange.planet1.symbol} ↔ ${exchange.planet2.symbol}",
                    style = MaterialTheme.typography.titleMedium,
                    color = AppTheme.AccentGold
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = AppTheme.AccentGold.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "H${exchange.house1}-H${exchange.house2}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.AccentGold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = exchange.effect,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun BenefitsSection(analysis: VipareetaRajaYogaCalculator.VipareetaRajaYogaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (analysis.primaryBenefits.isEmpty()) {
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
                        imageVector = Icons.Outlined.TrendingUp,
                        contentDescription = null,
                        tint = AppTheme.TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(StringKeyDosha.VIPAREETA_NO_BENEFITS),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
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
                            imageVector = Icons.Outlined.TrendingUp,
                            contentDescription = null,
                            tint = AppTheme.SuccessColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(StringKeyDosha.VIPAREETA_PRIMARY_BENEFITS),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    analysis.primaryBenefits.forEach { benefit ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = AppTheme.SuccessColor.copy(alpha = 0.08f)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = AppTheme.SuccessColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = benefit,
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
private fun VipareetaTimingSection(analysis: VipareetaRajaYogaCalculator.VipareetaRajaYogaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (analysis.activationTimeline.isEmpty()) {
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
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = AppTheme.TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(StringKeyDosha.VIPAREETA_NO_TIMING),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
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
                            text = stringResource(StringKeyDosha.VIPAREETA_ACTIVATION_PERIODS),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    analysis.activationTimeline.forEach { period ->
                        ActivationPeriodRow(period)
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivationPeriodRow(period: VipareetaRajaYogaCalculator.ActivationPeriod) {
    val language = LocalLanguage.current
    val yogaColor = period.yogaType?.let { getYogaTypeColor(it) } ?: AppTheme.AccentPrimary

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = period.planet.symbol,
                        fontSize = 18.sp,
                        color = yogaColor
                    )
                    Text(
                        text = StringResources.get(com.astro.storm.core.common.StringKeyMatch.DASHA_LEVEL_MAHADASHA, language) + " - ${period.planet.getLocalizedName(language)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }
                period.yogaType?.let { type ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = yogaColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = type.displayName.split(" ").first(),
                            style = MaterialTheme.typography.labelSmall,
                            color = yogaColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = period.description,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted
            )
            if (period.expectedEffects.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    period.expectedEffects.take(2).forEach { effect ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = AppTheme.ChipBackground
                        ) {
                            Text(
                                text = effect,
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FactorsSection(analysis: VipareetaRajaYogaCalculator.VipareetaRajaYogaAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Enhancement Factors
        if (analysis.enhancementFactors.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.SuccessColor.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(StringKeyDosha.VIPAREETA_ENHANCEMENT),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.SuccessColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    analysis.enhancementFactors.forEach { factor ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("✓", color = AppTheme.SuccessColor, fontWeight = FontWeight.Bold)
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

        // Cancellation Factors
        if (analysis.cancellationFactors.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.WarningColor.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(StringKeyDosha.VIPAREETA_CANCELLATION),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.WarningColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    analysis.cancellationFactors.forEach { factor ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("⚠", color = AppTheme.WarningColor)
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

        // Empty state
        if (analysis.enhancementFactors.isEmpty() && analysis.cancellationFactors.isEmpty()) {
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
                    Text(
                        text = stringResource(StringKeyDosha.VIPAREETA_NO_FACTORS),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun VipareetaLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.LifeAreaSpiritual)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyDosha.VIPAREETA_ANALYZING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun VipareetaEmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Stars,
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
                text = stringResource(StringKeyDosha.VIPAREETA_NO_CHART_DESC),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun VipareetaInfoDialog(onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyDosha.VIPAREETA_ABOUT_TITLE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                text = stringResource(StringKeyDosha.VIPAREETA_ABOUT_DESC),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(stringResource(StringKeyDosha.BTN_GOT_IT), color = AppTheme.LifeAreaSpiritual)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}

