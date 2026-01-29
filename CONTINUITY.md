## Goal (incl. success criteria):
Implement and fully integrate NEXT.md items 1-5: Triple-Pillar Predictive Engine, BNN Aspect Engine, KP 4-Step (Cusp Sub-Lords), Muhurta Optimization Search, and Ishta Devata + Beeja Mantra generator, all production-grade and Vedic-accurate.

## Constraints/Assumptions:
- Never build; verify mentally only.
- No TODOs or simplified logic; production-grade only.
- Modularization: keep files ~500-800 LOC max.
- Follow existing architecture (Kotlin, MVVM, Compose) and offline-first constraints.

## Key Decisions:
- UNCONFIRMED: None yet for new features.

## State:
- Done:
  - Read `NEXT.md` and task requirements.
  - Added Triple-Pillar engine, BNN aspect engine, KP system engine, and Ishta Devata/Beeja mantra generator.
  - Integrated Triple-Pillar into Deep Predictions, BNN into Nadi Amsha, KP into navigation, and Muhurta 5-minute optimization.
- Now:
  - Wiring KP screen and remaining UI/localization adjustments; checking for compile-level issues.
- Next:
  - Add missing localization keys for new UI labels where needed.
  - Review KP/BNN UI text and ensure minimal hardcoding.

## Open Questions (UNCONFIRMED if needed):
- Which screens or flows should surface each new feature first?
- Any specific Vedic sources/variants preferred for KP and BNN rules in this app?

## Working Set (files/ids/commands):
- `NEXT.md`
- `app/src/main/java/com/astro/storm/ephemeris/prediction/TriplePillarPredictiveEngine.kt`
- `app/src/main/java/com/astro/storm/ephemeris/bnn/BnnAspectEngine.kt`
- `app/src/main/java/com/astro/storm/ephemeris/kp/KpSystemEngine.kt`
- `app/src/main/java/com/astro/storm/ephemeris/remedy/IshtaDevataCalculator.kt`
- `app/src/main/java/com/astro/storm/ui/screen/KpSystemScreen.kt`
