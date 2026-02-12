package com.astro.storm.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign

/**
 * Unified App Theme Colors with Dark/Light Mode Support
 *
 * Updated for Astrostorm Design System:
 * Primary Background: Vellum (#F2EFE9)
 * Card Background: Paper (#EBE7DE)
 * Primary Accent: Cosmic Indigo (#1A233A)
 * Highlights: Vedic Gold (#C5A059)
 */
data class AppThemeColors(
    // Primary Background Colors
    val ScreenBackground: Color,
    val CardBackground: Color,
    val CardBackgroundElevated: Color,
    val SurfaceColor: Color,

    // Accent Colors
    val AccentPrimary: Color,
    val AccentSecondary: Color,
    val AccentGold: Color,
    val AccentTeal: Color,

    // Text Colors
    val TextPrimary: Color,
    val TextSecondary: Color,
    val TextMuted: Color,
    val TextSubtle: Color,

    // Border and Divider Colors
    val BorderColor: Color,
    val DividerColor: Color,

    // Interactive Element Colors
    val ChipBackground: Color,
    val ChipBackgroundSelected: Color,
    val ButtonBackground: Color,
    val ButtonText: Color,

    // Status Colors
    val SuccessColor: Color,
    val WarningColor: Color,
    val ErrorColor: Color,
    val InfoColor: Color,

    // Chart-Specific Colors
    val ChartBackground: Color,
    val ChartBorder: Color,

    // Planet Colors
    val PlanetSun: Color,
    val PlanetMoon: Color,
    val PlanetMars: Color,
    val PlanetMercury: Color,
    val PlanetJupiter: Color,
    val PlanetVenus: Color,
    val PlanetSaturn: Color,
    val PlanetRahu: Color,
    val PlanetKetu: Color,

    // Navigation Colors
    val NavBarBackground: Color,
    val NavItemSelected: Color,
    val NavItemUnselected: Color,
    val NavIndicator: Color,

    // Bottom Sheet Colors
    val BottomSheetBackground: Color,
    val BottomSheetHandle: Color,

    // Prediction Card Colors
    val PredictionCardToday: Color,
    val PredictionCardTomorrow: Color,
    val PredictionCardWeekly: Color,

    // Life Area Colors
    val LifeAreaCareer: Color,
    val LifeAreaLove: Color,
    val LifeAreaHealth: Color,
    val LifeAreaGrowth: Color,
    val LifeAreaFinance: Color,
    val LifeAreaSpiritual: Color,

    // Additional Colors for Light Theme
    val InputBackground: Color,
    val DialogBackground: Color,
    val ScrimColor: Color,

    // Is dark theme flag
    val isDark: Boolean
)

/**
 * Dark Theme Colors - Kept as fallback
 */
val DarkAppThemeColors = AppThemeColors(
    ScreenBackground = Color(0xFF101522),
    CardBackground = Color(0xFF1A233A),
    CardBackgroundElevated = Color(0xFF242F4D),
    SurfaceColor = Color(0xFF171B24),
    AccentPrimary = Color(0xFF7C8AFF),
    AccentSecondary = Color(0xFFFFB4AB),
    AccentGold = Color(0xFFE5C46C),
    AccentTeal = Color(0xFF86D997),
    TextPrimary = Color(0xFFE4E6ED),
    TextSecondary = Color(0xFFAEB4C2),
    TextMuted = Color(0xFF8A91A0),
    TextSubtle = Color(0xFF5C6270),
    BorderColor = Color(0xFF3D4556),
    DividerColor = Color(0xFF2A3040),
    ChipBackground = Color(0xFF2D3160),
    ChipBackgroundSelected = Color(0xFF3D4556),
    ButtonBackground = Color(0xFF7C8AFF),
    ButtonText = Color(0xFF1A233A),
    SuccessColor = Color(0xFF86D997),
    WarningColor = Color(0xFFFFD666),
    ErrorColor = Color(0xFFFFB4AB),
    InfoColor = Color(0xFF64B5F6),
    ChartBackground = Color(0xFF131720),
    ChartBorder = Color(0xFF7C8AFF),
    PlanetSun = Color(0xFFFFB74D),
    PlanetMoon = Color(0xFFE0E0E0),
    PlanetMars = Color(0xFFFF7043),
    PlanetMercury = Color(0xFF81C784),
    PlanetJupiter = Color(0xFFFFD54F),
    PlanetVenus = Color(0xFFF48FB1),
    PlanetSaturn = Color(0xFF90A4AE),
    PlanetRahu = Color(0xFF7986CB),
    PlanetKetu = Color(0xFFBA68C8),
    NavBarBackground = Color(0xFF171B24),
    NavItemSelected = Color(0xFF7C8AFF),
    NavItemUnselected = Color(0xFF8A91A0),
    NavIndicator = Color(0xFF2D3160),
    BottomSheetBackground = Color(0xFF171B24),
    BottomSheetHandle = Color(0xFF3D4556),
    PredictionCardToday = Color(0xFF1E2330),
    PredictionCardTomorrow = Color(0xFF1E2330),
    PredictionCardWeekly = Color(0xFF1E2330),
    LifeAreaCareer = Color(0xFFFFB74D),
    LifeAreaLove = Color(0xFFFFB4AB),
    LifeAreaHealth = Color(0xFF86D997),
    LifeAreaGrowth = Color(0xFF64B5F6),
    LifeAreaFinance = Color(0xFFFFD54F),
    LifeAreaSpiritual = Color(0xFFBA68C8),
    InputBackground = Color(0xFF1E2330),
    DialogBackground = Color(0xFF171B24),
    ScrimColor = Color(0x80000000),
    isDark = true
)

/**
 * Light Theme Colors - NEW ASTROSTORM PALETTE
 */
val LightAppThemeColors = AppThemeColors(
    ScreenBackground = Vellum,
    CardBackground = Paper,
    CardBackgroundElevated = Color.White,
    SurfaceColor = Vellum,

    AccentPrimary = CosmicIndigo,
    AccentSecondary = MarsRed,
    AccentGold = VedicGold,
    AccentTeal = Color(0xFF4A7C7C),

    TextPrimary = CosmicIndigo,
    TextSecondary = SlateMuted,
    TextMuted = SlateMuted.copy(alpha = 0.7f),
    TextSubtle = CosmicIndigo.copy(alpha = 0.4f),

    BorderColor = BorderSubtle,
    DividerColor = BorderSubtle.copy(alpha = 0.5f),

    ChipBackground = Paper,
    ChipBackgroundSelected = VedicGold.copy(alpha = 0.2f),
    ButtonBackground = CosmicIndigo,
    ButtonText = Vellum,

    SuccessColor = Color(0xFF2E7D32),
    WarningColor = Color(0xFFED6C02),
    ErrorColor = MarsRed,
    InfoColor = Color(0xFF0288D1),

    ChartBackground = Vellum,
    ChartBorder = CosmicIndigo,

    PlanetSun = VedicGold,
    PlanetMoon = SlateMuted,
    PlanetMars = MarsRed,
    PlanetMercury = CosmicIndigo,
    PlanetJupiter = VedicGold,
    PlanetVenus = Color(0xFF9370DB),
    PlanetSaturn = CosmicIndigo,
    PlanetRahu = MarsRed,
    PlanetKetu = MarsRed,

    NavBarBackground = Vellum,
    NavItemSelected = CosmicIndigo,
    NavItemUnselected = SlateMuted,
    NavIndicator = VedicGold.copy(alpha = 0.1f),

    BottomSheetBackground = Vellum,
    BottomSheetHandle = BorderSubtle,

    PredictionCardToday = Paper,
    PredictionCardTomorrow = Vellum,
    PredictionCardWeekly = Paper,

    LifeAreaCareer = VedicGold,
    LifeAreaLove = MarsRed,
    LifeAreaHealth = Color(0xFF2E7D32),
    LifeAreaGrowth = CosmicIndigo,
    LifeAreaFinance = VedicGold,
    LifeAreaSpiritual = CosmicIndigo,

    InputBackground = Paper,
    DialogBackground = Vellum,
    ScrimColor = Color(0x40000000),

    isDark = false
)

/**
 * CompositionLocal for accessing theme colors
 */
val LocalAppThemeColors = staticCompositionLocalOf { LightAppThemeColors }

/**
 * App Theme object for accessing current theme colors
 */
object AppTheme {
    val current: AppThemeColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current

    val ScreenBackground: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.ScreenBackground

    val CardBackground: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.CardBackground

    val CardBackgroundElevated: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.CardBackgroundElevated

    val SurfaceColor: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.SurfaceColor

    val AccentPrimary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.AccentPrimary

    val AccentSecondary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.AccentSecondary

    val AccentGold: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.AccentGold

    val AccentTeal: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.AccentTeal

    val TextPrimary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.TextPrimary

    val TextSecondary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.TextSecondary

    val TextMuted: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.TextMuted

    val TextSubtle: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.TextSubtle

    val BorderColor: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.BorderColor

    val DividerColor: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.DividerColor

    val ChipBackground: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.ChipBackground

    val ChipBackgroundSelected: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.ChipBackgroundSelected

    val ButtonBackground: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.ButtonBackground

    val ButtonText: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.ButtonText

    val SuccessColor: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.SuccessColor

    val WarningColor: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.WarningColor

    val ErrorColor: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.ErrorColor

    val InfoColor: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.InfoColor

    val ChartBackground: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.ChartBackground

    val ChartBorder: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.ChartBorder

    val PlanetSun: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.PlanetSun

    val PlanetMoon: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.PlanetMoon

    val PlanetMars: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.PlanetMars

    val PlanetMercury: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.PlanetMercury

    val PlanetJupiter: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.PlanetJupiter

    val PlanetVenus: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.PlanetVenus

    val PlanetSaturn: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.PlanetSaturn

    val PlanetRahu: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.PlanetRahu

    val PlanetKetu: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.PlanetKetu

    val NavBarBackground: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.NavBarBackground

    val NavItemSelected: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.NavItemSelected

    val NavItemUnselected: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.NavItemUnselected

    val NavIndicator: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.NavIndicator

    val BottomSheetBackground: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.BottomSheetBackground

    val BottomSheetHandle: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.BottomSheetHandle

    val PredictionCardToday: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.PredictionCardToday

    val PredictionCardTomorrow: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.PredictionCardTomorrow

    val PredictionCardWeekly: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.PredictionCardWeekly

    val LifeAreaCareer: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.LifeAreaCareer

    val LifeAreaLove: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.LifeAreaLove

    val LifeAreaHealth: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.LifeAreaHealth

    val LifeAreaGrowth: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.LifeAreaGrowth

    val LifeAreaFinance: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.LifeAreaFinance

    val LifeAreaSpiritual: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.LifeAreaSpiritual

    // Alias for backward compatibility
    val CardElevated: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current.CardBackgroundElevated

    /**
     * Get color for a specific planet - Theme-aware composable function
     */
    @Composable
    @ReadOnlyComposable
    fun getPlanetColor(planet: Planet): Color = when (planet) {
        Planet.SUN -> LocalAppThemeColors.current.PlanetSun
        Planet.MOON -> LocalAppThemeColors.current.PlanetMoon
        Planet.MARS -> LocalAppThemeColors.current.PlanetMars
        Planet.MERCURY -> LocalAppThemeColors.current.PlanetMercury
        Planet.JUPITER -> LocalAppThemeColors.current.PlanetJupiter
        Planet.VENUS -> LocalAppThemeColors.current.PlanetVenus
        Planet.SATURN -> LocalAppThemeColors.current.PlanetSaturn
        Planet.RAHU -> LocalAppThemeColors.current.PlanetRahu
        Planet.KETU -> LocalAppThemeColors.current.PlanetKetu
        else -> LocalAppThemeColors.current.AccentGold
    }

    /**
     * Get color for a specific zodiac sign based on its element
     */
    fun getSignColor(sign: ZodiacSign): Color = when (sign) {
        // Fire signs - energetic, warm tones
        ZodiacSign.ARIES -> Color(0xFFE53935)
        ZodiacSign.LEO -> Color(0xFFF57C00)
        ZodiacSign.SAGITTARIUS -> Color(0xFFFF7043)

        // Earth signs - stable, grounded tones
        ZodiacSign.TAURUS -> Color(0xFF43A047)
        ZodiacSign.VIRGO -> Color(0xFF8BC34A)
        ZodiacSign.CAPRICORN -> Color(0xFF795548)

        // Air signs - intellectual, light tones
        ZodiacSign.GEMINI -> Color(0xFF29B6F6)
        ZodiacSign.LIBRA -> Color(0xFF26C6DA)
        ZodiacSign.AQUARIUS -> Color(0xFF5C6BC0)

        // Water signs - emotional, deep tones
        ZodiacSign.CANCER -> Color(0xFF9575CD)
        ZodiacSign.SCORPIO -> Color(0xFF7E57C2)
        ZodiacSign.PISCES -> Color(0xFF42A5F5)
    }
}

/**
 * Provider composable for theme colors
 */
@Composable
fun ProvideAppThemeColors(
    colors: AppThemeColors,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalAppThemeColors provides colors) {
        content()
    }
}
