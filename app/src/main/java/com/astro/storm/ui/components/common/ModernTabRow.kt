package com.astro.storm.ui.components.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.NeoVedicTokens

/**
 * Tab data class for modern tab row
 */
data class TabItem(
    val title: String,
    val icon: ImageVector? = null,
    val badge: String? = null,
    val accentColor: Color = Color.Unspecified
)

/**
 * Modern chip-based tab row with smooth animations.
 * Replaces the old pill-container style with individual chips matching the Shadbala screen style.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernPillTabRow(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Transparent, // Ignored in new style
    contentPadding: Dp = 0.dp // Kept for source compatibility; ignored in new style
) {
    val listState = rememberLazyListState()

    LaunchedEffect(selectedIndex) {
        if (selectedIndex >= 0 && selectedIndex < tabs.size) {
            listState.animateScrollToItem(
                index = selectedIndex,
                scrollOffset = -100
            )
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor),
        horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.PillGap),
        contentPadding = PaddingValues(horizontal = NeoVedicTokens.PillHorizontalPadding + contentPadding)
    ) {
        itemsIndexed(tabs) { index, tab ->
            val isSelected = index == selectedIndex
            val accentColor = if (tab.accentColor == Color.Unspecified) {
                AppTheme.AccentPrimary
            } else {
                tab.accentColor
            }
            FilterChip(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                label = {
                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                leadingIcon = tab.icon?.let { icon ->
                    {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(NeoVedicTokens.SpaceMD + NeoVedicTokens.SpaceXXS)
                        )
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = accentColor.copy(alpha = 0.15f),
                    selectedLabelColor = accentColor,
                    containerColor = AppTheme.ChipBackground,
                    labelColor = AppTheme.TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = Color.Transparent,
                    selectedBorderColor = accentColor.copy(alpha = 0.2f),
                    borderWidth = NeoVedicTokens.BorderWidth,
                    selectedBorderWidth = NeoVedicTokens.BorderWidth
                ),
                shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius)
            )
        }
    }
}

/**
 * Modern scrollable tab row using chip style.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernScrollableTabRow(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Transparent, // Ignored
    indicatorColor: Color = AppTheme.AccentPrimary // Ignored
) {
    // Use the same implementation as ModernPillTabRow as it's already scrollable
    ModernPillTabRow(
        tabs = tabs,
        selectedIndex = selectedIndex,
        onTabSelected = onTabSelected,
        modifier = modifier
    )
}

/**
 * Compact chip-style tab selector for dense layouts
 */
@Composable
fun CompactChipTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = AppTheme.AccentPrimary
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = NeoVedicTokens.SpaceMD),
        horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.PillGap)
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = index == selectedIndex
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) accentColor.copy(alpha = 0.15f)
                else AppTheme.ChipBackground,
                animationSpec = tween(200),
                label = "chipBg"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) accentColor else AppTheme.TextMuted,
                animationSpec = tween(200),
                label = "chipText"
            )

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(NeoVedicTokens.ChipCornerRadius))
                    .clickable { onTabSelected(index) },
                color = backgroundColor,
                shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius)
            ) {
                Box(
                    modifier = Modifier.padding(vertical = NeoVedicTokens.PillVerticalPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = textColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * Segmented control style tabs
 */
@Composable
fun SegmentedControlTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = AppTheme.AccentPrimary
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = NeoVedicTokens.SpaceMD),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(NeoVedicTokens.SpaceXXS)
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = index == selectedIndex
                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) accentColor else Color.Transparent,
                    animationSpec = tween(200),
                    label = "segmentBg"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else AppTheme.TextMuted,
                    animationSpec = tween(200),
                    label = "segmentText"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(NeoVedicTokens.ChipCornerRadius))
                        .background(backgroundColor)
                        .clickable { onTabSelected(index) }
                        .padding(vertical = NeoVedicTokens.PillVerticalPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = textColor
                    )
                }
            }
        }
    }
}
