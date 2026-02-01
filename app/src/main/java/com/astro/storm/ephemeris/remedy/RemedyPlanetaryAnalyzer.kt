package com.astro.storm.ephemeris.remedy

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyRemedy
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.*
import com.astro.storm.ephemeris.VedicAstrologyUtils
import com.astro.storm.ephemeris.remedy.RemedyConstants.combustionDegrees
import com.astro.storm.ephemeris.remedy.RemedyConstants.exaltationDebilitationData
import com.astro.storm.ephemeris.remedy.RemedyConstants.mrityuBhagaDegrees
import kotlin.math.abs

object RemedyPlanetaryAnalyzer {

    fun analyzePlanet(
        planet: Planet,
        chart: VedicChart,
        ascendantSign: ZodiacSign,
        language: Language
    ): PlanetaryAnalysis {
        val position = chart.planetPositions.find { it.planet == planet }
            ?: return createDefaultAnalysis(planet)

        val issues = mutableListOf<String>()
        val positives = mutableListOf<String>()
        var strengthScore = 50.0

        val sign = position.sign
        val house = position.house
        val longitude = position.longitude
        val signLongitude = longitude % 30.0

        val exDebInfo = exaltationDebilitationData[planet]
        val isDebilitated = exDebInfo?.debilitationSign == sign
        val isExalted = exDebInfo?.exaltationSign == sign

        val deepExaltation = if (isExalted && exDebInfo != null) {
            abs(signLongitude - exDebInfo.exaltationDegree) <= 1.0
        } else false

        val deepDebilitation = if (isDebilitated && exDebInfo != null) {
            abs(signLongitude - exDebInfo.debilitationDegree) <= 1.0
        } else false

        if (isDebilitated) {
            if (deepDebilitation) {
                issues.add(StringResources.get(StringKeyAnalysis.ANALYSIS_DEBILITATED_DEEP, language, signLongitude, sign.getLocalizedName(language)))
                strengthScore -= 35
            } else {
                issues.add(StringResources.get(StringKeyAnalysis.ANALYSIS_DEBILITATED, language, sign.getLocalizedName(language)))
                strengthScore -= 25
            }
        }

        if (isExalted) {
            if (deepExaltation) {
                positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_EXALTED_DEEP, language, signLongitude, sign.getLocalizedName(language)))
                strengthScore += 35
            } else {
                positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_EXALTED, language, sign.getLocalizedName(language)))
                strengthScore += 25
            }
        }

        val hasNeechaBhanga = if (isDebilitated) checkNeechaBhangaRajaYoga(planet, chart, ascendantSign) else false
        if (hasNeechaBhanga) {
            positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_NEECHA_BHANGA, language))
            strengthScore += 20
        }

        val isOwnSign = isInOwnSign(planet, sign)
        if (isOwnSign) {
            positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_OWN_SIGN, language, sign.getLocalizedName(language)))
            strengthScore += 15
        }

        val isMooltrikona = isInMooltrikona(planet, sign, signLongitude)
        if (isMooltrikona && !isOwnSign) {
            positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_MOOLTRIKONA, language))
            strengthScore += 12
        }

        val relationship = VedicAstrologyUtils.getNaturalRelationship(planet, sign.ruler)
        val isFriendlySign = relationship in listOf(VedicAstrologyUtils.PlanetaryRelationship.FRIEND, VedicAstrologyUtils.PlanetaryRelationship.BEST_FRIEND)
        val isEnemySign = relationship in listOf(VedicAstrologyUtils.PlanetaryRelationship.ENEMY, VedicAstrologyUtils.PlanetaryRelationship.BITTER_ENEMY)
        val isNeutralSign = relationship == VedicAstrologyUtils.PlanetaryRelationship.NEUTRAL

        if (isFriendlySign && !isOwnSign && !isExalted) {
            positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_FRIEND_SIGN, language, sign.ruler.displayName))
            strengthScore += 8
        }

        if (isEnemySign && !isDebilitated) {
            issues.add(StringResources.get(StringKeyAnalysis.ANALYSIS_ENEMY_SIGN, language, sign.ruler.displayName))
            strengthScore -= 10
        }

        when (house) {
            1, 4, 7, 10 -> {
                positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_KENDRA, language, house))
                strengthScore += 10
            }
            5, 9 -> {
                positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_TRIKONA, language, house))
                strengthScore += 10
            }
            6, 8, 12 -> {
                issues.add(StringResources.get(StringKeyAnalysis.ANALYSIS_DUSTHANA, language, house))
                strengthScore -= 15
            }
            2, 11 -> {
                positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_WEALTH, language, house))
                strengthScore += 5
            }
        }

        val isRetrograde = position.isRetrograde
        if (isRetrograde && planet != Planet.SUN && planet != Planet.MOON) {
            if (planet in listOf(Planet.SATURN, Planet.JUPITER)) {
                positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_RETRO_STRONG, language))
                strengthScore += 5
            } else if (planet == Planet.MERCURY) {
                issues.add(StringResources.get(StringKeyAnalysis.ANALYSIS_RETRO_REVIEW, language))
            } else if (planet in listOf(Planet.MARS, Planet.VENUS)) {
                issues.add(StringResources.get(StringKeyAnalysis.ANALYSIS_RETRO_INTERNAL, language))
                strengthScore -= 5
            }
        }

        val isCombust = checkCombustion(planet, chart, isRetrograde)
        if (isCombust) {
            val combustStrength = if (planet == Planet.MOON) "severely" else "moderately"
            issues.add(StringResources.get(StringKeyAnalysis.ANALYSIS_COMBUST, language, combustStrength))
            strengthScore -= if (planet == Planet.MOON) 25 else 20
        }

        val conjunctMalefics = checkMaleficConjunction(planet, chart)
        if (conjunctMalefics.isNotEmpty()) {
            val names = conjunctMalefics.joinToString { it.getLocalizedName(language) }
            issues.add(StringResources.get(StringKeyAnalysis.ANALYSIS_CONJUNCT_MALEFICS, language, names))
            strengthScore -= conjunctMalefics.size * 7
        }

        val conjunctBenefics = checkBeneficConjunction(planet, chart)
        if (conjunctBenefics.isNotEmpty()) {
            val names = conjunctBenefics.joinToString { it.getLocalizedName(language) }
            positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_CONJUNCT_BENEFICS, language, names))
            strengthScore += conjunctBenefics.size * 5
        }

        val aspectingPlanets = getAspectingPlanets(planet, chart)
        val aspectedByBenefics = aspectingPlanets.any { it in listOf(Planet.JUPITER, Planet.VENUS) }
        val aspectedByMalefics = aspectingPlanets.any { it in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU) }

        if (aspectedByBenefics && Planet.JUPITER in aspectingPlanets) {
            positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_ASPECT_JUPITER, language))
            strengthScore += 8
        }
        if (aspectedByMalefics && Planet.SATURN in aspectingPlanets) {
            issues.add(StringResources.get(StringKeyAnalysis.ANALYSIS_ASPECT_SATURN, language))
            strengthScore -= 5
        }

        val isInGandanta = checkGandanta(sign, signLongitude)
        if (isInGandanta) {
            issues.add(StringResources.get(StringKeyAnalysis.ANALYSIS_GANDANTA, language))
            strengthScore -= 12
        }

        val isInMrityuBhaga = checkMrityuBhaga(planet, sign, signLongitude)
        if (isInMrityuBhaga) {
            issues.add(StringResources.get(StringKeyAnalysis.ANALYSIS_MRITYU_BHAGA, language))
            strengthScore -= 8
        }

        val isInPushkara = checkPushkaraNavamsha(signLongitude, sign)
        if (isInPushkara) {
            positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_PUSHKARA, language))
            strengthScore += 5
        }

        val isFunctionalBenefic = isFunctionalBeneficForLagna(planet, ascendantSign)
        val isFunctionalMalefic = isFunctionalMaleficForLagna(planet, ascendantSign)
        val isYogakaraka = isYogakarakaPlanet(planet, ascendantSign)

        if (isYogakaraka) {
            positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_YOGAKARAKA, language, ascendantSign.getLocalizedName(language)))
            strengthScore += 10
        }

        if (planet == Planet.MOON) {
            val moonStrength = checkMoonPaksha(chart)
            if (moonStrength < 0) {
                issues.add(StringResources.get(StringKeyAnalysis.ANALYSIS_MOON_DARK, language))
                strengthScore += moonStrength
            } else if (moonStrength > 0) {
                positives.add(StringResources.get(StringKeyAnalysis.ANALYSIS_MOON_BRIGHT, language))
                strengthScore += moonStrength
            }
        }

        strengthScore = strengthScore.coerceIn(0.0, 100.0)

        val strength = when {
            strengthScore >= 80 -> PlanetaryStrength.VERY_STRONG
            strengthScore >= 65 -> PlanetaryStrength.STRONG
            strengthScore >= 45 -> PlanetaryStrength.MODERATE
            strengthScore >= 30 -> PlanetaryStrength.WEAK
            strengthScore >= 15 -> PlanetaryStrength.VERY_WEAK
            else -> PlanetaryStrength.AFFLICTED
        }

        val needsRemedy = strength.severity >= 3 || issues.size >= 2 || isDebilitated || isCombust || isInGandanta
        val (nakshatra, pada) = Nakshatra.fromLongitude(longitude)
        val dignityDescription = buildDignityDescription(
            planet, sign, isExalted, isDebilitated, isOwnSign, isMooltrikona,
            isFriendlySign, isEnemySign, isNeutralSign, isRetrograde, isCombust, language
        )

        return PlanetaryAnalysis(
            planet = planet,
            strength = strength,
            strengthScore = strengthScore.toInt(),
            issues = issues,
            positives = positives,
            needsRemedy = needsRemedy,
            housePosition = house,
            sign = sign,
            nakshatra = nakshatra,
            nakshatraPada = pada,
            longitude = longitude,
            isRetrograde = isRetrograde,
            isCombust = isCombust,
            isDebilitated = isDebilitated,
            isExalted = isExalted,
            isOwnSign = isOwnSign,
            isMooltrikona = isMooltrikona,
            isFriendlySign = isFriendlySign,
            isEnemySign = isEnemySign,
            isNeutralSign = isNeutralSign,
            hasNeechaBhangaRajaYoga = hasNeechaBhanga,
            isInGandanta = isInGandanta,
            isInMrityuBhaga = isInMrityuBhaga,
            isInPushkarNavamsha = isInPushkara,
            isFunctionalBenefic = isFunctionalBenefic,
            isFunctionalMalefic = isFunctionalMalefic,
            isYogakaraka = isYogakaraka,
            aspectingPlanets = aspectingPlanets,
            aspectedByBenefics = aspectedByBenefics,
            aspectedByMalefics = aspectedByMalefics,
            shadbalaStrength = strengthScore / 100.0,
            dignityDescription = dignityDescription
        )
    }

    private fun createDefaultAnalysis(planet: Planet): PlanetaryAnalysis {
        val (nakshatra, pada) = Nakshatra.fromLongitude(0.0)
        return PlanetaryAnalysis(
            planet = planet, strength = PlanetaryStrength.MODERATE, strengthScore = 50, issues = emptyList(), positives = emptyList(),
            needsRemedy = false, housePosition = 1, sign = ZodiacSign.ARIES, nakshatra = nakshatra, nakshatraPada = pada, longitude = 0.0,
            isRetrograde = false, isCombust = false, isDebilitated = false, isExalted = false, isOwnSign = false, isMooltrikona = false,
            isFriendlySign = false, isEnemySign = false, isNeutralSign = true, hasNeechaBhangaRajaYoga = false, isInGandanta = false,
            isInMrityuBhaga = false, isInPushkarNavamsha = false, isFunctionalBenefic = false, isFunctionalMalefic = false,
            isYogakaraka = false, aspectingPlanets = emptyList(), aspectedByBenefics = false, aspectedByMalefics = false,
            shadbalaStrength = 0.5, dignityDescription = "Position unknown"
        )
    }

    private fun buildDignityDescription(
        planet: Planet, sign: ZodiacSign, isExalted: Boolean, isDebilitated: Boolean, isOwnSign: Boolean,
        isMooltrikona: Boolean, isFriendlySign: Boolean, isEnemySign: Boolean, isNeutralSign: Boolean,
        isRetrograde: Boolean, isCombust: Boolean, language: Language
    ): String {
        val parts = mutableListOf<String>()
        parts.add(StringResources.get(StringKeyUIPart1.DIGNITY_IN_SIGN, language, planet.getLocalizedName(language), sign.getLocalizedName(language)))
        when {
            isExalted -> parts.add(StringResources.get(StringKeyUIPart1.DIGNITY_EXALTED, language))
            isDebilitated -> parts.add(StringResources.get(StringKeyUIPart1.DIGNITY_DEBILITATED, language))
            isMooltrikona -> parts.add(StringResources.get(StringKeyUIPart1.DIGNITY_MOOLTRIKONA, language))
            isOwnSign -> parts.add(StringResources.get(StringKeyUIPart1.DIGNITY_OWN, language))
            isFriendlySign -> parts.add(StringResources.get(StringKeyUIPart1.DIGNITY_FRIEND, language))
            isEnemySign -> parts.add(StringResources.get(StringKeyUIPart1.DIGNITY_ENEMY, language))
            isNeutralSign -> parts.add(StringResources.get(StringKeyUIPart1.DIGNITY_NEUTRAL, language))
        }
        if (isRetrograde) parts.add(StringResources.get(StringKeyUIPart1.DIGNITY_RETRO, language))
        if (isCombust) parts.add(StringResources.get(StringKeyUIPart1.DIGNITY_COMBUST, language))
        return parts.joinToString(" ")
    }

    private fun isInOwnSign(planet: Planet, sign: ZodiacSign): Boolean {
        return when (planet) {
            Planet.SUN -> sign == ZodiacSign.LEO
            Planet.MOON -> sign == ZodiacSign.CANCER
            Planet.MARS -> sign in listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO)
            Planet.MERCURY -> sign in listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO)
            Planet.JUPITER -> sign in listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES)
            Planet.VENUS -> sign in listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA)
            Planet.SATURN -> sign in listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS)
            Planet.RAHU -> sign == ZodiacSign.AQUARIUS
            Planet.KETU -> sign == ZodiacSign.SCORPIO
            else -> false
        }
    }

    private fun isInMooltrikona(planet: Planet, sign: ZodiacSign, signDegree: Double): Boolean {
        val info = exaltationDebilitationData[planet] ?: return false
        return sign == info.mooltrikonaSign && signDegree >= info.mooltrikonaStartDegree && signDegree <= info.mooltrikonaEndDegree
    }

    private fun checkNeechaBhangaRajaYoga(planet: Planet, chart: VedicChart, ascendantSign: ZodiacSign): Boolean {
        val debInfo = exaltationDebilitationData[planet] ?: return false
        val debSign = debInfo.debilitationSign
        val exaltSign = debInfo.exaltationSign
        val debLord = debSign.ruler
        val exaltLord = exaltSign.ruler
        val moonPosition = chart.planetPositions.find { it.planet == Planet.MOON }
        val moonHouse = moonPosition?.house ?: 1
        val kendraHouses = listOf(1, 4, 7, 10)
        val debLordPosition = chart.planetPositions.find { it.planet == debLord }
        if (debLordPosition != null) {
            val debLordHouseFromAsc = debLordPosition.house
            val debLordHouseFromMoon = ((debLordPosition.house - moonHouse + 12) % 12) + 1
            if (debLordHouseFromAsc in kendraHouses || debLordHouseFromMoon in kendraHouses) return true
        }
        val exaltLordPosition = chart.planetPositions.find { it.planet == exaltLord }
        val planetPosition = chart.planetPositions.find { it.planet == planet }
        if (exaltLordPosition != null && planetPosition != null) {
            if (exaltLordPosition.house == planetPosition.house) return true
        }
        val exaltedPlanets = chart.planetPositions.filter { pos ->
            val exInfo = exaltationDebilitationData[pos.planet]
            exInfo?.exaltationSign == pos.sign
        }
        if (exaltedPlanets.any { it.house == planetPosition?.house }) return true
        return false
    }

    private fun checkCombustion(planet: Planet, chart: VedicChart, isRetrograde: Boolean): Boolean {
        if (planet == Planet.SUN || planet == Planet.RAHU || planet == Planet.KETU) return false
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return false
        val planetPos = chart.planetPositions.find { it.planet == planet } ?: return false
        val combustDegree = when (val deg = combustionDegrees[planet]) {
            is Double -> deg
            is Pair<*, *> -> if (isRetrograde) (deg.second as Double) else (deg.first as Double)
            else -> return false
        }
        val diff = abs(sunPos.longitude - planetPos.longitude)
        val normalizedDiff = if (diff > 180) 360 - diff else diff
        return normalizedDiff <= combustDegree
    }

    private fun checkMaleficConjunction(planet: Planet, chart: VedicChart): List<Planet> {
        val malefics = listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU, Planet.SUN)
        val planetPos = chart.planetPositions.find { it.planet == planet } ?: return emptyList()
        return chart.planetPositions
            .filter { it.planet in malefics && it.planet != planet }
            .filter { it.house == planetPos.house }
            .map { it.planet }
    }

    private fun checkBeneficConjunction(planet: Planet, chart: VedicChart): List<Planet> {
        val benefics = mutableListOf(Planet.JUPITER, Planet.VENUS)
        val moonPosition = chart.planetPositions.find { it.planet == Planet.MOON }
        if (moonPosition != null) {
            val paksha = checkMoonPaksha(chart)
            if (paksha > 0) benefics.add(Planet.MOON)
        }
        val mercuryPosition = chart.planetPositions.find { it.planet == Planet.MERCURY }
        if (mercuryPosition != null) {
            val mercuryConjuncts = chart.planetPositions
                .filter { it.house == mercuryPosition.house && it.planet != Planet.MERCURY }
                .map { it.planet }
            val maleficCount = mercuryConjuncts.count { it in listOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU) }
            val beneficCount = mercuryConjuncts.count { it in listOf(Planet.JUPITER, Planet.VENUS) }
            if (beneficCount >= maleficCount) benefics.add(Planet.MERCURY)
        }
        val planetPos = chart.planetPositions.find { it.planet == planet } ?: return emptyList()
        return chart.planetPositions
            .filter { it.planet in benefics && it.planet != planet }
            .filter { it.house == planetPos.house }
            .map { it.planet }
    }

    private fun getAspectingPlanets(planet: Planet, chart: VedicChart): List<Planet> {
        val planetPos = chart.planetPositions.find { it.planet == planet } ?: return emptyList()
        val planetHouse = planetPos.house
        val aspectingPlanets = mutableListOf<Planet>()
        chart.planetPositions.forEach { pos ->
            if (pos.planet == planet) return@forEach
            val aspecterHouse = pos.house
            val houseDiff = ((planetHouse - aspecterHouse + 12) % 12)
            val aspects = when (pos.planet) {
                Planet.MARS -> listOf(4, 7, 8)
                Planet.JUPITER -> listOf(5, 7, 9)
                Planet.SATURN -> listOf(3, 7, 10)
                Planet.RAHU, Planet.KETU -> listOf(5, 7, 9)
                else -> listOf(7)
            }
            if (houseDiff in aspects) aspectingPlanets.add(pos.planet)
        }
        return aspectingPlanets
    }

    private fun checkGandanta(sign: ZodiacSign, signDegree: Double): Boolean {
        val junctions = listOf(ZodiacSign.CANCER to ZodiacSign.LEO, ZodiacSign.SCORPIO to ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES to ZodiacSign.ARIES)
        for ((waterSign, fireSign) in junctions) {
            if (sign == waterSign && signDegree >= 26.40) return true
            if (sign == fireSign && signDegree <= 3.20) return true
        }
        return false
    }

    private fun checkMrityuBhaga(planet: Planet, sign: ZodiacSign, signDegree: Double): Boolean {
        val mrityuDegree = mrityuBhagaDegrees[sign]?.get(planet) ?: return false
        return abs(signDegree - mrityuDegree) <= 1.0
    }

    private fun checkPushkaraNavamsha(signDegree: Double, sign: ZodiacSign): Boolean {
        val navIndex = (signDegree / 3.333333).toInt()
        val pushkaras = when (sign) {
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> listOf(2, 5, 8)
            ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> listOf(1, 4, 7)
            ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> listOf(0, 3, 6)
            ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES -> listOf(2, 5, 8)
        }
        return navIndex in pushkaras
    }

    private fun checkMoonPaksha(chart: VedicChart): Int {
        val sunPos = chart.planetPositions.find { it.planet == Planet.SUN } ?: return 0
        val moonPos = chart.planetPositions.find { it.planet == Planet.MOON } ?: return 0
        var diff = moonPos.longitude - sunPos.longitude
        if (diff < 0) diff += 360
        val tithi = (diff / 12).toInt() + 1
        return when {
            tithi in 1..5 -> -10
            tithi in 6..10 -> -5
            tithi in 11..15 -> 5
            tithi in 16..20 -> 10
            tithi in 21..25 -> 5
            tithi in 26..30 -> -5
            else -> 0
        }
    }

    private fun isFunctionalBeneficForLagna(planet: Planet, lagna: ZodiacSign): Boolean {
        return when (lagna) {
            ZodiacSign.ARIES -> planet in listOf(Planet.SUN, Planet.MARS, Planet.JUPITER)
            ZodiacSign.TAURUS -> planet in listOf(Planet.SATURN, Planet.MERCURY, Planet.VENUS)
            ZodiacSign.GEMINI -> planet in listOf(Planet.VENUS, Planet.SATURN)
            ZodiacSign.CANCER -> planet in listOf(Planet.MARS, Planet.JUPITER, Planet.MOON)
            ZodiacSign.LEO -> planet in listOf(Planet.MARS, Planet.JUPITER, Planet.SUN)
            ZodiacSign.VIRGO -> planet in listOf(Planet.MERCURY, Planet.VENUS)
            ZodiacSign.LIBRA -> planet in listOf(Planet.SATURN, Planet.MERCURY, Planet.VENUS)
            ZodiacSign.SCORPIO -> planet in listOf(Planet.JUPITER, Planet.MOON, Planet.SUN)
            ZodiacSign.SAGITTARIUS -> planet in listOf(Planet.SUN, Planet.MARS, Planet.JUPITER)
            ZodiacSign.CAPRICORN -> planet in listOf(Planet.VENUS, Planet.MERCURY, Planet.SATURN)
            ZodiacSign.AQUARIUS -> planet in listOf(Planet.VENUS, Planet.SATURN)
            ZodiacSign.PISCES -> planet in listOf(Planet.MARS, Planet.MOON, Planet.JUPITER)
        }
    }

    private fun isFunctionalMaleficForLagna(planet: Planet, lagna: ZodiacSign): Boolean {
        return when (lagna) {
            ZodiacSign.ARIES -> planet in listOf(Planet.MERCURY, Planet.SATURN)
            ZodiacSign.TAURUS -> planet in listOf(Planet.JUPITER, Planet.MARS)
            ZodiacSign.GEMINI -> planet in listOf(Planet.MARS, Planet.JUPITER)
            ZodiacSign.CANCER -> planet in listOf(Planet.SATURN, Planet.MERCURY)
            ZodiacSign.LEO -> planet in listOf(Planet.SATURN, Planet.MERCURY)
            ZodiacSign.VIRGO -> planet in listOf(Planet.MARS, Planet.MOON)
            ZodiacSign.LIBRA -> planet in listOf(Planet.MARS, Planet.JUPITER, Planet.SUN)
            ZodiacSign.SCORPIO -> planet in listOf(Planet.VENUS, Planet.MERCURY)
            ZodiacSign.SAGITTARIUS -> planet in listOf(Planet.VENUS, Planet.SATURN)
            ZodiacSign.CAPRICORN -> planet in listOf(Planet.MARS, Planet.JUPITER, Planet.MOON)
            ZodiacSign.AQUARIUS -> planet in listOf(Planet.MARS, Planet.JUPITER, Planet.MOON)
            ZodiacSign.PISCES -> planet in listOf(Planet.SATURN, Planet.VENUS, Planet.SUN, Planet.MERCURY)
        }
    }

    private fun isYogakarakaPlanet(planet: Planet, lagna: ZodiacSign): Boolean {
        return when (lagna) {
            ZodiacSign.ARIES -> planet == Planet.SATURN
            ZodiacSign.TAURUS -> planet == Planet.SATURN
            ZodiacSign.CANCER -> planet == Planet.MARS
            ZodiacSign.LEO -> planet == Planet.MARS
            ZodiacSign.LIBRA -> planet == Planet.SATURN
            ZodiacSign.SCORPIO -> planet == Planet.MOON
            ZodiacSign.CAPRICORN -> planet == Planet.VENUS
            ZodiacSign.AQUARIUS -> planet == Planet.VENUS
            else -> false
        }
    }
}


