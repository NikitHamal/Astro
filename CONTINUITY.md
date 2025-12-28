# AstroStorm Continuity Ledger

## Goal (incl. success criteria)
Complete AstroStorm refactoring with three major objectives:
1. **Complete Nepali Localization** - Translate all hardcoded English texts (currently ~45-70% done)
2. **Fix Birth Chart House Number Inconsistency** - Planet positions not matching chart visualization
3. **Codebase Refactoring & Optimization** - Clean up redundancies, dead code, improve performance

Success Criteria:
- 100% user-visible text localized in English and Nepali
- House numbers in chart image match planet details below
- Codebase size reduced, performance improved
- All Vedic astrology calculations remain accurate
- No breaking changes

## Constraints/Assumptions
- Must maintain all existing functionality
- All Vedic astrology calculations must remain accurate per classical texts
- Production-grade, fully functional implementations only
- Use modularization and best practices
- Kotlin/Jetpack Compose codebase
- Cannot break existing features

## Key Decisions
1. Use existing StringResources.kt system for new localizations
2. Investigate house calculation in ChartRenderer.kt vs planet position display
3. Focus on high-impact refactoring first (large files, obvious duplications)

## State

### Done
- Explored full codebase structure (182 Kotlin files, ~164K lines)
- Identified localization architecture (StringResources.kt, LocalizedDisplayNames.kt)
- Located hardcoded English texts in multiple files
- Found birth chart related files (VedicChart.kt, ChartRenderer.kt)
- Identified major calculator files (44 calculators)

### Now
- Investigating birth chart house number inconsistency
- Identifying all hardcoded English texts for localization

### Next
- Fix house number display bug
- Add missing string keys for localization
- Refactor large files (LocalizedDisplayNames.kt - 34K lines)
- Performance optimization
- Dead code removal

## Open Questions
- UNCONFIRMED: Exact mechanism causing house number mismatch
- UNCONFIRMED: Which specific UI screens need hardcoded text extraction

## Working Set
- `StringResources.kt` - 5,740 lines, centralized string keys
- `LocalizedDisplayNames.kt` - 34,470 lines, enum localizations
- `VedicChart.kt` - Birth chart model
- `ChartRenderer.kt` - Chart visualization
- `BirthChartScreen.kt` - Birth chart display screen
- 44 calculator files in `/ephemeris/`
