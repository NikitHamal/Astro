# AstroStorm Codebase Findings

## Scope
Focused on ephemeris accuracy, Vedic-specific calculations, correctness, modularity, and performance. Findings below are prioritized by severity.

## Critical
1. Dasha timing uses `LocalDateTime` without timezone context.
   - `calculateDashaTimeline` uses `LocalDateTime.now()` and compares it to birth data that is also timezone-less, which can shift the active dasha when the device timezone differs from the birth timezone or when DST rules apply.
   - This affects every dasha level (mahadasha, antardasha, pratyantardasha, etc.) and can yield incorrect currently-active periods.
   - File: `app/src/main/java/com/astro/storm/ephemeris/DashaCalculator.kt` (around lines 524-606).

2. Panchanga day boundaries and Vara are computed from UTC JD rather than local sunrise.
   - Panchanga is traditionally sunrise-based; current logic computes sunrise/sunset using a JD anchored to UTC midnight and derives Vara from JD, which can be wrong for many locations/timezones.
   - This can misclassify Vara, tithi-at-sunrise, and daily panchanga outputs.
   - File: `app/src/main/java/com/astro/storm/ephemeris/PanchangaCalculator.kt` (around lines 49-115).

## High
3. Chart caching ignores runtime calculation settings.
   - Cache key only includes birth data, houseSystem, and constructor-time `ayanamsaType`. It omits current settings from `AstrologySettingsManager` (ayanamsa, node mode, house system).
   - Changing settings can return stale charts from cache, causing silent accuracy errors.
   - File: `app/src/main/java/com/astro/storm/ephemeris/SwissEphemerisEngine.kt` (around lines 269-340).

4. Single-planet calculation hardcodes Placidus houses.
   - `calculateSinglePlanetPosition` always uses house system `P` even when user settings are `WHOLE_SIGN`, `EQUAL`, etc.
   - This makes planet house placement inconsistent with the main chart for non-Placidus users.
   - File: `app/src/main/java/com/astro/storm/ephemeris/SwissEphemerisEngine.kt` (around lines 378-401).

5. Panchanga uses fixed Lahiri ayanamsa and separate Swiss Ephemeris instance.
   - Panchanga calculations ignore user-selected ayanamsa and node settings, risking inconsistency with chart outputs.
   - The ephemeris path is set but there is no asset copy/validation here, so high-precision data availability may differ from the main engine.
   - File: `app/src/main/java/com/astro/storm/ephemeris/PanchangaCalculator.kt` (around lines 44-67).

## Medium
6. Dasha year length is fixed to tropical year (365.24219) and not configurable.
   - Vimshottari and related dashas are often computed using sidereal or savana years (360 days), depending on tradition. Using a single tropical year can shift sub-period boundaries.
   - File: `app/src/main/java/com/astro/storm/ephemeris/DashaUtils.kt` (around lines 21-30).

7. Chara Karaka system is fixed to the 8-karaka + Rahu variant and uses placeholder defaults.
   - No option for 7-karaka tradition or alternative handling of Rahu/Ketu; placeholder fill with Sun/Aries can silently corrupt results if any planet data is missing.
   - File: `app/src/main/java/com/astro/storm/ephemeris/CharaDashaCalculator.kt` (around lines 289-338).

8. Divisional chart calculations are hard-coded to one scheme for each varga.
   - Several vargas (D2, D3, D30, etc.) have multiple classical methods; no settings exist to pick a tradition, which can lead to user-perceived errors.
   - File: `app/src/main/java/com/astro/storm/ephemeris/DivisionalChartCalculator.kt` (various).

9. Panchanga output lacks sunrise-based tithi/nakshatra end times.
   - Panchanga is typically reported with end times and “at sunrise” values; current output only returns instantaneous values at the input time.
   - File: `app/src/main/java/com/astro/storm/ephemeris/PanchangaCalculator.kt` (around lines 49-103).

## Low
10. Duplicate ayanamsa enums can drift over time.
   - `SwissEphemerisEngine.AyanamsaType` and `core.model.Ayanamsa` represent the same concept in different places.
   - This increases maintenance risk and can cause mismatches when expanding settings.
   - Files: `app/src/main/java/com/astro/storm/ephemeris/SwissEphemerisEngine.kt`, `core/model/src/main/java/com/astro/storm/core/model/CalculationSettings.kt`.

11. Localization gaps remain in model/ephemeris display strings.
   - Several `displayName`/`description` fields are hardcoded English and bypass `StringResources`.
   - This is inconsistent with the localization strategy used elsewhere.
   - Examples: `app/src/main/java/com/astro/storm/ephemeris/DivisionalChartCalculator.kt` (DivisionalChartType), `app/src/main/java/com/astro/storm/ephemeris/CharaDashaCalculator.kt` (description strings).

12. No automated test suites detected for core calculation logic.
   - No `src/test` or `src/androidTest` directories found in app/core modules.
   - This increases regression risk for math-sensitive logic.

## Architecture and Performance Opportunities
- Move ephemeris/calculation code out of `app` into a dedicated `:core:ephemeris` module to align with modularization requirements and enable targeted testing.
- Add settings-aware cache invalidation to `SwissEphemerisEngine` (ayanamsa, node mode, house system) and consider memoization for expensive divisional/dasha calculations.
- Guard `PanchangaCalculator` against concurrent access or share the thread-safe `SwissEphemerisEngine` with appropriate synchronization.

## Suggested Research Checks (Astro Accuracy)
- Verify each varga scheme (D2, D3, D7, D30, D60, D81, D108, D144) against the intended tradition (Parasari/Jaimini/Tajika) and document the chosen variant.
- Confirm dasha year-length standard for each dasha system (Vimshottari, Yogini, Ashtottari, Chara, Kala Chakra) and make it user-configurable per tradition.
- Validate Panchanga sunrise, tithi, and yoga computations against standard Panchanga references for multiple locations/timezones.
