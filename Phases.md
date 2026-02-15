# AstroStorm Neo-Vedic UI Revamp Master Plan

Last updated: 2026-02-15

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
- Hardcoded font sizes across full `ui/` tree: `15` (all in `ui/theme/Type.kt` type-scale definitions; expected)

## Completed In This Iteration

### Phase 0 - Documentation and Audit Foundation (Completed)
- Replaced this file with a canonical master plan.
- Added Windows-native audit scripts:
  - `scripts/ui_style_report.ps1`
  - `scripts/ui_consistency_check.ps1`
  - `scripts/ui_typography_nav_report.ps1`

### Phase 1 - Shared System Hardening (Completed)
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
- `ui_consistency_check.ps1` token coverage is `71/76` because it scans all `.kt` files under `ui/screen`, including utility/mapping files (`ChartDetailColors.kt`, `ChartDetailUtils.kt`, `MatchmakingReportUtils.kt`, `EphemerisUiMapper.kt`) that are not composable screens.
- Full Gradle verification (`assembleDebug`, `lintDebug`) was intentionally skipped in this environment per execution constraint; static/UI audit gates are fully green.

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
- v3.1 (2026-02-14)
  - Executed a broad multi-wave normalization pass across `41` screen files in `ui/screen` and `ui/screen/chartdetail/tabs`.
  - Standardized broken glyph encodings in visible UI strings to Unicode escapes (bullets/arrows/check/warning/sparkles/degree/dash and related separators).
  - Completed targeted follow-up fixes in:
    - `ChartInputScreen.kt` (`prime`/`double-prime` parsing symbols)
    - `SynastryScreen.kt` (aspect glyph symbol map)
- v3.2 (2026-02-14)
  - Localized remaining Arudha mixed-language UI fragments in `ArudhaPadaScreen.kt`:
    - Added localized connector and lord/sign template usage.
    - Replaced hardcoded key-Arudha description list with string-key backed descriptions.
  - Removed residual hardcoded English weekday literals in `RemediesScreen.kt` weekly schedule wiring by switching from `Triple` literals to localized `(Planet, label)` pairs.
  - Added supporting string keys in `StringKeyDosha` for new Arudha description/template text.
- v3.3 (2026-02-14)
  - Standardized date/time formatter usage in entry + transit wave screens:
    - `ChartInputScreen.kt`: replaced inline date/time formatter literals with shared screen-level formatter constants.
    - `AshtavargaTransitScreen.kt`: replaced repeated inline date patterns with centralized language-aware formatter helpers (`en`/`ne` locale selection).
  - Kept static gates stable (`ScreenTopBar=0`, direct tab APIs=0, hardcoded `fontSize = *.sp` in `ui/screen`=0).
- v3.4 (2026-02-14)
  - Extended locale-aware formatter cleanup across core main tabs:
    - `main/HomeTab.kt`: dasha timeline and snapshot date labels now use language-aware formatter helpers (removed hardcoded English weekday/date rendering).
    - `main/InsightsTab.kt`: consolidated formatter locale selection (`Locale.forLanguageTag("ne-NP")` / `Locale.ENGLISH`) and replaced inline weekday abbreviation formatter with shared helper.
  - Preserved all style/navigation gates after the batch.
- v3.5 (2026-02-14)
  - Continued transit/timing formatter hardening in Wave B:
    - `DashaSandhiScreen.kt`: replaced inline date pattern calls in current/upcoming/calendar sections with centralized language-aware formatter helpers.
    - `KakshaTransitScreen.kt`: replaced repeated inline timeline/favorable period date formatters with shared language-aware helper formatters.
  - Static audit gates remained stable post-change.
- v3.6 (2026-02-14)
  - Completed next timing-system formatter wave:
    - `DrigDashaScreen.kt`: replaced inline pattern allocation sites with shared locale-aware formatter helpers (`long`, `month-year`, `short` variants).
    - `ShoolaDashaScreen.kt`: replaced inline month-year formatters in current/period/vulnerability cards with shared language-aware formatter helper.
  - Confirmed no regression in style/nav static checks after the batch.
- v3.7 (2026-02-14)
  - Continued Wave A/B formatter-localization hardening:
    - `MuhurtaScreen.kt`: replaced static `Locale.getDefault()` formatter constants with language-aware formatter functions keyed to app language (`en`/`ne`).
    - `VarshaphalaScreen.kt`: made full/short date and time formatters explicitly locale-aware in solar return, key dates, and mudda dasha period cards.
  - Static consistency/typography gates remained green after this batch.
- v3.8 (2026-02-14)
  - Extended locale-safe formatter cleanup across additional wave screens:
    - `panchanga/PanchangaScreenRedesigned.kt`: centralized full-date, birth-date, and time formatting helpers with explicit `en`/`ne` locale mapping; replaced inline timing/date formatter calls in summary and timing sections.
    - `GocharaVedhaScreen.kt`: made forecast generation date locale-aware via shared formatter helper.
    - `AshtottariDashaScreen.kt`: localized antardasha compact date formatter with language-aware locale.
    - `NadiAmshaScreen.kt`: consolidated repeated `HH:mm:ss` pattern into shared formatter constant for consistency.
  - Static style/nav gates remained unchanged after the batch.
- v3.9 (2026-02-14)
  - Completed locale-aware formatter cleanup in remaining transit/chart detail paths:
    - `transits/EphemerisUiMapper.kt`: switched event time formatter to language-aware locale mapping (`en`/`ne`) and applied it to period/aspect/position time labels.
    - `transits/TransitsScreenRedesigned.kt`: normalized Nepali locale creation to `Locale.forLanguageTag("ne-NP")` in date-header formatting.
    - `chartdetail/tabs/ChartTabContent.kt`: replaced default-locale birth date/time formatters with app-language-aware formatter helpers.
  - Quality gate scripts remained stable after changes.
- v4.0 (2026-02-14)
  - Applied additional localization/visual consistency fixes:
    - `AshtamangalaPrashnaScreen.kt`:
      - Localized timing weekday labels using locale-aware `DayOfWeek` display names.
      - Localized center shell count numeral for Nepali mode.
    - `transits/EphemerisUiMapper.kt`:
      - Replaced mojibake aspect/planet glyph literals with stable Unicode escapes.
      - Normalized fallback aspect arrow glyph to Unicode escape.
  - Static UI consistency and typography/nav audits remained green.
- v4.1 (2026-02-14)
  - Completed codebase cleanup pass for residual encoding artifacts in large wave screens:
    - `NativeAnalysisScreen.kt`: replaced mojibake comment separators with stable ASCII separators.
    - `VarshaphalaScreen.kt`: replaced mojibake comment separators with stable ASCII separators.
  - Verified no remaining mojibake patterns in these files and re-ran UI consistency/typography reports.
- v4.2 (2026-02-14)
  - Localized remaining Ashtamangala UI enum-label leaks:
    - `AshtamangalaPrashnaScreen.kt`:
      - Replaced `QueryCategory.displayName` rendering with localized string-key mapping.
      - Replaced `ConfidenceLevel.name` badge rendering with localized confidence labels.
  - Kept all static consistency/typography gates green after the update.
- v4.3 (2026-02-15)
  - Completed shared/component typography migration pass:
    - Replaced remaining hardcoded `fontSize = *.sp` usages in:
      - `ui/components/BSDatePicker.kt`
      - `ui/components/ChartDialogs.kt`
      - `ui/components/LocationSearchField.kt`
      - `ui/components/TimezoneSelector.kt`
      - `ui/components/dialogs/DialogComponents.kt`
      - `ui/components/dialogs/FullScreenChartDialog.kt`
      - `ui/components/dialogs/PlanetDetailDialog.kt`
    - Standardized these to `NeoVedicFontSizes`.
  - Updated static metrics:
    - `ui/screen` hardcoded font-size gate remains `0`.
    - Full `ui/` hardcoded font-size count reduced to `15` (expected type-scale declarations in `ui/theme/Type.kt` only).
