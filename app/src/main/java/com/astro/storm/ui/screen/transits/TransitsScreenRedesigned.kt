package com.astro.storm.ui.screen.transits

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.ephemeris.TransitAnalyzer
import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.NeoVedicEmptyState
import com.astro.storm.ui.components.common.NeoVedicStatusPill
import com.astro.storm.ui.components.common.NeoVedicTimelineItem
import com.astro.storm.ui.components.common.TabItem
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.NeoVedicTokens
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private enum class EphemerisMode(val titleKey: StringKey) {
    TIMELINE(StringKey.PERIOD_TODAY),
    POSITIONS(StringKey.TAB_CURRENT_POSITIONS),
    ASPECTS(StringKey.TAB_ASPECTS)
}

@Composable
fun TransitsScreenRedesigned(
    chart: VedicChart?,
    transitAnalyzer: TransitAnalyzer,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    val colors = AppTheme.current
    val zoneId = remember(chart) { resolveZoneId(chart?.birthData?.timezone) }
    val nowInZone by rememberCurrentDateTime(zoneId)
    val asOf = remember(nowInZone) { nowInZone.withSecond(0).withNano(0) }

    var selectedMode by rememberSaveable { mutableIntStateOf(0) }
    var selectedDayOffset by rememberSaveable { mutableIntStateOf(0) }

    val analysis = remember(chart, asOf) {
        chart?.let {
            runCatching { transitAnalyzer.analyzeTransits(it, asOf, language) }.getOrNull()
        }
    }

    val events = remember(analysis, language) {
        analysis?.let { mapToEphemerisEvents(it, language) } ?: emptyList()
    }

    val dayBuckets = remember(events, asOf) {
        val grouped = events.groupBy { it.date }
        val today = asOf.toLocalDate()
        listOf(today, today.plusDays(1), today.plusDays(2)).map { date ->
            date to (grouped[date] ?: emptyList())
        }
    }

    val modeTabs = listOf(
        TabItem(StringResources.get(EphemerisMode.TIMELINE.titleKey, language), accentColor = colors.AccentGold),
        TabItem(StringResources.get(EphemerisMode.POSITIONS.titleKey, language), accentColor = colors.AccentTeal),
        TabItem(StringResources.get(EphemerisMode.ASPECTS.titleKey, language), accentColor = colors.AccentPrimary)
    )

    Scaffold(
        containerColor = colors.ScreenBackground,
        topBar = {
            EphemerisTopBar(
                onBack = onBack,
                onAction = { selectedDayOffset = (selectedDayOffset + 1) % 3 }
            )
        }
    ) { padding ->
        if (chart == null) {
            NeoVedicEmptyState(
                title = stringResource(StringKey.FEATURE_TRANSITS),
                subtitle = stringResource(StringKey.NO_PROFILE_MESSAGE),
                icon = Icons.Outlined.Public,
                modifier = Modifier.padding(padding)
            )
            return@Scaffold
        }

        if (analysis == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colors.AccentPrimary)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colors.ScreenBackground)
        ) {
            ModernPillTabRow(
                tabs = modeTabs,
                selectedIndex = selectedMode,
                onTabSelected = { selectedMode = it },
                modifier = Modifier.padding(horizontal = NeoVedicTokens.SpaceSM, vertical = NeoVedicTokens.SpaceXS)
            )

            when (EphemerisMode.entries[selectedMode]) {
                EphemerisMode.TIMELINE -> EphemerisTimelineMode(
                    dayBuckets = dayBuckets,
                    selectedDayOffset = selectedDayOffset,
                    language = language
                )

                EphemerisMode.POSITIONS -> TransitPositionsMode(analysis.transitPositions, language)

                EphemerisMode.ASPECTS -> TransitAspectsMode(analysis)
            }
        }
    }
}

@Composable
private fun EphemerisTimelineMode(
    dayBuckets: List<Pair<LocalDate, List<EphemerisEventUi>>>,
    selectedDayOffset: Int,
    language: Language
) {
    val (date, events) = dayBuckets.getOrElse(selectedDayOffset) { dayBuckets.first() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = NeoVedicTokens.SpaceSM),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceXXS)
    ) {
        item {
            EphemerisDayHeader(
                title = date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")).uppercase(),
                trailingLabel = when (selectedDayOffset) {
                    0 -> StringResources.get(StringKey.PERIOD_TODAY, language).uppercase()
                    1 -> StringResources.get(StringKey.PERIOD_TOMORROW, language).uppercase()
                    else -> "+2D"
                }
            )
        }

        if (events.isEmpty()) {
            item {
                NeoVedicEmptyState(
                    title = StringResources.get(StringKey.FEATURE_TRANSITS, language),
                    subtitle = StringResources.get(StringKeyMatch.MISC_NO_DATA, language),
                    icon = Icons.Outlined.Schedule,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                )
            }
        } else {
            items(events.size) { index ->
                val event = events[index]
                val severityColor = when (event.severity) {
                    EventSeverity.FAVORABLE -> AppTheme.SuccessColor
                    EventSeverity.NEUTRAL -> AppTheme.AccentGold
                    EventSeverity.CHALLENGING -> AppTheme.WarningColor
                    EventSeverity.ALERT -> AppTheme.ErrorColor
                }
                NeoVedicTimelineItem(
                    timeLabel = event.timeLabel,
                    glyphText = event.primaryGlyphs.joinToString("  ") { it.text },
                    title = event.title,
                    subtitle = event.subtitle,
                    severityColor = severityColor,
                    isHighlighted = event.isHighlighted,
                    showConnector = index < events.lastIndex
                )
            }
        }
    }
}

@Composable
private fun EphemerisTopBar(
    onBack: () -> Unit,
    onAction: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.ScreenBackground,
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = NeoVedicTokens.ScreenPadding, vertical = NeoVedicTokens.SpaceXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = AppTheme.TextPrimary
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = NeoVedicTokens.SpaceSM)
            ) {
                Text(
                    text = "EPHEMERIS",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontSize = 26.sp,
                        letterSpacing = 0.4.sp
                    ),
                    color = AppTheme.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "LIVE CELESTIAL MOVEMENTS",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp
                    ),
                    color = AppTheme.AccentGold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Surface(
                modifier = Modifier.size(44.dp),
                shape = androidx.compose.foundation.shape.CircleShape,
                color = AppTheme.CardBackground,
                border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
            ) {
                IconButton(onClick = onAction) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = AppTheme.TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun EphemerisDayHeader(
    title: String,
    trailingLabel: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = NeoVedicTokens.ScreenPadding, vertical = NeoVedicTokens.SpaceSM),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 16.sp,
                    letterSpacing = 0.6.sp
                ),
                color = AppTheme.TextPrimary
            )
            Text(
                text = trailingLabel,
                style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.sp),
                color = AppTheme.AccentGold
            )
        }
    }
}

@Composable
private fun TransitPositionsMode(
    positions: List<PlanetPosition>,
    language: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = NeoVedicTokens.ScreenPadding, vertical = NeoVedicTokens.SpaceSM),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceSM)
    ) {
        items(positions.filter { it.planet in Planet.MAIN_PLANETS }) { position ->
            Surface(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                color = AppTheme.CardBackground,
                border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
            ) {
                Column(modifier = Modifier.padding(NeoVedicTokens.SpaceSM)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = position.planet.getLocalizedName(language),
                            style = MaterialTheme.typography.titleSmall,
                            color = AppTheme.TextPrimary
                        )
                        NeoVedicStatusPill(
                            text = if (position.isRetrograde) "R" else "D",
                            textColor = if (position.isRetrograde) AppTheme.ErrorColor else AppTheme.SuccessColor,
                            containerColor = if (position.isRetrograde) AppTheme.ErrorColor.copy(alpha = 0.12f) else AppTheme.SuccessColor.copy(alpha = 0.12f)
                        )
                    }
                    Spacer(modifier = Modifier.height(NeoVedicTokens.SpaceXXS))
                    Text(
                        text = "${position.sign.getLocalizedName(language)} ${formatDegree(position.longitude)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun TransitAspectsMode(analysis: TransitAnalyzer.TransitAnalysis) {
    val language = analysis.language
    val topAspects = analysis.transitAspects.sortedByDescending { it.strength }.take(16)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = NeoVedicTokens.ScreenPadding, vertical = NeoVedicTokens.SpaceSM),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceSM)
    ) {
        if (topAspects.isEmpty()) {
            item {
                NeoVedicEmptyState(
                    title = stringResource(StringKey.TAB_ASPECTS),
                    subtitle = stringResource(StringKeyMatch.MISC_NO_DATA),
                    icon = Icons.Outlined.Sync,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                )
            }
        } else {
            items(topAspects) { aspect ->
                val strengthPercent = (aspect.strength * 100).roundToInt()
                val aspectColor = when {
                    strengthPercent >= 75 -> AppTheme.SuccessColor
                    strengthPercent >= 50 -> AppTheme.AccentGold
                    else -> AppTheme.WarningColor
                }

                Surface(
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                    color = AppTheme.CardBackground,
                    border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
                ) {
                    Column(modifier = Modifier.padding(NeoVedicTokens.SpaceSM)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${aspect.transitingPlanet.getLocalizedName(language)} -> ${aspect.natalPlanet.getLocalizedName(language)}",
                                style = MaterialTheme.typography.titleSmall,
                                color = AppTheme.TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            NeoVedicStatusPill(
                                text = "$strengthPercent%",
                                textColor = aspectColor,
                                containerColor = aspectColor.copy(alpha = 0.12f)
                            )
                        }
                        Spacer(modifier = Modifier.height(NeoVedicTokens.SpaceXXS))
                        Text(
                            text = aspect.interpretation,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }
            }
        }
    }
}

private fun formatDegree(longitude: Double): String {
    val degInSign = ((longitude % 30.0) + 30.0) % 30.0
    val deg = degInSign.toInt()
    val min = ((degInSign - deg) * 60).toInt()
    return "${deg}deg ${min}m"
}

private fun resolveZoneId(timezone: String?): ZoneId {
    if (timezone.isNullOrBlank()) return ZoneId.systemDefault()
    return try {
        ZoneId.of(timezone)
    } catch (_: DateTimeException) {
        val numericHours = timezone.trim().toDoubleOrNull()
        if (numericHours != null) {
            val totalSeconds = (numericHours * 3600.0).roundToInt().coerceIn(-18 * 3600, 18 * 3600)
            ZoneOffset.ofTotalSeconds(totalSeconds)
        } else {
            ZoneId.systemDefault()
        }
    }
}

@Composable
private fun rememberCurrentDateTime(zoneId: ZoneId): androidx.compose.runtime.State<LocalDateTime> =
    produceState(initialValue = LocalDateTime.now(zoneId), zoneId) {
        while (true) {
            val now = LocalDateTime.now(zoneId)
            value = now.withSecond(0).withNano(0)
            val delayMillis = ((60 - now.second) * 1000L) - (now.nano / 1_000_000L)
            delay(delayMillis.coerceAtLeast(250L))
        }
    }
