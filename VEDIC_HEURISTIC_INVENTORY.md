# Vedic Heuristic Inventory

Generated: 2026-03-05

| File | Line | Marker | Snippet |
|---|---:|---|---|
| app\src\main\java\com\astro\vajra\ephemeris\ArudhaPadaCalculator.kt | 148 | approximate | val approximateTiming: String |
| app\src\main\java\com\astro\vajra\ephemeris\ArudhaPadaCalculator.kt | 1008 | approximate | approximateTiming = "${planet.getLocalizedName(language)} transit through ${arudha.sign.getLocalizedName(language)}" |
| app\src\main\java\com\astro\vajra\ephemeris\AshtamangalaPrashnaCalculator.kt | 24 | randomization | * - Digital cowrie shell simulation with authentic randomization |
| app\src\main\java\com\astro\vajra\ephemeris\AshtamangalaPrashnaCalculator.kt | 368 | randomization | * Simulate cowrie shell throw with authentic randomization |
| app\src\main\java\com\astro\vajra\ephemeris\AshtamangalaPrashnaCalculator.kt | 371 | System.nanoTime | val random = seedValue?.let { Random(it) } ?: Random(System.nanoTime()) |
| app\src\main\java\com\astro\vajra\ephemeris\AshtavargaTransitCalculator.kt | 717 | approximate | Planet.SATURN -> "approximately 2.5 years" |
| app\src\main\java\com\astro\vajra\ephemeris\AshtavargaTransitCalculator.kt | 718 | approximate | Planet.JUPITER -> "approximately 1 year" |
| app\src\main\java\com\astro\vajra\ephemeris\AshtavargaTransitCalculator.kt | 719 | approximate | Planet.MARS -> "approximately 1.5 months" |
| app\src\main\java\com\astro\vajra\ephemeris\AshtavargaTransitCalculator.kt | 720 | approximate | else -> "approximately 1 month" |
| app\src\main\java\com\astro\vajra\ephemeris\AvasthaCalculator.kt | 114 | simplified | * Analyze nodes (Rahu/Ketu) - simplified analysis |
| app\src\main\java\com\astro\vajra\ephemeris\AvasthaCalculator.kt | 125 | simplified | // Nodes use simplified avasthas |
| app\src\main\java\com\astro\vajra\ephemeris\AvasthaCalculator.kt | 277 | simplified | * Calculate simplified Deeptadi for nodes |
| app\src\main\java\com\astro\vajra\ephemeris\AvasthaCalculator.kt | 360 | Simplified | // Simplified: if in enemy sign and watery |
| app\src\main\java\com\astro\vajra\ephemeris\BadhakaCalculator.kt | 612 | approximate | // Note: In a full impl we'd check owned houses, here we approximate with aspects/position |
| app\src\main\java\com\astro\vajra\ephemeris\BhriguBinduCalculator.kt | 427 | approximate | // Mars special aspect to 4th and 8th houses (90 and 210 degrees approximately) |
| app\src\main\java\com\astro\vajra\ephemeris\BhriguBinduCalculator.kt | 718 | fallback | fallbackDays = 180L |
| app\src\main\java\com\astro\vajra\ephemeris\BhriguBinduCalculator.kt | 733 | fallback | fallbackDays = 365L |
| app\src\main\java\com\astro\vajra\ephemeris\BhriguBinduCalculator.kt | 750 | fallback | fallbackDays: Long |
| app\src\main\java\com\astro\vajra\ephemeris\BhriguBinduCalculator.kt | 752 | fallback | val position = planetPosition ?: return currentDate.plusDays(fallbackDays) |
| app\src\main\java\com\astro\vajra\ephemeris\BhriguBinduCalculator.kt | 754 | fallback | if (!speed.isFinite() \|\| speed == 0.0) return currentDate.plusDays(fallbackDays) |
| app\src\main\java\com\astro\vajra\ephemeris\GrahaYuddhaCalculator.kt | 179 | simplified | // Check latitude (declination) - simplified as we may not have exact declination |
| app\src\main\java\com\astro\vajra\ephemeris\GrahaYuddhaCalculator.kt | 236 | Simplified | // Simplified: if separation would decrease based on relative speeds |
| app\src\main\java\com\astro\vajra\ephemeris\HoroscopeCalculator.kt | 928 | Placeholder | event = if (language == Language.NEPALI) com.astro.vajra.core.common.BikramSambatConverter.toNepaliNumerals(date.dayOfWeek.value.toString())... |
| app\src\main\java\com\astro\vajra\ephemeris\HoroscopeCalculator.kt | 1054 | Placeholder | val dayName = if (language == Language.NEPALI) com.astro.vajra.core.common.BikramSambatConverter.toNepaliNumerals(it.dayOfWeek.value.toStrin... |
| app\src\main\java\com\astro\vajra\ephemeris\KakshaTransitCalculator.kt | 42 | approximate | /** Average planetary speeds in degrees per day (approximate) */ |
| app\src\main\java\com\astro\vajra\ephemeris\KalachakraDashaCalculator.kt | 1282 | Fallback | areas.add("${getLocalizedPlanetName(signLord, language)}-${StringResources.get(com.astro.vajra.core.common.StringKey.VARSHAPHALA_KEY_DATES, ... |
| app\src\main\java\com\astro\vajra\ephemeris\NadiAmshaCalculator.kt | 130 | Placeholder | description = "Nadi #$nadiNumber in ${sign.displayName}", // Placeholder for specific Nadi name |
| app\src\main\java\com\astro\vajra\ephemeris\NadiAmshaCalculator.kt | 146 | Approximate | // Approximate ascendant speed for first-pass rectification search. |
| app\src\main\java\com\astro\vajra\ephemeris\NadiAmshaCalculator.kt | 149 | approx | // Check +/- 5 Nadis (approx +/- 4 minutes) |
| app\src\main\java\com\astro\vajra\ephemeris\NadiAmshaCalculator.kt | 165 | simplified | // Handle boundary crossing (very simplified, assumes same sign for small shifts) |
| app\src\main\java\com\astro\vajra\ephemeris\nativeanalysis\NativeAnalysisCalculator.kt | 106 | Simplified | private fun checkDhanaYoga(c: VedicChart): Boolean = true // Simplified |
| app\src\main\java\com\astro\vajra\ephemeris\prashna\PrashnaConstants.kt | 23 | approximate | // Moon movement per day (approximately 13.2 degrees) |
| app\src\main\java\com\astro\vajra\ephemeris\SadeSatiCalculator.kt | 24 | approximate | * Total duration: approximately 7.5 years (Sade = 7, Sati = half) |
| app\src\main\java\com\astro\vajra\ephemeris\SadeSatiCalculator.kt | 37 | Approximate | * - Approximate timeline calculation |
| app\src\main\java\com\astro\vajra\ephemeris\SadeSatiCalculator.kt | 51 | approximate | * Saturn takes approximately 29.5 years to complete one zodiac cycle |
| app\src\main\java\com\astro\vajra\ephemeris\SadeSatiCalculator.kt | 128 | approximate | val approximateDaysRemaining: Int, |
| app\src\main\java\com\astro\vajra\ephemeris\SadeSatiCalculator.kt | 129 | approximate | val approximateEndDate: LocalDate?, |
| app\src\main\java\com\astro\vajra\ephemeris\SadeSatiCalculator.kt | 265 | approximate | approximateDaysRemaining = daysRemaining, |
| app\src\main\java\com\astro\vajra\ephemeris\SadeSatiCalculator.kt | 266 | approximate | approximateEndDate = endDate, |
| app\src\main\java\com\astro\vajra\ephemeris\SadeSatiCalculator.kt | 585 | approximate | approximateDaysRemaining = 0, |
| app\src\main\java\com\astro\vajra\ephemeris\SadeSatiCalculator.kt | 586 | approximate | approximateEndDate = null, |
| app\src\main\java\com\astro\vajra\ephemeris\SarvatobhadraChakraCalculator.kt | 510 | simplified | // Calculate tithi (simplified) |
| app\src\main\java\com\astro\vajra\ephemeris\ShadbalaCalculator.kt | 790 | approx | // Obliquity of the ecliptic (Earth's axial tilt) - approx 23.44° for modern era |
| app\src\main\java\com\astro\vajra\ephemeris\SwissEphemerisEngine.kt | 563 | approx | // Precision threshold for boundary detection (approx 1 arc second) |
| app\src\main\java\com\astro\vajra\ephemeris\SwissEphemerisEngine.kt | 571 | Fallback | // Fallback for edge cases exactly on the 12th house cusp boundary |
| app\src\main\java\com\astro\vajra\ephemeris\TransitAnalyzer.kt | 835 | Fallback | // Fallback if key missing |
| app\src\main\java\com\astro\vajra\ephemeris\UpachayaTransitTracker.kt | 129 | approximate | approximateDuration = duration, |
| app\src\main\java\com\astro\vajra\ephemeris\UpachayaTransitTracker.kt | 546 | approximate | val approximateDuration: String, |
| app\src\main\java\com\astro\vajra\ephemeris\varga\GenericVargaAnalyzer.kt | 34 | Simplified | val houseLord = VedicAstrologyUtils.getHouseLord(chart, houseNum) // Simplified, uses D1 lord logic for generic |
| app\src\main\java\com\astro\vajra\ephemeris\varga\SaptamsaAnalyzer.kt | 1324 | approximate | // Calculate approximate timing based on child number |
| app\src\main\java\com\astro\vajra\ephemeris\varshaphala\SolarReturnCalculator.kt | 39 | approximate | val approximateJd = birthJd + (yearsElapsed * SIDEREAL_YEAR_DAYS) |
| app\src\main\java\com\astro\vajra\ephemeris\varshaphala\SolarReturnCalculator.kt | 40 | approximate | var currentJd = approximateJd |
| app\src\main\java\com\astro\vajra\ephemeris\varshaphala\TajikaYogaCalculator.kt | 533 | Placeholder | aspect = TajikaAspect.CONJUNCTION, // Placeholder |
| app\src\main\java\com\astro\vajra\ephemeris\varshaphala\TajikaYogaCalculator.kt | 793 | Simplified | // Simplified implementation - checks for chain of aspects |
| app\src\main\java\com\astro\vajra\ephemeris\varshaphala\TajikaYogaCalculator.kt | 928 | Simplified | // Simplified: check if faster planet's longitude is before the exact aspect point |
| app\src\main\java\com\astro\vajra\ephemeris\varshaphala\TajikaYogaCalculator.kt | 939 | Simplified | // Simplified Hadda implementation |
| app\src\main\java\com\astro\vajra\ephemeris\varshaphala\TajikaYogaCalculator.kt | 948 | Simplified | // Simplified - using Egyptian terms |
| app\src\main\java\com\astro\vajra\ephemeris\varshaphala\TajikaYogaCalculator.kt | 970 | Simplified | // Simplified chain finding |
| app\src\main\java\com\astro\vajra\ephemeris\varshaphala\TajikaYogaCalculator.kt | 1000 | Simplified | // Simplified - return the sign where Muntha falls |
| app\src\main\java\com\astro\vajra\ephemeris\varshaphala\TajikaYogaCalculator.kt | 1045 | Simplified | // Simplified monthly trend calculation based on Moon's nakshatra cycle |
| app\src\main\java\com\astro\vajra\ephemeris\yoga\ConjunctionYogaEvaluator.kt | 90 | Simplified | isAuspicious = true, // Simplified |
| app\src\main\java\com\astro\vajra\ephemeris\yoga\ConjunctionYogaEvaluator.kt | 116 | Fallback | else -> StringKeyYogaExpanded.YOGA_CONJUNCTION_TITLE // Fallback |
| app\src\main\java\com\astro\vajra\ephemeris\yoga\NabhasaYogaEvaluator.kt | 93 | Placeholder | descriptionKey = com.astro.vajra.core.common.StringKeyYogaExpanded.YOGA_CAT_NABHASA, // Placeholder or specific |
| app\src\main\java\com\astro\vajra\ephemeris\yoga\PlanetaryYogaEvaluator.kt | 226 | approximate | // Pushkara Navamsha positions by sign (approximate) |
| app\src\main\java\com\astro\vajra\ephemeris\yoga\PlanetaryYogaEvaluator.kt | 659 | Simplified | // Simplified: Jupiter in Kendra with strong dignity |
| app\src\main\java\com\astro\vajra\ephemeris\yoga\PlanetaryYogaEvaluator.kt | 1236 | simplified | // Determine winner based on brightness (simplified - Venus > Jupiter > Mars > Saturn > Mercury) |
| app\src\main\java\com\astro\vajra\ephemeris\yoga\YogaHelpers.kt | 622 | fallback | * Get house significations (English fallback) |
| app\src\main\java\com\astro\vajra\ephemeris\yoga\YogaLocalization.kt | 80 | Fallback | return yoga.description // Fallback to original English |
| app\src\main\java\com\astro\vajra\ephemeris\yoga\YogaLocalization.kt | 100 | Simplified | Planet.SUN -> StringKeyYogaExpanded.LORD_1 // Simplified mapping, ideally should be house-based |
| app\src\main\java\com\astro\vajra\ephemeris\yoga\YogaLocalization.kt | 268 | Fallback | else -> return englishName // Fallback to English name |
| app\src\main\java\com\astro\vajra\ephemeris\yoga\YogaModels.kt | 306 | fallback | // Basic localization or fallback |
