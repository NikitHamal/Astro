package com.astro.storm.ui.screen.chartdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.astro.storm.data.model.Planet
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.LocalAppThemeColors

/**
 * Centralized color palette for ChartDetail screens and components.
 * Now uses theme-aware colors from AppTheme for proper light/dark mode support.
 *
 * All color properties are @Composable to properly read from the current theme.
 */
object ChartDetailColors {
    // Screen backgrounds - theme-aware
    val ScreenBackground: Color
        @Composable @ReadOnlyComposable get() = AppTheme.ScreenBackground

    val SurfaceColor: Color
        @Composable @ReadOnlyComposable get() = AppTheme.SurfaceColor

    val CardBackground: Color
        @Composable @ReadOnlyComposable get() = AppTheme.CardBackground

    val CardBackgroundElevated: Color
        @Composable @ReadOnlyComposable get() = AppTheme.CardBackgroundElevated

    val ChartBackground: Color
        @Composable @ReadOnlyComposable get() = AppTheme.ChartBackground

    // Accent colors - theme-aware
    val AccentGold: Color
        @Composable @ReadOnlyComposable get() = AppTheme.AccentGold

    val AccentTeal: Color
        @Composable @ReadOnlyComposable get() = AppTheme.AccentTeal

    val AccentPurple: Color
        @Composable @ReadOnlyComposable get() = AppTheme.LifeAreaSpiritual

    val AccentRose: Color
        @Composable @ReadOnlyComposable get() = AppTheme.LifeAreaLove

    val AccentBlue: Color
        @Composable @ReadOnlyComposable get() = AppTheme.LifeAreaGrowth

    val AccentGreen: Color
        @Composable @ReadOnlyComposable get() = AppTheme.LifeAreaHealth

    val AccentOrange: Color
        @Composable @ReadOnlyComposable get() = AppTheme.LifeAreaCareer

    // Text colors - theme-aware
    val TextPrimary: Color
        @Composable @ReadOnlyComposable get() = AppTheme.TextPrimary

    val TextSecondary: Color
        @Composable @ReadOnlyComposable get() = AppTheme.TextSecondary

    val TextMuted: Color
        @Composable @ReadOnlyComposable get() = AppTheme.TextMuted

    // Divider and utility - theme-aware
    val DividerColor: Color
        @Composable @ReadOnlyComposable get() = AppTheme.DividerColor

    // Status colors - theme-aware
    val SuccessColor: Color
        @Composable @ReadOnlyComposable get() = AppTheme.SuccessColor

    val WarningColor: Color
        @Composable @ReadOnlyComposable get() = AppTheme.WarningColor

    val ErrorColor: Color
        @Composable @ReadOnlyComposable get() = AppTheme.ErrorColor

    /**
     * Returns the appropriate color for a planet using theme-aware colors.
     */
    @Composable
    @ReadOnlyComposable
    fun getPlanetColor(planet: Planet): Color = AppTheme.getPlanetColor(planet)

    /**
     * Returns color based on strength percentage using theme-aware colors.
     */
    @Composable
    @ReadOnlyComposable
    fun getStrengthColor(percentage: Double): Color = when {
        percentage >= 100 -> AppTheme.SuccessColor
        percentage >= 85 -> AppTheme.LifeAreaCareer  // Orange
        else -> AppTheme.ErrorColor
    }

    /**
     * Returns color based on bindu score using theme-aware colors.
     */
    @Composable
    @ReadOnlyComposable
    fun getBinduColor(bindus: Int): Color = when {
        bindus >= 5 -> AppTheme.SuccessColor
        bindus >= 4 -> AppTheme.AccentTeal
        bindus <= 2 -> AppTheme.ErrorColor
        else -> AppTheme.TextPrimary
    }

    /**
     * Returns color for SAV transit favorability using theme-aware colors.
     */
    @Composable
    @ReadOnlyComposable
    fun getSavFavorableColor(isFavorable: Boolean): Color =
        if (isFavorable) AppTheme.SuccessColor else AppTheme.WarningColor
}
