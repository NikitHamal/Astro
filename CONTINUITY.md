# AstroStorm Overhaul - Continuity Ledger
> Last updated: 2025-12-31T22:35:00+05:45

## Goal
Complete production-grade overhaul: 100% Nepali localization, fix birth chart accuracy, and full refactoring for performance and clarity.

## State
### Phase 1: Deep Nepali Localization (IN PROGRESS)
- **Done**: 
    - Systematic text discovery and audit completed.
    - `Planet`, `ZodiacSign`, `Nakshatra`, and `StrengthRating` enums refactored with `stringKey`.
    - `LocalizationProvider.kt` enhanced with `localized()`, `localizedName()`, and `TIME_ONLY` date formatting.
    - `PlanetsTabContent.kt`, `KalachakraDashaScreen.kt`, and `PrashnaScreen.kt` fully localized.
- **Now**: Finalizing infrastructure verification (placeholders, plurals).
- **Next**: Audit and localize remaining priority screens (`DashaSystemsScreen.kt`, `AvasthaScreen.kt`).

## Working Set
- **Modules**: `com.astro.storm.ui.screen`, `com.astro.storm.data.localization`
- **Active Files**: `LocalizationProvider.kt`, `PrashnaScreen.kt`, `KalachakraDashaScreen.kt`

## Issues Found
- `PrashnaFormatters` previously relied on `Locale.getDefault()`, introducing date/time localization inconsistencies. Solved by migrating to `LocalDateTime.formatLocalized`.
- Hardcoded unit concatenations (e.g., "months", " महिना") were scattered. Centralized in `StringKey.UNIT_...`.
