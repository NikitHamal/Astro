# AstroStorm — Neo-Vedic UI Revamp Phases

## Design System: "Ethereal Vedic Grid"

A minimalist, parchment-inspired design language that fuses ancient Vedic aesthetics with modern UI precision.

### Core Principles
- **Sharp, flat, structured**: 2dp corners, 0dp elevation, 1dp borders
- **Parchment palette**: Vellum (#F2EFE9) light / Deep Cosmic (#1A1E2E) dark
- **Three-tier typography**: CinzelDecorative (sacred display) → Poppins (reading) → SpaceGrotesk (data)
- **Compact density**: Tight spacing (4–16dp), information-rich cards

### Key Tokens (`NeoVedicTokens`)
| Token               | Value | Usage                          |
|---------------------|-------|--------------------------------|
| CardCornerRadius    | 2dp   | All card and surface shapes    |
| CardElevation       | 0dp   | Flat cards — no Material shadow|
| BorderWidth         | 1dp   | Subtle ink borders on cards    |
| ScreenPadding       | 16dp  | Horizontal screen gutters      |
| CardSpacing         | 10dp  | Between cards in lists         |
| SectionSpacing      | 24dp  | Between logical sections       |

### Card Pattern
```kotlin
Card(
    shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
    colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
    elevation = CardDefaults.cardElevation(defaultElevation = NeoVedicTokens.CardElevation),
    border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
)
```

### Section Header Pattern
```kotlin
Text(
    text = label.uppercase(),
    fontFamily = SpaceGroteskFamily,
    fontSize = 11.sp,
    fontWeight = FontWeight.Medium,
    letterSpacing = 2.sp,
    color = AppTheme.TextMuted
)
```

### Technical Data Text Pattern
```kotlin
Text(
    text = "27° 14' 32\"",
    fontFamily = SpaceGroteskFamily,
    fontSize = 13.sp,
    color = AppTheme.TextSecondary
)
```

---

## Phase 1 — Structural Consistency (COMPLETED)

**Goal**: Apply the Neo-Vedic card/border/elevation/typography foundation uniformly across all 60+ screens so every screen shares the same visual DNA.

### What Was Done

#### 1. Shared Component Library — `NeoVedicPrimitives.kt`
Enhanced with 12+ new reusable composables:
- `NeoVedicScreen` — Standard screen scaffold with AppTheme background
- `NeoVedicSectionLabel` — Uppercase SpaceGrotesk section headers with tracking
- `NeoVedicDisplayTitle` — CinzelDecorative brand-moment titles
- `NeoVedicCard` — Standard card with border, 0dp elevation, 2dp corners
- `NeoVedicExpandableSection` — Collapsible card with animated expand
- `NeoVedicStrengthBar` — Horizontal progress bar with label + value
- `NeoVedicPlanetBadge` — Planet indicator with Vedic color and icon
- `NeoVedicInfoPair` — Label/value data pair row
- `NeoVedicLoadingState` — Centered loading spinner with text
- `NeoVedicDivider` — Themed divider with subtle border color
- `NeoVedicScoreIndicator` — Circular/linear score visualization
- `NeoVedicAccentLine` — Decorative vedic gold accent line

#### 2. Files Modified (36 files, ~2000 insertions)

**Core Infrastructure:**
- `NeoVedicPrimitives.kt` — 12+ new shared components

**Main Tabs & Entry:**
- `SettingsTab.kt` — CinzelDecorative brand header, section spacing
- `ChartInputScreen.kt` — Full AppTheme migration, NeoVedicTokens shapes/borders

**Chart Analysis System:**
- `ChartAnalysisScreen.kt` — FilterChip labels, font imports
- `CommonComponents.kt` (chartdetail) — SpaceGrotesk section titles
- `ChartTabContent.kt` — SpaceGrotesk for chips, headers, house labels, degrees
- `PlanetsTabContent.kt` — SpaceGrotesk for section headers, degree/longitude text
- `DashasTabContent.kt` — SpaceGrotesk for birth nakshatra, lord labels, dasha periods, dates

**Standalone Analysis Screens:**
- `NakshatraScreen.kt` — BorderStroke on all cards, SpaceGrotesk section headers
- `ShadbalaScreen.kt` — BorderStroke + elevation + section headers
- `ShodashvargaScreen.kt` — CardCornerRadius normalization
- `NativeAnalysisScreen.kt` — 36 fully-qualified references cleaned, Card borders added
- `SynastryScreen.kt` — Imports + border/elevation/typography
- `PredictionsScreen.kt` — Card borders, elevation, SpaceGrotesk
- `DeepNativeAnalysisScreen.kt` — Full Neo-Vedic card treatment
- `DeepPredictionsScreen.kt` — Full Neo-Vedic card treatment
- `RemediesScreen.kt` — 20 references normalized + cards
- `PrashnaScreen.kt` — 29 references normalized + cards
- `VarshaphalaScreen.kt` — 35 references normalized + cards
- `MuhurtaScreen.kt` — 29 references normalized + cards

**Yoga & Specialty Screens (19 screens):**
- `KemadrumaYogaScreen.kt` — BorderStroke on all cards
- `PanchMahapurushaScreen.kt` — BorderStroke on all cards
- `NityaYogaScreen.kt` — BorderStroke on all cards
- `AvasthaScreen.kt` — BorderStroke on all cards
- `MarakaScreen.kt` — BorderStroke on all cards
- `BadhakaScreen.kt` — BorderStroke on all cards
- `VipareetaRajaYogaScreen.kt` — BorderStroke on all cards
- `IshtaKashtaPhalaScreen.kt` — BorderStroke on all cards
- `ShoolaDashaScreen.kt` — BorderStroke on all cards
- `NadiAmshaScreen.kt` — BorderStroke on all cards
- `DashaSandhiScreen.kt` — BorderStroke on all cards
- `KalachakraDashaScreen.kt` — Surface→Card conversion, shadow removal, SpaceGrotesk data/headers
- `MrityuBhagaScreen.kt` — BorderStroke on all cards
- `SarvatobhadraChakraScreen.kt` — BorderStroke on all cards
- `KalaBalaScreen.kt` — BorderStroke on all cards
- `SthanaBalaScreen.kt` — BorderStroke on all cards
- `SahamScreen.kt` — BorderStroke on all cards
- `LalKitabRemediesScreen.kt` — BorderStroke on all cards

**Matchmaking & Remaining:**
- `MatchmakingScreen.kt` — ElementCornerRadius→CardCornerRadius, BorderStroke, CardElevation
- `AshtamangalaPrashnaScreen.kt` — BorderStroke on all cards

#### 3. Screens Already Neo-Vedic Compliant (pre-existing)
These screens were built with or already adopted the design system:
- `HomeTab.kt` — Reference implementation
- `InsightsTab.kt` — Reference implementation
- `ArgalaScreen.kt`, `ArudhaPadaScreen.kt`, `BhriguBinduScreen.kt`
- `DrigBalaScreen.kt`, `DrigDashaScreen.kt`
- `GocharaVedhaScreen.kt`, `GrahaYuddhaScreen.kt`
- `JaiminiKarakaScreen.kt`, `SaptamsaScreen.kt`
- `UpachayaTransitScreen.kt`, `AshtavargaTransitScreen.kt`
- `AshtottariDashaScreen.kt`, `SudarshanaChakraScreen.kt`
- `CharaDashaScreen.kt`, `YoginiDashaScreen.kt`
- `TransitsScreenRedesigned.kt`, `YogasScreenRedesigned.kt`
- `PanchangaScreenRedesigned.kt`, `AshtakavargaScreenRedesigned.kt`
- `DivisionalChartsScreen.kt`, `OnboardingScreen.kt`
- All subdirectory tab contents (AshtottariDashaTabContent, SudarshanaChakraTabContent, etc.)

---

## Phase 2 — Polish, Density & Advanced Patterns (REMAINING)

**Goal**: Refine typography, tighten spacing, add advanced Neo-Vedic interaction patterns, and handle edge cases.

### 2A. Typography Deep Pass
- **Audit every screen** for remaining Poppins in technical data positions → replace with SpaceGrotesk
- **Audit display titles** → ensure CinzelDecorative is used for screen/card headers where brand moments are appropriate
- **CormorantGaramond** integration for decorative quote moments and astrological interpretations
- **Section header pattern** enforcement: verify all section labels use the uppercase 11sp/2sp tracking pattern
- **Font weight audit**: ensure Bold/SemiBold/Medium hierarchy is consistent

### 2B. Spacing & Density Refinement
- **Compact card interiors**: Reduce internal padding from 16dp to 12–14dp where content allows
- **Tighten list spacing**: Audit `Arrangement.spacedBy()` values — many screens use 12–16dp that could be 8–10dp
- **Bottom sheet padding**: Standardize all bottom sheets and dialogs to Neo-Vedic spacing
- **Remove excessive Spacer()** calls — replace with Arrangement.spacedBy or consistent padding

### 2C. Low-Coverage Screens
These files need deeper audit or first-pass application:
- `DashasScreen.kt` — 0 Neo-Vedic references, needs full migration
- `PlanetsScreen.kt` — Only 3 references, needs card/typography treatment
- `BirthChartScreen.kt` — Only 10 references, likely needs card borders
- `TarabalaScreen.kt` (root) — 0 references (note: `tarabala/TarabalaScreen.kt` has 108)
- `KakshaTransitScreen.kt` — Only 26 references, may need typography pass
- `AshtakavargaTabContent.kt` — Only 7 references
- `PanchangaTabContent.kt` — Only 10 references
- `TransitsTabContent.kt` — Only 10 references
- `MainScreen.kt` — Only 14 references (navigation scaffold)
- `DashaSystemsScreen.kt` — Only 35 references

### 2D. Advanced Visual Patterns
- **Ephemeris Transit Timeline**: Integrate the dot-timeline UI pattern from the reference image (parchment background, serif headers, timeline dots with celestial event descriptions) into TransitsScreenRedesigned
- **Vedic Gold accent lines**: Add `NeoVedicAccentLine` decorative dividers between major sections
- **Expandable card animations**: Migrate remaining manual expand/collapse logic to `NeoVedicExpandableSection`
- **Planet badge standardization**: Replace inline planet indicators with `NeoVedicPlanetBadge`
- **Score indicators**: Replace custom progress bars with `NeoVedicScoreIndicator` in Shadbala, KalaBala, SthanaBala, IshtaKashtaPhala

### 2E. Consistency Cleanup
- **Remove `ElementCornerRadius` usage** — any remaining should use `CardCornerRadius` or `ChipCornerRadius`
- **Remove fully-qualified `com.astro.storm.ui.theme.NeoVedicTokens.` references** — use short imports
- **Dark mode audit**: Verify all Neo-Vedic colors resolve correctly in dark theme
- **Remove unused imports** introduced during Phase 1 (e.g., old `Surface`, `shadow` imports)
- **Verify `MatchmakingComponents.kt`** — confirm all component cards have borders

### 2F. Performance & Polish
- **LazyColumn key audit**: Ensure all list items have stable keys for smooth scrolling
- **Recomposition optimization**: Extract stable lambdas and remember expensive computations
- **Loading state standardization**: Replace custom loading UIs with `NeoVedicLoadingState`
- **Error state standardization**: Create and apply a shared Neo-Vedic error state component
- **Empty state standardization**: Create and apply a shared Neo-Vedic empty state component

---

## Architecture Notes

### Theme Resolution Chain
```
AppTheme (CompositionLocal) → LocalAppThemeColors → Color.kt values
                             ↘ ChartDetailColors (bridge for chart tabs)
```

### File Organization
```
ui/
├── components/common/
│   └── NeoVedicPrimitives.kt    ← Shared composables
├── theme/
│   ├── AppTheme.kt              ← 50+ color properties
│   ├── Color.kt                 ← Raw color values
│   ├── DesignTokens.kt          ← NeoVedicTokens spacing/sizing
│   └── Type.kt                  ← Font families + Material3 Typography
└── screen/
    ├── main/                    ← HomeTab, InsightsTab, SettingsTab, MainScreen
    ├── chartdetail/             ← Chart analysis tabs
    ├── matchmaking/             ← Matchmaking flow
    ├── transits/                ← Transit redesign
    ├── yogas/                   ← Yogas redesign
    ├── panchanga/               ← Panchanga redesign
    ├── dasha/                   ← Dasha systems
    ├── ashtakavarga/            ← Ashtakavarga redesign
    └── *.kt                     ← 50+ standalone screens
```

### Key Color Mappings
| Semantic Name      | Light Mode  | Dark Mode   | Usage                    |
|-------------------|-------------|-------------|--------------------------|
| ScreenBackground  | Vellum      | DarkVellum  | All screen backgrounds   |
| CardBackground    | PressedPaper| DarkPaper   | Card container fills     |
| TextPrimary       | CosmicIndigo| OnBackground| Main readable text       |
| TextMuted         | SlateMuted  | DarkSlate   | Labels, metadata         |
| AccentGold        | VedicGold   | VedicGold   | Highlights, active states|
| BorderColor       | BorderSubtle| DarkBorder  | Card borders             |
| AccentRed         | MarsRed     | MarsRedLight| Malefic indicators       |
