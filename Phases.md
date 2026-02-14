# AstroStorm Neo-Vedic UI Revamp Master Plan

Last updated: 2026-02-14

## Purpose
This is the single source of truth for the Neo-Vedic redesign rollout.
It defines inventory, standards, wave sequencing, quality gates, and live progress.

## Current State (Measured)
Scope baseline from `app/src/main/java/com/astro/storm/ui/screen`:

- Total screen files: `76`
- `ScreenTopBar(` usages: `0` (target met)
- `NeoVedicPageHeader(` usages: `57`
- Shared tab row usages (`ModernPillTabRow`/`NeoVedicFeatureTabs`): `30`
- Direct tab API usages (`FilterChip`/`TabRow`/`ScrollableTabRow`/`Tab`): `0` (target met)
- Hardcoded font sizes in screen files (`fontSize = *.sp`): `0` (target met)
- Hardcoded hex colors in screen files: `0` (target met)

## Completed In This Iteration

### Phase 0 - Documentation and Audit Foundation (Completed)
- Replaced this file with a canonical master plan.
- Added Windows-native audit scripts:
  - `scripts/ui_style_report.ps1`
  - `scripts/ui_consistency_check.ps1`
  - `scripts/ui_typography_nav_report.ps1`

### Phase 1 - Shared System Hardening (In Progress)
- Expanded `NeoVedicPageHeader` in `app/src/main/java/com/astro/storm/ui/components/common/NeoVedicPrimitives.kt` to support:
  - `actions: RowScope.() -> Unit`
  - `subtitleContent: ColumnScope.() -> Unit`
- Deprecated `ScreenTopBar` and internally routed it to `NeoVedicPageHeader` in `app/src/main/java/com/astro/storm/ui/components/CommonComponents.kt`.
- Added additional Neo-Vedic layout tokens in `app/src/main/java/com/astro/storm/ui/theme/DesignTokens.kt`.
- Added semantic type aliases in `app/src/main/java/com/astro/storm/ui/theme/Type.kt` via `NeoVedicTypeScale`.
- Added standardized feature tab wrapper `NeoVedicFeatureTabs` in `app/src/main/java/com/astro/storm/ui/components/common/ModernTabRow.kt`.

### Phase 2 - Header Standardization (Completed)
- Migrated screen calls from `ScreenTopBar(` to `NeoVedicPageHeader(` across `ui/screen`.
- Result: `ScreenTopBar(` usage in screen files is now `0`.

## Target End State
- Feature screens use Neo-Vedic primitives and tokens only.
- No direct tab APIs in screen files.
- No uncontrolled hardcoded `fontSize = *.sp` in screen files.
- No hardcoded visible strings in revamped UI paths.
- English + Nepali localization intact for all updated UI copy.

## Public API and Type Changes

### Components
- `app/src/main/java/com/astro/storm/ui/components/common/NeoVedicPrimitives.kt`
  - `NeoVedicPageHeader` is the standard header primitive.
- `app/src/main/java/com/astro/storm/ui/components/CommonComponents.kt`
  - `ScreenTopBar` is deprecated and scheduled for removal in Phase 5.
- `app/src/main/java/com/astro/storm/ui/components/common/ModernTabRow.kt`
  - `NeoVedicFeatureTabs` added as standard feature-tab entrypoint.

### Theme
- `app/src/main/java/com/astro/storm/ui/theme/Type.kt`
  - `NeoVedicTypeScale` added for semantic typography use.
- `app/src/main/java/com/astro/storm/ui/theme/DesignTokens.kt`
  - Additional layout tokens added for standardized spacing/padding rhythms.

## Wave Execution Plan

### Wave A - Highest Complexity
- `VarshaphalaScreen.kt`
- `MuhurtaScreen.kt`
- `NativeAnalysisScreen.kt`
- `PredictionsScreen.kt`
- `PrashnaScreen.kt`
- `RemediesScreen.kt`
- `matchmaking/MatchmakingScreen.kt`

### Wave B - Timing and Transit Systems
- `KalachakraDashaScreen.kt`
- `YoginiDashaScreen.kt`
- `AshtottariDashaScreen.kt`
- `CharaDashaScreen.kt`
- `DrigDashaScreen.kt`
- `ShoolaDashaScreen.kt`
- `DashaSandhiScreen.kt`
- `AshtavargaTransitScreen.kt`
- `KakshaTransitScreen.kt`
- `UpachayaTransitScreen.kt`
- `GocharaVedhaScreen.kt`
- `transits/TransitsScreenRedesigned.kt`

### Wave C - Yoga, Jaimini, Strength, Divisional
- `ArgalaScreen.kt`
- `ArudhaPadaScreen.kt`
- `BhriguBinduScreen.kt`
- `GrahaYuddhaScreen.kt`
- `JaiminiKarakaScreen.kt`
- `KemadrumaYogaScreen.kt`
- `NityaYogaScreen.kt`
- `PanchMahapurushaScreen.kt`
- `VipareetaRajaYogaScreen.kt`
- `ShadbalaScreen.kt`
- `SthanaBalaScreen.kt`
- `KalaBalaScreen.kt`
- `DrigBalaScreen.kt`
- `AvasthaScreen.kt`
- `IshtaKashtaPhalaScreen.kt`
- `ShodashvargaScreen.kt`
- `DivisionalChartsScreen.kt`
- `SaptamsaScreen.kt`
- `SahamScreen.kt`
- `SarvatobhadraChakraScreen.kt`

### Wave D - Core, Entry, Support
- `BirthChartScreen.kt`
- `PlanetsScreen.kt`
- `ChartAnalysisScreen.kt`
- `ChartInputScreen.kt`
- `OnboardingScreen.kt`
- `main/MainScreen.kt`
- `main/HomeTab.kt`
- `main/InsightsTab.kt`
- `main/SettingsTab.kt`
- `ashtakavarga/AshtakavargaScreenRedesigned.kt`
- `panchanga/PanchangaScreenRedesigned.kt`
- `yogas/YogasScreenRedesigned.kt`

## Acceptance Gates

### Static/UI Gates
- `ScreenTopBar(` usage in `ui/screen`: target `0`.
- Direct tab API usage (`FilterChip`, `TabRow`, `ScrollableTabRow`, `Tab`) in `ui/screen`: target `0`.
- Hardcoded `fontSize = *.sp` in `ui/screen`: target `0` except explicit whitelist.
- Hardcoded `Color(0x` in `ui/screen`: target `0`.

### Build Gates
- `gradlew.bat :app:assembleDebug`
- `gradlew.bat :app:lintDebug`

### Manual UX Gates
- Flow: onboarding -> chart input -> main tabs -> representative screens from each wave.
- Header consistency (back nav, subtitle handling, action slots).
- Tab switching behavior, long-list scroll behavior, loading/error/empty states.
- Language pass in English and Nepali.
- Visual pass on phone + tablet, light + dark.

## Audit Commands (Windows)

### Scripted
- `powershell -ExecutionPolicy Bypass -File scripts/ui_style_report.ps1`
- `powershell -ExecutionPolicy Bypass -File scripts/ui_consistency_check.ps1`
- `powershell -ExecutionPolicy Bypass -File scripts/ui_typography_nav_report.ps1`

### One-off quick checks
- `rg -n "ScreenTopBar\(" app/src/main/java/com/astro/storm/ui/screen`
- `rg -n "FilterChip\(|ScrollableTabRow\(|\bTabRow\(|\bTab\(" app/src/main/java/com/astro/storm/ui/screen`
- `rg -n "fontSize\s*=\s*[0-9]+\.?[0-9]*\.sp" app/src/main/java/com/astro/storm/ui/screen`
- `rg -n "Color\(0x" app/src/main/java/com/astro/storm/ui/screen`

## Known Gaps
- Screen-level typography literal cleanup is complete; remaining literal sizes are outside `ui/screen` and will be handled in shared/component cleanup.
- Full localization enforcement for all revamped strings is still pending.
  - Ephemeris flow localization is migrated to centralized keys (`StringKeyEphemerisUi`) including motion status, orb labels, and degree/minute formatting in the redesigned transits UI.
  - Aspect classification now uses stable key metadata (language-agnostic), avoiding Nepali-mode regressions.
  - Wave A localization cleanup is partially advanced (Muhurta day/night labels now localized; several visible mojibake glyphs normalized in Native/Varshaphala screens).
  - Predictions screen period/date cards now use locale-aware date formatters; remaining Wave A string hardening in `PrashnaScreen.kt` is pending.
  - Prashna defaults are now localization-safe; remaining Wave A work is broader string/UI harmonization across other complex screens.
  - Remedies screen visible bullet separators are now normalized to shared localized bullet tokens.
- Legacy `ScreenTopBar` has been removed; `NeoVedicPageHeader` is now the only active screen header path.
- Local build gate currently blocked by environment JDK version parsing (`25.0.2`) during Gradle/Kotlin script setup.
- CI compile stability fixes are now tracked in this plan and shipped incrementally when errors surface.

## Version Ledger
- v2.0 (2026-02-14)
  - Canonical master-plan rewrite of `Phases.md`.
  - Added PowerShell audit scripts.
  - Completed screen-level header migration to `NeoVedicPageHeader`.
  - Deprecated `ScreenTopBar` and routed it through Neo-Vedic primitive.
  - Added type/token/tab standardization primitives.
- v2.1 (2026-02-14)
  - Removed direct tab API usage from `ui/screen` by migrating:
    - `TabRow`/`Tab` screens to shared Neo-Vedic tab rows.
    - `FilterChip` calls to shared `NeoVedicChoicePill` primitive.
  - Static nav-bypass gate reached `0`.
- v2.2 (2026-02-14)
  - Eliminated literal `fontSize = *.sp` in `ui/screen` by centralizing screen font sizes through `NeoVedicFontSizes`.
  - Static screen-level typography gate reached `0`.
- v2.3 (2026-02-14)
  - Removed legacy `ScreenTopBar` from `CommonComponents.kt`.
  - Completed header-system consolidation on `NeoVedicPageHeader`.
- v2.4 (2026-02-14)
  - Added `StringKeyEphemerisUi` and migrated hardcoded Ephemeris labels/status text to localized keys.
  - Localized day-offset badges and date-header locale behavior in `TransitsScreenRedesigned.kt`.
- v2.5 (2026-02-14)
  - Replaced remaining hardcoded transit-position/aspect labels in `TransitsScreenRedesigned.kt`:
    - Motion pill text (`Retrograde`/`Direct`) now localized.
    - Nakshatra pada label now localized via shared string keys.
    - Orb and degree-minute labels now localized via `StringKeyEphemerisUi`.
  - Hardened aspect-type detection in both transits mapper and UI to be language-agnostic (key-based), preventing Nepali localization breakage.
- v2.6 (2026-02-14)
  - Localized Choghadiya day/night section labels in `MuhurtaScreen.kt` via new `StringKeyMuhurta` entries.
  - Replaced visible mojibake glyph literals in `NativeAnalysisScreen.kt` with stable Unicode escapes (checkmark, warning, bullets, Jupiter glyph, folded-hands icon).
  - Replaced visible mojibake glyph literals in `VarshaphalaScreen.kt`:
    - Aspect separator arrow.
    - Specific-event bullet prefix.
    - Full zodiac symbol map in `getZodiacSymbol`.
- v2.7 (2026-02-14)
  - Replaced remaining mojibake bullet literal in `PredictionsScreen.kt` with shared localized bullet token.
  - Switched key date/period formatters in `PredictionsScreen.kt` to locale-aware formatters (`en`/`ne`) for favorable, caution, and key-date cards.
- v2.8 (2026-02-14)
  - Removed India-specific hardcoded defaults from `PrashnaScreen.kt`:
    - Timezone fallback now uses `ZoneId.systemDefault().id`.
    - Location fallback now uses localized key `PRASHNA_CURRENT_LOCATION`.
  - Added `PRASHNA_CURRENT_LOCATION` to `StringKeyAnalysis` for localized fallback location labeling.
- v2.9 (2026-02-14)
  - Replaced remaining mojibake bullet/separator literals in `RemediesScreen.kt` with shared localized bullet token (`StringKeyUICommon.BULLET`).
  - Kept screen-level typography/nav gates intact while improving visible text consistency in remedies UI.
- v3.0 (2026-02-14)
  - Fixed CI `compileReleaseKotlin` blockers:
    - `ModernTabRow.kt`: corrected `LocalContentColor` import to Material3 and aligned `interactionSource` parameters with non-null API expectations.
    - `YogasTabContent.kt`: replaced malformed Nepali digit char literals with Unicode escapes (`\u0966`..`\u096F`).
