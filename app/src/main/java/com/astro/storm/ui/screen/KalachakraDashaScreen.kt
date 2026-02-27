package com.astro.storm.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.astro.storm.core.common.BikramSambatConverter
import com.astro.storm.core.common.DateSystem
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.data.localization.DateFormat
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.formatLocalized
import com.astro.storm.data.localization.formatRemainingDaysLocalized
import com.astro.storm.data.localization.formatRemainingYearsLocalized
import com.astro.storm.data.localization.localized
import com.astro.storm.data.localization.localizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.ephemeris.KalachakraDashaCalculator
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
import com.astro.storm.ui.viewmodel.KalachakraDashaUiState
import com.astro.storm.ui.viewmodel.KalachakraDashaViewModel
import java.time.DateTimeException
import java.time.LocalDate
import java.time.ZoneId

/**
 * Kalachakra Dasha Screen - Production-grade UI
 *
 * Displays the Kalachakra Dasha analysis including:
 * - Current Mahadasha and Antardasha
 * - Deha (Body) and Jeeva (Soul) analysis
 * - Complete timeline of all Mahadashas
 * - Health and spiritual predictions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KalachakraDashaScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    viewModel: KalachakraDashaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val language = LocalLanguage.current
    val asOfDate = remember(chart) { LocalDate.now(resolveZoneId(chart?.birthData?.timezone)) }

    val chartKey = remember(chart, language) {
        chart?.let {
            buildString {
                append(it.birthData.dateTime.toString())
                append("|")
                append(String.format("%.6f", it.birthData.latitude))
                append("|")
                append(String.format("%.6f", it.birthData.longitude))
                append("|")
                append(language.name)
            }
        }
    }

    LaunchedEffect(chartKey) {
        viewModel.loadKalachakraDasha(chart, language)
    }

    val currentPeriodInfo = remember(uiState, language) {
        extractCurrentPeriodInfo(uiState, language)
    }

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            KalachakraDashaTopBar(
                chartName = chart?.birthData?.name ?: stringResource(StringKeyMatch.MISC_UNKNOWN),
                currentPeriodInfo = currentPeriodInfo,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppTheme.ScreenBackground)
        ) {
            AnimatedContent(
                targetState = uiState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(200))
                },
                label = "KalachakraDashaStateTransition",
                contentKey = { state -> state::class.simpleName }
            ) { state ->
                when (state) {
                    is KalachakraDashaUiState.Loading -> {
                        LoadingContent()
                    }
                    is KalachakraDashaUiState.Success -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            KalachakraTabRow(
                                selectedTab = selectedTab,
                                onTabSelected = { selectedTab = it }
                            )
                            when (selectedTab) {
                                0 -> CurrentPeriodTab(result = state.result, asOfDate = asOfDate)
                                1 -> DehaJeevaTab(result = state.result)
                                2 -> TimelineTab(result = state.result, asOfDate = asOfDate)
                            }
                        }
                    }
                    is KalachakraDashaUiState.Error -> {
                        ErrorContent(
                            message = state.message,
                            onRetry = { viewModel.loadKalachakraDasha(chart, language) }
                        )
                    }
                    is KalachakraDashaUiState.Idle -> {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            NeoVedicEmptyState(
                                title = stringResource(StringKeyDosha.KALACHAKRA_DASHA_TITLE),
                                subtitle = stringResource(StringKey.NO_PROFILE_MESSAGE),
                                icon = Icons.Outlined.Timeline
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class CurrentPeriodInfo(
    val mahadasha: String?,
    val antardasha: String?,
    val isLoading: Boolean,
    val hasError: Boolean
)

private fun extractCurrentPeriodInfo(
    uiState: KalachakraDashaUiState,
    language: Language
): CurrentPeriodInfo {
    return when (uiState) {
        is KalachakraDashaUiState.Success -> {
            val md = uiState.result.currentMahadasha
            val ad = uiState.result.currentAntardasha
            CurrentPeriodInfo(
                mahadasha = md?.sign?.getLocalizedName(language),
                antardasha = ad?.sign?.getLocalizedName(language),
                isLoading = false,
                hasError = false
            )
        }
        is KalachakraDashaUiState.Loading -> CurrentPeriodInfo(null, null, true, false)
        is KalachakraDashaUiState.Error -> CurrentPeriodInfo(null, null, false, true)
        is KalachakraDashaUiState.Idle -> CurrentPeriodInfo(null, null, false, false)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KalachakraDashaTopBar(
    chartName: String,
    currentPeriodInfo: CurrentPeriodInfo,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    val subtitle = when {
        currentPeriodInfo.isLoading -> stringResource(StringKey.DASHA_CALCULATING)
        currentPeriodInfo.hasError -> "${stringResource(StringKey.DASHA_ERROR)} - $chartName"
        currentPeriodInfo.mahadasha != null -> buildString {
            append(currentPeriodInfo.mahadasha)
            currentPeriodInfo.antardasha?.let {
                append(" -> ")
                append(it)
            }
            append(" | ")
            append(chartName)
        }
        else -> chartName
    }
    NeoVedicPageHeader(
        title = stringResource(StringKeyDosha.KALACHAKRA_DASHA_TITLE),
        subtitle = subtitle,
        onBack = onBack
    )
}

@Composable
private fun KalachakraTabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf(
        TabItem(title = stringResource(StringKeyDosha.KALACHAKRA_CURRENT)),
        TabItem(title = stringResource(StringKeyDosha.KALACHAKRA_DEHA_JEEVA)),
        TabItem(title = stringResource(StringKeyDosha.KALACHAKRA_TIMELINE))
    )

    ModernPillTabRow(
        tabs = tabs,
        selectedIndex = selectedTab,
        onTabSelected = onTabSelected,
        modifier = Modifier.padding(
            horizontal = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding,
            vertical = com.astro.storm.ui.theme.NeoVedicTokens.SpaceXS
        )
    )
}

// ============================================
// CURRENT PERIOD TAB
// ============================================

@Composable
private fun CurrentPeriodTab(
    result: KalachakraDashaCalculator.KalachakraDashaResult,
    asOfDate: LocalDate
) {
    val language = LocalLanguage.current
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding,
            vertical = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding
        ),
        verticalArrangement = Arrangement.spacedBy(com.astro.storm.ui.theme.NeoVedicTokens.SpaceMD)
    ) {
        item(key = "current_period") {
            CurrentPeriodCard(result = result, language = language, asOfDate = asOfDate)
        }

        item(key = "nakshatra_info") {
            NakshatraGroupCard(result = result, language = language)
        }

        item(key = "health_indicator") {
            HealthIndicatorCard(result = result, language = language)
        }

        item(key = "interpretation") {
            InterpretationCard(result = result, language = language)
        }

        item(key = "about_kalachakra") {
            AboutKalachakraCard()
        }

        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CurrentPeriodCard(
    result: KalachakraDashaCalculator.KalachakraDashaResult,
    language: Language,
    asOfDate: LocalDate
) {
    val currentMahadasha = result.currentMahadasha
    val currentAntardasha = result.currentAntardasha

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .vedicCornerMarkers(color = AppTheme.AccentGold),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(NeoVedicTokens.ScreenPadding)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    AppTheme.AccentGold.copy(alpha = 0.2f),
                                    AppTheme.AccentGold.copy(alpha = 0.1f)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = AppTheme.AccentGold,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = stringResource(StringKeyDosha.KALACHAKRA_CURRENT),
                        fontSize = NeoVedicFontSizes.S17,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CinzelDecorativeFamily,
                        color = AppTheme.TextPrimary,
                        letterSpacing = (-0.3).sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(StringKeyDosha.KALACHAKRA_DASHA_SUBTITLE),
                        fontSize = NeoVedicFontSizes.S12,
                        color = AppTheme.TextMuted,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (currentMahadasha != null) {
                SignPeriodRow(
                    label = stringResource(StringKey.DASHA_MAHADASHA),
                    sign = currentMahadasha.sign,
                    startDate = currentMahadasha.startDate,
                    endDate = currentMahadasha.endDate,
                    progress = currentMahadasha.getProgressPercent(asOfDate).toFloat() / 100f,
                    remainingText = formatRemainingYearsLocalized(
                        currentMahadasha.getRemainingDays(asOfDate) / 365.25,
                        language
                    ),
                    isLarge = true,
                    language = language
                )

                currentAntardasha?.let { ad ->
                    Spacer(modifier = Modifier.height(14.dp))
                    SignPeriodRow(
                        label = stringResource(StringKey.DASHA_ANTARDASHA),
                        sign = ad.sign,
                        startDate = ad.startDate,
                        endDate = ad.endDate,
                        progress = ad.getProgressPercent(asOfDate).toFloat() / 100f,
                        remainingText = formatRemainingDaysLocalized(
                            java.time.temporal.ChronoUnit.DAYS.between(asOfDate, ad.endDate).coerceAtLeast(0),
                            language
                        ),
                        isLarge = false,
                        language = language,
                        isDeha = ad.isDehaSign,
                        isJeeva = ad.isJeevaSign
                    )
                }

                HorizontalDivider(
                    color = AppTheme.DividerColor,
                    modifier = Modifier.padding(vertical = 18.dp)
                )

                CurrentEffectsSection(
                    mahadasha = currentMahadasha,
                    language = language
                )
            } else {
                Text(
                    text = stringResource(StringKeyMatch.DASHA_NO_CURRENT_PERIOD),
                    color = AppTheme.TextMuted,
                    fontSize = NeoVedicFontSizes.S14,
                    fontFamily = PoppinsFontFamily,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun SignPeriodRow(
    label: String,
    sign: ZodiacSign,
    startDate: LocalDate,
    endDate: LocalDate,
    progress: Float,
    remainingText: String,
    isLarge: Boolean,
    language: Language,
    isDeha: Boolean = false,
    isJeeva: Boolean = false
) {
    val signColor = AppTheme.getSignColor(sign)
    val circleSize = if (isLarge) 44.dp else 36.dp
    val mainFontSize = if (isLarge) 16.sp else 14.sp
    val subFontSize = if (isLarge) 12.sp else 11.sp
    val progressHeight = if (isLarge) 6.dp else 5.dp

    val startDateFormatted = startDate.formatLocalized(DateFormat.FULL)
    val endDateFormatted = endDate.formatLocalized(DateFormat.FULL)
    val percentComplete = ((progress * 100).toInt()).localized()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .background(signColor, CircleShape)
                    .border(2.dp, signColor.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = sign.symbol,
                    fontSize = if (isLarge) 17.sp else 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = SpaceGroteskFamily,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = label.uppercase(),
                        fontSize = subFontSize,
                        color = AppTheme.TextMuted,
                        fontFamily = SpaceGroteskFamily,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = sign.localizedName(),
                        fontSize = mainFontSize,
                        fontFamily = CinzelDecorativeFamily,
                        fontWeight = if (isLarge) FontWeight.Bold else FontWeight.SemiBold,
                        color = signColor
                    )
                }

                // Show Deha/Jeeva indicators
                if (isDeha || isJeeva) {
                    Row(
                        modifier = Modifier.padding(top = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (isDeha) {
                            Surface(
                                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                                color = AppTheme.SuccessColor.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = stringResource(StringKeyDosha.KALACHAKRA_DEHA).uppercase(),
                                    fontSize = NeoVedicFontSizes.S9,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.SuccessColor,
                                    fontFamily = SpaceGroteskFamily,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        if (isJeeva) {
                            Surface(
                                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                                color = AppTheme.InfoColor.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = stringResource(StringKeyDosha.KALACHAKRA_JEEVA).uppercase(),
                                    fontSize = NeoVedicFontSizes.S9,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.InfoColor,
                                    fontFamily = SpaceGroteskFamily,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "$startDateFormatted \u2013 $endDateFormatted",
                    fontSize = (subFontSize.value - 1).sp,
                    color = AppTheme.TextMuted,
                    fontFamily = SpaceGroteskFamily,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (remainingText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = remainingText,
                        fontSize = (subFontSize.value - 1).sp,
                        color = AppTheme.AccentTeal,
                        fontFamily = SpaceGroteskFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.width(70.dp)
        ) {
            Text(
                text = "$percentComplete%",
                fontSize = subFontSize,
                fontWeight = FontWeight.Bold,
                fontFamily = SpaceGroteskFamily,
                color = signColor
            )
            Spacer(modifier = Modifier.height(5.dp))
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(progressHeight)
                    .clip(RoundedCornerShape(progressHeight / 2)),
                color = signColor,
                trackColor = AppTheme.DividerColor
            )
        }
    }
}

@Composable
private fun CurrentEffectsSection(
    mahadasha: KalachakraDashaCalculator.KalachakraMahadasha,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackgroundElevated
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Psychology,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(StringKeyDosha.KALACHAKRA_EFFECTS),
                    fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.AccentGold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = mahadasha.interpretation.generalEffects,
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
                color = AppTheme.TextPrimary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun NakshatraGroupCard(
    result: KalachakraDashaCalculator.KalachakraDashaResult,
    language: Language
) {
    val groupColor = when (result.nakshatraGroup) {
        KalachakraDashaCalculator.NakshatraGroup.SAVYA -> AppTheme.SuccessColor
        KalachakraDashaCalculator.NakshatraGroup.APSAVYA -> AppTheme.WarningColor
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = groupColor.copy(alpha = 0.08f)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(groupColor.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Timeline,
                        contentDescription = null,
                        tint = groupColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(StringKeyDosha.KALACHAKRA_NAKSHATRA_GROUP),
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S14,
                        color = AppTheme.TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = result.nakshatraGroup.getLocalizedName(language),
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.Bold,
                        color = groupColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Birth nakshatra info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    label = stringResource(StringKeyDosha.KALACHAKRA_BIRTH_NAKSHATRA),
                    value = result.birthNakshatra.localizedName(),
                    color = AppTheme.TextPrimary
                )
                InfoItem(
                    label = stringResource(StringKeyDosha.KALACHAKRA_PADA),
                    value = result.birthNakshatraPada.localized(),
                    color = AppTheme.TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = result.nakshatraGroup.getLocalizedDescription(language),
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Applicability score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(StringKeyDosha.KALACHAKRA_APPLICABILITY),
                    fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted
                )
                Surface(
                    shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = getApplicabilityColor(result.applicabilityScore).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${result.applicabilityScore}%",
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
                        fontWeight = FontWeight.Bold,
                        color = getApplicabilityColor(result.applicabilityScore),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HealthIndicatorCard(
    result: KalachakraDashaCalculator.KalachakraDashaResult,
    language: Language
) {
    val currentMahadasha = result.currentMahadasha ?: return
    val healthIndicator = currentMahadasha.healthIndicator
    val healthColor = getHealthColor(healthIndicator)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackground
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(healthColor.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.HealthAndSafety,
                        contentDescription = null,
                        tint = healthColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(StringKeyDosha.KALACHAKRA_HEALTH_INDICATOR),
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S14,
                        color = AppTheme.TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = healthIndicator.getLocalizedName(language),
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.Bold,
                        color = healthColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = healthIndicator.getLocalizedDescription(language),
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
                color = AppTheme.TextSecondary,
                lineHeight = 19.sp
            )

            if (currentMahadasha.isParamaAyushSign) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = AppTheme.SuccessColor.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Favorite,
                            contentDescription = null,
                            tint = AppTheme.SuccessColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(StringKeyDosha.KALACHAKRA_PARAMA_AYUSH),
                            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.SuccessColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InterpretationCard(
    result: KalachakraDashaCalculator.KalachakraDashaResult,
    language: Language
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 250),
        label = "interpretation_rotation"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) { isExpanded = !isExpanded },
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackground
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .animateContentSize(animationSpec = tween(durationMillis = 250))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(AppTheme.AccentPrimary.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.SelfImprovement,
                            contentDescription = null,
                            tint = AppTheme.AccentPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(StringKeyDosha.KALACHAKRA_INTERPRETATION),
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Icon(
                    Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(tween(250)) + fadeIn(tween(250)),
                exit = shrinkVertically(tween(200)) + fadeOut(tween(150))
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor, modifier = Modifier.padding(bottom = 14.dp))

                    Text(
                        text = result.interpretation.systemOverview,
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(StringKeyDosha.KALACHAKRA_GUIDANCE),
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.AccentGold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    result.interpretation.generalGuidance.forEach { guidance ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("\u2022 ", fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12, color = AppTheme.TextSecondary)
                            Text(
                                text = guidance,
                                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
                                color = AppTheme.TextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutKalachakraCard() {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 250),
        label = "about_rotation"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) { isExpanded = !isExpanded },
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackground
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .animateContentSize(animationSpec = tween(durationMillis = 250))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(AppTheme.InfoColor.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            tint = AppTheme.InfoColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(StringKeyDosha.KALACHAKRA_DASHA_ABOUT),
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
                Icon(
                    Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(tween(250)) + fadeIn(tween(250)),
                exit = shrinkVertically(tween(200)) + fadeOut(tween(150))
            ) {
                Column(modifier = Modifier.padding(top = 18.dp)) {
                    Text(
                        text = stringResource(StringKeyDosha.KALACHAKRA_DASHA_ABOUT_DESC),
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

// ============================================
// DEHA-JEEVA TAB
// ============================================

@Composable
private fun DehaJeevaTab(result: KalachakraDashaCalculator.KalachakraDashaResult) {
    val language = LocalLanguage.current
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding,
            vertical = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding
        ),
        verticalArrangement = Arrangement.spacedBy(com.astro.storm.ui.theme.NeoVedicTokens.SpaceMD)
    ) {
        item(key = "deha_jeeva_overview") {
            DehaJeevaOverviewCard(result = result, language = language)
        }

        item(key = "deha_analysis") {
            DehaAnalysisCard(result = result, language = language)
        }

        item(key = "jeeva_analysis") {
            JeevaAnalysisCard(result = result, language = language)
        }

        item(key = "relationship") {
            RelationshipCard(result = result, language = language)
        }

        item(key = "recommendations") {
            RecommendationsCard(result = result, language = language)
        }

        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun DehaJeevaOverviewCard(
    result: KalachakraDashaCalculator.KalachakraDashaResult,
    language: Language
) {
    val dehaColor = AppTheme.getSignColor(result.dehaRashi)
    val jeevaColor = AppTheme.getSignColor(result.jeevaRashi)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .vedicCornerMarkers(color = AppTheme.AccentPrimary),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(NeoVedicTokens.ScreenPadding)) {
            Text(
                text = stringResource(StringKeyDosha.KALACHAKRA_DEHA_JEEVA_TITLE),
                fontSize = NeoVedicFontSizes.S17,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                fontFamily = CinzelDecorativeFamily
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(StringKeyDosha.KALACHAKRA_DEHA_JEEVA_SUBTITLE),
                fontSize = NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted,
                fontFamily = PoppinsFontFamily
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Deha column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(dehaColor, CircleShape)
                            .border(3.dp, dehaColor.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = result.dehaRashi.symbol,
                            fontSize = NeoVedicFontSizes.S24,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = SpaceGroteskFamily
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(StringKeyDosha.KALACHAKRA_DEHA),
                        fontSize = NeoVedicFontSizes.S12,
                        color = AppTheme.TextMuted,
                        fontWeight = FontWeight.Medium,
                        fontFamily = SpaceGroteskFamily
                    )
                    Text(
                        text = result.dehaRashi.getLocalizedName(language),
                        fontSize = NeoVedicFontSizes.S15,
                        fontWeight = FontWeight.SemiBold,
                        color = dehaColor,
                        fontFamily = CinzelDecorativeFamily
                    )
                    Text(
                        text = "(${stringResource(StringKeyDosha.KALACHAKRA_BODY)})",
                        fontSize = NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted,
                        fontFamily = PoppinsFontFamily
                    )
                }

                // Connector
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Jeeva column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(jeevaColor, CircleShape)
                            .border(3.dp, jeevaColor.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = result.jeevaRashi.symbol,
                            fontSize = NeoVedicFontSizes.S24,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = SpaceGroteskFamily
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(StringKeyDosha.KALACHAKRA_JEEVA),
                        fontSize = NeoVedicFontSizes.S12,
                        color = AppTheme.TextMuted,
                        fontWeight = FontWeight.Medium,
                        fontFamily = SpaceGroteskFamily
                    )
                    Text(
                        text = result.jeevaRashi.getLocalizedName(language),
                        fontSize = NeoVedicFontSizes.S15,
                        fontWeight = FontWeight.SemiBold,
                        color = jeevaColor,
                        fontFamily = CinzelDecorativeFamily
                    )
                    Text(
                        text = "(${stringResource(StringKeyDosha.KALACHAKRA_SOUL)})",
                        fontSize = NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted,
                        fontFamily = PoppinsFontFamily
                    )
                }
            }
        }
    }
}

@Composable
private fun DehaAnalysisCard(
    result: KalachakraDashaCalculator.KalachakraDashaResult,
    language: Language
) {
    val analysis = result.dehaJeevaAnalysis

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        color = AppTheme.SuccessColor.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.HealthAndSafety,
                    contentDescription = null,
                    tint = AppTheme.SuccessColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(StringKeyDosha.KALACHAKRA_DEHA_ANALYSIS),
                    fontSize = NeoVedicFontSizes.S15,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary,
                    fontFamily = CinzelDecorativeFamily
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    label = stringResource(StringKeyDosha.KALACHAKRA_DEHA_LORD),
                    value = analysis.dehaLord.getLocalizedName(language),
                    color = AppTheme.TextPrimary
                )
                InfoItem(
                    label = stringResource(StringKeyDosha.KALACHAKRA_STRENGTH),
                    value = analysis.dehaLordStrength.split(" - ").firstOrNull() ?: analysis.dehaLordStrength,
                    color = AppTheme.TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = analysis.healthPrediction,
                fontSize = NeoVedicFontSizes.S13,
                color = AppTheme.TextSecondary,
                lineHeight = 19.sp,
                fontFamily = PoppinsFontFamily
            )
        }
    }
}

@Composable
private fun JeevaAnalysisCard(
    result: KalachakraDashaCalculator.KalachakraDashaResult,
    language: Language
) {
    val analysis = result.dehaJeevaAnalysis

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        color = AppTheme.InfoColor.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.SelfImprovement,
                    contentDescription = null,
                    tint = AppTheme.InfoColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(StringKeyDosha.KALACHAKRA_JEEVA_ANALYSIS),
                    fontSize = NeoVedicFontSizes.S15,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary,
                    fontFamily = CinzelDecorativeFamily
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    label = stringResource(StringKeyDosha.KALACHAKRA_JEEVA_LORD),
                    value = analysis.jeevaLord.getLocalizedName(language),
                    color = AppTheme.TextPrimary
                )
                InfoItem(
                    label = stringResource(StringKeyDosha.KALACHAKRA_STRENGTH),
                    value = analysis.jeevaLordStrength.split(" - ").firstOrNull() ?: analysis.jeevaLordStrength,
                    color = AppTheme.TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = analysis.longevityPrediction,
                fontSize = NeoVedicFontSizes.S13,
                color = AppTheme.TextSecondary,
                lineHeight = 19.sp,
                fontFamily = PoppinsFontFamily
            )
        }
    }
}


@Composable
private fun RelationshipCard(
    result: KalachakraDashaCalculator.KalachakraDashaResult,
    language: Language
) {
    val relationship = result.dehaJeevaAnalysis.dehaJeevaRelationship
    val relationshipColor = getRelationshipColor(relationship)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackground
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(relationshipColor.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Favorite,
                        contentDescription = null,
                        tint = relationshipColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(StringKeyDosha.KALACHAKRA_RELATIONSHIP),
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S14,
                        color = AppTheme.TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = relationship.getLocalizedName(language),
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.Bold,
                        color = relationshipColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = relationship.getLocalizedDescription(language),
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
                color = AppTheme.TextSecondary,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
private fun RecommendationsCard(
    result: KalachakraDashaCalculator.KalachakraDashaResult,
    language: Language
) {
    val recommendations = result.dehaJeevaAnalysis.recommendations

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackground
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = stringResource(StringKeyMatch.DASHA_RECOMMENDATIONS),
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S16,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.AccentGold
            )

            Spacer(modifier = Modifier.height(14.dp))

            recommendations.forEach { recommendation ->
                Row(
                    modifier = Modifier.padding(vertical = 5.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "\u2022",
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
                        color = AppTheme.AccentGold,
                        modifier = Modifier.padding(end = 10.dp, top = 2.dp)
                    )
                    Text(
                        text = recommendation,
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
                        color = AppTheme.TextSecondary,
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}

// ============================================
// TIMELINE TAB
// ============================================

@Composable
private fun TimelineTab(
    result: KalachakraDashaCalculator.KalachakraDashaResult,
    asOfDate: LocalDate
) {
    val language = LocalLanguage.current
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding,
            vertical = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding
        ),
        verticalArrangement = Arrangement.spacedBy(com.astro.storm.ui.theme.NeoVedicTokens.SpaceMD)
    ) {
        item(key = "timeline_header") {
            TimelineHeaderCard(result = result, language = language)
        }

        items(
            items = result.mahadashas,
            key = { mahadasha -> "${mahadasha.sign.ordinal}_${mahadasha.startDate.toEpochDay()}" }
        ) { mahadasha ->
            MahadashaCard(
                mahadasha = mahadasha,
                isCurrent = mahadasha == result.currentMahadasha,
                currentAntardasha = if (mahadasha == result.currentMahadasha) result.currentAntardasha else null,
                dehaRashi = result.dehaRashi,
                jeevaRashi = result.jeevaRashi,
                language = language,
                asOfDate = asOfDate
            )
        }

        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TimelineHeaderCard(
    result: KalachakraDashaCalculator.KalachakraDashaResult,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackground
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(AppTheme.AccentGold.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Timeline,
                        contentDescription = null,
                        tint = AppTheme.AccentGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(StringKeyDosha.KALACHAKRA_TIMELINE),
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = "${result.mahadashas.size} ${stringResource(StringKey.DASHA_PERIOD)}",
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun MahadashaCard(
    mahadasha: KalachakraDashaCalculator.KalachakraMahadasha,
    isCurrent: Boolean,
    currentAntardasha: KalachakraDashaCalculator.KalachakraAntardasha?,
    dehaRashi: ZodiacSign,
    jeevaRashi: ZodiacSign,
    language: Language,
    asOfDate: LocalDate
) {
    var isExpanded by rememberSaveable { mutableStateOf(isCurrent) }
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 250),
        label = "expand_rotation"
    )

    val signColor = AppTheme.getSignColor(mahadasha.sign)
    val healthColor = getHealthColor(mahadasha.healthIndicator)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = signColor.copy(alpha = 0.3f))
            ) { isExpanded = !isExpanded },
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = if (isCurrent) {
            signColor.copy(alpha = 0.08f)
        } else {
            AppTheme.CardBackground
        },
        tonalElevation = if (isCurrent) 3.dp else 1.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize(animationSpec = tween(durationMillis = 250))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(signColor, CircleShape)
                            .then(
                                if (isCurrent) {
                                    Modifier.border(2.5.dp, signColor.copy(alpha = 0.4f), CircleShape)
                                } else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mahadasha.sign.symbol,
                            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S18,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = mahadasha.sign.getLocalizedName(language),
                                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S15,
                                fontWeight = FontWeight.SemiBold,
                                color = AppTheme.TextPrimary
                            )
                            if (isCurrent) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                                    color = signColor.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = stringResource(StringKey.DASHA_ACTIVE),
                                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S10,
                                        fontWeight = FontWeight.Bold,
                                        color = signColor,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "${formatYearsLocalized(mahadasha.durationYears, language)} \u2022 ${mahadasha.startDate.formatLocalized(DateFormat.YEAR_ONLY)} \u2013 ${mahadasha.endDate.formatLocalized(DateFormat.YEAR_ONLY)}",
                            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S11,
                            color = AppTheme.TextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (isCurrent) {
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "${String.format("%.1f", mahadasha.getProgressPercent(asOfDate))}% \u2022 ${formatRemainingYearsLocalized(mahadasha.getRemainingDays(asOfDate) / 365.25, language)}",
                                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S10,
                                color = AppTheme.AccentTeal,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Health indicator badge
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = healthColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = getHealthLocalizedName(mahadasha.healthIndicator, language),
                                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S9,
                                fontWeight = FontWeight.Medium,
                                color = healthColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Icon(
                    Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier
                        .size(26.dp)
                        .rotate(rotation)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(tween(250)) + fadeIn(tween(250)),
                exit = shrinkVertically(tween(200)) + fadeOut(tween(150))
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor, modifier = Modifier.padding(bottom = 14.dp))

                    // Interpretation
                    Text(
                        text = mahadasha.interpretation.generalEffects,
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = stringResource(StringKey.DASHA_ANTARDASHA),
                            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S14,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextSecondary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Surface(
                            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = AppTheme.CardBackgroundElevated
                        ) {
                            Text(
                                text = "${mahadasha.antardashas.size} ${stringResource(StringKey.DASHA_PERIOD)}",
                                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S10,
                                color = AppTheme.TextMuted,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    mahadasha.antardashas.forEach { antardasha ->
                        AntardashaRow(
                            antardasha = antardasha,
                            isCurrent = antardasha == currentAntardasha,
                            dehaRashi = dehaRashi,
                            jeevaRashi = jeevaRashi,
                            language = language,
                            asOfDate = asOfDate
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun AntardashaRow(
    antardasha: KalachakraDashaCalculator.KalachakraAntardasha,
    isCurrent: Boolean,
    dehaRashi: ZodiacSign,
    jeevaRashi: ZodiacSign,
    language: Language,
    asOfDate: LocalDate
) {
    val signColor = AppTheme.getSignColor(antardasha.sign)
    val today = asOfDate
    val isPast = antardasha.endDate.isBefore(today)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isCurrent) signColor.copy(alpha = 0.12f) else Color.Transparent,
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
            )
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = if (isPast) signColor.copy(alpha = 0.4f) else signColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = antardasha.sign.symbol,
                    fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S11,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = antardasha.sign.localizedName(),
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        color = when {
                            isCurrent -> signColor
                            isPast -> AppTheme.TextMuted
                            else -> AppTheme.TextPrimary
                        }
                    )
                    if (isCurrent) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${antardasha.getProgressPercent(asOfDate).toInt().localized()}%",
                            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S10,
                            fontWeight = FontWeight.Bold,
                            color = signColor.copy(alpha = 0.9f)
                        )
                    }
                }

                // Deha/Jeeva indicators
                if (antardasha.isDehaSign || antardasha.isJeevaSign) {
                    Row(
                        modifier = Modifier.padding(top = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (antardasha.isDehaSign) {
                            Surface(
                                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                                color = AppTheme.SuccessColor.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = stringResource(StringKeyDosha.KALACHAKRA_DEHA),
                                    fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S8,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.SuccessColor,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        if (antardasha.isJeevaSign) {
                            Surface(
                                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                                color = AppTheme.InfoColor.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = stringResource(StringKeyDosha.KALACHAKRA_JEEVA),
                                    fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S8,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.InfoColor,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${antardasha.startDate.formatLocalized(DateFormat.MONTH_YEAR)} \u2013 ${antardasha.endDate.formatLocalized(DateFormat.MONTH_YEAR)}",
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S11,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${antardasha.durationMonths.localized(1)} ${stringResource(StringKey.UNIT_MONTHS)}",
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S10,
                color = AppTheme.TextMuted.copy(alpha = 0.8f)
            )
        }
    }
}

// ============================================
// COMMON COMPONENTS
// ============================================

@Composable
private fun InfoItem(label: String, value: String, color: Color) {
    Column {
        Text(
            text = label,
            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S11,
            color = AppTheme.TextMuted,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S14,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
private fun EmptyPeriodState() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackgroundElevated
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(StringKey.DASHA_NO_ACTIVE_PERIOD),
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                AppTheme.AccentPrimary.copy(alpha = 0.15f),
                                AppTheme.AccentPrimary.copy(alpha = 0.05f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = AppTheme.AccentPrimary,
                    strokeWidth = 3.dp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(StringKey.DASHA_CALCULATING_TIMELINE),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKey.DASHA_CALCULATING_DESC),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius))
                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(StringKey.DASHA_CALCULATION_FAILED),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.AccentPrimary
                ),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Text(
                    text = stringResource(StringKey.BTN_TRY_AGAIN),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyContent(onBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius))
                    .background(AppTheme.CardBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonOff,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = AppTheme.TextMuted
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(StringKey.DASHA_NO_CHART_SELECTED),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKey.DASHA_NO_CHART_MESSAGE),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedButton(
                onClick = onBack,
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Text(
                    text = stringResource(StringKey.BTN_GO_BACK),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// ============================================
// HELPER FUNCTIONS
// ============================================



@Composable
private fun getHealthColor(health: KalachakraDashaCalculator.HealthIndicator): Color {
    return when (health) {
        KalachakraDashaCalculator.HealthIndicator.EXCELLENT -> AppTheme.SuccessColor
        KalachakraDashaCalculator.HealthIndicator.GOOD -> com.astro.storm.ui.theme.SuccessDark
        KalachakraDashaCalculator.HealthIndicator.MODERATE -> AppTheme.InfoColor
        KalachakraDashaCalculator.HealthIndicator.CHALLENGING -> AppTheme.WarningColor
        KalachakraDashaCalculator.HealthIndicator.CRITICAL -> AppTheme.ErrorColor
    }
}

@Composable
private fun getRelationshipColor(relationship: KalachakraDashaCalculator.DehaJeevaRelationship): Color {
    return when (relationship) {
        KalachakraDashaCalculator.DehaJeevaRelationship.HARMONIOUS -> AppTheme.SuccessColor
        KalachakraDashaCalculator.DehaJeevaRelationship.SUPPORTIVE -> com.astro.storm.ui.theme.SuccessDark
        KalachakraDashaCalculator.DehaJeevaRelationship.NEUTRAL -> AppTheme.InfoColor
        KalachakraDashaCalculator.DehaJeevaRelationship.CHALLENGING -> AppTheme.WarningColor
        KalachakraDashaCalculator.DehaJeevaRelationship.TRANSFORMATIVE -> AppTheme.AccentPrimary
    }
}

@Composable
private fun getApplicabilityColor(score: Int): Color {
    return when {
        score >= 70 -> AppTheme.SuccessColor
        score >= 50 -> AppTheme.InfoColor
        score >= 30 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }
}

/**
 * Get localized name for HealthIndicator
 */
private fun getHealthLocalizedName(health: KalachakraDashaCalculator.HealthIndicator, language: Language): String {
    return when (health) {
        KalachakraDashaCalculator.HealthIndicator.EXCELLENT -> StringResources.get(StringKeyDosha.STRENGTH_EXCELLENT, language)
        KalachakraDashaCalculator.HealthIndicator.GOOD -> StringResources.get(StringKeyDosha.STRENGTH_GOOD, language)
        KalachakraDashaCalculator.HealthIndicator.MODERATE -> StringResources.get(StringKeyDosha.STRENGTH_AVERAGE, language)
        KalachakraDashaCalculator.HealthIndicator.CHALLENGING -> StringResources.get(StringKeyDosha.YOGINI_NATURE_CHALLENGING, language)
        KalachakraDashaCalculator.HealthIndicator.CRITICAL -> StringResources.get(StringKeyDosha.STRENGTH_VERY_WEAK, language)
    }
}

private fun formatNumber(number: Int, language: Language): String {
    return when (language) {
        Language.ENGLISH -> number.toString()
        Language.NEPALI -> BikramSambatConverter.toNepaliNumerals(number)
    }
}

private fun formatYearsLocalized(years: Int, language: Language): String {
    return when (language) {
        Language.ENGLISH -> if (years == 1) "1 year" else "$years years"
        Language.NEPALI -> "${BikramSambatConverter.toNepaliNumerals(years)} à¤µà¤°à¥à¤·"
    }
}

private fun formatRemainingYearsLocalized(years: Double, language: Language): String {
    if (years <= 0) return ""
    val wholeYears = years.toInt()
    val remainingMonths = ((years - wholeYears) * 12).toInt()

    return when (language) {
        Language.ENGLISH -> when {
            wholeYears > 0 && remainingMonths > 0 -> "${wholeYears}y ${remainingMonths}m remaining"
            wholeYears > 0 -> "${wholeYears}y remaining"
            remainingMonths > 0 -> "${remainingMonths}m remaining"
            else -> ""
        }
        Language.NEPALI -> when {
            wholeYears > 0 && remainingMonths > 0 -> "${BikramSambatConverter.toNepaliNumerals(wholeYears)} à¤µà¤°à¥à¤· ${BikramSambatConverter.toNepaliNumerals(remainingMonths)} à¤®à¤¹à¤¿à¤¨à¤¾ à¤¬à¤¾à¤à¤•à¥€"
            wholeYears > 0 -> "${BikramSambatConverter.toNepaliNumerals(wholeYears)} à¤µà¤°à¥à¤· à¤¬à¤¾à¤à¤•à¥€"
            remainingMonths > 0 -> "${BikramSambatConverter.toNepaliNumerals(remainingMonths)} à¤®à¤¹à¤¿à¤¨à¤¾ à¤¬à¤¾à¤à¤•à¥€"
            else -> ""
        }
    }
}

private fun formatRemainingDaysLocalized(days: Long, language: Language): String {
    if (days <= 0) return ""
    val months = days / 30
    val remainingDays = days % 30

    return when (language) {
        Language.ENGLISH -> when {
            months > 0 && remainingDays > 0 -> "${months}m ${remainingDays}d remaining"
            months > 0 -> "${months}m remaining"
            else -> "${remainingDays}d remaining"
        }
        Language.NEPALI -> when {
            months > 0 && remainingDays > 0 -> "${BikramSambatConverter.toNepaliNumerals(months.toInt())} à¤®à¤¹à¤¿à¤¨à¤¾ ${BikramSambatConverter.toNepaliNumerals(remainingDays.toInt())} à¤¦à¤¿à¤¨ à¤¬à¤¾à¤à¤•à¥€"
            months > 0 -> "${BikramSambatConverter.toNepaliNumerals(months.toInt())} à¤®à¤¹à¤¿à¤¨à¤¾ à¤¬à¤¾à¤à¤•à¥€"
            else -> "${BikramSambatConverter.toNepaliNumerals(remainingDays.toInt())} à¤¦à¤¿à¤¨ à¤¬à¤¾à¤à¤•à¥€"
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







