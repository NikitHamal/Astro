# AstroVajra Vedic Accuracy Audit and Roadmap

Date: 2026-03-05

## Audit Objective
- Move the app toward stricter classical Jyotisha behavior where applicable.
- Keep Uranus, Neptune, and Pluto available for optional modern context (not as core Vedic timing determinants by default).
- Remove hidden non-determinism where deterministic outcomes are expected.
- Replace opaque heuristics and placeholders with explicit methodology, configurable policy, and provenance.

## Step 1 Deliverable (this file)
- Baseline audit of heuristic/incomplete areas across the app.
- Prioritized rollout plan with concrete modules.
- Immediate P0 changes tracked below.

## P0 Changes Applied
- Added user setting for Dasha year basis with configurable options:
  - Savana 360-day year (default; classical-first)
  - Tropical 365.24219-day year
- Wired Dasha timing basis to runtime calculation policy (`DashaUtils`) with immediate effect on future calculations.
- Updated major dasha cache keys to include year-basis policy to prevent stale timelines after settings change.
- Added user setting for Ashtamangala shell mode:
  - Deterministic (daily) (default)
  - Classic random throw
- Made Ashtamangala generation deterministic-by-default when no explicit seed is passed.
- Added user setting for outer-planet policy:
  - Classical only (default)
  - Include in extended analysis
- Wired outer-planet policy into key hardcoded exclusion points:
  - `AspectCalculator`
  - `DrigBalaCalculator`
  - `RetrogradeCombustionCalculator`
- Replaced duplicated timezone fallback parsing with centralized `TimezoneSanitizer` across dasha-related engines:
  - `DashaCalculator`
  - `AshtottariDashaCalculator`
  - `CharaDashaCalculator`
  - `KalachakraDashaCalculator`
  - `YoginiDashaCalculator`
  - `DashaSandhiAnalyzer`
  - `SudarshanaChakraDashaCalculator`
- Extended timezone sanitization unification across remaining manual parsers:
  - `AshtamangalaPrashnaCalculator`
  - `AshtavargaTransitCalculator`
  - `BhriguBinduCalculator`
  - `HoroscopeCalculator`
  - `KakshaTransitCalculator`
  - `PanchangaCalculator`
  - `SadeSatiCalculator`
  - `SarvatobhadraChakraCalculator`
  - `GocharaVedhaCalculator`
  - `TarabalaCalculator`
  - `UpachayaTransitTracker`
  - `jaimini/DrigDashaCalculator`
  - `muhurta/MuhurtaCalculator`
  - `prashna/PrashnaCalculator`
  - `shoola/ShoolaDashaCalculator`
  - `varshaphala/MuddaDashaCalculator`
  - `varshaphala/VarshaphalaHelpers`
- Extended timezone sanitization to UI/viewmodel resolver points to eliminate layer drift:
  - `ui/screen/AshtottariDashaScreen`
  - `ui/screen/BhriguBinduScreen`
  - `ui/screen/DrigDashaScreen`
  - `ui/screen/KalachakraDashaScreen`
  - `ui/screen/MuhurtaScreen`
  - `ui/screen/PredictionsScreen`
  - `ui/screen/SudarshanaChakraScreen`
  - `ui/screen/VarshaphalaScreen`
  - `ui/screen/YoginiDashaScreen`
  - `ui/screen/main/HomeTab`
  - `ui/screen/main/InsightsTab`
  - `ui/viewmodel/AshtavargaTransitViewModel`
  - `ui/viewmodel/InsightsViewModel`
  - `ui/viewmodel/KakshaTransitViewModel`
- Replaced 7-day fixed significant transit period buckets with daily sampled, contiguous sign-window period detection in `TransitAnalyzer`.

## High-Priority Heuristic/Incomplete Hotspots

### P1: Timing and Dasha Method Consistency
- `app/src/main/java/com/astro/vajra/ephemeris/DashaCalculator.kt`
  - Mixed fixed assumptions and local conversions should be normalized through one explicit timing basis policy throughout all layers.
- `app/src/main/java/com/astro/vajra/ephemeris/AshtottariDashaCalculator.kt`
  - Previously cached days/year constant; now dynamic, but broader method validation and textual disclosure still needed.
- `app/src/main/java/com/astro/vajra/ephemeris/CharaDashaCalculator.kt`
  - Ensure all derived duration math references centralized policy and explicit references.
- `app/src/main/java/com/astro/vajra/ephemeris/KalachakraDashaCalculator.kt`
  - Same as above, plus classical source notes per calculation branch.
- `app/src/main/java/com/astro/vajra/ephemeris/YoginiDashaCalculator.kt`
  - Verify every duration and sub-period scaling against classical source interpretation and selected year basis.

### P1: Non-deterministic or Opaque Prediction Inputs
- `app/src/main/java/com/astro/vajra/ephemeris/AshtamangalaPrashnaCalculator.kt`
  - Previously random by `System.nanoTime()`. Now configurable deterministic/random mode; next step is adding explicit provenance display in UI.

### P1: Placeholder/Approximation Markers Affecting Practical Usefulness
- `app/src/main/java/com/astro/vajra/ephemeris/BhriguBinduCalculator.kt`
  - Placeholder estimated dates.
- `app/src/main/java/com/astro/vajra/ephemeris/DrigBalaCalculator.kt`
  - Placeholder substitution for house aspect data path.
- `app/src/main/java/com/astro/vajra/ephemeris/NadiAmshaCalculator.kt`
  - Simplified boundary handling and approximation comments in rectification path.
- `app/src/main/java/com/astro/vajra/ephemeris/SarvatobhadraChakraCalculator.kt`
  - Simplified tithi calculation marker.
- `app/src/main/java/com/astro/vajra/ephemeris/SadeSatiCalculator.kt`
  - Approximate timeline framing requires formalization or explicit confidence labeling.

### P2: Simplified Classical Logic Requiring Upgrade Path
- `app/src/main/java/com/astro/vajra/ephemeris/AvasthaCalculator.kt`
  - Simplified node avastha logic path.
- `app/src/main/java/com/astro/vajra/ephemeris/GrahaYuddhaCalculator.kt`
  - Simplified declination and trend checks.
- `app/src/main/java/com/astro/vajra/ephemeris/BadhakaCalculator.kt`
  - Approximation note in full-rule handling.
- `app/src/main/java/com/astro/vajra/ephemeris/ShadbalaCalculator.kt`
  - Sunrise/sunset approximation marker in one path.

## Outer Planets Policy (strict classics + keep U/N/P)
- Keep Uranus/Neptune/Pluto in data model, transit views, and optional modern analytics.
- Do not include them in classical Vedic foundational rule engines by default unless the user explicitly opts into extended mode.
- Areas already excluding them in classical-specific logic include:
  - `AspectCalculator.kt`
  - `DrigBalaCalculator.kt`
  - `RetrogradeCombustionCalculator.kt`

## Next Execution Steps
1. Add methodology disclosure in each dasha/ashtamangala screen showing selected timing/throw mode and source assumptions.
2. Replace P1 placeholders with either exact computation paths or visible confidence tags and user-facing caveats.
3. Introduce a "Classical Core vs Extended" calculation profile to preserve strict defaults while allowing optional modern overlays.
4. Add deterministic regression fixtures for dasha timelines and Ashtamangala seeds.
5. Add module-by-module reference mapping to source texts (BPHS, Phaladeepika, Jaimini Sutra, Prashna Marga).
