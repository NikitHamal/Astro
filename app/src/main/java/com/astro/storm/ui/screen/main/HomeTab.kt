package com.astro.storm.ui.screen.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.AllInclusive
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyInterface
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.model.VedicChart
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.CinzelDecorativeFontFamily
import com.astro.storm.ui.theme.CormorantGaramondFontFamily
import com.astro.storm.ui.theme.CosmicIndigo
import com.astro.storm.ui.theme.Paper
import com.astro.storm.ui.theme.SpaceGroteskFontFamily
import com.astro.storm.ui.theme.VedicGold
import com.astro.storm.ui.theme.MarsRed
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.random.Random

// ============================================================================
// DESIGN TOKENS
// ============================================================================
private object HomeDesignTokens {
    val ScreenPadding = 16.dp
    val SectionSpacing = 32.dp
    val CardCornerRadius = 2.dp
}

// ============================================================================
// GRAIN TEXTURE
// ============================================================================
@Composable
fun GrainTextureOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val random = Random(42)
        for (i in 0..5000) {
            val x = random.nextFloat() * size.width
            val y = random.nextFloat() * size.height
            drawCircle(
                color = Color.Black.copy(alpha = 0.04f),
                radius = 1f,
                center = Offset(x, y)
            )
        }
    }
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
        EmptyHomeState(onCreateProfile = onAddNewChart, modifier = modifier)
        return
    }

    val listState = rememberLazyListState()
    val language = LocalLanguage.current
    
    val dashaTimeline = remember(chart) {
        try { DashaCalculator.calculateDashaTimeline(chart) } catch (e: Exception) { null }
    }
    val currentDasha = dashaTimeline?.currentMahadasha

    Box(modifier = modifier.fillMaxSize()) {
        GrainTextureOverlay()

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(HomeDesignTokens.SectionSpacing)
        ) {
            // Maha Dasha Orbital Card
            item(key = "hero_orbital") {
                MahaDashaOrbitalCard(
                    currentDasha = currentDasha,
                    language = language,
                    onClick = { onFeatureClick(InsightFeature.DASHAS) }
                )
            }

            // Quick Actions Section
            item(key = "quick_actions") {
                QuickActionsSection(onFeatureClick = onFeatureClick, language = language)
            }

            // Today's Snapshot Section
            item(key = "today_snapshot") {
                TodaySnapshotSection(
                    onPanchangaClick = { onFeatureClick(InsightFeature.PANCHANGA) },
                    onTransitsClick = { onFeatureClick(InsightFeature.TRANSITS) }
                )
            }

            // Core Analysis Section
            item(key = "core_analysis") {
                CoreAnalysisSection(onFeatureClick = onFeatureClick, language = language)
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

// ============================================================================
// ORBITAL DASHA CARD
// ============================================================================
@Composable
private fun MahaDashaOrbitalCard(
    currentDasha: DashaCalculator.Mahadasha?,
    language: Language,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(HomeDesignTokens.ScreenPadding)
            .clickable(onClick = onClick),
        color = AppTheme.CardBackground,
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(HomeDesignTokens.CardCornerRadius)
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Decorative corner markers
            Box(Modifier.fillMaxSize()) {
                CornerMarker(Alignment.TopStart)
                CornerMarker(Alignment.TopEnd)
                CornerMarker(Alignment.BottomStart)
                CornerMarker(Alignment.BottomEnd)
            }

            // Orbital Visualization
            val infiniteTransition = rememberInfiniteTransition(label = "orbital")
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(tween(60000, easing = LinearEasing)),
                label = "rotation"
            )
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 0.02f,
                targetValue = 0.08f,
                animationSpec = infiniteRepeatable(tween(4000, easing = EaseInOutSine), repeatMode = RepeatMode.Reverse),
                label = "pulse"
            )

            Box(modifier = Modifier.size(256.dp), contentAlignment = Alignment.Center) {
                // Background Aura
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(VedicGold.copy(alpha = pulseAlpha), CircleShape)
                )

                // Static Gold Ring
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(color = VedicGold.copy(alpha = 0.3f), style = Stroke(width = 1.dp.toPx()))

                    // Progress Arc (Approx 65%)
                    drawArc(
                        color = VedicGold,
                        startAngle = -90f,
                        sweepAngle = 234f,
                        useCenter = false,
                        style = Stroke(width = 0.5.dp.toPx())
                    )
                }

                // Rotating Dashed Ring
                Canvas(modifier = Modifier.fillMaxSize().padding(16.dp).rotate(rotation)) {
                    drawCircle(
                        color = CosmicIndigo.copy(alpha = 0.2f),
                        style = Stroke(
                            width = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    )
                }

                // Center Content
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "MAHA DASHA",
                        style = MaterialTheme.typography.labelSmall,
                        color = VedicGold,
                        letterSpacing = 2.sp,
                        fontFamily = SpaceGroteskFontFamily
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currentDasha?.planet?.getLocalizedName(language)?.uppercase() ?: "SATURN",
                        style = MaterialTheme.typography.displaySmall,
                        color = CosmicIndigo,
                        fontFamily = CinzelDecorativeFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Box(modifier = Modifier.width(32.dp).height(1.dp).background(VedicGold.copy(alpha = 0.5f)))
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    if (currentDasha != null) {
                        val today = LocalDate.now()
                        val remainingDays = ChronoUnit.DAYS.between(today, currentDasha.endDate.toLocalDate())
                        val years = remainingDays / 365
                        val months = (remainingDays % 365) / 30
                        
                        Text(
                            text = "${years} Years, ${months} Months Left",
                            style = MaterialTheme.typography.titleMedium,
                            color = CosmicIndigo.copy(alpha = 0.8f),
                            fontFamily = CormorantGaramondFontFamily,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                        Text(
                            text = "${currentDasha.startDate.format(DateTimeFormatter.ofPattern("MMM yyyy"))} - ${currentDasha.endDate.format(DateTimeFormatter.ofPattern("MMM yyyy"))}",
                            style = MaterialTheme.typography.labelSmall,
                            color = CosmicIndigo.copy(alpha = 0.5f),
                            fontFamily = SpaceGroteskFontFamily,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Bottom Left Label
            Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.BottomStart) {
                Text(
                    text = "CURRENT CYCLE",
                    style = MaterialTheme.typography.labelSmall,
                    color = CosmicIndigo.copy(alpha = 0.4f),
                    fontFamily = SpaceGroteskFontFamily,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun CornerMarker(alignment: Alignment) {
    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .align(alignment)
                .drawBehind {
                    val stroke = 1.dp.toPx()
                    val color = VedicGold.copy(alpha = 0.5f)
                    when (alignment) {
                        Alignment.TopStart -> {
                            drawLine(color, Offset(0f, 0f), Offset(size.width, 0f), stroke)
                            drawLine(color, Offset(0f, 0f), Offset(0f, size.height), stroke)
                        }
                        Alignment.TopEnd -> {
                            drawLine(color, Offset(0f, 0f), Offset(size.width, 0f), stroke)
                            drawLine(color, Offset(size.width, 0f), Offset(size.width, size.height), stroke)
                        }
                        Alignment.BottomStart -> {
                            drawLine(color, Offset(0f, size.height), Offset(size.width, size.height), stroke)
                            drawLine(color, Offset(0f, 0f), Offset(0f, size.height), stroke)
                        }
                        Alignment.BottomEnd -> {
                            drawLine(color, Offset(0f, size.height), Offset(size.width, size.height), stroke)
                            drawLine(color, Offset(size.width, 0f), Offset(size.width, size.height), stroke)
                        }
                    }
                }
        )
    }
}

// ============================================================================
// QUICK ACTIONS
// ============================================================================
@Composable
private fun QuickActionsSection(onFeatureClick: (InsightFeature) -> Unit, language: Language) {
    Column(modifier = Modifier.padding(horizontal = HomeDesignTokens.ScreenPadding)) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            color = CosmicIndigo,
            fontFamily = CinzelDecorativeFontFamily,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionButton(
                title = "Birth Chart",
                icon = Icons.Outlined.GridView,
                color = VedicGold,
                onClick = { onFeatureClick(InsightFeature.FULL_CHART) },
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                title = "Yogas",
                icon = Icons.Outlined.AllInclusive,
                color = VedicGold,
                onClick = { onFeatureClick(InsightFeature.YOGAS) },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionButton(
                title = "Predictions",
                icon = Icons.Outlined.Visibility,
                color = CosmicIndigo,
                onClick = { onFeatureClick(InsightFeature.PREDICTIONS) },
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                title = "Matchmaking",
                icon = Icons.Outlined.FavoriteBorder,
                color = MarsRed,
                onClick = { onFeatureClick(InsightFeature.MATCHMAKING) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.aspectRatio(1.1f),
        color = AppTheme.CardBackground,
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(HomeDesignTokens.CardCornerRadius),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.End)
                    .background(color.copy(alpha = 0.05f), CircleShape)
                    .border(1.dp, color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = CosmicIndigo, modifier = Modifier.size(20.dp))
            }
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = CosmicIndigo,
                fontFamily = SpaceGroteskFontFamily,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ============================================================================
// TODAY'S SNAPSHOT
// ============================================================================
@Composable
private fun TodaySnapshotSection(onPanchangaClick: () -> Unit, onTransitsClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = HomeDesignTokens.ScreenPadding)) {
        Text(
            text = "Today's Snapshot",
            style = MaterialTheme.typography.titleLarge,
            color = CosmicIndigo,
            fontFamily = CinzelDecorativeFontFamily,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SnapshotCard(
                category = "Panchanga",
                title = LocalDate.now().dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
                subtitle = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                icon = Icons.Outlined.CalendarMonth,
                color = VedicGold,
                onClick = onPanchangaClick,
                modifier = Modifier.weight(1f)
            )
            SnapshotCard(
                category = "Transits",
                title = "Live",
                subtitle = "View current planetary positions",
                icon = Icons.Outlined.Update,
                color = CosmicIndigo,
                onClick = onTransitsClick,
                modifier = Modifier.weight(1f),
                showArrow = true
            )
        }
    }
}

@Composable
private fun SnapshotCard(
    category: String,
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showArrow: Boolean = false
) {
    Surface(
        modifier = modifier,
        color = AppTheme.CardBackground,
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(HomeDesignTokens.CardCornerRadius),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier.size(32.dp).background(color.copy(alpha = 0.1f), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
                }
                Text(category.uppercase(), style = MaterialTheme.typography.labelSmall, color = CosmicIndigo, fontFamily = SpaceGroteskFontFamily)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, style = MaterialTheme.typography.titleLarge, color = CosmicIndigo, fontFamily = CinzelDecorativeFontFamily, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = CosmicIndigo.copy(alpha = 0.6f), fontFamily = CormorantGaramondFontFamily)
            
            if (showArrow) {
                Icon(Icons.Outlined.ArrowForward, null, tint = VedicGold, modifier = Modifier.size(16.dp).padding(top = 8.dp))
            }
        }
    }
}

// ============================================================================
// CORE ANALYSIS
// ============================================================================
@Composable
private fun CoreAnalysisSection(onFeatureClick: (InsightFeature) -> Unit, language: Language) {
    Column(modifier = Modifier.padding(horizontal = HomeDesignTokens.ScreenPadding)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Core Analysis",
                style = MaterialTheme.typography.titleLarge,
                color = CosmicIndigo,
                fontFamily = CinzelDecorativeFontFamily,
                fontWeight = FontWeight.Bold
            )
            Surface(color = CosmicIndigo.copy(alpha = 0.05f), shape = RoundedCornerShape(2.dp)) {
                Text(
                    "6",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            color = AppTheme.CardBackground,
            border = BorderStroke(1.dp, AppTheme.BorderColor),
            shape = RoundedCornerShape(HomeDesignTokens.CardCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier.size(40.dp).background(AppTheme.ScreenBackground).border(BorderStroke(1.dp, AppTheme.BorderColor), RoundedCornerShape(2.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.GridView, null, tint = CosmicIndigo.copy(alpha = 0.7f))
                        }
                        Column {
                            Text("Core Analysis", style = MaterialTheme.typography.titleMedium, color = CosmicIndigo, fontFamily = CormorantGaramondFontFamily, fontWeight = FontWeight.Bold)
                            Text("Charts, Planets, Nakshatras".uppercase(), style = MaterialTheme.typography.labelSmall, color = CosmicIndigo.copy(alpha = 0.5f), fontFamily = SpaceGroteskFontFamily, fontSize = 8.sp)
                        }
                    }
                    Icon(Icons.Default.ExpandMore, null, tint = CosmicIndigo.copy(alpha = 0.4f))
                }
                
                val items = listOf(
                    Triple("Birth Chart", Icons.Outlined.GridOn, InsightFeature.FULL_CHART),
                    Triple("Planets", Icons.Outlined.Public, InsightFeature.PLANETS),
                    Triple("Nakshatras", Icons.Outlined.Star, InsightFeature.NAKSHATRA_ANALYSIS),
                    Triple("Divisional", Icons.Outlined.Dashboard, InsightFeature.DIVISIONAL_CHARTS),
                    Triple("Shodasha", Icons.Outlined.Apps, InsightFeature.SHODASHVARGA),
                    Triple("Ashtakavarga", Icons.Outlined.BarChart, InsightFeature.ASHTAKAVARGA)
                )
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { item ->
                                AnalysisItem(item.first, item.second, onClick = { onFeatureClick(item.third) }, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnalysisItem(title: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        color = AppTheme.ScreenBackground,
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        shape = RoundedCornerShape(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(32.dp).background(AppTheme.CardBackground).border(BorderStroke(1.dp, AppTheme.BorderColor), RoundedCornerShape(2.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = CosmicIndigo.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
            }
            Text(title.uppercase(), style = MaterialTheme.typography.labelSmall, color = CosmicIndigo, fontFamily = SpaceGroteskFontFamily, fontSize = 9.sp)
        }
    }
}

// ============================================================================
// EMPTY STATE (Fallback)
// ============================================================================
@Composable
private fun EmptyHomeState(onCreateProfile: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().background(AppTheme.ScreenBackground), contentAlignment = Alignment.Center) {
        Button(onClick = onCreateProfile) { Text("Create Chart") }
    }
}

// Reuse InsightFeature enum from original file
@Stable
enum class InsightFeature(
    val titleKey: com.astro.storm.core.common.StringKeyInterface,
    val descriptionKey: com.astro.storm.core.common.StringKeyInterface,
    val icon: ImageVector,
    val color: Color,
    val isImplemented: Boolean
) {
    FULL_CHART(StringKey.FEATURE_BIRTH_CHART, StringKey.FEATURE_BIRTH_CHART_DESC, Icons.Outlined.GridView, VedicGold, true),
    PLANETS(StringKey.FEATURE_PLANETS, StringKey.FEATURE_PLANETS_DESC, Icons.Outlined.Public, VedicGold, true),
    YOGAS(StringKey.FEATURE_YOGAS, StringKey.FEATURE_YOGAS_DESC, Icons.Outlined.AutoAwesome, VedicGold, true),
    DASHAS(StringKey.FEATURE_DASHAS, StringKey.FEATURE_DASHAS_DESC, Icons.Outlined.Timeline, CosmicIndigo, true),
    TRANSITS(StringKey.FEATURE_TRANSITS, StringKey.FEATURE_TRANSITS_DESC, Icons.Outlined.Sync, CosmicIndigo, true),
    ASHTAKAVARGA(StringKey.FEATURE_ASHTAKAVARGA, StringKey.FEATURE_ASHTAKAVARGA_DESC, Icons.Outlined.BarChart, VedicGold, true),
    PANCHANGA(StringKey.FEATURE_PANCHANGA, StringKey.FEATURE_PANCHANGA_DESC, Icons.Outlined.CalendarMonth, VedicGold, true),
    MATCHMAKING(StringKey.FEATURE_MATCHMAKING, StringKey.FEATURE_MATCHMAKING_DESC, Icons.Outlined.Favorite, MarsRed, true),
    PREDICTIONS(StringKey.FEATURE_PREDICTIONS, StringKey.FEATURE_PREDICTIONS_DESC, Icons.Outlined.AutoAwesome, CosmicIndigo, true),
    NAKSHATRA_ANALYSIS(StringKey.FEATURE_NAKSHATRAS, StringKey.FEATURE_NAKSHATRAS_DESC, Icons.Outlined.Stars, VedicGold, true),
    SHODASHVARGA(StringKey.FEATURE_SHODASHVARGA, StringKey.FEATURE_SHODASHVARGA_DESC, Icons.Outlined.GridView, VedicGold, true),
    DIVISIONAL_CHARTS(StringKey.FEATURE_DIVISIONAL_CHARTS, StringKey.FEATURE_DIVISIONAL_CHARTS_DESC, Icons.Outlined.GridView, CosmicIndigo, true),
    NATIVE_ANALYSIS(StringKey.FEATURE_NATIVE_ANALYSIS, StringKey.FEATURE_NATIVE_ANALYSIS_DESC, Icons.Outlined.Person, CosmicIndigo, true);

    fun getLocalizedTitle(language: Language): String = StringResources.get(titleKey, language)
}
