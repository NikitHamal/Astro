package com.astro.storm.ui.screen.main

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
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.HelpOutline
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
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

@Composable
fun HomeTab(
    chart: VedicChart?,
    onFeatureClick: (InsightFeature) -> Unit,
    onAddNewChart: () -> Unit = {},
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(bottom = 100.dp)
) {
    val colors = AppTheme.current

    if (chart == null) {
        EmptyHomeState(
            onCreateProfile = onAddNewChart,
            modifier = modifier
        )
        return
    }

    val essentials = remember {
        listOf(
            InsightFeature.FULL_CHART,
            InsightFeature.PREDICTIONS,
            InsightFeature.PANCHANGA,
            InsightFeature.MATCHMAKING
        )
    }

    val planetary = remember {
        listOf(
            InsightFeature.PLANETS,
            InsightFeature.TRANSITS,
            InsightFeature.DASHAS,
            InsightFeature.NAKSHATRA_ANALYSIS,
            InsightFeature.YOGAS,
            InsightFeature.REMEDIES
        )
    }

    val advanced = remember {
        InsightFeature.entries.filter {
            !essentials.contains(it) && !planetary.contains(it) && it.isImplemented
        }
    }

    val comingSoon = remember { InsightFeature.comingSoonFeatures }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.ScreenBackground),
        contentPadding = contentPadding
    ) {
        item {
            HeroSection(
                onFeatureClick = onFeatureClick,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            QuickAccessRow(
                features = essentials.drop(2), 
                onFeatureClick = onFeatureClick
            )
        }

        item {
            CategorySection(
                title = stringResource(StringKey.FEATURE_PLANETS),
                features = planetary,
                onFeatureClick = onFeatureClick
            )
        }

        item {
            CategorySection(
                title = stringResource(StringKey.HOME_CHART_ANALYSIS), 
                features = advanced,
                onFeatureClick = onFeatureClick
            )
        }

        if (comingSoon.isNotEmpty()) {
            item {
                CategorySection(
                    title = stringResource(StringKey.HOME_COMING_SOON),
                    features = comingSoon,
                    onFeatureClick = {},
                    isDisabled = true
                )
            }
        }
    }
}

@Composable
private fun HeroSection(
    onFeatureClick: (InsightFeature) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeroCard(
            feature = InsightFeature.FULL_CHART,
            onClick = { onFeatureClick(InsightFeature.FULL_CHART) },
            modifier = Modifier.weight(0.6f).height(160.dp),
            isPrimary = true
        )
        
        Column(
            modifier = Modifier.weight(0.4f).height(160.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HeroCard(
                feature = InsightFeature.PREDICTIONS,
                onClick = { onFeatureClick(InsightFeature.PREDICTIONS) },
                modifier = Modifier.weight(1f).fillMaxWidth(),
                isPrimary = false
            )
            HeroCard(
                feature = InsightFeature.MUHURTA,
                onClick = { onFeatureClick(InsightFeature.MUHURTA) },
                modifier = Modifier.weight(1f).fillMaxWidth(),
                isPrimary = false
            )
        }
    }
}

@Composable
private fun HeroCard(
    feature: InsightFeature,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean
) {
    val title = feature.getLocalizedTitle(LocalLanguage.current)
    val desc = feature.getLocalizedDescription(LocalLanguage.current)
    
    val bgBrush = if (isPrimary) {
        Brush.linearGradient(
            colors = listOf(
                AppTheme.current.AccentPrimary,
                AppTheme.current.AccentPrimary.copy(alpha = 0.8f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                AppTheme.current.CardBackground,
                AppTheme.current.CardBackground
            )
        )
    }

    val contentColor = if (isPrimary) AppTheme.current.ButtonText else AppTheme.current.TextPrimary
    val subColor = if (isPrimary) AppTheme.current.ButtonText.copy(alpha = 0.8f) else AppTheme.current.TextMuted

    ElevatedCard(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (isPrimary) 4.dp else 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgBrush)
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isPrimary) Color.White.copy(alpha = 0.2f) else feature.color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = feature.icon,
                        contentDescription = null,
                        tint = if (isPrimary) Color.White else feature.color,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        style = if (isPrimary) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = contentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (isPrimary) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = subColor,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickAccessRow(
    features: List<InsightFeature>,
    onFeatureClick: (InsightFeature) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        features.forEach { feature ->
            QuickAccessCard(
                feature = feature,
                onClick = { onFeatureClick(feature) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickAccessCard(
    feature: InsightFeature,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(16.dp),
        color = AppTheme.current.CardBackground,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(feature.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = feature.color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = feature.getLocalizedTitle(LocalLanguage.current),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.current.TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategorySection(
    title: String,
    features: List<InsightFeature>,
    onFeatureClick: (InsightFeature) -> Unit,
    isDisabled: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isDisabled) AppTheme.current.TextMuted else AppTheme.current.TextPrimary,
                modifier = Modifier.weight(1f)
            )
            if (!isDisabled) {
                HorizontalDivider(
                    modifier = Modifier
                        .width(40.dp)
                        .padding(start = 12.dp),
                    color = AppTheme.current.DividerColor
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 2
        ) {
            features.forEach { feature ->
                ModernFeatureCard(
                    feature = feature,
                    onClick = { onFeatureClick(feature) },
                    isDisabled = isDisabled,
                    modifier = Modifier.weight(1f).fillMaxWidth(0.45f)
                )
            }
            if (features.size % 2 != 0) {
                 Spacer(modifier = Modifier.weight(1f).fillMaxWidth(0.45f))
            }
        }
    }
}

@Composable
private fun ModernFeatureCard(
    feature: InsightFeature,
    onClick: () -> Unit,
    isDisabled: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current
    val containerColor = if (isDisabled) colors.CardBackground.copy(alpha = 0.5f) else colors.CardBackground
    val contentAlpha = if (isDisabled) 0.5f else 1f
    
    Surface(
        onClick = onClick,
        enabled = !isDisabled,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        tonalElevation = if (isDisabled) 0.dp else 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isDisabled) colors.TextSubtle.copy(alpha = 0.1f) else feature.color.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = if (isDisabled) colors.TextSubtle else feature.color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = feature.getLocalizedTitle(LocalLanguage.current),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary.copy(alpha = contentAlpha),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = feature.getLocalizedDescription(LocalLanguage.current),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.TextMuted.copy(alpha = contentAlpha),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 12.sp
                )
            }
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
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(colors.AccentPrimary.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonAddAlt,
                    contentDescription = null,
                    tint = colors.AccentPrimary,
                    modifier = Modifier.size(48.dp)
                )
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
                style = MaterialTheme.typography.bodyLarge,
                color = colors.TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onCreateProfile,
                modifier = Modifier
                    .height(56.dp)
                    .widthIn(min = 220.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.AccentPrimary,
                    contentColor = colors.ButtonText
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 2.dp
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
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Stable
enum class InsightFeature(
    val titleKey: StringKey,
    val descriptionKey: StringKey,
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
    );

    fun getLocalizedTitle(language: Language): String {
        return StringResources.get(titleKey, language)
    }

    fun getLocalizedDescription(language: Language): String {
        return StringResources.get(descriptionKey, language)
    }

    companion object {
        val comingSoonFeatures: List<InsightFeature> by lazy(LazyThreadSafetyMode.PUBLICATION) {
            entries.filter { !it.isImplemented }
        }
    }
}