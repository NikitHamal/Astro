# AstroStorm Overhaul - Continuity Ledger
> Last updated: 2026-01-19T19:20:00+05:45

## Goal (incl. success criteria)
Deliver a production-grade, deeply integrated Native Analysis and Predictions system:
- Deep, deterministic Vedic analysis across all calculators/features
- Long, highly descriptive outputs assembled from localization resources (no hardcoded text)
- Modular, performant implementation (500–800 LOC per file max)
- No AI usage in analysis/predictions flows

## Constraints/Assumptions
- Follow existing patterns and MVVM/Compose architecture
- Zero hardcoded text; use StringKey + StringResources
- No builds/tests; verify mentally
- Vedic accuracy per classical texts
- Modularization and performance focus

## State

- Done:
  - Existing refactors and core module work from prior ledger ✅
  - Added prediction engine + models with deterministic scoring and narrative templates ✅
  - Expanded Native Analysis calculator with deeper logic and localized narratives ✅
  - Introduced StringKeyPredictionNarrative/StringKeyNativeNarrative with new templates ✅

- Now:
  - Cleaning remaining localization gaps in NativeAnalysis/Predictions UI and keys.

- Next:
  - Finish UI wiring for new prediction/remedy models and remove any remaining hardcoded text.
  - Verify new StringKey entries are used consistently.

## Working Set (files/ids/commands)
- `app/src/main/java/com/astro/storm/ephemeris/nativeanalysis/NativeAnalysisCalculator.kt`
- `app/src/main/java/com/astro/storm/ui/screen/PredictionsScreen.kt`
- `app/src/main/java/com/astro/storm/ui/viewmodel/NativeAnalysisViewModel.kt`
- `core/common/src/main/java/com/astro/storm/core/common/StringKeyPrediction.kt`
