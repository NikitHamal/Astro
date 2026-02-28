package com.astro.vajra.ephemeris.prashna

import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.NATURAL_BENEFICS
import com.astro.vajra.ephemeris.prashna.PrashnaConstants.NATURAL_MALEFICS
import com.astro.vajra.ephemeris.prashna.PrashnaHelpers.angularDistance
import com.astro.vajra.ephemeris.prashna.PrashnaHelpers.isGandanta
import com.astro.vajra.ephemeris.prashna.PrashnaHelpers.isPushkaraNavamsha
import com.astro.vajra.ephemeris.prashna.PrashnaHelpers.normalizeDegrees

object PrashnaYogaEvaluator {

    fun detectPrashnaYogas(
        chart: VedicChart,
        moonAnalysis: MoonAnalysis,
        lagnaAnalysis: LagnaAnalysis,
        language: Language = Language.ENGLISH
    ): List<PrashnaYoga> {
        val yogas = mutableListOf<PrashnaYoga>()

        if (isIthasalaPresent(moonAnalysis)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_ITHASALA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_ITHASALA_DESC, language),
                    isPositive = true,
                    strength = 4,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_ITHASALA_INTERP, language)
                )
            )
        }

        if (isMusariphaPresent(moonAnalysis)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_MUSARIPHA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_MUSARIPHA_DESC, language),
                    isPositive = false,
                    strength = 3,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_MUSARIPHA_INTERP, language)
                )
            )
        }

        if (isNaktaPresent(chart)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_NAKTA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_NAKTA_DESC, language),
                    isPositive = true,
                    strength = 3,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_NAKTA_INTERP, language)
                )
            )
        }

        if (isManaouPresent(chart)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_MANAOU_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_MANAOU_DESC, language),
                    isPositive = false,
                    strength = 4,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_MANAOU_INTERP, language)
                )
            )
        }

        if (isKamboolaPresent(moonAnalysis)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_KAMBOOLA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_KAMBOOLA_DESC, language),
                    isPositive = true,
                    strength = 4,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_KAMBOOLA_INTERP, language)
                )
            )
        }

        if (isGairiKamboolaPresent(moonAnalysis)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_GAIRI_KAMBOOLA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_GAIRI_KAMBOOLA_DESC, language),
                    isPositive = false,
                    strength = 2,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_GAIRI_KAMBOOLA_INTERP, language)
                )
            )
        }

        if (isDhurufaPresent(moonAnalysis)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_DHURUFA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_DHURUFA_DESC, language),
                    isPositive = false,
                    strength = 4,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_DHURUFA_INTERP, language)
                )
            )
        }

        if (isPushkaraNavamsha(moonAnalysis.position.longitude)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_PUSHKARA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_PUSHKARA_DESC, language),
                    isPositive = true,
                    strength = 5,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_PUSHKARA_INTERP, language)
                )
            )
        }

        if (isGandanta(moonAnalysis.position.longitude)) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_GANDANTA_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_GANDANTA_DESC, language),
                    isPositive = false,
                    strength = 5,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_GANDANTA_INTERP, language)
                )
            )
        }

        if (moonAnalysis.moonHouse == lagnaAnalysis.lagnaLordPosition.house ||
            angularDistance(moonAnalysis.position.longitude, lagnaAnalysis.lagnaLordPosition.longitude) < 10) {
            yogas.add(
                PrashnaYoga(
                    name = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_UNION_NAME, language),
                    description = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_UNION_DESC, language),
                    isPositive = true,
                    strength = 4,
                    interpretation = StringResources.get(StringKeyAnalysis.PRASHNA_YOGA_UNION_INTERP, language)
                )
            )
        }

        return yogas
    }

    private fun isIthasalaPresent(moonAnalysis: MoonAnalysis): Boolean {
        if (moonAnalysis.position.speed <= 0) return false
        return moonAnalysis.nextAspect != null && moonAnalysis.nextAspect.isBenefic
    }

    private fun isMusariphaPresent(moonAnalysis: MoonAnalysis): Boolean {
        return moonAnalysis.lastAspect != null && !moonAnalysis.isVoidOfCourse
    }

    private fun isNaktaPresent(chart: VedicChart): Boolean {
        val moon = chart.planetPositions.first { it.planet == Planet.MOON }
        val benefics = chart.planetPositions.filter { it.planet in NATURAL_BENEFICS && it.planet != Planet.MOON }
        var separating = false
        var applying = false
        for (benefic in benefics) {
            val distance = normalizeDegrees(benefic.longitude - moon.longitude)
            if (distance < 10 && distance > 0) applying = true
            if (distance > 350 || (distance < 0 && distance > -10)) separating = true
        }
        return separating && applying
    }

    private fun isManaouPresent(chart: VedicChart): Boolean {
        val moon = chart.planetPositions.first { it.planet == Planet.MOON }
        val lagnaLord = ZodiacSign.fromLongitude(chart.ascendant).ruler
        val lagnaLordPos = chart.planetPositions.first { it.planet == lagnaLord }
        val moonLong = moon.longitude
        val lordLong = lagnaLordPos.longitude
        for (malefic in chart.planetPositions.filter { it.planet in NATURAL_MALEFICS }) {
            val maleficLong = malefic.longitude
            if ((maleficLong > moonLong && maleficLong < lordLong) ||
                (maleficLong < moonLong && maleficLong > lordLong)) {
                return true
            }
        }
        return false
    }

    private fun isKamboolaPresent(moonAnalysis: MoonAnalysis): Boolean {
        val angularHouses = listOf(1, 4, 7, 10)
        return moonAnalysis.moonHouse in angularHouses &&
               moonAnalysis.moonStrength.score >= 3
    }

    private fun isGairiKamboolaPresent(moonAnalysis: MoonAnalysis): Boolean {
        val angularHouses = listOf(1, 4, 7, 10)
        return moonAnalysis.moonHouse in angularHouses &&
               moonAnalysis.moonStrength.score < 3
    }

    private fun isDhurufaPresent(moonAnalysis: MoonAnalysis): Boolean {
        val cadentHouses = listOf(3, 6, 9, 12)
        return moonAnalysis.moonHouse in cadentHouses &&
               moonAnalysis.moonStrength.score <= 2
    }
}


