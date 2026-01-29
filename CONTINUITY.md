# AstroStorm Overhaul - Continuity Ledger
> Last updated: 2026-01-29T15:30:00+05:45

## Goal (incl. success criteria)
Implement three advanced Vedic astrology predictive engines:
1. **Triple-Pillar Predictive Engine** - Synthesize Vimshottari Dasha + Gochara (Transits) + Ashtakavarga for "Success Probability" timelines
2. **Bhrigu Nandi Nadi (BNN) Aspect Engine** - Implement Nadi aspects (1,5,9,2,12,7) with recursive planetary link graph traversal
3. **KP System (Krishnamurti Padhdhati)** - Calculate Cusp Sub-Lords with 4-Step Theory for binary event verification

Success criteria:
- All implementations must be production-grade, fully functional, and Vedically accurate
- Modular code (500-800 lines per file max)
- Full UI integration with screens, ViewModels, and navigation
- Multi-language localization support

## Constraints/Assumptions
- Follow existing MVVM architecture patterns
- Use existing Swiss Ephemeris engine for calculations
- Build on existing Vimshottari, Ashtakavarga, Transit calculators
- Zero hardcoded text - use localization keys
- No TODOs or placeholder code - everything production-ready

## Key Decisions
- Triple-Pillar: Create new synthesis engine combining existing calculators
- BNN: New dedicated module ignoring Lagna, focusing on planetary yogas by consecutive signs
- KP: Implement 249 zodiac divisions for sub-lord determination

## State

- Done:
  - Comprehensive codebase exploration completed
  - Identified all existing calculation engines and integration points
  - Located key files: DashaCalculator, AshtakavargaCalculator, TransitAnalyzer, AspectCalculator
  - **Triple-Pillar Predictive Engine - CORE COMPLETE:**
    - `TriplePillarModels.kt` - All data models, enums, config classes (~535 lines)
    - `TriplePillarEngine.kt` - Main synthesis engine (~800 lines)
    - `DashaLordAnalyzer.kt` - Dasha lord combination analysis (~450 lines)

- Now:
  - Starting BNN Aspect Engine implementation

- Next:
  - KP System implementation
  - UI screens and ViewModels for all three systems
  - Navigation integration
  - Localization strings

## Open Questions (UNCONFIRMED if needed)
- None currently

## Working Set (files/ids/commands)
- **Existing Calculators to build upon:**
  - `/app/src/main/java/com/astro/storm/ephemeris/DashaCalculator.kt`
  - `/app/src/main/java/com/astro/storm/ephemeris/AshtakavargaCalculator.kt`
  - `/app/src/main/java/com/astro/storm/ephemeris/TransitAnalyzer.kt`
  - `/app/src/main/java/com/astro/storm/ephemeris/AspectCalculator.kt`
- **Models to extend:**
  - `/core/model/src/main/java/com/astro/storm/core/model/`
- **New files to create:**
  - `/app/src/main/java/com/astro/storm/ephemeris/triplepillar/` (new package)
  - `/app/src/main/java/com/astro/storm/ephemeris/bnn/` (new package)
  - `/app/src/main/java/com/astro/storm/ephemeris/kpsystem/` (new package)
