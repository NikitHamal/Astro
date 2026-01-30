# AstroStorm Research - Continuity Ledger
> Last updated: 2026-01-30T08:17:12Z

## Goal (incl. success criteria)
Extend yoga analysis to 500+ verified yogas with accurate detection and high-quality descriptions; ensure production-grade modular implementation and Vedic accuracy.

## Constraints/Assumptions
- Production-grade quality required (no TODOs, basic implementations)
- Must use best possible methods, modularization, accurate Vedic astrology
- Verify against classical texts (BPHS, Phaladeepika, Jataka Parijata)
- Build instructions: never build; verify mentally

## Key Decisions
- None yet for implementation approach

## State

- Done:
  - Prior research pass documented in `FINDINGS.md`
  - Reviewed yoga engine orchestration in `YogaCalculator` and core yoga models
  - Reviewed localization + string key mapping approach for yoga names/effects

- Now:
  - Assess feasibility for 500+ yoga expansion and required data sources

- Next:
  - Propose modular data model and extensibility plan for 500+ yogas
  - Confirm authoritative sources and target language coverage for yoga descriptions

## Open Questions (UNCONFIRMED if needed)
- Which classical sources should be treated as authoritative for the 500+ yoga list and effect descriptions?
- Is there an existing canonical yoga data source in this repo or external references to use?
- Should descriptions be English-only or include Nepali/localized equivalents?

## Working Set (files/ids/commands)
- `app/src/main/java/com/astro/storm/ephemeris/yoga/YogaModels.kt`
- `app/src/main/java/com/astro/storm/ephemeris/yoga/YogaLocalization.kt`
- `app/src/main/java/com/astro/storm/ephemeris/YogaCalculator.kt`
- `core/common/src/main/java/com/astro/storm/core/common/StringKeyYogaExpanded.kt`
