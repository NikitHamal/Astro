package com.astro.vajra.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringKeyDosha
import com.astro.vajra.core.common.StringKeyMatch
import com.astro.vajra.core.common.StringKeyUICommon
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.data.localization.LocalLanguage
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.ephemeris.jaimini.DrigDashaCalculator
import com.astro.vajra.ephemeris.jaimini.DrigDashaCalculator.AyurSpan
import com.astro.vajra.ephemeris.jaimini.DrigDashaCalculator.DrigAntardasha
import com.astro.vajra.ephemeris.jaimini.DrigDashaCalculator.DrigDashaAnalysis
import com.astro.vajra.ephemeris.jaimini.DrigDashaCalculator.DrigDashaPeriod
import com.astro.vajra.ephemeris.jaimini.DrigDashaCalculator.SthiraKarakaInfo
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.NeoVedicEmptyState
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import com.astro.vajra.ui.components.common.TabItem
import com.astro.vajra.ui.components.common.vedicCornerMarkers
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.NeoVedicFontSizes
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.theme.PoppinsFontFamily
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

private fun drigLocale(language: Language): Locale =
    if (language == Language.NEPALI) Locale.forLanguageTag("ne-NP") else Locale.ENGLISH

private fun drigLongDateFormatter(language: Language): DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMM dd, yyyy", drigLocale(language))

private fun drigMonthYearFormatter(language: Language): DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMM yyyy", drigLocale(language))

private fun drigShortDateFormatter(language: Language): DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMM yy", drigLocale(language))

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
            errorMessage = com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyUICommon.NO_DATA, language)
            return@LaunchedEffect
        }

        isLoading = true
        errorMessage = null

        withContext(Dispatchers.Default) {
            try {
                analysis = DrigDashaCalculator.calculateDrigDasha(chart)
            } catch (e: Exception) {
                errorMessage = e.message ?: com.astro.vajra.core.common.StringResources.get(com.astro.vajra.core.common.StringKeyDosha.ERROR_CALCULATION, language)
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
    NeoVedicPageHeader(
        title = stringResource(com.astro.vajra.core.common.StringKeyDosha.DRIG_DASHA_TITLE),
        subtitle = chartName,
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
                horizontal = com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding,
                vertical = com.astro.vajra.ui.theme.NeoVedicTokens.SpaceXS
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
    val dateFormatter = remember(language) { drigLongDateFormatter(language) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(NeoVedicTokens.ScreenPadding),
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
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                color = AppTheme.CardBackground,
                border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(NeoVedicTokens.ScreenPadding)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Description,
                            contentDescription = null,
                            tint = AppTheme.AccentPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(StringKeyDosha.UI_INTERPRETATION),
                            fontSize = NeoVedicFontSizes.S16,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary,
                            fontFamily = CinzelDecorativeFamily
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(
                            com.astro.vajra.core.common.StringKeyDosha.DRIG_RUDRA_SIGN_DESC,
                            analysis.longevitySpan.displayName
                        ) + " (${analysis.longevitySpan.minYears}-${analysis.longevitySpan.maxYears} " + stringResource(com.astro.vajra.core.common.StringKeyDosha.DRIG_YEARS) + "). " +
                                stringResource(com.astro.vajra.core.common.StringKeyDosha.DRIG_CURRENT_DASHA) + " is ruled by ${analysis.currentDasha?.signLord?.getLocalizedName(language) ?: stringResource(com.astro.vajra.core.common.StringKeyMatch.MISC_UNKNOWN)}, " +
                                stringResource(com.astro.vajra.core.common.StringKeyUICommon.FOR) + " ${analysis.currentDasha?.interpretation ?: stringResource(com.astro.vajra.core.common.StringKeyAnalysis.DASHA_KW_GENERAL)}.",
                        fontSize = NeoVedicFontSizes.S13,
                        color = AppTheme.TextSecondary,
                        lineHeight = 22.sp,
                        fontFamily = PoppinsFontFamily
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

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .vedicCornerMarkers(color = spanColor),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        color = spanColor.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, spanColor.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
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
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(StringKeyDosha.DRIG_LONGEVITY_CLASSIFICATION),
                fontSize = NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted,
                fontWeight = FontWeight.Medium,
                fontFamily = SpaceGroteskFamily,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = longevitySpan.name.replace("_", " "),
                fontSize = NeoVedicFontSizes.S20,
                fontWeight = FontWeight.Bold,
                color = spanColor,
                fontFamily = CinzelDecorativeFamily
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${longevitySpan.minYears}-${longevitySpan.maxYears} " + stringResource(StringKeyDosha.DRIG_YEARS),
                fontSize = NeoVedicFontSizes.S15,
                color = AppTheme.TextSecondary,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PoppinsFontFamily
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = longevitySpan.description,
                fontSize = NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center,
                fontFamily = PoppinsFontFamily
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

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(NeoVedicTokens.ScreenPadding)
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
                            .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius))
                            .background(signColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = period.sign.getLocalizedName(language).take(2),
                            fontSize = NeoVedicFontSizes.S16,
                            fontWeight = FontWeight.Bold,
                            color = signColor
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = stringResource(StringKeyDosha.DRIG_CURRENT_DASHA),
                            fontSize = NeoVedicFontSizes.S12,
                            color = AppTheme.TextMuted,
                            fontFamily = SpaceGroteskFamily
                        )
                        Text(
                            text = period.sign.getLocalizedName(language),
                            fontSize = NeoVedicFontSizes.S18,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary,
                            fontFamily = CinzelDecorativeFamily
                        )
                    }
                }

                // Period Badge
                Surface(
                    shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
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
                        fontSize = NeoVedicFontSizes.S10,
                        fontWeight = FontWeight.SemiBold,
                        color = if (period.isMarakaPeriod)
                            AppTheme.WarningColor
                        else
                            AppTheme.SuccessColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontFamily = SpaceGroteskFamily
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
                    fontSize = NeoVedicFontSizes.S11,
                    color = AppTheme.TextMuted,
                    fontFamily = SpaceGroteskFamily
                )
                Text(
                    text = "${period.durationYears} " + stringResource(StringKeyDosha.DRIG_YEARS),
                    fontSize = NeoVedicFontSizes.S11,
                    fontWeight = FontWeight.SemiBold,
                    color = signColor,
                    fontFamily = SpaceGroteskFamily
                )
                Text(
                    text = period.endDate.format(dateFormatter),
                    fontSize = NeoVedicFontSizes.S11,
                    color = AppTheme.TextMuted,
                    fontFamily = SpaceGroteskFamily
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)),
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
                fontSize = NeoVedicFontSizes.S11,
                color = AppTheme.TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontFamily = PoppinsFontFamily
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
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = stringResource(StringKeyDosha.DRIG_TRIMURTI_SIGNS),
                fontSize = NeoVedicFontSizes.S16,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                fontFamily = CinzelDecorativeFamily
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
            fontSize = NeoVedicFontSizes.S11,
            fontWeight = FontWeight.Bold,
            color = color,
            fontFamily = SpaceGroteskFamily
        )

        Text(
            text = sign.getLocalizedName(language),
            fontSize = NeoVedicFontSizes.S14,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            fontFamily = PoppinsFontFamily
        )

        Text(
            text = description,
            fontSize = NeoVedicFontSizes.S10,
            color = AppTheme.TextMuted,
            textAlign = TextAlign.Center,
            fontFamily = PoppinsFontFamily
        )
    }
}

@Composable
private fun DashaPeriodTab(
    analysis: DrigDashaAnalysis,
    language: Language
) {
    val dateFormatter = remember(language) { drigMonthYearFormatter(language) }
    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = stringResource(StringKeyDosha.DRIG_DASHA_SEQUENCE),
                fontSize = NeoVedicFontSizes.S16,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp),
                fontFamily = CinzelDecorativeFamily
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

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isCurrentPeriod) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
            )
            .clickable { onExpandClick() },
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        color = if (isCurrentPeriod)
            AppTheme.AccentPrimary.copy(alpha = 0.05f)
        else
            AppTheme.CardBackground
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
                        .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius))
                        .background(signColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = period.sign.getLocalizedName(language).take(2),
                        fontSize = NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Bold,
                        color = signColor,
                        fontFamily = SpaceGroteskFamily
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = period.sign.getLocalizedName(language),
                            fontSize = NeoVedicFontSizes.S15,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary,
                            fontFamily = CinzelDecorativeFamily
                        )

                        if (isCurrentPeriod) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                                color = AppTheme.AccentPrimary.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = stringResource(StringKeyDosha.UI_CURRENT).uppercase(),
                                    fontSize = NeoVedicFontSizes.S9,
                                    color = AppTheme.AccentPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = SpaceGroteskFamily
                                )
                            }
                        }
                    }

                    Text(
                        text = "${period.startDate.format(dateFormatter)} - ${period.endDate.format(dateFormatter)}",
                        fontSize = NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted,
                        fontFamily = PoppinsFontFamily
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (period.isMarakaPeriod) {
                        Surface(
                            shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                            color = AppTheme.WarningColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = stringResource(StringKeyDosha.DRIG_MARAKA).uppercase(),
                                fontSize = NeoVedicFontSizes.S9,
                                color = AppTheme.WarningColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontWeight = FontWeight.Bold,
                                fontFamily = SpaceGroteskFamily
                            )
                        }
                    }

                    Text(
                        text = "${period.durationYears}y",
                        fontSize = NeoVedicFontSizes.S12,
                        fontWeight = FontWeight.SemiBold,
                        color = signColor,
                        fontFamily = SpaceGroteskFamily
                    )

                    Icon(
                        imageVector = if (isExpanded)
                            Icons.Default.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) 
                            stringResource(com.astro.vajra.core.common.StringKeyMatch.MISC_COLLAPSE) 
                        else 
                            stringResource(com.astro.vajra.core.common.StringKeyMatch.MISC_EXPAND),
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
                        fontSize = NeoVedicFontSizes.S12,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextMuted,
                        fontFamily = SpaceGroteskFamily
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
    val dateFormatter = remember(language) { drigShortDateFormatter(language) }
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
                fontSize = NeoVedicFontSizes.S11,
                fontWeight = FontWeight.Bold,
                color = signColor,
                fontFamily = SpaceGroteskFamily
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = antardasha.sign.getLocalizedName(language),
            fontSize = NeoVedicFontSizes.S13,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextSecondary,
            modifier = Modifier.weight(1f),
            fontFamily = PoppinsFontFamily
        )

        Text(
            text = "${antardasha.startDate.format(dateFormatter)} - ${antardasha.endDate.format(dateFormatter)}",
            fontSize = NeoVedicFontSizes.S11,
            color = AppTheme.TextMuted,
            fontFamily = SpaceGroteskFamily
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
        contentPadding = PaddingValues(com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
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
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
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
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding)
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
    val dateFormatter = remember(language) { drigMonthYearFormatter(language) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
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
                    .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius))
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
        contentPadding = PaddingValues(com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
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
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
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
                    .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius))
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
                fontSize = NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted,
                fontFamily = PoppinsFontFamily
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
        NeoVedicEmptyState(
            title = stringResource(StringKeyDosha.ERROR_CALCULATION),
            subtitle = message,
            icon = Icons.Outlined.Error
        )
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




