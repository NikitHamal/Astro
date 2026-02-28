package com.astro.vajra.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ============================================================================
// NEO-VEDIC MATERIAL 3 COLOR SCHEMES
// "Ethereal Vedic Grid" Design Language
// ============================================================================

private val DarkColorScheme = darkColorScheme(
    // Primary colors - Cosmic Indigo inverted for dark
    primary = Color(0xFFB8C4E0),
    onPrimary = CosmicIndigoDark,
    primaryContainer = CosmicIndigoLight,
    onPrimaryContainer = Color(0xFFDDE3F0),

    // Secondary colors - Mars Red for malefic/alert
    secondary = MarsRedLight,
    onSecondary = Color(0xFF3A1F1F),
    secondaryContainer = Color(0xFF5C2E2E),
    onSecondaryContainer = Color(0xFFFFDAD6),

    // Tertiary colors - Vedic Gold accent
    tertiary = VedicGoldLight,
    onTertiary = Color(0xFF3D3515),
    tertiaryContainer = Color(0xFF4A4225),
    onTertiaryContainer = Color(0xFFFFF0C7),

    // Background & Surface - Deep Cosmic Night
    background = DarkVellum,
    onBackground = Color(0xFFE4E6ED),
    surface = DarkPaper,
    onSurface = Color(0xFFE4E6ED),
    surfaceVariant = DarkPaperHover,
    onSurfaceVariant = Color(0xFFAEB4C2),

    // Outline
    outline = DarkBorderSubtle,
    outlineVariant = Color(0xFF2A3040),

    // Inverse
    inverseSurface = Vellum,
    inverseOnSurface = CosmicIndigo,
    inversePrimary = CosmicIndigo,

    // Error
    error = MarsRedLight,
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    // Surface tints
    surfaceTint = Color(0xFFB8C4E0),
    scrim = Color(0xFF000000)
)

private val LightColorScheme = lightColorScheme(
    // Primary colors - Cosmic Indigo as ink on parchment
    primary = CosmicIndigo,
    onPrimary = Vellum,
    primaryContainer = Color(0xFFE6EAF3),
    onPrimaryContainer = CosmicIndigo,

    // Secondary colors - Mars Red for accents/alerts
    secondary = MarsRed,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3DEDE),
    onSecondaryContainer = MarsRedDark,

    // Tertiary colors - Vedic Gold
    tertiary = VedicGold,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF6EACF),
    onTertiaryContainer = VedicGoldDark,

    // Background & Surface - Vellum parchment
    background = Vellum,
    onBackground = CosmicIndigo,
    surface = Vellum,
    onSurface = CosmicIndigo,
    surfaceVariant = PressedPaper,
    onSurfaceVariant = CosmicIndigo,

    // Outline - Subtle borders
    outline = BorderStrong,
    outlineVariant = BorderSubtle,

    // Inverse
    inverseSurface = CosmicIndigo,
    inverseOnSurface = Vellum,
    inversePrimary = Color(0xFFB8C4E0),

    // Error
    error = MarsRed,
    onError = Color.White,
    errorContainer = Color(0xFFFCE4E4),
    onErrorContainer = MarsRedDark,

    // Surface tints
    surfaceTint = CosmicIndigo,
    scrim = Color(0x40000000)
)

/**
 * Main theme composable for AstroVajra
 *
 * @param darkTheme Whether to use dark theme. If not specified, follows system preference.
 * @param dynamicColor Whether to use dynamic color (Material You). Not currently used.
 * @param content The content to render with this theme.
 */
@Composable
fun AstroVajraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Select color scheme based on dark/light preference
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Select AppTheme colors based on dark/light preference
    val appThemeColors = if (darkTheme) DarkAppThemeColors else LightAppThemeColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Use appropriate status bar color based on theme
            window.statusBarColor = if (darkTheme) {
                DarkAppThemeColors.ScreenBackground.toArgb()
            } else {
                LightAppThemeColors.ScreenBackground.toArgb()
            }
            window.navigationBarColor = if (darkTheme) {
                DarkAppThemeColors.NavBarBackground.toArgb()
            } else {
                LightAppThemeColors.NavBarBackground.toArgb()
            }
            WindowCompat.getInsetsController(window, view).apply {
                // Light status bar icons for dark theme, dark icons for light theme
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    // Provide both Material theme and custom AppTheme colors
    ProvideAppThemeColors(colors = appThemeColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
