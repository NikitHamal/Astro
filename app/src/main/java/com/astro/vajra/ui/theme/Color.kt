package com.astro.vajra.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================================
// NEO-VEDIC MINIMALIST COLOR SYSTEM
// "Ethereal Vedic Grid" Design Language
// ============================================================================

// Primary Palette - Cosmic Indigo (Ink)
val CosmicIndigo = Color(0xFF1A233A)
val CosmicIndigoLight = Color(0xFF2D3654)
val CosmicIndigoDark = Color(0xFF0F1520)

// Background Palette - Vellum (Parchment Paper)
val Vellum = Color(0xFFF2EFE9)
val PressedPaper = Color(0xFFEBE7DE)
val PaperHover = Color(0xFFE0DCCF)
val PaperDark = Color(0xFFD8D3C8)

// Accent - Vedic Gold
val VedicGold = Color(0xFFC5A059)
val VedicGoldLight = Color(0xFFD4B574)
val VedicGoldDark = Color(0xFFA88840)
val VedicGoldSubtle = Color(0x1AC5A059) // 10% opacity

// Accent Sub - Mars Red (Malefic indicators)
val MarsRed = Color(0xFFB85C5C)
val MarsRedLight = Color(0xFFCF7A7A)
val MarsRedDark = Color(0xFF943E3E)
val MarsRedSubtle = Color(0x0DB85C5C) // 5% opacity

// Muted Text - Slate
val SlateMuted = Color(0xFF747985)
val SlateLight = Color(0xFF8E94A0)
val SlateDark = Color(0xFF5A6070)

// Border Color
val BorderSubtle = Color(0xFFC8C4BA)
val BorderStrong = Color(0xFFA9A598)

// ============================================================================
// DARK MODE PALETTE - Deep Cosmic Night
// ============================================================================
val DarkVellum = Color(0xFF1A1E2E)
val DarkPaper = Color(0xFF232840)
val DarkPaperHover = Color(0xFF2D3352)
val DarkBorderSubtle = Color(0xFF3A3F55)
val DarkBorderStrong = Color(0xFF4A5070)
val DarkSlateMuted = Color(0xFF7A7E8A)

// ============================================================================
// LEGACY COMPAT - Keep old names mapped to new palette
// ============================================================================
val PrimaryDark = CosmicIndigo
val PrimaryLight = CosmicIndigoLight
val PrimaryContainer = Color(0xFF2D3160)
val OnPrimaryContainer = Color(0xFFE0E2FF)

val SecondaryDark = MarsRedLight
val SecondaryLight = Color(0xFFFFDAD6)
val SecondaryContainer = Color(0xFF5C3F3B)
val OnSecondaryContainer = Color(0xFFFFDAD6)

val TertiaryDark = VedicGold
val TertiaryLight = VedicGoldLight
val TertiaryContainer = Color(0xFF4A4225)
val OnTertiaryContainer = Color(0xFFFFF0C7)

val BackgroundDark = DarkVellum
val SurfaceDark = DarkPaper
val SurfaceVariant = DarkPaperHover
val SurfaceElevated = Color(0xFF252B3B)
val SurfaceHighest = Color(0xFF2D3446)

val OnBackgroundDark = Color(0xFFE4E6ED)
val OnSurfaceDark = Color(0xFFE4E6ED)
val OnSurfaceVariant = Color(0xFFAEB4C2)
val TextMuted = SlateMuted
val TextSubtle = SlateDark

val OutlineDark = DarkBorderSubtle
val OutlineVariant = Color(0xFF2A3040)

val ErrorDark = MarsRedLight
val ErrorContainer = Color(0xFF93000A)
val SuccessDark = Color(0xFF86D997)
val SuccessContainer = Color(0xFF1E5C2A)
val WarningDark = VedicGold
val WarningContainer = Color(0xFF5C4D1F)

// Chart-specific colors - Neo-Vedic
val ChartBackground = PressedPaper
val ChartBorder = CosmicIndigo
val ChartHouseLine = CosmicIndigo
val ChartPlanetText = CosmicIndigo
val ChartAscendant = VedicGold
val ChartHouseNumber = SlateMuted
val ChartRetrogradeIndicator = MarsRed

// Planet-specific colors - Traditional Vedic associations
val PlanetSun = Color(0xFFD2691E)     // Warm amber-orange
val PlanetMoon = Color(0xFF8C8F96)    // Silver-grey
val PlanetMars = Color(0xFFB85C5C)    // Mars Red
val PlanetMercury = Color(0xFF4A8B5E) // Forest green
val PlanetJupiter = Color(0xFFC5A059) // Vedic Gold
val PlanetVenus = Color(0xFF9370DB)   // Royal purple
val PlanetSaturn = Color(0xFF4169E1)  // Royal blue
val PlanetRahu = Color(0xFF5C5C8A)    // Smoky indigo
val PlanetKetu = Color(0xFF8B6B5C)    // Earthy brown
