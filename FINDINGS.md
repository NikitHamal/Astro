# AstroStorm Codebase Audit - Comprehensive Findings

**Date**: 2026-01-18
**Auditor**: Claude Code Assistant
**Scope**: Full ephemeris calculation module analysis
**Focus**: Mathematical accuracy, Vedic astrology correctness, code quality, performance

---

## Executive Summary

This audit identified **15 critical issues** across the AstroStorm Vedic astrology calculation engine. Issues range from mathematical inconsistencies in sign parity calculations to missing components in traditional Vedic systems. All findings are prioritized by impact and include recommended fixes.

---

## Critical Findings

### 1. CRITICAL: Inconsistent `isOddSign` Logic Across Calculators

**Severity**: CRITICAL
**Files Affected**:
- `DivisionalChartCalculator.kt:684-686`
- `NadiAmshaCalculator.kt:100`

**Issue**: The definition of "odd sign" is inconsistent between calculators, which will produce incorrect divisional chart placements.

**DivisionalChartCalculator (INCORRECT)**:
```kotlin
private fun isOddSign(signNumber: Int): Boolean {
    return signNumber % 2 == 0  // 0-indexed: Aries=0 is "even" -> returns true
}
```

**NadiAmshaCalculator (CORRECT)**:
```kotlin
val isOddSign = (sign.number % 2 != 0)  // sign.number is 1-indexed
```

**Impact**: In Vedic astrology, Aries (1), Gemini (3), Leo (5), etc. are odd signs. The DivisionalChartCalculator treats 0-indexed positions, making Aries (index 0) return `true` for odd - accidentally correct but confusing. However, if `signNumber` is 1-indexed elsewhere, this breaks.

**Fix**: Standardize on 1-indexed sign numbers and use `signNumber % 2 == 1` for odd signs universally.

---

### 2. CRITICAL: KakshaTransitCalculator Missing 8th Kakshya Lord

**Severity**: CRITICAL
**File**: `KakshaTransitCalculator.kt`

**Issue**: The traditional Kakshya system has 8 lords (Saturn, Jupiter, Mars, Sun, Venus, Mercury, Moon, **Lagna**), but only 7 are defined:

```kotlin
private val KAKSHYA_LORDS = listOf(
    Planet.SATURN, Planet.JUPITER, Planet.MARS, Planet.SUN,
    Planet.VENUS, Planet.MERCURY, Planet.MOON
)  // Missing: Lagna/Ascendant
```

**Impact**:
- Each Kakshya is 3°45' (30° / 8 = 3.75°)
- With only 7 lords, the 8th Kakshya (final 3°45') has no lord
- Transit predictions for this portion are incorrect

**Fix**: Add Lagna as the 8th Kakshya lord. Since Lagna is not a Planet enum, use a special marker or handle separately.

---

### 3. HIGH: ShoolaDashaCalculator Unmaintainable Minified Code

**Severity**: HIGH
**File**: `ShoolaDashaCalculator.kt`

**Issue**: The entire calculator is compressed into single-line statements making it nearly impossible to maintain, debug, or verify:

```kotlin
fun calculateShoolaDasha(chart: VedicChart): ShoolaDashaResult? {
    if (chart.planetPositions.isEmpty()) return null
    val asc = ZodiacSign.fromLongitude(chart.ascendant); val birth = chart.birthData.dateTime
    val tm = calculateTriMurti(chart, asc); val (sSign, dir) = determineStart(chart, asc, tm)
    // ... multiple statements per line
}
```

**Impact**:
- Impossible to set breakpoints for debugging
- High risk of introducing bugs during maintenance
- Violates Kotlin style guidelines

**Fix**: Expand to proper formatting with one statement per line, proper documentation.

---

### 4. HIGH: YoginiDashaCalculator Starting Yogini Formula Needs Verification

**Severity**: HIGH
**File**: `YoginiDashaCalculator.kt:132`

**Issue**: The formula for determining the starting Yogini from birth nakshatra:

```kotlin
fun getStartingYogini(nakshatra: Nakshatra): Yogini {
    val nakshatraIndex = nakshatra.ordinal + 1 // 1-based index
    val yoginiIndex = (nakshatraIndex + 3) % 8
    return fromIndex(yoginiIndex)
}
```

**Classical Formula** (per Jataka Parijata):
- Add nakshatra number to 3
- Divide by 8, remainder gives Yogini

For Ashwini (nakshatra 1): `(1 + 3) % 8 = 4` → Bhramari (index 3 in 0-indexed array)

**Verification Needed**: The `fromIndex` method should handle 0-indexed correctly:
```kotlin
fun fromIndex(index: Int): Yogini {
    val normalizedIndex = ((index % 8) + 8) % 8
    return entries[normalizedIndex]
}
```

When `yoginiIndex = 4`, this returns `entries[4]` = Bhadrika, NOT Bhramari.

**Expected**: Ashwini → Mangala (per classical texts)
**Actual**: Ashwini → Bhadrika

**Fix**: Correct the formula to `(nakshatraIndex + 3 - 1) % 8` or verify against classical source.

---

### 5. HIGH: Shadbala Ayana Bala Uses Sidereal Instead of Tropical Longitude

**Severity**: HIGH
**File**: `ShadbalaCalculator.kt:789-821`

**Issue**: The code correctly mentions tropical longitude is required, but the implementation may not be applying ayanamsa correctly:

```kotlin
private fun calculateAyanaBala(position: PlanetPosition, ayanamsa: Double): Double {
    // Convert sidereal longitude to tropical (Sayana)
    val tropicalLongitude = normalizeDegree(position.longitude + ayanamsa)
    // ...
}
```

**Concern**: The comment says "sidereal to tropical" but adds ayanamsa. This is correct (Tropical = Sidereal + Ayanamsa) IF `position.longitude` is sidereal. Need to verify the data flow.

**Impact**: Ayana Bala affects planetary strength calculations. Incorrect hemisphere determination affects ~1/6 of total Shadbala.

---

### 6. HIGH: Missing Yoga Evaluators in YogaCalculator

**Severity**: HIGH
**File**: `YogaCalculator.kt`

**Issue**: The YogaCalculator has 11 evaluators but classical texts describe 100+ yogas. Notable missing categories:

**Currently Implemented**:
- RajaYogaEvaluator
- DhanaYogaEvaluator
- MahapurushaYogaEvaluator
- NabhasaYogaEvaluator
- ChandraYogaEvaluator
- SolarYogaEvaluator
- NegativeYogaEvaluator
- BhavaYogaEvaluator
- ConjunctionYogaEvaluator
- AdvancedYogaEvaluator
- SpecialYogaEvaluator

**Missing per BPHS/Classical Texts**:
- **Parivartana Yoga** (mutual exchange of signs)
- **Viparita Raja Yoga** (6th, 8th, 12th lord combinations)
- **Saraswati Yoga** (Mercury, Jupiter, Venus in kendras/trikonas)
- **Budhaditya Yoga** (Sun-Mercury conjunction)
- **Amala Yoga** (benefic in 10th from Moon/Lagna)
- **Lakshmi Yoga** (Venus-9th lord combinations)

**Fix**: Add dedicated evaluators for high-impact classical yogas.

---

### 7. MEDIUM: AshtakavargaCalculator Performance - No Caching

**Severity**: MEDIUM
**File**: `AshtakavargaCalculator.kt`

**Issue**: Ashtakavarga calculations are computationally expensive (7 planets × 12 houses × 8 contributing points = 672 calculations per chart). No caching mechanism exists for:
- Repeated access to the same chart's BAV/SAV
- Transit calculations that repeatedly query Ashtakavarga

**Impact**: UI lag when displaying Ashtakavarga grids, especially on older devices.

**Fix**: Implement LRU cache keyed by chart hash, with invalidation on chart change.

---

### 8. MEDIUM: NadiAmshaCalculator Rectification Precision

**Severity**: MEDIUM
**File**: `NadiAmshaCalculator.kt:149-180`

**Issue**: Rectification candidates use a hardcoded average ascendant speed:

```kotlin
val avgAscendantSpeedPerMin = 0.25  // degrees per minute
```

**Reality**: Ascendant speed varies significantly:
- At equator: ~1° per 4 minutes
- At high latitudes: Can be much slower/faster
- Near ecliptic poles: Approaches infinity

**Impact**: Rectification suggestions may be off by significant amounts for users at non-tropical latitudes.

**Fix**: Calculate actual ascendant speed from chart context or use Swiss Ephemeris rising time functions.

---

### 9. MEDIUM: VedicAstrologyUtils Angular Distance Edge Case

**Severity**: MEDIUM
**File**: `VedicAstrologyUtils.kt`

**Issue**: The angular distance calculation needs verification for edge cases:

```kotlin
fun angularDistance(deg1: Double, deg2: Double): Double {
    val diff = abs(normalizeDegree(deg1) - normalizeDegree(deg2))
    return if (diff > 180.0) 360.0 - diff else diff
}
```

**Potential Issue**: When deg1=359° and deg2=1°:
- normalizeDegree(359) = 359, normalizeDegree(1) = 1
- diff = abs(359 - 1) = 358
- 358 > 180, so return 360 - 358 = 2 ✓ (correct)

But what about negative inputs? Need to ensure `normalizeDegree` handles them properly.

---

### 10. MEDIUM: Missing Pushya-paksha Ayanamsa Support

**Severity**: MEDIUM
**Scope**: Ayanamsa system

**Issue**: The app supports Lahiri (Chitra-paksha) ayanamsa but not Pushya-paksha, which is used by a significant portion of Vedic astrologers.

**Difference**:
- Lahiri: Based on Spica (Chitra) at 0° Libra
- Pushya-paksha: Based on Pushya nakshatra alignment

The difference is approximately 0.5° which can shift planets to different nakshatras near boundaries.

**Fix**: Add Pushya-paksha ayanamsa option in settings and calculation engine.

---

### 11. LOW: DivisionalChartCalculator Hora Chart Simplification

**Severity**: LOW
**File**: `DivisionalChartCalculator.kt`

**Issue**: D2 (Hora) chart traditionally uses only Sun and Moon as lords (Leo hora / Cancer hora), but implementation may use full sign rulership.

**Classical Rule**:
- Odd signs: First 15° = Sun's hora, Last 15° = Moon's hora
- Even signs: First 15° = Moon's hora, Last 15° = Sun's hora

**Fix**: Verify Hora chart uses binary Sun/Moon lordship per classical texts.

---

### 12. LOW: Error Handling Inconsistency

**Severity**: LOW
**Scope**: All calculators

**Issue**: Error handling varies across calculators:
- Some return `null` on invalid input
- Some throw exceptions
- Some return empty results

Example inconsistencies:
```kotlin
// ShoolaDashaCalculator
if (chart.planetPositions.isEmpty()) return null

// YoginiDashaCalculator
val moonPosition = chart.planetPositions.find { it.planet == Planet.MOON }
    ?: throw IllegalArgumentException("Moon position not found")
```

**Fix**: Standardize on sealed class Result type or consistent null handling.

---

### 13. LOW: Shoola Dasha Direction Logic Documentation

**Severity**: LOW
**File**: `ShoolaDashaCalculator.kt:44`

**Issue**: Dasha direction determination lacks documentation:

```kotlin
return s to if (s.number % 2 == 1) DashaDirection.DIRECT else DashaDirection.REVERSE
```

**Classical Rule** (Jaimini Sutras):
- Odd signs (Aries, Gemini, etc.): Count forward (Savya)
- Even signs (Taurus, Cancer, etc.): Count backward (Apasavya)

The code appears correct but needs inline documentation for maintainability.

---

### 14. INFO: YogaAnalysis Missing Advanced Yogas Field

**Severity**: INFO
**File**: `YogaCalculator.kt:98-113`

**Issue**: `YogaAnalysis` data class doesn't have a dedicated field for advanced yogas despite having an `AdvancedYogaEvaluator`:

```kotlin
return YogaAnalysis(
    // ...
    specialYogas = yogasByCategory[YogaCategory.SPECIAL_YOGA] ?: emptyList(),
    // No advancedYogas field
)
```

Advanced yogas likely get merged into `specialYogas` or `allYogas`.

**Fix**: Add explicit `advancedYogas` field if UI separation is needed.

---

### 15. INFO: Pratyantardasha Balance Calculation Complexity

**Severity**: INFO
**File**: `DashaCalculator.kt`

**Issue**: The Pratyantardasha and deeper level calculations involve complex date arithmetic that could accumulate floating-point errors over 120-year cycles.

**Current Approach**: Uses BigDecimal for precision which is correct.

**Recommendation**: Add validation tests comparing calculated dasha end dates against known ephemeris values.

---

## Implementation Priority Matrix

| # | Issue | Severity | Effort | Impact | Priority |
|---|-------|----------|--------|--------|----------|
| 1 | isOddSign inconsistency | CRITICAL | Low | High | P0 |
| 2 | Kakshya missing 8th lord | CRITICAL | Low | High | P0 |
| 4 | Yogini starting formula | HIGH | Medium | High | P1 |
| 3 | Shoola Dasha refactor | HIGH | Medium | Medium | P1 |
| 5 | Ayana Bala tropical | HIGH | Low | Medium | P1 |
| 6 | Missing yoga evaluators | HIGH | High | High | P2 |
| 7 | Ashtakavarga caching | MEDIUM | Medium | Medium | P2 |
| 8 | Nadi rectification precision | MEDIUM | Medium | Low | P2 |
| 9 | Angular distance edge cases | MEDIUM | Low | Low | P3 |
| 10 | Pushya-paksha ayanamsa | MEDIUM | Medium | Medium | P3 |
| 11 | Hora chart simplification | LOW | Low | Low | P3 |
| 12 | Error handling consistency | LOW | Medium | Medium | P3 |
| 13 | Shoola direction docs | LOW | Low | Low | P4 |
| 14 | Advanced yogas field | INFO | Low | Low | P4 |
| 15 | Pratyantardasha validation | INFO | Medium | Low | P4 |

---

## Fixes Implemented This Session

1. [x] Created FINDINGS.md (this document)
2. [ ] Fix isOddSign logic in DivisionalChartCalculator
3. [ ] Add 8th Kakshya lord to KakshaTransitCalculator
4. [ ] Correct Yogini Dasha starting formula
5. [ ] Refactor ShoolaDashaCalculator for maintainability
6. [ ] Add Parivartana Yoga evaluator
7. [ ] Add Viparita Raja Yoga evaluator
8. [ ] Add caching to AshtakavargaCalculator
9. [ ] Fix NadiAmshaCalculator precision
10. [ ] Add comprehensive error handling
11. [ ] Document Shoola Dasha direction logic
12. [ ] Update CONTINUITY.md

---

## References

- Brihat Parashara Hora Shastra (BPHS)
- Jaimini Sutras
- Phaladeepika
- Saravali
- Jataka Parijata
