# AstroStorm - Serious Next Steps (Vedic Astro & Code Magic)

This document outlines the next phase of development for AstroStorm, focusing on deep Vedic astrology principles and robust software engineering. These steps are strictly offline-first, requiring no AI, cloud services, or paid APIs.

---

## 1. Shri Pati Paddhati House System
**Description:** Implement the Shri Pati house division (Sripati Paddhati), a popular system in North India based on equal division from the Midheaven (MC).
- **Core Task:** Calculate MC-based house cusps and compare with Whole Sign and Bhava Chalit.
- **Vedic Value:** Provides a more traditional North Indian perspective on house strengths and planetary placement.
- **Reference:** *Jyotish Ratnamala* by Shri Pati.

## 2. Bhava Madhya & Bhava Sandhi Analysis
**Description:** Move beyond simple sign-based placement to precise house-junction (Sandhi) analysis.
- **Core Task:** Calculate Bhava Madhya (house mid-point) and Bhava Sandhi (junction). Detect planets within 5° of Sandhi.
- **Vedic Value:** Planets in Sandhi lose significant strength (Bala) and give mixed or weakened results, a critical detail often missed in basic apps.

## 3. Panchapakshi System (The Five Birds)
**Description:** Implement the sophisticated South Indian system of five birds representing different elemental energies.
- **Core Task:** Determine birth bird from Nakshatra and moon phase. Generate daily/hourly cycles for the five activities (Eating, Walking, Ruling, Sleeping, Dying).
- **Vedic Value:** A highly practical tool for identifying auspicious windows for specific types of actions throughout the day.

## 4. Deepening Indu Lagna (Wealth Lagna)
**Description:** Replace the current simplified Indu Lagna logic with the full classical mathematical model.
- **Core Task:** Sum the specific units (Kalās) of the 9th lord from Lagna and 9th lord from Moon. Divide by 12 and count the remainder from Moon's sign.
- **Vedic Value:** Accurately identifies the "Lagna of Wealth" and its potential for financial accumulation.

## 5. Krishnamurti Padhdhati (K.P.) Foundation
**Description:** Integrate core K.P. astrology concepts for micro-timing of events.
- **Core Task:** Implement calculation of Sub-lords and Sub-sub-lords for all planets and house cusps (Placidus/KP house system). Generate planetary and house significators.
- **Vedic Value:** Adds a layer of precision to "When will it happen?" questions that standard Parashari methods may leave vague.

## 6. Custom Yoga DSL & Rule Engine
**Description:** Migrate from hardcoded Kotlin Yoga evaluators to a JSON-based Domain Specific Language (DSL).
- **Core Task:** Create an engine that reads yoga definitions (e.g., `{"planet": "MARS", "house": 10, "dignity": "EXALTED"}`) and evaluates them against the chart.
- **Code Magic:** Allows adding hundreds of classical Yogas from BPHS and Saravali without re-compiling the app.

## 7. Jaimini Astrology Expansion
**Description:** Deepen the Jaimini system support beyond basic Chara Dasha.
- **Core Task:** Implement Jaimini Rashi Drishti (sign-based aspects) and Karakamsha (Atmakaraka's Navamsa sign) analysis.
- **Vedic Value:** Provides a second, independent opinion on the chart's potential as per the Jaimini Sutras.

## 8. Ashtakavarga Transit (Kakshaya) Analysis
**Description:** Enhance transit analysis using the 8-fold division of signs.
- **Core Task:** Calculate the Kakshaya (3°45' division) for transiting planets and check if they have a bindu (point) in the natal chart's Ashtakavarga for that specific Kakshaya.
- **Vedic Value:** Explains why a "good" transit sometimes fails to deliver results—because the specific Kakshaya is point-less.

## 9. Tajika Varshaphala Enhancement
**Description:** Deepen the annual solar return chart analysis.
- **Core Task:** Implement Muntha movement analysis and Harsha Bala (the four-fold strength system for Tajika planets).
- **Vedic Value:** Brings the annual prediction system up to professional standards as per *Tajika Nilakanthi*.

## 10. Interactive Strength Visualizations
**Description:** Use advanced UI components to visualize complex data.
- **Core Task:** Implement Radar Charts for Shadbala (six-fold strength) and Heatmaps for Dasha favorability.
- **Code Magic:** Improves UX by making abstract numerical strengths immediately intuitive to the user.

## 11. Varga-Specific Deep Interpretations
**Description:** Provide specialized analysis for divisional charts.
- **Core Task:** Create specific modules for D10 (Dashamsha) career types, D24 (Chaturvimshamsha) education fields, and D60 (Shashtiamsha) past life karmic debts.
- **Vedic Value:** Moves away from generic "You will be successful" to "You are suited for administrative roles (D10)."

## 12. Local Encrypted Backup (Code Magic)
**Description:** Secure, offline data portability.
- **Core Task:** Implement an AES-encrypted export/import system for the local chart database.
- **Code Magic:** Allows users to backup and sync their data manually between devices without ever touching a cloud server, ensuring absolute privacy.

---

*This roadmap ensures AstroStorm remains the most authentic and technically superior Vedic astrology tool available.*
