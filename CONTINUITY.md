## Goal (incl. success criteria):
Fix recurring `length=13; index=13` runtime failures, unblock transit screen loading, and prevent stale error toast recurrence after back navigation; then commit and push.

## Constraints/Assumptions:
- User requested direct fix + push.
- Skip tests and Gradle/`gradlew` commands.
- Preserve existing chart behavior while handling legacy/invalid stored timezone strings safely.

## Key Decisions:
- Add centralized timezone sanitizer/parser with safe fallback to avoid parser edge-case crashes from malformed/legacy timezone strings.
- Normalize timezone values at repository mapping boundaries so loaded charts use valid timezone IDs.
- Stop infinite loading in transit screen by handling analysis exceptions explicitly with retry UI.
- Reset chart UI error state when leaving chart input and after showing error snackbar to avoid repeated stale toasts.

## State:
- Done:
  - Added `TimezoneSanitizer` utility and integrated it in chart repository, transit analyzer, chart input, and Swiss engine timezone resolution.
  - Added explicit transit analysis error state + retry in `TransitsScreenRedesigned`.
  - Added state reset hooks in navigation callbacks and main-screen error snackbar handling.
  - Added chart-input error-dialog reset behavior for runtime (non-validation) errors.
- Now:
  - Final diff review, commit, and push.
- Next:
  - Ask user to verify profile edit no-change save, transit load, and back navigation behavior.

## Open Questions (UNCONFIRMED if needed):
- UNCONFIRMED: exact low-level source of `StringIndexOutOfBoundsException(length=13,index=13)` in prior runtime path (guarded via timezone sanitization/fallback).

## Working Set (files/ids/commands):
- `app/src/main/java/com/astro/storm/util/TimezoneSanitizer.kt`
- `app/src/main/java/com/astro/storm/data/repository/ChartRepository.kt`
- `app/src/main/java/com/astro/storm/ephemeris/SwissEphemerisEngine.kt`
- `app/src/main/java/com/astro/storm/ephemeris/TransitAnalyzer.kt`
- `app/src/main/java/com/astro/storm/ui/screen/transits/TransitsScreenRedesigned.kt`
- `app/src/main/java/com/astro/storm/ui/screen/ChartInputScreen.kt`
- `app/src/main/java/com/astro/storm/ui/screen/main/MainScreen.kt`
- `app/src/main/java/com/astro/storm/ui/navigation/Navigation.kt`
- `CONTINUITY.md`
- Commands:
  - `git status --short`
  - `git diff -- <files>`
  - `git add <files>`
  - `git commit`
  - `git push`
