package com.astro.storm.ui.screen.main.uimodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.StringKey
import com.astro.storm.data.localization.StringResources
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