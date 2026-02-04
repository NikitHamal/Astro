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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.astro.storm.core.common.StringKeyShadbala
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.DarkAppThemeColors
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

// ============================================================================
// DESIGN TOKENS
// ============================================================================
private object HomeDesignTokens {
    val ScreenPadding = 16.dp
    val SectionSpacing = 24.dp
    val CardSpacing = 12.dp
    val CardCornerRadius = 20.dp
    val SmallCardCornerRadius = 16.dp
    val QuickActionSize = 72.dp
    val QuickActionIconSize = 28.dp
    val HeroCardMinHeight = 160.dp
}

// ============================================================================
// MAIN HOME TAB COMPOSABLE
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
        // Hero Section - Current Dasha Period
        item(key = "hero_dasha") {
            HeroDashaCard(
                chart = chart,
                currentDasha = currentDasha,
                language = language,
                onClick = { onFeatureClick(InsightFeature.DASHAS) }
            )
        }

        // Quick Actions - Most used features
        item(key = "quick_actions") {
            QuickActionsSection(
                onFeatureClick = onFeatureClick,
                language = language
            )
        }

        // Today's Snapshot
        item(key = "today_snapshot") {
            TodaySnapshotSection(
                chart = chart,
                language = language,
                onPanchangaClick = { onFeatureClick(InsightFeature.PANCHANGA) },
                onTransitsClick = { onFeatureClick(InsightFeature.TRANSITS) }
            )
        }

        // Chart Analysis Section
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
// HERO DASHA CARD
// ============================================================================
@Composable
private fun HeroDashaCard(
    chart: VedicChart,
    currentDasha: DashaCalculator.Mahadasha?,
    language: Language,
    onClick: () -> Unit
) {
    val colors = AppTheme.current
    val infiniteTransition = rememberInfiniteTransition(label = "hero_glow")
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    
    val planetColor = remember(currentDasha) {
        currentDasha?.planet?.let { getPlanetDisplayColor(it) } ?: DarkAppThemeColors.AccentGold
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDesignTokens.ScreenPadding)
            .padding(top = HomeDesignTokens.ScreenPadding),
        shape = RoundedCornerShape(HomeDesignTokens.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = HomeDesignTokens.HeroCardMinHeight)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            planetColor.copy(alpha = 0.15f),
                            colors.CardBackground,
                            colors.CardBackground
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .drawBehind {
                    // Subtle glow effect
                    drawCircle(
                        color = planetColor.copy(alpha = glowAlpha * 0.3f),
                        radius = size.width * 0.4f,
                        center = Offset(size.width * 0.9f, size.height * 0.2f)
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Label
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = planetColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = stringResource(StringKey.CURRENT_MAHA_DASHA),
                            style = MaterialTheme.typography.labelMedium,
                            color = planetColor,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                    
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = stringResource(StringKey.BTN_VIEW_DETAILS),
                        tint = colors.TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (currentDasha != null) {
                    // Planet Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Planet Icon Container
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(planetColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentDasha.planet.getLocalizedName(language).take(2),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = planetColor
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column {
                            Text(
                                text = "${currentDasha.planet.getLocalizedName(language)} ${stringResource(StringKey.HOME_DASHA_LABEL)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.TextPrimary
                            )
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            // Duration
                            val remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), currentDasha.endDate.toLocalDate())
                            val years = remainingDays / 365
                            val months = (remainingDays % 365) / 30
                            
                            Text(
                                text = if (years > 0) {
                                    stringResource(StringKey.REMAINING_PERIOD_YEARS, years.toString(), months.toString())
                                } else {
                                    stringResource(StringKey.REMAINING_PERIOD_MONTHS, months.toString())
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress Bar
                    val totalDays = ChronoUnit.DAYS.between(currentDasha.startDate.toLocalDate(), currentDasha.endDate.toLocalDate()).toFloat()
                    val elapsedDays = ChronoUnit.DAYS.between(currentDasha.startDate.toLocalDate(), LocalDate.now()).toFloat()
                    val progress = (elapsedDays / totalDays).coerceIn(0f, 1f)
                    
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = currentDasha.startDate.format(DateTimeFormatter.ofPattern("MMM yyyy")),
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.TextMuted
                            )
                            Text(
                                text = currentDasha.endDate.format(DateTimeFormatter.ofPattern("MMM yyyy")),
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.TextMuted
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = planetColor,
                            trackColor = colors.DividerColor
                        )
                    }
                } else {
                    // No dasha data available
                    Text(
                        text = stringResource(StringKey.TAP_TO_VIEW_DASHAS),
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.TextMuted
                    )
                }
            }
        }
    }
}

// ============================================================================
// QUICK ACTIONS SECTION
// ============================================================================
@Composable
private fun QuickActionsSection(
    onFeatureClick: (InsightFeature) -> Unit,
    language: Language
) {
    val colors = AppTheme.current
    
    val quickActions = remember {
        listOf(
            QuickAction(InsightFeature.FULL_CHART, Icons.Outlined.GridView, DarkAppThemeColors.AccentPrimary),
            QuickAction(InsightFeature.YOGAS, Icons.Outlined.AutoAwesome, DarkAppThemeColors.AccentGold),
            QuickAction(InsightFeature.PREDICTIONS, Icons.Outlined.TipsAndUpdates, DarkAppThemeColors.LifeAreaSpiritual),
            QuickAction(InsightFeature.MATCHMAKING, Icons.Outlined.Favorite, DarkAppThemeColors.LifeAreaLove)
        )
    }

    Column(
        modifier = Modifier.padding(horizontal = HomeDesignTokens.ScreenPadding)
    ) {
        SectionHeader(
            title = stringResource(StringKey.QUICK_ACTIONS),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            quickActions.forEach { action ->
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
    val interactionSource = remember { MutableInteractionSource() }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(HomeDesignTokens.SmallCardCornerRadius))
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = accentColor),
                onClick = onClick
            )
            .padding(vertical = 8.dp)
    ) {
        // Card-like square container with subtle background - responsive width with max cap
        Box(
            modifier = Modifier
                .widthIn(max = HomeDesignTokens.QuickActionSize)
                .fillMaxWidth(0.85f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(accentColor.copy(alpha = 0.12f))
                .border(
                    width = 1.dp,
                    color = accentColor.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = feature.getLocalizedTitle(language),
                tint = accentColor,
                modifier = Modifier.size(HomeDesignTokens.QuickActionIconSize)
            )
        }
        
        Spacer(modifier = Modifier.height(10.dp))
        
        Text(
            text = feature.getLocalizedTitle(language),
            style = MaterialTheme.typography.labelSmall,
            color = colors.TextSecondary,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )
    }
}

private data class QuickAction(
    val feature: InsightFeature,
    val icon: ImageVector,
    val color: Color
)

// ============================================================================
// TODAY'S SNAPSHOT SECTION
// ============================================================================
@Composable
private fun TodaySnapshotSection(
    chart: VedicChart,
    language: Language,
    onPanchangaClick: () -> Unit,
    onTransitsClick: () -> Unit
) {
    val colors = AppTheme.current
    val today = LocalDate.now()
    
    Column(
        modifier = Modifier.padding(horizontal = HomeDesignTokens.ScreenPadding)
    ) {
        SectionHeader(
            title = stringResource(StringKey.TODAYS_SNAPSHOT),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(HomeDesignTokens.CardSpacing)
        ) {
            // Panchanga Card
            SnapshotCard(
                title = stringResource(StringKey.FEATURE_PANCHANGA),
                icon = Icons.Outlined.CalendarMonth,
                accentColor = colors.LifeAreaFinance,
                modifier = Modifier.weight(1f),
                onClick = onPanchangaClick
            ) {
                // Tithi, Nakshatra info would go here
                Text(
                    text = today.format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = today.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.TextMuted
                )
            }

            // Transits Card
            SnapshotCard(
                title = stringResource(StringKey.FEATURE_TRANSITS),
                icon = Icons.Outlined.Sync,
                accentColor = colors.AccentTeal,
                modifier = Modifier.weight(1f),
                onClick = onTransitsClick
            ) {
                Text(
                    text = stringResource(StringKey.VIEW_CURRENT_TRANSITS),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
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
    
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(HomeDesignTokens.SmallCardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground),
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
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(10.dp))
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            content()
        }
    }
}

// ============================================================================
// FEATURE CATEGORY CARD
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
    
    Card(
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
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground)
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
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.2f),
                                    accentColor.copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(26.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Feature count badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = features.size.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Expand indicator
                val rotation by animateFloatAsState(
                    targetValue = if (isExpanded) 90f else 0f,
                    animationSpec = tween(300),
                    label = "expand_rotation"
                )
                
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = colors.TextMuted,
                    modifier = Modifier.graphicsLayer { rotationZ = rotation }
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
                    HorizontalDivider(
                        color = colors.DividerColor,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Features Grid
                    val chunkedFeatures = features.chunked(2)
                    chunkedFeatures.forEach { rowFeatures ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = if (rowFeatures != chunkedFeatures.last()) 10.dp else 0.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowFeatures.forEach { feature ->
                                CompactFeatureCard(
                                    feature = feature,
                                    language = language,
                                    onClick = { onFeatureClick(feature) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Fill empty space if odd number
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
    val interactionSource = remember { MutableInteractionSource() }
    
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = feature.color),
                onClick = onClick
            ),
        shape = RoundedCornerShape(12.dp),
        color = colors.ChipBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(feature.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = feature.color,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(10.dp))
            
            Text(
                text = feature.getLocalizedTitle(language),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = colors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ============================================================================
// SECTION HEADER
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
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colors.TextPrimary
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = colors.TextMuted
            )
        }
    }
}

// ============================================================================
// EMPTY STATE
// ============================================================================
@Composable
private fun EmptyHomeState(
    onCreateProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current
    
    val infiniteTransition = rememberInfiniteTransition(label = "empty_state")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

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
            // Animated Icon Container
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .graphicsLayer {
                        scaleX = pulseScale
                        scaleY = pulseScale
                    }
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                colors.AccentPrimary.copy(alpha = 0.2f),
                                colors.AccentPrimary.copy(alpha = 0.05f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(colors.CardBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PersonAddAlt,
                        contentDescription = null,
                        tint = colors.AccentPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(StringKey.NO_PROFILE_SELECTED),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = colors.TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(StringKey.NO_PROFILE_MESSAGE),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onCreateProfile,
                modifier = Modifier
                    .height(56.dp)
                    .widthIn(min = 220.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.AccentPrimary,
                    contentColor = colors.ButtonText
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(StringKey.BTN_CREATE_CHART),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
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
            InsightFeature.SUDARSHANA_CHAKRA,
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
private fun getPlanetDisplayColor(planet: Planet): Color {
    return when (planet) {
        Planet.SUN -> DarkAppThemeColors.PlanetSun
        Planet.MOON -> DarkAppThemeColors.PlanetMoon
        Planet.MARS -> DarkAppThemeColors.PlanetMars
        Planet.MERCURY -> DarkAppThemeColors.PlanetMercury
        Planet.JUPITER -> DarkAppThemeColors.PlanetJupiter
        Planet.VENUS -> DarkAppThemeColors.PlanetVenus
        Planet.SATURN -> DarkAppThemeColors.PlanetSaturn
        Planet.RAHU -> DarkAppThemeColors.PlanetRahu
        Planet.KETU -> DarkAppThemeColors.PlanetKetu
        else -> DarkAppThemeColors.AccentPrimary
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
    val color: Color,
    val isImplemented: Boolean
) {
    FULL_CHART(
        titleKey = StringKey.FEATURE_BIRTH_CHART,
        descriptionKey = StringKey.FEATURE_BIRTH_CHART_DESC,
        icon = Icons.Outlined.GridView,
        color = DarkAppThemeColors.AccentPrimary,
        isImplemented = true
    ),
    PLANETS(
        titleKey = StringKey.FEATURE_PLANETS,
        descriptionKey = StringKey.FEATURE_PLANETS_DESC,
        icon = Icons.Outlined.Public,
        color = DarkAppThemeColors.LifeAreaCareer,
        isImplemented = true
    ),
    YOGAS(
        titleKey = StringKey.FEATURE_YOGAS,
        descriptionKey = StringKey.FEATURE_YOGAS_DESC,
        icon = Icons.Outlined.AutoAwesome,
        color = DarkAppThemeColors.AccentGold,
        isImplemented = true
    ),
    DASHAS(
        titleKey = StringKey.FEATURE_DASHAS,
        descriptionKey = StringKey.FEATURE_DASHAS_DESC,
        icon = Icons.Outlined.Timeline,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        isImplemented = true
    ),
    TRANSITS(
        titleKey = StringKey.FEATURE_TRANSITS,
        descriptionKey = StringKey.FEATURE_TRANSITS_DESC,
        icon = Icons.Outlined.Sync,
        color = DarkAppThemeColors.AccentTeal,
        isImplemented = true
    ),
    ASHTAKAVARGA(
        titleKey = StringKey.FEATURE_ASHTAKAVARGA,
        descriptionKey = StringKey.FEATURE_ASHTAKAVARGA_DESC,
        icon = Icons.Outlined.BarChart,
        color = DarkAppThemeColors.SuccessColor,
        isImplemented = true
    ),
    PANCHANGA(
        titleKey = StringKey.FEATURE_PANCHANGA,
        descriptionKey = StringKey.FEATURE_PANCHANGA_DESC,
        icon = Icons.Outlined.CalendarMonth,
        color = DarkAppThemeColors.LifeAreaFinance,
        isImplemented = true
    ),
    MATCHMAKING(
        titleKey = StringKey.FEATURE_MATCHMAKING,
        descriptionKey = StringKey.FEATURE_MATCHMAKING_DESC,
        icon = Icons.Outlined.Favorite,
        color = DarkAppThemeColors.LifeAreaLove,
        isImplemented = true
    ),
    MUHURTA(
        titleKey = StringKey.FEATURE_MUHURTA,
        descriptionKey = StringKey.FEATURE_MUHURTA_DESC,
        icon = Icons.Outlined.AccessTime,
        color = DarkAppThemeColors.WarningColor,
        isImplemented = true
    ),
    REMEDIES(
        titleKey = StringKey.FEATURE_REMEDIES,
        descriptionKey = StringKey.FEATURE_REMEDIES_DESC,
        icon = Icons.Outlined.Spa,
        color = DarkAppThemeColors.LifeAreaHealth,
        isImplemented = true
    ),
    VARSHAPHALA(
        titleKey = StringKey.FEATURE_VARSHAPHALA,
        descriptionKey = StringKey.FEATURE_VARSHAPHALA_DESC,
        icon = Icons.Outlined.Cake,
        color = DarkAppThemeColors.LifeAreaCareer,
        isImplemented = true
    ),
    PRASHNA(
        titleKey = StringKey.FEATURE_PRASHNA,
        descriptionKey = StringKey.FEATURE_PRASHNA_DESC,
        icon = Icons.Outlined.HelpOutline,
        color = DarkAppThemeColors.AccentTeal,
        isImplemented = true
    ),
    CHART_COMPARISON(
        titleKey = StringKey.FEATURE_SYNASTRY,
        descriptionKey = StringKey.FEATURE_SYNASTRY_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = DarkAppThemeColors.LifeAreaFinance,
        isImplemented = true
    ),
    NAKSHATRA_ANALYSIS(
        titleKey = StringKey.FEATURE_NAKSHATRAS,
        descriptionKey = StringKey.FEATURE_NAKSHATRAS_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.AccentGold,
        isImplemented = true
    ),
    SHADBALA(
        titleKey = StringKey.FEATURE_SHADBALA,
        descriptionKey = StringKey.FEATURE_SHADBALA_DESC,
        icon = Icons.Outlined.Speed,
        color = DarkAppThemeColors.SuccessColor,
        isImplemented = true
    ),
    SHODASHVARGA(
        titleKey = StringKey.FEATURE_SHODASHVARGA,
        descriptionKey = StringKey.FEATURE_SHODASHVARGA_DESC,
        icon = Icons.Outlined.GridView,
        color = DarkAppThemeColors.AccentGold,
        isImplemented = true
    ),
    YOGINI_DASHA(
        titleKey = StringKey.FEATURE_YOGINI_DASHA,
        descriptionKey = StringKey.FEATURE_YOGINI_DASHA_DESC,
        icon = Icons.Outlined.Timeline,
        color = DarkAppThemeColors.LifeAreaLove,
        isImplemented = true
    ),
    ARGALA(
        titleKey = StringKey.FEATURE_ARGALA,
        descriptionKey = StringKey.FEATURE_ARGALA_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = DarkAppThemeColors.AccentTeal,
        isImplemented = true
    ),
    CHARA_DASHA(
        titleKey = StringKey.FEATURE_CHARA_DASHA,
        descriptionKey = StringKey.FEATURE_CHARA_DASHA_DESC,
        icon = Icons.Outlined.Sync,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        isImplemented = true
    ),
    BHRIGU_BINDU(
        titleKey = StringKey.FEATURE_BHRIGU_BINDU,
        descriptionKey = StringKey.FEATURE_BHRIGU_BINDU_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.WarningColor,
        isImplemented = true
    ),
    PREDICTIONS(
        titleKey = StringKey.FEATURE_PREDICTIONS,
        descriptionKey = StringKey.FEATURE_PREDICTIONS_DESC,
        icon = Icons.Outlined.AutoAwesome,
        color = DarkAppThemeColors.AccentPrimary,
        isImplemented = true
    ),
    ASHTOTTARI_DASHA(
        titleKey = StringKey.FEATURE_ASHTOTTARI_DASHA,
        descriptionKey = StringKey.FEATURE_ASHTOTTARI_DASHA_DESC,
        icon = Icons.Outlined.Timeline,
        color = DarkAppThemeColors.AccentGold,
        isImplemented = true
    ),
    SUDARSHANA_CHAKRA(
        titleKey = StringKey.FEATURE_SUDARSHANA_CHAKRA,
        descriptionKey = StringKey.FEATURE_SUDARSHANA_CHAKRA_DESC,
        icon = Icons.Outlined.Sync,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        isImplemented = true
    ),
    MRITYU_BHAGA(
        titleKey = StringKey.FEATURE_MRITYU_BHAGA,
        descriptionKey = StringKey.FEATURE_MRITYU_BHAGA_DESC,
        icon = Icons.Outlined.BarChart,
        color = DarkAppThemeColors.WarningColor,
        isImplemented = true
    ),
    LAL_KITAB(
        titleKey = StringKey.FEATURE_LAL_KITAB,
        descriptionKey = StringKey.FEATURE_LAL_KITAB_DESC,
        icon = Icons.Outlined.Spa,
        color = DarkAppThemeColors.LifeAreaHealth,
        isImplemented = true
    ),
    DIVISIONAL_CHARTS(
        titleKey = StringKey.FEATURE_DIVISIONAL_CHARTS,
        descriptionKey = StringKey.FEATURE_DIVISIONAL_CHARTS_DESC,
        icon = Icons.Outlined.GridView,
        color = DarkAppThemeColors.AccentTeal,
        isImplemented = true
    ),
    UPACHAYA_TRANSIT(
        titleKey = StringKey.FEATURE_UPACHAYA_TRANSIT,
        descriptionKey = StringKey.FEATURE_UPACHAYA_TRANSIT_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.SuccessColor,
        isImplemented = true
    ),
    KALACHAKRA_DASHA(
        titleKey = StringKey.FEATURE_KALACHAKRA_DASHA,
        descriptionKey = StringKey.FEATURE_KALACHAKRA_DASHA_DESC,
        icon = Icons.Outlined.HealthAndSafety,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        isImplemented = true
    ),
    TARABALA(
        titleKey = StringKey.FEATURE_TARABALA,
        descriptionKey = StringKey.FEATURE_TARABALA_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.PlanetMoon,
        isImplemented = true
    ),
    ARUDHA_PADA(
        titleKey = StringKey.FEATURE_ARUDHA_PADA,
        descriptionKey = StringKey.FEATURE_ARUDHA_PADA_DESC,
        icon = Icons.Outlined.Spa,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        isImplemented = true
    ),
    GRAHA_YUDDHA(
        titleKey = StringKey.FEATURE_GRAHA_YUDDHA,
        descriptionKey = StringKey.FEATURE_GRAHA_YUDDHA_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = DarkAppThemeColors.WarningColor,
        isImplemented = true
    ),
    DASHA_SANDHI(
        titleKey = StringKey.FEATURE_DASHA_SANDHI,
        descriptionKey = StringKey.FEATURE_DASHA_SANDHI_DESC,
        icon = Icons.Outlined.Timeline,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        isImplemented = true
    ),
    GOCHARA_VEDHA(
        titleKey = StringKey.FEATURE_GOCHARA_VEDHA,
        descriptionKey = StringKey.FEATURE_GOCHARA_VEDHA_DESC,
        icon = Icons.Outlined.Block,
        color = DarkAppThemeColors.WarningColor,
        isImplemented = true
    ),
    KEMADRUMA_YOGA(
        titleKey = StringKey.FEATURE_KEMADRUMA_YOGA,
        descriptionKey = StringKey.FEATURE_KEMADRUMA_YOGA_DESC,
        icon = Icons.Outlined.Brightness2,
        color = DarkAppThemeColors.PlanetMoon,
        isImplemented = true
    ),
    PANCH_MAHAPURUSHA(
        titleKey = StringKey.FEATURE_PANCH_MAHAPURUSHA,
        descriptionKey = StringKey.FEATURE_PANCH_MAHAPURUSHA_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.AccentGold,
        isImplemented = true
    ),
    NITYA_YOGA(
        titleKey = StringKey.FEATURE_NITYA_YOGA,
        descriptionKey = StringKey.FEATURE_NITYA_YOGA_DESC,
        icon = Icons.Outlined.CalendarMonth,
        color = DarkAppThemeColors.AccentTeal,
        isImplemented = true
    ),
    AVASTHA(
        titleKey = StringKey.FEATURE_AVASTHA,
        descriptionKey = StringKey.FEATURE_AVASTHA_DESC,
        icon = Icons.Outlined.Psychology,
        color = DarkAppThemeColors.AccentPrimary,
        isImplemented = true
    ),
    MARAKA(
        titleKey = StringKey.FEATURE_MARAKA,
        descriptionKey = StringKey.FEATURE_MARAKA_DESC,
        icon = Icons.Outlined.Warning,
        color = DarkAppThemeColors.ErrorColor,
        isImplemented = true
    ),
    BADHAKA(
        titleKey = StringKey.FEATURE_BADHAKA,
        descriptionKey = StringKey.FEATURE_BADHAKA_DESC,
        icon = Icons.Outlined.Block,
        color = DarkAppThemeColors.WarningColor,
        isImplemented = true
    ),
    VIPAREETA_RAJA_YOGA(
        titleKey = StringKey.FEATURE_VIPAREETA_RAJA_YOGA,
        descriptionKey = StringKey.FEATURE_VIPAREETA_RAJA_YOGA_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        isImplemented = true
    ),
    ISHTA_KASHTA_PHALA(
        titleKey = StringKey.FEATURE_ISHTA_KASHTA_PHALA,
        descriptionKey = StringKey.FEATURE_ISHTA_KASHTA_PHALA_DESC,
        icon = Icons.Outlined.Balance,
        color = DarkAppThemeColors.AccentGold,
        isImplemented = true
    ),
    SHOOLA_DASHA(
        titleKey = StringKey.FEATURE_SHOOLA_DASHA,
        descriptionKey = StringKey.FEATURE_SHOOLA_DASHA_DESC,
        icon = Icons.Outlined.HealthAndSafety,
        color = DarkAppThemeColors.WarningColor,
        isImplemented = true
    ),
    ASHTAVARGA_TRANSIT(
        titleKey = StringKey.FEATURE_ASHTAVARGA_TRANSIT,
        descriptionKey = StringKey.FEATURE_ASHTAVARGA_TRANSIT_DESC,
        icon = Icons.Outlined.BarChart,
        color = DarkAppThemeColors.AccentTeal,
        isImplemented = true
    ),
    KAKSHYA_TRANSIT(
        titleKey = StringKey.FEATURE_KAKSHYA_TRANSIT,
        descriptionKey = StringKey.FEATURE_KAKSHYA_TRANSIT_DESC,
        icon = Icons.Outlined.Speed,
        color = DarkAppThemeColors.AccentPrimary,
        isImplemented = true
    ),
    NADI_AMSHA(
        titleKey = StringKeyAdvanced.NADI_TITLE,
        descriptionKey = StringKeyAdvanced.NADI_DESC,
        icon = Icons.Outlined.Timer,
        color = DarkAppThemeColors.AccentGold,
        isImplemented = true
    ),
    SARVATOBHADRA_CHAKRA(
        titleKey = StringKeyShadbala.SARVATOBHADRA_TITLE,
        descriptionKey = StringKeyShadbala.SARVATOBHADRA_DESC,
        icon = Icons.Outlined.Grid4x4,
        color = DarkAppThemeColors.AccentTeal,
        isImplemented = true
    ),
    DRIG_BALA(
        titleKey = StringKeyShadbala.DRIG_TITLE,
        descriptionKey = StringKeyShadbala.DRIG_DESC,
        icon = Icons.Outlined.Visibility,
        color = DarkAppThemeColors.SuccessColor,
        isImplemented = true
    ),
    STHANA_BALA(
        titleKey = StringKeyShadbala.STHANA_TITLE,
        descriptionKey = StringKeyShadbala.STHANA_DESC,
        icon = Icons.Outlined.Home,
        color = DarkAppThemeColors.LifeAreaCareer,
        isImplemented = true
    ),
    KALA_BALA(
        titleKey = StringKeyShadbala.KALA_TITLE,
        descriptionKey = StringKeyShadbala.KALA_DESC,
        icon = Icons.Outlined.Timelapse,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        isImplemented = true
    ),
    SAHAM(
        titleKey = StringKeyShadbala.SAHAM_TITLE,
        descriptionKey = StringKeyShadbala.SAHAM_DESC,
        icon = Icons.Outlined.Calculate,
        color = DarkAppThemeColors.AccentGold,
        isImplemented = true
    ),
    NATIVE_ANALYSIS(
        titleKey = StringKey.FEATURE_NATIVE_ANALYSIS,
        descriptionKey = StringKey.FEATURE_NATIVE_ANALYSIS_DESC,
        icon = Icons.Outlined.Person,
        color = DarkAppThemeColors.AccentPrimary,
        isImplemented = true
    ),
    JAIMINI_KARAKA(
        titleKey = StringKey.FEATURE_JAIMINI_KARAKA,
        descriptionKey = StringKey.FEATURE_JAIMINI_KARAKA_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        isImplemented = true
    ),
    DRIG_DASHA(
        titleKey = StringKey.FEATURE_DRIG_DASHA,
        descriptionKey = StringKey.FEATURE_DRIG_DASHA_DESC,
        icon = Icons.Outlined.HealthAndSafety,
        color = DarkAppThemeColors.WarningColor,
        isImplemented = true
    ),
    SAPTAMSA(
        titleKey = StringKey.FEATURE_SAPTAMSA,
        descriptionKey = StringKey.FEATURE_SAPTAMSA_DESC,
        icon = Icons.Outlined.ChildCare,
        color = DarkAppThemeColors.LifeAreaLove,
        isImplemented = true
    );

    /**
     * Get localized title
     */
    fun getLocalizedTitle(language: Language): String {
        return StringResources.get(titleKey, language)
    }

    /**
     * Get localized description
     */
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
