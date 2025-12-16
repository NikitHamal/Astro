package com.astro.storm.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    // Primary colors
    primary = PrimaryDark,
    onPrimary = Color(0xFF1A1D35),
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,

    // Secondary colors
    secondary = SecondaryDark,
    onSecondary = Color(0xFF442926),
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,

    // Tertiary colors
    tertiary = TertiaryDark,
    onTertiary = Color(0xFF3D3515),
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,

    // Background & Surface
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,

    // Outline
    outline = OutlineDark,
    outlineVariant = OutlineVariant,

    // Inverse
    inverseSurface = Color(0xFFE4E6ED),
    inverseOnSurface = Color(0xFF2E3138),
    inversePrimary = Color(0xFF4A5ABA),

    // Error
    error = ErrorDark,
    onError = Color(0xFF690005),
    errorContainer = ErrorContainer,
    onErrorContainer = Color(0xFFFFDAD6),

    // Surface tints
    surfaceTint = PrimaryDark,
    scrim = Color(0xFF000000)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6B5D4D),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE7DF),
    onPrimaryContainer = Color(0xFF2C2418),
    secondary = Color(0xFF8B7355),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD4C8B8),
    onSecondaryContainer = Color(0xFF3A2D1E),
    tertiary = Color(0xFFB8860B),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFF0C7),
    onTertiaryContainer = Color(0xFF231B00),
    background = Color(0xFFF5F2ED),
    onBackground = Color(0xFF2C2418),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF2C2418),
    surfaceVariant = Color(0xFFF5F2ED),
    onSurfaceVariant = Color(0xFF5A4D3D)
)

@Composable
fun AstroStormTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val appThemeColors = if (darkTheme) DarkAppThemeColors else LightAppThemeColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = appThemeColors.ScreenBackground.toArgb()
            window.navigationBarColor = appThemeColors.NavBarBackground.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    ProvideAppThemeColors(colors = appThemeColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
