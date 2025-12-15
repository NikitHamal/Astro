package com.astro.storm.ephemeris

import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.R
import com.astro.storm.data.localization.LocalizableString
import com.astro.storm.data.model.Nakshatra
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import kotlin.math.abs
import kotlin.math.min

/**
 * Comprehensive Yoga Calculator for Vedic Astrology
 *
 * This calculator identifies all major Yogas (planetary combinations) including:
 * 1. Raja Yogas - Power and authority combinations
 * 2. Dhana Yogas - Wealth combinations
 * 3. Pancha Mahapurusha Yogas - Five great person yogas
 * 4. Nabhasa Yogas - Sky/pattern-based yogas
 * 5. Chandra Yogas - Moon-based combinations
 * 6. Solar Yogas - Sun-based combinations (Vesi, Vosi, Ubhayachari)
 * 7. Negative Yogas - Kemadruma, Daridra, etc.
 *
 * All calculations are based on:
 * - Brihat Parasara Hora Shastra (BPHS)
 * - Phaladeepika
 * - Saravali
 * - Jataka Parijata
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
object YogaCalculator {

    /**
     * Yoga category enumeration
     */
    enum class YogaCategory {
        RAJA_YOGA,
        DHANA_YOGA,
        MAHAPURUSHA_YOGA,
        NABHASA_YOGA,
        CHANDRA_YOGA,
        SOLAR_YOGA,
        NEGATIVE_YOGA,
        SPECIAL_YOGA;

        val displayName: LocalizableString
            get() = LocalizableString.Resource(
                when (this) {
                    RAJA_YOGA -> R.string.yoga_category_raja
                    DHANA_YOGA -> R.string.yoga_category_dhana
                    MAHAPURUSHA_YOGA -> R.string.yoga_category_pancha_mahapurusha
                    NABHASA_YOGA -> R.string.yoga_category_nabhasa
                    CHANDRA_YOGA -> R.string.yoga_category_chandra
                    SOLAR_YOGA -> R.string.yoga_category_solar
                    NEGATIVE_YOGA -> R.string.yoga_category_negative
                    SPECIAL_YOGA -> R.string.yoga_category_special
                }
            )

        val description: LocalizableString
            get() = LocalizableString.Resource(
                when (this) {
                    RAJA_YOGA -> R.string.yoga_category_raja_desc
                    DHANA_YOGA -> R.string.yoga_category_dhana_desc
                    MAHAPURUSHA_YOGA -> R.string.yoga_category_pancha_mahapurusha_desc
                    NABHASA_YOGA -> R.string.yoga_category_nabhasa_desc
                    CHANDRA_YOGA -> R.string.yoga_category_chandra_desc
                    SOLAR_YOGA -> R.string.yoga_category_solar_desc
                    NEGATIVE_YOGA -> R.string.yoga_category_negative_desc
                    SPECIAL_YOGA -> R.string.yoga_category_special_desc
                }
            )
    }

    /**
     * Yoga strength level
     */
    enum class YogaStrength(val value: Int) {
        EXTREMELY_STRONG(5),
        STRONG(4),
        MODERATE(3),
        WEAK(2),
        VERY_WEAK(1);

        val displayName: LocalizableString
            get() = LocalizableString.Resource(
                when (this) {
                    EXTREMELY_STRONG -> R.string.yoga_strength_extremely_strong
                    STRONG -> R.string.yoga_strength_strong
                    MODERATE -> R.string.yoga_strength_moderate
                    WEAK -> R.string.yoga_strength_weak
                    VERY_WEAK -> R.string.yoga_strength_very_weak
                }
            )
    }

    /**
     * Complete Yoga data class
     */
    data class Yoga(
        val name: LocalizableString,
        val sanskritName: String,
        val category: YogaCategory,
        val planets: List<Planet>,
        val houses: List<Int>,
        val description: LocalizableString,
        val effects: LocalizableString,
        val strength: YogaStrength,
        val strengthPercentage: Double,
        val isAuspicious: Boolean,
        val activationPeriod: LocalizableString,
        val cancellationFactors: List<LocalizableString>
    )

    /**
     * Complete Yoga analysis result
     */
    data class YogaAnalysis(
        val chart: VedicChart,
        val allYogas: List<Yoga>,
        val rajaYogas: List<Yoga>,
        val dhanaYogas: List<Yoga>,
        val mahapurushaYogas: List<Yoga>,
        val nabhasaYogas: List<Yoga>,
        val chandraYogas: List<Yoga>,
        val solarYogas: List<Yoga>,
        val negativeYogas: List<Yoga>,
        val specialYogas: List<Yoga>,
        val dominantYogaCategory: YogaCategory,
        val overallYogaStrength: Double,
        val timestamp: Long = System.currentTimeMillis()
    )

    /**
     * Calculate all Yogas in a chart
     */
    fun calculateYogas(chart: VedicChart): YogaAnalysis {
        val allYogas = mutableListOf<Yoga>()

        // Calculate each category
        val rajaYogas = calculateRajaYogas(chart)
        val dhanaYogas = calculateDhanaYogas(chart)
        val mahapurushaYogas = calculatePanchaMahapurushaYogas(chart)
        val nabhasaYogas = calculateNabhasaYogas(chart)
        val chandraYogas = calculateChandraYogas(chart)
        val solarYogas = calculateSolarYogas(chart)
        val negativeYogas = calculateNegativeYogas(chart)
        val additionalYogas = calculateAdditionalYogas(chart)
        val specialYogas = calculateSpecialYogas(chart)

        allYogas.addAll(rajaYogas)
        allYogas.addAll(dhanaYogas)
        allYogas.addAll(mahapurushaYogas)
        allYogas.addAll(nabhasaYogas)
        allYogas.addAll(chandraYogas)
        allYogas.addAll(solarYogas)
        allYogas.addAll(negativeYogas)
        allYogas.addAll(additionalYogas)
        allYogas.addAll(specialYogas)

        // Calculate dominant category
        val categoryCount = mapOf(
            YogaCategory.RAJA_YOGA to rajaYogas.size,
            YogaCategory.DHANA_YOGA to dhanaYogas.size,
            YogaCategory.MAHAPURUSHA_YOGA to mahapurushaYogas.size,
            YogaCategory.NABHASA_YOGA to nabhasaYogas.size,
            YogaCategory.CHANDRA_YOGA to chandraYogas.size,
            YogaCategory.SOLAR_YOGA to solarYogas.size,
            YogaCategory.SPECIAL_YOGA to specialYogas.size
        )
        val dominantCategory = categoryCount.maxByOrNull { it.value }?.key ?: YogaCategory.SPECIAL_YOGA

        // Calculate overall strength
        val positiveYogas = allYogas.filter { it.isAuspicious }
        val negativeCount = negativeYogas.size
        val overallStrength = if (positiveYogas.isNotEmpty()) {
            val avgStrength = positiveYogas.map { it.strengthPercentage }.average()
            (avgStrength * (1.0 - (negativeCount * 0.1))).coerceIn(0.0, 100.0)
        } else 50.0

        return YogaAnalysis(
            chart = chart,
            allYogas = allYogas,
            rajaYogas = rajaYogas,
            dhanaYogas = dhanaYogas,
            mahapurushaYogas = mahapurushaYogas,
            nabhasaYogas = nabhasaYogas,
            chandraYogas = chandraYogas,
            solarYogas = solarYogas,
            negativeYogas = negativeYogas,
            specialYogas = specialYogas,
            dominantYogaCategory = dominantCategory,
            overallYogaStrength = overallStrength
        )
    }

    // ==================== RAJA YOGAS ====================

    /**
     * Calculate all Raja Yogas
     * Raja Yogas are formed by the association of Kendra lords (1,4,7,10) and Trikona lords (1,5,9)
     */
    private fun calculateRajaYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        // Get house lords
        val houseLords = getHouseLords(ascendantSign)
        if (houseLords.isEmpty()) {
            // Log an error or handle the case where house lords cannot be determined.
            // For now, returning an empty list as no yogas can be calculated.
            return yogas
        }
        val kendraLords = listOf(houseLords[1], houseLords[4], houseLords[7], houseLords[10]).filterNotNull()
        val trikonaLords = listOf(houseLords[1], houseLords[5], houseLords[9]).filterNotNull()

        // 1. Kendra-Trikona Raja Yoga
        kendraLords.forEach { kendraLord ->
            trikonaLords.forEach { trikonaLord ->
                if (kendraLord != trikonaLord) {
                    val kendraPos = chart.planetPositions.find { it.planet == kendraLord }
                    val trikonaPos = chart.planetPositions.find { it.planet == trikonaLord }

                    if (kendraPos != null && trikonaPos != null) {
                        // Check for conjunction
                        if (areConjunct(kendraPos, trikonaPos)) {
                            val strength = calculateYogaStrength(chart, listOf(kendraPos, trikonaPos))
                            yogas.add(createKendraTrikonaRajaYoga(kendraLord, trikonaLord, "conjunction", strength, chart))
                        }

                        // Check for mutual aspect
                        if (areMutuallyAspecting(kendraPos, trikonaPos)) {
                            val strength = calculateYogaStrength(chart, listOf(kendraPos, trikonaPos)) * 0.8
                            yogas.add(createKendraTrikonaRajaYoga(kendraLord, trikonaLord, "aspect", strength, chart))
                        }

                        // Check for exchange (Parivartana)
                        if (areInExchange(kendraPos, trikonaPos)) {
                            val strength = calculateYogaStrength(chart, listOf(kendraPos, trikonaPos)) * 1.2
                            yogas.add(createParivartanaRajaYoga(kendraLord, trikonaLord, strength, chart))
                        }
                    }
                }
            }
        }

        // 2. Viparita Raja Yoga (lords of 6, 8, 12 in each other's houses)
        val dusthanaLords = listOf(houseLords[6], houseLords[8], houseLords[12]).filterNotNull()
        dusthanaLords.forEachIndexed { i, lord1 ->
            dusthanaLords.drop(i + 1).forEach { lord2 ->
                val pos1 = chart.planetPositions.find { it.planet == lord1 }
                val pos2 = chart.planetPositions.find { it.planet == lord2 }

                if (pos1 != null && pos2 != null) {
                    if (areInExchange(pos1, pos2) || areConjunct(pos1, pos2)) {
                        val strength = calculateYogaStrength(chart, listOf(pos1, pos2)) * 0.7
                        yogas.add(createViparitaRajaYoga(lord1, lord2, strength, chart))
                    }
                }
            }
        }

        // 3. Neecha Bhanga Raja Yoga
        chart.planetPositions.forEach { pos ->
            if (isDebilitated(pos)) {
                if (hasNeechaBhanga(pos, chart)) {
                    val strength = calculateYogaStrength(chart, listOf(pos))
                    yogas.add(createNeechaBhangaRajaYoga(pos.planet, strength, chart))
                }
            }
        }

        // 4. Maha Raja Yoga (specific powerful combinations)
        // Jupiter and Venus in Kendra from Moon
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }

        if (moonPos != null && jupiterPos != null && venusPos != null) {
            if (isInKendraFrom(jupiterPos, moonPos) && isInKendraFrom(venusPos, moonPos)) {
                val strength = calculateYogaStrength(chart, listOf(jupiterPos, venusPos, moonPos))
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_maha_raja),
                        sanskritName = "Maha Raja Yoga",
                        category = YogaCategory.RAJA_YOGA,
                        planets = listOf(Planet.JUPITER, Planet.VENUS, Planet.MOON),
                        houses = listOf(jupiterPos.house, venusPos.house),
                        description = LocalizableString.Resource(R.string.yoga_maha_raja_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_maha_raja),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_jupiter_venus),
                        cancellationFactors = emptyList()
                    )
                )
            }
        }

        return yogas
    }

    // ==================== DHANA YOGAS ====================

    /**
     * Calculate all Dhana (Wealth) Yogas
     */
    private fun calculateDhanaYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = getHouseLords(ascendantSign)

        // Dhana houses: 2 (wealth), 5 (past merit), 9 (fortune), 11 (gains)
        val dhanaHouses = listOf(2, 5, 9, 11)
        val dhanaLords = dhanaHouses.mapNotNull { houseLords[it] }

        // 1. Basic Dhana Yoga: Lords of 2, 5, 9, 11 in conjunction or exchange
        dhanaLords.forEachIndexed { i, lord1 ->
            dhanaLords.drop(i + 1).forEach { lord2 ->
                val pos1 = chart.planetPositions.find { it.planet == lord1 }
                val pos2 = chart.planetPositions.find { it.planet == lord2 }

                if (pos1 != null && pos2 != null && areConjunct(pos1, pos2)) {
                    val strength = calculateYogaStrength(chart, listOf(pos1, pos2))
                    yogas.add(
                        Yoga(
                            name = LocalizableString.Resource(R.string.yoga_dhana),
                            sanskritName = "Dhana Yoga",
                            category = YogaCategory.DHANA_YOGA,
                            planets = listOf(lord1, lord2),
                            houses = listOf(pos1.house, pos2.house),
                            description = LocalizableString.Resource(R.string.yoga_dhana_desc),
                            effects = LocalizableString.ResourceWithArgs(
                                R.string.yoga_effect_dhana,
                                listOf(getLocalizedHouseSignifications(pos1.house))
                            ),
                            strength = strengthFromPercentage(strength),
                            strengthPercentage = strength,
                            isAuspicious = true,
                            activationPeriod = LocalizableString.Resource(R.string.yoga_activation_dasha),
                            cancellationFactors = listOf(
                                LocalizableString.Resource(R.string.yoga_cancel_combustion),
                                LocalizableString.Resource(R.string.yoga_cancel_debilitation)
                            )
                        )
                    )
                }
            }
        }

        // 2. Lakshmi Yoga - Venus in own/exalted sign in Kendra/Trikona
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
        if (venusPos != null) {
            val isStrong = isInOwnSign(venusPos) || isExalted(venusPos)
            val isGoodHouse = venusPos.house in listOf(1, 4, 5, 7, 9, 10)

            if (isStrong && isGoodHouse) {
                val strength = calculateYogaStrength(chart, listOf(venusPos)) * 1.2
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_lakshmi),
                        sanskritName = "Lakshmi Yoga",
                        category = YogaCategory.DHANA_YOGA,
                        planets = listOf(Planet.VENUS),
                        houses = listOf(venusPos.house),
                        description = LocalizableString.Resource(R.string.yoga_lakshmi_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_lakshmi),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_venus_mahadasha),
                        cancellationFactors = listOf(LocalizableString.Resource(R.string.yoga_cancel_malefic_affliction))
                    )
                )
            }
        }

        // 3. Kubera Yoga - Jupiter in 2nd with Mercury
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }

        if (jupiterPos != null && mercuryPos != null) {
            if (jupiterPos.house == 2 && areConjunct(jupiterPos, mercuryPos)) {
                val strength = calculateYogaStrength(chart, listOf(jupiterPos, mercuryPos))
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_kubera),
                        sanskritName = "Kubera Yoga",
                        category = YogaCategory.DHANA_YOGA,
                        planets = listOf(Planet.JUPITER, Planet.MERCURY),
                        houses = listOf(2),
                        description = LocalizableString.Resource(R.string.yoga_kubera_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_kubera),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_jupiter_mercury),
                        cancellationFactors = emptyList()
                    )
                )
            }
        }

        // 4. Chandra-Mangala Yoga (Moon-Mars conjunction for wealth)
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }

        if (moonPos != null && marsPos != null && areConjunct(moonPos, marsPos)) {
            val strength = calculateYogaStrength(chart, listOf(moonPos, marsPos))
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_chandra_mangala),
                    sanskritName = "Chandra-Mangala Yoga",
                    category = YogaCategory.DHANA_YOGA,
                    planets = listOf(Planet.MOON, Planet.MARS),
                    houses = listOf(moonPos.house),
                    description = LocalizableString.Resource(R.string.yoga_chandra_mangala_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_chandra_mangala),
                    strength = strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_moon_mars),
                    cancellationFactors = listOf(LocalizableString.Resource(R.string.yoga_cancel_in_dusthana))
                )
            )
        }

        // 5. Dhana Yoga from 11th lord placement
        val lord11 = houseLords[11]
        if (lord11 != null) {
            val lord11Pos = chart.planetPositions.find { it.planet == lord11 }
            if (lord11Pos != null && lord11Pos.house in listOf(1, 2, 5, 9, 10, 11)) {
                val strength = calculateYogaStrength(chart, listOf(lord11Pos))
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_labha),
                        sanskritName = "Labha Yoga",
                        category = YogaCategory.DHANA_YOGA,
                        planets = listOf(lord11),
                        houses = listOf(lord11Pos.house),
                        description = LocalizableString.Resource(R.string.yoga_labha_desc),
                        effects = LocalizableString.ResourceWithArgs(
                            R.string.yoga_effect_labha,
                            listOf(getLocalizedHouseSignifications(lord11Pos.house))
                        ),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_dasha),
                        cancellationFactors = emptyList()
                    )
                )
            }
        }

        return yogas
    }

    // ==================== PANCHA MAHAPURUSHA YOGAS ====================

    /**
     * Calculate Pancha Mahapurusha Yogas
     * Formed when Mars, Mercury, Jupiter, Venus, or Saturn is:
     * - In its own sign or exaltation
     * - In a Kendra house (1, 4, 7, 10)
     */
    private fun calculatePanchaMahapurushaYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val kendraHouses = listOf(1, 4, 7, 10)

        // Ruchaka Yoga - Mars
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
        if (marsPos != null && marsPos.house in kendraHouses) {
            if (marsPos.sign in listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO, ZodiacSign.CAPRICORN)) {
                val (strength, cancellations) = calculateMahapurushaStrengthWithReasons(marsPos, chart)
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_ruchaka),
                        sanskritName = "Ruchaka Mahapurusha Yoga",
                        category = YogaCategory.MAHAPURUSHA_YOGA,
                        planets = listOf(Planet.MARS),
                        houses = listOf(marsPos.house),
                        description = LocalizableString.Resource(R.string.yoga_ruchaka_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_ruchaka),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_mars_mahadasha),
                        cancellationFactors = cancellations.ifEmpty { listOf(LocalizableString.Resource(R.string.yoga_cancel_none)) }
                    )
                )
            }
        }

        // Bhadra Yoga - Mercury
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }
        if (mercuryPos != null && mercuryPos.house in kendraHouses) {
            if (mercuryPos.sign in listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO)) {
                val (strength, cancellations) = calculateMahapurushaStrengthWithReasons(mercuryPos, chart)
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_bhadra),
                        sanskritName = "Bhadra Mahapurusha Yoga",
                        category = YogaCategory.MAHAPURUSHA_YOGA,
                        planets = listOf(Planet.MERCURY),
                        houses = listOf(mercuryPos.house),
                        description = LocalizableString.Resource(R.string.yoga_bhadra_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_bhadra),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_mercury_mahadasha),
                        cancellationFactors = cancellations.ifEmpty { listOf(LocalizableString.Resource(R.string.yoga_cancel_none)) }
                    )
                )
            }
        }

        // Hamsa Yoga - Jupiter
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null && jupiterPos.house in kendraHouses) {
            if (jupiterPos.sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES, ZodiacSign.CANCER)) {
                val (strength, cancellations) = calculateMahapurushaStrengthWithReasons(jupiterPos, chart)
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_hamsa),
                        sanskritName = "Hamsa Mahapurusha Yoga",
                        category = YogaCategory.MAHAPURUSHA_YOGA,
                        planets = listOf(Planet.JUPITER),
                        houses = listOf(jupiterPos.house),
                        description = LocalizableString.Resource(R.string.yoga_hamsa_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_hamsa),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_jupiter_mahadasha),
                        cancellationFactors = cancellations.ifEmpty { listOf(LocalizableString.Resource(R.string.yoga_cancel_none)) }
                    )
                )
            }
        }

        // Malavya Yoga - Venus
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
        if (venusPos != null && venusPos.house in kendraHouses) {
            if (venusPos.sign in listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA, ZodiacSign.PISCES)) {
                val (strength, cancellations) = calculateMahapurushaStrengthWithReasons(venusPos, chart)
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_malavya),
                        sanskritName = "Malavya Mahapurusha Yoga",
                        category = YogaCategory.MAHAPURUSHA_YOGA,
                        planets = listOf(Planet.VENUS),
                        houses = listOf(venusPos.house),
                        description = LocalizableString.Resource(R.string.yoga_malavya_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_malavya),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_venus_mahadasha),
                        cancellationFactors = cancellations.ifEmpty { listOf(LocalizableString.Resource(R.string.yoga_cancel_none)) }
                    )
                )
            }
        }

        // Sasa Yoga - Saturn
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        if (saturnPos != null && saturnPos.house in kendraHouses) {
            if (saturnPos.sign in listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS, ZodiacSign.LIBRA)) {
                val (strength, cancellations) = calculateMahapurushaStrengthWithReasons(saturnPos, chart)
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_sasa),
                        sanskritName = "Sasa Mahapurusha Yoga",
                        category = YogaCategory.MAHAPURUSHA_YOGA,
                        planets = listOf(Planet.SATURN),
                        houses = listOf(saturnPos.house),
                        description = LocalizableString.Resource(R.string.yoga_sasa_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_sasa),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_saturn_mahadasha),
                        cancellationFactors = cancellations.ifEmpty { listOf(LocalizableString.Resource(R.string.yoga_cancel_none)) }
                    )
                )
            }
        }

        return yogas
    }

    // ==================== NABHASA YOGAS ====================

    /**
     * Calculate Nabhasa Yogas (Pattern-based)
     * These are based on the distribution of planets across signs/houses
     */
    private fun calculateNabhasaYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // Get occupied houses
        val occupiedHouses = chart.planetPositions
            .filter { it.planet in Planet.MAIN_PLANETS }
            .map { it.house }
            .distinct()
            .sorted()

        val occupiedSigns = chart.planetPositions
            .filter { it.planet in Planet.MAIN_PLANETS }
            .map { it.sign }
            .distinct()

        // 1. Yava Yoga - All planets in houses 1 and 7 (or 4 and 10)
        if (occupiedHouses.all { it in listOf(1, 7) }) {
            yogas.add(
                createNabhasaYoga(
                    LocalizableString.Resource(R.string.yoga_yava), "Yava Yoga",
                    LocalizableString.Resource(R.string.yoga_yava_desc),
                    LocalizableString.Resource(R.string.yoga_effect_yava)
                )
            )
        }

        // 2. Shringataka Yoga - Planets in trines (1, 5, 9)
        if (occupiedHouses.all { it in listOf(1, 5, 9) }) {
            yogas.add(
                createNabhasaYoga(
                    LocalizableString.Resource(R.string.yoga_shringataka), "Shringataka Yoga",
                    LocalizableString.Resource(R.string.yoga_shringataka_desc),
                    LocalizableString.Resource(R.string.yoga_effect_shringataka)
                )
            )
        }

        // 3. Gada Yoga - Planets in two adjacent Kendras (1-4, 4-7, 7-10, 10-1)
        val kendras = listOf(1, 4, 7, 10)
        var gadaYogaFound = false
        for (i in kendras.indices) {
            val kendra1 = kendras[i]
            val kendra2 = kendras[(i + 1) % 4]
            // Check if planets are distributed between two adjacent kendras
            val hasInFirstKendra = occupiedHouses.any { it == kendra1 }
            val hasInSecondKendra = occupiedHouses.any { it == kendra2 }
            val allInTwoKendras = occupiedHouses.all { it == kendra1 || it == kendra2 }

            if (hasInFirstKendra && hasInSecondKendra && allInTwoKendras) {
                yogas.add(
                    createNabhasaYoga(
                        LocalizableString.Resource(R.string.yoga_gada), "Gada Yoga",
                        LocalizableString.Resource(R.string.yoga_gada_desc),
                        LocalizableString.Resource(R.string.yoga_effect_gada)
                    )
                )
                gadaYogaFound = true
                break
            }
        }

        // 4. Shakata Yoga - Planets in houses 1 and 7 only (lagna and 7th)
        val planetsInLagna = chart.planetPositions.count { it.house == 1 && it.planet in Planet.MAIN_PLANETS }
        val planetsIn7th = chart.planetPositions.count { it.house == 7 && it.planet in Planet.MAIN_PLANETS }
        if (planetsInLagna > 0 && planetsIn7th > 0 && occupiedHouses.all { it in listOf(1, 7) }) {
            yogas.add(
                createNabhasaYoga(
                    LocalizableString.Resource(R.string.yoga_shakata), "Shakata Yoga",
                    LocalizableString.Resource(R.string.yoga_shakata_desc),
                    LocalizableString.Resource(R.string.yoga_effect_shakata)
                )
            )
        }

        // 5. Rajju Yoga - All planets in movable signs
        val movableSigns = listOf(ZodiacSign.ARIES, ZodiacSign.CANCER, ZodiacSign.LIBRA, ZodiacSign.CAPRICORN)
        if (occupiedSigns.all { it in movableSigns }) {
            yogas.add(
                createNabhasaYoga(
                    LocalizableString.Resource(R.string.yoga_rajju), "Rajju Yoga",
                    LocalizableString.Resource(R.string.yoga_rajju_desc),
                    LocalizableString.Resource(R.string.yoga_effect_rajju)
                )
            )
        }

        // 6. Musala Yoga - All planets in fixed signs
        val fixedSigns = listOf(ZodiacSign.TAURUS, ZodiacSign.LEO, ZodiacSign.SCORPIO, ZodiacSign.AQUARIUS)
        if (occupiedSigns.all { it in fixedSigns }) {
            yogas.add(
                createNabhasaYoga(
                    LocalizableString.Resource(R.string.yoga_musala), "Musala Yoga",
                    LocalizableString.Resource(R.string.yoga_musala_desc),
                    LocalizableString.Resource(R.string.yoga_effect_musala)
                )
            )
        }

        // 7. Nala Yoga - All planets in dual signs
        val dualSigns = listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES)
        if (occupiedSigns.all { it in dualSigns }) {
            yogas.add(
                createNabhasaYoga(
                    LocalizableString.Resource(R.string.yoga_nala), "Nala Yoga",
                    LocalizableString.Resource(R.string.yoga_nala_desc),
                    LocalizableString.Resource(R.string.yoga_effect_nala)
                )
            )
        }

        // 8. Kedara Yoga - All planets in 4 signs
        if (occupiedSigns.size == 4) {
            yogas.add(
                createNabhasaYoga(
                    LocalizableString.Resource(R.string.yoga_kedara), "Kedara Yoga",
                    LocalizableString.Resource(R.string.yoga_kedara_desc),
                    LocalizableString.Resource(R.string.yoga_effect_kedara)
                )
            )
        }

        // 9. Shoola Yoga - All planets in 3 signs
        if (occupiedSigns.size == 3) {
            yogas.add(
                createNabhasaYoga(
                    LocalizableString.Resource(R.string.yoga_shoola), "Shoola Yoga",
                    LocalizableString.Resource(R.string.yoga_shoola_desc),
                    LocalizableString.Resource(R.string.yoga_effect_shoola)
                )
            )
        }

        // 10. Yuga Yoga - All planets in 2 signs
        if (occupiedSigns.size == 2) {
            yogas.add(
                createNabhasaYoga(
                    LocalizableString.Resource(R.string.yoga_yuga), "Yuga Yoga",
                    LocalizableString.Resource(R.string.yoga_yuga_desc),
                    LocalizableString.Resource(R.string.yoga_effect_yuga)
                )
            )
        }

        // 11. Gola Yoga - All planets in 1 sign
        if (occupiedSigns.size == 1) {
            yogas.add(
                createNabhasaYoga(
                    LocalizableString.Resource(R.string.yoga_gola), "Gola Yoga",
                    LocalizableString.Resource(R.string.yoga_gola_desc),
                    LocalizableString.Resource(R.string.yoga_effect_gola)
                )
            )
        }

        // 12. Veena Yoga - All planets in 7 signs
        if (occupiedSigns.size == 7) {
            yogas.add(
                createNabhasaYoga(
                    LocalizableString.Resource(R.string.yoga_veena), "Veena Yoga",
                    LocalizableString.Resource(R.string.yoga_veena_desc),
                    LocalizableString.Resource(R.string.yoga_effect_veena)
                )
            )
        }

        return yogas
    }

    // ==================== CHANDRA YOGAS ====================

    /**
     * Calculate Moon-based Yogas
     */
    private fun calculateChandraYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas

        // 1. Sunafa Yoga - Planet (except Sun) in 2nd from Moon
        val planetsIn2ndFromMoon = chart.planetPositions.filter {
            it.planet != Planet.SUN && it.planet != Planet.MOON &&
                    getHouseFrom(it.sign, moonPos.sign) == 2
        }
        if (planetsIn2ndFromMoon.isNotEmpty()) {
            val planets = planetsIn2ndFromMoon.map { it.planet }
            val strength = calculateYogaStrength(chart, planetsIn2ndFromMoon)
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_sunafa),
                    sanskritName = "Sunafa Yoga",
                    category = YogaCategory.CHANDRA_YOGA,
                    planets = planets,
                    houses = planetsIn2ndFromMoon.map { it.house },
                    description = LocalizableString.Resource(R.string.yoga_sunafa_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_sunafa),
                    strength = strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_moon_dasha),
                    cancellationFactors = emptyList()
                )
            )
        }

        // 2. Anafa Yoga - Planet (except Sun) in 12th from Moon
        val planetsIn12thFromMoon = chart.planetPositions.filter {
            it.planet != Planet.SUN && it.planet != Planet.MOON &&
                    getHouseFrom(it.sign, moonPos.sign) == 12
        }
        if (planetsIn12thFromMoon.isNotEmpty()) {
            val planets = planetsIn12thFromMoon.map { it.planet }
            val strength = calculateYogaStrength(chart, planetsIn12thFromMoon)
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_anafa),
                    sanskritName = "Anafa Yoga",
                    category = YogaCategory.CHANDRA_YOGA,
                    planets = planets,
                    houses = planetsIn12thFromMoon.map { it.house },
                    description = LocalizableString.Resource(R.string.yoga_anafa_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_anafa),
                    strength = strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_moon_dasha),
                    cancellationFactors = emptyList()
                )
            )
        }

        // 3. Durudhara Yoga - Planets in both 2nd and 12th from Moon
        if (planetsIn2ndFromMoon.isNotEmpty() && planetsIn12thFromMoon.isNotEmpty()) {
            val planets = (planetsIn2ndFromMoon + planetsIn12thFromMoon).map { it.planet }
            val strength = calculateYogaStrength(chart, planetsIn2ndFromMoon + planetsIn12thFromMoon)
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_durudhara),
                    sanskritName = "Durudhara Yoga",
                    category = YogaCategory.CHANDRA_YOGA,
                    planets = planets,
                    houses = (planetsIn2ndFromMoon + planetsIn12thFromMoon).map { it.house },
                    description = LocalizableString.Resource(R.string.yoga_durudhara_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_durudhara),
                    strength = strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_moon_dasha),
                    cancellationFactors = emptyList()
                )
            )
        }

        // 4. Gaja-Kesari Yoga - Jupiter in Kendra from Moon
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null) {
            val houseFromMoon = getHouseFrom(jupiterPos.sign, moonPos.sign)
            if (houseFromMoon in listOf(1, 4, 7, 10)) {
                val strength = calculateYogaStrength(chart, listOf(jupiterPos, moonPos))
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_gaja_kesari),
                        sanskritName = "Gaja-Kesari Yoga",
                        category = YogaCategory.CHANDRA_YOGA,
                        planets = listOf(Planet.JUPITER, Planet.MOON),
                        houses = listOf(jupiterPos.house, moonPos.house),
                        description = LocalizableString.ResourceWithArgs(
                            R.string.yoga_gaja_kesari_desc,
                            listOf(houseFromMoon)
                        ),
                        effects = LocalizableString.Resource(R.string.yoga_effect_gaja_kesari),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_jupiter_moon),
                        cancellationFactors = listOf(LocalizableString.Resource(R.string.yoga_cancel_jupiter_combust_debilitated))
                    )
                )
            }
        }

        // 5. Adhi Yoga - Benefics in 6, 7, 8 from Moon
        val beneficsFrom678 = chart.planetPositions.filter {
            it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY) &&
                    getHouseFrom(it.sign, moonPos.sign) in listOf(6, 7, 8)
        }
        if (beneficsFrom678.size >= 2) {
            val strength = calculateYogaStrength(chart, beneficsFrom678)
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_adhi),
                    sanskritName = "Adhi Yoga",
                    category = YogaCategory.CHANDRA_YOGA,
                    planets = beneficsFrom678.map { it.planet },
                    houses = beneficsFrom678.map { it.house },
                    description = LocalizableString.Resource(R.string.yoga_adhi_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_adhi),
                    strength = strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_benefic_dashas),
                    cancellationFactors = emptyList()
                )
            )
        }

        return yogas
    }

    // ==================== SOLAR YOGAS ====================

    /**
     * Calculate Sun-based Yogas
     */
    private fun calculateSolarYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return yogas

        // Get planets in 2nd and 12th from Sun (excluding Moon)
        val planetsIn2ndFromSun = chart.planetPositions.filter {
            it.planet != Planet.MOON && it.planet != Planet.SUN &&
                    getHouseFrom(it.sign, sunPos.sign) == 2
        }
        val planetsIn12thFromSun = chart.planetPositions.filter {
            it.planet != Planet.MOON && it.planet != Planet.SUN &&
                    getHouseFrom(it.sign, sunPos.sign) == 12
        }

        // 1. Vesi Yoga - Planet in 2nd from Sun
        if (planetsIn2ndFromSun.isNotEmpty()) {
            val planets = planetsIn2ndFromSun.map { it.planet }
            val strength = calculateYogaStrength(chart, planetsIn2ndFromSun)
            val effects = if (planets.any { it in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY) }) {
                LocalizableString.Resource(R.string.yoga_effect_vesi_benefic)
            } else {
                LocalizableString.Resource(R.string.yoga_effect_vesi_malefic)
            }
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_vesi),
                    sanskritName = "Vesi Yoga",
                    category = YogaCategory.SOLAR_YOGA,
                    planets = planets,
                    houses = planetsIn2ndFromSun.map { it.house },
                    description = LocalizableString.Resource(R.string.yoga_vesi_desc),
                    effects = effects,
                    strength = strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = planets.any { it in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY) },
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_sun_dasha),
                    cancellationFactors = emptyList()
                )
            )
        }

        // 2. Vosi Yoga - Planet in 12th from Sun
        if (planetsIn12thFromSun.isNotEmpty()) {
            val planets = planetsIn12thFromSun.map { it.planet }
            val strength = calculateYogaStrength(chart, planetsIn12thFromSun)
            val effects = if (planets.any { it in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY) }) {
                LocalizableString.Resource(R.string.yoga_effect_vosi_benefic)
            } else {
                LocalizableString.Resource(R.string.yoga_effect_vosi_malefic)
            }
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_vosi),
                    sanskritName = "Vosi Yoga",
                    category = YogaCategory.SOLAR_YOGA,
                    planets = planets,
                    houses = planetsIn12thFromSun.map { it.house },
                    description = LocalizableString.Resource(R.string.yoga_vosi_desc),
                    effects = effects,
                    strength = strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = planets.any { it in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY) },
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_sun_dasha),
                    cancellationFactors = emptyList()
                )
            )
        }

        // 3. Ubhayachari Yoga - Planets on both sides of Sun
        if (planetsIn2ndFromSun.isNotEmpty() && planetsIn12thFromSun.isNotEmpty()) {
            val planets = (planetsIn2ndFromSun + planetsIn12thFromSun).map { it.planet }
            val strength = calculateYogaStrength(chart, planetsIn2ndFromSun + planetsIn12thFromSun)
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_ubhayachari),
                    sanskritName = "Ubhayachari Yoga",
                    category = YogaCategory.SOLAR_YOGA,
                    planets = planets,
                    houses = (planetsIn2ndFromSun + planetsIn12thFromSun).map { it.house },
                    description = LocalizableString.Resource(R.string.yoga_ubhayachari_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_ubhayachari),
                    strength = strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_sun_dasha),
                    cancellationFactors = listOf(LocalizableString.Resource(R.string.yoga_cancel_malefics_flanking_sun))
                )
            )
        }

        return yogas
    }

    // ==================== NEGATIVE YOGAS ====================

    /**
     * Calculate Negative/Challenging Yogas
     */
    private fun calculateNegativeYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return yogas

        // 1. Kemadruma Yoga - No planets in 2nd and 12th from Moon
        val planetsIn2ndFromMoon = chart.planetPositions.filter {
            it.planet != Planet.SUN && it.planet != Planet.MOON &&
                    it.planet in Planet.MAIN_PLANETS &&
                    getHouseFrom(it.sign, moonPos.sign) == 2
        }
        val planetsIn12thFromMoon = chart.planetPositions.filter {
            it.planet != Planet.SUN && it.planet != Planet.MOON &&
                    it.planet in Planet.MAIN_PLANETS &&
                    getHouseFrom(it.sign, moonPos.sign) == 12
        }

        if (planetsIn2ndFromMoon.isEmpty() && planetsIn12thFromMoon.isEmpty()) {
            // Check for cancellation factors
            val cancellations = mutableListOf<LocalizableString>()

            // Cancellation 1: Moon in Kendra from Lagna
            if (moonPos.house in listOf(1, 4, 7, 10)) {
                cancellations.add(LocalizableString.Plain("Moon in Kendra (${moonPos.house}th house)"))
            }

            // Cancellation 2: Moon aspected by/conjunct Jupiter
            val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
            if (jupiterPos != null && (areConjunct(moonPos, jupiterPos) || areMutuallyAspecting(moonPos, jupiterPos))) {
                cancellations.add(LocalizableString.Resource(R.string.yoga_cancel_jupiter_aspects_conjoins_moon))
            }

            // Cancellation 3: Planet in Kendra from Moon
            val planetsInKendraFromMoon = chart.planetPositions.filter {
                it.planet in Planet.MAIN_PLANETS && it.planet != Planet.MOON &&
                        getHouseFrom(it.sign, moonPos.sign) in listOf(1, 4, 7, 10)
            }
            if (planetsInKendraFromMoon.isNotEmpty()) {
                cancellations.add(LocalizableString.Plain("Planet(s) in Kendra from Moon"))
            }

            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_kemadruma),
                    sanskritName = "Kemadruma Yoga",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.MOON),
                    houses = listOf(moonPos.house),
                    description = LocalizableString.Resource(R.string.yoga_kemadruma_desc),
                    effects = if (cancellations.isEmpty())
                        LocalizableString.Resource(R.string.yoga_effect_kemadruma)
                    else
                        LocalizableString.Resource(R.string.yoga_effect_kemadruma_cancelled),
                    strength = if (cancellations.isEmpty()) YogaStrength.STRONG else YogaStrength.WEAK,
                    strengthPercentage = if (cancellations.isEmpty()) 80.0 else 20.0,
                    isAuspicious = false,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_moon_dasha_uncancelled),
                    cancellationFactors = cancellations
                )
            )
        }

        // 2. Daridra Yoga - 11th lord in 6th, 8th, or 12th
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = getHouseLords(ascendantSign)
        val lord11 = houseLords[11]

        if (lord11 != null) {
            val lord11Pos = chart.planetPositions.find { it.planet == lord11 }
            if (lord11Pos != null && lord11Pos.house in listOf(6, 8, 12)) {
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_daridra),
                        sanskritName = "Daridra Yoga",
                        category = YogaCategory.NEGATIVE_YOGA,
                        planets = listOf(lord11),
                        houses = listOf(lord11Pos.house),
                        description = LocalizableString.Resource(R.string.yoga_daridra_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_daridra),
                        strength = YogaStrength.MODERATE,
                        strengthPercentage = 60.0,
                        isAuspicious = false,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_dasha),
                        cancellationFactors = listOf(LocalizableString.Resource(R.string.yoga_cancel_jupiter_aspect_strong_lord))
                    )
                )
            }
        }

        // 3. Graha Malika Yoga check for malefic version
        // If all planets are hemmed between malefics (Papakartari)

        // 4. Shakata Yoga (negative form) - Moon in 6, 8, 12 from Jupiter
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null) {
            val moonFromJupiter = getHouseFrom(moonPos.sign, jupiterPos.sign)
            if (moonFromJupiter in listOf(6, 8, 12)) {
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_shakata_negative),
                        sanskritName = "Shakata Yoga",
                        category = YogaCategory.NEGATIVE_YOGA,
                        planets = listOf(Planet.MOON, Planet.JUPITER),
                        houses = listOf(moonPos.house, jupiterPos.house),
                        description = LocalizableString.ResourceWithArgs(
                            R.string.yoga_shakata_negative_desc,
                            listOf(moonFromJupiter)
                        ),
                        effects = LocalizableString.Resource(R.string.yoga_effect_shakata_negative),
                        strength = YogaStrength.MODERATE,
                        strengthPercentage = 50.0,
                        isAuspicious = false,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_moon_jupiter_periods),
                        cancellationFactors = listOf(
                            LocalizableString.Resource(R.string.yoga_cancel_moon_in_kendra),
                            LocalizableString.Resource(R.string.yoga_cancel_jupiter_strong)
                        )
                    )
                )
            }
        }

        // 5. Guru-Chandal Yoga - Jupiter with Rahu
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU }
        if (jupiterPos != null && rahuPos != null && areConjunct(jupiterPos, rahuPos)) {
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_guru_chandal),
                    sanskritName = "Guru-Chandal Yoga",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.JUPITER, Planet.RAHU),
                    houses = listOf(jupiterPos.house),
                    description = LocalizableString.Resource(R.string.yoga_guru_chandal_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_guru_chandal),
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 65.0,
                    isAuspicious = false,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_jupiter_rahu_periods),
                    cancellationFactors = listOf(
                        LocalizableString.Resource(R.string.yoga_cancel_jupiter_own_exalted),
                        LocalizableString.Resource(R.string.yoga_cancel_aspect_from_benefics)
                    )
                )
            )
        }

        // 6. Grahan Yoga - Rahu/Ketu conjunct Sun or Moon (eclipsed luminaries)
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }

        // Sun-Rahu Grahan (Surya Grahan Yoga)
        if (sunPos != null && rahuPos != null && areConjunct(sunPos, rahuPos)) {
            val cancellations = mutableListOf<LocalizableString>()
            if (sunPos.house in listOf(3, 6, 10, 11)) {
                cancellations.add(LocalizableString.Resource(R.string.yoga_cancel_sun_in_upachaya))
            }
            if (isExalted(sunPos) || isInOwnSign(sunPos)) {
                cancellations.add(LocalizableString.Resource(R.string.yoga_cancel_sun_strong_mitigates))
            }
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_surya_grahan),
                    sanskritName = "Surya Grahan Yoga",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.SUN, Planet.RAHU),
                    houses = listOf(sunPos.house),
                    description = LocalizableString.Resource(R.string.yoga_surya_grahan_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_surya_grahan),
                    strength = if (cancellations.isEmpty()) YogaStrength.STRONG else YogaStrength.WEAK,
                    strengthPercentage = if (cancellations.isEmpty()) 75.0 else 35.0,
                    isAuspicious = false,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_sun_rahu_periods),
                    cancellationFactors = cancellations.ifEmpty { listOf(LocalizableString.Resource(R.string.yoga_cancel_none_identified)) }
                )
            )
        }

        // Sun-Ketu Grahan
        if (sunPos != null && ketuPos != null && areConjunct(sunPos, ketuPos)) {
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_surya_ketu_grahan),
                    sanskritName = "Surya-Ketu Grahan Yoga",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.SUN, Planet.KETU),
                    houses = listOf(sunPos.house),
                    description = LocalizableString.Resource(R.string.yoga_surya_ketu_grahan_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_surya_ketu_grahan),
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 55.0,
                    isAuspicious = false,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_sun_ketu_periods),
                    cancellationFactors = listOf(
                        LocalizableString.Resource(R.string.yoga_cancel_jupiter_aspect),
                        LocalizableString.Resource(R.string.yoga_cancel_sun_own_exalted)
                    )
                )
            )
        }

        // Moon-Rahu Grahan (Chandra Grahan Yoga)
        if (rahuPos != null && areConjunct(moonPos, rahuPos)) {
            val cancellations = mutableListOf<LocalizableString>()
            if (isExalted(moonPos) || isInOwnSign(moonPos)) {
                cancellations.add(LocalizableString.Resource(R.string.yoga_cancel_moon_strong_reduces_effects))
            }
            if (jupiterPos != null && (areConjunct(moonPos, jupiterPos) || areMutuallyAspecting(moonPos, jupiterPos))) {
                cancellations.add(LocalizableString.Resource(R.string.yoga_cancel_jupiter_aspects_conjoins_moon))
            }
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_chandra_grahan),
                    sanskritName = "Chandra Grahan Yoga",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.MOON, Planet.RAHU),
                    houses = listOf(moonPos.house),
                    description = LocalizableString.Resource(R.string.yoga_chandra_grahan_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_chandra_grahan),
                    strength = if (cancellations.isEmpty()) YogaStrength.STRONG else YogaStrength.WEAK,
                    strengthPercentage = if (cancellations.isEmpty()) 70.0 else 30.0,
                    isAuspicious = false,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_moon_rahu_periods),
                    cancellationFactors = cancellations.ifEmpty { listOf(LocalizableString.Resource(R.string.yoga_cancel_none_identified)) }
                )
            )
        }

        // Moon-Ketu conjunction
        if (ketuPos != null && areConjunct(moonPos, ketuPos)) {
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_chandra_ketu),
                    sanskritName = "Chandra-Ketu Yoga",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.MOON, Planet.KETU),
                    houses = listOf(moonPos.house),
                    description = LocalizableString.Resource(R.string.yoga_chandra_ketu_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_chandra_ketu),
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 50.0,
                    isAuspicious = false,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_moon_ketu_periods),
                    cancellationFactors = listOf(
                        LocalizableString.Resource(R.string.yoga_cancel_jupiter_aspect),
                        LocalizableString.Resource(R.string.yoga_cancel_moon_own_exalted),
                        LocalizableString.Resource(R.string.yoga_cancel_benefics_in_kendra)
                    )
                )
            )
        }

        // 7. Angarak Yoga - Mars with Rahu (fiery and aggressive combination)
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
        if (marsPos != null && rahuPos != null && areConjunct(marsPos, rahuPos)) {
            val cancellations = mutableListOf<LocalizableString>()
            if (isExalted(marsPos) || isInOwnSign(marsPos)) {
                cancellations.add(LocalizableString.Resource(R.string.yoga_cancel_mars_strong_channels_energy))
            }
            if (marsPos.house in listOf(3, 6, 10, 11)) {
                cancellations.add(LocalizableString.Resource(R.string.yoga_cancel_mars_in_upachaya_aggression_to_drive))
            }
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_angarak),
                    sanskritName = "Angarak Yoga",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.MARS, Planet.RAHU),
                    houses = listOf(marsPos.house),
                    description = LocalizableString.Resource(R.string.yoga_angarak_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_angarak),
                    strength = if (cancellations.isEmpty()) YogaStrength.STRONG else YogaStrength.MODERATE,
                    strengthPercentage = if (cancellations.isEmpty()) 80.0 else 50.0,
                    isAuspicious = false,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_mars_rahu_periods),
                    cancellationFactors = cancellations.ifEmpty { listOf(LocalizableString.Resource(R.string.yoga_cancel_none_identified)) }
                )
            )
        }

        // 8. Shrapit Yoga - Saturn with Rahu (cursed combination from past life)
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        if (saturnPos != null && rahuPos != null && areConjunct(saturnPos, rahuPos)) {
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_shrapit),
                    sanskritName = "Shrapit Yoga",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.SATURN, Planet.RAHU),
                    houses = listOf(saturnPos.house),
                    description = LocalizableString.Resource(R.string.yoga_shrapit_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_shrapit),
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 75.0,
                    isAuspicious = false,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_saturn_rahu_impactful),
                    cancellationFactors = listOf(
                        LocalizableString.Resource(R.string.yoga_cancel_jupiter_aspect),
                        LocalizableString.Resource(R.string.yoga_cancel_saturn_own_exalted),
                        LocalizableString.Resource(R.string.yoga_cancel_remedial_measures)
                    )
                )
            )
        }

        // 9. Kala Sarpa Yoga - All planets hemmed between Rahu and Ketu
        if (rahuPos != null && ketuPos != null) {
            val kalaSarpa = checkKalaSarpaYoga(chart, rahuPos, ketuPos)
            if (kalaSarpa != null) {
                yogas.add(kalaSarpa)
            }
        }

        // 10. Papakartari Yoga on Lagna - Malefics in 2nd and 12th from Ascendant
        val planetsIn2nd = chart.planetPositions.filter { it.house == 2 }
        val planetsIn12th = chart.planetPositions.filter { it.house == 12 }
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)

        val hasMaleficIn2nd = planetsIn2nd.any { it.planet in malefics }
        val hasMaleficIn12th = planetsIn12th.any { it.planet in malefics }

        if (hasMaleficIn2nd && hasMaleficIn12th) {
            val maleficPlanets = (planetsIn2nd.filter { it.planet in malefics } +
                    planetsIn12th.filter { it.planet in malefics }).map { it.planet }.distinct()
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_papakartari),
                    sanskritName = "Papakartari Yoga",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = maleficPlanets,
                    houses = listOf(1, 2, 12),
                    description = LocalizableString.Resource(R.string.yoga_papakartari_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_papakartari),
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 60.0,
                    isAuspicious = false,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_throughout_life_malefic_dashas),
                    cancellationFactors = listOf(
                        LocalizableString.Resource(R.string.yoga_cancel_strong_lagna_lord),
                        LocalizableString.Resource(R.string.yoga_cancel_benefics_aspecting_lagna),
                        LocalizableString.Resource(R.string.yoga_cancel_jupiter_in_kendra)
                    )
                )
            )
        }

        return yogas
    }

    /**
     * Check for Kala Sarpa Yoga - All planets between Rahu-Ketu axis
     * This is a significant yoga indicating karmic patterns from past lives.
     *
     * Types of Kala Sarpa based on Rahu's house position:
     * 1st house: Anant, 2nd: Kulik, 3rd: Vasuki, 4th: Shankhpal,
     * 5th: Padma, 6th: Mahapadma, 7th: Takshak, 8th: Karkotak,
     * 9th: Shankhachud, 10th: Ghatak, 11th: Vishdhar, 12th: Sheshnag
     */
    private fun checkKalaSarpaYoga(
        chart: VedicChart,
        rahuPos: PlanetPosition,
        ketuPos: PlanetPosition
    ): Yoga? {
        // Get all main planets excluding Rahu-Ketu
        val mainPlanets = chart.planetPositions.filter {
            it.planet in Planet.MAIN_PLANETS && it.planet != Planet.RAHU && it.planet != Planet.KETU
        }

        // Calculate Rahu-Ketu axis in terms of longitude
        val rahuLong = rahuPos.longitude
        val ketuLong = ketuPos.longitude

        // Check if all planets are on one side of the Rahu-Ketu axis
        // Rahu and Ketu are always 180° apart
        var allOnRahuSide = true
        var allOnKetuSide = true

        mainPlanets.forEach { planet ->
            val planetLong = planet.longitude

            // Check relative position to Rahu-Ketu axis
            // A planet is between Rahu and Ketu if its longitude falls in the arc from Rahu to Ketu (going one direction)
            val rahuToKetu = if (rahuLong < ketuLong) {
                planetLong in rahuLong..ketuLong
            } else {
                planetLong >= rahuLong || planetLong <= ketuLong
            }

            if (rahuToKetu) {
                allOnKetuSide = false
            } else {
                allOnRahuSide = false
            }
        }

        // If all planets are on one side, it's Kala Sarpa Yoga
        if (allOnRahuSide || allOnKetuSide) {
            // Determine the type based on Rahu's house
            val kalaSarpaType = when (rahuPos.house) {
                1 -> "Anant"
                2 -> "Kulik"
                3 -> "Vasuki"
                4 -> "Shankhpal"
                5 -> "Padma"
                6 -> "Mahapadma"
                7 -> "Takshak"
                8 -> "Karkotak"
                9 -> "Shankhachud"
                10 -> "Ghatak"
                11 -> "Vishdhar"
                12 -> "Sheshnag"
                else -> "Kala Sarpa"
            }

            // Determine direction (ascending or descending)
            val direction = if (allOnRahuSide)
                LocalizableString.Resource(R.string.kala_sarpa_direction_ascending)
            else
                LocalizableString.Resource(R.string.kala_sarpa_direction_descending)

            // Calculate cancellation factors
            val cancellations = mutableListOf<LocalizableString>()

            // Cancellation 1: If any planet is exactly conjunct Rahu or Ketu
            mainPlanets.forEach { planet ->
                if (areConjunct(planet, rahuPos, customOrb = 3.0) ||
                    areConjunct(planet, ketuPos, customOrb = 3.0)) {
                    cancellations.add(LocalizableString.Resource(R.string.yoga_cancel_kala_sarpa_conjunct_node))
                }
            }

            // Cancellation 2: Jupiter aspecting Rahu or Ketu
            val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
            if (jupiterPos != null) {
                if (isAspecting(jupiterPos, rahuPos) || isAspecting(jupiterPos, ketuPos)) {
                    cancellations.add(LocalizableString.Resource(R.string.yoga_cancel_kala_sarpa_jupiter_aspect))
                }
            }

            return Yoga(
                name = LocalizableString.Plain("$kalaSarpaType Kala Sarpa Yoga"),
                sanskritName = "कालसर्प योग - $kalaSarpaType",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.RAHU, Planet.KETU),
                houses = listOf(rahuPos.house, ketuPos.house),
                description = LocalizableString.ResourceWithArgs(
                    R.string.yoga_kala_sarpa_desc,
                    listOf(direction)
                ),
                effects = LocalizableString.ResourceWithArgs(
                    R.string.yoga_effect_kala_sarpa,
                    listOf(getKalaSarpaEffectArea(rahuPos.house))
                ),
                strength = if (cancellations.isEmpty()) YogaStrength.STRONG else YogaStrength.MODERATE,
                strengthPercentage = if (cancellations.isEmpty()) 85.0 else 55.0,
                isAuspicious = false,
                activationPeriod = LocalizableString.Resource(R.string.yoga_activation_rahu_ketu_impactful),
                cancellationFactors = cancellations.ifEmpty { listOf(LocalizableString.Resource(R.string.yoga_cancel_none_present)) }
            )
        }

        return null
    }

    /**
     * Get the primary effect area based on Rahu's house position for Kala Sarpa
     */
    private fun getKalaSarpaEffectArea(rahuHouse: Int): LocalizableString {
        return LocalizableString.Resource(
            when (rahuHouse) {
                1 -> R.string.kala_sarpa_effect_1
                2 -> R.string.kala_sarpa_effect_2
                3 -> R.string.kala_sarpa_effect_3
                4 -> R.string.kala_sarpa_effect_4
                5 -> R.string.kala_sarpa_effect_5
                6 -> R.string.kala_sarpa_effect_6
                7 -> R.string.kala_sarpa_effect_7
                8 -> R.string.kala_sarpa_effect_8
                9 -> R.string.kala_sarpa_effect_9
                10 -> R.string.kala_sarpa_effect_10
                11 -> R.string.kala_sarpa_effect_11
                12 -> R.string.kala_sarpa_effect_12
                else -> R.string.kala_sarpa_effect_various
            }
        )
    }

    // ==================== ADDITIONAL IMPORTANT YOGAS ====================

    /**
     * Calculate additional important yoga types not covered in other categories
     * This includes Parivartana variations, Dasa-Mula, and other significant combinations
     */
    private fun calculateAdditionalYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. Dasa-Mula Yoga - Birth Nakshatra in 8th from Moon
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null) {
            val dasaMulaNakshatras = listOf(
                Nakshatra.ASHWINI,
                Nakshatra.DHANISHTHA,
                Nakshatra.MULA,
                Nakshatra.REVATI
            )
            val (birthNakshatra, _) = Nakshatra.fromLongitude(moonPos.longitude)
            if (birthNakshatra in dasaMulaNakshatras) {
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_dasa_mula),
                        sanskritName = "Dasa-Mula Yoga",
                        category = YogaCategory.NEGATIVE_YOGA,
                        planets = listOf(Planet.MOON),
                        houses = listOf(moonPos.house),
                        description = LocalizableString.Resource(R.string.yoga_dasa_mula_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_dasa_mula),
                        strength = YogaStrength.MODERATE,
                        strengthPercentage = 60.0,
                        isAuspicious = false,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_early_life),
                        cancellationFactors = listOf(
                            LocalizableString.Resource(R.string.yoga_cancel_jupiter_aspect),
                            LocalizableString.Resource(R.string.yoga_cancel_nakshatra_lord_strong),
                            LocalizableString.Resource(R.string.yoga_cancel_benefic_in_4_7)
                        )
                    )
                )
            }
        }

        // 2. Vargottama Yogas - Planets in same sign in Rashi and Navamsa
        // This checks for Rashi-Navamsa alignment for strength
        chart.planetPositions.forEach { pos ->
            // Simplified vargottama check - planet's rashi lord strong indicator
            if (pos.planet in Planet.MAIN_PLANETS && (isInOwnSign(pos) || isExalted(pos))) {
                val strength = calculateYogaStrength(chart, listOf(pos)) * 1.1
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_vargottama_strength),
                        sanskritName = "Vargottama Bala",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(pos.planet),
                        houses = listOf(pos.house),
                        description = LocalizableString.Resource(R.string.yoga_vargottama_strength_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_vargottama_strength),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_dasha),
                        cancellationFactors = emptyList()
                    )
                )
            }
        }

        return yogas
    }

    // ==================== SPECIAL YOGAS ====================

    /**
     * Calculate other special Yogas
     */
    private fun calculateSpecialYogas(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()

        // 1. Budha-Aditya Yoga - Sun-Mercury conjunction
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        val mercuryPos = chart.planetPositions.find { it.planet == Planet.MERCURY }

        if (sunPos != null && mercuryPos != null && areConjunct(sunPos, mercuryPos, customOrb = 6.0)) {
            // Mercury combustion threshold is tighter (12-14° as per classics)
            val distance = abs(sunPos.longitude - mercuryPos.longitude)
            val normalizedDistance = if (distance > 180) 360 - distance else distance
            val isCombust = normalizedDistance < 12.0

            val strength = if (isCombust) 45.0 else calculateYogaStrength(chart, listOf(sunPos, mercuryPos))
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_budha_aditya),
                    sanskritName = "Budha-Aditya Yoga",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.SUN, Planet.MERCURY),
                    houses = listOf(sunPos.house),
                    description = LocalizableString.Resource(R.string.yoga_budha_aditya_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_budha_aditya),
                    strength = strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_sun_mercury_dashas),
                    cancellationFactors = if (isCombust)
                        listOf(LocalizableString.Resource(R.string.yoga_cancel_mercury_combust_reduced))
                    else
                        emptyList()
                )
            )
        }

        // 2. Amala Yoga - Natural benefic in 10th from Lagna or Moon
        val benefics = chart.planetPositions.filter {
            it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        }
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }

        benefics.forEach { beneficPos ->
            if (beneficPos.house == 10 || (moonPos != null && getHouseFrom(beneficPos.sign, moonPos.sign) == 10)) {
                val strength = calculateYogaStrength(chart, listOf(beneficPos))
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_amala),
                        sanskritName = "Amala Yoga",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(beneficPos.planet),
                        houses = listOf(10),
                        description = LocalizableString.Resource(R.string.yoga_amala_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_amala),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_dasha),
                        cancellationFactors = emptyList()
                    )
                )
            }
        }

        // 3. Saraswati Yoga - Jupiter, Venus, Mercury in Kendra/Trikona, Jupiter strong
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }

        if (jupiterPos != null && venusPos != null && mercuryPos != null) {
            val goodHouses = listOf(1, 4, 5, 7, 9, 10)
            val allInGoodHouses = listOf(jupiterPos, venusPos, mercuryPos).all { it.house in goodHouses }
            val jupiterStrong = isInOwnSign(jupiterPos) || isExalted(jupiterPos) || jupiterPos.house in listOf(1, 4, 5, 7, 9, 10)

            if (allInGoodHouses && jupiterStrong) {
                val strength = calculateYogaStrength(chart, listOf(jupiterPos, venusPos, mercuryPos))
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_saraswati),
                        sanskritName = "Saraswati Yoga",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY),
                        houses = listOf(jupiterPos.house, venusPos.house, mercuryPos.house),
                        description = LocalizableString.Resource(R.string.yoga_saraswati_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_saraswati),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_jvm_periods),
                        cancellationFactors = emptyList()
                    )
                )
            }
        }

        // 4. Parvata Yoga - Benefics in Kendras, no malefics in Kendras
        val kendras = listOf(1, 4, 7, 10)
        val beneficsInKendras = chart.planetPositions.filter {
            it.planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY) && it.house in kendras
        }
        val maleficsInKendras = chart.planetPositions.filter {
            it.planet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU) && it.house in kendras
        }

        if (beneficsInKendras.isNotEmpty() && maleficsInKendras.isEmpty()) {
            val strength = calculateYogaStrength(chart, beneficsInKendras)
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_parvata),
                    sanskritName = "Parvata Yoga",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = beneficsInKendras.map { it.planet },
                    houses = beneficsInKendras.map { it.house },
                    description = LocalizableString.Resource(R.string.yoga_parvata_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_parvata),
                    strength = strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_benefic_planet_dashas),
                    cancellationFactors = emptyList()
                )
            )
        }

        // 5. Kahala Yoga - Lords of 4th and 9th strong and connected
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = getHouseLords(ascendantSign)
        val lord4 = houseLords[4]
        val lord9 = houseLords[9]

        if (lord4 != null && lord9 != null) {
            val lord4Pos = chart.planetPositions.find { it.planet == lord4 }
            val lord9Pos = chart.planetPositions.find { it.planet == lord9 }

            if (lord4Pos != null && lord9Pos != null) {
                if (areConjunct(lord4Pos, lord9Pos) || areMutuallyAspecting(lord4Pos, lord9Pos) || areInExchange(lord4Pos, lord9Pos)) {
                    val strength = calculateYogaStrength(chart, listOf(lord4Pos, lord9Pos))
                    yogas.add(
                        Yoga(
                            name = LocalizableString.Resource(R.string.yoga_kahala),
                            sanskritName = "Kahala Yoga",
                            category = YogaCategory.SPECIAL_YOGA,
                            planets = listOf(lord4, lord9),
                            houses = listOf(lord4Pos.house, lord9Pos.house),
                            description = LocalizableString.Resource(R.string.yoga_kahala_desc),
                            effects = LocalizableString.Resource(R.string.yoga_effect_kahala),
                            strength = strengthFromPercentage(strength),
                            strengthPercentage = strength,
                            isAuspicious = true,
                            activationPeriod = LocalizableString.Resource(R.string.yoga_activation_dasha),
                            cancellationFactors = emptyList()
                        )
                    )
                }
            }
        }

        // 6. Shubhakartari Yoga - Benefics in 2nd and 12th from Ascendant (opposite of Papakartari)
        val planetsIn2nd = chart.planetPositions.filter { it.house == 2 }
        val planetsIn12th = chart.planetPositions.filter { it.house == 12 }
        val beneficPlanets = listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)

        val hasBeneficIn2nd = planetsIn2nd.any { it.planet in beneficPlanets }
        val hasBeneficIn12th = planetsIn12th.any { it.planet in beneficPlanets }

        if (hasBeneficIn2nd && hasBeneficIn12th) {
            val beneficsList = (planetsIn2nd.filter { it.planet in beneficPlanets } +
                    planetsIn12th.filter { it.planet in beneficPlanets }).map { it.planet }.distinct()
            val strength = calculateYogaStrength(chart,
                (planetsIn2nd + planetsIn12th).filter { it.planet in beneficPlanets })
            yogas.add(
                Yoga(
                    name = LocalizableString.Resource(R.string.yoga_shubhakartari),
                    sanskritName = "Shubhakartari Yoga",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = beneficsList,
                    houses = listOf(1, 2, 12),
                    description = LocalizableString.Resource(R.string.yoga_shubhakartari_desc),
                    effects = LocalizableString.Resource(R.string.yoga_effect_shubhakartari),
                    strength = strengthFromPercentage(strength),
                    strengthPercentage = strength,
                    isAuspicious = true,
                    activationPeriod = LocalizableString.Resource(R.string.yoga_activation_throughout_life_benefic_dashas),
                    cancellationFactors = emptyList()
                )
            )
        }

        // 7. Sanyasa Yoga - Multiple planets in one house, especially 10th
        // Four or more planets in a single house create renunciation tendencies
        val houseOccupancy = (1..12).associateWith { house ->
            chart.planetPositions.filter { it.house == house && it.planet in Planet.MAIN_PLANETS }
        }

        houseOccupancy.forEach { (house, planets) ->
            if (planets.size >= 4) {
                // Check if Saturn is involved (stronger indication)
                val hasSaturn = planets.any { it.planet == Planet.SATURN }
                val hasKetu = planets.any { it.planet == Planet.KETU }

                // Sanyasa yoga is more prominent in houses 1, 5, 9, 10, 12
                val isSanyasaHouse = house in listOf(1, 5, 9, 10, 12)

                if (isSanyasaHouse || hasSaturn || hasKetu) {
                    val strength = calculateYogaStrength(chart, planets)
                    yogas.add(
                        Yoga(
                            name = LocalizableString.Resource(R.string.yoga_sanyasa),
                            sanskritName = "Sanyasa Yoga",
                            category = YogaCategory.SPECIAL_YOGA,
                            planets = planets.map { it.planet },
                            houses = listOf(house),
                            description = LocalizableString.ResourceWithArgs(
                                R.string.yoga_sanyasa_desc,
                                listOf(planets.size, house)
                            ),
                            effects = LocalizableString.Resource(R.string.yoga_effect_sanyasa),
                            strength = strengthFromPercentage(strength),
                            strengthPercentage = strength,
                            isAuspicious = true, // Spiritually auspicious
                            activationPeriod = LocalizableString.Resource(R.string.yoga_activation_during_conjunct_dashas),
                            cancellationFactors = listOf(
                                LocalizableString.Resource(R.string.yoga_cancel_sanyasa_attachment),
                                LocalizableString.Resource(R.string.yoga_cancel_sanyasa_jupiter_afflicted)
                            )
                        )
                    )
                }
            }
        }

        // 8. Hans Yoga (variant spelling check) - Already covered as Hamsa in Mahapurusha

        // 9. Chamara Yoga - Lagna lord exalted and aspected by Jupiter
        val lord1 = houseLords[1]
        if (lord1 != null) {
            val lord1Pos = chart.planetPositions.find { it.planet == lord1 }
            if (lord1Pos != null && isExalted(lord1Pos)) {
                if (jupiterPos != null && isAspecting(jupiterPos, lord1Pos)) {
                    val strength = calculateYogaStrength(chart, listOf(lord1Pos, jupiterPos))
                    yogas.add(
                        Yoga(
                            name = LocalizableString.Resource(R.string.yoga_chamara),
                            sanskritName = "Chamara Yoga",
                            category = YogaCategory.SPECIAL_YOGA,
                            planets = listOf(lord1, Planet.JUPITER),
                            houses = listOf(lord1Pos.house, jupiterPos.house),
                            description = LocalizableString.Resource(R.string.yoga_chamara_desc),
                            effects = LocalizableString.Resource(R.string.yoga_effect_chamara),
                            strength = strengthFromPercentage(strength),
                            strengthPercentage = strength,
                            isAuspicious = true,
                            activationPeriod = LocalizableString.Resource(R.string.yoga_activation_dasha),
                            cancellationFactors = emptyList()
                        )
                    )
                }
            }
        }

        // 10. Dharma-Karmadhipati Yoga - 9th and 10th lords connected (very important Raja Yoga)
        val lord9Pos = chart.planetPositions.find { it.planet == houseLords[9] }
        val lord10 = houseLords[10]
        val lord10Pos = chart.planetPositions.find { it.planet == lord10 }

        if (lord9Pos != null && lord10Pos != null && houseLords[9] != lord10) {
            if (areConjunct(lord9Pos, lord10Pos) || areMutuallyAspecting(lord9Pos, lord10Pos) || areInExchange(lord9Pos, lord10Pos)) {
                val strength = calculateYogaStrength(chart, listOf(lord9Pos, lord10Pos)) * 1.15 // Extra weight
                yogas.add(
                    Yoga(
                        name = LocalizableString.Resource(R.string.yoga_dharma_karmadhipati),
                        sanskritName = "Dharma-Karmadhipati Yoga",
                        category = YogaCategory.SPECIAL_YOGA,
                        planets = listOf(houseLords[9]!!, lord10!!),
                        houses = listOf(lord9Pos.house, lord10Pos.house),
                        description = LocalizableString.Resource(R.string.yoga_dharma_karmadhipati_desc),
                        effects = LocalizableString.Resource(R.string.yoga_effect_dharma_karmadhipati),
                        strength = strengthFromPercentage(strength),
                        strengthPercentage = strength,
                        isAuspicious = true,
                        activationPeriod = LocalizableString.Resource(R.string.yoga_activation_periods),
                        cancellationFactors = emptyList()
                    )
                )
            }
        }

        return yogas
    }

    // ==================== COMBUSTION & AFFLICTION DETECTION ====================

    /**
     * Combustion orbs based on BPHS and Saravali:
     * - Moon: 12° (but considers phase - dark moon is stronger)
     * - Mars: 17°
     * - Mercury: 14° (direct) / 12° (retrograde - considered less combust)
     * - Jupiter: 11°
     * - Venus: 10° (direct) / 8° (retrograde)
     * - Saturn: 15°
     *
     * Planets within these orbs from Sun are considered combust (Asta).
     * Combustion significantly weakens a planet's ability to deliver yoga results.
     */
    private fun getCombustionOrb(planet: Planet, isRetrograde: Boolean): Double {
        return when (planet) {
            Planet.MOON -> 12.0
            Planet.MARS -> 17.0
            Planet.MERCURY -> if (isRetrograde) 12.0 else 14.0
            Planet.JUPITER -> 11.0
            Planet.VENUS -> if (isRetrograde) 8.0 else 10.0
            Planet.SATURN -> 15.0
            else -> 0.0 // Rahu/Ketu cannot be combust
        }
    }

    /**
     * Check if a planet is combust (Asta) based on Vedic combustion rules.
     * Returns a combustion factor between 0.0 (fully combust) and 1.0 (not combust)
     * that can be used to reduce yoga strength.
     */
    private fun getCombustionFactor(pos: PlanetPosition, chart: VedicChart): Double {
        if (pos.planet == Planet.SUN || pos.planet in listOf(Planet.RAHU, Planet.KETU)) {
            return 1.0 // Sun, Rahu, Ketu cannot be combust
        }

        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return 1.0

        val distance = abs(pos.longitude - sunPos.longitude)
        val normalizedDistance = if (distance > 180) 360 - distance else distance

        val combustionOrb = getCombustionOrb(pos.planet, pos.isRetrograde)

        if (normalizedDistance >= combustionOrb) {
            return 1.0 // Not combust
        }

        // Deep combustion (within 3°) - severely weakens the planet
        if (normalizedDistance <= 3.0) {
            return 0.2 // 80% reduction
        }

        // Calculate gradual combustion factor
        // Closer to Sun = more combust = lower factor
        val combustionDepth = 1.0 - (normalizedDistance / combustionOrb)
        return 1.0 - (combustionDepth * 0.6) // Max 60% reduction at orb edge
    }

    /**
     * Check if a planet is under Papakartari Yoga (hemmed between malefics).
     * This severely restricts a planet's positive effects.
     */
    private fun isPapakartari(pos: PlanetPosition, chart: VedicChart): Boolean {
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)

        val house = pos.house
        val prevHouse = if (house == 1) 12 else house - 1
        val nextHouse = if (house == 12) 1 else house + 1

        val hasMaleficBefore = chart.planetPositions.any {
            it.planet in malefics && it.house == prevHouse
        }
        val hasMaleficAfter = chart.planetPositions.any {
            it.planet in malefics && it.house == nextHouse
        }

        return hasMaleficBefore && hasMaleficAfter
    }

    /**
     * Check if planet is afflicted by malefic aspects using proper Vedic aspects.
     * Returns affliction factor between 0.0 (severely afflicted) and 1.0 (not afflicted)
     *
     * Vedic Aspect Rules:
     * - All planets aspect the 7th house from their position (opposition)
     * - Mars additionally aspects 4th and 8th houses
     * - Jupiter additionally aspects 5th and 9th houses
     * - Saturn additionally aspects 3rd and 10th houses
     * - Rahu/Ketu aspect like Saturn according to some authorities
     */
    private fun getMaleficAfflictionFactor(pos: PlanetPosition, chart: VedicChart): Double {
        val malefics = mapOf(
            Planet.SATURN to 0.25,  // Saturn aspect is most restrictive
            Planet.MARS to 0.20,    // Mars aspect causes aggression/conflicts
            Planet.RAHU to 0.18,    // Rahu creates illusion/obsession
            Planet.KETU to 0.12,    // Ketu creates detachment/confusion
            Planet.SUN to 0.08      // Sun's malefic aspect is milder (ego clashes)
        )

        var totalAffliction = 0.0

        malefics.forEach { (malefic, strength) ->
            if (malefic == pos.planet) return@forEach // Planet can't afflict itself

            val maleficPos = chart.planetPositions.find { it.planet == malefic } ?: return@forEach

            if (isAspecting(maleficPos, pos)) {
                totalAffliction += strength
            }
        }

        // Cap total affliction at 60% reduction
        return (1.0 - min(totalAffliction, 0.6))
    }

    /**
     * Check if aspectingPlanet aspects targetPlanet using Vedic aspect rules.
     * Uses 5° orb for aspect calculations.
     */
    private fun isAspecting(aspectingPlanet: PlanetPosition, targetPlanet: PlanetPosition): Boolean {
        val aspectOrb = 5.0 // Degrees of orb for aspects

        // Calculate house distance (1-12)
        val houseDistance = getHouseFrom(targetPlanet.sign, aspectingPlanet.sign)

        // All planets have 7th house (opposition) aspect
        if (houseDistance == 7) {
            return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 180.0, aspectOrb)
        }

        // Mars special aspects: 4th and 8th houses
        // 4th house = 90° (square), 8th house = 210° (quincunx + 30°)
        if (aspectingPlanet.planet == Planet.MARS) {
            if (houseDistance == 4) {
                return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 90.0, aspectOrb)
            }
            if (houseDistance == 8) {
                // 8th house is (8-1)*30 = 210° ahead
                return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 210.0, aspectOrb)
            }
        }

        // Jupiter special aspects: 5th and 9th houses
        // 5th house = 120° (trine), 9th house = 240° (trine)
        if (aspectingPlanet.planet == Planet.JUPITER) {
            if (houseDistance == 5) {
                return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 120.0, aspectOrb)
            }
            if (houseDistance == 9) {
                return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 240.0, aspectOrb)
            }
        }

        // Saturn special aspects: 3rd and 10th houses
        // 3rd house = 60° (sextile), 10th house = 270° (square from behind)
        if (aspectingPlanet.planet == Planet.SATURN) {
            if (houseDistance == 3) {
                return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 60.0, aspectOrb)
            }
            if (houseDistance == 10) {
                return isWithinAspectOrb(aspectingPlanet.longitude, targetPlanet.longitude, 270.0, aspectOrb)
            }
        }

        // Rahu/Ketu aspects like Saturn (some traditions)
        if (aspectingPlanet.planet in listOf(Planet.RAHU, Planet.KETU)) {
            if (houseDistance == 3 || houseDistance == 10) {
                return true // Using sign-based aspect for nodes
            }
        }

        return false
    }

    /**
     * Check if two longitudes are within orb of an expected aspect angle.
     */
    private fun isWithinAspectOrb(
        long1: Double,
        long2: Double,
        expectedAngle: Double,
        orb: Double
    ): Boolean {
        val actualAngle = abs(long1 - long2)
        val normalizedAngle = if (actualAngle > 180) 360 - actualAngle else actualAngle
        return abs(normalizedAngle - expectedAngle) <= orb
    }

    /**
     * Check if planet receives benefic aspect (Shubha Drishti).
     * Returns a boost factor between 1.0 (no benefic aspect) and 1.3 (strong benefic aspects)
     */
    private fun getBeneficAspectBoost(pos: PlanetPosition, chart: VedicChart): Double {
        val benefics = mapOf(
            Planet.JUPITER to 0.15,  // Jupiter aspect is most beneficial
            Planet.VENUS to 0.10,    // Venus aspect adds comfort/harmony
            Planet.MERCURY to 0.08,  // Mercury aspect adds intelligence (if not afflicted)
            Planet.MOON to 0.05      // Moon aspect adds emotional support (if waxing)
        )

        var totalBoost = 0.0

        benefics.forEach { (benefic, strength) ->
            if (benefic == pos.planet) return@forEach

            val beneficPos = chart.planetPositions.find { it.planet == benefic } ?: return@forEach

            // Skip Moon if waning (weak)
            if (benefic == Planet.MOON) {
                val moonStrength = getMoonPhaseStrength(beneficPos, chart)
                if (moonStrength < 0.5) return@forEach
            }

            // Skip Mercury if combust
            if (benefic == Planet.MERCURY) {
                val combustionFactor = getCombustionFactor(beneficPos, chart)
                if (combustionFactor < 0.6) return@forEach
            }

            if (isAspecting(beneficPos, pos)) {
                totalBoost += strength
            }
        }

        return 1.0 + min(totalBoost, 0.3) // Max 30% boost
    }

    /**
     * Calculate Moon phase strength (Paksha Bala).
     * Returns 0.0-1.0 where 1.0 is full moon and 0.0 is new moon.
     */
    private fun getMoonPhaseStrength(moonPos: PlanetPosition, chart: VedicChart): Double {
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return 0.5

        val distance = (moonPos.longitude - sunPos.longitude + 360) % 360

        // 0° = New Moon, 180° = Full Moon
        return if (distance <= 180) {
            distance / 180.0 // Waxing: 0 to 1
        } else {
            (360 - distance) / 180.0 // Waning: 1 to 0
        }
    }

    /**
     * Calculate the net strength modification factor for a yoga considering all cancellation factors.
     * This is the core function that applies cancellation logic to yoga strength.
     *
     * Factors considered:
     * 1. Combustion (Asta) - proximity to Sun
     * 2. Papakartari - hemmed between malefics
     * 3. Malefic aspects - Saturn, Mars, Rahu aspects
     * 4. Debilitation - planet in fall
     * 5. Enemy sign placement
     * 6. Benefic aspects (positive boost)
     * 7. Strength of yoga-forming planets
     */
    private fun calculateCancellationFactor(
        positions: List<PlanetPosition>,
        chart: VedicChart
    ): Pair<Double, List<LocalizableString>> {
        val cancellationFactors = mutableListOf<LocalizableString>()
        var netFactor = 1.0

        positions.forEach { pos ->
            // 1. Check combustion
            val combustionFactor = getCombustionFactor(pos, chart)
            if (combustionFactor < 0.9) {
                netFactor *= combustionFactor
                if (combustionFactor < 0.5) {
                    cancellationFactors.add(LocalizableString.Resource(R.string.planet_deeply_combust))
                } else if (combustionFactor < 0.8) {
                    cancellationFactors.add(LocalizableString.Resource(R.string.planet_is_combust))
                }
            }

            // 2. Check Papakartari
            if (isPapakartari(pos, chart)) {
                netFactor *= 0.7 // 30% reduction
                cancellationFactors.add(LocalizableString.Resource(R.string.planet_hemmed_between_malefics))
            }

            // 3. Check malefic aspects
            val afflictionFactor = getMaleficAfflictionFactor(pos, chart)
            if (afflictionFactor < 0.9) {
                netFactor *= afflictionFactor
                if (afflictionFactor < 0.7) {
                    cancellationFactors.add(LocalizableString.Resource(R.string.planet_severely_afflicted))
                }
            }

            // 4. Check debilitation (extra reduction if in enemy sign after debilitation)
            if (isDebilitated(pos)) {
                if (!hasNeechaBhanga(pos, chart)) {
                    netFactor *= 0.5 // 50% reduction for uncancelled debilitation
                    cancellationFactors.add(LocalizableString.Resource(R.string.planet_debilitated_no_cancellation))
                }
            }

            // 5. Check enemy sign (Shatru Kshetra)
            if (isInEnemySign(pos)) {
                netFactor *= 0.85 // 15% reduction
                cancellationFactors.add(LocalizableString.Resource(R.string.planet_in_enemy_sign))
            }

            // 6. Benefic aspect boost (positive factor)
            val beneficBoost = getBeneficAspectBoost(pos, chart)
            if (beneficBoost > 1.0) {
                netFactor *= beneficBoost
            }
        }

        return Pair(netFactor.coerceIn(0.1, 1.5), cancellationFactors)
    }

    /**
     * Check if planet is in enemy sign based on natural friendship.
     * Vedic planetary friendships (Naisargika Maitri):
     */
    private fun isInEnemySign(pos: PlanetPosition): Boolean {
        val enemies = getEnemies(pos.planet)
        return pos.sign.ruler in enemies
    }

    /**
     * Get natural enemies of a planet based on BPHS.
     */
    private fun getEnemies(planet: Planet): List<Planet> {
        return when (planet) {
            Planet.SUN -> listOf(Planet.SATURN, Planet.VENUS)
            Planet.MOON -> emptyList() // Moon has no natural enemies
            Planet.MARS -> listOf(Planet.MERCURY)
            Planet.MERCURY -> listOf(Planet.MOON)
            Planet.JUPITER -> listOf(Planet.MERCURY, Planet.VENUS)
            Planet.VENUS -> listOf(Planet.SUN, Planet.MOON)
            Planet.SATURN -> listOf(Planet.SUN, Planet.MOON, Planet.MARS)
            Planet.RAHU -> listOf(Planet.SUN, Planet.MOON)
            Planet.KETU -> listOf(Planet.SUN, Planet.MOON)
            else -> emptyList()
        }
    }

    // ==================== HELPER FUNCTIONS ====================

    private fun getHouseLords(ascendantSign: ZodiacSign): Map<Int, Planet> {
        val lords = mutableMapOf<Int, Planet>()
        for (house in 1..12) {
            val signIndex = (ascendantSign.ordinal + house - 1) % 12
            val sign = ZodiacSign.entries[signIndex]
            lords[house] = sign.ruler
        }
        return lords
    }

    private fun areConjunct(pos1: PlanetPosition, pos2: PlanetPosition, customOrb: Double? = null): Boolean {
        // Use precise orb-based conjunction detection (within 8° as per Vedic astrology standards)
        val distance = abs(pos1.longitude - pos2.longitude)
        val normalizedDistance = if (distance > 180) 360 - distance else distance

        // Conjunction orb: typically 8° in Vedic astrology for accurate detection
        // Can be customized for specific planetary combinations (Mercury conjunctions closer, etc.)
        val orb = customOrb ?: 8.0
        return normalizedDistance <= orb
    }

    private fun areMutuallyAspecting(pos1: PlanetPosition, pos2: PlanetPosition): Boolean {
        val angle = abs(pos1.longitude - pos2.longitude)
        val normalizedAngle = if (angle > 180) 360 - angle else angle
        // Check for opposition (180°) or special aspects
        return normalizedAngle in 170.0..190.0
    }

    private fun areInExchange(pos1: PlanetPosition, pos2: PlanetPosition): Boolean {
        return pos1.sign.ruler == pos2.planet && pos2.sign.ruler == pos1.planet
    }

    private fun isInKendraFrom(pos: PlanetPosition, reference: PlanetPosition): Boolean {
        val house = getHouseFrom(pos.sign, reference.sign)
        return house in listOf(1, 4, 7, 10)
    }

    private fun getHouseFrom(targetSign: ZodiacSign, referenceSign: ZodiacSign): Int {
        val diff = targetSign.number - referenceSign.number
        return if (diff >= 0) diff + 1 else diff + 13
    }

    private fun isInOwnSign(pos: PlanetPosition): Boolean {
        // Delegate to centralized utility for consistency across the codebase
        return VedicAstrologyUtils.isInOwnSign(pos)
    }

    private fun isExalted(pos: PlanetPosition): Boolean {
        // Delegate to centralized utility for consistency across the codebase
        return VedicAstrologyUtils.isExalted(pos)
    }

    private fun isDebilitated(pos: PlanetPosition): Boolean {
        // Delegate to centralized utility for consistency across the codebase
        return VedicAstrologyUtils.isDebilitated(pos)
    }

    private fun hasNeechaBhanga(pos: PlanetPosition, chart: VedicChart): Boolean {
        // Neecha Bhanga conditions:
        // 1. Lord of debilitation sign aspects the debilitated planet
        // 2. Lord of exaltation sign aspects the debilitated planet
        // 3. Debilitated planet is in Kendra from Lagna or Moon
        // 4. Lord of the sign where planet is debilitated is in Kendra from Lagna or Moon

        // Check condition 3
        if (pos.house in listOf(1, 4, 7, 10)) return true

        // Check condition 4
        val debilitatedSignLord = pos.sign.ruler
        val lordPos = chart.planetPositions.find { it.planet == debilitatedSignLord }
        if (lordPos != null && lordPos.house in listOf(1, 4, 7, 10)) return true

        return false
    }

    /**
     * Calculate yoga strength with comprehensive cancellation logic applied.
     * This is the main strength calculation that integrates all Vedic factors.
     *
     * Base strength factors:
     * - Exaltation: +15%
     * - Own sign (Swakshetra): +12%
     * - Friend's sign (Mitra Kshetra): +6%
     * - Kendra/Trikona placement: +8%
     * - Debilitation: -15% (before cancellation check)
     * - Dusthana placement (6,8,12): -10%
     * - Retrograde benefics: +5% (considered stronger)
     *
     * Then applies cancellation factors from calculateCancellationFactor()
     */
    private fun calculateYogaStrength(chart: VedicChart, positions: List<PlanetPosition>): Double {
        var baseStrength = 50.0

        positions.forEach { pos ->
            // Add strength for exaltation
            if (isExalted(pos)) baseStrength += 15.0

            // Add strength for own sign
            if (isInOwnSign(pos)) baseStrength += 12.0

            // Add strength for friend's sign
            if (isInFriendSign(pos)) baseStrength += 6.0

            // Add strength for good houses (Kendra/Trikona)
            if (pos.house in listOf(1, 4, 5, 7, 9, 10)) baseStrength += 8.0

            // Add for 2nd and 11th (wealth houses)
            if (pos.house in listOf(2, 11)) baseStrength += 4.0

            // Reduce for debilitation
            if (isDebilitated(pos)) baseStrength -= 15.0

            // Reduce for 6, 8, 12 placement (Dusthanas)
            if (pos.house in listOf(6, 8, 12)) baseStrength -= 10.0

            // Check for retrograde - benefics gain strength, malefics more intense
            if (pos.isRetrograde) {
                when (pos.planet) {
                    Planet.JUPITER, Planet.VENUS, Planet.MERCURY -> baseStrength += 5.0
                    Planet.SATURN -> baseStrength += 3.0 // Saturn gains focus when retrograde
                    Planet.MARS -> baseStrength -= 2.0 // Mars becomes more erratic
                    else -> {}
                }
            }

            // Dig Bala (directional strength) - planet in its preferred direction
            if (hasDigBala(pos)) baseStrength += 7.0
        }

        // Apply comprehensive cancellation factors
        val (cancellationFactor, _) = calculateCancellationFactor(positions, chart)
        val adjustedStrength = baseStrength * cancellationFactor

        return adjustedStrength.coerceIn(10.0, 100.0)
    }

    /**
     * Calculate yoga strength and return both strength and cancellation reasons.
     * Used when creating Yoga objects to populate cancellationFactors list.
     */
    private fun calculateYogaStrengthWithReasons(
        chart: VedicChart,
        positions: List<PlanetPosition>
    ): Pair<Double, List<LocalizableString>> {
        var baseStrength = 50.0

        positions.forEach { pos ->
            if (isExalted(pos)) baseStrength += 15.0
            if (isInOwnSign(pos)) baseStrength += 12.0
            if (isInFriendSign(pos)) baseStrength += 6.0
            if (pos.house in listOf(1, 4, 5, 7, 9, 10)) baseStrength += 8.0
            if (pos.house in listOf(2, 11)) baseStrength += 4.0
            if (isDebilitated(pos)) baseStrength -= 15.0
            if (pos.house in listOf(6, 8, 12)) baseStrength -= 10.0

            if (pos.isRetrograde) {
                when (pos.planet) {
                    Planet.JUPITER, Planet.VENUS, Planet.MERCURY -> baseStrength += 5.0
                    Planet.SATURN -> baseStrength += 3.0
                    Planet.MARS -> baseStrength -= 2.0
                    else -> {}
                }
            }

            if (hasDigBala(pos)) baseStrength += 7.0
        }

        val (cancellationFactor, cancellationReasons) = calculateCancellationFactor(positions, chart)
        val adjustedStrength = (baseStrength * cancellationFactor).coerceIn(10.0, 100.0)

        return Pair(adjustedStrength, cancellationReasons)
    }

    /**
     * Check if planet has Dig Bala (directional strength).
     * Based on BPHS:
     * - Sun/Mars: Strong in 10th house (South)
     * - Moon/Venus: Strong in 4th house (North)
     * - Mercury/Jupiter: Strong in 1st house (East)
     * - Saturn: Strong in 7th house (West)
     */
    private fun hasDigBala(pos: PlanetPosition): Boolean {
        // Delegate to centralized utility for consistency across the codebase
        return VedicAstrologyUtils.hasDigBala(pos)
    }

    /**
     * Check if planet is in a friend's sign based on natural friendship.
     */
    private fun isInFriendSign(pos: PlanetPosition): Boolean {
        // Delegate to centralized utility for consistency across the codebase
        return VedicAstrologyUtils.isInFriendSign(pos)
    }

    /**
     * Calculate Pancha Mahapurusha Yoga strength with proper cancellation logic.
     * These yogas have specific requirements and cancellation factors per BPHS/Phaladeepika:
     *
     * Full strength conditions:
     * - Planet in Lagna or 10th house (highest strength)
     * - Planet in 7th or 4th house (good strength)
     * - Free from combustion
     * - Not afflicted by malefics
     * - Aspected by benefics (bonus)
     *
     * Cancellation factors (as per classical texts):
     * - Combustion severely reduces Mahapurusha yoga
     * - Malefic aspects reduce results
     * - Placement in Dusthana from Moon weakens it
     */
    private fun calculateMahapurushaStrength(pos: PlanetPosition, chart: VedicChart): Double {
        var strength = 70.0 // Base strength for Mahapurusha

        // Boost for specific house placements (Dig Bala alignment)
        when (pos.house) {
            1 -> strength += 15.0 // In Lagna - strongest
            10 -> strength += 12.0 // In 10th - very strong (especially for Sun/Mars)
            7 -> strength += 10.0 // In 7th - strong (especially for Saturn)
            4 -> strength += 8.0 // In 4th - good (especially for Moon/Venus)
        }

        // Extra boost if planet has Dig Bala
        if (hasDigBala(pos)) strength += 5.0

        // Apply combustion check (critical for Mahapurusha yogas)
        val combustionFactor = getCombustionFactor(pos, chart)
        if (combustionFactor < 1.0) {
            strength *= combustionFactor
        }

        // Check benefic aspects using proper Vedic aspect rules
        val beneficBoost = getBeneficAspectBoost(pos, chart)
        strength *= beneficBoost

        // Check malefic affliction
        val afflictionFactor = getMaleficAfflictionFactor(pos, chart)
        strength *= afflictionFactor

        // Check Papakartari (hemmed between malefics)
        if (isPapakartari(pos, chart)) {
            strength *= 0.75 // 25% reduction
        }

        // Check placement from Moon (Chandra Lagna strength)
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null) {
            val houseFromMoon = getHouseFrom(pos.sign, moonPos.sign)
            // Weak if in Dusthana from Moon
            if (houseFromMoon in listOf(6, 8, 12)) {
                strength *= 0.85
            }
            // Strong if in Kendra from Moon
            if (houseFromMoon in listOf(1, 4, 7, 10)) {
                strength *= 1.1
            }
        }

        return strength.coerceIn(30.0, 100.0)
    }

    /**
     * Calculate Mahapurusha strength with cancellation reasons for UI display.
     */
    private fun calculateMahapurushaStrengthWithReasons(
        pos: PlanetPosition,
        chart: VedicChart
    ): Pair<Double, List<LocalizableString>> {
        val cancellations = mutableListOf<LocalizableString>()
        var strength = 70.0

        when (pos.house) {
            1 -> strength += 15.0
            10 -> strength += 12.0
            7 -> strength += 10.0
            4 -> strength += 8.0
        }

        if (hasDigBala(pos)) strength += 5.0

        val combustionFactor = getCombustionFactor(pos, chart)
        if (combustionFactor < 1.0) {
            strength *= combustionFactor
            if (combustionFactor < 0.6) {
                cancellations.add(LocalizableString.Resource(R.string.mahapurusha_yoga_weakened_combustion))
            }
        }

        strength *= getBeneficAspectBoost(pos, chart)

        val afflictionFactor = getMaleficAfflictionFactor(pos, chart)
        if (afflictionFactor < 0.85) {
            strength *= afflictionFactor
            cancellations.add(LocalizableString.Resource(R.string.mahapurusha_yoga_reduced_malefic_aspects))
        }

        if (isPapakartari(pos, chart)) {
            strength *= 0.75
            cancellations.add(LocalizableString.Resource(R.string.planet_hemmed_between_malefics))
        }

        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPos != null) {
            val houseFromMoon = getHouseFrom(pos.sign, moonPos.sign)
            if (houseFromMoon in listOf(6, 8, 12)) {
                strength *= 0.85
                cancellations.add(LocalizableString.Resource(R.string.mahapurusha_yoga_weak_pos_from_moon))
            } else if (houseFromMoon in listOf(1, 4, 7, 10)) {
                strength *= 1.1
            }
        }

        return Pair(strength.coerceIn(30.0, 100.0), cancellations)
    }

    private fun strengthFromPercentage(percentage: Double): YogaStrength {
        return when {
            percentage >= 85 -> YogaStrength.EXTREMELY_STRONG
            percentage >= 70 -> YogaStrength.STRONG
            percentage >= 50 -> YogaStrength.MODERATE
            percentage >= 30 -> YogaStrength.WEAK
            else -> YogaStrength.VERY_WEAK
        }
    }

    private fun createKendraTrikonaRajaYoga(
        kendraLord: Planet,
        trikonaLord: Planet,
        type: String,
        baseStrength: Double,
        chart: VedicChart
    ): Yoga {
        val kendraPos = chart.planetPositions.find { it.planet == kendraLord }
        val trikonaPos = chart.planetPositions.find { it.planet == trikonaLord }
        val positions = listOfNotNull(kendraPos, trikonaPos)

        // Apply comprehensive cancellation logic
        val (cancellationFactor, cancellationReasons) = calculateCancellationFactor(positions, chart)
        val adjustedStrength = (baseStrength * cancellationFactor).coerceIn(10.0, 100.0)

        return Yoga(
            name = LocalizableString.Resource(R.string.yoga_kendra_trikona_raja),
            sanskritName = "Kendra-Trikona Raja Yoga",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(kendraLord, trikonaLord),
            houses = listOfNotNull(kendraPos?.house, trikonaPos?.house),
            description = LocalizableString.ResourceWithArgs(
                R.string.yoga_kendra_trikona_raja_desc,
                listOf(kendraLord.displayName, trikonaLord.displayName, type)
            ),
            effects = LocalizableString.Resource(R.string.yoga_effect_kendra_trikona_raja),
            strength = strengthFromPercentage(adjustedStrength),
            strengthPercentage = adjustedStrength,
            isAuspicious = true,
            activationPeriod = LocalizableString.ResourceWithArgs(
                R.string.yoga_activation_dasha_antardasha,
                listOf(kendraLord.displayName, trikonaLord.displayName)
            ),
            cancellationFactors = cancellationReasons.ifEmpty { listOf(LocalizableString.Resource(R.string.yoga_cancel_none_unafflicted)) }
        )
    }

    private fun createParivartanaRajaYoga(
        planet1: Planet,
        planet2: Planet,
        baseStrength: Double,
        chart: VedicChart
    ): Yoga {
        val pos1 = chart.planetPositions.find { it.planet == planet1 }
        val pos2 = chart.planetPositions.find { it.planet == planet2 }
        val positions = listOfNotNull(pos1, pos2)

        // Apply comprehensive cancellation logic
        val (cancellationFactor, cancellationReasons) = calculateCancellationFactor(positions, chart)
        val adjustedStrength = (baseStrength * cancellationFactor).coerceIn(10.0, 100.0)

        return Yoga(
            name = LocalizableString.Resource(R.string.yoga_parivartana_raja),
            sanskritName = "Parivartana Raja Yoga",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(planet1, planet2),
            houses = listOfNotNull(pos1?.house, pos2?.house),
            description = LocalizableString.ResourceWithArgs(
                R.string.yoga_parivartana_raja_desc,
                listOf(planet1.displayName, planet2.displayName)
            ),
            effects = LocalizableString.Resource(R.string.yoga_effect_parivartana_raja),
            strength = strengthFromPercentage(adjustedStrength),
            strengthPercentage = adjustedStrength,
            isAuspicious = true,
            activationPeriod = LocalizableString.ResourceWithArgs(
                R.string.yoga_activation_dasha_of,
                listOf(planet1.displayName, planet2.displayName)
            ),
            cancellationFactors = cancellationReasons.ifEmpty { listOf(LocalizableString.Resource(R.string.yoga_cancel_none_unafflicted)) }
        )
    }

    private fun createViparitaRajaYoga(
        planet1: Planet,
        planet2: Planet,
        baseStrength: Double,
        chart: VedicChart
    ): Yoga {
        val pos1 = chart.planetPositions.find { it.planet == planet1 }
        val pos2 = chart.planetPositions.find { it.planet == planet2 }
        val positions = listOfNotNull(pos1, pos2)

        // Apply comprehensive cancellation logic
        val (cancellationFactor, cancellationReasons) = calculateCancellationFactor(positions, chart)
        val adjustedStrength = (baseStrength * cancellationFactor).coerceIn(10.0, 100.0)

        // Add Viparita-specific consideration
        val specificReasons = cancellationReasons.toMutableList()
        // Viparita Raja works best when Dusthana lords are weak; strong lords may not give classical results
        positions.forEach { pos ->
            if (isExalted(pos) || isInOwnSign(pos)) {
                specificReasons.add(
                    LocalizableString.ResourceWithArgs(
                        R.string.yoga_cancel_viparita_strong_lord,
                        listOf(pos.planet.displayName)
                    )
                )
            }
        }

        return Yoga(
            name = LocalizableString.Resource(R.string.yoga_viparita_raja),
            sanskritName = "Viparita Raja Yoga",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(planet1, planet2),
            houses = positions.map { it.house },
            description = LocalizableString.Resource(R.string.yoga_viparita_raja_desc),
            effects = LocalizableString.Resource(R.string.yoga_effect_viparita_raja),
            strength = strengthFromPercentage(adjustedStrength),
            strengthPercentage = adjustedStrength,
            isAuspicious = true,
            activationPeriod = LocalizableString.ResourceWithArgs(
                R.string.yoga_activation_periods_of,
                listOf(planet1.displayName, planet2.displayName)
            ),
            cancellationFactors = specificReasons.ifEmpty { listOf(LocalizableString.Resource(R.string.yoga_cancel_none_unafflicted)) }
        )
    }

    private fun createNeechaBhangaRajaYoga(
        planet: Planet,
        baseStrength: Double,
        chart: VedicChart
    ): Yoga {
        val pos = chart.planetPositions.find { it.planet == planet }

        // For Neecha Bhanga, we apply different cancellation rules
        val cancellationReasons = mutableListOf<LocalizableString>()
        var adjustedStrength = baseStrength

        if (pos != null) {
            // Check combustion (still affects the planet)
            val combustionFactor = getCombustionFactor(pos, chart)
            if (combustionFactor < 0.9) {
                adjustedStrength *= combustionFactor
                if (combustionFactor < 0.6) {
                    cancellationReasons.add(
                        LocalizableString.ResourceWithArgs(
                            R.string.yoga_cancel_neecha_bhanga_combust,
                            listOf(planet.displayName)
                        )
                    )
                }
            }

            // Check malefic aspects
            val afflictionFactor = getMaleficAfflictionFactor(pos, chart)
            if (afflictionFactor < 0.85) {
                adjustedStrength *= afflictionFactor
                cancellationReasons.add(LocalizableString.Resource(R.string.yoga_cancel_malefic_aspects_effectiveness))
            }

            // Check Papakartari
            if (isPapakartari(pos, chart)) {
                adjustedStrength *= 0.8
                cancellationReasons.add(LocalizableString.Resource(R.string.yoga_cancel_hemmed_between_malefics))
            }

            // Benefic aspects boost Neecha Bhanga
            val beneficBoost = getBeneficAspectBoost(pos, chart)
            if (beneficBoost > 1.0) {
                adjustedStrength *= beneficBoost
            }

            // Identify the cancellation type for informational purposes
            if (pos.house in listOf(1, 4, 7, 10)) {
                cancellationReasons.add(
                    0,
                    LocalizableString.Resource(R.string.yoga_neecha_bhanga_via_kendra)
                )
            } else {
                val debilitatedSignLord = pos.sign.ruler
                val lordPos = chart.planetPositions.find { it.planet == debilitatedSignLord }
                if (lordPos != null && lordPos.house in listOf(1, 4, 7, 10)) {
                    cancellationReasons.add(
                        0,
                        LocalizableString.Resource(R.string.yoga_neecha_bhanga_via_sign_lord_kendra)
                    )
                }
            }
        }

        return Yoga(
            name = LocalizableString.Resource(R.string.yoga_neecha_bhanga_raja),
            sanskritName = "Neecha Bhanga Raja Yoga",
            category = YogaCategory.RAJA_YOGA,
            planets = listOf(planet),
            houses = listOfNotNull(pos?.house),
            description = LocalizableString.ResourceWithArgs(
                R.string.yoga_neecha_bhanga_raja_desc,
                listOf(planet.displayName)
            ),
            effects = LocalizableString.Resource(R.string.yoga_effect_neecha_bhanga_raja),
            strength = strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
            strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = LocalizableString.ResourceWithArgs(
                R.string.yoga_activation_dasha_of_planet,
                listOf(planet.displayName)
            ),
            cancellationFactors = cancellationReasons.ifEmpty { listOf(LocalizableString.Resource(R.string.yoga_cancel_clean_neecha_bhanga)) }
        )
    }

    private fun createNabhasaYoga(
        name: String,
        sanskritName: String,
        description: String,
        effects: String
    ): Yoga {
        return Yoga(
            name = name,
            sanskritName = sanskritName,
            category = YogaCategory.NABHASA_YOGA,
            planets = emptyList(),
            houses = emptyList(),
            description = description,
            effects = effects,
            strength = YogaStrength.MODERATE,
            strengthPercentage = 60.0,
            isAuspicious = !effects.lowercase().contains("poor") && !effects.lowercase().contains("dirty"),
            activationPeriod = "Throughout life",
            cancellationFactors = emptyList()
        )
    }

    private fun getLocalizedHouseSignifications(house: Int): LocalizableString {
        return LocalizableString.Resource(
            when (house) {
                1 -> R.string.house_1_signification
                2 -> R.string.house_2_signification
                3 -> R.string.house_3_signification
                4 -> R.string.house_4_signification
                5 -> R.string.house_5_signification
                6 -> R.string.house_6_signification
                7 -> R.string.house_7_signification
                8 -> R.string.house_8_signification
                9 -> R.string.house_9_signification
                10 -> R.string.house_10_signification
                11 -> R.string.house_11_signification
                12 -> R.string.house_12_signification
                else -> R.string.various_activities
            }
        )
    }
}
