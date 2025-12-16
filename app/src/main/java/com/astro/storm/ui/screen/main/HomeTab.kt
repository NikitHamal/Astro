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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.StringKey
import com.astro.storm.data.localization.StringResources
import com.astro.storm.data.localization.getLocalizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ui.theme.AppTheme

private val GridSpacing = 12.dp
private val CardCornerRadius = 16.dp
private val IconContainerSize = 44.dp
private val IconContainerCornerRadius = 12.dp
private val IconSize = 24.dp
private val CardContentPadding = 16.dp

private val HeroGradientStart = Color(0xFF7C4DFF)
private val HeroGradientEnd = Color(0xFF536DFE)

private val ChartAnalysisBlue = Color(0xFFE3F2FD)
private val ChartAnalysisOrange = Color(0xFFFFF3E0)
private val ChartAnalysisPink = Color(0xFFFCE4EC)
private val ChartAnalysisGreen = Color(0xFFE8F5E9)

private val ToolPink = Color(0xFFFCE4EC)
private val ToolRed = Color(0xFFFFEBEE)
private val ToolBlue = Color(0xFFE3F2FD)

@Composable
fun HomeTab(
    chart: VedicChart?,
    onFeatureClick: (InsightFeature) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(bottom = 100.dp)
) {
    if (chart == null) {
        EmptyHomeState(modifier = modifier)
        return
    }

    val language = LocalLanguage.current
    val chartAnalysisFeatures = remember {
        listOf(
            InsightFeature.FULL_CHART,
            InsightFeature.PLANETS,
            InsightFeature.YOGAS,
            InsightFeature.DASHAS
        )
    }
    val toolsFeatures = remember {
        listOf(
            InsightFeature.PANCHANGA,
            InsightFeature.MATCHMAKING,
            InsightFeature.MUHURTA
        )
    }
    val comingSoonFeatures = remember {
        listOf(InsightFeature.CHART_COMPARISON)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground),
        contentPadding = contentPadding
    ) {
        item(key = "welcome_header") {
            WelcomeHeader(
                name = chart.birthData.name,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }

        item(key = "hero_card") {
            TodayEnergyHeroCard(
                chart = chart,
                language = language,
                onViewInsights = { onFeatureClick(InsightFeature.PREDICTIONS) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item(key = "chart_analysis_header") {
            SectionHeaderWithAction(
                title = stringResource(StringKey.HOME_CHART_ANALYSIS),
                actionText = stringResource(StringKey.HOME_VIEW_ALL),
                onActionClick = { },
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 12.dp)
            )
        }

        item(key = "chart_analysis_grid") {
            ChartAnalysisGrid(
                features = chartAnalysisFeatures,
                onFeatureClick = onFeatureClick,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item(key = "tools_header") {
            SectionHeader(
                text = stringResource(StringKey.HOME_ASTROLOGY_TOOLS),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 12.dp)
            )
        }

        item(key = "tools_row") {
            AstrologyToolsRow(
                features = toolsFeatures,
                onFeatureClick = onFeatureClick,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        item(key = "coming_soon_header") {
            SectionHeader(
                text = stringResource(StringKey.HOME_COMING_SOON),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 12.dp)
            )
        }

        item(key = "coming_soon_card") {
            ComingSoonCard(
                feature = comingSoonFeatures.first(),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun WelcomeHeader(
    name: String,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current
    val greeting = when (language) {
        Language.ENGLISH -> "Welcome back,"
        Language.NEPALI -> "स्वागत छ,"
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = greeting,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(AppTheme.AccentPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.split(" ")
                    .take(2)
                    .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                    .joinToString(""),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.AccentPrimary
            )
        }
    }
}

@Composable
private fun TodayEnergyHeroCard(
    chart: VedicChart,
    language: Language,
    onViewInsights: () -> Unit,
    modifier: Modifier = Modifier
) {
    val moonSign = chart.planetPositions.find {
        it.planet == com.astro.storm.data.model.Planet.MOON
    }?.sign?.getLocalizedName(language) ?: ""

    val titleText = when (language) {
        Language.ENGLISH -> "Discipline & Growth"
        Language.NEPALI -> "अनुशासन र विकास"
    }

    val descriptionText = when (language) {
        Language.ENGLISH -> "Saturn's influence calls for patience today.\nEmbrace challenges as opportunities for..."
        Language.NEPALI -> "आज शनिको प्रभावले धैर्यको आवश्यकता छ।\nचुनौतीहरूलाई अवसरको रूपमा..."
    }

    val viewInsightsText = when (language) {
        Language.ENGLISH -> "View Insights"
        Language.NEPALI -> "अन्तर्दृष्टि हेर्नुहोस्"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(HeroGradientStart, HeroGradientEnd)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (language) {
                                Language.ENGLISH -> "Today's Energy"
                                Language.NEPALI -> "आजको ऊर्जा"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Outlined.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = titleText,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = descriptionText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "\uD83E\uDDD8",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "\uD83D\uDE0A",
                            fontSize = 20.sp
                        )
                    }

                    Surface(
                        onClick = onViewInsights,
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = viewInsightsText,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeaderWithAction(
    title: String,
    actionText: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary
        )
        TextButton(onClick = onActionClick) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.AccentPrimary
            )
        }
    }
}

@Composable
private fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = AppTheme.TextPrimary,
        modifier = modifier
    )
}

@Composable
private fun ChartAnalysisGrid(
    features: List<InsightFeature>,
    onFeatureClick: (InsightFeature) -> Unit,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current
    val colors = listOf(ChartAnalysisBlue, ChartAnalysisOrange, ChartAnalysisPink, ChartAnalysisGreen)
    val iconColors = listOf(
        Color(0xFF1976D2),
        Color(0xFFF57C00),
        Color(0xFFE91E63),
        Color(0xFF388E3C)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(GridSpacing)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(GridSpacing)
        ) {
            features.take(2).forEachIndexed { index, feature ->
                ChartAnalysisCard(
                    feature = feature,
                    backgroundColor = colors[index],
                    iconColor = iconColors[index],
                    onClick = { onFeatureClick(feature) },
                    language = language,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(GridSpacing)
        ) {
            features.drop(2).take(2).forEachIndexed { index, feature ->
                ChartAnalysisCard(
                    feature = feature,
                    backgroundColor = colors[index + 2],
                    iconColor = iconColors[index + 2],
                    onClick = { onFeatureClick(feature) },
                    language = language,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ChartAnalysisCard(
    feature: InsightFeature,
    backgroundColor: Color,
    iconColor: Color,
    onClick: () -> Unit,
    language: Language,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(CardCornerRadius))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CardContentPadding)
        ) {
            Box(
                modifier = Modifier
                    .size(IconContainerSize)
                    .clip(RoundedCornerShape(IconContainerCornerRadius))
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(IconSize)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = feature.getLocalizedTitle(language),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = feature.getLocalizedDescription(language),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF666666),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun AstrologyToolsRow(
    features: List<InsightFeature>,
    onFeatureClick: (InsightFeature) -> Unit,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current
    val colors = listOf(ToolPink, ToolRed, ToolBlue)
    val iconColors = listOf(
        Color(0xFFE91E63),
        Color(0xFFE53935),
        Color(0xFF1976D2)
    )

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        features.forEachIndexed { index, feature ->
            AstrologyToolCard(
                feature = feature,
                backgroundColor = colors.getOrElse(index) { ToolBlue },
                iconColor = iconColors.getOrElse(index) { Color(0xFF1976D2) },
                onClick = { onFeatureClick(feature) },
                language = language
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
private fun AstrologyToolCard(
    feature: InsightFeature,
    backgroundColor: Color,
    iconColor: Color,
    onClick: () -> Unit,
    language: Language,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(120.dp)
            .clip(RoundedCornerShape(CardCornerRadius))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = feature.getLocalizedTitle(language),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1A1A),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = feature.getLocalizedDescription(language),
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF888888),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ComingSoonCard(
    feature: InsightFeature,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppTheme.TextMuted.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = feature.getLocalizedTitle(language),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = feature.getLocalizedDescription(language),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = AppTheme.TextMuted.copy(alpha = 0.12f)
            ) {
                Text(
                    text = stringResource(StringKey.HOME_SOON_BADGE),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
        color = AppTheme.AccentPrimary,
        isImplemented = true
    ),
    PLANETS(
        titleKey = StringKey.FEATURE_PLANETS,
        descriptionKey = StringKey.FEATURE_PLANETS_DESC,
        icon = Icons.Outlined.Public,
        color = AppTheme.LifeAreaCareer,
        isImplemented = true
    ),
    YOGAS(
        titleKey = StringKey.FEATURE_YOGAS,
        descriptionKey = StringKey.FEATURE_YOGAS_DESC,
        icon = Icons.Outlined.AutoAwesome,
        color = AppTheme.AccentGold,
        isImplemented = true
    ),
    DASHAS(
        titleKey = StringKey.FEATURE_DASHAS,
        descriptionKey = StringKey.FEATURE_DASHAS_DESC,
        icon = Icons.Outlined.Timeline,
        color = AppTheme.LifeAreaSpiritual,
        isImplemented = true
    ),
    TRANSITS(
        titleKey = StringKey.FEATURE_TRANSITS,
        descriptionKey = StringKey.FEATURE_TRANSITS_DESC,
        icon = Icons.Outlined.Sync,
        color = AppTheme.AccentTeal,
        isImplemented = true
    ),
    ASHTAKAVARGA(
        titleKey = StringKey.FEATURE_ASHTAKAVARGA,
        descriptionKey = StringKey.FEATURE_ASHTAKAVARGA_DESC,
        icon = Icons.Outlined.BarChart,
        color = AppTheme.SuccessColor,
        isImplemented = true
    ),
    PANCHANGA(
        titleKey = StringKey.FEATURE_PANCHANGA,
        descriptionKey = StringKey.FEATURE_PANCHANGA_DESC,
        icon = Icons.Outlined.CalendarMonth,
        color = AppTheme.LifeAreaFinance,
        isImplemented = true
    ),
    MATCHMAKING(
        titleKey = StringKey.FEATURE_MATCHMAKING,
        descriptionKey = StringKey.FEATURE_MATCHMAKING_DESC,
        icon = Icons.Outlined.Favorite,
        color = AppTheme.LifeAreaLove,
        isImplemented = true
    ),
    MUHURTA(
        titleKey = StringKey.FEATURE_MUHURTA,
        descriptionKey = StringKey.FEATURE_MUHURTA_DESC,
        icon = Icons.Outlined.AccessTime,
        color = AppTheme.WarningColor,
        isImplemented = true
    ),
    REMEDIES(
        titleKey = StringKey.FEATURE_REMEDIES,
        descriptionKey = StringKey.FEATURE_REMEDIES_DESC,
        icon = Icons.Outlined.Spa,
        color = AppTheme.LifeAreaHealth,
        isImplemented = true
    ),
    VARSHAPHALA(
        titleKey = StringKey.FEATURE_VARSHAPHALA,
        descriptionKey = StringKey.FEATURE_VARSHAPHALA_DESC,
        icon = Icons.Outlined.Cake,
        color = AppTheme.LifeAreaCareer,
        isImplemented = true
    ),
    PRASHNA(
        titleKey = StringKey.FEATURE_PRASHNA,
        descriptionKey = StringKey.FEATURE_PRASHNA_DESC,
        icon = Icons.Outlined.HelpOutline,
        color = AppTheme.AccentTeal,
        isImplemented = true
    ),
    CHART_COMPARISON(
        titleKey = StringKey.FEATURE_SYNASTRY,
        descriptionKey = StringKey.FEATURE_SYNASTRY_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = AppTheme.LifeAreaFinance,
        isImplemented = true
    ),
    NAKSHATRA_ANALYSIS(
        titleKey = StringKey.FEATURE_NAKSHATRAS,
        descriptionKey = StringKey.FEATURE_NAKSHATRAS_DESC,
        icon = Icons.Outlined.Stars,
        color = AppTheme.AccentGold,
        isImplemented = true
    ),
    SHADBALA(
        titleKey = StringKey.FEATURE_SHADBALA,
        descriptionKey = StringKey.FEATURE_SHADBALA_DESC,
        icon = Icons.Outlined.Speed,
        color = AppTheme.SuccessColor,
        isImplemented = true
    ),
    SHODASHVARGA(
        titleKey = StringKey.FEATURE_SHODASHVARGA,
        descriptionKey = StringKey.FEATURE_SHODASHVARGA_DESC,
        icon = Icons.Outlined.GridView,
        color = AppTheme.AccentGold,
        isImplemented = true
    ),
    YOGINI_DASHA(
        titleKey = StringKey.FEATURE_YOGINI_DASHA,
        descriptionKey = StringKey.FEATURE_YOGINI_DASHA_DESC,
        icon = Icons.Outlined.Timeline,
        color = AppTheme.LifeAreaLove,
        isImplemented = true
    ),
    ARGALA(
        titleKey = StringKey.FEATURE_ARGALA,
        descriptionKey = StringKey.FEATURE_ARGALA_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = AppTheme.AccentTeal,
        isImplemented = true
    ),
    CHARA_DASHA(
        titleKey = StringKey.FEATURE_CHARA_DASHA,
        descriptionKey = StringKey.FEATURE_CHARA_DASHA_DESC,
        icon = Icons.Outlined.Sync,
        color = AppTheme.LifeAreaSpiritual,
        isImplemented = true
    ),
    BHRIGU_BINDU(
        titleKey = StringKey.FEATURE_BHRIGU_BINDU,
        descriptionKey = StringKey.FEATURE_BHRIGU_BINDU_DESC,
        icon = Icons.Outlined.Stars,
        color = AppTheme.WarningColor,
        isImplemented = true
    ),
    PREDICTIONS(
        titleKey = StringKey.FEATURE_PREDICTIONS,
        descriptionKey = StringKey.FEATURE_PREDICTIONS_DESC,
        icon = Icons.Outlined.AutoAwesome,
        color = AppTheme.AccentPrimary,
        isImplemented = true
    ),
    ASHTOTTARI_DASHA(
        titleKey = StringKey.FEATURE_ASHTOTTARI_DASHA,
        descriptionKey = StringKey.FEATURE_ASHTOTTARI_DASHA_DESC,
        icon = Icons.Outlined.Timeline,
        color = AppTheme.AccentGold,
        isImplemented = true
    ),
    SUDARSHANA_CHAKRA(
        titleKey = StringKey.FEATURE_SUDARSHANA_CHAKRA,
        descriptionKey = StringKey.FEATURE_SUDARSHANA_CHAKRA_DESC,
        icon = Icons.Outlined.Sync,
        color = AppTheme.LifeAreaSpiritual,
        isImplemented = true
    ),
    MRITYU_BHAGA(
        titleKey = StringKey.FEATURE_MRITYU_BHAGA,
        descriptionKey = StringKey.FEATURE_MRITYU_BHAGA_DESC,
        icon = Icons.Outlined.BarChart,
        color = AppTheme.WarningColor,
        isImplemented = true
    ),
    LAL_KITAB(
        titleKey = StringKey.FEATURE_LAL_KITAB,
        descriptionKey = StringKey.FEATURE_LAL_KITAB_DESC,
        icon = Icons.Outlined.Spa,
        color = AppTheme.LifeAreaHealth,
        isImplemented = true
    ),
    DIVISIONAL_CHARTS(
        titleKey = StringKey.FEATURE_DIVISIONAL_CHARTS,
        descriptionKey = StringKey.FEATURE_DIVISIONAL_CHARTS_DESC,
        icon = Icons.Outlined.GridView,
        color = AppTheme.AccentTeal,
        isImplemented = true
    ),
    UPACHAYA_TRANSIT(
        titleKey = StringKey.FEATURE_UPACHAYA_TRANSIT,
        descriptionKey = StringKey.FEATURE_UPACHAYA_TRANSIT_DESC,
        icon = Icons.Outlined.Stars,
        color = AppTheme.SuccessColor,
        isImplemented = true
    ),
    KALACHAKRA_DASHA(
        titleKey = StringKey.FEATURE_KALACHAKRA_DASHA,
        descriptionKey = StringKey.FEATURE_KALACHAKRA_DASHA_DESC,
        icon = Icons.Outlined.HealthAndSafety,
        color = AppTheme.LifeAreaSpiritual,
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

@Composable
private fun EmptyHomeState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.PersonAddAlt,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(StringKey.NO_PROFILE_SELECTED),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(StringKey.NO_PROFILE_MESSAGE),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 280.dp)
            )
        }
    }
}
