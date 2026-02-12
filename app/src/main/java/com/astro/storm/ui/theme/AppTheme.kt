package com.astro.storm.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign

// ============================================================================
// NEO-VEDIC APP THEME COLORS
// "Ethereal Vedic Grid" Design Language
// ============================================================================

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

    // Additional Colors
    val InputBackground: Color,
    val DialogBackground: Color,
    val ScrimColor: Color,

    // Is dark theme flag
    val isDark: Boolean
)

// ============================================================================
// DARK THEME - Deep Cosmic Night
// ============================================================================
val DarkAppThemeColors = AppThemeColors(
    // Primary Background Colors
    ScreenBackground = DarkVellum,
    CardBackground = DarkPaper,
    CardBackgroundElevated = DarkPaperHover,
    SurfaceColor = Color(0xFF1E2336),

    // Accent Colors
    AccentPrimary = Color(0xFFB8C4E0),
    AccentSecondary = MarsRedLight,
    AccentGold = VedicGold,
    AccentTeal = Color(0xFF5AA3A3),

    // Text Colors
    TextPrimary = Color(0xFFE4E6ED),
    TextSecondary = Color(0xFFAEB4C2),
    TextMuted = DarkSlateMuted,
    TextSubtle = Color(0xFF5A5E6A),

    // Border and Divider Colors
    BorderColor = DarkBorderSubtle,
    DividerColor = Color(0xFF303550),

    // Interactive Element Colors
    ChipBackground = DarkPaperHover,
    ChipBackgroundSelected = Color(0xFF3A4060),
    ButtonBackground = VedicGold,
    ButtonText = CosmicIndigoDark,

    // Status Colors
    SuccessColor = Color(0xFF86D997),
    WarningColor = VedicGold,
    ErrorColor = MarsRedLight,
    InfoColor = Color(0xFF7EAAE0),

    // Chart-Specific Colors
    ChartBackground = DarkVellum,
    ChartBorder = Color(0xFFB8C4E0),

    // Planet Colors - Traditional Vedic associations
    PlanetSun = Color(0xFFD2691E),
    PlanetMoon = Color(0xFF8C8F96),
    PlanetMars = MarsRedLight,
    PlanetMercury = Color(0xFF5AAF6E),
    PlanetJupiter = VedicGoldLight,
    PlanetVenus = Color(0xFF9370DB),
    PlanetSaturn = Color(0xFF4169E1),
    PlanetRahu = Color(0xFF7A7AAF),
    PlanetKetu = Color(0xFFA08070),

    // Navigation Colors
    NavBarBackground = DarkVellum,
    NavItemSelected = VedicGold,
    NavItemUnselected = DarkSlateMuted,
    NavIndicator = Color(0x1AC5A059),

    // Bottom Sheet Colors
    BottomSheetBackground = DarkPaper,
    BottomSheetHandle = DarkBorderStrong,

    // Prediction Card Colors
    PredictionCardToday = DarkPaper,
    PredictionCardTomorrow = DarkPaperHover,
    PredictionCardWeekly = Color(0xFF283050),

    // Life Area Colors
    LifeAreaCareer = VedicGold,
    LifeAreaLove = MarsRedLight,
    LifeAreaHealth = Color(0xFF86D997),
    LifeAreaGrowth = Color(0xFF7EAAE0),
    LifeAreaFinance = VedicGoldLight,
    LifeAreaSpiritual = Color(0xFF9370DB),

    // Additional Colors
    InputBackground = DarkPaper,
    DialogBackground = DarkPaper,
    ScrimColor = Color(0x80000000),

    // Is dark theme flag
    isDark = true
)

// ============================================================================
// LIGHT THEME - Vellum Parchment
// ============================================================================
val LightAppThemeColors = AppThemeColors(
    // Primary Background Colors
    ScreenBackground = Vellum,
    CardBackground = Color(0xFFF6F3EC),
    CardBackgroundElevated = Color(0xFFFAF8F3),
    SurfaceColor = Vellum,

    // Accent Colors
    AccentPrimary = CosmicIndigo,
    AccentSecondary = MarsRed,
    AccentGold = VedicGold,
    AccentTeal = Color(0xFF3D8888),

    // Text Colors
    TextPrimary = CosmicIndigo,
    TextSecondary = CosmicIndigoLight,
    TextMuted = SlateDark,
    TextSubtle = SlateMuted,

    // Border and Divider Colors
    BorderColor = BorderStrong,
    DividerColor = BorderSubtle,

    // Interactive Element Colors
    ChipBackground = Color(0xFFEDEAE2),
    ChipBackgroundSelected = Color(0xFFD7DEEE),
    ButtonBackground = CosmicIndigo,
    ButtonText = Vellum,

    // Status Colors
    SuccessColor = Color(0xFF2E7D32),
    WarningColor = VedicGoldDark,
    ErrorColor = MarsRed,
    InfoColor = Color(0xFF3670B0),

    // Chart-Specific Colors
    ChartBackground = PressedPaper,
    ChartBorder = CosmicIndigo,

    // Planet Colors - Traditional Vedic (adjusted for light bg)
    PlanetSun = Color(0xFFD2691E),
    PlanetMoon = SlateMuted,
    PlanetMars = MarsRed,
    PlanetMercury = Color(0xFF4A8B5E),
    PlanetJupiter = VedicGold,
    PlanetVenus = Color(0xFF7B68EE),
    PlanetSaturn = Color(0xFF4169E1),
    PlanetRahu = Color(0xFF5C5C8A),
    PlanetKetu = Color(0xFF8B6B5C),

    // Navigation Colors
    NavBarBackground = Vellum,
    NavItemSelected = CosmicIndigo,
    NavItemUnselected = SlateDark,
    NavIndicator = Color(0x33384A74),

    // Bottom Sheet Colors
    BottomSheetBackground = Vellum,
    BottomSheetHandle = BorderStrong,

    // Prediction Card Colors
    PredictionCardToday = PressedPaper,
    PredictionCardTomorrow = PaperHover,
    PredictionCardWeekly = PaperDark,

    // Life Area Colors
    LifeAreaCareer = VedicGoldDark,
    LifeAreaLove = MarsRed,
    LifeAreaHealth = Color(0xFF2E7D32),
    LifeAreaGrowth = Color(0xFF3670B0),
    LifeAreaFinance = VedicGold,
    LifeAreaSpiritual = Color(0xFF7B68EE),

    // Additional Colors
    InputBackground = PressedPaper,
    DialogBackground = Vellum,
    ScrimColor = Color(0x40000000),

    // Is dark theme flag
    isDark = false
)

/**
 * CompositionLocal for accessing theme colors
 */
val LocalAppThemeColors = staticCompositionLocalOf { DarkAppThemeColors }

/**
 * App Theme object for accessing current theme colors
 *
 * IMPORTANT: In Composable functions, use LocalAppThemeColors.current for theme-aware colors.
 * The static getters on this object are DEPRECATED and only kept for backward compatibility.
 * They always return dark theme colors regardless of the actual theme setting.
 *
 * For new code, always use:
 *   val colors = LocalAppThemeColors.current
 *   colors.ScreenBackground // etc.
 */
object AppTheme {
    // Current theme accessor for Composables - USE THIS FOR THEME-AWARE COLORS
    val current: AppThemeColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeColors.current

    // COMPOSABLE THEME-AWARE ACCESSORS
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
     * Neo-Vedic palette: muted, sophisticated tones
     */
    fun getSignColor(sign: ZodiacSign): Color = when (sign) {
        // Fire signs - warm, muted flame
        ZodiacSign.ARIES -> Color(0xFFC26B5A)
        ZodiacSign.LEO -> Color(0xFFD2691E)
        ZodiacSign.SAGITTARIUS -> Color(0xFFBF7840)

        // Earth signs - grounded, organic
        ZodiacSign.TAURUS -> Color(0xFF4A8B5E)
        ZodiacSign.VIRGO -> Color(0xFF7A9E6B)
        ZodiacSign.CAPRICORN -> Color(0xFF8B6B5C)

        // Air signs - cool, cerebral
        ZodiacSign.GEMINI -> Color(0xFF5A8BAF)
        ZodiacSign.LIBRA -> Color(0xFF5C8C8C)
        ZodiacSign.AQUARIUS -> Color(0xFF5C5C8A)

        // Water signs - deep, intuitive
        ZodiacSign.CANCER -> Color(0xFF7B68AE)
        ZodiacSign.SCORPIO -> Color(0xFF8B4573)
        ZodiacSign.PISCES -> Color(0xFF4A7EB5)
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
