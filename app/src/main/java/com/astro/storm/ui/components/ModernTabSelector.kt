package com.astro.storm.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.ui.theme.AppTheme

/**
 * A modern, reusable tab selector composable.
 *
 * This component provides a consistent and professional-looking tab navigation
 * experience, replacing the inconsistent `FilterChip` implementations used previously.
 * It is theme-aware and animates color changes for a smooth user experience.
 *
 * @param tabs A list of strings representing the tab titles.
 * @param selectedTab The index of the currently selected tab.
 * @param onTabSelected A callback function invoked when a tab is selected.
 */
@Composable
fun ModernTabSelector(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(tabs) { index, title ->
            val isSelected = selectedTab == index

            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) AppTheme.current.AccentPrimary.copy(alpha = 0.15f) else AppTheme.current.ChipBackground,
                animationSpec = tween(durationMillis = 300, easing = LinearEasing),
                label = "tab_background_color"
            )

            val textColor by animateColorAsState(
                targetValue = if (isSelected) AppTheme.current.AccentPrimary else AppTheme.current.TextSecondary,
                animationSpec = tween(durationMillis = 300, easing = LinearEasing),
                label = "tab_text_color"
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor)
                    .clickable { onTabSelected(index) }
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = textColor,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}
