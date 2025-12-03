package com.astro.storm.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Unified App Theme Colors
 *
 * These colors are derived from the ChartInputScreen's dark brown theme
 * and will be used consistently throughout the app for a cohesive look.
 */
object AppTheme {
    // Primary Background Colors
    val ScreenBackground = Color(0xFF1C1410)
    val CardBackground = Color(0xFF2A201A)
    val CardBackgroundElevated = Color(0xFF352A22)
    val SurfaceColor = Color(0xFF241C16)

    // Accent Colors
    val AccentPrimary = Color(0xFFB8A99A)  // Warm beige - primary accent
    val AccentSecondary = Color(0xFF8B7355)  // Darker brown accent
    val AccentGold = Color(0xFFD4AF37)  // Gold for highlights
    val AccentTeal = Color(0xFF4DB6AC)  // Teal for secondary highlights

    // Text Colors
    val TextPrimary = Color(0xFFE8DFD6)
    val TextSecondary = Color(0xFFB8A99A)
    val TextMuted = Color(0xFF8A7A6A)
    val TextSubtle = Color(0xFF6A5A4A)

    // Border and Divider Colors
    val BorderColor = Color(0xFF4A3F38)
    val DividerColor = Color(0xFF3A302A)

    // Interactive Element Colors
    val ChipBackground = Color(0xFF3D322B)
    val ChipBackgroundSelected = Color(0xFF4A3F38)
    val ButtonBackground = Color(0xFFB8A99A)
    val ButtonText = Color(0xFF1C1410)

    // Status Colors
    val SuccessColor = Color(0xFF81C784)
    val WarningColor = Color(0xFFFFB74D)
    val ErrorColor = Color(0xFFCF6679)
    val InfoColor = Color(0xFF64B5F6)

    // Chart-Specific Colors
    val ChartBackground = Color(0xFF1A1512)
    val ChartBorder = Color(0xFFB8A99A)

    // Planet Colors
    val PlanetSun = Color(0xFFD2691E)
    val PlanetMoon = Color(0xFFDC143C)
    val PlanetMars = Color(0xFFDC143C)
    val PlanetMercury = Color(0xFF228B22)
    val PlanetJupiter = Color(0xFFDAA520)
    val PlanetVenus = Color(0xFF9370DB)
    val PlanetSaturn = Color(0xFF4169E1)
    val PlanetRahu = Color(0xFF8B0000)
    val PlanetKetu = Color(0xFF8B0000)

    // Navigation Colors
    val NavBarBackground = Color(0xFF241C16)
    val NavItemSelected = Color(0xFFB8A99A)
    val NavItemUnselected = Color(0xFF6A5A4A)
    val NavIndicator = Color(0xFF3D322B)

    // Bottom Sheet Colors
    val BottomSheetBackground = Color(0xFF2A201A)
    val BottomSheetHandle = Color(0xFF4A3F38)

    // Prediction Card Colors
    val PredictionCardToday = Color(0xFF2D2520)
    val PredictionCardTomorrow = Color(0xFF2A2520)
    val PredictionCardWeekly = Color(0xFF282520)

    // Life Area Colors
    val LifeAreaCareer = Color(0xFFFFB74D)
    val LifeAreaLove = Color(0xFFE57373)
    val LifeAreaHealth = Color(0xFF81C784)
    val LifeAreaGrowth = Color(0xFF64B5F6)
    val LifeAreaFinance = Color(0xFFFFD54F)
    val LifeAreaSpiritual = Color(0xFFBA68C8)
}
