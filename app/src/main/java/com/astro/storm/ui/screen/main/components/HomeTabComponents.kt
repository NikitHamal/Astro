package com.astro.storm.ui.screen.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.astro.storm.data.localization.stringResource
import com.astro.storm.ui.screen.main.uimodel.FeatureCategory
import com.astro.storm.ui.screen.main.uimodel.InsightFeature
import com.astro.storm.ui.theme.AppTheme

@Composable
internal fun CategoryFilterRow(
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
internal fun CategoryChip(
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
        border = BorderStroke(1.dp, borderColor)
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
internal fun QuickAccessSection(
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
internal fun QuickAccessCard(
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
internal fun ExpandableCategorySection(
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
internal fun CategoryHeader(
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
internal fun CompactFeatureGrid(
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
internal fun CompactFeatureItem(
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
internal fun EmptyHomeState(
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