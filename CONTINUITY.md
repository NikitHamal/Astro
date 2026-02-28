## Goal (incl. success criteria):
Fully eliminate recurring `length=13; index=13` failures in chart create/edit flows, keep transit stable, and push a robust runtime-safe fix.

## Constraints/Assumptions:
- User requested direct fix + push.
- Skip tests and Gradle/`gradlew` commands.
- Preserve existing chart behavior while handling legacy/invalid stored timezone strings safely.

## Key Decisions:
- Add centralized timezone sanitizer/parser with safe fallback to avoid parser edge-case crashes from malformed/legacy timezone strings.
- Normalize timezone values at repository mapping boundaries so loaded charts use valid timezone IDs.
- Stop infinite loading in transit screen by handling analysis exceptions explicitly with retry UI.
- Reset chart UI error state when leaving chart input and after showing error snackbar to avoid repeated stale toasts.
- Add resilient chart calculation fallback in `ChartViewModel` (timezone-attempt sequence + safe mapped error) so create/update does not fail hard on parser edge cases.

## State:
- Done:
  - Added `TimezoneSanitizer` utility and integrated it in chart repository, transit analyzer, chart input, and Swiss engine timezone resolution.
  - Added explicit transit analysis error state + retry in `TransitsScreenRedesigned`.
  - Added state reset hooks in navigation callbacks and main-screen error snackbar handling.
  - Added chart-input error-dialog reset behavior for runtime (non-validation) errors.
  - Added `ChartViewModel` resilient chart calculation path with timezone fallback attempts and calculation error mapping.
- Now:
  - Commit and push the new `ChartViewModel` fallback patch.
- Next:
  - Ask user to verify new chart creation with `Generate & Save`, plus edit/transit regressions.

## Open Questions (UNCONFIRMED if needed):
- UNCONFIRMED: exact low-level source of `StringIndexOutOfBoundsException(length=13,index=13)` in prior runtime path (guarded via timezone sanitization/fallback).

## Working Set (files/ids/commands):
- `app/src/main/java/com/astro/vajra/util/TimezoneSanitizer.kt`
- `app/src/main/java/com/astro/vajra/data/repository/ChartRepository.kt`
- `app/src/main/java/com/astro/vajra/ephemeris/SwissEphemerisEngine.kt`
- `app/src/main/java/com/astro/vajra/ephemeris/TransitAnalyzer.kt`
- `app/src/main/java/com/astro/vajra/ui/screen/transits/TransitsScreenRedesigned.kt`
- `app/src/main/java/com/astro/vajra/ui/screen/ChartInputScreen.kt`
- `app/src/main/java/com/astro/vajra/ui/screen/main/MainScreen.kt`
- `app/src/main/java/com/astro/vajra/ui/navigation/Navigation.kt`
- `app/src/main/java/com/astro/vajra/ui/viewmodel/ChartViewModel.kt`
- `CONTINUITY.md`
- Commands:
  - `git status --short`
  - `git diff -- <files>`
  - `git add <files>`
  - `git commit`
  - `git push`
