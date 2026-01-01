package com.astro.storm.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Brightness2
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.StringKey
import com.astro.storm.data.localization.StringResources
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ui.theme.AppTheme

/**
 * Completely Revamped Home Tab - Modern, Minimal, Professional UI
 *
 * Design Principles:
 * - Clean, uncluttered layout with logical grouping
 * - Compact responsive design that adapts to screen sizes
 * - Quick access to most-used features
 * - Expandable categories for better organization
 * - Modern card design with subtle gradients
 * - Proper spacing and visual hierarchy
 */

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

    // Track expanded state for each category
    val expandedCategories = remember { mutableStateMapOf<FeatureCategory, Boolean>() }
    val language = LocalLanguage.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Profile Header Card
        item(key = "profile_header") {
            ProfileHeaderCard(
                chart = chart,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        // Quick Actions - Horizontal scrollable chips for most-used features
        item(key = "quick_actions") {
            QuickActionsSection(
                onFeatureClick = onFeatureClick,
                language = language
            )
        }

        // Feature Categories - Organized groups
        items(
            items = FeatureCategory.entries,
            key = { it.name }
        ) { category ->
            val isExpanded = expandedCategories[category] ?: category.defaultExpanded

            FeatureCategoryCard(
                category = category,
                isExpanded = isExpanded,
                onToggleExpand = { expandedCategories[category] = !isExpanded },
                onFeatureClick = onFeatureClick,
                language = language,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        // Bottom spacer
        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Compact Profile Header showing current profile summary
 */
@Composable
private fun ProfileHeaderCard(
    chart: VedicChart,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.CardBackground
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            AppTheme.AccentPrimary.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AppTheme.AccentPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chart.birthData.name.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.AccentPrimary
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Profile Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chart.birthData.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Lagna/Ascendant
                    Text(
                        text = chart.ascendantSign.getLocalizedName(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.AccentGold,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = " • ",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted
                    )
                    // Moon Nakshatra
                    Text(
                        text = chart.moonNakshatra.getLocalizedName(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            // Current Dasha Indicator
            chart.currentMahaDasha?.let { dasha ->
                Surface(
                    color = AppTheme.AccentTeal.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dasha.planet.symbol,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.AccentTeal
                        )
                        Text(
                            text = stringResource(StringKey.DASHA_CURRENT),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }
            }
        }
    }
}

/**
 * Horizontal scrollable quick actions for most-used features
 */
@Composable
private fun QuickActionsSection(
    onFeatureClick: (InsightFeature) -> Unit,
    language: Language
) {
    val quickFeatures = remember {
        listOf(
            InsightFeature.FULL_CHART,
            InsightFeature.PLANETS,
            InsightFeature.YOGAS,
            InsightFeature.DASHAS,
            InsightFeature.TRANSITS,
            InsightFeature.MATCHMAKING
        )
    }

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = stringResource(StringKey.HOME_QUICK_ACCESS),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextMuted,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(quickFeatures, key = { it.name }) { feature ->
                QuickActionChip(
                    feature = feature,
                    onClick = { onFeatureClick(feature) },
                    language = language
                )
            }
        }
    }
}

/**
 * Compact quick action chip
 */
@Suppress("DEPRECATION")
@Composable
private fun QuickActionChip(
    feature: InsightFeature,
    onClick: () -> Unit,
    language: Language
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = feature.color),
                onClick = onClick
            ),
        color = feature.color.copy(alpha = 0.10f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = null,
                tint = feature.color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = feature.getLocalizedTitle(language),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = feature.color,
                maxLines = 1
            )
        }
    }
}

/**
 * Expandable category card containing related features
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FeatureCategoryCard(
    category: FeatureCategory,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onFeatureClick: (InsightFeature) -> Unit,
    language: Language,
    modifier: Modifier = Modifier
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300),
        label = "arrow_rotation"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Category Header - Always visible, clickable to expand
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggleExpand)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(category.color.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = null,
                        tint = category.color,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = category.getLocalizedTitle(language),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = category.getLocalizedDescription(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Feature count badge
                Surface(
                    color = AppTheme.ChipBackground,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${category.features.size}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextMuted,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (isExpanded)
                        stringResource(StringKey.BTN_COLLAPSE)
                    else
                        stringResource(StringKey.BTN_EXPAND),
                    tint = AppTheme.TextMuted,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotationAngle)
                )
            }

            // Expandable Feature Grid
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    category.features.forEach { feature ->
                        CompactFeatureChip(
                            feature = feature,
                            onClick = { onFeatureClick(feature) },
                            language = language
                        )
                    }
                }
            }
        }
    }
}

/**
 * Compact feature chip for use inside categories
 */
@Suppress("DEPRECATION")
@Composable
private fun CompactFeatureChip(
    feature: InsightFeature,
    onClick: () -> Unit,
    language: Language
) {
    val interactionSource = remember { MutableInteractionSource() }
    val title = feature.getLocalizedTitle(language)

    Surface(
        modifier = Modifier
            .semantics { contentDescription = title }
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = feature.color),
                onClick = onClick
            ),
        color = AppTheme.ChipBackground,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(feature.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = feature.color,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Feature Categories - Organized groupings of related features
 */
@Stable
enum class FeatureCategory(
    val titleKey: StringKey,
    val descriptionKey: StringKey,
    val icon: ImageVector,
    val color: Color,
    val features: List<InsightFeature>,
    val defaultExpanded: Boolean = false
) {
    CORE_ANALYSIS(
        titleKey = StringKey.CATEGORY_CORE_ANALYSIS,
        descriptionKey = StringKey.CATEGORY_CORE_ANALYSIS_DESC,
        icon = Icons.Outlined.GridView,
        color = Color(0xFF6B5D4D),
        features = listOf(
            InsightFeature.FULL_CHART,
            InsightFeature.PLANETS,
            InsightFeature.NAKSHATRA_ANALYSIS,
            InsightFeature.SHADBALA,
            InsightFeature.AVASTHA
        ),
        defaultExpanded = true
    ),
    YOGAS_COMBINATIONS(
        titleKey = StringKey.CATEGORY_YOGAS,
        descriptionKey = StringKey.CATEGORY_YOGAS_DESC,
        icon = Icons.Outlined.AutoAwesome,
        color = Color(0xFFD4AF37),
        features = listOf(
            InsightFeature.YOGAS,
            InsightFeature.KEMADRUMA_YOGA,
            InsightFeature.PANCH_MAHAPURUSHA,
            InsightFeature.NITYA_YOGA
        )
    ),
    DASHA_SYSTEMS(
        titleKey = StringKey.CATEGORY_DASHAS,
        descriptionKey = StringKey.CATEGORY_DASHAS_DESC,
        icon = Icons.Outlined.Timeline,
        color = Color(0xFF7B1FA2),
        features = listOf(
            InsightFeature.DASHAS,
            InsightFeature.YOGINI_DASHA,
            InsightFeature.ASHTOTTARI_DASHA,
            InsightFeature.CHARA_DASHA,
            InsightFeature.KALACHAKRA_DASHA,
            InsightFeature.SUDARSHANA_CHAKRA,
            InsightFeature.DASHA_SANDHI
        )
    ),
    TRANSITS_GOCHARA(
        titleKey = StringKey.CATEGORY_TRANSITS,
        descriptionKey = StringKey.CATEGORY_TRANSITS_DESC,
        icon = Icons.Outlined.Sync,
        color = Color(0xFF00897B),
        features = listOf(
            InsightFeature.TRANSITS,
            InsightFeature.TARABALA,
            InsightFeature.UPACHAYA_TRANSIT,
            InsightFeature.GOCHARA_VEDHA
        )
    ),
    DIVISIONAL_STRENGTH(
        titleKey = StringKey.CATEGORY_DIVISIONAL,
        descriptionKey = StringKey.CATEGORY_DIVISIONAL_DESC,
        icon = Icons.Outlined.BarChart,
        color = Color(0xFF388E3C),
        features = listOf(
            InsightFeature.SHODASHVARGA,
            InsightFeature.DIVISIONAL_CHARTS,
            InsightFeature.ASHTAKAVARGA,
            InsightFeature.ARGALA,
            InsightFeature.ARUDHA_PADA
        )
    ),
    PREDICTIONS_TIMING(
        titleKey = StringKey.CATEGORY_PREDICTIONS,
        descriptionKey = StringKey.CATEGORY_PREDICTIONS_DESC,
        icon = Icons.Outlined.Stars,
        color = Color(0xFFFF6F00),
        features = listOf(
            InsightFeature.PREDICTIONS,
            InsightFeature.VARSHAPHALA,
            InsightFeature.PANCHANGA,
            InsightFeature.MUHURTA,
            InsightFeature.BHRIGU_BINDU,
            InsightFeature.MRITYU_BHAGA
        )
    ),
    COMPATIBILITY_MATCHING(
        titleKey = StringKey.CATEGORY_COMPATIBILITY,
        descriptionKey = StringKey.CATEGORY_COMPATIBILITY_DESC,
        icon = Icons.Outlined.Favorite,
        color = Color(0xFFD32F2F),
        features = listOf(
            InsightFeature.MATCHMAKING,
            InsightFeature.CHART_COMPARISON,
            InsightFeature.GRAHA_YUDDHA
        )
    ),
    REMEDIES_GUIDANCE(
        titleKey = StringKey.CATEGORY_REMEDIES,
        descriptionKey = StringKey.CATEGORY_REMEDIES_DESC,
        icon = Icons.Outlined.Spa,
        color = Color(0xFF0097A7),
        features = listOf(
            InsightFeature.REMEDIES,
            InsightFeature.LAL_KITAB,
            InsightFeature.PRASHNA
        )
    );

    fun getLocalizedTitle(language: Language): String {
        return StringResources.get(titleKey, language)
    }

    fun getLocalizedDescription(language: Language): String {
        return StringResources.get(descriptionKey, language)
    }
}

/**
 * All available insight features with localization support
 */
@Stable
enum class InsightFeature(
    val titleKey: StringKey,
    val descriptionKey: StringKey,
    val icon: ImageVector,
    val color: Color
) {
    FULL_CHART(
        titleKey = StringKey.FEATURE_BIRTH_CHART,
        descriptionKey = StringKey.FEATURE_BIRTH_CHART_DESC,
        icon = Icons.Outlined.GridView,
        color = Color(0xFF6B5D4D)
    ),
    PLANETS(
        titleKey = StringKey.FEATURE_PLANETS,
        descriptionKey = StringKey.FEATURE_PLANETS_DESC,
        icon = Icons.Outlined.Public,
        color = Color(0xFFED6C02)
    ),
    YOGAS(
        titleKey = StringKey.FEATURE_YOGAS,
        descriptionKey = StringKey.FEATURE_YOGAS_DESC,
        icon = Icons.Outlined.AutoAwesome,
        color = Color(0xFFD4AF37)
    ),
    DASHAS(
        titleKey = StringKey.FEATURE_DASHAS,
        descriptionKey = StringKey.FEATURE_DASHAS_DESC,
        icon = Icons.Outlined.Timeline,
        color = Color(0xFF7B1FA2)
    ),
    TRANSITS(
        titleKey = StringKey.FEATURE_TRANSITS,
        descriptionKey = StringKey.FEATURE_TRANSITS_DESC,
        icon = Icons.Outlined.Sync,
        color = Color(0xFF00897B)
    ),
    ASHTAKAVARGA(
        titleKey = StringKey.FEATURE_ASHTAKAVARGA,
        descriptionKey = StringKey.FEATURE_ASHTAKAVARGA_DESC,
        icon = Icons.Outlined.BarChart,
        color = Color(0xFF388E3C)
    ),
    PANCHANGA(
        titleKey = StringKey.FEATURE_PANCHANGA,
        descriptionKey = StringKey.FEATURE_PANCHANGA_DESC,
        icon = Icons.Outlined.CalendarMonth,
        color = Color(0xFFF9A825)
    ),
    MATCHMAKING(
        titleKey = StringKey.FEATURE_MATCHMAKING,
        descriptionKey = StringKey.FEATURE_MATCHMAKING_DESC,
        icon = Icons.Outlined.Favorite,
        color = Color(0xFFD32F2F)
    ),
    MUHURTA(
        titleKey = StringKey.FEATURE_MUHURTA,
        descriptionKey = StringKey.FEATURE_MUHURTA_DESC,
        icon = Icons.Outlined.AccessTime,
        color = Color(0xFFFF6F00)
    ),
    REMEDIES(
        titleKey = StringKey.FEATURE_REMEDIES,
        descriptionKey = StringKey.FEATURE_REMEDIES_DESC,
        icon = Icons.Outlined.Spa,
        color = Color(0xFF0097A7)
    ),
    VARSHAPHALA(
        titleKey = StringKey.FEATURE_VARSHAPHALA,
        descriptionKey = StringKey.FEATURE_VARSHAPHALA_DESC,
        icon = Icons.Outlined.Cake,
        color = Color(0xFFED6C02)
    ),
    PRASHNA(
        titleKey = StringKey.FEATURE_PRASHNA,
        descriptionKey = StringKey.FEATURE_PRASHNA_DESC,
        icon = Icons.Outlined.HelpOutline,
        color = Color(0xFF00897B)
    ),
    CHART_COMPARISON(
        titleKey = StringKey.FEATURE_SYNASTRY,
        descriptionKey = StringKey.FEATURE_SYNASTRY_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = Color(0xFFF9A825)
    ),
    NAKSHATRA_ANALYSIS(
        titleKey = StringKey.FEATURE_NAKSHATRAS,
        descriptionKey = StringKey.FEATURE_NAKSHATRAS_DESC,
        icon = Icons.Outlined.Stars,
        color = Color(0xFFD4AF37)
    ),
    SHADBALA(
        titleKey = StringKey.FEATURE_SHADBALA,
        descriptionKey = StringKey.FEATURE_SHADBALA_DESC,
        icon = Icons.Outlined.Speed,
        color = Color(0xFF388E3C)
    ),
    SHODASHVARGA(
        titleKey = StringKey.FEATURE_SHODASHVARGA,
        descriptionKey = StringKey.FEATURE_SHODASHVARGA_DESC,
        icon = Icons.Outlined.GridView,
        color = Color(0xFFD4AF37)
    ),
    YOGINI_DASHA(
        titleKey = StringKey.FEATURE_YOGINI_DASHA,
        descriptionKey = StringKey.FEATURE_YOGINI_DASHA_DESC,
        icon = Icons.Outlined.Timeline,
        color = Color(0xFFD32F2F)
    ),
    ARGALA(
        titleKey = StringKey.FEATURE_ARGALA,
        descriptionKey = StringKey.FEATURE_ARGALA_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = Color(0xFF00897B)
    ),
    CHARA_DASHA(
        titleKey = StringKey.FEATURE_CHARA_DASHA,
        descriptionKey = StringKey.FEATURE_CHARA_DASHA_DESC,
        icon = Icons.Outlined.Sync,
        color = Color(0xFF7B1FA2)
    ),
    BHRIGU_BINDU(
        titleKey = StringKey.FEATURE_BHRIGU_BINDU,
        descriptionKey = StringKey.FEATURE_BHRIGU_BINDU_DESC,
        icon = Icons.Outlined.Stars,
        color = Color(0xFFFF6F00)
    ),
    PREDICTIONS(
        titleKey = StringKey.FEATURE_PREDICTIONS,
        descriptionKey = StringKey.FEATURE_PREDICTIONS_DESC,
        icon = Icons.Outlined.AutoAwesome,
        color = Color(0xFF6B5D4D)
    ),
    ASHTOTTARI_DASHA(
        titleKey = StringKey.FEATURE_ASHTOTTARI_DASHA,
        descriptionKey = StringKey.FEATURE_ASHTOTTARI_DASHA_DESC,
        icon = Icons.Outlined.Timeline,
        color = Color(0xFFD4AF37)
    ),
    SUDARSHANA_CHAKRA(
        titleKey = StringKey.FEATURE_SUDARSHANA_CHAKRA,
        descriptionKey = StringKey.FEATURE_SUDARSHANA_CHAKRA_DESC,
        icon = Icons.Outlined.Sync,
        color = Color(0xFF7B1FA2)
    ),
    MRITYU_BHAGA(
        titleKey = StringKey.FEATURE_MRITYU_BHAGA,
        descriptionKey = StringKey.FEATURE_MRITYU_BHAGA_DESC,
        icon = Icons.Outlined.BarChart,
        color = Color(0xFFFF6F00)
    ),
    LAL_KITAB(
        titleKey = StringKey.FEATURE_LAL_KITAB,
        descriptionKey = StringKey.FEATURE_LAL_KITAB_DESC,
        icon = Icons.Outlined.Spa,
        color = Color(0xFF0097A7)
    ),
    DIVISIONAL_CHARTS(
        titleKey = StringKey.FEATURE_DIVISIONAL_CHARTS,
        descriptionKey = StringKey.FEATURE_DIVISIONAL_CHARTS_DESC,
        icon = Icons.Outlined.GridView,
        color = Color(0xFF00897B)
    ),
    UPACHAYA_TRANSIT(
        titleKey = StringKey.FEATURE_UPACHAYA_TRANSIT,
        descriptionKey = StringKey.FEATURE_UPACHAYA_TRANSIT_DESC,
        icon = Icons.Outlined.Stars,
        color = Color(0xFF388E3C)
    ),
    KALACHAKRA_DASHA(
        titleKey = StringKey.FEATURE_KALACHAKRA_DASHA,
        descriptionKey = StringKey.FEATURE_KALACHAKRA_DASHA_DESC,
        icon = Icons.Outlined.HealthAndSafety,
        color = Color(0xFF7B1FA2)
    ),
    TARABALA(
        titleKey = StringKey.FEATURE_TARABALA,
        descriptionKey = StringKey.FEATURE_TARABALA_DESC,
        icon = Icons.Outlined.Stars,
        color = Color(0xFFB22222)
    ),
    ARUDHA_PADA(
        titleKey = StringKey.FEATURE_ARUDHA_PADA,
        descriptionKey = StringKey.FEATURE_ARUDHA_PADA_DESC,
        icon = Icons.Outlined.Spa,
        color = Color(0xFF7B1FA2)
    ),
    GRAHA_YUDDHA(
        titleKey = StringKey.FEATURE_GRAHA_YUDDHA,
        descriptionKey = StringKey.FEATURE_GRAHA_YUDDHA_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = Color(0xFFFF6F00)
    ),
    DASHA_SANDHI(
        titleKey = StringKey.FEATURE_DASHA_SANDHI,
        descriptionKey = StringKey.FEATURE_DASHA_SANDHI_DESC,
        icon = Icons.Outlined.Timeline,
        color = Color(0xFF7B1FA2)
    ),
    GOCHARA_VEDHA(
        titleKey = StringKey.FEATURE_GOCHARA_VEDHA,
        descriptionKey = StringKey.FEATURE_GOCHARA_VEDHA_DESC,
        icon = Icons.Outlined.Block,
        color = Color(0xFFFF6F00)
    ),
    KEMADRUMA_YOGA(
        titleKey = StringKey.FEATURE_KEMADRUMA_YOGA,
        descriptionKey = StringKey.FEATURE_KEMADRUMA_YOGA_DESC,
        icon = Icons.Outlined.Brightness2,
        color = Color(0xFFB22222)
    ),
    PANCH_MAHAPURUSHA(
        titleKey = StringKey.FEATURE_PANCH_MAHAPURUSHA,
        descriptionKey = StringKey.FEATURE_PANCH_MAHAPURUSHA_DESC,
        icon = Icons.Outlined.Stars,
        color = Color(0xFFD4AF37)
    ),
    NITYA_YOGA(
        titleKey = StringKey.FEATURE_NITYA_YOGA,
        descriptionKey = StringKey.FEATURE_NITYA_YOGA_DESC,
        icon = Icons.Outlined.CalendarMonth,
        color = Color(0xFF00897B)
    ),
    AVASTHA(
        titleKey = StringKey.FEATURE_AVASTHA,
        descriptionKey = StringKey.FEATURE_AVASTHA_DESC,
        icon = Icons.Outlined.Psychology,
        color = Color(0xFF6B5D4D)
    );

    fun getLocalizedTitle(language: Language): String {
        return StringResources.get(titleKey, language)
    }

    fun getLocalizedDescription(language: Language): String {
        return StringResources.get(descriptionKey, language)
    }
}

/**
 * Minimal Empty State UI for Home Screen
 */
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
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(colors.ChipBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonAddAlt,
                    contentDescription = null,
                    tint = colors.TextMuted,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(StringKey.NO_PROFILE_SELECTED),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = colors.TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(StringKey.NO_PROFILE_MESSAGE),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onCreateProfile,
                modifier = Modifier
                    .height(52.dp)
                    .widthIn(min = 200.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.AccentPrimary,
                    contentColor = colors.ButtonText
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(StringKey.BTN_CREATE_CHART),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
