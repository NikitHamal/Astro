package com.astro.storm.ui.screen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.ephemeris.AshtottariAntardasha
import com.astro.storm.ephemeris.AshtottariDashaCalculator
import com.astro.storm.ephemeris.AshtottariDashaResult
import com.astro.storm.ephemeris.AshtottariMahadasha
import com.astro.storm.ephemeris.PlanetRelationship
import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.NeoVedicPageHeader
import com.astro.storm.ui.components.common.TabItem
import com.astro.storm.ui.components.common.vedicCornerMarkers
import com.astro.storm.ui.screen.chartdetail.ChartDetailColors
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.CinzelDecorativeFamily
import com.astro.storm.ui.theme.NeoVedicFontSizes
import com.astro.storm.ui.theme.NeoVedicTokens
import com.astro.storm.ui.theme.PoppinsFontFamily
import com.astro.storm.ui.theme.SpaceGroteskFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

// Helper extension for planet abbreviation
private val Planet.abbreviation: String
    get() = when (this) {
        Planet.SUN -> "Su"
        Planet.MOON -> "Mo"
        Planet.MARS -> "Ma"
        Planet.MERCURY -> "Me"
        Planet.JUPITER -> "Ju"
        Planet.VENUS -> "Ve"
        Planet.SATURN -> "Sa"
        Planet.RAHU -> "Ra"
        Planet.KETU -> "Ke"
        else -> name.take(2)
    }

/**
 * Ashtottari Dasha Screen
 *
 * Comprehensive display of the 108-year Ashtottari Dasha system.
 * Shows applicability check, timeline, current periods, and interpretations.
 *
 * Key Features:
 * - Applicability check (Rahu in Kendra/Trikona from Lagna Lord)
 * - Complete timeline with all Mahadashas
 * - Current period details with Antardashas
 * - Comparison with Vimshottari when both are applicable
 *
 * Reference: BPHS Chapter 46, Uttara Kalamrita
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AshtottariDashaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(
            title = stringResource(StringKeyDosha.ASHTOTTARI_TITLE),
            message = stringResource(StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    val language = LocalLanguage.current
    val asOf = remember(chart) { LocalDateTime.now(resolveZoneId(chart.birthData.timezone)) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var dashaResult by remember { mutableStateOf<AshtottariDashaResult?>(null) }
    var selectedMahadasha by remember { mutableStateOf<AshtottariMahadasha?>(null) }
    var expandedMahadashaIndex by remember { mutableIntStateOf(-1) }

    val tabs = listOf(
        stringResource(StringKeyDosha.SCREEN_ABOUT),
        stringResource(StringKeyDosha.SCREEN_TIMELINE),
        stringResource(StringKeyDosha.SCREEN_INTERPRETATION)
    )

    // Calculate Ashtottari Dasha
    LaunchedEffect(chart) {
        isCalculating = true
        delay(300)
        try {
            dashaResult = withContext(Dispatchers.Default) {
                AshtottariDashaCalculator.calculateAshtottariDasha(chart)
            }
        } catch (e: Exception) {
            // Handle error silently
        }
        isCalculating = false
    }

    if (showInfoDialog) {
        AshtottariInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            AshtottariDashaTopBar(
                chartName = chart.birthData.name,
                result = dashaResult,
                isCalculating = isCalculating,
                onBack = onBack,
                onInfoClick = { showInfoDialog = true }
            )
        }
    ) { paddingValues ->
        if (isCalculating) {
            LoadingContent(paddingValues)
        } else if (dashaResult != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Tab selector
                TabSelector(
                    tabs = tabs,
                    selectedIndex = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                // Content based on selected tab
                when (selectedTab) {
                    0 -> ApplicabilityContent(dashaResult!!, chart, asOf)
                    1 -> TimelineContent(
                        result = dashaResult!!,
                        expandedIndex = expandedMahadashaIndex,
                        onExpandChange = { expandedMahadashaIndex = if (expandedMahadashaIndex == it) -1 else it },
                        asOf = asOf
                    )
                    2 -> InterpretationContent(dashaResult!!)
                }
            }
        } else {
            ErrorContent(paddingValues, stringResource(StringKeyDosha.SCREEN_ERROR_CALCULATION))
        }
    }
}

@Composable
private fun AshtottariDashaTopBar(
    chartName: String,
    result: AshtottariDashaResult?,
    isCalculating: Boolean,
    onBack: () -> Unit,
    onInfoClick: () -> Unit
) {
    val language = LocalLanguage.current
    val subtitle = when {
        isCalculating -> stringResource(StringKey.DASHA_CALCULATING)
        result != null -> buildString {
            result.currentMahadasha?.let { md ->
                append(md.planet.getLocalizedName(language))
                result.currentAntardasha?.let { ad ->
                    append(" -> ")
                    append(ad.antardashaLord.getLocalizedName(language))
                }
                append(" | ")
            }
            append(chartName)
        }
        else -> chartName
    }
    NeoVedicPageHeader(
        title = stringResource(StringKeyDosha.ASHTOTTARI_TITLE),
        subtitle = subtitle,
        onBack = onBack,
        actionIcon = Icons.Outlined.Info,
        onAction = onInfoClick
    )
}

@Composable
private fun LoadingContent(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(StringKeyDosha.SCREEN_CALCULATING),
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun ErrorContent(paddingValues: PaddingValues, message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = AppTheme.ErrorColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(message, color = AppTheme.TextMuted)
        }
    }
}

@Composable
private fun TabSelector(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabItems = tabs.map { TabItem(title = it, accentColor = AppTheme.AccentPrimary) }

    ModernPillTabRow(
        tabs = tabItems,
        selectedIndex = selectedIndex,
        onTabSelected = onTabSelected,
        modifier = Modifier.padding(
            horizontal = com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding,
            vertical = com.astro.storm.ui.theme.NeoVedicTokens.SpaceXS
        )
    )
}

@Composable
private fun ApplicabilityContent(
    result: AshtottariDashaResult,
    chart: VedicChart,
    asOf: LocalDateTime
) {
    val language = LocalLanguage.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(com.astro.storm.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(com.astro.storm.ui.theme.NeoVedicTokens.SpaceMD)
    ) {
        // Applicability Status Card
        item {
            ApplicabilityStatusCard(result)
        }

        // Condition Details Card
        item {
            ConditionDetailsCard(result)
        }

        // Current Period Card if applicable
        if (result.currentMahadasha != null) {
            item {
                CurrentPeriodCard(result, asOf)
            }
        }

        // System Info Card
        item {
            SystemInfoCard()
        }
    }
}

@Composable
private fun ApplicabilityStatusCard(result: AshtottariDashaResult) {
    val language = LocalLanguage.current
    val isApplicable = result.applicability.isApplicable
    val statusColor = if (isApplicable) AppTheme.SuccessColor else AppTheme.WarningColor

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .vedicCornerMarkers(color = statusColor),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(NeoVedicTokens.ScreenPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                 Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(statusColor.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isApplicable) Icons.Filled.CheckCircle else Icons.Outlined.Info,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(StringKeyDosha.ASHTOTTARI_APPLICABILITY),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = NeoVedicFontSizes.S17,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = if (isApplicable)
                            stringResource(StringKeyDosha.ASHTOTTARI_APPLICABLE)
                        else
                            stringResource(StringKeyDosha.ASHTOTTARI_NOT_APPLICABLE),
                        fontFamily = PoppinsFontFamily,
                        fontSize = NeoVedicFontSizes.S13,
                        color = AppTheme.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(NeoVedicTokens.SpaceMD))

            Text(
                text = result.applicability.reason,
                fontFamily = PoppinsFontFamily,
                fontSize = NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun ConditionDetailsCard(result: AshtottariDashaResult) {
    val language = LocalLanguage.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(NeoVedicTokens.ScreenPadding)
        ) {
            Text(
                text = stringResource(StringKeyDosha.ASHTOTTARI_IDEAL_CONDITION),
                fontFamily = SpaceGroteskFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(NeoVedicTokens.SpaceMD))

            // Birth Nakshatra Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(StringKeyDosha.BIRTH_NAKSHATRA),
                    fontFamily = PoppinsFontFamily,
                    color = AppTheme.TextMuted,
                    fontSize = NeoVedicFontSizes.S14
                )
                Text(
                    text = result.moonNakshatra.getLocalizedName(language),
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.TextPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = NeoVedicFontSizes.S14
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Starting Lord
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(StringKeyDosha.ASHTOTTARI_STARTING_LORD),
                    fontFamily = PoppinsFontFamily,
                    color = AppTheme.TextMuted,
                    fontSize = NeoVedicFontSizes.S14
                )
                Text(
                    text = result.startingLord.getLocalizedName(language),
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.TextPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = NeoVedicFontSizes.S14
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Balance at Birth
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(StringKeyDosha.ASHTOTTARI_BALANCE),
                    fontFamily = PoppinsFontFamily,
                    color = AppTheme.TextMuted,
                    fontSize = NeoVedicFontSizes.S14
                )
                Text(
                    text = String.format("%.2f %s", result.balanceAtBirth, stringResource(StringKey.YEARS)),
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.TextPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = NeoVedicFontSizes.S14
                )
            }
        }
    }
}

@Composable
private fun CurrentPeriodCard(
    result: AshtottariDashaResult,
    asOf: LocalDateTime
) {
    val language = LocalLanguage.current
    val mahadasha = result.currentMahadasha ?: return
    val antardasha = result.currentAntardasha
    val locale = if (language == Language.NEPALI) Locale("ne", "NP") else Locale.ENGLISH
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", locale)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .vedicCornerMarkers(color = AppTheme.AccentPrimary),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(NeoVedicTokens.ScreenPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                 Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(AppTheme.AccentPrimary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(StringKeyDosha.ASHTOTTARI_CURRENT_PERIOD),
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = NeoVedicFontSizes.S17,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Current Mahadasha
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.AccentPrimary.copy(alpha = 0.05f),
                shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.AccentPrimary.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(StringKey.DASHA_MAHADASHA).uppercase(),
                            color = AppTheme.TextSecondary,
                            fontFamily = SpaceGroteskFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = NeoVedicFontSizes.S10,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = mahadasha.planet.getLocalizedName(language),
                            fontFamily = CinzelDecorativeFamily,
                            color = AppTheme.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = NeoVedicFontSizes.S18
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Surface(
                            shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
                            color = AppTheme.CardBackground
                        ) {
                             Text(
                                text = "${mahadasha.startDate.format(dateFormatter)} - ${mahadasha.endDate.format(dateFormatter)}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontFamily = SpaceGroteskFamily,
                                color = AppTheme.TextSecondary,
                                fontSize = NeoVedicFontSizes.S11
                            )
                        }
                    }
                }
            }

            // Current Antardasha
            if (antardasha != null) {
                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = AppTheme.CardBackground,
                    shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                    border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.BorderColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = stringResource(StringKey.DASHA_ANTARDASHA).uppercase(),
                                color = AppTheme.TextSecondary,
                                fontFamily = SpaceGroteskFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = NeoVedicFontSizes.S10,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = antardasha.antardashaLord.getLocalizedName(language),
                                fontFamily = PoppinsFontFamily,
                                color = AppTheme.TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = NeoVedicFontSizes.S16
                            )
                        }
                        RelationshipChip(antardasha.relationship)
                    }
                }
            }

            // Progress Bar
            Spacer(modifier = Modifier.height(16.dp))

            val progress = calculateMahadashaProgress(mahadasha, asOf)
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(StringKey.DASHA_PROGRESS),
                        fontFamily = SpaceGroteskFamily,
                        color = AppTheme.TextMuted,
                        fontSize = NeoVedicFontSizes.S11
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontFamily = SpaceGroteskFamily,
                        color = AppTheme.AccentPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = NeoVedicFontSizes.S11
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { progress.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)),
                    color = AppTheme.AccentPrimary,
                    trackColor = AppTheme.AccentPrimary.copy(alpha = 0.1f)
                )
            }
        }
    }
}

@Composable
private fun RelationshipChip(relationship: PlanetRelationship) {
    val language = LocalLanguage.current
    val (color, text) = when (relationship) {
        PlanetRelationship.FRIEND -> AppTheme.SuccessColor to stringResource(StringKeyDosha.RELATIONSHIP_FRIEND)
        PlanetRelationship.ENEMY -> AppTheme.ErrorColor to stringResource(StringKeyDosha.RELATIONSHIP_ENEMY)
        PlanetRelationship.NEUTRAL -> AppTheme.WarningColor to stringResource(StringKeyDosha.RELATIONSHIP_NEUTRAL)
        PlanetRelationship.SAME -> AppTheme.AccentPrimary to stringResource(StringKeyDosha.RELATIONSHIP_SAME)
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.ThinBorderWidth, color.copy(alpha = 0.3f))
    ) {
        Text(
            text = text.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = color,
            fontFamily = SpaceGroteskFamily,
            fontSize = NeoVedicFontSizes.S10,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun SystemInfoCard() {
    val language = LocalLanguage.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(NeoVedicTokens.ScreenPadding)
        ) {
            Text(
                text = stringResource(StringKeyDosha.ASHTOTTARI_SUBTITLE),
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.Bold,
                fontSize = NeoVedicFontSizes.S16,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(NeoVedicTokens.SpaceMD))

            // Planet periods
            val periods = listOf(
                StringKeyDosha.ASHTOTTARI_SUN_DURATION,
                StringKeyDosha.ASHTOTTARI_MOON_DURATION,
                StringKeyDosha.ASHTOTTARI_MARS_DURATION,
                StringKeyDosha.ASHTOTTARI_MERCURY_DURATION,
                StringKeyDosha.ASHTOTTARI_SATURN_DURATION,
                StringKeyDosha.ASHTOTTARI_JUPITER_DURATION,
                StringKeyDosha.ASHTOTTARI_RAHU_DURATION,
                StringKeyDosha.ASHTOTTARI_VENUS_DURATION
            )

            periods.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    row.forEach { period ->
                        Text(
                            text = stringResource(period),
                            fontFamily = PoppinsFontFamily,
                            color = AppTheme.TextSecondary,
                            fontSize = NeoVedicFontSizes.S13,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = AppTheme.DividerColor)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(StringKeyDosha.ASHTOTTARI_TOTAL_YEARS),
                color = AppTheme.AccentPrimary,
                fontFamily = SpaceGroteskFamily,
                fontWeight = FontWeight.Bold,
                fontSize = NeoVedicFontSizes.S14
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(StringKeyDosha.ASHTOTTARI_PLANETS_USED),
                color = AppTheme.TextMuted,
                fontFamily = PoppinsFontFamily,
                fontSize = NeoVedicFontSizes.S12
            )
        }
    }
}

@Composable
private fun TimelineContent(
    result: AshtottariDashaResult,
    expandedIndex: Int,
    onExpandChange: (Int) -> Unit,
    asOf: LocalDateTime
) {
    val language = LocalLanguage.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceSM)
    ) {
        itemsIndexed(result.mahadashas) { index, mahadasha ->
            MahadashaTimelineCard(
                mahadasha = mahadasha,
                isExpanded = expandedIndex == index,
                onExpandChange = { onExpandChange(index) },
                isCurrent = mahadasha.isCurrentlyRunning,
                asOf = asOf
            )
        }
    }
}

@Composable
private fun MahadashaTimelineCard(
    mahadasha: AshtottariMahadasha,
    isExpanded: Boolean,
    onExpandChange: () -> Unit,
    isCurrent: Boolean,
    asOf: LocalDateTime
) {
    val language = LocalLanguage.current
    val locale = if (language == Language.NEPALI) Locale("ne", "NP") else Locale.ENGLISH
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", locale)
    val antardashas = remember(mahadasha, asOf) {
        AshtottariDashaCalculator.calculateAntardashas(mahadasha, asOf)
    }
    val planetColor = ChartDetailColors.getPlanetColor(mahadasha.planet)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .vedicCornerMarkers(
                 color = if (isCurrent) AppTheme.AccentPrimary else Color.Transparent
            ),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        color = if (isCurrent) AppTheme.AccentPrimary.copy(alpha = 0.05f) else AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(
            NeoVedicTokens.BorderWidth, 
            if (isCurrent) AppTheme.AccentPrimary.copy(alpha = 0.5f) else AppTheme.BorderColor
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { onExpandChange() }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Planet indicator
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                planetColor.copy(alpha = 0.2f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mahadasha.planet.abbreviation,
                            color = planetColor,
                            fontFamily = SpaceGroteskFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = NeoVedicFontSizes.S14
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = mahadasha.planet.getLocalizedName(language),
                                fontFamily = CinzelDecorativeFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = NeoVedicFontSizes.S16,
                                color = AppTheme.TextPrimary
                            )
                            if (isCurrent) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = AppTheme.SuccessColor.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius)
                                ) {
                                    Text(
                                        text = stringResource(StringKeyDosha.CURRENT_LABEL).uppercase(),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        color = AppTheme.SuccessColor,
                                        fontFamily = SpaceGroteskFamily,
                                        fontSize = NeoVedicFontSizes.S9,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Text(
                            text = "${String.format("%.1f", mahadasha.actualYears)} ${stringResource(StringKey.YEARS)}",
                            fontFamily = SpaceGroteskFamily,
                            color = AppTheme.TextMuted,
                            fontSize = NeoVedicFontSizes.S11
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = mahadasha.startDate.format(dateFormatter),
                            fontFamily = SpaceGroteskFamily,
                            color = AppTheme.TextSecondary,
                            fontSize = NeoVedicFontSizes.S11
                        )
                        Text(
                            text = mahadasha.endDate.format(dateFormatter),
                            fontFamily = SpaceGroteskFamily,
                            color = AppTheme.TextSecondary,
                            fontSize = NeoVedicFontSizes.S11
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = AppTheme.TextMuted
                    )
                }
            }

            // Progress if current
            if (isCurrent) {
                Spacer(modifier = Modifier.height(12.dp))
                val progress = calculateMahadashaProgress(mahadasha, asOf)
                LinearProgressIndicator(
                    progress = { progress.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)),
                    color = AppTheme.AccentPrimary,
                    trackColor = AppTheme.AccentPrimary.copy(alpha = 0.1f)
                )
            }

            // Expanded content - Antardashas
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(StringKey.DASHA_ANTARDASHA),
                        fontFamily = SpaceGroteskFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    antardashas.forEach { antardasha ->
                        AntardashaRow(antardasha)
                    }
                }
            }
        }
    }
}

@Composable
private fun AntardashaRow(antardasha: AshtottariAntardasha) {
    val language = LocalLanguage.current
    val locale = if (language == Language.NEPALI) Locale.forLanguageTag("ne-NP") else Locale.ENGLISH
    val dateFormatter = remember(language) { DateTimeFormatter.ofPattern("dd/MM/yy", locale) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                if (antardasha.isCurrentlyRunning)
                    AppTheme.AccentPrimary.copy(alpha = 0.08f)
                else
                    Color.Transparent,
                RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
            )
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (antardasha.isCurrentlyRunning) AppTheme.AccentPrimary else AppTheme.DividerColor, 
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = antardasha.antardashaLord.getLocalizedName(language),
                fontFamily = PoppinsFontFamily,
                color = if (antardasha.isCurrentlyRunning) AppTheme.TextPrimary else AppTheme.TextSecondary,
                fontWeight = if (antardasha.isCurrentlyRunning) FontWeight.Bold else FontWeight.Medium,
                fontSize = NeoVedicFontSizes.S14
            )
        }
        Text(
            text = "${antardasha.startDate.format(dateFormatter)} - ${antardasha.endDate.format(dateFormatter)}",
            fontFamily = SpaceGroteskFamily,
            color = AppTheme.TextMuted,
            fontSize = NeoVedicFontSizes.S11
        )
    }
}

@Composable
private fun InterpretationContent(result: AshtottariDashaResult) {
    val language = LocalLanguage.current
    val interpretation = result.interpretation

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceMD)
    ) {
        // Key Themes
        if (interpretation.keyThemes.isNotEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = AppTheme.CardBackground,
                    shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                    border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
                ) {
                    Column(modifier = Modifier.padding(NeoVedicTokens.ScreenPadding)) {
                        Text(
                            text = stringResource(StringKeyDosha.KEY_THEMES),
                            fontFamily = CinzelDecorativeFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = NeoVedicFontSizes.S16,
                            color = AppTheme.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(interpretation.keyThemes) { theme ->
                                Surface(
                                    color = AppTheme.AccentPrimary.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
                                    border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.AccentPrimary.copy(alpha = 0.3f))
                                ) {
                                    Text(
                                        text = theme,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        color = AppTheme.AccentPrimary,
                                        fontFamily = SpaceGroteskFamily,
                                        fontSize = NeoVedicFontSizes.S13,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Combined Effects
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.CardBackground,
                shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
            ) {
                Column(modifier = Modifier.padding(NeoVedicTokens.ScreenPadding)) {
                    Text(
                        text = stringResource(StringKeyDosha.SUDARSHANA_COMBINED_ANALYSIS),
                        fontFamily = CinzelDecorativeFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = NeoVedicFontSizes.S16,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = interpretation.combinedEffects,
                        fontFamily = PoppinsFontFamily,
                        color = AppTheme.TextSecondary,
                        fontSize = NeoVedicFontSizes.S14,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Mahadasha Effects
        if (interpretation.mahadashaEffects.isNotEmpty()) {
            item {
                EffectsCard(
                    title = stringResource(StringKey.DASHA_MAHADASHA) + " " + stringResource(StringKeyDosha.EFFECTS_LABEL),
                    effects = interpretation.mahadashaEffects,
                    icon = Icons.Filled.Star
                )
            }
        }

        // Antardasha Effects
        if (interpretation.antardashaEffects.isNotEmpty()) {
            item {
                EffectsCard(
                    title = stringResource(StringKey.DASHA_ANTARDASHA) + " " + stringResource(StringKeyDosha.EFFECTS_LABEL),
                    effects = interpretation.antardashaEffects,
                    icon = Icons.Outlined.StarBorder
                )
            }
        }

        // Recommendations
        if (interpretation.recommendations.isNotEmpty()) {
            item {
                RecommendationsCard(interpretation.recommendations)
            }
        }
    }
}

@Composable
private fun EffectsCard(
    title: String,
    effects: List<String>,
    icon: ImageVector
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(NeoVedicTokens.ScreenPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(AppTheme.AccentPrimary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = NeoVedicFontSizes.S16,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            effects.forEach { effect ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("\u2022", color = AppTheme.AccentPrimary, modifier = Modifier.padding(end = 8.dp))
                    Text(
                        text = effect,
                        fontFamily = PoppinsFontFamily,
                        color = AppTheme.TextSecondary,
                        fontSize = NeoVedicFontSizes.S14,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendationsCard(recommendations: List<String>) {
    val language = LocalLanguage.current

    Surface(
        modifier = Modifier.fillMaxWidth().vedicCornerMarkers(color = AppTheme.SuccessColor),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(NeoVedicTokens.ScreenPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(AppTheme.SuccessColor.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lightbulb,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(StringKeyDosha.SCREEN_RECOMMENDATIONS),
                    fontFamily = CinzelDecorativeFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = NeoVedicFontSizes.S16,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            recommendations.forEachIndexed { index, rec ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "${index + 1}.",
                        fontFamily = SpaceGroteskFamily,
                        color = AppTheme.SuccessColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp),
                        fontSize = NeoVedicFontSizes.S13
                    )
                    Text(
                        text = rec,
                        fontFamily = PoppinsFontFamily,
                        color = AppTheme.TextSecondary,
                        fontSize = NeoVedicFontSizes.S14,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun AshtottariInfoDialog(onDismiss: () -> Unit) {
    val language = LocalLanguage.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.CardBackground,
        title = {
            Text(
                stringResource(StringKeyDosha.ASHTOTTARI_ABOUT),
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                stringResource(StringKeyDosha.ASHTOTTARI_ABOUT_DESC),
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(StringKey.BTN_CLOSE), color = AppTheme.AccentPrimary)
            }
        }
    )
}

// Helper functions
private fun calculateMahadashaProgress(mahadasha: AshtottariMahadasha, asOf: LocalDateTime): Double {
    val now = asOf
    if (now.isBefore(mahadasha.startDate)) return 0.0
    if (now.isAfter(mahadasha.endDate)) return 1.0

    val totalDays = ChronoUnit.DAYS.between(mahadasha.startDate, mahadasha.endDate).toDouble()
    val elapsedDays = ChronoUnit.DAYS.between(mahadasha.startDate, now).toDouble()
    return (elapsedDays / totalDays).coerceIn(0.0, 1.0)
}

@Composable
private fun getPlanetColor(planet: Planet): Color {
    return when (planet) {
        Planet.SUN -> com.astro.storm.ui.theme.PlanetSun
        Planet.MOON -> com.astro.storm.ui.theme.PlanetMercury
        Planet.MARS -> com.astro.storm.ui.theme.MarsRed
        Planet.MERCURY -> com.astro.storm.ui.theme.PlanetMercury
        Planet.JUPITER -> com.astro.storm.ui.theme.VedicGold
        Planet.VENUS -> com.astro.storm.ui.theme.PlanetVenus
        Planet.SATURN -> com.astro.storm.ui.theme.SlateDark
        Planet.RAHU -> com.astro.storm.ui.theme.PlanetRahu
        Planet.KETU -> com.astro.storm.ui.theme.PlanetVenus
        else -> AppTheme.AccentPrimary
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






