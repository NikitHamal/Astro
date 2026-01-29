# AstroStorm - Comprehensive Code Audit and Research Findings

> **Analysis Date:** January 29, 2026  
> **Analyst:** Automated Code Audit System  
> **Scope:** Complete Vedic Astrology Calculation Engine Analysis

---

## Table of Contents
1. [Executive Summary](#executive-summary)
2. [Architecture Overview](#architecture-overview)
3. [Mathematical and Astronomical Precision Issues](#mathematical-and-astronomical-precision-issues)
4. [Vedic Astrology Accuracy Issues](#vedic-astrology-accuracy-issues)
5. [Code Quality Issues](#code-quality-issues)
6. [Performance Considerations](#performance-considerations)
7. [Modularization Analysis](#modularization-analysis)
8. [Critical Bug Reports](#critical-bug-reports)
9. [Recommendations](#recommendations)

---

## Executive Summary

AstroStorm is a comprehensive Vedic astrology application with solid foundational architecture. The codebase demonstrates good modularization with `:core:common` and `:core:model` modules, proper use of Swiss Ephemeris, and extensive coverage of Vedic astrology concepts including Dashas, Vargas, Yogas, Shadbala, Matchmaking, and more.

**Overall Assessment:** GOOD with areas requiring improvement

**Strengths:**
- Comprehensive Swiss Ephemeris integration with proper thread safety
- Well-structured divisional chart calculations (23 varga charts)
- Proper modularization and separation of concerns
- Extensive localization support (English/Nepali)
- Solid Vimshottari Dasha implementation with all 6 levels

**Areas Requiring Attention:**
- Several Vedic astrology calculations need verification against classical texts
- Some mathematical edge cases not handled
- Performance optimization opportunities in heavy calculations
- Incomplete implementations in some advanced features

---

## Architecture Overview

### Module Structure
```
app/
├── src/main/java/com/astro/storm/
│   ├── ephemeris/           # All calculation engines
│   │   ├── deepanalysis/    # Character, Career, Health analysis
│   │   ├── muhurta/         # Auspicious timing calculations
│   │   ├── prashna/         # Horary astrology
│   │   ├── remedy/          # Remedies system
│   │   ├── shoola/          # Shoola Dasha system
│   │   ├── varga/           # Divisional chart analyzers
│   │   └── yoga/            # Yoga evaluators
│   ├── data/                # Data layer
│   └── ui/                  # UI components
core/
├── common/                  # Localization and utilities
└── model/                   # Data models
```

### Key Components
1. **SwissEphemerisEngine** (`ephemeris/SwissEphemerisEngine.kt:53-631`)
   - Singleton pattern with thread-safe initialization
   - LRU cache for chart calculations
   - Dynamic Ayanamsa selection
   - Support for both Mean and True Node calculations

2. **DashaCalculator** (`ephemeris/DashaCalculator.kt:26-1179`)
   - Full 6-level Vimshottari Dasha implementation
   - BigDecimal precision for calculations
   - Dasha Sandhi (transition periods) calculation

3. **ShadbalaCalculator** (`ephemeris/ShadbalaCalculator.kt:215-978`)
   - Complete six-fold strength calculation
   - BPHS-compliant values

---

## Mathematical and Astronomical Precision Issues

### ISSUE M-001: Floating Point Precision in Divisional Charts
**Location:** `ephemeris/DivisionalChartCalculator.kt`  
**Severity:** MEDIUM  
**Description:** Several divisional chart calculations use floating-point division without proper rounding, which can lead to off-by-one errors at sign boundaries.

**Example at line 688-691:**
```kotlin
private fun getDivisionPart(degreeInSign: Double, divisionSize: Double, maxParts: Int): Int {
    val part = floor(degreeInSign / divisionSize).toInt()
    return part.coerceIn(0, maxParts - 1)
}
```

**Issue:** When `degreeInSign` is exactly at a boundary (e.g., 3.33333... for Navamsa), floating-point precision can cause the planet to be placed in the wrong division.

**Recommended Fix:**
```kotlin
private fun getDivisionPart(degreeInSign: Double, divisionSize: Double, maxParts: Int): Int {
    // Add small epsilon to handle floating point boundary issues
    val epsilon = 1.0e-10
    val adjustedDegree = degreeInSign.coerceIn(0.0, 30.0 - epsilon)
    val part = floor(adjustedDegree / divisionSize).toInt()
    return part.coerceIn(0, maxParts - 1)
}
```

---

### ISSUE M-002: Julian Day Calculation Nanosecond Precision
**Location:** `ephemeris/SwissEphemerisEngine.kt:531-546`  
**Severity:** LOW  
**Description:** The Julian Day calculation includes nanoseconds, but the divisor may cause precision loss.

**Current Code:**
```kotlin
val decimalHours = utcDateTime.hour +
        (utcDateTime.minute / 60.0) +
        (utcDateTime.second / 3600.0) +
        (utcDateTime.nano / 3_600_000_000_000.0)
```

**Note:** This is technically correct but could benefit from using BigDecimal for sub-second precision in critical birth time rectification scenarios.

---

### ISSUE M-003: Shoola Dasha Sign Duration
**Location:** `ephemeris/shoola/ShoolaDashaCalculator.kt:20`  
**Severity:** HIGH  
**Description:** Shoola Dasha uses a fixed 9-year period per sign which is a simplification.

**Current Code:**
```kotlin
private const val SIGN_DASHA_YEARS = 9.0
```

**Vedic Reference Issue:** According to classical texts like Jaimini Sutras, Shoola Dasha periods should be calculated based on:
1. The distance of the sign from the Lagna or
2. The Chara Karakas involved

The 9-year fixed period is a simplified interpretation. For production-grade accuracy:
- Implement variable periods based on sign characteristics (Movable/Fixed/Dual)
- Consider Nakshatra Shoola variant for health timing

---

### ISSUE M-004: Nadi Amsha (D-150) Calculation Method
**Location:** `ephemeris/NadiAmshaCalculator.kt:100-117`  
**Severity:** HIGH  
**Description:** The D-150 calculation uses a simplified cyclic counting method instead of the authentic Nadi Amsha methodology.

**Current Code:**
```kotlin
val nadiSignIndex = (signIndex + nadiNumber - 1) % 12
val nadiSign = ZodiacSign.entries[nadiSignIndex]
```

**Issue:** This is a placeholder implementation. True Nadi Amsha requires:
1. Specific 150-part names from classical texts (Chandra Kala Nadi)
2. Deha-Jiva pairs for each Nadi
3. Lord assignments that don't follow simple cyclic patterns

**Recommended Action:** Research and implement authentic Nadi Amsha tables from:
- Chandra Kala Nadi
- Dhruva Nadi
- Satyanadi

---

### ISSUE M-005: Ayana Bala Declination Calculation
**Location:** `ephemeris/ShadbalaCalculator.kt:789-821`  
**Severity:** LOW  
**Description:** The Ayana Bala calculation correctly converts to tropical longitude but uses a fixed obliquity value.

**Current Code:**
```kotlin
val obliquity = 23.44
```

**Enhancement:** Earth's obliquity varies slightly over time (Nutation). For maximum precision:
```kotlin
// Calculate current obliquity based on Julian century
val T = (julianDay - 2451545.0) / 36525.0
val obliquity = 23.439291 - 0.013004 * T - 0.00000016 * T * T + 0.00000050 * T * T * T
```

---

## Vedic Astrology Accuracy Issues

### ISSUE V-001: Moolatrikona Ranges Discrepancy
**Location:** `ephemeris/ShadbalaCalculator.kt:266-274`  
**Severity:** MEDIUM  
**Description:** There's a discrepancy in Mercury's Moolatrikona range between ShadbalaCalculator and AstrologicalConstants.

**ShadbalaCalculator (line 270):**
```kotlin
Planet.MERCURY to Range(ZodiacSign.VIRGO, 15.0, 20.0)
```

**AstrologicalConstants (line 256):**
```kotlin
Planet.MERCURY to MoolatrikonaRange(ZodiacSign.VIRGO, 16.0, 20.0)
```

**Resolution:** Per BPHS Chapter 3, Mercury's Moolatrikona is 15°-20° Virgo. The ShadbalaCalculator is correct; update AstrologicalConstants to match.

---

### ISSUE V-002: Varna Koot Nakshatra Assignments
**Location:** `ephemeris/VedicAstrologyUtils.kt:587-615`  
**Severity:** MEDIUM  
**Description:** Several Nakshatra Varna assignments don't match standard texts.

**Examples of potential discrepancies:**
- ASHWINI: Listed as VAISHYA - Some texts assign KSHATRIYA (being ruled by Ashwini Kumaras, divine physicians)
- PUSHYA: Listed as KSHATRIYA - Some texts assign BRAHMIN (ruled by Brihaspati)

**Recommended Action:** Cross-reference with:
- Brihat Samhita
- Muhurta Chintamani
- Regional traditions (North Indian vs South Indian)

Consider adding a configuration option for different regional traditions.

---

### ISSUE V-003: Incomplete Rajju Nakshatra Mapping
**Location:** `ephemeris/VedicAstrologyUtils.kt:637-674`  
**Severity:** LOW  
**Description:** The Rajju (body part) Nakshatra mapping is incomplete - only 21 of 27 Nakshatras are explicitly mapped.

**Missing Nakshatras in explicit mapping:**
- Some entries may fall through to the default case

**Recommendation:** Ensure all 27 Nakshatras are explicitly mapped to prevent any edge-case issues.

---

### ISSUE V-004: Trimsamsa Calculation Method
**Location:** `ephemeris/DivisionalChartCalculator.kt:438-472`  
**Severity:** LOW  
**Description:** The Trimsamsa (D30) calculation implements the Parashari method correctly. However, some practitioners use the Jaimini method which differs.

**Current implementation:** BPHS Parashari Trimsamsa (Mars 5°, Saturn 5°, Jupiter 8°, Mercury 7°, Venus 5° for odd signs)

**Enhancement:** Consider adding support for:
1. Jaimini Trimsamsa (equal 5° divisions)
2. User preference selection between methods

---

### ISSUE V-005: Missing Drishti Bala Fractional Aspects
**Location:** `ephemeris/ShadbalaCalculator.kt:883-906`  
**Severity:** MEDIUM  
**Description:** The Drik Bala calculation doesn't implement the full Parashari graded aspects.

**Current Code:**
```kotlin
val aspectStrength = VedicAspects.getAspectStrength(planet, houseDiff)
```

**Issue:** Only considers full and special aspects. Per BPHS, all planets have fractional aspects:
- 3rd and 10th houses: 1/4 strength
- 4th and 8th houses: 3/4 strength (except Mars full on 4th and 8th)
- 5th and 9th houses: 1/2 strength (except Jupiter full)
- 7th house: Full strength (all planets)

**Recommended Fix:** Implement complete graded aspect system in `VedicAspects.getAspectStrength()`:
```kotlin
fun getAspectStrength(planet: Planet, houseDifference: Int): Double {
    // Full aspect on 7th for all
    if (houseDifference == 7) return 1.0
    
    // Check special aspects first
    specialAspects[planet]?.find { it.house == houseDifference }?.let { return it.strength }
    
    // Graded aspects per BPHS
    return when (houseDifference) {
        3, 10 -> 0.25  // Quarter aspect
        5, 9 -> 0.5    // Half aspect
        4, 8 -> 0.75   // Three-quarter aspect
        else -> 0.0
    }
}
```

---

### ISSUE V-006: Kalachakra Dasha Navamsa Grouping
**Location:** `ephemeris/KalachakraDashaCalculator.kt`  
**Severity:** MEDIUM  
**Description:** Review required to ensure the Kalachakra Dasha implementation correctly handles:
1. Savya (clockwise) vs Apasavya (anti-clockwise) movement
2. Deha-Jiva nakshatra classification
3. Jump conditions at cardinal nakshatras

**Recommended Action:** Verify against:
- Jataka Parijata Chapter 14
- Brihat Parashara Hora Shastra Chapter 52

---

### ISSUE V-007: Ashtakavarga Sarvashtakavarga Reduction
**Location:** `ephemeris/AshtakavargaCalculator.kt`  
**Severity:** LOW  
**Description:** Verify that the Trikona Shodhana and Ekadhipatya Shodhana reductions are implemented if the app displays Prastara Ashtakavarga.

---

## Code Quality Issues

### ISSUE C-001: Duplicate Moolatrikona Definitions
**Location:** Multiple files  
**Severity:** LOW  
**Description:** Moolatrikona data is defined in both:
- `ShadbalaCalculator.MoolatrikonaData` (lines 248-280)
- `AstrologicalConstants.MOOLATRIKONA_RANGES` (lines 252-260)

**Recommendation:** Remove the duplicate from `ShadbalaCalculator` and use `AstrologicalConstants` as the single source of truth.

---

### ISSUE C-002: PanchangaCalculator Creates New SwissEph Instance
**Location:** `ephemeris/PanchangaCalculator.kt:25`  
**Severity:** MEDIUM  
**Description:** PanchangaCalculator creates its own SwissEph instance instead of using the singleton SwissEphemerisEngine.

**Current Code:**
```kotlin
private val swissEph: SwissEph = SwissEph()
```

**Issues:**
1. Multiple SwissEph instances may cause resource contention
2. Inconsistent Ayanamsa settings if not synchronized
3. Ephemeris path set separately

**Recommended Fix:** Inject or use `SwissEphemerisEngine.getInstance()` to ensure consistent configuration.

---

### ISSUE C-003: Missing Error Handling in Yoga Evaluators
**Location:** `ephemeris/YogaCalculator.kt:81-89`  
**Severity:** LOW  
**Description:** Error handling in yoga evaluation silently logs and continues, which is good for robustness but may hide calculation issues.

**Current Code:**
```kotlin
try {
    val yogas = evaluator.evaluate(chart)
    allYogas.addAll(yogas)
    yogasByCategory[evaluator.category]?.addAll(yogas)
} catch (e: Exception) {
    android.util.Log.e("YogaCalculator", "Error in ${evaluator.category}: ${e.message}")
}
```

**Recommendation:** Consider adding debug-mode detailed logging or error accumulation for diagnostic purposes.

---

### ISSUE C-004: Hard-coded English Strings in Shoola Dasha
**Location:** `ephemeris/shoola/ShoolaDashaCalculator.kt:52`  
**Severity:** LOW  
**Description:** Some strings are still hardcoded instead of using localization.

**Current Code:**
```kotlin
"${s.displayName} period", "${s.displayName} अवधि"
```

**Recommendation:** Use StringResources consistently.

---

### ISSUE C-005: Thread Safety in ShadbalaCalculator ChartContext
**Location:** `ephemeris/ShadbalaCalculator.kt:345-397`  
**Severity:** LOW  
**Description:** The `ChartContext` class uses `by lazy` for several computed properties which is thread-safe, but the `PanchangaCalculator` instantiation within could cause issues.

**Current Code:**
```kotlin
val panchangaData by lazy {
    PanchangaCalculator(context).use { ... }
}
```

**Note:** The `use` extension properly closes the calculator, but consider caching at a higher level to avoid repeated calculations.

---

## Performance Considerations

### ISSUE P-001: Repeated Divisional Chart Calculations in Shadbala
**Location:** `ephemeris/ShadbalaCalculator.kt:353-359`  
**Severity:** MEDIUM  
**Description:** All divisional charts are calculated for each Shadbala computation.

**Current Code:**
```kotlin
val divisionalCharts: List<DivisionalChartData> by lazy {
    DivisionalChartCalculator.calculateAllDivisionalCharts(chart)
}
```

**Impact:** For Saptavargaja Bala, only 7 vargas are needed (D1, D2, D3, D9, D12, D30, D60), but all 23 are calculated.

**Recommended Fix:**
```kotlin
val saptavargaCharts: List<DivisionalChartData> by lazy {
    listOf(
        DivisionalChartType.D1_RASHI,
        DivisionalChartType.D2_HORA,
        DivisionalChartType.D3_DREKKANA,
        DivisionalChartType.D9_NAVAMSA,
        DivisionalChartType.D12_DWADASAMSA,
        DivisionalChartType.D30_TRIMSAMSA,
        DivisionalChartType.D60_SHASHTIAMSA
    ).map { DivisionalChartCalculator.calculateDivisionalChart(chart, it) }
}
```

---

### ISSUE P-002: Cache Key String Concatenation
**Location:** `ephemeris/SwissEphemerisEngine.kt:269-271`  
**Severity:** LOW  
**Description:** Cache key generation uses string concatenation which creates intermediate objects.

**Current Code:**
```kotlin
private fun getCacheKey(birthData: BirthData, houseSystem: HouseSystem): String {
    return "${birthData.dateTime}_${birthData.latitude}_${birthData.longitude}_${birthData.timezone}_${houseSystem.name}_$ayanamsaType"
}
```

**Recommendation:** Use data class as cache key for better performance and equals/hashCode semantics:
```kotlin
data class CacheKey(
    val dateTime: LocalDateTime,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val houseSystem: HouseSystem,
    val ayanamsa: AyanamsaType
)
```

---

### ISSUE P-003: Dasha Calculation Depth Optimization
**Location:** `ephemeris/DashaCalculator.kt:553-606`  
**Severity:** LOW  
**Description:** The Dasha calculation always computes sub-periods for the current Mahadasha even when not needed.

**Enhancement:** Add lazy loading pattern:
```kotlin
enum class CalculationDepth {
    MAHADASHA_ONLY,
    WITH_ANTARDASHA,
    WITH_PRATYANTARDASHA,
    WITH_SOOKSHMADASHA,
    FULL
}
```
This is already defined but not used in the main calculation function.

---

### ISSUE P-004: Matchmaking Calculator Single-Pass Optimization
**Location:** `ephemeris/MatchmakingCalculator.kt`  
**Severity:** LOW  
**Description:** The matchmaking calculation extracts Moon position multiple times.

**Example:**
```kotlin
val brideMoon = brideChart.planetPositions.find { it.planet == Planet.MOON }
// ... later ...
val brideMoon = brideChart.planetPositions.find { it.planet == Planet.MOON }
```

**Recommendation:** Cache planet positions at the start of calculation.

---

## Modularization Analysis

### Positive Findings
1. **Core Modules** (`:core:common`, `:core:model`) properly separate shared logic
2. **Calculator Strategy Pattern** in YogaCalculator with separate evaluators
3. **Proper Dependency Injection** setup with Hilt
4. **Clean separation** between UI (viewmodel/) and business logic (ephemeris/)

### Areas for Improvement

#### ISSUE MOD-001: Ephemeris Package Too Large
**Description:** The `ephemeris/` package contains 80+ files. Consider sub-packaging by domain:

**Recommended Structure:**
```
ephemeris/
├── core/           # SwissEphemerisEngine, VedicAstrologyUtils
├── dasha/          # All dasha systems
├── strength/       # Shadbala, Ashtakavarga
├── compatibility/  # Matchmaking, Guna Milan
├── timing/         # Muhurta, Panchanga
├── yoga/           # Already separated
├── varga/          # Already separated
├── prashna/        # Already separated
└── transit/        # Transit calculations
```

---

#### ISSUE MOD-002: Feature Module Candidates
**Description:** Consider extracting feature modules for:
1. `:feature:matchmaking` - Kundali matching
2. `:feature:dasha` - All dasha systems
3. `:feature:muhurta` - Auspicious timing
4. `:feature:export` - PDF/chart export

Benefits:
- Faster incremental builds
- Better separation of concerns
- Potential for dynamic feature delivery

---

## Critical Bug Reports

### BUG-001: Odd Sign Detection in Divisional Charts
**Location:** `ephemeris/DivisionalChartCalculator.kt:684-686`  
**Severity:** HIGH  
**Description:** The `isOddSign` function checks if sign number is even (0-indexed) but this logic is inverted.

**Current Code:**
```kotlin
private fun isOddSign(signNumber: Int): Boolean {
    return signNumber % 2 == 0  // Returns true for 0, 2, 4... which are Aries, Gemini, Leo (odd signs)
}
```

**Analysis:** Actually this IS correct because:
- signNumber 0 = Aries (1st sign, odd)
- signNumber 1 = Taurus (2nd sign, even)
- signNumber 2 = Gemini (3rd sign, odd)

So the function works correctly despite the confusing naming. The 0-indexed sign number's evenness corresponds to odd zodiac signs.

**Status:** FALSE POSITIVE - Code is correct

---

### BUG-002: Tribhaga Bala Night Calculation Issue
**Location:** `ephemeris/ShadbalaCalculator.kt:718-751`  
**Severity:** MEDIUM  
**Description:** The night Tribhaga calculation may make redundant Panchanga calls.

**Current Code:**
```kotlin
val (nightStart, nightEnd) = if (birthJD < sunriseJD) {
    val prevSunsetJD = PanchangaCalculator(context.context).use { ... }
    Pair(prevSunsetJD, sunriseJD)
} else {
    val nextSunriseJD = PanchangaCalculator(context.context).use { ... }
    Pair(sunsetJD, nextSunriseJD)
}
```

**Issue:** Creates new PanchangaCalculator instances for boundary cases, which:
1. Is slow (creates new SwissEph)
2. May not use consistent settings

**Recommended Fix:** Cache previous/next day Panchanga data or pass as parameter.

---

## Recommendations

### Priority 1 - Critical (Implement Immediately)

1. **Nadi Amsha Enhancement** - Implement authentic D-150 calculation from classical texts
2. **Fractional Aspects** - Add graded aspects to Drik Bala calculation
3. **Shoola Dasha Variable Periods** - Implement proper Jaimini-based period calculation
4. **PanchangaCalculator Singleton** - Use shared SwissEphemerisEngine instance

### Priority 2 - High (Next Sprint)

1. **Divisional Chart Boundary Handling** - Add epsilon adjustment for floating-point boundaries
2. **Moolatrikona Single Source** - Remove duplicate definitions, use AstrologicalConstants
3. **Saptavarga Optimization** - Only calculate needed vargas for Shadbala
4. **Mercury Moolatrikona Fix** - Update AstrologicalConstants to 15°-20°

### Priority 3 - Medium (Backlog)

1. **Regional Tradition Support** - Add configuration for North/South Indian traditions
2. **Jaimini Trimsamsa Option** - Add alternative D30 calculation method
3. **Package Restructuring** - Break up large ephemeris package
4. **Cache Key Optimization** - Use data class instead of string concatenation
5. **Complete Rajju Mapping** - Ensure all 27 Nakshatras explicitly mapped

### Priority 4 - Low (Future Enhancement)

1. **Dynamic Obliquity** - Calculate actual obliquity for Ayana Bala
2. **Feature Modules** - Extract into separate Gradle modules
3. **Kalachakra Verification** - Verify Savya/Apasavya implementation
4. **Ashtakavarga Reductions** - Implement Shodhana if not present

---

## Appendix: Files Analyzed

| File | Lines | Purpose |
|------|-------|---------|
| SwissEphemerisEngine.kt | 632 | Core ephemeris calculations |
| DashaCalculator.kt | 1179+ | Vimshottari Dasha system |
| ShadbalaCalculator.kt | 981 | Six-fold planetary strength |
| PanchangaCalculator.kt | 135 | Daily Panchanga elements |
| DivisionalChartCalculator.kt | 1043 | All 23 varga charts |
| VedicAstrologyUtils.kt | 1051 | Shared utilities |
| AstrologicalConstants.kt | 516 | Centralized constants |
| MatchmakingCalculator.kt | 712 | Kundali matching |
| YogaCalculator.kt | 234 | Yoga orchestration |
| NadiAmshaCalculator.kt | 191 | D-150 calculations |
| ShoolaDashaCalculator.kt | 84 | Shoola Dasha system |

---

*This report was generated based on static code analysis and Vedic astrology domain knowledge. All recommendations should be validated against specific regional traditions and classical text references before implementation.*
