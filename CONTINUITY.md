# AstroStorm Overhaul - Continuity Ledger
> Last updated: 2026-01-19T21:30:00Z

## Goal (incl. success criteria):
- Deliver deeply integrated, production-grade native analysis and predictions engines.
- Expand descriptive, personalized output using modular, data-driven text (no hardcoded strings, no AI).
- Maintain performance and file size limits (500–800 lines per file).

## Constraints/Assumptions:
- No AI-assisted text generation at runtime.
- No hardcoded texts; use localization/data resources.
- Vedic astrology accuracy per classical sources.
- No TODOs or placeholder implementations.

## Key Decisions:
- Introduced `LocalizedText` and `InsightSection` to drive narrative output.
- Added `StringKeyNativeTraits`, `StringKeyNativeNarrative`, `StringKeyPredictionNarrative` for new localized templates.
- Rewrote native analysis to be rule-based with detailed insight sections.
- Reworked prediction scoring to be deterministic (no random), integrating dasha and yoga data.

## State:
- Done:
  - Added `core/common/LocalizedText.kt` for localized templates.
  - Added narrative/trait string key enums for native analysis and prediction timing.
  - Rebuilt `NativeAnalysisCalculator` with deeper, structured insights.
  - Updated `NativeAnalysisViewModel` and `NativeAnalysisScreen` to use localized keys and insight sections.
  - Added `AspectUtils` for shared aspect logic.
  - Updated prediction calculations to deterministic scoring and yoga integration.
- Now:
  - Audit remaining UI for modularization and any lingering hardcoded text.
- Next:
  - Split oversized UI files (`NativeAnalysisScreen.kt`, `PredictionsScreen.kt`) into modular files under 800 lines.
  - Verify predictions UI still aligns with new calculation outputs.

## Open Questions (UNCONFIRMED if needed):
- None.

## Working Set (files/ids/commands):
- `core/common/LocalizedText.kt`
- `core/common/StringKeyNativeTraits.kt`
- `core/common/StringKeyNativeNarrative.kt`
- `core/common/StringKeyPredictionNarrative.kt`
- `app/src/main/java/com/astro/storm/ephemeris/AspectUtils.kt`
- `app/src/main/java/com/astro/storm/ephemeris/nativeanalysis/NativeAnalysisCalculator.kt`
- `app/src/main/java/com/astro/storm/ephemeris/nativeanalysis/NativeModels.kt`
- `app/src/main/java/com/astro/storm/ui/viewmodel/NativeAnalysisViewModel.kt`
- `app/src/main/java/com/astro/storm/ui/screen/NativeAnalysisScreen.kt`
- `app/src/main/java/com/astro/storm/ui/screen/PredictionsScreen.kt`
