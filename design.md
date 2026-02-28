# AstroVajra Neo-Vedic Design System Report

## Document intent and scope

This document captures the current Neo-Vedic design system implementation in AstroVajra, with a special focus on the primary user-facing flows you mentioned:

- Home tab
- Insights tab
- Birth Chart screen
- Yogas
- Predictions
- Matchmaking
- And broader adoption across other analysis screens

It is written as an implementation report (what exists now), not just a guideline.

---

## Core design language

The design language is explicitly named in code comments as:

- `Ethereal Vedic Grid`
- `Neo-Vedic Minimalist`

The visual identity aims for:

- Parchment-like backgrounds (vellum/paper tones)
- Ink-like text/accent primaries (cosmic indigo)
- Gold accents for sacred/highlight meaning
- Flat surfaces with very low/no elevation
- Sharp geometry (2dp corner radii in tokens)
- Hairline borders and divider lines
- Data-forward typography and glyph-heavy cards

Primary source files:

- `app/src/main/java/com/astro/vajra/ui/theme/Color.kt`
- `app/src/main/java/com/astro/vajra/ui/theme/AppTheme.kt`
- `app/src/main/java/com/astro/vajra/ui/theme/Theme.kt`
- `app/src/main/java/com/astro/vajra/ui/theme/Type.kt`
- `app/src/main/java/com/astro/vajra/ui/theme/DesignTokens.kt`

---

## Token system

### 1) Color tokens (raw palette)

From `Color.kt`:

- Cosmic indigo family
  - `CosmicIndigo` `#1A233A`
  - `CosmicIndigoLight` `#2D3654`
  - `CosmicIndigoDark` `#0F1520`
- Parchment family
  - `Vellum` `#F2EFE9`
  - `PressedPaper` `#EBE7DE`
  - `PaperHover` `#E0DCCF`
  - `PaperDark` `#D8D3C8`
- Gold family
  - `VedicGold` `#C5A059`
  - `VedicGoldLight` `#D4B574`
  - `VedicGoldDark` `#A88840`
  - `VedicGoldSubtle` (10% alpha)
- Mars red family
  - `MarsRed` `#B85C5C`
  - `MarsRedLight` `#CF7A7A`
  - `MarsRedDark` `#943E3E`
  - `MarsRedSubtle` (5% alpha)
- Slate text family
  - `SlateMuted` `#747985`
  - `SlateLight` `#8E94A0`
  - `SlateDark` `#5A6070`
- Border family
  - `BorderSubtle` `#C8C4BA`
  - `BorderStrong` `#A9A598`

Dark counterparts:

- `DarkVellum`, `DarkPaper`, `DarkPaperHover`
- `DarkBorderSubtle`, `DarkBorderStrong`
- `DarkSlateMuted`

### 2) Semantic theme colors

`AppThemeColors` in `AppTheme.kt` defines semantic color slots for:

- surfaces/backgrounds
- accents
- text tiers
- borders/dividers
- chips/buttons
- status states
- chart colors
- planet colors
- nav colors
- bottom-sheet colors
- prediction card colors
- life area colors
- utility surfaces (`InputBackground`, `DialogBackground`, `ScrimColor`)

Two concrete sets:

- `LightAppThemeColors`
- `DarkAppThemeColors`

Theme access pattern:

- Composition-local: `LocalAppThemeColors`
- App accessor object: `AppTheme.current` and property aliases

### 3) Material3 bridge

`Theme.kt` maps Neo-Vedic palette to Material3 `lightColorScheme` and `darkColorScheme`.

Also updates system bars at runtime:

- status bar color from theme screen background
- nav bar color from theme nav background
- icon luminance toggled by dark/light

Applied from app root in `MainActivity.kt` via `AstroVajraTheme(darkTheme = useDarkTheme)`.

### 4) Spacing, shape, border, elevation tokens

From `DesignTokens.kt` (`NeoVedicTokens`):

- spacing: `4, 8, 10, 14, 16, 20, 24dp`
- card/chip geometry: `2dp` corners for card/element/chip
- borders: `1dp` and `0.5dp`
- elevation: `0dp` for card and surfaces (intentional flatness)
- top bar paddings/icon size
- content section inset defaults
- dense row paddings for data/table-like UIs
- timeline connector and list-item sizing

### 5) Typography tokens

From `Type.kt`:

- Display font: `CinzelDecorativeFamily`
- Serif accent font: `CormorantGaramondFamily`
- Data/tech font: `SpaceGroteskFamily`
- Transitional primary UI font: `PoppinsFontFamily`

Material Typography setup:

- display styles use Cinzel
- headline/title/body/label currently use Poppins for readability and migration safety

Additional aliases:

- `NeoVedicTypeScale` semantic text aliases
- `NeoVedicFontSizes` centralized `sp` constants used throughout revamped screens

Important migration note embedded in code:

- Poppins is intentionally kept for backward compatibility while revamp continues.

---

## Primitive component layer

Main primitive kit lives in `NeoVedicPrimitives.kt`.

### Implemented primitives

- `NeoVedicPageHeader`
  - full-width flat header
  - optional back/action
  - title + optional subtitle/subtitle slot
  - border and status bar padding
- `NeoVedicStatusPill`
- `NeoVedicTimelineSectionHeader`
- `NeoVedicStatRow`
- `NeoVedicTimelineItem`
- `NeoVedicEphemerisDateHeader`
- `NeoVedicEphemerisTransitItem`
- `NeoVedicStrengthIndicator`
- `NeoVedicPlanetCard`
- `NeoVedicSummaryStatCard`
- `NeoVedicSectionDivider`
- `NeoVedicEmptyState`
- `Modifier.vedicCornerMarkers(...)`

These are the key shared abstractions that make the style reusable across many feature screens.

### Tab primitives

From `ModernTabRow.kt`:

- `ModernPillTabRow` (chip-based tab navigation)
- `ModernScrollableTabRow`
- `CompactChipTabs`
- `SegmentedControlTabs`
- wrapper aliases (`NeoVedicFeatureTabs`, `NeoVedicChoicePill`, `NeoVedicNavItem`)

Visual behavior:

- selected tab uses tinted accent background
- unselected uses chip background
- smooth color transitions and auto-scroll to selected tab

---

## App shell and navigation styling

From `MainScreen.kt`:

- Top shell (`NeoVedicTopBar`)
  - flat parchment surface
  - ASTROSTORM wordmark in Cinzel
  - hairline bottom border
  - profile switcher on right
- Bottom nav (`NeoVedicBottomNavigation`)
  - hairline top border
  - uppercase Space Grotesk labels
  - tinted selected indicator (`NavIndicator`)
  - no heavy elevation
- Content transitions
  - tab-level `Crossfade`
- Export feedback
  - themed snackbar colors mapped to success/error/exporting state

---

## Screen-level implementation report

## Home tab (major reference implementation)

File: `app/src/main/java/com/astro/vajra/ui/screen/main/HomeTab.kt`

High-level structure:

- Hero dasha card
- 2x2 quick action bento grid
- Today snapshot strip (Panchanga + Transits)
- Category cards for many feature clusters

Notable design details:

- Dedicated `HomeDesignTokens` layered on top of `NeoVedicTokens`
- L-shaped corner marker motif (local modifier implementation)
- Strong typography hierarchy:
  - uppercase Space Grotesk micro-labels
  - Cinzel for ritual/primary headings
  - Poppins for readable body copy
- Flat surfaces, sharp corners, hairline borders
- Accent color per feature category and per planet
- Motion:
  - expanding categories (`AnimatedVisibility`)
  - icon rotation for expand state
  - animated content sizing
- Empty state includes geometric icon container and outlined CTA

System significance:

- Home is one of the clearest demonstrations of the Neo-Vedic language in one place.

## Insights tab (second major reference implementation)

File: `app/src/main/java/com/astro/vajra/ui/screen/main/InsightsTab.kt`

Architecture and states:

- Explicit state machine handling:
  - loading skeleton
  - error screen
  - partial error banner
  - success content
  - empty state
- Time-period segmented model:
  - Today, Tomorrow, Weekly

Visual implementation highlights:

- Same flat, bordered card language
- Uppercase Space Grotesk section headers with letter spacing
- Cinzel used for major card headlines and themed moments
- Pull-quote style cards with vertical accent stripe
- Data visualizations:
  - energy dot grid
  - dasha progress bars
  - weekly energy bars
  - transit strength dot clusters
- Expanded cards with animated expand/collapse
- Strong semantic color coding:
  - success/warning/error/planet/life-area tokens

System significance:

- Insights operationalizes the design system for dense, mixed-content analytics.

## Birth Chart screen

Entry: `app/src/main/java/com/astro/vajra/ui/screen/BirthChartScreen.kt`

Delegated main content:

- `ChartTabContent` in `app/src/main/java/com/astro/vajra/ui/screen/chartdetail/tabs/ChartTabContent.kt`

Design behavior:

- Uses `NeoVedicPageHeader`
- Uses `NeoVedicEmptyState` for null chart case
- Chart detail cards are flat, sharp, bordered, expandable
- Divisional chart selector implemented as chip pills
- Uses chart-specific theme adapter (`ChartDetailColors`) to remain theme-aware
- Includes iconography + data tables for planetary/house/birth/astronomical info
- Full-screen chart modal and detail dialogs preserve same color intent

Implementation note:

- Chart details layer reuses some distinct accent aliases (e.g., `AccentPurple`) that map to semantic theme colors.

## Yogas screen

File: `app/src/main/java/com/astro/vajra/ui/screen/yogas/YogasScreenRedesigned.kt`

Design characteristics:

- Uses `NeoVedicPageHeader`
- Summary card with corner markers and gradient accent wash
- Category filter chips with selected-state tinting
- Expandable yoga cards with:
  - category badges
  - strength badges and progress bars
  - planet chips and house data
  - effects and optional deep-analysis summary
- Strong usage of category-color semantics and auspicious indicators
- Empty state via `NeoVedicEmptyState`

System significance:

- Good example of balancing decorative sacred cues with information density.

## Predictions screen

File: `app/src/main/java/com/astro/vajra/ui/screen/PredictionsScreen.kt`

Interaction model:

- Header + tabbed sections:
  - Overview
  - Life Areas
  - Timing
  - Remedies
- Tab transitions via `AnimatedContent` slide directions

Design implementation:

- Uses `ModernPillTabRow`
- Broad usage of flat card system with semantic accent per content type
- Overview card modules:
  - life path
  - current period
  - active yogas summary
  - challenges/opportunities
  - quick life area ratings
- Timing modules:
  - favorable periods
  - caution periods
  - important dates
- Remedies module with numbered treatment items
- Uses `NeoVedicEmptyState` for no chart and themed loading/error states

Data implementation note:

- Prediction content includes deterministic templates plus some randomized placeholders in current implementation (e.g., mock-style strength/impact values), but visual system is consistent.

## Matchmaking screen

File: `app/src/main/java/com/astro/vajra/ui/screen/matchmaking/MatchmakingScreen.kt`
Support components: `app/src/main/java/com/astro/vajra/ui/screen/matchmaking/MatchmakingComponents.kt`

Design characteristics:

- Uses `NeoVedicPageHeader`
- Profile selection module with dual cards and heart/connection indicator
- Circular compatibility score hero with animated progress ring
- Horizontal quick insight chips
- Pill-tabbed detail sections:
  - overview
  - gunas
  - doshas
  - nakshatras
  - remedies
- Heavy use of semantic status badges and bordered cards
- Bottom sheet profile selector styled to same palette and shape system
- Includes disclaimers/info panels in low-alpha status tints

Interaction details:

- Haptics on key actions
- Animated scale/rotation/progress/visibility transitions

Implementation nuance:

- Some helper colors in `MatchmakingComponents.kt` use legacy constants (`SuccessDark`, `MarsRed`) directly, while most UI uses `AppTheme` semantic colors.

---

## Broader adoption across "and so on" screens

The design system is not limited to the six areas above.

Observed from code search:

- `NeoVedicPageHeader` appears in 58 locations across many feature screens.
- `ModernPillTabRow` appears in 33 locations.
- `vedicCornerMarkers` is used widely across dasha, varga, transit, nakshatra, yoga, predictions, matchmaking, and home surfaces.

Representative additional screen families already using Neo-Vedic patterns:

- Dasha suite (`Vimsottari`, `Yogini`, `Chara`, `Ashtottari`, `Kalachakra`, `Shoola`, `Drig`, `DashaSandhi`)
- Divisional and varga suites (`DivisionalCharts`, `Shodashvarga`, `Ashtakavarga`)
- Transit and timing suites (`GocharaVedha`, `AshtavargaTransit`, `KakshaTransit`, `UpachayaTransit`)
- Advanced analysis (`Argala`, `BhriguBindu`, `JaiminiKaraka`, `Saptamsa`, `Avastha`, `Maraka`, `Badhaka`, `IshtaKashtaPhala`, etc.)

This confirms a broad ongoing rollout, not just isolated redesigns.

---

## Repeating visual grammar (cross-screen)

Across the implemented screens, the same patterns repeat:

- flat `Surface` + border instead of elevated cards
- very tight radius geometry (mostly 2dp)
- uppercase micro-labels in Space Grotesk for metadata
- headline moments in Cinzel
- body in Poppins for readability
- low-alpha accent wash backgrounds for semantic grouping
- thin progress bars and compact status pills
- animated expansion for detailed analytics blocks
- empty/loading/error states styled as part of system, not generic defaults

---

## Localization and formatting behavior in UI style

The design system is localization-aware:

- string keys throughout (English + Nepali)
- locale-sensitive date formatters in Home, Insights, Chart tabs, Predictions
- Nepali numeral conversion in multiple places
- localized planet/sign/nakshatra labels

This means typography and spacing choices are intentionally flexible enough for bilingual content density.

---

## Motion and interaction behavior

Common motion patterns observed:

- card expand/collapse (`AnimatedVisibility`, `animateContentSize`)
- progress and indicator animations (`animateFloatAsState`, `animateIntAsState`)
- tab transitions (`AnimatedContent`, `Crossfade`)
- skeleton shimmer for loading

Interaction patterns:

- clear visual selected states in chips/tabs
- haptic feedback on important navigation/actions (notably Matchmaking and Birth Chart back action)
- icon rotation cues for disclosure state

---

## Current implementation strengths

1. Strong semantic token architecture (`AppThemeColors` + `NeoVedicTokens`).
2. Good primitive layer (`NeoVedicPrimitives.kt`) with reusable building blocks.
3. High adoption in major and advanced screens.
4. Consistent flat/sacred-grid identity across dense analytical UI.
5. Localization-aware rendering integrated into visual components.

---

## Current implementation gaps and inconsistencies

These are practical observations from current code, useful for your ongoing implementation work:

1. Duplicate corner-marker implementation exists:
   - local in `HomeTab.kt`
   - shared in `NeoVedicPrimitives.kt`
   - suggest unifying on shared modifier to avoid drift.

2. Typography migration is still hybrid:
   - comments describe three-tier font system,
   - but Material `headline/title/body/label` mostly use Poppins for now.

3. Color access is mostly semantic but not fully uniform:
   - some modules still use legacy static constants directly (`MatchmakingComponents.kt`),
   - while most screens use `AppTheme`/`LocalAppThemeColors`.

4. Spacing values are mostly tokenized but still include hardcoded dp/sp in several feature files.

5. Chart detail layer has its own color adapter (`ChartDetailColors`) which is good for isolation, but it is a parallel abstraction that can diverge from base semantics if not actively governed.

---

## Suggested next phase for full system completion

If you want this design system to become fully locked and scalable, prioritize:

1. Consolidate all decorative primitives (especially corner markers) into one shared source.
2. Finish typography migration toward semantic aliases (`NeoVedicTypeScale`) and reduce ad-hoc `fontSize` calls.
3. Remove remaining direct legacy color usage in favor of semantic `AppTheme` slots.
4. Enforce token-only spacing/radius/border via lint/convention checks.
5. Build a dedicated design-system showcase screen that renders every primitive and state (light/dark, EN/NE).

---

## Key source index

Theme and tokens:

- `app/src/main/java/com/astro/vajra/ui/theme/Color.kt`
- `app/src/main/java/com/astro/vajra/ui/theme/AppTheme.kt`
- `app/src/main/java/com/astro/vajra/ui/theme/Theme.kt`
- `app/src/main/java/com/astro/vajra/ui/theme/Type.kt`
- `app/src/main/java/com/astro/vajra/ui/theme/DesignTokens.kt`

Core primitives and common components:

- `app/src/main/java/com/astro/vajra/ui/components/common/NeoVedicPrimitives.kt`
- `app/src/main/java/com/astro/vajra/ui/components/common/ModernTabRow.kt`
- `app/src/main/java/com/astro/vajra/ui/components/common/ModernCards.kt`

Main shell:

- `app/src/main/java/com/astro/vajra/ui/screen/main/MainScreen.kt`

Primary screens analyzed in this report:

- `app/src/main/java/com/astro/vajra/ui/screen/main/HomeTab.kt`
- `app/src/main/java/com/astro/vajra/ui/screen/main/InsightsTab.kt`
- `app/src/main/java/com/astro/vajra/ui/screen/BirthChartScreen.kt`
- `app/src/main/java/com/astro/vajra/ui/screen/chartdetail/tabs/ChartTabContent.kt`
- `app/src/main/java/com/astro/vajra/ui/screen/yogas/YogasScreenRedesigned.kt`
- `app/src/main/java/com/astro/vajra/ui/screen/PredictionsScreen.kt`
- `app/src/main/java/com/astro/vajra/ui/screen/matchmaking/MatchmakingScreen.kt`
- `app/src/main/java/com/astro/vajra/ui/screen/matchmaking/MatchmakingComponents.kt`

---

## Final takeaway

AstroVajra already has a real, production-scale Neo-Vedic design system - not just a visual idea. It has token foundations, reusable primitives, broad multi-screen adoption, and strong thematic consistency. The remaining work is mostly governance and consolidation (unifying duplicated patterns, finishing semantic migration, and enforcing token-only usage) rather than inventing the system from scratch.
