## Goal (incl. success criteria):
- Implement and fully integrate NEXT.md features (1–5): Triple-Pillar engine, BNN aspect engine, KP 4-step system, Muhurta optimization search, Ishta Devata & Beeja Mantra generator; production-grade, accurate, integrated in UI + calculations.

## Constraints/Assumptions:
- Follow existing code patterns and Vedic accuracy; no TODOs or simplified logic.
- Modularize code (500–800 lines per file max).
- Kotlin, MVVM, Jetpack Compose; Swiss Ephemeris used for calculations.

## Key Decisions:
- UNCONFIRMED: Integrate Triple-Pillar into Predictions/DeepPredictions timing sections.
- UNCONFIRMED: Add dedicated screens for BNN and KP under InsightFeature.
- UNCONFIRMED: Extend Muhurta search UI with optimization results.
- UNCONFIRMED: Integrate Ishta Devata/Beeja Mantra in Remedies.

## State:
- Done:
  - Located `NEXT.md` and reviewed scope for items 1–5.
  - Audited existing Dasha, Transit, Ashtakavarga, Muhurta, Remedies, and Predictions modules for integration points.
- Now:
  - Design new calculation modules and data models for Triple-Pillar, BNN, KP, Muhurta optimization, Ishta Devata/Mantra.
- Next:
  - Implement new engines and integrate with UI/viewmodels and navigation.

## Open Questions (UNCONFIRMED if needed):
- Where exactly should new UI panels live (new screens vs tabs in existing Predictions/Muhurta/Remedies)?
- Should KP use current house system (KP/Placidus) or keep chart house cusps as-is?
- Preferred date range defaults for Muhurta optimization (e.g., 30 days) and max results?

## Working Set (files/ids/commands):
- `NEXT.md`
- `CONTINUITY.md`
- `app/src/main/java/com/astro/storm/ephemeris/DashaCalculator.kt`
- `app/src/main/java/com/astro/storm/ephemeris/TransitAnalyzer.kt`
- `app/src/main/java/com/astro/storm/ephemeris/AshtakavargaCalculator.kt`
- `app/src/main/java/com/astro/storm/ephemeris/muhurta/MuhurtaCalculator.kt`
- `app/src/main/java/com/astro/storm/ephemeris/muhurta/MuhurtaEvaluator.kt`
- `app/src/main/java/com/astro/storm/ephemeris/remedy/RemediesCalculator.kt`
- `app/src/main/java/com/astro/storm/ui/screen/PredictionsScreen.kt`
- `app/src/main/java/com/astro/storm/ephemeris/deepanalysis/predictions/DeepPredictionEngine.kt`
