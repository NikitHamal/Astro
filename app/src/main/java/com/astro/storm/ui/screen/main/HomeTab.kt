package com.astro.storm.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Brightness2
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
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
import com.astro.storm.ui.theme.DarkAppThemeColors

@Stable
enum class FeatureCategory(
    val labelKey: StringKey,
    val icon: ImageVector,
    val color: Color
) {
    ALL(
        labelKey = StringKey.CATEGORY_ALL,
        icon = Icons.Outlined.GridView,
        color = DarkAppThemeColors.AccentPrimary
    ),
    CHARTS(
        labelKey = StringKey.CATEGORY_CHARTS,
        icon = Icons.Outlined.GridView,
        color = DarkAppThemeColors.AccentTeal
    ),
    DASHAS(
        labelKey = StringKey.CATEGORY_DASHAS,
        icon = Icons.Outlined.Timeline,
        color = DarkAppThemeColors.LifeAreaSpiritual
    ),
    YOGAS(
        labelKey = StringKey.CATEGORY_YOGAS,
        icon = Icons.Outlined.AutoAwesome,
        color = DarkAppThemeColors.AccentGold
    ),
    TRANSITS(
        labelKey = StringKey.CATEGORY_TRANSITS,
        icon = Icons.Outlined.Sync,
        color = DarkAppThemeColors.SuccessColor
    ),
    PREDICTIONS(
        labelKey = StringKey.CATEGORY_PREDICTIONS,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.LifeAreaFinance
    ),
    COMPATIBILITY(
        labelKey = StringKey.CATEGORY_COMPATIBILITY,
        icon = Icons.Outlined.Favorite,
        color = DarkAppThemeColors.LifeAreaLove
    );

    fun getLocalizedLabel(language: Language): String = StringResources.get(labelKey, language)
}

@Stable
enum class InsightFeature(
    val titleKey: StringKey,
    val descriptionKey: StringKey,
    val icon: ImageVector,
    val color: Color,
    val category: FeatureCategory,
    val isPinned: Boolean = false
) {
    FULL_CHART(
        titleKey = StringKey.FEATURE_BIRTH_CHART,
        descriptionKey = StringKey.FEATURE_BIRTH_CHART_DESC,
        icon = Icons.Outlined.GridView,
        color = DarkAppThemeColors.AccentPrimary,
        category = FeatureCategory.CHARTS,
        isPinned = true
    ),
    PLANETS(
        titleKey = StringKey.FEATURE_PLANETS,
        descriptionKey = StringKey.FEATURE_PLANETS_DESC,
        icon = Icons.Outlined.Public,
        color = DarkAppThemeColors.LifeAreaCareer,
        category = FeatureCategory.CHARTS,
        isPinned = true
    ),
    YOGAS(
        titleKey = StringKey.FEATURE_YOGAS,
        descriptionKey = StringKey.FEATURE_YOGAS_DESC,
        icon = Icons.Outlined.AutoAwesome,
        color = DarkAppThemeColors.AccentGold,
        category = FeatureCategory.YOGAS,
        isPinned = true
    ),
    DASHAS(
        titleKey = StringKey.FEATURE_DASHAS,
        descriptionKey = StringKey.FEATURE_DASHAS_DESC,
        icon = Icons.Outlined.Timeline,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        category = FeatureCategory.DASHAS,
        isPinned = true
    ),
    TRANSITS(
        titleKey = StringKey.FEATURE_TRANSITS,
        descriptionKey = StringKey.FEATURE_TRANSITS_DESC,
        icon = Icons.Outlined.Sync,
        color = DarkAppThemeColors.AccentTeal,
        category = FeatureCategory.TRANSITS,
        isPinned = true
    ),
    PREDICTIONS(
        titleKey = StringKey.FEATURE_PREDICTIONS,
        descriptionKey = StringKey.FEATURE_PREDICTIONS_DESC,
        icon = Icons.Outlined.AutoAwesome,
        color = DarkAppThemeColors.AccentPrimary,
        category = FeatureCategory.PREDICTIONS,
        isPinned = true
    ),
    ASHTAKAVARGA(
        titleKey = StringKey.FEATURE_ASHTAKAVARGA,
        descriptionKey = StringKey.FEATURE_ASHTAKAVARGA_DESC,
        icon = Icons.Outlined.BarChart,
        color = DarkAppThemeColors.SuccessColor,
        category = FeatureCategory.CHARTS
    ),
    PANCHANGA(
        titleKey = StringKey.FEATURE_PANCHANGA,
        descriptionKey = StringKey.FEATURE_PANCHANGA_DESC,
        icon = Icons.Outlined.CalendarMonth,
        color = DarkAppThemeColors.LifeAreaFinance,
        category = FeatureCategory.TRANSITS
    ),
    MATCHMAKING(
        titleKey = StringKey.FEATURE_MATCHMAKING,
        descriptionKey = StringKey.FEATURE_MATCHMAKING_DESC,
        icon = Icons.Outlined.Favorite,
        color = DarkAppThemeColors.LifeAreaLove,
        category = FeatureCategory.COMPATIBILITY
    ),
    MUHURTA(
        titleKey = StringKey.FEATURE_MUHURTA,
        descriptionKey = StringKey.FEATURE_MUHURTA_DESC,
        icon = Icons.Outlined.AccessTime,
        color = DarkAppThemeColors.WarningColor,
        category = FeatureCategory.TRANSITS
    ),
    REMEDIES(
        titleKey = StringKey.FEATURE_REMEDIES,
        descriptionKey = StringKey.FEATURE_REMEDIES_DESC,
        icon = Icons.Outlined.Spa,
        color = DarkAppThemeColors.LifeAreaHealth,
        category = FeatureCategory.PREDICTIONS
    ),
    VARSHAPHALA(
        titleKey = StringKey.FEATURE_VARSHAPHALA,
        descriptionKey = StringKey.FEATURE_VARSHAPHALA_DESC,
        icon = Icons.Outlined.Cake,
        color = DarkAppThemeColors.LifeAreaCareer,
        category = FeatureCategory.PREDICTIONS
    ),
    PRASHNA(
        titleKey = StringKey.FEATURE_PRASHNA,
        descriptionKey = StringKey.FEATURE_PRASHNA_DESC,
        icon = Icons.Outlined.HelpOutline,
        color = DarkAppThemeColors.AccentTeal,
        category = FeatureCategory.PREDICTIONS
    ),
    CHART_COMPARISON(
        titleKey = StringKey.FEATURE_SYNASTRY,
        descriptionKey = StringKey.FEATURE_SYNASTRY_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = DarkAppThemeColors.LifeAreaFinance,
        category = FeatureCategory.COMPATIBILITY
    ),
    NAKSHATRA_ANALYSIS(
        titleKey = StringKey.FEATURE_NAKSHATRAS,
        descriptionKey = StringKey.FEATURE_NAKSHATRAS_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.AccentGold,
        category = FeatureCategory.CHARTS
    ),
    SHADBALA(
        titleKey = StringKey.FEATURE_SHADBALA,
        descriptionKey = StringKey.FEATURE_SHADBALA_DESC,
        icon = Icons.Outlined.Speed,
        color = DarkAppThemeColors.SuccessColor,
        category = FeatureCategory.CHARTS
    ),
    SHODASHVARGA(
        titleKey = StringKey.FEATURE_SHODASHVARGA,
        descriptionKey = StringKey.FEATURE_SHODASHVARGA_DESC,
        icon = Icons.Outlined.GridView,
        color = DarkAppThemeColors.AccentGold,
        category = FeatureCategory.CHARTS
    ),
    YOGINI_DASHA(
        titleKey = StringKey.FEATURE_YOGINI_DASHA,
        descriptionKey = StringKey.FEATURE_YOGINI_DASHA_DESC,
        icon = Icons.Outlined.Timeline,
        color = DarkAppThemeColors.LifeAreaLove,
        category = FeatureCategory.DASHAS
    ),
    ARGALA(
        titleKey = StringKey.FEATURE_ARGALA,
        descriptionKey = StringKey.FEATURE_ARGALA_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = DarkAppThemeColors.AccentTeal,
        category = FeatureCategory.CHARTS
    ),
    CHARA_DASHA(
        titleKey = StringKey.FEATURE_CHARA_DASHA,
        descriptionKey = StringKey.FEATURE_CHARA_DASHA_DESC,
        icon = Icons.Outlined.Sync,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        category = FeatureCategory.DASHAS
    ),
    BHRIGU_BINDU(
        titleKey = StringKey.FEATURE_BHRIGU_BINDU,
        descriptionKey = StringKey.FEATURE_BHRIGU_BINDU_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.WarningColor,
        category = FeatureCategory.CHARTS
    ),
    ASHTOTTARI_DASHA(
        titleKey = StringKey.FEATURE_ASHTOTTARI_DASHA,
        descriptionKey = StringKey.FEATURE_ASHTOTTARI_DASHA_DESC,
        icon = Icons.Outlined.Timeline,
        color = DarkAppThemeColors.AccentGold,
        category = FeatureCategory.DASHAS
    ),
    SUDARSHANA_CHAKRA(
        titleKey = StringKey.FEATURE_SUDARSHANA_CHAKRA,
        descriptionKey = StringKey.FEATURE_SUDARSHANA_CHAKRA_DESC,
        icon = Icons.Outlined.Sync,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        category = FeatureCategory.CHARTS
    ),
    MRITYU_BHAGA(
        titleKey = StringKey.FEATURE_MRITYU_BHAGA,
        descriptionKey = StringKey.FEATURE_MRITYU_BHAGA_DESC,
        icon = Icons.Outlined.BarChart,
        color = DarkAppThemeColors.WarningColor,
        category = FeatureCategory.CHARTS
    ),
    LAL_KITAB(
        titleKey = StringKey.FEATURE_LAL_KITAB,
        descriptionKey = StringKey.FEATURE_LAL_KITAB_DESC,
        icon = Icons.Outlined.Spa,
        color = DarkAppThemeColors.LifeAreaHealth,
        category = FeatureCategory.PREDICTIONS
    ),
    DIVISIONAL_CHARTS(
        titleKey = StringKey.FEATURE_DIVISIONAL_CHARTS,
        descriptionKey = StringKey.FEATURE_DIVISIONAL_CHARTS_DESC,
        icon = Icons.Outlined.GridView,
        color = DarkAppThemeColors.AccentTeal,
        category = FeatureCategory.CHARTS
    ),
    UPACHAYA_TRANSIT(
        titleKey = StringKey.FEATURE_UPACHAYA_TRANSIT,
        descriptionKey = StringKey.FEATURE_UPACHAYA_TRANSIT_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.SuccessColor,
        category = FeatureCategory.TRANSITS
    ),
    KALACHAKRA_DASHA(
        titleKey = StringKey.FEATURE_KALACHAKRA_DASHA,
        descriptionKey = StringKey.FEATURE_KALACHAKRA_DASHA_DESC,
        icon = Icons.Outlined.HealthAndSafety,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        category = FeatureCategory.DASHAS
    ),
    TARABALA(
        titleKey = StringKey.FEATURE_TARABALA,
        descriptionKey = StringKey.FEATURE_TARABALA_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.PlanetMoon,
        category = FeatureCategory.TRANSITS
    ),
    ARUDHA_PADA(
        titleKey = StringKey.FEATURE_ARUDHA_PADA,
        descriptionKey = StringKey.FEATURE_ARUDHA_PADA_DESC,
        icon = Icons.Outlined.Spa,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        category = FeatureCategory.CHARTS
    ),
    GRAHA_YUDDHA(
        titleKey = StringKey.FEATURE_GRAHA_YUDDHA,
        descriptionKey = StringKey.FEATURE_GRAHA_YUDDHA_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = DarkAppThemeColors.WarningColor,
        category = FeatureCategory.YOGAS
    ),
    DASHA_SANDHI(
        titleKey = StringKey.FEATURE_DASHA_SANDHI,
        descriptionKey = StringKey.FEATURE_DASHA_SANDHI_DESC,
        icon = Icons.Outlined.Timeline,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        category = FeatureCategory.DASHAS
    ),
    GOCHARA_VEDHA(
        titleKey = StringKey.FEATURE_GOCHARA_VEDHA,
        descriptionKey = StringKey.FEATURE_GOCHARA_VEDHA_DESC,
        icon = Icons.Outlined.Block,
        color = DarkAppThemeColors.WarningColor,
        category = FeatureCategory.TRANSITS
    ),
    KEMADRUMA_YOGA(
        titleKey = StringKey.FEATURE_KEMADRUMA_YOGA,
        descriptionKey = StringKey.FEATURE_KEMADRUMA_YOGA_DESC,
        icon = Icons.Outlined.Brightness2,
        color = DarkAppThemeColors.PlanetMoon,
        category = FeatureCategory.YOGAS
    ),
    PANCH_MAHAPURUSHA(
        titleKey = StringKey.FEATURE_PANCH_MAHAPURUSHA,
        descriptionKey = StringKey.FEATURE_PANCH_MAHAPURUSHA_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.AccentGold,
        category = FeatureCategory.YOGAS
    ),
    NITYA_YOGA(
        titleKey = StringKey.FEATURE_NITYA_YOGA,
        descriptionKey = StringKey.FEATURE_NITYA_YOGA_DESC,
        icon = Icons.Outlined.CalendarMonth,
        color = DarkAppThemeColors.AccentTeal,
        category = FeatureCategory.YOGAS
    ),
    AVASTHA(
        titleKey = StringKey.FEATURE_AVASTHA,
        descriptionKey = StringKey.FEATURE_AVASTHA_DESC,
        icon = Icons.Outlined.Psychology,
        color = DarkAppThemeColors.AccentPrimary,
        category = FeatureCategory.CHARTS
    );

    fun getLocalizedTitle(language: Language): String = StringResources.get(titleKey, language)
    fun getLocalizedDescription(language: Language): String = StringResources.get(descriptionKey, language)

    companion object {
        val pinnedFeatures: List<InsightFeature> by lazy(LazyThreadSafetyMode.PUBLICATION) {
            entries.filter { it.isPinned }
        }

        fun getByCategory(category: FeatureCategory): List<InsightFeature> =
            if (category == FeatureCategory.ALL) entries.toList()
            else entries.filter { it.category == category }
    }
}

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

    var selectedCategory by rememberSaveable { mutableStateOf(FeatureCategory.ALL) }
    val language = LocalLanguage.current

    val filteredFeatures = remember(selectedCategory) {
        InsightFeature.getByCategory(selectedCategory)
    }

    val groupedFeatures = remember(filteredFeatures, selectedCategory) {
        if (selectedCategory == FeatureCategory.ALL) {
            filteredFeatures.groupBy { it.category }
                .filterKeys { it != FeatureCategory.ALL }
                .toSortedMap(compareBy { it.ordinal })
        } else {
            mapOf(selectedCategory to filteredFeatures)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground),
        contentPadding = contentPadding
    ) {
        item(key = "category_filter") {
            CategoryFilterRow(
                categories = FeatureCategory.entries,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (selectedCategory == FeatureCategory.ALL) {
            item(key = "quick_access") {
                QuickAccessSection(
                    features = InsightFeature.pinnedFeatures,
                    onFeatureClick = onFeatureClick,
                    language = language
                )
            }
        }

        groupedFeatures.forEach { (category, features) ->
            item(key = "section_${category.name}") {
                ExpandableCategorySection(
                    category = category,
                    features = features,
                    onFeatureClick = onFeatureClick,
                    language = language,
                    initiallyExpanded = selectedCategory != FeatureCategory.ALL
                )
            }
        }

        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CategoryFilterRow(
    categories: List<FeatureCategory>,
    selectedCategory: FeatureCategory,
    onCategorySelected: (FeatureCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            CategoryChip(
                label = category.getLocalizedLabel(language),
                isSelected = selectedCategory == category,
                color = category.color,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Suppress("DEPRECATION")
@Composable
private fun CategoryChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) color.copy(alpha = 0.15f) else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "chip_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) color else AppTheme.Divider,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "chip_border"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) color else AppTheme.TextMuted,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "chip_text"
    )

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = color),
                onClick = onClick
            ),
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = textColor,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun QuickAccessSection(
    features: List<InsightFeature>,
    onFeatureClick: (InsightFeature) -> Unit,
    language: Language,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(top = 20.dp)) {
        Text(
            text = stringResource(StringKey.HOME_QUICK_ACCESS),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = features,
                key = { it.name }
            ) { feature ->
                QuickAccessCard(
                    feature = feature,
                    language = language,
                    onClick = { onFeatureClick(feature) }
                )
            }
        }
    }
}

@Suppress("DEPRECATION")
@Composable
private fun QuickAccessCard(
    feature: InsightFeature,
    language: Language,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val title = feature.getLocalizedTitle(language)
    val description = feature.getLocalizedDescription(language)

    Surface(
        modifier = modifier
            .width(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(color = feature.color),
                onClick = onClick
            )
            .semantics { contentDescription = "$title: $description" },
        shape = RoundedCornerShape(16.dp),
        color = AppTheme.CardBackground,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                feature.color.copy(alpha = 0.2f),
                                feature.color.copy(alpha = 0.08f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = feature.color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clearAndSetSemantics { }
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 14.sp,
                modifier = Modifier.clearAndSetSemantics { }
            )
        }
    }
}

@Composable
private fun ExpandableCategorySection(
    category: FeatureCategory,
    features: List<InsightFeature>,
    onFeatureClick: (InsightFeature) -> Unit,
    language: Language,
    initiallyExpanded: Boolean = true,
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable(category) { mutableStateOf(initiallyExpanded) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "rotation"
    )

    Column(modifier = modifier.padding(top = 16.dp)) {
        CategoryHeader(
            category = category,
            language = language,
            isExpanded = isExpanded,
            rotationAngle = rotationAngle,
            featureCount = features.size,
            onClick = { isExpanded = !isExpanded }
        )

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            CompactFeatureGrid(
                features = features,
                onFeatureClick = onFeatureClick,
                language = language,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Suppress("DEPRECATION")
@Composable
private fun CategoryHeader(
    category: FeatureCategory,
    language: Language,
    isExpanded: Boolean,
    rotationAngle: Float,
    featureCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(category.color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = category.color,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.getLocalizedLabel(language),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Text(
                text = "$featureCount ${stringResource(StringKey.FEATURES_LABEL)}",
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
        }

        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = if (isExpanded) 
                stringResource(StringKey.COLLAPSE) 
            else 
                stringResource(StringKey.EXPAND),
            tint = AppTheme.TextMuted,
            modifier = Modifier
                .size(24.dp)
                .rotate(rotationAngle)
        )
    }
}

@Composable
private fun CompactFeatureGrid(
    features: List<InsightFeature>,
    onFeatureClick: (InsightFeature) -> Unit,
    language: Language,
    modifier: Modifier = Modifier,
    columns: Int = 3
) {
    val chunkedFeatures = remember(features, columns) { features.chunked(columns) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        chunkedFeatures.forEach { rowFeatures ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowFeatures.forEach { feature ->
                    CompactFeatureItem(
                        feature = feature,
                        language = language,
                        onClick = { onFeatureClick(feature) },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(columns - rowFeatures.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Suppress("DEPRECATION")
@Composable
private fun CompactFeatureItem(
    feature: InsightFeature,
    language: Language,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val title = feature.getLocalizedTitle(language)
    val description = feature.getLocalizedDescription(language)

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(color = feature.color),
                onClick = onClick
            )
            .semantics { contentDescription = "$title: $description" },
        shape = RoundedCornerShape(12.dp),
        color = AppTheme.CardBackground,
        tonalElevation = 0.5.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
                modifier = Modifier.clearAndSetSemantics { }
            )
        }
    }
}

@Composable
private fun EmptyHomeState(
    onCreateProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colors.ScreenBackground,
                        colors.CardBackground.copy(alpha = 0.5f)
                    )
                )
            )
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
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                colors.AccentPrimary.copy(alpha = 0.15f),
                                colors.AccentPrimary.copy(alpha = 0.05f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = colors.AccentPrimary.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonAddAlt,
                    contentDescription = null,
                    tint = colors.AccentPrimary,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = stringResource(StringKey.NO_PROFILE_SELECTED),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = colors.TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(StringKey.NO_PROFILE_MESSAGE),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.widthIn(max = 280.dp)
            )

            Spacer(modifier = Modifier.height(36.dp))

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