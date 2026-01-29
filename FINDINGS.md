# AstroStorm Codebase Findings

## Scope & Method
- Reviewed core ephemeris engines, divisional chart calculators, dasha logic, panchanga logic, aspects, and shared model/constants.
- Verification is by source inspection only (no builds or tests).

## High-Priority Accuracy Issues
1. **Ayanamsa + node mode cache mismatch**
   - `SwissEphemerisEngine` calculates charts using `AstrologySettingsManager` (dynamic ayanamsa and node mode), but the cache key only includes the constructor `ayanamsaType` and ignores `nodeMode` and updated settings.
   - Result: chart cache can return stale planet positions after settings change.
   - Files: `app/src/main/java/com/astro/storm/ephemeris/SwissEphemerisEngine.kt:244`, `app/src/main/java/com/astro/storm/ephemeris/SwissEphemerisEngine.kt:321`.

2. **Panchanga ignores user ayanamsa settings**
   - `PanchangaCalculator` hardcodes Lahiri and uses its own `SwissEph` instance. This can diverge from chart calculations if the user selects a different ayanamsa.
   - Files: `app/src/main/java/com/astro/storm/ephemeris/PanchangaCalculator.kt:30`.

3. **Ketu latitude handling likely incorrect**
   - Ketu is computed by shifting Rahu longitude by 180° and negating speed, but latitude is not inverted. In Swiss Ephemeris, Ketu latitude is the negative of Rahu’s latitude.
   - This can affect any logic that uses node latitude/declination.
   - Files: `app/src/main/java/com/astro/storm/ephemeris/SwissEphemerisEngine.kt:411`.

4. **Special aspect strengths conflict with BPHS constants**
   - `AspectCalculator` assigns reduced strengths to Mars 4th, Jupiter 5th, Saturn 3rd aspects (0.75/0.5), while BPHS treats these as full aspects (100%).
   - This conflicts with internal `AstrologicalConstants.SPECIAL_ASPECTS` and can under-weight Drishti Bala.
   - Files: `app/src/main/java/com/astro/storm/ephemeris/AspectCalculator.kt:28`, `app/src/main/java/com/astro/storm/ephemeris/AstrologicalConstants.kt:90`.

5. **Vimshottari year base not configurable**
   - `DashaUtils` uses 365.24219 days as the default for Vimshottari, but many Vedic implementations use a 360‑day savana year.
   - `DAYS_PER_SAVANA_YEAR` exists but is unused; needs a per-dasha configuration or explicit alignment.
   - Files: `app/src/main/java/com/astro/storm/ephemeris/DashaUtils.kt:25`.

## Divisional Chart Formula Risks (Accuracy Verification Required)
- Several vargas are implemented via generalized formulas that do not appear to follow classical sign-based rules.
- Examples:
  - D45 uses modality-based starting sign (classical D45 uses sign-specific mapping).
  - D81/D108/D144 map `part % 12` without sign parity logic.
- These are high-risk areas for precision Vedic astrology.
- Files: `app/src/main/java/com/astro/storm/ephemeris/DivisionalChartCalculator.kt:524`, `app/src/main/java/com/astro/storm/ephemeris/DivisionalChartCalculator.kt:642`.

## House System Defaults (Vedic Consistency)
- `HouseSystem.DEFAULT` and `CalculationSettings` default to Placidus, which is Western‑centric.
- For Vedic accuracy, default should be Whole Sign or a Vedic‑standard system like Sripati.
- Files: `core/model/HouseSystem.kt:6`, `core/model/CalculationSettings.kt:27`.

## Performance & Scalability Findings
1. **Shadbala computes every varga**
   - `ShadbalaCalculator` uses `calculateAllDivisionalCharts`, which generates 20+ vargas, even though only a subset is needed for Saptavargaja.
   - This is a high-cost operation on every Shadbala call; prefer lazy computation or targeted vargas.
   - Files: `app/src/main/java/com/astro/storm/ephemeris/ShadbalaCalculator.kt:353`, `app/src/main/java/com/astro/storm/ephemeris/DivisionalChartCalculator.kt:749`.

2. **Ashtakavarga is repeatedly recomputed**
   - `calculateKakshaPosition` can call full Ashtakavarga if analysis not passed in; this is heavy if used per‑planet/per‑transit.
   - Provide memoization per chart and reuse the analysis for all Kaksha calls.
   - Files: `app/src/main/java/com/astro/storm/ephemeris/AshtakavargaCalculator.kt:506`.

3. **Single-planet calculation forces Placidus**
   - `calculateSinglePlanetPosition` always uses Placidus houses (`'P'`), ignoring the active house system.
   - Can yield inconsistent house placement between single-planet calls and full chart calculations.
   - Files: `app/src/main/java/com/astro/storm/ephemeris/SwissEphemerisEngine.kt:387`.

## Consistency & Modularity Gaps
- Panchanga logic duplicates ephemeris setup and ayanamsa handling instead of reusing `SwissEphemerisEngine` or a shared abstraction.
  - This fragments settings consistency and increases maintenance risk.
  - Files: `app/src/main/java/com/astro/storm/ephemeris/PanchangaCalculator.kt:23`.

## Non‑Production Placeholders
- UI and localization contain TODO placeholders in production strings.
  - `StringKeyDosha.TODO_IN_PROGRESS` and UI “TODO LIST SECTION” comment indicate unfinished UX surface.
  - Files: `core/common/src/main/java/com/astro/storm/core/common/StringKeyDosha.kt:2532`, `app/src/main/java/com/astro/storm/ui/components/agentic/SectionedComponents.kt:1098`.

## Suggested High‑Impact Fixes (No Code Changes Applied)
- Add a settings-aware cache key (ayanamsa + node mode + house system) and invalidate cache on settings update.
- Unify Panchanga computation with the same ephemeris engine and ayanamsa settings.
- Align aspect strengths to BPHS and enforce a single source of truth.
- Make Vimshottari year base selectable (tropical vs savana) with explicit labeling.
- Revalidate all varga formulas against BPHS/Phaladeepika; avoid generalized mappings for D45/D81/D108/D144.
- Implement lazy divisional chart computation (calculate only needed vargas).

---

If you want, I can propose a prioritized remediation plan and implement fixes incrementally while preserving Vedic accuracy.