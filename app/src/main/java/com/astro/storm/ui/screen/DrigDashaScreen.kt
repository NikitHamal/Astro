package com.astro.storm.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.NeoVedicPageHeader
import com.astro.storm.ui.components.common.TabItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringKeyUICommon
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.StringKeyUIExtra
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.jaimini.DrigDashaCalculator
import com.astro.storm.ephemeris.jaimini.DrigDashaCalculator.DrigDashaAnalysis
import com.astro.storm.ephemeris.jaimini.DrigDashaCalculator.AyurSpan
import com.astro.storm.ephemeris.jaimini.DrigDashaCalculator.DrigDashaPeriod
import com.astro.storm.ephemeris.jaimini.DrigDashaCalculator.DrigAntardasha
import com.astro.storm.ephemeris.jaimini.DrigDashaCalculator.SthiraKarakaInfo
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import androidx.compose.ui.text.style.TextOverflow

/**
 * DrigDashaScreen - Jaimini Drig (Sthira) Dasha System Screen
 *
 * Displays the Drig Dasha system for longevity/Ayurdaya analysis,
 * including longevity spans, Maraka periods, and Antardasha calculations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrigDashaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    val asOf = remember(chart) { LocalDateTime.now(resolveZoneId(chart?.birthData?.timezone)) }
    var analysis by remember { mutableStateOf<DrigDashaAnalysis?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Calculate Drig Dasha Analysis
    LaunchedEffect(chart) {
        if (chart == null) {
            isLoading = false
            errorMessage = com.astro.storm.core.common.StringResources.get(com.astro.storm.core.common.StringKeyUICommon.NO_DATA, language)
            return@LaunchedEffect
        }

        isLoading = true
        errorMessage = null

        withContext(Dispatchers.Default) {
            try {
                analysis = DrigDashaCalculator.calculateDrigDasha(chart)
            } catch (e: Exception) {
                errorMessage = e.message ?: com.astro.storm.core.common.StringResources.get(com.astro.storm.core.common.StringKeyDosha.ERROR_CALCULATION, language)
            }
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            DrigDashaTopBar(
                chartName = chart?.birthData?.name ?: stringResource(StringKeyMatch.MISC_UNKNOWN),
                analysis = analysis,
                isLoading = isLoading,
                onBack = onBack
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { padding ->
        when {
            isLoading -> LoadingStateDD(modifier = Modifier.padding(padding))
            errorMessage != null -> ErrorStateDD(errorMessage!!, modifier = Modifier.padding(padding))
            analysis != null -> DrigDashaContent(
                analysis = analysis!!,
                language = language,
                asOf = asOf,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun DrigDashaTopBar(
    chartName: String,
    analysis: DrigDashaAnalysis?,
    isLoading: Boolean,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    val subtitle = when {
        isLoading -> stringResource(StringKey.DASHA_CALCULATING)
        analysis != null -> buildString {
            analysis.currentDasha?.let { md ->
                append(md.sign.getLocalizedName(language))
                append(StringResources.get(StringKeyUIExtra.BULLET_SPACE, language))
            }
            append(chartName)
        }
        else -> chartName
    }
    NeoVedicPageHeader(
        title = stringResource(com.astro.storm.core.common.StringKeyDosha.DRIG_DASHA_TITLE),
        subtitle = subtitle,
        onBack = onBack
    )
}

@Composable
private fun DrigDashaContent(
    analysis: DrigDashaAnalysis,
    language: Language,
    asOf: LocalDateTime,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val overviewTabTitle = stringResource(StringKeyDosha.DRIG_TAB_OVERVIEW)
    val periodsTabTitle = stringResource(StringKeyDosha.DRIG_TAB_PERIODS)
    val marakaTabTitle = stringResource(StringKeyDosha.DRIG_TAB_MARAKA)
    val karakasTabTitle = stringResource(StringKeyDosha.DRIG_TAB_KARAKAS)

    val tabs = listOf(
        overviewTabTitle,
        periodsTabTitle,
        marakaTabTitle,
        karakasTabTitle
    )

    Column(modifier = modifier.fillMaxSize()) {
        // Tabs
        ModernPillTabRow(
            tabs = tabs.mapIndexed { index, title ->
                TabItem(
                    title = title,
                    accentColor = if (selectedTab == index) AppTheme.AccentPrimary else Color.Unspecified
                )
            },
            selectedIndex = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier.padding(
                horizontal = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding,
                vertical = com.astro.storm.ui.theme.NeoVedicTokens.SpaceXS
            )
        )

        // Tab Content
        when (selectedTab) {
            0 -> OverviewTab(analysis, language, asOf)
            1 -> DashaPeriodTab(analysis, language)
            2 -> MarakaAnalysisTab(analysis, language)
            3 -> SthiraKarakasTab(analysis, language)
        }
    }
}

@Composable
private fun OverviewTab(
    analysis: DrigDashaAnalysis,
    language: Language,
    asOf: LocalDateTime
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM dd, yyyy") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Longevity Card
        item {
            LongevityCard(analysis.longevitySpan)
        }

        // Current Period Card
        analysis.currentDasha?.let { currentDasha ->
            item {
                CurrentPeriodCard(
                    period = currentDasha,
                    language = language,
                    dateFormatter = dateFormatter,
                    asOf = asOf
                )
            }
        }

        // Key Signs Card
        item {
            KeySignsCard(
                brahmaSign = analysis.brahmaSign,
                rudraSign = analysis.rudraSign,
                maheshwaraSign = analysis.maheshwaraSign,
                language = language
            )
        }

        // Interpretation
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Description,
                            contentDescription = null,
                            tint = AppTheme.AccentPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(StringKeyDosha.UI_INTERPRETATION),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(
                            com.astro.storm.core.common.StringKeyDosha.DRIG_RUDRA_SIGN_DESC,
                            analysis.longevitySpan.displayName
                        ) + " (${analysis.longevitySpan.minYears}-${analysis.longevitySpan.maxYears} " + stringResource(com.astro.storm.core.common.StringKeyDosha.DRIG_YEARS) + "). " +
                                stringResource(com.astro.storm.core.common.StringKeyDosha.DRIG_CURRENT_DASHA) + " is ruled by ${analysis.currentDasha?.signLord?.getLocalizedName(language) ?: stringResource(com.astro.storm.core.common.StringKeyMatch.MISC_UNKNOWN)}, " +
                                stringResource(com.astro.storm.core.common.StringKeyUICommon.FOR) + " ${analysis.currentDasha?.interpretation ?: stringResource(com.astro.storm.core.common.StringKeyAnalysis.DASHA_KW_GENERAL)}.",
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
private fun LongevityCard(
    longevitySpan: AyurSpan
) {
    val spanColor = when (longevitySpan) {
        AyurSpan.BALA_ARISHTA -> AppTheme.ErrorColor
        AyurSpan.ALPAYU -> AppTheme.WarningColor
        AyurSpan.MADHYAYU -> AppTheme.AccentGold
        AyurSpan.PURNAYU -> AppTheme.SuccessColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = spanColor.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                spanColor.copy(alpha = 0.3f),
                                spanColor.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .border(2.dp, spanColor.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.HealthAndSafety,
                    contentDescription = null,
                    tint = spanColor,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(StringKeyDosha.DRIG_LONGEVITY_CLASSIFICATION),
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.TextMuted
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = longevitySpan.name.replace("_", " "),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = spanColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${longevitySpan.minYears}-${longevitySpan.maxYears} " + stringResource(StringKeyDosha.DRIG_YEARS),
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = longevitySpan.description,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CurrentPeriodCard(
    period: DrigDashaPeriod,
    language: Language,
    dateFormatter: DateTimeFormatter,
    asOf: LocalDateTime
) {
    val now = asOf
    val totalDays = ChronoUnit.DAYS.between(period.startDate, period.endDate).toFloat()
    val elapsedDays = ChronoUnit.DAYS.between(period.startDate, now).toFloat()
    val progress = (elapsedDays / totalDays).coerceIn(0f, 1f)

    val signColor = getSignColor(period.sign)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius))
                            .background(signColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = period.sign.getLocalizedName(language).take(2),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = signColor
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = stringResource(StringKeyDosha.DRIG_CURRENT_DASHA),
                            style = MaterialTheme.typography.labelMedium,
                            color = AppTheme.TextMuted
                        )
                        Text(
                            text = period.sign.getLocalizedName(language),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                    }
                }

                // Period Badge
                Surface(
                    shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = if (period.isMarakaPeriod)
                        AppTheme.WarningColor.copy(alpha = 0.15f)
                    else
                        AppTheme.SuccessColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (period.isMarakaPeriod)
                            stringResource(StringKeyDosha.DRIG_MARAKA)
                        else
                            stringResource(StringKeyDosha.DRIG_NORMAL),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = if (period.isMarakaPeriod)
                            AppTheme.WarningColor
                        else
                            AppTheme.SuccessColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Duration
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
                    text = "${period.durationYears} " + stringResource(StringKeyDosha.DRIG_YEARS),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = signColor
                )
                Text(
                    text = period.endDate.format(dateFormatter),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                color = signColor,
                trackColor = AppTheme.DividerColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Remaining time
            val remainingDays = ChronoUnit.DAYS.between(now, period.endDate)
            val remainingYears = remainingDays / 365
            val remainingMonths = (remainingDays % 365) / 30

            Text(
                text = if (remainingYears > 0)
                    stringResource(StringKeyDosha.DRIG_YEARS_REMAINING, remainingYears, remainingMonths)
                else
                    stringResource(StringKeyDosha.DRIG_MONTHS_REMAINING, remainingMonths),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun resolveZoneId(timezone: String?): ZoneId {
    if (timezone.isNullOrBlank()) return ZoneId.systemDefault()
    return try {
        ZoneId.of(timezone.trim())
    } catch (_: DateTimeException) {
        val normalized = timezone.trim()
            .replace("UTC", "", ignoreCase = true)
            .replace("GMT", "", ignoreCase = true)
            .trim()
        if (normalized.isNotEmpty()) {
            runCatching { ZoneId.of("UTC$normalized") }.getOrElse { ZoneId.systemDefault() }
        } else {
            ZoneId.systemDefault()
        }
    }
}

@Composable
private fun KeySignsCard(
    brahmaSign: ZodiacSign,
    rudraSign: ZodiacSign,
    maheshwaraSign: ZodiacSign,
    language: Language
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = stringResource(StringKeyDosha.DRIG_TRIMURTI_SIGNS),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TrimurtiSignItem(
                    title = stringResource(StringKeyDosha.DRIG_BRAHMA),
                    sign = brahmaSign,
                    language = language,
                    icon = Icons.Outlined.AutoAwesome,
                    color = AppTheme.AccentGold,
                    description = stringResource(StringKeyDosha.DRIG_BRAHMA_DESC)
                )
                TrimurtiSignItem(
                    title = stringResource(StringKeyDosha.DRIG_RUDRA),
                    sign = rudraSign,
                    language = language,
                    icon = Icons.Outlined.Warning,
                    color = AppTheme.WarningColor,
                    description = stringResource(StringKeyDosha.DRIG_RUDRA_DESC)
                )
                TrimurtiSignItem(
                    title = stringResource(StringKeyDosha.DRIG_MAHESHWARA),
                    sign = maheshwaraSign,
                    language = language,
                    icon = Icons.Outlined.SelfImprovement,
                    color = AppTheme.LifeAreaSpiritual,
                    description = stringResource(StringKeyDosha.DRIG_MAHESHWARA_DESC)
                )
            }
        }
    }
}

@Composable
private fun TrimurtiSignItem(
    title: String,
    sign: ZodiacSign,
    language: Language,
    icon: ImageVector,
    color: Color,
    description: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Text(
            text = sign.getLocalizedName(language),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary
        )

        Text(
            text = description,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DashaPeriodTab(
    analysis: DrigDashaAnalysis,
    language: Language
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM yyyy") }
    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = stringResource(StringKeyDosha.DRIG_DASHA_SEQUENCE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        itemsIndexed(analysis.drigDashaSequence) { index, period ->
            DashaPeriodCard(
                period = period,
                language = language,
                dateFormatter = dateFormatter,
                isExpanded = expandedIndex == index,
                isCurrentPeriod = period == analysis.currentDasha,
                onExpandClick = {
                    expandedIndex = if (expandedIndex == index) null else index
                }
            )
        }
    }
}

@Composable
private fun DashaPeriodCard(
    period: DrigDashaPeriod,
    language: Language,
    dateFormatter: DateTimeFormatter,
    isExpanded: Boolean,
    isCurrentPeriod: Boolean,
    onExpandClick: () -> Unit
) {
    val signColor = getSignColor(period.sign)
    val borderColor = if (isCurrentPeriod)
        AppTheme.AccentPrimary
    else
        Color.Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isCurrentPeriod) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
            )
            .clickable { onExpandClick() },
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentPeriod)
                AppTheme.AccentPrimary.copy(alpha = 0.05f)
            else
                AppTheme.CardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius))
                        .background(signColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = period.sign.getLocalizedName(language).take(2),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = signColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = period.sign.getLocalizedName(language),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )

                        if (isCurrentPeriod) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                                color = AppTheme.AccentPrimary.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = stringResource(StringKeyDosha.UI_CURRENT),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppTheme.AccentPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = "${period.startDate.format(dateFormatter)} - ${period.endDate.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (period.isMarakaPeriod) {
                        Surface(
                            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = AppTheme.WarningColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = stringResource(StringKeyDosha.DRIG_MARAKA),
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.WarningColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "${period.durationYears}y",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = signColor
                    )

                    Icon(
                        imageVector = if (isExpanded)
                            Icons.Default.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) 
                            stringResource(com.astro.storm.core.common.StringKeyMatch.MISC_COLLAPSE) 
                        else 
                            stringResource(com.astro.storm.core.common.StringKeyMatch.MISC_EXPAND),
                        tint = AppTheme.TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Expanded content - Antardashas
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(StringKeyDosha.DRIG_ANTARDASHAS),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    period.criticalSubPeriods.forEach { antardasha ->
                        AntardashaRow(antardasha, language)
                    }
                }
            }
        }
    }
}

@Composable
private fun AntardashaRow(
    antardasha: DrigAntardasha,
    language: Language
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM yy") }
    val signColor = getSignColor(antardasha.sign)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(signColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = antardasha.sign.getLocalizedName(language).take(1),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = signColor
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = antardasha.sign.getLocalizedName(language),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextSecondary,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${antardasha.startDate.format(dateFormatter)} - ${antardasha.endDate.format(dateFormatter)}",
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun MarakaAnalysisTab(
    analysis: DrigDashaAnalysis,
    language: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.WarningColor.copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = null,
                        tint = AppTheme.WarningColor,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(StringKeyDosha.DRIG_MARAKA_ANALYSIS_TITLE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = stringResource(StringKeyDosha.DRIG_MARAKA_ANALYSIS_DESC),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }

        // Maraka Periods
        val marakaPeriods = analysis.drigDashaSequence.filter { it.isMarakaPeriod }
        if (marakaPeriods.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = AppTheme.SuccessColor,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(StringKeyDosha.DRIG_NO_MARAKA_DETECTED),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(marakaPeriods) { period ->
                MarakaPeriodCard(period, language)
            }
        }

        // Rudra Sign Analysis
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.HealthAndSafety,
                            contentDescription = null,
                            tint = AppTheme.WarningColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(StringKeyDosha.DRIG_RUDRA_SIGN_ANALYSIS),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(
                            StringKeyDosha.DRIG_RUDRA_SIGN_DESC,
                            analysis.rudraSign.getLocalizedName(language)
                        ),
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
private fun MarakaPeriodCard(
    period: DrigDashaPeriod,
    language: Language
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM yyyy") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius))
                    .background(AppTheme.WarningColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = AppTheme.WarningColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = period.sign.getLocalizedName(language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = "${period.startDate.format(dateFormatter)} - ${period.endDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }

            Text(
                text = "${period.durationYears} years",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.WarningColor
            )
        }
    }
}

@Composable
private fun SthiraKarakasTab(
    analysis: DrigDashaAnalysis,
    language: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.AccentPrimary.copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Stars,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(StringKeyDosha.DRIG_STHIRA_KARAKAS_TITLE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = stringResource(StringKeyDosha.DRIG_STHIRA_KARAKAS_DESC),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }

        items(analysis.sthiraKarakas) { karakaInfo ->
            SthiraKarakaCard(karakaInfo, language)
        }
    }
}

@Composable
private fun SthiraKarakaCard(
    karakaInfo: SthiraKarakaInfo,
    language: Language
) {
    val signColor = getSignColor(karakaInfo.position.sign)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius))
                    .background(signColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = karakaInfo.position.sign.getLocalizedName(language).take(2),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = signColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = karakaInfo.sthiraKaraka.name.replace("_", " "),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = "${karakaInfo.position.sign.getLocalizedName(language)} (${karakaInfo.position.planet.displayName})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary
                )
                Text(
                    text = karakaInfo.sthiraKaraka.signification,
                    style = MaterialTheme.typography.bodySmall,
                    color = signColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingStateDD(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyDosha.DRIG_CALCULATING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun ErrorStateDD(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Error,
                contentDescription = null,
                tint = AppTheme.ErrorColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
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
private fun getSignColor(sign: ZodiacSign): Color {
    return when (sign.element) {
        "Fire" -> AppTheme.PlanetMars
        "Earth" -> AppTheme.PlanetMercury
        "Air" -> AppTheme.PlanetVenus
        "Water" -> AppTheme.PlanetMoon
        else -> AppTheme.PlanetMars
    }
}



