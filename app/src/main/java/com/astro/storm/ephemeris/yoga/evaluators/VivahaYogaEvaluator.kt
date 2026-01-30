package com.astro.storm.ephemeris.yoga.evaluators

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.yoga.Yoga
import com.astro.storm.ephemeris.yoga.YogaCategory
import com.astro.storm.ephemeris.yoga.YogaEvaluator
import com.astro.storm.ephemeris.yoga.YogaHelpers
import com.astro.storm.ephemeris.yoga.YogaStrength

/**
 * Vivaha (Marriage) Yoga Evaluator
 *
 * Evaluates all marriage, relationship, and partnership-related yogas based on classical Vedic texts.
 * The 7th house (Kalatra Bhava) is the primary house for marriage, along with Venus (natural significator),
 * 2nd house (family), and 4th house (domestic happiness).
 *
 * Yogas Evaluated:
 * - Kalatra Yoga (Spouse Quality)
 * - Shubha Kalatra Yoga (Auspicious Spouse)
 * - Dwikalatra Yoga (Multiple Marriages)
 * - Bahustri Yoga (Multiple Marriages - Male)
 * - Manglik Dosha (Mars Affliction)
 * - Kuja Dosha Bhanga (Mars Dosha Cancellation)
 * - Vivaha Badhaka Yoga (Marriage Obstacles)
 * - Sukha Vivaha Yoga (Happy Marriage)
 * - Kalatrahani Yoga (Loss of Spouse)
 * - Sama Saptama Yoga (Balanced Partnership)
 * - Stri Jataka Yogas (Female-specific)
 * - Purusha Jataka Yogas (Male-specific)
 * - Upapada Yogas (From Upapada Lagna)
 * - Venus-based Marriage Yogas
 * - Late Marriage Yogas
 * - Early Marriage Yogas
 *
 * Based on:
 * - Brihat Parashara Hora Shastra (BPHS)
 * - Phaladeepika
 * - Jataka Parijata
 * - Saravali
 * - Muhurta Chintamani
 *
 * @author AstroStorm
 */
class VivahaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.SPECIAL_YOGA

    companion object {
        private val KENDRA_HOUSES = setOf(1, 4, 7, 10)
        private val TRIKONA_HOUSES = setOf(1, 5, 9)
        private val DUSTHANA_HOUSES = setOf(6, 8, 12)
        private val UPACHAYA_HOUSES = setOf(3, 6, 10, 11)
        
        // Manglik houses (for Kuja Dosha)
        private val MANGLIK_HOUSES = setOf(1, 2, 4, 7, 8, 12)
        
        // Benefic planets for marriage
        private val MARRIAGE_BENEFICS = setOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        
        // Malefic planets for marriage
        private val MARRIAGE_MALEFICS = setOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)
    }

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // Core Marriage Yogas
        evaluateKalatraYoga(chart, houseLords)?.let { yogas.addAll(it) }
        evaluateShubhaKalatraYoga(chart, houseLords)?.let { yogas.add(it) }
        
        // Manglik Dosha Analysis
        evaluateManglikDosha(chart, houseLords)?.let { yogas.add(it) }
        evaluateKujaDoshaBhanga(chart, houseLords)?.let { yogas.add(it) }
        
        // Multiple Marriage Yogas
        evaluateDwikalatraYoga(chart, houseLords)?.let { yogas.addAll(it) }
        
        // Marriage Timing Yogas
        evaluateLateMarriageYoga(chart, houseLords)?.let { yogas.addAll(it) }
        evaluateEarlyMarriageYoga(chart, houseLords)?.let { yogas.add(it) }
        
        // Marriage Quality Yogas
        evaluateSukhaVivahaYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateVivahaVighnYoga(chart, houseLords)?.let { yogas.addAll(it) }
        evaluateSamaSaptamaYoga(chart, houseLords)?.let { yogas.add(it) }
        
        // Spouse Loss/Separation Yogas
        evaluateKalatrahaniYoga(chart, houseLords)?.let { yogas.addAll(it) }
        evaluateVidhavaYoga(chart, houseLords)?.let { yogas.add(it) }
        
        // Venus-based Yogas
        evaluateVenusMarriageYogas(chart, houseLords)?.let { yogas.addAll(it) }
        
        // 7th Lord Position Yogas
        evaluateSeventhLordYogas(chart, houseLords)?.let { yogas.addAll(it) }
        
        // Special Combinations
        evaluateStriDirghaYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateBharyaSaubhagyaYoga(chart, houseLords)?.let { yogas.add(it) }
        evaluateJaraVivahaYoga(chart, houseLords)?.let { yogas.add(it) }

        return yogas
    }

    /**
     * Kalatra Yoga - General Spouse Quality
     * Based on 7th house, 7th lord, and Venus
     */
    private fun evaluateKalatraYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val lord7 = houseLords[7] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null

        // Check planets in 7th house
        val planetsIn7th = chart.planetPositions.filter { it.house == 7 }
        
        // Benefics in 7th
        val beneficsIn7th = planetsIn7th.filter { it.planet in MARRIAGE_BENEFICS }
        if (beneficsIn7th.isNotEmpty()) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, beneficsIn7th)
            
            yogas.add(Yoga(
                name = "Shubha Saptama Yoga",
                sanskritName = "शुभ सप्तम योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = beneficsIn7th.map { it.planet },
                houses = listOf(7),
                description = "Benefic(s) in 7th house: ${beneficsIn7th.joinToString { it.planet.displayName }}",
                effects = "Auspicious marriage, attractive spouse, harmonious partnership, " +
                        "happiness through marriage, supportive life partner, " +
                        "good marital relations",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = true,
                activationPeriod = "Dashas of benefics in 7th house",
                cancellationFactors = emptyList()
            ))
        }

        // Malefics in 7th (without benefic aspect)
        val maleficsIn7th = planetsIn7th.filter { it.planet in MARRIAGE_MALEFICS }
        if (maleficsIn7th.isNotEmpty()) {
            // Check for benefic aspects
            val hasBeneficAspect = chart.planetPositions.any { pos ->
                pos.planet in MARRIAGE_BENEFICS && 
                maleficsIn7th.any { malefic -> YogaHelpers.isAspecting(pos, malefic) }
            }
            
            if (!hasBeneficAspect) {
                val strengthPct = YogaHelpers.calculateYogaStrength(chart, maleficsIn7th)
                
                yogas.add(Yoga(
                    name = "Papa Saptama Yoga",
                    sanskritName = "पाप सप्तम योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = maleficsIn7th.map { it.planet },
                    houses = listOf(7),
                    description = "Malefic(s) in 7th house: ${maleficsIn7th.joinToString { it.planet.displayName }}",
                    effects = "Challenges in marriage, disagreements with spouse, " +
                            "delay or difficulty in marriage, need for patience in partnership, " +
                            "spouse may have health issues or difficult temperament",
                    strength = YogaHelpers.strengthFromPercentage(strengthPct),
                    strengthPercentage = strengthPct,
                    isAuspicious = false,
                    activationPeriod = "Dashas of malefics in 7th house",
                    cancellationFactors = listOf(
                        "Benefic aspects mitigate",
                        "7th lord strong helps",
                        "Venus well-placed provides relief"
                    )
                ))
            }
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Shubha Kalatra Yoga - Auspicious Spouse
     * 7th lord in Kendra/Trikona, strong Venus
     */
    private fun evaluateShubhaKalatraYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord7 = houseLords[7] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null

        val goodHouses = KENDRA_HOUSES + TRIKONA_HOUSES

        // 7th lord in good house
        val lord7InGoodHouse = pos7.house in goodHouses
        // Venus strong
        val venusStrong = YogaHelpers.isExalted(venusPos) || 
                YogaHelpers.isInOwnSign(venusPos) || 
                YogaHelpers.isInFriendSign(venusPos)
        // Venus not combust
        val venusNotCombust = YogaHelpers.getCombustionFactor(venusPos, chart) > 0.6

        // Need at least two conditions
        val conditionsMet = listOf(lord7InGoodHouse, venusStrong, venusNotCombust).count { it }
        if (conditionsMet < 2) return null

        // 7th lord should not be debilitated
        if (YogaHelpers.isDebilitated(pos7)) return null

        val positions = listOf(pos7, venusPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)
        val adjustedStrength = strengthPct * (1 + conditionsMet * 0.05)

        return Yoga(
            name = "Shubha Kalatra Yoga",
            sanskritName = "शुभ कलत्र योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(lord7, Planet.VENUS),
            houses = listOf(7, pos7.house, venusPos.house),
            description = "7th lord ${lord7.displayName} well-placed with strong Venus",
            effects = "Beautiful, virtuous, and supportive spouse; happy marriage; " +
                    "spouse from good family; love and harmony in relationship; " +
                    "spouse brings prosperity; lasting marital bond",
            strength = YogaHelpers.strengthFromPercentage(adjustedStrength.coerceIn(10.0, 100.0)),
            strengthPercentage = adjustedStrength.coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "Dasha of ${lord7.displayName} or Venus",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Manglik Dosha (Kuja Dosha) - Mars Affliction
     * Mars in 1, 2, 4, 7, 8, or 12 from Lagna, Moon, or Venus
     */
    private fun evaluateManglikDosha(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS } ?: return null
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null

        // Check from Lagna
        val manglikFromLagna = marsPos.house in MANGLIK_HOUSES

        // Check from Moon
        val houseFromMoon = YogaHelpers.getHouseFrom(marsPos.sign, moonPos.sign)
        val manglikFromMoon = houseFromMoon in MANGLIK_HOUSES

        // Check from Venus
        val houseFromVenus = YogaHelpers.getHouseFrom(marsPos.sign, venusPos.sign)
        val manglikFromVenus = houseFromVenus in MANGLIK_HOUSES

        // Determine severity
        val manglikCount = listOf(manglikFromLagna, manglikFromMoon, manglikFromVenus).count { it }
        
        if (manglikCount == 0) return null

        // Check for cancellation conditions
        val cancellations = mutableListOf<String>()
        
        // Cancellation 1: Mars in own sign or exalted
        if (YogaHelpers.isExalted(marsPos) || YogaHelpers.isInOwnSign(marsPos)) {
            cancellations.add("Mars in own/exalted sign - dosha reduced")
        }
        
        // Cancellation 2: Mars aspected by Jupiter
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, marsPos)) {
            cancellations.add("Jupiter's aspect on Mars - dosha mitigated")
        }
        
        // Cancellation 3: Mars with benefics
        val beneficsWithMars = chart.planetPositions.filter {
            it.planet in listOf(Planet.JUPITER, Planet.VENUS) && it.house == marsPos.house
        }
        if (beneficsWithMars.isNotEmpty()) {
            cancellations.add("Benefics conjunct Mars - dosha reduced")
        }
        
        // Cancellation 4: Mars in 1, 4 in Leo/Aries/Scorpio/Capricorn
        if (marsPos.house in listOf(1, 4) && 
            marsPos.sign in listOf(ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SCORPIO, ZodiacSign.CAPRICORN)) {
            cancellations.add("Mars in friendly sign in 1st/4th - dosha cancelled")
        }
        
        // Cancellation 5: Mars in 2nd in Gemini/Virgo
        if (marsPos.house == 2 && marsPos.sign in listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO)) {
            cancellations.add("Mars in 2nd in Gemini/Virgo - dosha cancelled")
        }
        
        // Cancellation 6: Mars in 7th in Cancer/Capricorn
        if (marsPos.house == 7 && marsPos.sign in listOf(ZodiacSign.CANCER, ZodiacSign.CAPRICORN)) {
            cancellations.add("Mars in 7th in Cancer/Capricorn - dosha reduced")
        }
        
        // Cancellation 7: Mars in 8th in Sagittarius/Pisces
        if (marsPos.house == 8 && marsPos.sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES)) {
            cancellations.add("Mars in 8th in Sagittarius/Pisces - dosha cancelled")
        }
        
        // Cancellation 8: Mars in 12th in Taurus/Libra
        if (marsPos.house == 12 && marsPos.sign in listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA)) {
            cancellations.add("Mars in 12th in Taurus/Libra - dosha cancelled")
        }

        // If major cancellation present, don't report dosha
        val majorCancellation = cancellations.size >= 2 || 
                (YogaHelpers.isExalted(marsPos) && jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, marsPos))
        
        if (majorCancellation) return null

        // Calculate severity
        var severity = when (manglikCount) {
            3 -> 85.0
            2 -> 65.0
            else -> 45.0
        }
        
        // Reduce severity based on cancellations
        severity *= (1 - cancellations.size * 0.15)

        val description = buildString {
            append("Mars in ${ordinal(marsPos.house)} house")
            if (manglikFromLagna) append(" from Lagna")
            if (manglikFromMoon) append(", ${ordinal(houseFromMoon)} from Moon")
            if (manglikFromVenus) append(", ${ordinal(houseFromVenus)} from Venus")
        }

        val effects = when (marsPos.house) {
            1 -> "Aggressive personality may affect marriage, dominating nature, " +
                    "health issues for spouse, passionate but argumentative"
            2 -> "Harsh speech affects family life, financial disputes in marriage, " +
                    "arguments with in-laws, need for diplomatic communication"
            4 -> "Domestic discord, property disputes with spouse, " +
                    "mother-spouse conflicts, restlessness at home"
            7 -> "Most significant Manglik placement, strong personality clashes, " +
                    "spouse may have health issues, passionate but turbulent relationship"
            8 -> "Concerns about longevity of marriage, obstacles in married life, " +
                    "spouse's family issues, transformation through partnership"
            12 -> "Expenses through marriage, spouse from distant place, " +
                    "bedroom disharmony, spiritual journey through relationship"
            else -> "Mars energy affects marital harmony"
        }

        return Yoga(
            name = "Manglik Dosha",
            sanskritName = "मांगलिक दोष",
            category = YogaCategory.NEGATIVE_YOGA,
            planets = listOf(Planet.MARS),
            houses = listOf(marsPos.house, 7),
            description = description,
            effects = effects,
            strength = YogaHelpers.strengthFromPercentage(severity),
            strengthPercentage = severity.coerceIn(10.0, 100.0),
            isAuspicious = false,
            activationPeriod = "Mars Dasha/Antardasha, 7th lord periods",
            cancellationFactors = cancellations.ifEmpty { listOf("Marriage with another Manglik person") }
        )
    }

    /**
     * Kuja Dosha Bhanga - Mars Dosha Cancellation
     * When Mars dosha is fully cancelled
     */
    private fun evaluateKujaDoshaBhanga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS } ?: return null
        
        // Check if Mars is in Manglik house
        val manglikFromLagna = marsPos.house in MANGLIK_HOUSES
        if (!manglikFromLagna) return null

        val cancellations = mutableListOf<String>()
        var isCancelled = false

        // Cancellation 1: Mars in own sign (Aries, Scorpio) or exalted (Capricorn)
        if (YogaHelpers.isExalted(marsPos) || YogaHelpers.isInOwnSign(marsPos)) {
            cancellations.add("Mars in dignity")
            isCancelled = true
        }

        // Cancellation 2: 7th lord in Kendra with benefic aspect
        val lord7 = houseLords[7]
        val pos7 = lord7?.let { chart.planetPositions.find { it.planet == lord7 } }
        if (pos7 != null && pos7.house in KENDRA_HOUSES) {
            val hasBeneficAspect = chart.planetPositions.any { pos ->
                pos.planet in listOf(Planet.JUPITER, Planet.VENUS) && 
                YogaHelpers.isAspecting(pos, pos7)
            }
            if (hasBeneficAspect) {
                cancellations.add("7th lord in Kendra with benefic aspect")
                isCancelled = true
            }
        }

        // Cancellation 3: Venus and Jupiter aspect 7th house
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS }
        val seventhHouseSign = ZodiacSign.entries[(ZodiacSign.fromLongitude(chart.ascendant).ordinal + 6) % 12]
        
        // Create dummy position for 7th house
        val dummyPos = marsPos.copy(sign = seventhHouseSign, house = 7)
        
        val jupiterAspects7th = jupiterPos != null && YogaHelpers.isAspecting(jupiterPos, dummyPos)
        val venusIn7thOrAspects = venusPos != null && (venusPos.house == 7 || YogaHelpers.isAspecting(venusPos, dummyPos))
        
        if (jupiterAspects7th && venusIn7thOrAspects) {
            cancellations.add("Jupiter and Venus influence 7th house")
            isCancelled = true
        }

        // Cancellation 4: Benefics in 1, 4, 7, 8, 12 houses
        val beneficsInManglikHouses = chart.planetPositions.filter {
            it.planet in listOf(Planet.JUPITER, Planet.VENUS) && it.house in MANGLIK_HOUSES
        }
        if (beneficsInManglikHouses.size >= 2) {
            cancellations.add("Multiple benefics in Manglik houses")
            isCancelled = true
        }

        if (!isCancelled || cancellations.isEmpty()) return null

        val strengthPct = 70.0 + cancellations.size * 10

        return Yoga(
            name = "Kuja Dosha Bhanga",
            sanskritName = "कुज दोष भंग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(Planet.MARS),
            houses = listOf(marsPos.house, 7),
            description = "Mars Dosha cancelled: ${cancellations.joinToString(", ")}",
            effects = "Manglik dosha neutralized, normal marriage prospects, " +
                    "Mars energy channeled constructively, partnership success possible, " +
                    "reduced need for Manglik matching",
            strength = YogaHelpers.strengthFromPercentage(strengthPct.coerceIn(10.0, 100.0)),
            strengthPercentage = strengthPct.coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "Throughout life, especially in Jupiter/Venus periods",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Dwikalatra Yoga - Multiple Marriages
     * Various combinations indicating more than one marriage
     */
    private fun evaluateDwikalatraYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val lord7 = houseLords[7] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null

        // Type 1: 7th lord in dual sign (Gemini, Virgo, Sagittarius, Pisces)
        val dualSigns = listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES)
        if (pos7.sign in dualSigns) {
            // Check if afflicted by malefics
            val isAfflicted = chart.planetPositions.any { pos ->
                pos.planet in MARRIAGE_MALEFICS && 
                (pos.house == pos7.house || YogaHelpers.isAspecting(pos, pos7))
            }
            
            if (isAfflicted) {
                yogas.add(Yoga(
                    name = "Dwikalatra Yoga (Dual Sign)",
                    sanskritName = "द्विकलत्र योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(lord7),
                    houses = listOf(7, pos7.house),
                    description = "7th lord ${lord7.displayName} in dual sign ${pos7.sign.displayName} with malefic influence",
                    effects = "Possibility of second marriage, remarriage after separation/widowhood, " +
                            "multiple significant relationships, need for flexibility in partnership",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 55.0,
                    isAuspicious = false,
                    activationPeriod = "7th lord Dasha, especially in malefic sub-periods",
                    cancellationFactors = listOf(
                        "Jupiter's aspect on 7th house protects",
                        "Strong Venus maintains single marriage"
                    )
                ))
            }
        }

        // Type 2: Venus in dual sign with malefic association
        if (venusPos.sign in dualSigns) {
            val venusAfflicted = chart.planetPositions.any { pos ->
                pos.planet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU) && 
                (pos.house == venusPos.house || YogaHelpers.isAspecting(pos, venusPos))
            }
            
            if (venusAfflicted) {
                yogas.add(Yoga(
                    name = "Dwikalatra Yoga (Venus)",
                    sanskritName = "द्विकलत्र योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.VENUS),
                    houses = listOf(venusPos.house, 7),
                    description = "Venus in dual sign ${venusPos.sign.displayName} with malefic affliction",
                    effects = "Multiple romantic relationships, possibility of remarriage, " +
                            "complex love life, learning through relationships",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 50.0,
                    isAuspicious = false,
                    activationPeriod = "Venus Dasha, malefic sub-periods",
                    cancellationFactors = listOf(
                        "Jupiter's aspect protects",
                        "7th lord strong maintains stability"
                    )
                ))
            }
        }

        // Type 3: Mars and Venus in 7th
        val marsIn7th = chart.planetPositions.any { it.planet == Planet.MARS && it.house == 7 }
        val venusIn7th = venusPos.house == 7
        
        if (marsIn7th && venusIn7th) {
            yogas.add(Yoga(
                name = "Bahustri Yoga",
                sanskritName = "बहुस्त्री योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.MARS, Planet.VENUS),
                houses = listOf(7),
                description = "Mars and Venus together in 7th house",
                effects = "Multiple marriages or relationships, passionate love life, " +
                        "strong romantic desires, need for balance in relationships",
                strength = YogaStrength.STRONG,
                strengthPercentage = 65.0,
                isAuspicious = false,
                activationPeriod = "Mars or Venus Dasha",
                cancellationFactors = listOf(
                    "Jupiter's aspect on 7th provides stability",
                    "Saturn's aspect delays but stabilizes"
                )
            ))
        }

        // Type 4: 2nd and 7th lords conjunct in 6, 8, or 12
        val lord2 = houseLords[2]
        val pos2 = lord2?.let { chart.planetPositions.find { it.planet == lord2 } }
        
        if (pos2 != null && lord2 != lord7) {
            val areConjunct = YogaHelpers.areConjunct(pos2, pos7)
            val inDusthana = pos2.house in DUSTHANA_HOUSES && pos7.house in DUSTHANA_HOUSES
            
            if (areConjunct && inDusthana) {
                yogas.add(Yoga(
                    name = "Kutumba-Kalatra Dosha",
                    sanskritName = "कुटुंब-कलत्र दोष",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(lord2, lord7),
                    houses = listOf(2, 7, pos2.house),
                    description = "2nd and 7th lords conjunct in ${ordinal(pos2.house)} house (dusthana)",
                    effects = "Family-marriage conflicts, first marriage difficulties, " +
                            "financial issues affect marriage, remarriage possibility",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 55.0,
                    isAuspicious = false,
                    activationPeriod = "Dasha of 2nd or 7th lord",
                    cancellationFactors = listOf(
                        "Benefic aspects mitigate",
                        "Strong lagna lord protects"
                    )
                ))
            }
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Late Marriage Yoga - Delayed marriage indicators
     */
    private fun evaluateLateMarriageYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val lord7 = houseLords[7] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN } ?: return null

        // Type 1: Saturn in 7th house
        if (saturnPos.house == 7) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(saturnPos))
            
            yogas.add(Yoga(
                name = "Shani Saptama - Late Marriage",
                sanskritName = "शनि सप्तम विलंब विवाह",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.SATURN),
                houses = listOf(7),
                description = "Saturn in 7th house",
                effects = "Marriage after 28-30 years, mature spouse, " +
                        "responsible partnership, marriage to older person, " +
                        "enduring but initially delayed marriage, karmic partnership",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = true, // Not negative, just delayed
                activationPeriod = "After Saturn return (age 28-30), Saturn Dasha",
                cancellationFactors = listOf(
                    "Jupiter's aspect can bring earlier marriage",
                    "Venus strong may reduce delay"
                )
            ))
        }

        // Type 2: Saturn aspects 7th house or 7th lord
        val saturnAspects7th = saturnPos.house in listOf(1, 5, 9, 10) // Saturn's aspects reach 7th
        val saturnAspectsLord7 = YogaHelpers.isAspecting(saturnPos, pos7)
        
        if ((saturnAspects7th || saturnAspectsLord7) && saturnPos.house != 7) {
            yogas.add(Yoga(
                name = "Shani Drishti Vivaha Vilamba",
                sanskritName = "शनि दृष्टि विवाह विलंब",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.SATURN, lord7),
                houses = listOf(saturnPos.house, 7),
                description = "Saturn aspects 7th house or 7th lord from ${ordinal(saturnPos.house)} house",
                effects = "Delayed marriage due to circumstances, career priority, " +
                        "cautious approach to commitment, marriage after settling down, " +
                        "mature choice of partner",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 55.0,
                isAuspicious = true,
                activationPeriod = "Marriage likely after Saturn maturity (36 years)",
                cancellationFactors = listOf("Strong Venus/Jupiter can expedite")
            ))
        }

        // Type 3: 7th lord in 6, 8, or 12
        if (pos7.house in DUSTHANA_HOUSES) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(pos7))
            
            val effects = when (pos7.house) {
                6 -> "Marriage delayed due to health/service commitments, spouse through work"
                8 -> "Sudden marriage after delays, transformative partnership, hidden obstacles"
                12 -> "Foreign spouse possibility, marriage in distant place, spiritual partnership"
                else -> "Delayed marriage"
            }
            
            yogas.add(Yoga(
                name = "Saptamesh Dusthana - Delayed Marriage",
                sanskritName = "सप्तमेश दुस्थान विलंब",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord7),
                houses = listOf(7, pos7.house),
                description = "7th lord ${lord7.displayName} in ${ordinal(pos7.house)} house",
                effects = effects,
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = false,
                activationPeriod = "7th lord Dasha brings marriage opportunity",
                cancellationFactors = listOf(
                    "Benefic aspects help",
                    "Strong Venus mitigates delay"
                )
            ))
        }

        // Type 4: Venus combust
        val venusCombust = YogaHelpers.getCombustionFactor(venusPos, chart) < 0.6
        if (venusCombust) {
            yogas.add(Yoga(
                name = "Shukra Asta - Venus Combust Marriage Delay",
                sanskritName = "शुक्र अस्त विवाह विलंब",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.VENUS, Planet.SUN),
                houses = listOf(venusPos.house),
                description = "Venus combust (too close to Sun)",
                effects = "Romantic difficulties, self-esteem issues affect relationships, " +
                        "delayed recognition of love, marriage after overcoming ego issues",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 50.0,
                isAuspicious = false,
                activationPeriod = "Venus Dasha may bring challenges first",
                cancellationFactors = listOf(
                    "Jupiter's aspect on Venus helps",
                    "Strong 7th lord compensates"
                )
            ))
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Early Marriage Yoga
     */
    private fun evaluateEarlyMarriageYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord7 = houseLords[7] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return null

        val conditions = mutableListOf<String>()

        // Venus in own sign or exalted
        if (YogaHelpers.isExalted(venusPos) || YogaHelpers.isInOwnSign(venusPos)) {
            conditions.add("Venus dignified")
        }

        // 7th lord in Kendra
        if (pos7.house in KENDRA_HOUSES) {
            conditions.add("7th lord in Kendra")
        }

        // Moon strong and aspecting 7th or Venus
        val moonStrong = YogaHelpers.getMoonPhaseStrength(moonPos, chart) > 0.6
        if (moonStrong && (moonPos.house == 7 || YogaHelpers.isAspecting(moonPos, venusPos))) {
            conditions.add("Strong Moon influences marriage")
        }

        // Jupiter aspects 7th house or Venus
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null) {
            val jupiterAspects7th = jupiterPos.house in listOf(1, 3, 5, 9, 11) // Houses from which Jupiter aspects 7th
            if (jupiterAspects7th || YogaHelpers.isAspecting(jupiterPos, venusPos)) {
                conditions.add("Jupiter's beneficial influence")
            }
        }

        // Need at least 3 conditions for early marriage
        if (conditions.size < 3) return null

        val positions = listOf(pos7, venusPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        return Yoga(
            name = "Shighra Vivaha Yoga",
            sanskritName = "शीघ्र विवाह योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(lord7, Planet.VENUS),
            houses = listOf(7, pos7.house, venusPos.house),
            description = "Early marriage indicators: ${conditions.joinToString(", ")}",
            effects = "Marriage at young age (before 25), romantic nature, " +
                    "quick decisions in love, early family life, " +
                    "strong desire for partnership",
            strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.1),
            strengthPercentage = (strengthPct * 1.1).coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "Venus or 7th lord Dasha in early life",
            cancellationFactors = listOf("Saturn's strong influence delays")
        )
    }

    /**
     * Sukha Vivaha Yoga - Happy Marriage
     */
    private fun evaluateSukhaVivahaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord7 = houseLords[7] ?: return null
        val lord4 = houseLords[4] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null
        val pos4 = chart.planetPositions.find { it.planet == lord4 } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }

        val conditions = mutableListOf<String>()
        val involvedPlanets = mutableListOf<PlanetPosition>(pos7, venusPos)

        // 7th lord in Kendra/Trikona
        val goodHouses = KENDRA_HOUSES + TRIKONA_HOUSES
        if (pos7.house in goodHouses) {
            conditions.add("7th lord well-placed")
        }

        // Venus not afflicted
        val venusAfflicted = chart.planetPositions.any { pos ->
            pos.planet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU) &&
            YogaHelpers.areConjunct(venusPos, pos)
        }
        if (!venusAfflicted && !YogaHelpers.isDebilitated(venusPos)) {
            conditions.add("Venus unafflicted")
        }

        // Jupiter aspects 7th house or lord
        if (jupiterPos != null) {
            if (YogaHelpers.isAspecting(jupiterPos, pos7) || jupiterPos.house == 7) {
                conditions.add("Jupiter's blessing on marriage")
                involvedPlanets.add(jupiterPos)
            }
        }

        // 4th lord strong (domestic happiness)
        if (pos4.house in goodHouses || YogaHelpers.isExalted(pos4) || YogaHelpers.isInOwnSign(pos4)) {
            conditions.add("Strong domestic happiness indicator")
            involvedPlanets.add(pos4)
        }

        // 7th house has benefic(s)
        val beneficsIn7th = chart.planetPositions.filter {
            it.house == 7 && it.planet in MARRIAGE_BENEFICS
        }
        if (beneficsIn7th.isNotEmpty()) {
            conditions.add("Benefics in 7th house")
        }

        if (conditions.size < 3) return null

        val strengthPct = YogaHelpers.calculateYogaStrength(chart, involvedPlanets)

        return Yoga(
            name = "Sukha Vivaha Yoga",
            sanskritName = "सुख विवाह योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = involvedPlanets.map { it.planet },
            houses = listOf(4, 7),
            description = "Happy marriage indicators: ${conditions.joinToString(", ")}",
            effects = "Harmonious married life, understanding spouse, " +
                    "domestic happiness, mutual respect and love, " +
                    "supportive partnership, lasting marital bond, " +
                    "prosperity through marriage",
            strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.1),
            strengthPercentage = (strengthPct * 1.1).coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "7th lord, Venus, or Jupiter Dasha",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Vivaha Vighn Yoga - Marriage Obstacles
     */
    private fun evaluateVivahaVighnYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val lord7 = houseLords[7] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null

        // Type 1: Rahu in 7th house
        val rahuPos = chart.planetPositions.find { it.planet == Planet.RAHU }
        if (rahuPos != null && rahuPos.house == 7) {
            yogas.add(Yoga(
                name = "Rahu Saptama Dosha",
                sanskritName = "राहु सप्तम दोष",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.RAHU),
                houses = listOf(7),
                description = "Rahu in 7th house",
                effects = "Unconventional relationships, inter-caste/inter-cultural marriage, " +
                        "misunderstandings with spouse, unusual circumstances in marriage, " +
                        "spouse may have different background, need for clear communication",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 55.0,
                isAuspicious = false,
                activationPeriod = "Rahu Dasha, especially Rahu-Rahu",
                cancellationFactors = listOf(
                    "Jupiter's aspect mitigates",
                    "Strong Venus helps",
                    "7th lord dignified compensates"
                )
            ))
        }

        // Type 2: Ketu in 7th house
        val ketuPos = chart.planetPositions.find { it.planet == Planet.KETU }
        if (ketuPos != null && ketuPos.house == 7) {
            yogas.add(Yoga(
                name = "Ketu Saptama Yoga",
                sanskritName = "केतु सप्तम योग",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(Planet.KETU),
                houses = listOf(7),
                description = "Ketu in 7th house",
                effects = "Detachment in relationships, spiritual partner, " +
                        "unconventional marriage views, past-life karmic relationships, " +
                        "need for spiritual connection with spouse, may delay marriage",
                strength = YogaStrength.MODERATE,
                strengthPercentage = 50.0,
                isAuspicious = false,
                activationPeriod = "Ketu Dasha",
                cancellationFactors = listOf(
                    "Venus strong maintains relationships",
                    "Jupiter's aspect provides stability"
                )
            ))
        }

        // Type 3: 7th lord debilitated
        if (YogaHelpers.isDebilitated(pos7)) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(pos7))
            
            yogas.add(Yoga(
                name = "Neecha Saptamesh Dosha",
                sanskritName = "नीच सप्तमेश दोष",
                category = YogaCategory.NEGATIVE_YOGA,
                planets = listOf(lord7),
                houses = listOf(7, pos7.house),
                description = "7th lord ${lord7.displayName} debilitated in ${pos7.sign.displayName}",
                effects = "Challenges in finding suitable partner, spouse may face difficulties, " +
                        "need for patience in marriage, partnership requires extra effort, " +
                        "potential for growth through relationship challenges",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = false,
                activationPeriod = "7th lord Dasha",
                cancellationFactors = listOf(
                    "Neecha Bhanga if conditions met",
                    "Benefic aspects help",
                    "Strong Venus compensates"
                )
            ))
        }

        // Type 4: 7th lord with Sun (combust)
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
        if (sunPos != null && lord7 != Planet.SUN && YogaHelpers.areConjunct(pos7, sunPos)) {
            val combustFactor = YogaHelpers.getCombustionFactor(pos7, chart)
            if (combustFactor < 0.7) {
                yogas.add(Yoga(
                    name = "Saptamesh Asta Yoga",
                    sanskritName = "सप्तमेश अस्त योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(lord7, Planet.SUN),
                    houses = listOf(7, pos7.house),
                    description = "7th lord ${lord7.displayName} combust by Sun",
                    effects = "Ego issues affect marriage, spouse may be overshadowed, " +
                            "need for balance of power in relationship, " +
                            "father may influence marriage decisions",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 50.0,
                    isAuspicious = false,
                    activationPeriod = "7th lord or Sun Dasha",
                    cancellationFactors = listOf(
                        "Jupiter's aspect helps",
                        "Venus strong mitigates"
                    )
                ))
            }
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Sama Saptama Yoga - Balanced Partnership
     * When 7th house and lord are well balanced
     */
    private fun evaluateSamaSaptamaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord7 = houseLords[7] ?: return null
        val lord1 = houseLords[1] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null
        val pos1 = chart.planetPositions.find { it.planet == lord1 } ?: return null

        // Check for balance: both Lagna lord and 7th lord similarly strong
        val strength1 = YogaHelpers.calculateYogaStrength(chart, listOf(pos1))
        val strength7 = YogaHelpers.calculateYogaStrength(chart, listOf(pos7))

        // Balance check: strengths within 20% of each other
        val isBalanced = kotlin.math.abs(strength1 - strength7) <= 20

        if (!isBalanced) return null

        // Both should be reasonably strong (above 50%)
        if (strength1 < 50 || strength7 < 50) return null

        // Should not be in 6-8 or 2-12 relationship
        val houseRelation = YogaHelpers.getHouseFrom(pos7.sign, pos1.sign)
        val badRelations = listOf(2, 6, 8, 12)
        if (houseRelation in badRelations) return null

        val avgStrength = (strength1 + strength7) / 2

        return Yoga(
            name = "Sama Saptama Yoga",
            sanskritName = "सम सप्तम योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(lord1, lord7),
            houses = listOf(1, 7),
            description = "Lagna lord and 7th lord equally strong and well-related",
            effects = "Equal partnership, mutual respect in marriage, " +
                    "balanced give and take, spouse of equal status, " +
                    "harmonious power dynamics, successful joint ventures, " +
                    "partnership brings out the best in both",
            strength = YogaHelpers.strengthFromPercentage(avgStrength),
            strengthPercentage = avgStrength,
            isAuspicious = true,
            activationPeriod = "Dasha of Lagna lord or 7th lord",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Kalatrahani Yoga - Loss of Spouse/Separation
     */
    private fun evaluateKalatrahaniYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val lord7 = houseLords[7] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null

        // Type 1: 7th lord in 8th house with malefic
        if (pos7.house == 8) {
            val maleficsIn8th = chart.planetPositions.filter {
                it.house == 8 && it.planet in MARRIAGE_MALEFICS
            }
            
            if (maleficsIn8th.isNotEmpty()) {
                yogas.add(Yoga(
                    name = "Kalatra Nashaka Yoga",
                    sanskritName = "कलत्र नाशक योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(lord7) + maleficsIn8th.map { it.planet },
                    houses = listOf(7, 8),
                    description = "7th lord in 8th with malefic(s)",
                    effects = "Challenges to spouse's well-being, separation possible, " +
                            "sudden changes in marriage, need for spouse's health care, " +
                            "transformation through partnership crisis",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 55.0,
                    isAuspicious = false,
                    activationPeriod = "7th lord Dasha, 8th house periods",
                    cancellationFactors = listOf(
                        "Jupiter's aspect protects",
                        "Strong Venus safeguards",
                        "7th lord in own sign reduces danger"
                    )
                ))
            }
        }

        // Type 2: Venus conjunct Saturn and Mars
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN }
        val marsPos = chart.planetPositions.find { it.planet == Planet.MARS }
        
        if (saturnPos != null && marsPos != null) {
            val venusWithSaturn = YogaHelpers.areConjunct(venusPos, saturnPos)
            val venusWithMars = YogaHelpers.areConjunct(venusPos, marsPos)
            
            if (venusWithSaturn && venusWithMars) {
                yogas.add(Yoga(
                    name = "Shukra Peedita Yoga",
                    sanskritName = "शुक्र पीड़ित योग",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.VENUS, Planet.SATURN, Planet.MARS),
                    houses = listOf(venusPos.house),
                    description = "Venus hemmed between Saturn and Mars",
                    effects = "Love life challenges, delays and conflicts in relationships, " +
                            "need for patience and effort in marriage, " +
                            "potential for separation if not handled wisely",
                    strength = YogaStrength.STRONG,
                    strengthPercentage = 65.0,
                    isAuspicious = false,
                    activationPeriod = "Venus, Saturn, or Mars Dasha",
                    cancellationFactors = listOf(
                        "Jupiter's aspect on Venus protects",
                        "Strong 7th lord helps"
                    )
                ))
            }
        }

        // Type 3: 7th and 8th lords together
        val lord8 = houseLords[8]
        val pos8 = lord8?.let { chart.planetPositions.find { it.planet == lord8 } }
        
        if (lord8 != null && lord7 != lord8 && pos8 != null) {
            if (YogaHelpers.areConjunct(pos7, pos8)) {
                yogas.add(Yoga(
                    name = "Saptama-Ashtama Sambandha",
                    sanskritName = "सप्तम-अष्टम संबंध",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(lord7, lord8),
                    houses = listOf(7, 8),
                    description = "7th lord conjunct 8th lord",
                    effects = "Spouse may face health challenges, sudden events in marriage, " +
                            "need for insurance and security, inheritance through marriage, " +
                            "transformative but potentially difficult partnership",
                    strength = YogaStrength.MODERATE,
                    strengthPercentage = 55.0,
                    isAuspicious = false,
                    activationPeriod = "7th or 8th lord Dasha",
                    cancellationFactors = listOf(
                        "Benefic aspects mitigate",
                        "Dignified planets reduce severity"
                    )
                ))
            }
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Vidhava Yoga - Widowhood indicators (to be used cautiously)
     */
    private fun evaluateVidhavaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord7 = houseLords[7] ?: return null
        val lord8 = houseLords[8] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null
        val pos8 = chart.planetPositions.find { it.planet == lord8 } ?: return null

        // Very specific and rare combination
        // 7th lord in 8th, 8th lord in 7th (exchange)
        val isExchange = pos7.house == 8 && pos8.house == 7

        if (!isExchange) return null

        // Must also have malefic aspects to be concerning
        val hasMaleficAspects = chart.planetPositions.any { pos ->
            pos.planet in listOf(Planet.SATURN, Planet.MARS) &&
            (YogaHelpers.isAspecting(pos, pos7) || YogaHelpers.isAspecting(pos, pos8))
        }

        if (!hasMaleficAspects) return null

        return Yoga(
            name = "Saptama-Ashtama Parivartana",
            sanskritName = "सप्तम-अष्टम परिवर्तन",
            category = YogaCategory.NEGATIVE_YOGA,
            planets = listOf(lord7, lord8),
            houses = listOf(7, 8),
            description = "7th and 8th lords in mutual exchange with malefic aspects",
            effects = "Serious concern for spouse's longevity, need for protective measures, " +
                    "spouse may have chronic health issues, transformative marriage experiences, " +
                    "spiritual growth through partnership challenges. Remedies strongly advised.",
            strength = YogaStrength.STRONG,
            strengthPercentage = 65.0,
            isAuspicious = false,
            activationPeriod = "7th or 8th lord Dasha - requires caution",
            cancellationFactors = listOf(
                "Jupiter's strong aspect on 7th house protects",
                "Venus exalted or in own sign provides relief",
                "Benefics in 7th house safeguard"
            )
        )
    }

    /**
     * Venus-based Marriage Yogas
     */
    private fun evaluateVenusMarriageYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null

        // Type 1: Venus exalted (Pisces)
        if (YogaHelpers.isExalted(venusPos)) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(venusPos))
            
            yogas.add(Yoga(
                name = "Uccha Shukra Vivaha Yoga",
                sanskritName = "उच्च शुक्र विवाह योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.VENUS),
                houses = listOf(venusPos.house),
                description = "Venus exalted in ${venusPos.sign.displayName}",
                effects = "Beautiful and cultured spouse, artistic partner, " +
                        "love marriage success, refined tastes, luxurious married life, " +
                        "harmonious relationship, spouse from good family",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.2),
                strengthPercentage = (strengthPct * 1.2).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Venus Dasha brings excellent marriage",
                cancellationFactors = emptyList()
            ))
        }

        // Type 2: Venus in own sign (Taurus, Libra)
        if (YogaHelpers.isInOwnSign(venusPos) && !YogaHelpers.isExalted(venusPos)) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(venusPos))
            
            yogas.add(Yoga(
                name = "Swakshetra Shukra Vivaha Yoga",
                sanskritName = "स्वक्षेत्र शुक्र विवाह योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.VENUS),
                houses = listOf(venusPos.house),
                description = "Venus in own sign ${venusPos.sign.displayName}",
                effects = "Comfortable married life, attractive spouse, " +
                        "material pleasures through marriage, stable relationship, " +
                        "spouse enjoys arts and luxuries",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.1),
                strengthPercentage = (strengthPct * 1.1).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Venus Dasha brings marriage and comforts",
                cancellationFactors = emptyList()
            ))
        }

        // Type 3: Venus in 7th house
        if (venusPos.house == 7) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(venusPos))
            
            // Check for afflictions
            val isAfflicted = chart.planetPositions.any { pos ->
                pos.planet in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU) &&
                YogaHelpers.areConjunct(venusPos, pos)
            }
            
            if (!isAfflicted) {
                yogas.add(Yoga(
                    name = "Shukra Saptama Yoga",
                    sanskritName = "शुक्र सप्तम योग",
                    category = YogaCategory.SPECIAL_YOGA,
                    planets = listOf(Planet.VENUS),
                    houses = listOf(7),
                    description = "Venus in 7th house",
                    effects = "Charming and attractive spouse, love in marriage, " +
                            "romantic relationship, spouse may be artistic, " +
                            "beautiful partnership, harmony in married life",
                    strength = YogaHelpers.strengthFromPercentage(strengthPct),
                    strengthPercentage = strengthPct,
                    isAuspicious = true,
                    activationPeriod = "Venus Dasha brings marriage",
                    cancellationFactors = emptyList()
                ))
            }
        }

        // Type 4: Venus-Jupiter conjunction
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        if (jupiterPos != null && YogaHelpers.areConjunct(venusPos, jupiterPos)) {
            val positions = listOf(venusPos, jupiterPos)
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)
            
            yogas.add(Yoga(
                name = "Guru-Shukra Vivaha Yoga",
                sanskritName = "गुरु-शुक्र विवाह योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(Planet.VENUS, Planet.JUPITER),
                houses = listOf(venusPos.house),
                description = "Venus conjunct Jupiter in ${ordinal(venusPos.house)} house",
                effects = "Wise and beautiful spouse, dharmic marriage, " +
                        "spouse brings wisdom and wealth, blessed married life, " +
                        "children through marriage, religious ceremonies honored",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.15),
                strengthPercentage = (strengthPct * 1.15).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "Venus or Jupiter Dasha",
                cancellationFactors = emptyList()
            ))
        }

        // Type 5: Venus debilitated (Virgo) - challenging
        if (YogaHelpers.isDebilitated(venusPos)) {
            val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(venusPos))
            
            // Check for Neecha Bhanga
            val hasNeechaBhanga = YogaHelpers.hasNeechaBhanga(venusPos, chart)
            
            if (!hasNeechaBhanga) {
                yogas.add(Yoga(
                    name = "Neecha Shukra Dosha",
                    sanskritName = "नीच शुक्र दोष",
                    category = YogaCategory.NEGATIVE_YOGA,
                    planets = listOf(Planet.VENUS),
                    houses = listOf(venusPos.house),
                    description = "Venus debilitated in Virgo",
                    effects = "Critical nature affects relationships, high expectations from spouse, " +
                            "need for realistic approach to love, may delay marriage, " +
                            "learning to appreciate imperfections",
                    strength = YogaHelpers.strengthFromPercentage(strengthPct),
                    strengthPercentage = strengthPct,
                    isAuspicious = false,
                    activationPeriod = "Venus Dasha may bring relationship tests",
                    cancellationFactors = listOf(
                        "Neecha Bhanga if Mercury strong in Kendra",
                        "Jupiter's aspect helps",
                        "7th lord strong compensates"
                    )
                ))
            }
        }

        return yogas.ifEmpty { null }
    }

    /**
     * 7th Lord Position Yogas
     */
    private fun evaluateSeventhLordYogas(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Yoga>? {
        val yogas = mutableListOf<Yoga>()
        val lord7 = houseLords[7] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null

        val strengthPct = YogaHelpers.calculateYogaStrength(chart, listOf(pos7))

        // 7th lord in different houses with marriage significations
        when (pos7.house) {
            1 -> yogas.add(Yoga(
                name = "Saptamesh Lagna Yoga",
                sanskritName = "सप्तमेश लग्न योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord7),
                houses = listOf(1, 7),
                description = "7th lord ${lord7.displayName} in Lagna",
                effects = "Spouse devoted to native, marriage important to identity, " +
                        "partner similar to self, spouse may be in same field, " +
                        "marriage brings self-improvement",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.1),
                strengthPercentage = (strengthPct * 1.1).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "7th lord Dasha",
                cancellationFactors = emptyList()
            ))
            
            4 -> yogas.add(Yoga(
                name = "Saptamesh Chaturthi Yoga",
                sanskritName = "सप्तमेश चतुर्थी योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord7),
                houses = listOf(4, 7),
                description = "7th lord ${lord7.displayName} in 4th house",
                effects = "Happy home after marriage, spouse attached to home, " +
                        "property through marriage, mother approves spouse, " +
                        "emotional fulfillment in marriage",
                strength = YogaHelpers.strengthFromPercentage(strengthPct),
                strengthPercentage = strengthPct,
                isAuspicious = true,
                activationPeriod = "7th lord Dasha",
                cancellationFactors = emptyList()
            ))
            
            5 -> yogas.add(Yoga(
                name = "Saptamesh Panchama Yoga",
                sanskritName = "सप्तमेश पंचम योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord7),
                houses = listOf(5, 7),
                description = "7th lord ${lord7.displayName} in 5th house",
                effects = "Love marriage likely, romantic relationship, " +
                        "children bring happiness to marriage, creative spouse, " +
                        "past-life connection with partner",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.1),
                strengthPercentage = (strengthPct * 1.1).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "7th lord Dasha",
                cancellationFactors = emptyList()
            ))
            
            9 -> yogas.add(Yoga(
                name = "Saptamesh Navama Yoga",
                sanskritName = "सप्तमेश नवम योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord7),
                houses = listOf(7, 9),
                description = "7th lord ${lord7.displayName} in 9th house",
                effects = "Fortunate marriage, religious spouse, " +
                        "spouse from different culture or distant place, " +
                        "father-in-law supportive, dharmic partnership",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.15),
                strengthPercentage = (strengthPct * 1.15).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "7th lord Dasha",
                cancellationFactors = emptyList()
            ))
            
            10 -> yogas.add(Yoga(
                name = "Saptamesh Dashama Yoga",
                sanskritName = "सप्तमेश दशम योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord7),
                houses = listOf(7, 10),
                description = "7th lord ${lord7.displayName} in 10th house",
                effects = "Marriage benefits career, spouse helps profession, " +
                        "public recognition through partnership, working couple, " +
                        "spouse may be colleague or business partner",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.1),
                strengthPercentage = (strengthPct * 1.1).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "7th lord Dasha",
                cancellationFactors = emptyList()
            ))
            
            11 -> yogas.add(Yoga(
                name = "Saptamesh Labha Yoga",
                sanskritName = "सप्तमेश लाभ योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord7),
                houses = listOf(7, 11),
                description = "7th lord ${lord7.displayName} in 11th house",
                effects = "Gains through marriage, wealthy spouse, " +
                        "desires fulfilled after marriage, spouse's network helps, " +
                        "elder sibling-in-law beneficial",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.1),
                strengthPercentage = (strengthPct * 1.1).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "7th lord Dasha",
                cancellationFactors = emptyList()
            ))
            
            12 -> yogas.add(Yoga(
                name = "Saptamesh Vyaya Yoga",
                sanskritName = "सप्तमेश व्यय योग",
                category = YogaCategory.SPECIAL_YOGA,
                planets = listOf(lord7),
                houses = listOf(7, 12),
                description = "7th lord ${lord7.displayName} in 12th house",
                effects = "Foreign spouse or marriage in foreign land, " +
                        "bedroom pleasures, expenses after marriage, " +
                        "spiritual partnership, spouse may live abroad",
                strength = YogaHelpers.strengthFromPercentage(strengthPct * 0.9),
                strengthPercentage = (strengthPct * 0.9).coerceIn(10.0, 100.0),
                isAuspicious = true,
                activationPeriod = "7th lord Dasha",
                cancellationFactors = listOf("May indicate separation if afflicted")
            ))
        }

        return yogas.ifEmpty { null }
    }

    /**
     * Stri Dirgha Yoga - Long-lasting wife (for male charts)
     */
    private fun evaluateStriDirghaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord7 = houseLords[7] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }

        val goodConditions = mutableListOf<String>()

        // Venus strong
        if (YogaHelpers.isExalted(venusPos) || YogaHelpers.isInOwnSign(venusPos)) {
            goodConditions.add("Venus dignified")
        }

        // 7th lord strong
        if (YogaHelpers.isExalted(pos7) || YogaHelpers.isInOwnSign(pos7)) {
            goodConditions.add("7th lord dignified")
        }

        // Jupiter aspects 7th house or 7th lord
        if (jupiterPos != null && (YogaHelpers.isAspecting(jupiterPos, pos7) || jupiterPos.house == 7)) {
            goodConditions.add("Jupiter's protection")
        }

        // 8th house from 7th (2nd house) is not afflicted - indicates spouse's longevity
        val planetsIn2nd = chart.planetPositions.filter { it.house == 2 }
        val noMaleficsIn2nd = planetsIn2nd.none { it.planet in MARRIAGE_MALEFICS }
        if (noMaleficsIn2nd) {
            goodConditions.add("2nd house (spouse's longevity) protected")
        }

        if (goodConditions.size < 3) return null

        val positions = listOf(pos7, venusPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        return Yoga(
            name = "Stri Dirgha Yoga",
            sanskritName = "स्त्री दीर्घ योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(lord7, Planet.VENUS),
            houses = listOf(2, 7),
            description = "Long-lasting marriage indicators: ${goodConditions.joinToString(", ")}",
            effects = "Long-lived spouse, enduring marriage, " +
                    "spouse's health protected, lasting partnership, " +
                    "mutual care in old age",
            strength = YogaHelpers.strengthFromPercentage(strengthPct),
            strengthPercentage = strengthPct,
            isAuspicious = true,
            activationPeriod = "7th lord and Venus Dasha periods",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Bharya Saubhagya Yoga - Wife's Fortune
     */
    private fun evaluateBharyaSaubhagyaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord7 = houseLords[7] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null

        // 7th lord in Trikona with Venus well-placed
        val lord7InTrikona = pos7.house in TRIKONA_HOUSES
        val venusNotAfflicted = !YogaHelpers.isDebilitated(venusPos) && 
                YogaHelpers.getCombustionFactor(venusPos, chart) > 0.6

        if (!lord7InTrikona || !venusNotAfflicted) return null

        // Additional check: benefics aspect 7th house
        val jupiterPos = chart.planetPositions.find { it.planet == Planet.JUPITER }
        val beneficAspectOn7th = jupiterPos != null && jupiterPos.house in listOf(1, 3, 5, 9, 11)

        if (!beneficAspectOn7th) return null

        val positions = listOf(pos7, venusPos)
        val strengthPct = YogaHelpers.calculateYogaStrength(chart, positions)

        return Yoga(
            name = "Bharya Saubhagya Yoga",
            sanskritName = "भार्या सौभाग्य योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(lord7, Planet.VENUS, Planet.JUPITER),
            houses = listOf(7, pos7.house),
            description = "7th lord in Trikona with unafflicted Venus and Jupiter's blessing",
            effects = "Fortunate spouse, wife brings luck, " +
                    "prosperity after marriage, spouse from wealthy family, " +
                    "domestic happiness, blessed married life",
            strength = YogaHelpers.strengthFromPercentage(strengthPct * 1.15),
            strengthPercentage = (strengthPct * 1.15).coerceIn(10.0, 100.0),
            isAuspicious = true,
            activationPeriod = "7th lord or Venus Dasha",
            cancellationFactors = emptyList()
        )
    }

    /**
     * Jara Vivaha Yoga - Marriage in Old Age
     */
    private fun evaluateJaraVivahaYoga(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val lord7 = houseLords[7] ?: return null
        val pos7 = chart.planetPositions.find { it.planet == lord7 } ?: return null
        val saturnPos = chart.planetPositions.find { it.planet == Planet.SATURN } ?: return null
        val venusPos = chart.planetPositions.find { it.planet == Planet.VENUS } ?: return null

        var delayFactors = 0
        val reasons = mutableListOf<String>()

        // Saturn aspects Venus
        if (YogaHelpers.isAspecting(saturnPos, venusPos)) {
            delayFactors++
            reasons.add("Saturn aspects Venus")
        }

        // Saturn aspects 7th lord
        if (YogaHelpers.isAspecting(saturnPos, pos7)) {
            delayFactors++
            reasons.add("Saturn aspects 7th lord")
        }

        // Saturn in 7th
        if (saturnPos.house == 7) {
            delayFactors++
            reasons.add("Saturn in 7th house")
        }

        // 7th lord in Saturn's sign (Capricorn, Aquarius)
        if (pos7.sign in listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS)) {
            delayFactors++
            reasons.add("7th lord in Saturn's sign")
        }

        // Venus in Saturn's sign
        if (venusPos.sign in listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS)) {
            delayFactors++
            reasons.add("Venus in Saturn's sign")
        }

        if (delayFactors < 3) return null

        return Yoga(
            name = "Jara Vivaha Yoga",
            sanskritName = "जरा विवाह योग",
            category = YogaCategory.SPECIAL_YOGA,
            planets = listOf(Planet.SATURN, lord7, Planet.VENUS),
            houses = listOf(7, saturnPos.house),
            description = "Strong Saturn influence on marriage: ${reasons.joinToString(", ")}",
            effects = "Marriage significantly delayed (after 35-40), " +
                    "marriage to older or mature person, " +
                    "career/responsibilities delay marriage, " +
                    "stable marriage when it happens, patient approach needed",
            strength = YogaStrength.MODERATE,
            strengthPercentage = 50.0 + delayFactors * 5,
            isAuspicious = true, // Not negative, just delayed
            activationPeriod = "After Saturn maturity, Saturn Dasha in later life",
            cancellationFactors = listOf(
                "Jupiter's strong aspect can bring earlier opportunity",
                "Venus Dasha may provide marriage chance"
            )
        )
    }

    /**
     * Convert number to ordinal string
     */
    private fun ordinal(n: Int): String = when (n) {
        1 -> "1st"
        2 -> "2nd"
        3 -> "3rd"
        else -> "${n}th"
    }
}
