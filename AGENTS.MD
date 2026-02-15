# AstroStorm Agent Knowledge Base

## Learnings
- PWA cache strategy: switching local assets from cache-first to stale-while-revalidate improves update visibility.
- Dasha precision: Jaimini-oriented dashas are more accurate with exaltation/debilitation weighting adjustments.
- Localization safety: use display-name helper indirection to avoid wrong translation namespaces.
- Edit/update UX: when no fields are changed in chart edit mode, treat update as a no-op success.

## Actions Taken
- Added/expanded advanced dasha and yoga capabilities.
- Added dedicated Avasthas screen and supporting UI structure.
- Improved localization consistency across analysis screens.
- Added robust no-change short-circuit + timezone normalization in chart edit submission flow.

## Architecture Decisions
- Maintain modular ephemeris engines and feature-specific calculators.
- Keep data entry resilient: normalize timezone IDs before storage/calculation.

## App Structure
- Input/edit flow: `app/src/main/java/com/astro/storm/ui/screen/ChartInputScreen.kt`
- Settings/preferences: `app/src/main/java/com/astro/storm/ui/screen/main/SettingsTab.kt`
- Calculation core: `app/src/main/java/com/astro/storm/ephemeris/`

## Known Issues
- Exact original stack origin for `length=13; index=13` remains UNCONFIRMED due missing runtime trace.
