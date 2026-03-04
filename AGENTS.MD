# AstroVajra Agent Knowledge Base

## Learnings
- PWA cache strategy: switching local assets from cache-first to stale-while-revalidate improves update visibility.
- Dasha precision: Jaimini-oriented dashas are more accurate with exaltation/debilitation weighting adjustments.
- Localization safety: use display-name helper indirection to avoid wrong translation namespaces.
- Edit/update UX: when no fields are changed in chart edit mode, treat update as a no-op success.
- Runtime safety: sanitize legacy/invalid timezone strings centrally before calculation to prevent parser edge-case crashes.
- Nepali typography: Devanagari can appear optically smaller under Latin-oriented uppercase/micro-label styling; use language-aware casing/tracking and a modest Nepali-only font-scale multiplier.

## Actions Taken
- Added/expanded advanced dasha and yoga capabilities.
- Added dedicated Avasthas screen and supporting UI structure.
- Improved localization consistency across analysis screens.
- Added robust no-change short-circuit + timezone normalization in chart edit submission flow.
- Added `TimezoneSanitizer` and wired it into repository mapping, transit analyzer, and Swiss ephemeris timezone resolution.
- Added transit screen error-state UI with retry instead of infinite loading when analysis fails.
- Added navigation/main-screen reset hooks to avoid stale error snackbar reappearing after back navigation.
- Added resilient fallback calculation flow in `ChartViewModel` to retry with sanitized/system/UTC timezone before surfacing an error.
- Added Nepali readability patch in Neo-Vedic shell/home layers:
  - theme-level Nepali font scale via `LocalDensity` in `ui/theme/Theme.kt`.
  - language-aware casing/tracking/font-family updates in:
    - `ui/screen/main/MainScreen.kt`
    - `ui/screen/main/HomeTab.kt`
    - `ui/screen/main/SettingsTab.kt`
    - `ui/components/common/NeoVedicPrimitives.kt`
- Applied CI compile hotfix for Nepali patch:
  - fixed missing `language` binding inside `SnapshotCard(...)` in `ui/screen/main/HomeTab.kt` (`Unresolved reference: language`).

## Architecture Decisions
- Maintain modular ephemeris engines and feature-specific calculators.
- Keep data entry resilient: normalize timezone IDs before storage/calculation.
- Normalize timezone IDs when mapping DB entities to domain models to harden old data compatibility.

## App Structure
- Input/edit flow: `app/src/main/java/com/astro/vajra/ui/screen/ChartInputScreen.kt`
- Settings/preferences: `app/src/main/java/com/astro/vajra/ui/screen/main/SettingsTab.kt`
- Calculation core: `app/src/main/java/com/astro/vajra/ephemeris/`

## Known Issues
- Exact original stack origin for `length=13; index=13` remains UNCONFIRMED due missing runtime trace; defensive timezone sanitization/fallback now guards this path.
