package com.astro.storm.ui.screen.main

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAdvanced
import com.astro.storm.core.common.StringKeyInterface
import com.astro.storm.core.common.StringKeyUICommon
import com.astro.storm.core.common.StringKeyShadbala
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.CinzelDecorativeFamily
import com.astro.storm.ui.theme.NeoVedicTokens
import com.astro.storm.ui.theme.PoppinsFontFamily
import com.astro.storm.ui.theme.SpaceGroteskFamily
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.roundToInt

// ============================================================================
// NEO-VEDIC DESIGN TOKENS
// "Ethereal Vedic Grid" - Sharp, parchment-flat, scripture-like
// ============================================================================
private object HomeDesignTokens {
    val ScreenPadding = NeoVedicTokens.ScreenPadding
    val SectionSpacing = NeoVedicTokens.SectionSpacing
    val CardSpacing = NeoVedicTokens.CardSpacing
    val CardCornerRadius = NeoVedicTokens.CardCornerRadius
    val QuickActionSize = 72.dp
    val QuickActionIconSize = 26.dp
    val HeroCardMinHeight = 160.dp
    val SnapshotCardMinHeight = 148.dp
    val CornerMarkerLength = 12.dp       // L-shaped corner markers
    val BorderWidth = NeoVedicTokens.BorderWidth
}

// ============================================================================
// MAIN HOME TAB COMPOSABLE - CELESTIAL DASHBOARD
// ============================================================================
@Composable
fun HomeTab(
    chart: VedicChart?,
    onFeatureClick: (InsightFeature) -> Unit,
    onAddNewChart: () -> Unit = {},
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(bottom = 100.dp)
) {
    if (chart == null) {
        EmptyHomeState(
            onCreateProfile = onAddNewChart,
            modifier = modifier
        )
        return
    }

    val listState = rememberLazyListState()
    val colors = AppTheme.current
    val language = LocalLanguage.current

    // Pre-calculate dasha data once
    val dashaTimeline = remember(chart) {
        try {
            DashaCalculator.calculateDashaTimeline(chart)
        } catch (e: Exception) {
            null
        }
    }
    val currentDasha = dashaTimeline?.currentMahadasha

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(colors.ScreenBackground),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(HomeDesignTokens.SectionSpacing)
    ) {
        // Hero Section - Current Maha Dasha
        item(key = "hero_dasha") {
            HeroDashaCard(
                chart = chart,
                currentDasha = currentDasha,
                language = language,
                onClick = { onFeatureClick(InsightFeature.DASHAS) }
            )
        }

        // Quick Actions - Bento Grid
        item(key = "quick_actions") {
            QuickActionsSection(
                onFeatureClick = onFeatureClick,
                language = language
            )
        }

        // Today's Snapshot - Panchanga Strip
        item(key = "today_snapshot") {
            TodaySnapshotSection(
                chart = chart,
                language = language,
                onPanchangaClick = { onFeatureClick(InsightFeature.PANCHANGA) },
                onTransitsClick = { onFeatureClick(InsightFeature.TRANSITS) }
            )
        }

        // Chart Analysis Section Header
        item(key = "chart_analysis_header") {
            SectionHeader(
                title = stringResource(StringKey.HOME_CHART_ANALYSIS),
                subtitle = stringResource(StringKey.HOME_EXPLORE_FEATURES),
                modifier = Modifier.padding(horizontal = HomeDesignTokens.ScreenPadding)
            )
        }

        // Feature Categories
        item(key = "category_core") {
            FeatureCategoryCard(
                title = stringResource(StringKey.CATEGORY_CORE_ANALYSIS),
                subtitle = stringResource(StringKey.CATEGORY_CORE_DESC),
                icon = Icons.Outlined.GridView,
                accentColor = colors.AccentPrimary,
                features = FeatureCategory.CORE.features,
                onFeatureClick = onFeatureClick,
                language = language
            )
        }

        item(key = "category_dashas") {
            FeatureCategoryCard(
                title = stringResource(StringKey.CATEGORY_TIMING_SYSTEMS),
                subtitle = stringResource(StringKey.CATEGORY_TIMING_DESC),
                icon = Icons.Outlined.Timeline,
                accentColor = colors.LifeAreaSpiritual,
                features = FeatureCategory.TIMING_SYSTEMS.features,
                onFeatureClick = onFeatureClick,
                language = language
            )
        }

        item(key = "category_predictions") {
            FeatureCategoryCard(
                title = stringResource(StringKey.CATEGORY_PREDICTIONS),
                subtitle = stringResource(StringKey.CATEGORY_PREDICTIONS_DESC),
                icon = Icons.Outlined.AutoAwesome,
                accentColor = colors.AccentGold,
                features = FeatureCategory.PREDICTIONS.features,
                onFeatureClick = onFeatureClick,
                language = language
            )
        }

        item(key = "category_strength") {
            FeatureCategoryCard(
                title = stringResource(StringKey.CATEGORY_STRENGTH_ANALYSIS),
                subtitle = stringResource(StringKey.CATEGORY_STRENGTH_DESC),
                icon = Icons.Outlined.Speed,
                accentColor = colors.SuccessColor,
                features = FeatureCategory.STRENGTH_ANALYSIS.features,
                onFeatureClick = onFeatureClick,
                language = language
            )
        }

        item(key = "category_advanced") {
            FeatureCategoryCard(
                title = stringResource(StringKey.CATEGORY_ADVANCED),
                subtitle = stringResource(StringKey.CATEGORY_ADVANCED_DESC),
                icon = Icons.Outlined.Psychology,
                accentColor = colors.AccentTeal,
                features = FeatureCategory.ADVANCED.features,
                onFeatureClick = onFeatureClick,
                language = language
            )
        }

        item(key = "category_remedial") {
            FeatureCategoryCard(
                title = stringResource(StringKey.CATEGORY_REMEDIAL),
                subtitle = stringResource(StringKey.CATEGORY_REMEDIAL_DESC),
                icon = Icons.Outlined.Spa,
                accentColor = colors.LifeAreaHealth,
                features = FeatureCategory.REMEDIAL.features,
                onFeatureClick = onFeatureClick,
                language = language
            )
        }

        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ============================================================================
// VEDIC CORNER MARKERS
// L-shaped corner accents on cards for that engraved/stamped feel
// ============================================================================
private fun Modifier.vedicCornerMarkers(
    color: Color,
    cornerLength: Dp = HomeDesignTokens.CornerMarkerLength,
    strokeWidth: Dp = 1.dp
) = this.drawWithContent {
    drawContent()
    val len = cornerLength.toPx()
    val sw = strokeWidth.toPx()

    // Top-left
    drawLine(color, Offset(0f, sw / 2), Offset(len, sw / 2), sw)
    drawLine(color, Offset(sw / 2, 0f), Offset(sw / 2, len), sw)

    // Top-right
    drawLine(color, Offset(size.width - len, sw / 2), Offset(size.width, sw / 2), sw)
    drawLine(color, Offset(size.width - sw / 2, 0f), Offset(size.width - sw / 2, len), sw)

    // Bottom-left
    drawLine(color, Offset(0f, size.height - sw / 2), Offset(len, size.height - sw / 2), sw)
    drawLine(color, Offset(sw / 2, size.height - len), Offset(sw / 2, size.height), sw)

    // Bottom-right
    drawLine(color, Offset(size.width - len, size.height - sw / 2), Offset(size.width, size.height - sw / 2), sw)
    drawLine(color, Offset(size.width - sw / 2, size.height - len), Offset(size.width - sw / 2, size.height), sw)
}

// ============================================================================
// HERO DASHA CARD - Maha Dasha Period Display
// Neo-Vedic: Sharp corners, hairline border, Cinzel header, Space Grotesk data
// ============================================================================
@Composable
private fun HeroDashaCard(
    chart: VedicChart,
    currentDasha: DashaCalculator.Mahadasha?,
    language: Language,
    onClick: () -> Unit
) {
    val colors = AppTheme.current
    val today = remember(chart) { LocalDate.now(resolveZoneId(chart.birthData.timezone)) }

    val planetColor = currentDasha?.planet?.let { getPlanetDisplayColor(it) } ?: AppTheme.AccentGold

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDesignTokens.ScreenPadding)
            .padding(top = HomeDesignTokens.ScreenPadding)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .vedicCornerMarkers(color = planetColor)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(HomeDesignTokens.CardCornerRadius),
            color = colors.CardBackground,
            border = androidx.compose.foundation.BorderStroke(
                HomeDesignTokens.BorderWidth,
                colors.BorderColor
            ),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = HomeDesignTokens.HeroCardMinHeight)
                    .padding(24.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Label - Space Grotesk uppercase
                    Text(
                        text = stringResource(StringKey.CURRENT_MAHA_DASHA).uppercase(),
                        fontFamily = SpaceGroteskFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S10,
                        letterSpacing = 2.sp,
                        color = planetColor
                    )

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = stringResource(StringKey.BTN_VIEW_DETAILS),
                        tint = colors.TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (currentDasha != null) {
                    // Planet Name Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Planet glyph circle
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .border(
                                    width = 1.dp,
                                    color = planetColor.copy(alpha = 0.4f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentDasha.planet.getLocalizedName(language).take(2),
                                fontFamily = CinzelDecorativeFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S16,
                                color = planetColor
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            // Planet name in Cinzel
                            Text(
                                text = "${currentDasha.planet.getLocalizedName(language)} ${stringResource(StringKey.HOME_DASHA_LABEL)}",
                                fontFamily = CinzelDecorativeFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S19,
                                color = colors.TextPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Remaining time in Space Grotesk
                            val remainingDays = ChronoUnit.DAYS.between(today, currentDasha.endDate.toLocalDate())
                            val years = remainingDays / 365
                            val months = (remainingDays % 365) / 30

                            Text(
                                text = if (years > 0) {
                                    stringResource(StringKey.REMAINING_PERIOD_YEARS, years.toString(), months.toString())
                                } else {
                                    stringResource(StringKey.REMAINING_PERIOD_MONTHS, months.toString())
                                },
                                fontFamily = SpaceGroteskFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
                                color = colors.TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Progress Bar - thin, elegant
                    val totalDays = ChronoUnit.DAYS.between(currentDasha.startDate.toLocalDate(), currentDasha.endDate.toLocalDate()).toFloat()
                    val elapsedDays = ChronoUnit.DAYS.between(currentDasha.startDate.toLocalDate(), today).toFloat()
                    val progress = (elapsedDays / totalDays).coerceIn(0f, 1f)

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = currentDasha.startDate.format(DateTimeFormatter.ofPattern("MMM yyyy")),
                                fontFamily = SpaceGroteskFamily,
                                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S10,
                                letterSpacing = 0.5.sp,
                                color = colors.TextMuted
                            )
                            Text(
                                text = currentDasha.endDate.format(DateTimeFormatter.ofPattern("MMM yyyy")),
                                fontFamily = SpaceGroteskFamily,
                                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S10,
                                letterSpacing = 0.5.sp,
                                color = colors.TextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Thin progress track
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp),
                            color = planetColor,
                            trackColor = colors.DividerColor
                        )
                    }
                } else {
                    Text(
                        text = stringResource(StringKey.TAP_TO_VIEW_DASHAS),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S16,
                        color = colors.TextMuted
                    )
                }
            }
        }
    }
}

// ============================================================================
// QUICK ACTIONS - 2x2 Bento Grid
// Sharp square cards, hairline borders, Space Grotesk labels
// ============================================================================
@Composable
private fun QuickActionsSection(
    onFeatureClick: (InsightFeature) -> Unit,
    language: Language
) {
    val colors = AppTheme.current
    val quickActions = listOf(
        QuickAction(InsightFeature.FULL_CHART, Icons.Outlined.GridView, colors.AccentPrimary),
        QuickAction(InsightFeature.YOGAS, Icons.Outlined.AutoAwesome, colors.AccentGold),
        QuickAction(InsightFeature.PREDICTIONS, Icons.Outlined.TipsAndUpdates, colors.LifeAreaSpiritual),
        QuickAction(InsightFeature.MATCHMAKING, Icons.Outlined.Favorite, colors.LifeAreaLove)
    )

    Column(
        modifier = Modifier.padding(horizontal = HomeDesignTokens.ScreenPadding)
    ) {
        SectionHeader(
            title = stringResource(StringKey.QUICK_ACTIONS),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 2x2 bento grid
        Column(
            verticalArrangement = Arrangement.spacedBy(HomeDesignTokens.CardSpacing)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HomeDesignTokens.CardSpacing)
            ) {
                quickActions.take(2).forEach { action ->
                    QuickActionItem(
                        feature = action.feature,
                        icon = action.icon,
                        accentColor = action.color,
                        language = language,
                        onClick = { onFeatureClick(action.feature) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HomeDesignTokens.CardSpacing)
            ) {
                quickActions.drop(2).forEach { action ->
                    QuickActionItem(
                        feature = action.feature,
                        icon = action.icon,
                        accentColor = action.color,
                        language = language,
                        onClick = { onFeatureClick(action.feature) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionItem(
    feature: InsightFeature,
    icon: ImageVector,
    accentColor: Color,
    language: Language,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current

    Surface(
        modifier = modifier
            .aspectRatio(1.3f),
        shape = RoundedCornerShape(HomeDesignTokens.CardCornerRadius),
        color = colors.CardBackground,
        border = androidx.compose.foundation.BorderStroke(
            HomeDesignTokens.BorderWidth,
            colors.BorderColor
        ),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon at top
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(HomeDesignTokens.QuickActionIconSize)
            )

            // Label at bottom - Space Grotesk
            Text(
                text = feature.getLocalizedTitle(language),
                fontFamily = SpaceGroteskFamily,
                fontWeight = FontWeight.Medium,
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
                letterSpacing = 0.5.sp,
                color = colors.TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private data class QuickAction(
    val feature: InsightFeature,
    val icon: ImageVector,
    val color: Color
)

// ============================================================================
// TODAY'S SNAPSHOT - Panchanga Strip
// Two side-by-side flat cards with hairline borders
// ============================================================================
@Composable
private fun TodaySnapshotSection(
    chart: VedicChart,
    language: Language,
    onPanchangaClick: () -> Unit,
    onTransitsClick: () -> Unit
) {
    val colors = AppTheme.current
    val today = remember(chart) { LocalDate.now(resolveZoneId(chart.birthData.timezone)) }

    Column(
        modifier = Modifier.padding(horizontal = HomeDesignTokens.ScreenPadding)
    ) {
        SectionHeader(
            title = stringResource(StringKey.TODAYS_SNAPSHOT),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(HomeDesignTokens.CardSpacing)
        ) {
            // Panchanga Card
            SnapshotCard(
                title = stringResource(StringKey.FEATURE_PANCHANGA),
                icon = Icons.Outlined.CalendarMonth,
                accentColor = colors.AccentGold,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                onClick = onPanchangaClick
            ) {
                Text(
                    text = today.format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S16,
                    color = colors.TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = today.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S11,
                    letterSpacing = 0.5.sp,
                    color = colors.TextMuted
                )
            }

            // Transits Card
            SnapshotCard(
                title = stringResource(StringKey.FEATURE_TRANSITS),
                icon = Icons.Outlined.Sync,
                accentColor = colors.AccentTeal,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                onClick = onTransitsClick
            ) {
                Text(
                    text = stringResource(StringKey.VIEW_CURRENT_TRANSITS),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S15,
                    color = colors.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun SnapshotCard(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = AppTheme.current

    Surface(
        modifier = modifier.heightIn(min = HomeDesignTokens.SnapshotCardMinHeight),
        shape = RoundedCornerShape(HomeDesignTokens.CardCornerRadius),
        color = colors.CardBackground,
        border = androidx.compose.foundation.BorderStroke(
            HomeDesignTokens.BorderWidth,
            colors.BorderColor
        ),
        tonalElevation = NeoVedicTokens.CardElevation,
        shadowElevation = NeoVedicTokens.CardElevation,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title.uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S10,
                    letterSpacing = 1.5.sp,
                    color = colors.TextMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

// ============================================================================
// FEATURE CATEGORY CARD
// Expandable section with hairline border, Cinzel title, grid of features
// ============================================================================
@Composable
private fun FeatureCategoryCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    features: List<InsightFeature>,
    onFeatureClick: (InsightFeature) -> Unit,
    language: Language
) {
    val colors = AppTheme.current
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDesignTokens.ScreenPadding)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(HomeDesignTokens.CardCornerRadius),
        color = colors.CardBackground,
        border = androidx.compose.foundation.BorderStroke(
            HomeDesignTokens.BorderWidth,
            colors.BorderColor
        ),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column {
            // Header - Always visible
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontFamily = CinzelDecorativeFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S14,
                        color = colors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S11,
                        color = colors.TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Feature count
                Text(
                    text = features.size.toString(),
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
                    letterSpacing = 0.5.sp,
                    color = accentColor
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Expand indicator
                val rotation by animateFloatAsState(
                    targetValue = if (isExpanded) 90f else 0f,
                    animationSpec = tween(300),
                    label = "expand_rotation"
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = if (isExpanded) stringResource(StringKeyUICommon.COLLAPSE) else stringResource(StringKeyUICommon.EXPAND),
                    tint = colors.TextMuted,
                    modifier = Modifier
                        .size(18.dp)
                        .graphicsLayer { rotationZ = rotation }
                )
            }

            // Expanded Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(200)) + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 20.dp
                    )
                ) {
                    // Hairline divider
                    HorizontalDivider(
                        color = colors.DividerColor,
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Features Grid - 2 columns
                    val chunkedFeatures = features.chunked(2)
                    chunkedFeatures.forEach { rowFeatures ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = if (rowFeatures != chunkedFeatures.last()) 8.dp else 0.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowFeatures.forEach { feature ->
                                CompactFeatureCard(
                                    feature = feature,
                                    language = language,
                                    onClick = { onFeatureClick(feature) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowFeatures.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactFeatureCard(
    feature: InsightFeature,
    language: Language,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(HomeDesignTokens.CardCornerRadius),
        color = colors.ChipBackground,
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp,
            colors.BorderColor.copy(alpha = 0.5f)
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = null,
                tint = featureDisplayColor(feature),
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = feature.getLocalizedTitle(language),
                fontFamily = SpaceGroteskFamily,
                fontWeight = FontWeight.Medium,
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
                color = colors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ============================================================================
// SECTION HEADER - Cinzel title, Space Grotesk subtitle
// ============================================================================
@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    val colors = AppTheme.current

    Column(modifier = modifier) {
        Text(
            text = title.uppercase(),
            fontFamily = SpaceGroteskFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S11,
            letterSpacing = 2.sp,
            color = colors.TextMuted
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S14,
                color = colors.TextSubtle
            )
        }
    }
}

// ============================================================================
// EMPTY STATE - Neo-Vedic styled
// ============================================================================
@Composable
private fun EmptyHomeState(
    onCreateProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.ScreenBackground)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Vedic geometric icon
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(
                        width = 1.dp,
                        color = colors.AccentGold.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
                    .vedicCornerMarkers(color = colors.AccentGold),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonAddAlt,
                    contentDescription = null,
                    tint = colors.AccentGold,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(StringKey.NO_PROFILE_SELECTED),
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.Bold,
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S17,
                color = colors.TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(StringKey.NO_PROFILE_MESSAGE),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S16,
                color = colors.TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // CTA button - flat, sharp corners, outlined
            OutlinedButton(
                onClick = onCreateProfile,
                modifier = Modifier
                    .height(48.dp)
                    .widthIn(min = 200.dp),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    colors.AccentGold
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.AccentGold
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(StringKey.BTN_CREATE_CHART).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
                    letterSpacing = 1.5.sp
                )
            }
        }
    }
}

// ============================================================================
// FEATURE CATEGORIES
// ============================================================================
enum class FeatureCategory(val features: List<InsightFeature>) {
    CORE(
        listOf(
            InsightFeature.FULL_CHART,
            InsightFeature.PLANETS,
            InsightFeature.NAKSHATRA_ANALYSIS,
            InsightFeature.DIVISIONAL_CHARTS,
            InsightFeature.SHODASHVARGA,
            InsightFeature.ASHTAKAVARGA
        )
    ),
    TIMING_SYSTEMS(
        listOf(
            InsightFeature.DASHAS,
            InsightFeature.YOGINI_DASHA,
            InsightFeature.CHARA_DASHA,
            InsightFeature.ASHTOTTARI_DASHA,
            InsightFeature.SUDARSHANA_CHAKRA,
            InsightFeature.KALACHAKRA_DASHA,
            InsightFeature.SHOOLA_DASHA,
            InsightFeature.DASHA_SANDHI,
            InsightFeature.DRIG_DASHA
        )
    ),
    PREDICTIONS(
        listOf(
            InsightFeature.PREDICTIONS,
            InsightFeature.YOGAS,
            InsightFeature.TRANSITS,
            InsightFeature.VARSHAPHALA,
            InsightFeature.PRASHNA,
            InsightFeature.MUHURTA,
            InsightFeature.NATIVE_ANALYSIS,
            InsightFeature.SAPTAMSA
        )
    ),
    STRENGTH_ANALYSIS(
        listOf(
            InsightFeature.SHADBALA,
            InsightFeature.STHANA_BALA,
            InsightFeature.KALA_BALA,
            InsightFeature.DRIG_BALA,
            InsightFeature.ISHTA_KASHTA_PHALA,
            InsightFeature.AVASTHA
        )
    ),
    ADVANCED(
        listOf(
            InsightFeature.ARGALA,
            InsightFeature.ARUDHA_PADA,
            InsightFeature.BHRIGU_BINDU,
            InsightFeature.SARVATOBHADRA_CHAKRA,
            InsightFeature.GRAHA_YUDDHA,
            InsightFeature.MRITYU_BHAGA,
            InsightFeature.GOCHARA_VEDHA,
            InsightFeature.NADI_AMSHA,
            InsightFeature.NITYA_YOGA,
            InsightFeature.TARABALA,
            InsightFeature.UPACHAYA_TRANSIT,
            InsightFeature.ASHTAVARGA_TRANSIT,
            InsightFeature.KAKSHYA_TRANSIT,
            InsightFeature.JAIMINI_KARAKA
        )
    ),
    REMEDIAL(
        listOf(
            InsightFeature.REMEDIES,
            InsightFeature.LAL_KITAB,
            InsightFeature.SAHAM,
            InsightFeature.MARAKA,
            InsightFeature.BADHAKA,
            InsightFeature.KEMADRUMA_YOGA,
            InsightFeature.PANCH_MAHAPURUSHA,
            InsightFeature.VIPAREETA_RAJA_YOGA,
            InsightFeature.MATCHMAKING,
            InsightFeature.CHART_COMPARISON,
            InsightFeature.PANCHANGA
        )
    )
}

// ============================================================================
// HELPER FUNCTIONS
// ============================================================================
@Composable
private fun getPlanetDisplayColor(planet: Planet): Color {
    return when (planet) {
        Planet.SUN -> AppTheme.PlanetSun
        Planet.MOON -> AppTheme.PlanetMoon
        Planet.MARS -> AppTheme.PlanetMars
        Planet.MERCURY -> AppTheme.PlanetMercury
        Planet.JUPITER -> AppTheme.PlanetJupiter
        Planet.VENUS -> AppTheme.PlanetVenus
        Planet.SATURN -> AppTheme.PlanetSaturn
        Planet.RAHU -> AppTheme.PlanetRahu
        Planet.KETU -> AppTheme.PlanetKetu
        else -> AppTheme.AccentPrimary
    }
}

@Composable
private fun featureDisplayColor(feature: InsightFeature): Color {
    return feature.colorToken.resolve()
}

enum class FeatureColorToken {
    AccentPrimary,
    AccentGold,
    AccentTeal,
    SuccessColor,
    WarningColor,
    ErrorColor,
    LifeAreaCareer,
    LifeAreaLove,
    LifeAreaHealth,
    LifeAreaFinance,
    LifeAreaSpiritual,
    PlanetMoon
}

@Composable
private fun FeatureColorToken.resolve(): Color {
    return when (this) {
        FeatureColorToken.AccentPrimary -> AppTheme.AccentPrimary
        FeatureColorToken.AccentGold -> AppTheme.AccentGold
        FeatureColorToken.AccentTeal -> AppTheme.AccentTeal
        FeatureColorToken.SuccessColor -> AppTheme.SuccessColor
        FeatureColorToken.WarningColor -> AppTheme.WarningColor
        FeatureColorToken.ErrorColor -> AppTheme.ErrorColor
        FeatureColorToken.LifeAreaCareer -> AppTheme.LifeAreaCareer
        FeatureColorToken.LifeAreaLove -> AppTheme.LifeAreaLove
        FeatureColorToken.LifeAreaHealth -> AppTheme.LifeAreaHealth
        FeatureColorToken.LifeAreaFinance -> AppTheme.LifeAreaFinance
        FeatureColorToken.LifeAreaSpiritual -> AppTheme.LifeAreaSpiritual
        FeatureColorToken.PlanetMoon -> AppTheme.PlanetMoon
    }
}

// ============================================================================
// INSIGHT FEATURE ENUM (Preserved from original)
// ============================================================================
@Stable
enum class InsightFeature(
    val titleKey: StringKeyInterface,
    val descriptionKey: StringKeyInterface,
    val icon: ImageVector,
    val colorToken: FeatureColorToken,
    val isImplemented: Boolean
) {
    FULL_CHART(
        titleKey = StringKey.FEATURE_BIRTH_CHART,
        descriptionKey = StringKey.FEATURE_BIRTH_CHART_DESC,
        icon = Icons.Outlined.GridView,
        colorToken = FeatureColorToken.AccentPrimary,
        isImplemented = true
    ),
    PLANETS(
        titleKey = StringKey.FEATURE_PLANETS,
        descriptionKey = StringKey.FEATURE_PLANETS_DESC,
        icon = Icons.Outlined.Public,
        colorToken = FeatureColorToken.LifeAreaCareer,
        isImplemented = true
    ),
    YOGAS(
        titleKey = StringKey.FEATURE_YOGAS,
        descriptionKey = StringKey.FEATURE_YOGAS_DESC,
        icon = Icons.Outlined.AutoAwesome,
        colorToken = FeatureColorToken.AccentGold,
        isImplemented = true
    ),
    DASHAS(
        titleKey = StringKey.FEATURE_DASHAS,
        descriptionKey = StringKey.FEATURE_DASHAS_DESC,
        icon = Icons.Outlined.Timeline,
        colorToken = FeatureColorToken.LifeAreaSpiritual,
        isImplemented = true
    ),
    TRANSITS(
        titleKey = StringKey.FEATURE_TRANSITS,
        descriptionKey = StringKey.FEATURE_TRANSITS_DESC,
        icon = Icons.Outlined.Sync,
        colorToken = FeatureColorToken.AccentTeal,
        isImplemented = true
    ),
    ASHTAKAVARGA(
        titleKey = StringKey.FEATURE_ASHTAKAVARGA,
        descriptionKey = StringKey.FEATURE_ASHTAKAVARGA_DESC,
        icon = Icons.Outlined.BarChart,
        colorToken = FeatureColorToken.SuccessColor,
        isImplemented = true
    ),
    PANCHANGA(
        titleKey = StringKey.FEATURE_PANCHANGA,
        descriptionKey = StringKey.FEATURE_PANCHANGA_DESC,
        icon = Icons.Outlined.CalendarMonth,
        colorToken = FeatureColorToken.LifeAreaFinance,
        isImplemented = true
    ),
    MATCHMAKING(
        titleKey = StringKey.FEATURE_MATCHMAKING,
        descriptionKey = StringKey.FEATURE_MATCHMAKING_DESC,
        icon = Icons.Outlined.Favorite,
        colorToken = FeatureColorToken.LifeAreaLove,
        isImplemented = true
    ),
    MUHURTA(
        titleKey = StringKey.FEATURE_MUHURTA,
        descriptionKey = StringKey.FEATURE_MUHURTA_DESC,
        icon = Icons.Outlined.AccessTime,
        colorToken = FeatureColorToken.WarningColor,
        isImplemented = true
    ),
    REMEDIES(
        titleKey = StringKey.FEATURE_REMEDIES,
        descriptionKey = StringKey.FEATURE_REMEDIES_DESC,
        icon = Icons.Outlined.Spa,
        colorToken = FeatureColorToken.LifeAreaHealth,
        isImplemented = true
    ),
    VARSHAPHALA(
        titleKey = StringKey.FEATURE_VARSHAPHALA,
        descriptionKey = StringKey.FEATURE_VARSHAPHALA_DESC,
        icon = Icons.Outlined.Cake,
        colorToken = FeatureColorToken.LifeAreaCareer,
        isImplemented = true
    ),
    PRASHNA(
        titleKey = StringKey.FEATURE_PRASHNA,
        descriptionKey = StringKey.FEATURE_PRASHNA_DESC,
        icon = Icons.Outlined.HelpOutline,
        colorToken = FeatureColorToken.AccentTeal,
        isImplemented = true
    ),
    CHART_COMPARISON(
        titleKey = StringKey.FEATURE_SYNASTRY,
        descriptionKey = StringKey.FEATURE_SYNASTRY_DESC,
        icon = Icons.Outlined.CompareArrows,
        colorToken = FeatureColorToken.LifeAreaFinance,
        isImplemented = true
    ),
    NAKSHATRA_ANALYSIS(
        titleKey = StringKey.FEATURE_NAKSHATRAS,
        descriptionKey = StringKey.FEATURE_NAKSHATRAS_DESC,
        icon = Icons.Outlined.Stars,
        colorToken = FeatureColorToken.AccentGold,
        isImplemented = true
    ),
    SHADBALA(
        titleKey = StringKey.FEATURE_SHADBALA,
        descriptionKey = StringKey.FEATURE_SHADBALA_DESC,
        icon = Icons.Outlined.Speed,
        colorToken = FeatureColorToken.SuccessColor,
        isImplemented = true
    ),
    SHODASHVARGA(
        titleKey = StringKey.FEATURE_SHODASHVARGA,
        descriptionKey = StringKey.FEATURE_SHODASHVARGA_DESC,
        icon = Icons.Outlined.GridView,
        colorToken = FeatureColorToken.AccentGold,
        isImplemented = true
    ),
    YOGINI_DASHA(
        titleKey = StringKey.FEATURE_YOGINI_DASHA,
        descriptionKey = StringKey.FEATURE_YOGINI_DASHA_DESC,
        icon = Icons.Outlined.Timeline,
        colorToken = FeatureColorToken.LifeAreaLove,
        isImplemented = true
    ),
    ARGALA(
        titleKey = StringKey.FEATURE_ARGALA,
        descriptionKey = StringKey.FEATURE_ARGALA_DESC,
        icon = Icons.Outlined.CompareArrows,
        colorToken = FeatureColorToken.AccentTeal,
        isImplemented = true
    ),
    CHARA_DASHA(
        titleKey = StringKey.FEATURE_CHARA_DASHA,
        descriptionKey = StringKey.FEATURE_CHARA_DASHA_DESC,
        icon = Icons.Outlined.Sync,
        colorToken = FeatureColorToken.LifeAreaSpiritual,
        isImplemented = true
    ),
    BHRIGU_BINDU(
        titleKey = StringKey.FEATURE_BHRIGU_BINDU,
        descriptionKey = StringKey.FEATURE_BHRIGU_BINDU_DESC,
        icon = Icons.Outlined.Stars,
        colorToken = FeatureColorToken.WarningColor,
        isImplemented = true
    ),
    PREDICTIONS(
        titleKey = StringKey.FEATURE_PREDICTIONS,
        descriptionKey = StringKey.FEATURE_PREDICTIONS_DESC,
        icon = Icons.Outlined.AutoAwesome,
        colorToken = FeatureColorToken.AccentPrimary,
        isImplemented = true
    ),
    ASHTOTTARI_DASHA(
        titleKey = StringKey.FEATURE_ASHTOTTARI_DASHA,
        descriptionKey = StringKey.FEATURE_ASHTOTTARI_DASHA_DESC,
        icon = Icons.Outlined.Timeline,
        colorToken = FeatureColorToken.AccentGold,
        isImplemented = true
    ),
    SUDARSHANA_CHAKRA(
        titleKey = StringKey.FEATURE_SUDARSHANA_CHAKRA,
        descriptionKey = StringKey.FEATURE_SUDARSHANA_CHAKRA_DESC,
        icon = Icons.Outlined.Sync,
        colorToken = FeatureColorToken.LifeAreaSpiritual,
        isImplemented = true
    ),
    MRITYU_BHAGA(
        titleKey = StringKey.FEATURE_MRITYU_BHAGA,
        descriptionKey = StringKey.FEATURE_MRITYU_BHAGA_DESC,
        icon = Icons.Outlined.BarChart,
        colorToken = FeatureColorToken.WarningColor,
        isImplemented = true
    ),
    LAL_KITAB(
        titleKey = StringKey.FEATURE_LAL_KITAB,
        descriptionKey = StringKey.FEATURE_LAL_KITAB_DESC,
        icon = Icons.Outlined.Spa,
        colorToken = FeatureColorToken.LifeAreaHealth,
        isImplemented = true
    ),
    DIVISIONAL_CHARTS(
        titleKey = StringKey.FEATURE_DIVISIONAL_CHARTS,
        descriptionKey = StringKey.FEATURE_DIVISIONAL_CHARTS_DESC,
        icon = Icons.Outlined.GridView,
        colorToken = FeatureColorToken.AccentTeal,
        isImplemented = true
    ),
    UPACHAYA_TRANSIT(
        titleKey = StringKey.FEATURE_UPACHAYA_TRANSIT,
        descriptionKey = StringKey.FEATURE_UPACHAYA_TRANSIT_DESC,
        icon = Icons.Outlined.Stars,
        colorToken = FeatureColorToken.SuccessColor,
        isImplemented = true
    ),
    KALACHAKRA_DASHA(
        titleKey = StringKey.FEATURE_KALACHAKRA_DASHA,
        descriptionKey = StringKey.FEATURE_KALACHAKRA_DASHA_DESC,
        icon = Icons.Outlined.HealthAndSafety,
        colorToken = FeatureColorToken.LifeAreaSpiritual,
        isImplemented = true
    ),
    TARABALA(
        titleKey = StringKey.FEATURE_TARABALA,
        descriptionKey = StringKey.FEATURE_TARABALA_DESC,
        icon = Icons.Outlined.Stars,
        colorToken = FeatureColorToken.PlanetMoon,
        isImplemented = true
    ),
    ARUDHA_PADA(
        titleKey = StringKey.FEATURE_ARUDHA_PADA,
        descriptionKey = StringKey.FEATURE_ARUDHA_PADA_DESC,
        icon = Icons.Outlined.Spa,
        colorToken = FeatureColorToken.LifeAreaSpiritual,
        isImplemented = true
    ),
    GRAHA_YUDDHA(
        titleKey = StringKey.FEATURE_GRAHA_YUDDHA,
        descriptionKey = StringKey.FEATURE_GRAHA_YUDDHA_DESC,
        icon = Icons.Outlined.CompareArrows,
        colorToken = FeatureColorToken.WarningColor,
        isImplemented = true
    ),
    DASHA_SANDHI(
        titleKey = StringKey.FEATURE_DASHA_SANDHI,
        descriptionKey = StringKey.FEATURE_DASHA_SANDHI_DESC,
        icon = Icons.Outlined.Timeline,
        colorToken = FeatureColorToken.LifeAreaSpiritual,
        isImplemented = true
    ),
    GOCHARA_VEDHA(
        titleKey = StringKey.FEATURE_GOCHARA_VEDHA,
        descriptionKey = StringKey.FEATURE_GOCHARA_VEDHA_DESC,
        icon = Icons.Outlined.Block,
        colorToken = FeatureColorToken.WarningColor,
        isImplemented = true
    ),
    KEMADRUMA_YOGA(
        titleKey = StringKey.FEATURE_KEMADRUMA_YOGA,
        descriptionKey = StringKey.FEATURE_KEMADRUMA_YOGA_DESC,
        icon = Icons.Outlined.Brightness2,
        colorToken = FeatureColorToken.PlanetMoon,
        isImplemented = true
    ),
    PANCH_MAHAPURUSHA(
        titleKey = StringKey.FEATURE_PANCH_MAHAPURUSHA,
        descriptionKey = StringKey.FEATURE_PANCH_MAHAPURUSHA_DESC,
        icon = Icons.Outlined.Stars,
        colorToken = FeatureColorToken.AccentGold,
        isImplemented = true
    ),
    NITYA_YOGA(
        titleKey = StringKey.FEATURE_NITYA_YOGA,
        descriptionKey = StringKey.FEATURE_NITYA_YOGA_DESC,
        icon = Icons.Outlined.CalendarMonth,
        colorToken = FeatureColorToken.AccentTeal,
        isImplemented = true
    ),
    AVASTHA(
        titleKey = StringKey.FEATURE_AVASTHA,
        descriptionKey = StringKey.FEATURE_AVASTHA_DESC,
        icon = Icons.Outlined.Psychology,
        colorToken = FeatureColorToken.AccentPrimary,
        isImplemented = true
    ),
    MARAKA(
        titleKey = StringKey.FEATURE_MARAKA,
        descriptionKey = StringKey.FEATURE_MARAKA_DESC,
        icon = Icons.Outlined.Warning,
        colorToken = FeatureColorToken.ErrorColor,
        isImplemented = true
    ),
    BADHAKA(
        titleKey = StringKey.FEATURE_BADHAKA,
        descriptionKey = StringKey.FEATURE_BADHAKA_DESC,
        icon = Icons.Outlined.Block,
        colorToken = FeatureColorToken.WarningColor,
        isImplemented = true
    ),
    VIPAREETA_RAJA_YOGA(
        titleKey = StringKey.FEATURE_VIPAREETA_RAJA_YOGA,
        descriptionKey = StringKey.FEATURE_VIPAREETA_RAJA_YOGA_DESC,
        icon = Icons.Outlined.Stars,
        colorToken = FeatureColorToken.LifeAreaSpiritual,
        isImplemented = true
    ),
    ISHTA_KASHTA_PHALA(
        titleKey = StringKey.FEATURE_ISHTA_KASHTA_PHALA,
        descriptionKey = StringKey.FEATURE_ISHTA_KASHTA_PHALA_DESC,
        icon = Icons.Outlined.Balance,
        colorToken = FeatureColorToken.AccentGold,
        isImplemented = true
    ),
    SHOOLA_DASHA(
        titleKey = StringKey.FEATURE_SHOOLA_DASHA,
        descriptionKey = StringKey.FEATURE_SHOOLA_DASHA_DESC,
        icon = Icons.Outlined.HealthAndSafety,
        colorToken = FeatureColorToken.WarningColor,
        isImplemented = true
    ),
    ASHTAVARGA_TRANSIT(
        titleKey = StringKey.FEATURE_ASHTAVARGA_TRANSIT,
        descriptionKey = StringKey.FEATURE_ASHTAVARGA_TRANSIT_DESC,
        icon = Icons.Outlined.BarChart,
        colorToken = FeatureColorToken.AccentTeal,
        isImplemented = true
    ),
    KAKSHYA_TRANSIT(
        titleKey = StringKey.FEATURE_KAKSHYA_TRANSIT,
        descriptionKey = StringKey.FEATURE_KAKSHYA_TRANSIT_DESC,
        icon = Icons.Outlined.Speed,
        colorToken = FeatureColorToken.AccentPrimary,
        isImplemented = true
    ),
    NADI_AMSHA(
        titleKey = StringKeyAdvanced.NADI_TITLE,
        descriptionKey = StringKeyAdvanced.NADI_DESC,
        icon = Icons.Outlined.Timer,
        colorToken = FeatureColorToken.AccentGold,
        isImplemented = true
    ),
    SARVATOBHADRA_CHAKRA(
        titleKey = StringKeyShadbala.SARVATOBHADRA_TITLE,
        descriptionKey = StringKeyShadbala.SARVATOBHADRA_DESC,
        icon = Icons.Outlined.Grid4x4,
        colorToken = FeatureColorToken.AccentTeal,
        isImplemented = true
    ),
    DRIG_BALA(
        titleKey = StringKeyShadbala.DRIG_TITLE,
        descriptionKey = StringKeyShadbala.DRIG_DESC,
        icon = Icons.Outlined.Visibility,
        colorToken = FeatureColorToken.SuccessColor,
        isImplemented = true
    ),
    STHANA_BALA(
        titleKey = StringKeyShadbala.STHANA_TITLE,
        descriptionKey = StringKeyShadbala.STHANA_DESC,
        icon = Icons.Outlined.Home,
        colorToken = FeatureColorToken.LifeAreaCareer,
        isImplemented = true
    ),
    KALA_BALA(
        titleKey = StringKeyShadbala.KALA_TITLE,
        descriptionKey = StringKeyShadbala.KALA_DESC,
        icon = Icons.Outlined.Timelapse,
        colorToken = FeatureColorToken.LifeAreaSpiritual,
        isImplemented = true
    ),
    SAHAM(
        titleKey = StringKeyShadbala.SAHAM_TITLE,
        descriptionKey = StringKeyShadbala.SAHAM_DESC,
        icon = Icons.Outlined.Calculate,
        colorToken = FeatureColorToken.AccentGold,
        isImplemented = true
    ),
    NATIVE_ANALYSIS(
        titleKey = StringKey.FEATURE_NATIVE_ANALYSIS,
        descriptionKey = StringKey.FEATURE_NATIVE_ANALYSIS_DESC,
        icon = Icons.Outlined.Person,
        colorToken = FeatureColorToken.AccentPrimary,
        isImplemented = true
    ),
    JAIMINI_KARAKA(
        titleKey = StringKey.FEATURE_JAIMINI_KARAKA,
        descriptionKey = StringKey.FEATURE_JAIMINI_KARAKA_DESC,
        icon = Icons.Outlined.Stars,
        colorToken = FeatureColorToken.LifeAreaSpiritual,
        isImplemented = true
    ),
    DRIG_DASHA(
        titleKey = StringKey.FEATURE_DRIG_DASHA,
        descriptionKey = StringKey.FEATURE_DRIG_DASHA_DESC,
        icon = Icons.Outlined.HealthAndSafety,
        colorToken = FeatureColorToken.WarningColor,
        isImplemented = true
    ),
    SAPTAMSA(
        titleKey = StringKey.FEATURE_SAPTAMSA,
        descriptionKey = StringKey.FEATURE_SAPTAMSA_DESC,
        icon = Icons.Outlined.ChildCare,
        colorToken = FeatureColorToken.LifeAreaLove,
        isImplemented = true
    );

    fun getLocalizedTitle(language: Language): String {
        return StringResources.get(titleKey, language)
    }

    fun getLocalizedDescription(language: Language): String {
        return StringResources.get(descriptionKey, language)
    }

    companion object {
        val implementedFeatures: List<InsightFeature> by lazy(LazyThreadSafetyMode.PUBLICATION) {
            entries.filter { it.isImplemented }
        }

        val comingSoonFeatures: List<InsightFeature> by lazy(LazyThreadSafetyMode.PUBLICATION) {
            entries.filter { !it.isImplemented }
        }
    }
}

private fun resolveZoneId(timezone: String): ZoneId {
    return try {
        ZoneId.of(timezone)
    } catch (_: DateTimeException) {
        val trimmed = timezone.trim()
        val numericHours = trimmed.toDoubleOrNull()
        if (numericHours != null) {
            val totalSeconds = (numericHours * 3600.0).roundToInt()
            ZoneOffset.ofTotalSeconds(totalSeconds.coerceIn(-18 * 3600, 18 * 3600))
        } else {
            ZoneId.systemDefault()
        }
    }
}





