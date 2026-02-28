package com.astro.vajra.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.astro.vajra.R

// ============================================================================
// NEO-VEDIC TYPOGRAPHY SYSTEM
// Three-tier hierarchy: Display / Serif / Tech
// ============================================================================

// Cinzel Decorative - Display/Headings Font
// Used for: Screen titles, card headers, the "ASTROSTORM" logotype
val CinzelDecorativeFamily = FontFamily(
    Font(R.font.cinzel_decorative_regular, FontWeight.Normal),
    Font(R.font.cinzel_decorative_bold, FontWeight.Bold)
)

// Cormorant Garamond - Serif Accent Font
// Used for: Optional decorative headings and quote moments only
val CormorantGaramondFamily = FontFamily(
    Font(R.font.cormorant_garamond_regular, FontWeight.Normal),
    Font(R.font.cormorant_garamond_medium, FontWeight.Medium),
    Font(R.font.cormorant_garamond_semibold, FontWeight.SemiBold),
    Font(R.font.cormorant_garamond_bold, FontWeight.Bold),
    Font(R.font.cormorant_garamond_italic, FontWeight.Normal, FontStyle.Italic)
)

// Space Grotesk - Tech/Data Font
// Used for: Degrees, nakshatras, technical labels, navigation, timestamps
val SpaceGroteskFamily = FontFamily(
    Font(R.font.space_grotesk_light, FontWeight.Light),
    Font(R.font.space_grotesk_regular, FontWeight.Normal),
    Font(R.font.space_grotesk_medium, FontWeight.Medium),
    Font(R.font.space_grotesk_semibold, FontWeight.SemiBold),
    Font(R.font.space_grotesk_bold, FontWeight.Bold)
)

// Keep Poppins for backward compatibility in screens not yet fully revamped
val PoppinsFontFamily = FontFamily(
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

// ============================================================================
// MATERIAL 3 TYPOGRAPHY - Readability-First Configuration
// Display: Cinzel Decorative (brand moments only)
// Headline/Title/Body/Label: Poppins (primary UI typeface)
// ============================================================================
val Typography = Typography(
    // Display styles - Cinzel Decorative for dramatic headers
    displayLarge = TextStyle(
        fontFamily = CinzelDecorativeFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = CinzelDecorativeFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.25).sp
    ),
    displaySmall = TextStyle(
        fontFamily = CinzelDecorativeFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),

    // Headline styles - Poppins for better mobile legibility
    headlineLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 27.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    // Title styles - Poppins for card/section titles
    titleLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 23.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    // Body styles - Poppins for all core reading content
    bodyLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 23.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    ),

    // Label styles - Poppins for controls, tabs, chips, and metadata
    labelLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.1.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.1.sp
    )
)

/**
 * Semantic typography aliases used by revamped screens.
 * New UI code should use these aliases (or Material semantic styles)
 * instead of hardcoded `fontSize = *.sp`.
 */
object NeoVedicTypeScale {
    val PageTitle: TextStyle = Typography.displaySmall
    val PageSubtitle: TextStyle = Typography.labelLarge
    val SectionTitle: TextStyle = Typography.titleMedium
    val CardTitle: TextStyle = Typography.titleSmall
    val Body: TextStyle = Typography.bodyMedium
    val BodyCompact: TextStyle = Typography.bodySmall
    val DataValue: TextStyle = Typography.titleSmall.copy(fontFamily = SpaceGroteskFamily)
    val DataLabel: TextStyle = Typography.labelMedium.copy(fontFamily = SpaceGroteskFamily)
    val ChipLabel: TextStyle = Typography.labelMedium
    val TimelineTime: TextStyle = Typography.titleSmall.copy(fontFamily = SpaceGroteskFamily)
}

/**
 * Transitional centralized font-size scale for legacy UI code.
 * Use this instead of inline numeric `.sp` literals in screens.
 */
object NeoVedicFontSizes {
    val S8 = 8.sp
    val S9 = 9.sp
    val S10 = 10.sp
    val S11 = 11.sp
    val S12 = 12.sp
    val S13 = 13.sp
    val S14 = 14.sp
    val S15 = 15.sp
    val S16 = 16.sp
    val S17 = 17.sp
    val S18 = 18.sp
    val S19 = 19.sp
    val S20 = 20.sp
    val S21 = 21.sp
    val S22 = 22.sp
    val S24 = 24.sp
    val S26 = 26.sp
    val S28 = 28.sp
    val S40 = 40.sp
}
