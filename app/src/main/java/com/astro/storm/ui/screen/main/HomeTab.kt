package com.astro.storm.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Brightness2
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Grid4x4
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAdvanced
import com.astro.storm.core.common.StringKeyInterface
import com.astro.storm.core.common.StringKeyShadbala
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.DarkAppThemeColors

/**
 * Revamped Home Tab - Professional Vedic Astrology Dashboard
 *
 * Design Philosophy:
 * - Clean, uncluttered interface with clear visual hierarchy
 * - Chart summary at top for quick reference
 * - Categorized features in horizontal scrollable sections
 * - Performance optimized with stable keys and minimal recomposition
 * - Professional vedic astrology app aesthetic
 */

// ============================================================================
// FEATURE CATEGORIES - Organized grouping for better UX
// ============================================================================

@Immutable
enum class FeatureCategory(
    val titleKey: StringKey,
    val color: Color
) {
    CHART_BASICS(StringKey.CATEGORY_CHART_BASICS, DarkAppThemeColors.AccentPrimary),
    TIMING_SYSTEMS(StringKey.CATEGORY_TIMING_SYSTEMS, DarkAppThemeColors.LifeAreaSpiritual),
    STRENGTH_ANALYSIS(StringKey.CATEGORY_STRENGTH_ANALYSIS, DarkAppThemeColors.SuccessColor),
    PREDICTIONS(StringKey.CATEGORY_PREDICTIONS, DarkAppThemeColors.AccentGold),
    COMPATIBILITY(StringKey.CATEGORY_COMPATIBILITY, DarkAppThemeColors.LifeAreaLove),
    ADVANCED(StringKey.CATEGORY_ADVANCED, DarkAppThemeColors.AccentTeal);

    fun getLocalizedTitle(language: Language): String {
        return StringResources.get(titleKey, language)
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
        EmptyHomeState(
            onCreateProfile = onAddNewChart,
            modifier = modifier
        )
        return
    }

    val colors = AppTheme.current
    val language = LocalLanguage.current

    // Pre-compute categorized features for performance
    val categorizedFeatures = remember {
        mapOf(
            FeatureCategory.CHART_BASICS to listOf(
                InsightFeature.FULL_CHART,
                InsightFeature.PLANETS,
                InsightFeature.NAKSHATRA_ANALYSIS,
                InsightFeature.DIVISIONAL_CHARTS,
                InsightFeature.SHODASHVARGA,
                InsightFeature.NATIVE_ANALYSIS
            ),
            FeatureCategory.TIMING_SYSTEMS to listOf(
                InsightFeature.DASHAS,
                InsightFeature.YOGINI_DASHA,
                InsightFeature.CHARA_DASHA,
                InsightFeature.ASHTOTTARI_DASHA,
                InsightFeature.KALACHAKRA_DASHA,
                InsightFeature.SHOOLA_DASHA,
                InsightFeature.DASHA_SANDHI
            ),
            FeatureCategory.STRENGTH_ANALYSIS to listOf(
                InsightFeature.SHADBALA,
                InsightFeature.ASHTAKAVARGA,
                InsightFeature.STHANA_BALA,
                InsightFeature.KALA_BALA,
                InsightFeature.DRIG_BALA,
                InsightFeature.ISHTA_KASHTA_PHALA
            ),
            FeatureCategory.PREDICTIONS to listOf(
                InsightFeature.PREDICTIONS,
                InsightFeature.YOGAS,
                InsightFeature.TRANSITS,
                InsightFeature.VARSHAPHALA,
                InsightFeature.PANCHANGA,
                InsightFeature.MUHURTA,
                InsightFeature.PRASHNA
            ),
            FeatureCategory.COMPATIBILITY to listOf(
                InsightFeature.MATCHMAKING,
                InsightFeature.CHART_COMPARISON,
                InsightFeature.TARABALA
            ),
            FeatureCategory.ADVANCED to listOf(
                InsightFeature.ARGALA,
                InsightFeature.ARUDHA_PADA,
                InsightFeature.BHRIGU_BINDU,
                InsightFeature.SUDARSHANA_CHAKRA,
                InsightFeature.MRITYU_BHAGA,
                InsightFeature.GRAHA_YUDDHA,
                InsightFeature.GOCHARA_VEDHA,
                InsightFeature.REMEDIES,
                InsightFeature.LAL_KITAB,
                InsightFeature.AVASTHA,
                InsightFeature.MARAKA,
                InsightFeature.BADHAKA,
                InsightFeature.KEMADRUMA_YOGA,
                InsightFeature.PANCH_MAHAPURUSHA,
                InsightFeature.VIPAREETA_RAJA_YOGA,
                InsightFeature.NITYA_YOGA,
                InsightFeature.UPACHAYA_TRANSIT,
                InsightFeature.ASHTAVARGA_TRANSIT,
                InsightFeature.KAKSHYA_TRANSIT,
                InsightFeature.NADI_AMSHA,
                InsightFeature.SARVATOBHADRA_CHAKRA,
                InsightFeature.SAHAM
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.ScreenBackground),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Chart Summary Card
        item(key = "chart_summary") {
            ChartSummaryCard(
                chart = chart,
                onViewChart = { onFeatureClick(InsightFeature.FULL_CHART) },
                modifier = Modifier.padding(16.dp)
            )
        }

        // Quick Actions Row
        item(key = "quick_actions") {
            QuickActionsRow(
                onFeatureClick = onFeatureClick,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Divider
        item(key = "divider_1") {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                color = colors.DividerColor.copy(alpha = 0.5f),
                thickness = 0.5.dp
            )
        }

        // Feature Categories
        FeatureCategory.entries.forEach { category ->
            val features = categorizedFeatures[category] ?: emptyList()
            if (features.isNotEmpty()) {
                item(key = "section_${category.name}") {
                    FeatureCategorySection(
                        category = category,
                        features = features,
                        onFeatureClick = onFeatureClick
                    )
                }
            }
        }

        // Bottom spacing
        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ============================================================================
// CHART SUMMARY CARD - Shows key natal chart information at a glance
// ============================================================================

@Composable
private fun ChartSummaryCard(
    chart: VedicChart,
    onViewChart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current
    val language = LocalLanguage.current

    // Extract chart data
    val ascendantSign = chart.ascendant.sign
    val moonSign = chart.planets.find { it.planet == Planet.MOON }?.sign ?: ascendantSign
    val sunSign = chart.planets.find { it.planet == Planet.SUN }?.sign ?: ascendantSign

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(StringKey.HOME_CHART_OVERVIEW),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )

                Surface(
                    onClick = onViewChart,
                    shape = RoundedCornerShape(8.dp),
                    color = colors.AccentPrimary.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(StringKey.HOME_VIEW_CHART),
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.AccentPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Outlined.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = colors.AccentPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Key positions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ChartPositionItem(
                    label = stringResource(StringKey.LABEL_ASCENDANT),
                    sign = ascendantSign,
                    icon = Icons.Outlined.Person
                )
                ChartPositionItem(
                    label = stringResource(StringKey.LABEL_MOON),
                    sign = moonSign,
                    icon = Icons.Outlined.Brightness2
                )
                ChartPositionItem(
                    label = stringResource(StringKey.LABEL_SUN),
                    sign = sunSign,
                    icon = Icons.Outlined.Public
                )
            }
        }
    }
}

@Composable
private fun ChartPositionItem(
    label: String,
    sign: ZodiacSign,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current
    val signColor = AppTheme.getSignColor(sign)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon with colored background
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(signColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = signColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = colors.TextMuted
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = sign.displayName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = colors.TextPrimary
        )
    }
}

// ============================================================================
// QUICK ACTIONS ROW - Most commonly used features
// ============================================================================

@Composable
private fun QuickActionsRow(
    onFeatureClick: (InsightFeature) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current
    val quickActions = remember {
        listOf(
            InsightFeature.FULL_CHART,
            InsightFeature.DASHAS,
            InsightFeature.TRANSITS,
            InsightFeature.YOGAS
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        quickActions.forEach { feature ->
            QuickActionChip(
                feature = feature,
                onClick = { onFeatureClick(feature) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Suppress("DEPRECATION")
@Composable
private fun QuickActionChip(
    feature: InsightFeature,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current
    val language = LocalLanguage.current
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
        color = feature.color.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = null,
                tint = feature.color,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = feature.getLocalizedTitle(language),
                style = MaterialTheme.typography.labelSmall,
                color = colors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ============================================================================
// FEATURE CATEGORY SECTION - Horizontal scrollable feature cards
// ============================================================================

@Composable
private fun FeatureCategorySection(
    category: FeatureCategory,
    features: List<InsightFeature>,
    onFeatureClick: (InsightFeature) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current
    val language = LocalLanguage.current

    Column(modifier = modifier) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 20.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(category.color)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = category.getLocalizedTitle(language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )
            }
        }

        // Horizontal scrolling features
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = features,
                key = { it.name }
            ) { feature ->
                CompactFeatureCard(
                    feature = feature,
                    onClick = { onFeatureClick(feature) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Suppress("DEPRECATION")
@Composable
private fun CompactFeatureCard(
    feature: InsightFeature,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current
    val language = LocalLanguage.current
    val interactionSource = remember { MutableInteractionSource() }
    val title = feature.getLocalizedTitle(language)

    Card(
        modifier = modifier
            .width(140.dp)
            .semantics { contentDescription = title }
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = feature.color),
                onClick = onClick
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(feature.color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = feature.color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = colors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clearAndSetSemantics { }
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = feature.getLocalizedDescription(language),
                style = MaterialTheme.typography.bodySmall,
                color = colors.TextMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 14.sp,
                modifier = Modifier.clearAndSetSemantics { }
            )
        }
    }
}

// ============================================================================
// EMPTY HOME STATE
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
                    pressedElevation = 0.dp
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

// ============================================================================
// INSIGHT FEATURE ENUM - All available features
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
