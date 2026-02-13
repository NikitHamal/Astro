# AstroStorm Neo-Vedic UI/UX Revamp Plan

## Overview

This document outlines the comprehensive plan to revamp all 60+ screens in AstroStorm to the new "Neo-Vedic" (Ethereal Vedic Grid) design system. The Home and Insights tabs have already been redesigned and serve as the reference implementation.

## Current State

### Already Redesigned (10+ screens/components):
1. **HomeTab.kt** - Celestial dashboard with hero card, quick actions, snapshot cards
2. **InsightsTab.kt** - Horoscope predictions with life area cards
3. **TransitsScreenRedesigned.kt** - Enhanced Ephemeris UI with planet glyphs (☉, ☽, ♂, etc.), aspect symbols (☌, ☍, △, □, ⚹), and status indicators
4. **YogasScreenRedesigned.kt** - Category filtering, expandable yoga cards
5. **PanchangaScreenRedesigned.kt** - Five limbs with tab navigation
6. **AshtakavargaScreenRedesigned.kt** - Grid-based visualization
7. **MatchmakingScreen.kt** - Modern cards with Guna visualization
8. **ShadbalaScreen.kt** - ModernPillTabRow, Neo-Vedic cards with borders
9. **SthanaBalaScreen.kt** - ModernPillTabRow, Neo-Vedic cards with borders
10. **KalaBalaScreen.kt** - ModernPillTabRow, Neo-Vedic cards with borders

### Design System Components:
- `NeoVedicPrimitives.kt` - Enhanced with:
  - NeoVedicPageHeader - Screen headers with back button
  - NeoVedicTimelineItem - Timeline display items
  - NeoVedicStatusPill - Status indicator pills
  - NeoVedicEmptyState - Empty state component
  - **NEW**: NeoVedicEphemerisDateHeader - Date headers with TODAY/TOMORROW badges
  - **NEW**: NeoVedicEphemerisTransitItem - Enhanced transit items with planet/aspect glyphs
  - **NEW**: NeoVedicStrengthIndicator - Progress bars with strength coloring
  - **NEW**: NeoVedicPlanetCard - Planet display cards with metrics
  - **NEW**: NeoVedicSummaryStatCard - Summary statistics display
  - **NEW**: NeoVedicSectionDivider - Section dividers with optional titles
- `ModernCards.kt` - ModernCard, ExpandableCard, InfoCard, ProgressCard, StatCard, HighlightCard
- `ModernTabRow.kt` - ModernPillTabRow, CompactChipTabs, SegmentedControlTabs
- `EphemerisUiMapper.kt` - Enhanced with:
  - Planet Unicode glyphs (☉, ☽, ♂, ☿, ♃, ♀, ♄, ☊, ☋)
  - Aspect glyphs (☌ conjunction, ☍ opposition, △ trine, □ square, ⚹ sextile)
  - Transit status types (RETROGRADE, DIRECT, EXALTED, DEBILITATED, OWN_SIGN)

## Neo-Vedic Design Language

### Core Principles:
1. **Sharp, Minimal Corners** - 2dp corner radius (parchment-flat aesthetic)
2. **No Elevation/Shadow** - Flat design with hairline borders (1dp)
3. **Vellum Background** - Parchment paper tones (#F2EFE9 light / #1A1E2E dark)
4. **Vedic Gold Accent** - #C5A059 for primary accents and active indicators
5. **Mars Red Secondary** - #B85C5C for alerts and malefic indicators
6. **Scripture-like Typography** - Cinzel Decorative for headings, Poppins for body, Space Grotesk for data

### Design Tokens (NeoVedicTokens):
- ScreenPadding: 16dp
- SectionSpacing: 24dp
- CardSpacing: 10dp
- CardCornerRadius: 2dp
- BorderWidth: 1dp
- ThinBorderWidth: 0.5dp

### Color Palette:
- **Backgrounds**: Vellum (#F2EFE9), DarkVellum (#1A1E2E)
- **Cards**: CardBackground (#F6F3EC), DarkPaper (#232840)
- **Text**: CosmicIndigo (#1A233A), Slate variants
- **Accents**: VedicGold (#C5A059), AccentTeal (#5AA3A3), MarsRed (#B85C5C)
- **Status**: Success (green), Warning (gold), Error (red), Info (blue)

---

## PHASE 1: Foundation & Core Screens (COMPLETED)

### Objectives:
1. ✅ Enhance shared component library with missing Neo-Vedic primitives
2. ✅ Revamp core analysis screens (Bala screens)
3. ✅ Enhance Transits screen with the reference Ephemeris UI design
4. Partially complete - Additional Bala screens need revamp

### Completed Deliverables:

#### 1.1 Shared Components Enhancement
- [x] NeoVedicPrimitives.kt - Added 6 new components
- [x] EphemerisUiMapper.kt - Added planet glyphs, aspect symbols, status types
- [x] Existing ModernCards.kt
- [x] Existing ModernTabRow.kt

#### 1.2 Strength Analysis Screens (Bala)
- [x] ShadbalaScreen.kt - Full Neo-Vedic revamp with ModernPillTabRow
- [x] SthanaBalaScreen.kt - Full Neo-Vedic revamp with ModernPillTabRow
- [x] KalaBalaScreen.kt - Full Neo-Vedic revamp with ModernPillTabRow
- [ ] DrigBalaScreen.kt - Pending (Phase 2)
- [ ] AvasthaScreen.kt - Pending (Phase 2)
- [ ] IshtaKashtaPhalaScreen.kt - Pending (Phase 2)

#### 1.3 Transits Enhancement
- [x] TransitsScreenRedesigned.kt - Enhanced with reference Ephemeris UI
  - ✅ Date grouping with "TODAY", "TOMORROW" labels using NeoVedicEphemerisDateHeader
  - ✅ Timeline dots with connector lines using NeoVedicEphemerisTransitItem
  - ✅ Event type indicators (MOTION REVERSED, DEBILITATED, EXALTED, etc.)
  - ✅ Aspect glyphs (☌, ☍, △, □, ⚹)
  - ✅ Planet symbols with proper Vedic glyphs (☉, ☽, ♂, ☿, ♃, ♀, ♄, ☊, ☋)
  - ✅ Enhanced Positions mode with planet glyph circles
  - ✅ Enhanced Aspects mode with aspect glyph display

---

## PHASE 2: Advanced Analysis & Dashas (Future)

### Objectives:
1. Revamp remaining Bala screens (DrigBala, Avastha, IshtaKashta)
2. Revamp all Dasha system screens
3. Revamp Chakra visualization screens
4. Revamp Yoga analysis screens
5. Revamp Varga/Divisional chart screens
6. Revamp all other screens

### Screens to Revamp:

#### 2.1 Remaining Bala Screens
- DrigBalaScreen.kt
- AvasthaScreen.kt
- IshtaKashtaPhalaScreen.kt

#### 2.2 Dasha Screens
- DashasScreen.kt (wrapper)
- DashaSystemsScreen.kt
- AshtottariDashaScreen.kt
- CharaDashaScreen.kt
- YoginiDashaScreen.kt
- KalachakraDashaScreen.kt
- DrigDashaScreen.kt
- ShoolaDashaScreen.kt
- DashaSandhiScreen.kt

#### 2.3 Chakra Screens
- SudarshanaChakraScreen.kt
- SarvatobhadraChakraScreen.kt

#### 2.4 Yoga Screens
- BhriguBinduScreen.kt
- PanchMahapurushaScreen.kt
- KemadrumaYogaScreen.kt
- NityaYogaScreen.kt
- VipareetaRajaYogaScreen.kt

#### 2.5 Varga Screens
- ShodashvargaScreen.kt
- DivisionalChartsScreen.kt
- SaptamsaScreen.kt
- SahamScreen.kt

#### 2.6 Transit Variants
- AshtavargaTransitScreen.kt
- KakshaTransitScreen.kt
- UpachayaTransitScreen.kt
- GocharaVedhaScreen.kt

#### 2.7 Jaimini & Special
- JaiminiKarakaScreen.kt
- ArudhaPadaScreen.kt
- ArgalaScreen.kt
- GrahaYuddhaScreen.kt

#### 2.8 Prediction Screens
- PredictionsScreen.kt
- DeepPredictionsScreen.kt
- DeepNativeAnalysisScreen.kt
- NativeAnalysisScreen.kt
- VarshaphalaScreen.kt

#### 2.9 Other Screens
- MuhurtaScreen.kt
- PrashnaScreen.kt
- SynastryScreen.kt
- NadiAmshaScreen.kt
- NakshatraScreen.kt
- MrityuBhagaScreen.kt
- AshtamangalaPrashnaScreen.kt
- TarabalaScreen.kt
- MarakaScreen.kt
- BadhakaScreen.kt
- RemediesScreen.kt
- LalKitabRemediesScreen.kt
- ChartInputScreen.kt
- OnboardingScreen.kt
- SettingsTab.kt
- BirthChartScreen.kt
- PlanetsScreen.kt

---

## Reference Image: Ephemeris UI (Implemented)

The reference image showing an elegant transit/ephemeris display has been fully implemented:

1. **Header**: ✅ "EPHEMERIS" title with "LIVE CELESTIAL MOVEMENTS" subtitle and calendar icon
2. **Date Sections**: ✅ Date headers with "TODAY"/"TOMORROW" labels using NeoVedicEphemerisDateHeader
3. **Timeline Items**: ✅ Using NeoVedicEphemerisTransitItem with:
   - Time label (e.g., "09:42 AM", "11:15 AM")
   - Planet glyphs (☉, ☽, ♂, ☿, ♃, ♀, ♄, ☊, ☋)
   - Aspect glyphs (☌, ☍, △, □, ⚹)
   - Event title (e.g., "Moon aspecting Mars", "Mercury Retrograde")
   - Position text (e.g., "SCORPIO", "ORB 2°")
   - Status indicators (MOTION REVERSED, DEBILITATED, EXALTED, etc.)
4. **Visual Elements**: ✅
   - Timeline dots with connector lines
   - Highlighted items with colored accent
   - Status pills for special conditions

---

## Technical Guidelines

### Screen Template (Neo-Vedic):
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenName(
    chart: VedicChart?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current
    val colors = AppTheme.current

    Scaffold(
        containerColor = colors.ScreenBackground,
        topBar = {
            ScreenTopBar(
                title = "SCREEN TITLE",
                subtitle = "SUBTITLE",
                onBack = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(NeoVedicTokens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SectionSpacing)
        ) {
            // Content using design tokens and theme colors
        }
    }
}
```

### Component Usage:
- Use `AppTheme.current` for theme-aware colors
- Use `NeoVedicTokens` for spacing and sizing
- Use `ModernPillTabRow` for tab navigation (replaces FilterChip rows)
- Use `Surface` with `BorderStroke` instead of `Card` for Neo-Vedic flat design
- Use `NeoVedicStatusPill` for status indicators
- Use `NeoVedicEmptyState` for empty states
- Use `NeoVedicEphemerisTransitItem` for timeline displays
- Use `NeoVedicStrengthIndicator` for progress bars

### Tab Selector Pattern:
```kotlin
val tabItems = tabs.mapIndexed { index, title ->
    TabItem(
        title = title,
        accentColor = when (index) {
            0 -> AppTheme.AccentGold
            1 -> AppTheme.AccentTeal
            else -> AppTheme.AccentPrimary
        }
    )
}

ModernPillTabRow(
    tabs = tabItems,
    selectedIndex = selectedTab,
    onTabSelected = onTabSelected,
    modifier = Modifier.padding(horizontal = NeoVedicTokens.ScreenPadding, vertical = NeoVedicTokens.SpaceXS)
)
```

### Card Pattern:
```kotlin
Surface(
    modifier = Modifier.fillMaxWidth(),
    color = AppTheme.CardBackground,
    shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
    border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
) {
    // Card content
}
```

---

## Version History

- **v1.0** (2026-02-13): Initial plan created, Phase 1 started
- **v1.1** (2026-02-13): Phase 1 completed
  - Enhanced NeoVedicPrimitives.kt with 6 new components
  - Enhanced EphemerisUiMapper.kt with planet/aspect glyphs and status types
  - Fully implemented reference Ephemeris UI in TransitsScreenRedesigned.kt
  - Revamped ShadbalaScreen, SthanaBalaScreen, KalaBalaScreen to Neo-Vedic design
  - All screens now use ModernPillTabRow, Surface with BorderStroke, and NeoVedicTokens
