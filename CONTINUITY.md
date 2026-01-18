# AstroStorm Overhaul - Continuity Ledger
> Last updated: 2026-01-18T14:30:00+05:45

## Goal (incl. success criteria)
Implement advanced Vedic astrology features and refactor codebase for production-grade quality.
1. Modularize core logic into :core:common and :core:model ✅
2. Refactor all 1000+ line monoliths into maintainable packages ✅
3. Improve Shadbala precision (D60, BPHS values) ✅
4. Implement persistent calculation settings (Node, Ayanamsa) ✅
5. Enhance PDF export quality with vector rendering ✅
6. **NEW** Add caching infrastructure to CPU-intensive calculators ✅
7. **NEW** Fix mathematical precision issues in D-150 and Yogini Dasha ✅
8. **NEW** Complete yoga evaluator suite (Parivartana, Viparita Raja) ✅

## Constraints/Assumptions
- Follow existing patterns
- Zero hardcoded text where possible
- Vedic accuracy per classical texts (BPHS, Jaimini Sutras)
- Production-grade: NO TODOs, complete implementations only

## State

- Done (Previous Sessions):
  - Created :core:common and :core:model modules ✅
  - Moved all core models and localization keys to core modules ✅
  - Refactored Prashna, Remedies, Varshaphala, Muhurta, Varga, NativeAnalysis, and Shoola calculators ✅
  - Updated Shadbala to Tradition B (D60) with precise BPHS values ✅
  - Implemented persistent Ayanamsa and Node settings in AstrologySettingsManager ✅
  - Updated SwissEphemerisEngine to use dynamic settings ✅
  - Enhanced ChartExporter to use vector-based rendering for charts ✅
  - Established unit test foundation for Panchanga logic ✅

- Done (Session 2026-01-18):
  1. **FINDINGS.md Created** - Comprehensive audit of codebase issues documented
  2. **DivisionalChartCalculator Fixed** - isOddSign naming consistency and logic corrected
  3. **KakshaTransitCalculator Fixed** - 8th Kaksha (Lagna) now properly handled
  4. **YoginiDashaCalculator Fixed** - Starting Yogini formula corrected per Jataka Parijata
  5. **ShoolaDashaCalculator Refactored** - Expanded minified code with full documentation
  6. **ParivartanaYogaEvaluator Registered** - Added to YogaCalculator evaluator chain
  7. **Viparita Raja Yoga** - Detection logic added in dusthana exchange handling
  8. **AshtakavargaCalculator Caching** - LRU cache with 50-entry limit, 30-min expiration
  9. **NadiAmshaCalculator Precision** - floor() instead of toInt(), second-level rectification
  10. **Shoola Dasha Documented** - Comprehensive Savya/Apasavya direction logic documented

- Now:
  - All 10+ high-impact changes from session completed
  - CONTINUITY.md updated with progress

- Next:
  - Further localization of remaining hardcoded strings in minor components
  - Expansion of unit tests to cover all 16 divisional charts
  - Implementation of Transit Alert background service (from AI.md)
  - Add Result type error handling to calculators for better error propagation
  - Consider adding caching to other CPU-intensive calculators (DivisionalChartCalculator)

## Working Set (files/ids/commands)

### Core Calculator Files Modified:
- `ephemeris/AshtakavargaCalculator.kt` - Added LRU caching infrastructure
- `ephemeris/NadiAmshaCalculator.kt` - Enhanced precision, second-level rectification
- `ephemeris/YoginiDashaCalculator.kt` - Fixed starting Yogini formula
- `ephemeris/shoola/ShoolaDashaCalculator.kt` - Comprehensive documentation added

### Yoga System Files:
- `ephemeris/YogaCalculator.kt` - Registered ParivartanaYogaEvaluator
- `ephemeris/yoga/ParivartanaYogaEvaluator.kt` - Complete BPHS Ch.32 implementation
- `ephemeris/yoga/YogaHelpers.kt` - Shared utilities for yoga calculations

### Documentation:
- `FINDINGS.md` - Comprehensive audit results
- `CONTINUITY.md` - Session ledger (this file)

## Key Technical Decisions

### Caching Strategy (AshtakavargaCalculator)
- **Key**: Chart hash based on ascendant + planet positions (4 decimal precision)
- **Cache Size**: 50 entries max (LRU eviction)
- **Expiration**: 30 minutes
- **Thread Safety**: ConcurrentHashMap + AtomicLong for counters
- **Bypass Option**: `calculateAshtakavarga(chart, bypassCache = true)`

### Nadi Amsha Precision
- Using `floor()` instead of `toInt()` for proper segment calculation
- Second-level precision for rectification candidates
- Precision warnings when birth time uncertainty > 5 Nadis

### Shoola Dasha Direction
- Odd signs (1,3,5,7,9,11): Savya (Direct/Clockwise)
- Even signs (2,4,6,8,10,12): Apasavya (Reverse/Counter-clockwise)
- Starting sign determined by Rudra planet position
