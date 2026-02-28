# AstroVajra - Future Development Roadmap (Pending Items)

This document outlines the remaining practical and impactful next steps for the AstroVajra Vedic astrology application. These ideas are based on authentic Vedic astrology principles and are intended to enhance the app's structural accuracy and predictive depth.

---

## 1. Shri Pati Paddhati House Division

### Description
Implement Shri Pati (equal house division from mid-heaven) as an alternative house system popular in North India. This system provides a more nuanced view of house strengths compared to Whole Sign houses.

### Implementation Details
- Calculate MC-based equal houses
- Comparison view with Whole Sign houses
- Bhava Madhya (house middle) calculations
- Bhava Sandhi (cusp) analysis
- Planet's house position adjustment
- Visual toggle between house systems

### Vedic References
- Shri Pati's Jyotish Ratnamala
- North Indian astrological traditions

---

## 2. Bhava Madhya and Bhava Sandhi Analysis

### Description
Implement precise Bhava (house) cusp calculations and analyze planets near house boundaries (Sandhi) where results become mixed or weakened.

### Implementation Details
- Calculate Bhava Madhya (house mid-point) for all 12 houses
- Calculate Bhava Sandhi (house cusp/junction)
- Detect planets within 5° of Sandhi (boundary effect)
- House overlap analysis for Shri Pati system
- Planet's actual house determination
- Sandhi planet weakness assessment
- Remedial measures for Sandhi-placed planets
- Comparative analysis with whole-sign houses

### Vedic References
- Shri Pati Jyotish Ratnamala
- North Indian house calculation traditions

---

## Implementation Priority Matrix

| Priority | Feature | Complexity | Impact | Status |
|----------|---------|------------|--------|--------|
| Medium | Bhava Sandhi Analysis | Medium | Medium | Pending |
| Low | Shri Pati Paddhati | Medium | Low | Pending |

---

## Technical Recommendations

### Code Quality
1. Create shared constants file for astrological rules (exaltation degrees, aspects, etc.)
2. Implement dependency injection (Hilt) for better testability
3. Add comprehensive unit tests for all calculators
4. Create base calculator class for common patterns

### Performance
1. Implement lazy calculation for divisional charts (calculate on demand)
2. Add caching layer for expensive ephemeris calculations
3. Use coroutines for background chart calculations
4. Optimize memory usage in large chart calculations

### User Experience
1. Add chart comparison view (two charts side by side)
2. Implement chart rectification tool
3. Add educational tooltips explaining astrological concepts
4. Create glossary of Vedic astrology terms

---

## References & Classical Sources

All implementations should follow authentic Vedic astrology principles from:
- **Brihat Parashara Hora Shastra (BPHS)** - Primary reference
- **Phaladeepika** by Mantreswara
- **Jataka Parijata** by Vaidyanatha Dikshita
- **Saravali** by Kalyana Varma
- **Jaimini Sutras** for Jaimini system
- **Prashna Marga** for horary astrology
- **Muhurta Chintamani** for muhurta analysis

---

*This document serves as a living roadmap for AstroVajra development. Features should be implemented with proper Vedic astrology validation and user testing.*
