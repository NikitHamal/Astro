## Goal (incl. success criteria):
Fix Nepali readability regression where UI text appears optically smaller than English in Neo-Vedic screens, and push a production-safe patch without running build/test/gradle commands.

## Constraints/Assumptions:
- User requested direct fix + push.
- Skip tests and Gradle/`gradlew` commands.
- Preserve existing app behavior and navigation/contracts.

## Key Decisions:
- Apply a centralized Nepali-only typography scale via `LocalDensity.fontScale` in app theme so all `sp` text scales consistently without per-screen refactors.
- Remove forced uppercase + high tracking for Nepali in key high-visibility shell/home labels while keeping English styling unchanged.
- Keep patch scope to shared shell/home primitives for immediate perceptual impact and low regression risk.

## State:
- Done:
  - Implemented Nepali readability patch set:
    - `app/src/main/java/com/astro/vajra/ui/theme/Theme.kt`
      - added Nepali-only typography scale (`1.12x`) through `CompositionLocalProvider(LocalDensity ...)`.
    - `app/src/main/java/com/astro/vajra/ui/screen/main/MainScreen.kt`
      - bottom nav labels now avoid uppercase for Nepali, use `PoppinsFontFamily`, and reduced tracking.
    - `app/src/main/java/com/astro/vajra/ui/screen/main/HomeTab.kt`
      - added localized micro-label helpers for case/font/spacing/size.
      - applied to home labels (`Current Maha Dasha`, snapshot card titles, section headers, create-chart CTA).
    - `app/src/main/java/com/astro/vajra/ui/screen/main/SettingsTab.kt`
      - section headers now avoid uppercase for Nepali and switch to `PoppinsFontFamily`.
    - `app/src/main/java/com/astro/vajra/ui/components/common/NeoVedicPrimitives.kt`
      - `NeoVedicSectionDivider` now avoids uppercase in Nepali.
- Now:
  - Commit and push Nepali readability patch.
- Next:
  - Ask user to verify Nepali visual size/spacing in Home/Insights/Settings shell screens.

## Open Questions (UNCONFIRMED if needed):
- None.

## Working Set (files/ids/commands):
- `app/src/main/java/com/astro/vajra/ui/theme/Theme.kt`
- `app/src/main/java/com/astro/vajra/ui/screen/main/MainScreen.kt`
- `app/src/main/java/com/astro/vajra/ui/screen/main/HomeTab.kt`
- `app/src/main/java/com/astro/vajra/ui/screen/main/SettingsTab.kt`
- `app/src/main/java/com/astro/vajra/ui/components/common/NeoVedicPrimitives.kt`
- `CONTINUITY.md`
- Commands:
  - `git status --short`
  - `git diff -- <files>`
  - `git add <files>`
  - `git commit`
  - `git push`
