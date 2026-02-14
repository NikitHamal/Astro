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
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material.icons.outlined.Brightness7
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Stars
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeySaham
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyShadbala
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.SahamCalculator
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.compose.ui.text.style.TextOverflow

/**
 * Saham (Arabic Parts/Lots) Analysis Screen
 *
 * Displays comprehensive analysis of Arabic Parts including:
 * - 20 Major and Minor Sahams covering all life areas
 * - Day/Night birth formula reversal per Tajika tradition
 * - House placement and lord analysis
 * - Nakshatra position for fine-tuned timing
 * - Strength assessment based on lord dignity
 *
 * Vedic References: Tajika Neelakanthi, Prashna Marga
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SahamScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var analysis by remember { mutableStateOf<SahamCalculator.SahamAnalysis?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(StringKeyShadbala.SAHAM_TAB_OVERVIEW),
        stringResource(StringKeyShadbala.SAHAM_TAB_MAJOR),
        stringResource(StringKeyShadbala.SAHAM_TAB_MINOR)
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
                SahamCalculator.analyzeSahams(chart)
            }
        } catch (_: Exception) { }
        isCalculating = false
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyShadbala.SAHAM_TITLE),
                subtitle = stringResource(StringKeyShadbala.SAHAM_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeySaham.SHOW_INFO),
                            tint = AppTheme.TextSecondary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            isCalculating -> SahamLoadingContent(modifier = Modifier.padding(paddingValues))
            chart == null || analysis == null -> SahamEmptyContent(modifier = Modifier.padding(paddingValues))
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        SahamTabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                    when (selectedTab) {
                        0 -> item { SahamOverviewSection(analysis!!) }
                        1 -> item { SahamListSection(analysis!!.majorSahams, stringResource(StringKeyShadbala.SAHAM_TAB_MAJOR)) }
                        2 -> item { SahamListSection(analysis!!.minorSahams, stringResource(StringKeyShadbala.SAHAM_TAB_MINOR)) }
                    }
                }
            }
        }
    }

    if (showInfoDialog) {
        SahamInfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun SahamTabSelector(
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
private fun SahamOverviewSection(analysis: SahamCalculator.SahamAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SahamScoreCard(analysis)
        SahamQuickStats(analysis)
        SahamBirthTypeCard(analysis.isDayBirth)
        if (analysis.keyInsights.isNotEmpty()) {
            SahamInsightsCard(analysis.keyInsights)
        }
        if (analysis.recommendations.isNotEmpty()) {
            SahamRecommendationsCard(analysis.recommendations)
        }
    }
}

@Composable
private fun SahamScoreCard(analysis: SahamCalculator.SahamAnalysis) {
    val scoreColor = when {
        analysis.overallScore >= 70 -> AppTheme.SuccessColor
        analysis.overallScore >= 50 -> AppTheme.AccentGold
        analysis.overallScore >= 30 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = scoreColor.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
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
                    .background(scoreColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Stars,
                    contentDescription = null,
                    tint = scoreColor,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${analysis.overallScore.toInt()}%",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = scoreColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(StringKeyShadbala.SAHAM_STRENGTH),
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { (analysis.overallScore / 100f).toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                color = scoreColor,
                trackColor = scoreColor.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )

            // Show strongest and weakest Saham
            analysis.strongestSaham?.let { strongest ->
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(StringKeySaham.STRONGEST),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                        Text(
                            text = strongest.type.displayName.split(" ")[0],
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.SuccessColor
                        )
                    }
                    analysis.weakestSaham?.let { weakest ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(StringKeySaham.NEEDS_FOCUS),
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                            Text(
                                text = weakest.type.displayName.split(" ")[0],
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = AppTheme.WarningColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SahamQuickStats(analysis: SahamCalculator.SahamAnalysis) {
    val activatedCount = analysis.sahams.count { it.isActivated }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SahamStatCard(
            title = stringResource(StringKeyShadbala.SAHAM_TAB_MAJOR),
            value = "${analysis.majorSahams.size}",
            color = AppTheme.AccentPrimary,
            modifier = Modifier.weight(1f)
        )
        SahamStatCard(
            title = stringResource(StringKeyShadbala.SAHAM_TAB_MINOR),
            value = "${analysis.minorSahams.size}",
            color = AppTheme.AccentTeal,
            modifier = Modifier.weight(1f)
        )
        SahamStatCard(
            title = stringResource(StringKeySaham.ACTIVATED),
            value = "$activatedCount",
            color = AppTheme.SuccessColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SahamStatCard(
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
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SahamBirthTypeCard(isDayBirth: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppTheme.AccentGold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isDayBirth) Icons.Outlined.Brightness7 else Icons.Outlined.Brightness4,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isDayBirth) stringResource(StringKeySaham.DAY_BIRTH) else stringResource(StringKeySaham.NIGHT_BIRTH),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = stringResource(StringKeySaham.FORMULA_ADJUSTED_DESC),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun SahamInsightsCard(insights: List<String>) {
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
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyAnalysis.UI_KEY_INSIGHTS),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            insights.take(5).forEach { insight ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("â€¢", color = AppTheme.AccentGold, fontWeight = FontWeight.Bold)
                    Text(
                        text = insight,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SahamRecommendationsCard(recommendations: List<String>) {
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
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = AppTheme.AccentTeal,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeySaham.RECOMMENDATIONS),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            recommendations.take(5).forEach { recommendation ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("â†’", color = AppTheme.AccentTeal, fontWeight = FontWeight.Bold)
                    Text(
                        text = recommendation,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SahamListSection(
    sahams: List<SahamCalculator.SahamResult>,
    sectionTitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (sahams.isEmpty()) {
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
                        text = stringResource(StringKeySaham.NO_SAHAMS_TYPE_FOUND, sectionTitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            sahams.forEach { saham ->
                SahamCard(saham = saham)
            }
        }
    }
}

@Composable
private fun SahamCard(saham: SahamCalculator.SahamResult) {
    val language = LocalLanguage.current
    var expanded by remember { mutableStateOf(false) }
    val strengthColor = when (saham.strength) {
        SahamCalculator.SahamStrength.EXCELLENT -> AppTheme.SuccessColor
        SahamCalculator.SahamStrength.STRONG -> AppTheme.AccentTeal
        SahamCalculator.SahamStrength.MODERATE -> AppTheme.AccentGold
        SahamCalculator.SahamStrength.WEAK -> AppTheme.WarningColor
        SahamCalculator.SahamStrength.AFFLICTED -> AppTheme.ErrorColor
    }

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
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(strengthColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = saham.lord.symbol,
                            fontSize = 20.sp,
                            color = strengthColor
                        )
                    }
                    Column {
                        Text(
                            text = saham.type.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = saham.type.nepaliName,
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
                        color = strengthColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = saham.strength.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = strengthColor,
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

                    // Position details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        DetailItem(
                            label = stringResource(StringKeyShadbala.SAHAM_SIGN),
                            value = saham.sign.getLocalizedName(language)
                        )
                        DetailItem(
                            label = stringResource(StringKeyShadbala.SAHAM_HOUSE),
                            value = "${stringResource(StringKey.YOGA_HOUSE_PREFIX)}${saham.house}"
                        )
                        DetailItem(
                            label = stringResource(StringKeyShadbala.SAHAM_LORD),
                            value = saham.lord.displayName
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nakshatra
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(StringKeyShadbala.SAHAM_NAKSHATRA),
                            style = MaterialTheme.typography.labelMedium,
                            color = AppTheme.TextMuted
                        )
                        Text(
                            text = "${saham.nakshatra.displayName} (Pada ${saham.nakshatraPada})",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Formula
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(StringKeyShadbala.SAHAM_FORMULA),
                            style = MaterialTheme.typography.labelMedium,
                            color = AppTheme.TextMuted
                        )
                        Text(
                            text = saham.formula,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.AccentGold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Degree in sign
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(StringKeyShadbala.SAHAM_POSITION),
                            style = MaterialTheme.typography.labelMedium,
                            color = AppTheme.TextMuted
                        )
                        Text(
                            text = "${String.format("%.2f", saham.degreeInSign)}Â°",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextSecondary
                        )
                    }

                    // Activation status
                    if (saham.isActivated) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = AppTheme.SuccessColor.copy(alpha = 0.1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("âœ“", color = AppTheme.SuccessColor, fontWeight = FontWeight.Bold)
                                Text(
                                    text = stringResource(StringKeySaham.ACTIVATED),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppTheme.SuccessColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Interpretation
                    Text(
                        text = stringResource(StringKeyShadbala.SAHAM_INTERPRETATION),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = saham.interpretation,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )

                    // Description
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = saham.type.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun SahamLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = AppTheme.AccentPrimary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyShadbala.SAHAM_ANALYZING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun SahamEmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(AppTheme.CardBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Stars,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyShadbala.SAHAM_UNABLE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKeySaham.SELECT_CHART_MESSAGE),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SahamInfoDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(StringKeyShadbala.SAHAM_INFO_TITLE),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyShadbala.SAHAM_INFO_DESC),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyShadbala.SAHAM_VEDIC_REF),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { onDismiss() },
                    shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = AppTheme.AccentPrimary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = stringResource(StringKeyShadbala.COMMON_GOT_IT),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.AccentPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}





