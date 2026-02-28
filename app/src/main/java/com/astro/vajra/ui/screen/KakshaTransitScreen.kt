package com.astro.vajra.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import com.astro.vajra.ui.components.common.TabItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.vajra.core.common.Language
import com.astro.vajra.data.localization.LocalLanguage
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringKeyAdvanced
import com.astro.vajra.core.common.StringKeyUICommon
import com.astro.vajra.core.common.StringKeyUIExtra
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ephemeris.KakshaTransitCalculator
import com.astro.vajra.ephemeris.KakshaTransitCalculator.KakshaQuality
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.viewmodel.KakshaTab
import com.astro.vajra.ui.viewmodel.KakshaTransitUiState
import com.astro.vajra.ui.viewmodel.KakshaTransitViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import com.astro.vajra.ui.theme.PoppinsFontFamily
import androidx.compose.foundation.BorderStroke

/**
 * Kakshya Transit Analysis Screen
 *
 * Displays micro-transit analysis where each sign is divided into 8 parts (3\u00B045' each).
 * This system provides high-precision timing for planetary results based on Ashtakavarga.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KakshaTransitScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    viewModel: KakshaTransitViewModel = hiltViewModel()
) {
    val language = LocalLanguage.current
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()

    LaunchedEffect(chart) {
        chart?.let {
            viewModel.calculateKakshaTransits(it, language)
        }
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyAdvanced.KAKSHYA_TITLE),
                subtitle = stringResource(StringKeyAdvanced.KAKSHYA_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { chart?.let { viewModel.calculateKakshaTransits(it, language, true) } }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = stringResource(StringKey.BTN_RETRY),
                            tint = AppTheme.TextPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is KakshaTransitUiState.Loading -> KakshaLoadingContent(modifier = Modifier.padding(paddingValues))
            is KakshaTransitUiState.Error -> KakshaErrorContent(
                message = state.message,
                modifier = Modifier.padding(paddingValues),
                onRetry = { chart?.let { viewModel.calculateKakshaTransits(it, language, true) } }
            )
            is KakshaTransitUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    KakshaTabSelector(
                        selectedTab = selectedTab,
                        onTabSelected = { viewModel.selectTab(it) },
                        language = language
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        when (selectedTab) {
                            KakshaTab.CURRENT -> CurrentKakshaTab(state.result, viewModel, language)
                            KakshaTab.PLANETS -> PlanetsKakshaTab(state.result, viewModel, language)
                            KakshaTab.TIMELINE -> TimelineKakshaTab(state.result, language)
                            KakshaTab.FAVORABLE -> FavorableKakshaTab(state.result, language)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KakshaTabSelector(
    selectedTab: KakshaTab,
    onTabSelected: (KakshaTab) -> Unit,
    language: Language
) {
    val tabItems = KakshaTab.entries.map { tab ->
        TabItem(
            title = getTabTitle(tab, language),
            accentColor = if (selectedTab == tab) AppTheme.AccentGold else Color.Unspecified
        )
    }

    ModernPillTabRow(
        tabs = tabItems,
        selectedIndex = selectedTab.ordinal,
        onTabSelected = { index -> onTabSelected(KakshaTab.entries[index]) },
        modifier = Modifier.padding(
            horizontal = NeoVedicTokens.ScreenPadding,
            vertical = NeoVedicTokens.SpaceXS
        )
    )
}

private fun getTabTitle(tab: KakshaTab, language: Language): String {
    val key = when (tab) {
        KakshaTab.CURRENT -> StringKeyAdvanced.KAKSHYA_TAB_CURRENT
        KakshaTab.PLANETS -> StringKeyAdvanced.KAKSHYA_TAB_PLANETS
        KakshaTab.TIMELINE -> StringKeyAdvanced.KAKSHYA_TAB_TIMELINE
        KakshaTab.FAVORABLE -> StringKeyAdvanced.KAKSHYA_TAB_FAVORABLE
    }
    return com.astro.vajra.core.common.StringResources.get(key, language)
}

private fun kakshaLocale(language: Language): Locale =
    if (language == Language.NEPALI) Locale.forLanguageTag("ne-NP") else Locale.ENGLISH

private fun kakshaDateTimeFormatter(language: Language): DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMM d, HH:mm", kakshaLocale(language))

private fun kakshaMonthDayFormatter(language: Language): DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMM d", kakshaLocale(language))

@Composable
private fun CurrentKakshaTab(
    result: KakshaTransitCalculator.KakshaTransitResult,
    viewModel: KakshaTransitViewModel,
    language: Language
) {
    val summary = viewModel.getSummary()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            KakshaSummaryCard(summary, language)
        }

        item {
            Text(
                text = stringResource(StringKeyAdvanced.KAKSHYA_PLANET_POSITIONS).uppercase(),
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextMuted,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        items(result.currentPositions) { position ->
            KakshaPlanetCard(position, language)
        }
    }
}

@Composable
private fun KakshaSummaryCard(summary: com.astro.vajra.ui.viewmodel.KakshaSummary?, language: Language) {
    if (summary == null) return
    val qualityColor = getQualityColor(summary.overallQuality)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(StringKeyAdvanced.KAKSHYA_OVERALL_QUALITY),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = summary.overallQuality.getLocalizedName(language),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                        fontWeight = FontWeight.Bold,
                        color = qualityColor
                    )
                }
                
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { (summary.overallScore / 100).toFloat() },
                        modifier = Modifier.size(64.dp),
                        color = qualityColor,
                        trackColor = qualityColor.copy(alpha = 0.1f),
                        strokeWidth = 8.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        text = "${summary.overallScore.toInt()}",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = AppTheme.DividerColor)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryStat(
                    label = stringResource(StringKeyAdvanced.KAKSHYA_QUALITY_EXCELLENT),
                    value = summary.excellentCount.toString(),
                    color = AppTheme.SuccessColor
                )
                SummaryStat(
                    label = stringResource(StringKeyAdvanced.KAKSHYA_QUALITY_GOOD),
                    value = summary.goodCount.toString(),
                    color = AppTheme.AccentTeal
                )
                SummaryStat(
                    label = stringResource(StringKeyAdvanced.KAKSHYA_QUALITY_MODERATE),
                    value = summary.moderateCount.toString(),
                    color = AppTheme.AccentGold
                )
                SummaryStat(
                    label = stringResource(StringKeyAdvanced.KAKSHYA_QUALITY_POOR),
                    value = summary.poorCount.toString(),
                    color = AppTheme.ErrorColor
                )
            }
        }
    }
}

@Composable
private fun SummaryStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontFamily = SpaceGroteskFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontFamily = PoppinsFontFamily,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
            color = AppTheme.TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun KakshaPlanetCard(position: KakshaTransitCalculator.KakshaPlanetPosition, language: Language) {
    val qualityColor = getQualityColor(position.quality)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AppTheme.getPlanetColor(position.planet).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = position.planet.symbol,
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.getPlanetColor(position.planet)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = position.planet.getLocalizedName(language),
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = position.sign.getLocalizedName(language) + " (${position.degreeInSign.toInt()}\u00B0" + ((position.degreeInSign % 1) * 60).toInt() + "')",
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = AppTheme.TextMuted
                        )
                    }
                }
                
                Surface(
                    shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                    color = qualityColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = position.quality.getLocalizedName(language),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                        fontWeight = FontWeight.Bold,
                        color = qualityColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(StringKeyAdvanced.KAKSHYA_LORD),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = position.kakshaLord,
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyAdvanced.ASHTAVARGA_BAV_TITLE),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = "${position.bavScore}/8",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Bold,
                        color = if (position.hasBinbu) AppTheme.SuccessColor else AppTheme.TextPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(StringKeyAdvanced.KAKSHYA_NEXT),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = "${position.timeToNextKaksha}h",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.AccentPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                color = AppTheme.CardBackgroundElevated
            ) {
                Text(
                    text = if (language == Language.NEPALI) position.interpretationNe else position.interpretation,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary,
                    modifier = Modifier.padding(12.dp),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun PlanetsKakshaTab(
    result: KakshaTransitCalculator.KakshaTransitResult,
    viewModel: KakshaTransitViewModel,
    language: Language
) {
    val selectedPlanet by viewModel.selectedPlanet.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(result.currentPositions) { pos ->
                com.astro.vajra.ui.components.common.NeoVedicChoicePill(
                    selected = selectedPlanet == pos.planet,
                    onClick = { viewModel.selectPlanet(if (selectedPlanet == pos.planet) null else pos.planet) },
                    label = { Text(pos.planet.getLocalizedName(language), fontFamily = SpaceGroteskFamily) },
                    leadingIcon = if (selectedPlanet == pos.planet) {
                        { Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.2f),
                        selectedLabelColor = AppTheme.AccentPrimary
                    )
                )
            }
        }

        if (selectedPlanet != null) {
            val planetPos = viewModel.getPositionForPlanet(selectedPlanet!!)
            val changes = viewModel.getChangesForPlanet(selectedPlanet!!)
            val favorable = viewModel.getFavorablePeriodsForPlanet(selectedPlanet!!)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (planetPos != null) {
                    item { KakshaPlanetCard(planetPos, language) }
                }

                if (favorable.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(StringKeyAdvanced.KAKSHYA_UPCOMING_FAVORABLE).uppercase(),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.SuccessColor
                        )
                    }
                    items(favorable) { period ->
                        FavorablePeriodItem(period, language)
                    }
                }

                if (changes.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(StringKeyAdvanced.KAKSHYA_UPCOMING_CHANGES).uppercase(),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.AccentPrimary
                        )
                    }
                    items(changes) { change ->
                        KakshaChangeItem(change, language)
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.Public,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = AppTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(StringKeyAdvanced.KAKSHYA_SELECT_PLANET),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = AppTheme.TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun FavorablePeriodItem(period: KakshaTransitCalculator.FavorableKakshaPeriod, language: Language) {
    val dateFormatter = kakshaDateTimeFormatter(language)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.SuccessColor.copy(alpha = 0.05f),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.SuccessColor.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AppTheme.AccentGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = period.kakshaLord,
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                    color = AppTheme.SuccessColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "BAV: ${period.bavScore}",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.SuccessColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${period.startTime.format(dateFormatter)} - ${period.endTime.format(dateFormatter)}",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (language == Language.NEPALI) period.descriptionNe else period.description,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun KakshaChangeItem(change: KakshaTransitCalculator.KakshaChange, language: Language) {
    val dateFormatter = kakshaDateTimeFormatter(language)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Kakshya ${change.currentKaksha}",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp).padding(horizontal = 4.dp),
                        tint = AppTheme.TextMuted
                    )
                    Text(
                        text = change.nextKakshaLord,
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.AccentPrimary
                    )
                }
                Text(
                    text = change.expectedTime.format(dateFormatter),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                    color = AppTheme.TextMuted
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                    color = if (change.willHaveBindu) AppTheme.SuccessColor.copy(alpha = 0.15f) else AppTheme.ErrorColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (change.willHaveBindu) "Bindu +" else "Bindu -",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        fontWeight = FontWeight.Bold,
                        color = if (change.willHaveBindu) AppTheme.SuccessColor else AppTheme.ErrorColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = "in ${change.hoursFromNow}h",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun TimelineKakshaTab(
    result: KakshaTransitCalculator.KakshaTransitResult,
    language: Language
) {
    if (result.upcomingChanges.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(StringKeyAdvanced.KAKSHYA_NO_CHANGES),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(result.upcomingChanges) { change ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardBackground,
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(AppTheme.getPlanetColor(change.planet).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = change.planet.symbol,
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.getPlanetColor(change.planet)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == Language.NEPALI) change.impactDescriptionNe else change.impactDescription,
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary,
                            lineHeight = 18.sp
                        )
                        Text(
                            text = change.expectedTime.format(kakshaDateTimeFormatter(language)),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                            color = AppTheme.TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavorableKakshaTab(
    result: KakshaTransitCalculator.KakshaTransitResult,
    language: Language
) {
    if (result.favorablePeriods.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(StringKeyAdvanced.KAKSHYA_NO_FAVORABLE),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(result.favorablePeriods) { period ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardBackground,
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = AppTheme.AccentGold.copy(alpha = 0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Star, null, tint = AppTheme.AccentGold, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${period.planet.getLocalizedName(language)} in ${period.kakshaLord} Kakshya",
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = if (language == Language.NEPALI) period.descriptionNe else period.description,
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, null, tint = AppTheme.TextMuted, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${period.startTime.format(kakshaMonthDayFormatter(language))} - ${period.endTime.format(kakshaMonthDayFormatter(language))} (${period.duration/24} days)",
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = AppTheme.TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KakshaLoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = AppTheme.AccentPrimary)
    }
}

@Composable
private fun KakshaErrorContent(message: String, modifier: Modifier = Modifier, onRetry: () -> Unit) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Error, null, tint = AppTheme.ErrorColor, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message, fontFamily = PoppinsFontFamily, color = AppTheme.TextPrimary, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(text = stringResource(StringKey.BTN_RETRY), fontFamily = SpaceGroteskFamily)
            }
        }
    }
}

@Composable
private fun getQualityColor(quality: KakshaQuality): Color {
    return when (quality) {
        KakshaQuality.EXCELLENT -> com.astro.vajra.ui.theme.SuccessDark
        KakshaQuality.GOOD -> AppTheme.PlanetSaturn
        KakshaQuality.MODERATE -> AppTheme.AccentGold
        KakshaQuality.POOR -> AppTheme.ErrorColor
    }
}
