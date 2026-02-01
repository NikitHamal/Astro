package com.astro.storm.ephemeris.prashna

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.prashna.PrashnaConstants.PRASHNA_HOUSE_SIGNIFICATIONS
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.angularDistance
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.calculateHoraLord
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.detectPlanetaryWars
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.getCombustionOrb
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.getDayLord
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.isAuspiciousNakshatra
import com.astro.storm.ephemeris.prashna.PrashnaHelpers.localized
import java.time.LocalDateTime

object PrashnaOmenEvaluator {

    fun detectOmens(
        chart: VedicChart,
        questionTime: LocalDateTime,
        moonAnalysis: MoonAnalysis,
        language: Language
    ): List<PrashnaOmen> {
        val omens = mutableListOf<PrashnaOmen>()

        val lagnaSign = ZodiacSign.fromLongitude(chart.ascendant)
        omens.add(
            PrashnaOmen(
                type = OmenType.PRASHNA_LAGNA,
                description = StringResources.get(StringKeyPrashnaPart1.PRASHNA_OMEN_LAGNA_DESC, language, lagnaSign.getLocalizedName(language)),
                indication = getLagnaSignIndication(lagnaSign, language),
                isPositive = lagnaSign.element in listOf("Fire", "Air")
            )
        )

        val moonHouseSignification = PRASHNA_HOUSE_SIGNIFICATIONS[moonAnalysis.moonHouse]
        omens.add(
            PrashnaOmen(
                type = OmenType.MOON_PLACEMENT,
                description = StringResources.get(StringKeyPrashnaPart1.PRASHNA_OMEN_MOON_PLACEMENT_DESC, language, moonAnalysis.moonHouse.localized(language), moonHouseSignification?.getLocalizedName(language) ?: ""),
                indication = getMoonHouseIndication(moonAnalysis.moonHouse, language),
                isPositive = moonAnalysis.moonHouse in listOf(1, 4, 5, 7, 9, 10, 11)
            )
        )

        val horaLord = calculateHoraLord(questionTime)
        omens.add(
            PrashnaOmen(
                type = OmenType.HORA_LORD,
                description = StringResources.get(StringKeyPrashnaPart1.PRASHNA_OMEN_HORA_LORD_DESC, language, horaLord.getLocalizedName(language)),
                indication = getHoraLordIndication(horaLord, language),
                isPositive = horaLord in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
            )
        )

        val dayLord = getDayLord(questionTime)
        omens.add(
            PrashnaOmen(
                type = OmenType.DAY_LORD,
                description = StringResources.get(StringKeyPrashnaPart1.PRASHNA_OMEN_DAY_LORD_DESC, language, dayLord.getLocalizedName(language)),
                indication = getDayLordIndication(dayLord, language),
                isPositive = dayLord in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)
            )
        )

        omens.add(
            PrashnaOmen(
                type = OmenType.NAKSHATRA,
                description = StringResources.get(StringKeyPrashnaPart1.PRASHNA_OMEN_NAKSHATRA_DESC, language, moonAnalysis.nakshatra.getLocalizedName(language)),
                indication = getNakshatraIndication(moonAnalysis.nakshatra, language),
                isPositive = isAuspiciousNakshatra(moonAnalysis.nakshatra)
            )
        )

        val planetaryWars = detectPlanetaryWars(chart)
        for (war in planetaryWars) {
            omens.add(
                PrashnaOmen(
                    type = OmenType.PLANETARY_WAR,
                    description = StringResources.get(StringKeyPrashnaPart1.PRASHNA_OMEN_PLANETARY_WAR_DESC, language, war.first.getLocalizedName(language), war.second.getLocalizedName(language)),
                    indication = StringResources.get(StringKeyPrashnaPart1.PRASHNA_OMEN_WAR_IND, language),
                    isPositive = false
                )
            )
        }

        val sunPosition = chart.planetPositions.first { it.planet == Planet.SUN }
        val combustPlanets = chart.planetPositions.filter { position ->
            position.planet != Planet.SUN &&
                    angularDistance(position.longitude, sunPosition.longitude) < getCombustionOrb(position.planet)
        }
        for (planet in combustPlanets) {
            omens.add(
                PrashnaOmen(
                    type = OmenType.COMBUSTION,
                    description = StringResources.get(StringKeyPrashnaPart1.PRASHNA_OMEN_COMBUSTION_DESC, language, planet.planet.getLocalizedName(language)),
                    indication = StringResources.get(StringKeyPrashnaPart1.PRASHNA_OMEN_COMBUST_IND, language, planet.planet.getLocalizedName(language)),
                    isPositive = false
                )
            )
        }

        val retrogradePlanets = chart.planetPositions.filter {
            it.isRetrograde && it.planet !in listOf(Planet.SUN, Planet.MOON, Planet.RAHU, Planet.KETU)
        }
        for (planet in retrogradePlanets) {
            omens.add(
                PrashnaOmen(
                    type = OmenType.RETROGRADE,
                    description = StringResources.get(StringKeyPrashnaPart1.PRASHNA_OMEN_RETROGRADE_DESC, language, planet.planet.getLocalizedName(language)),
                    indication = StringResources.get(StringKeyPrashnaPart1.PRASHNA_OMEN_RETRO_IND, language, planet.planet.getLocalizedName(language)),
                    isPositive = false
                )
            )
        }

        return omens
    }

    private fun getLagnaSignIndication(sign: ZodiacSign, language: Language): String {
        val key = when (sign.element.lowercase()) {
            "fire" -> StringKeyPrashnaPart1.PRASHNA_TATTVA_FIRE
            "earth" -> StringKeyPrashnaPart1.PRASHNA_TATTVA_EARTH
            "air" -> StringKeyPrashnaPart1.PRASHNA_TATTVA_AIR
            "water" -> StringKeyPrashnaPart1.PRASHNA_TATTVA_WATER
            else -> StringKeyPrashnaPart1.PRASHNA_TATTVA_ETHER
        }
        return StringResources.get(key, language)
    }

    private fun getMoonHouseIndication(house: Int, language: Language): String {
        return StringResources.get(StringKeyPrashnaPart1.PRASHNA_MOON_HOUSE_IND_TEMPLATE, language).format(house.localized(language))
    }

    private fun getHoraLordIndication(lord: Planet, language: Language): String {
        val key = when (lord) {
            Planet.SUN -> StringKeyVarshaphala.VARSHA_YEARLORD_SUN
            Planet.MOON -> StringKeyVarshaphala.VARSHA_YEARLORD_MOON
            Planet.MARS -> StringKeyVarshaphala.VARSHA_YEARLORD_MARS
            Planet.MERCURY -> StringKeyVarshaphala.VARSHA_YEARLORD_MERCURY
            Planet.JUPITER -> StringKeyVarshaphala.VARSHA_YEARLORD_JUPITER
            Planet.VENUS -> StringKeyVarshaphala.VARSHA_YEARLORD_VENUS
            Planet.SATURN -> StringKeyVarshaphala.VARSHA_YEARLORD_SATURN
            else -> StringKeyVarshaphala.VARSHA_YEARLORD_GENERIC
        }
        return StringResources.get(key, language)
    }

    private fun getDayLordIndication(lord: Planet, language: Language): String {
        val key = when (lord) {
            Planet.SUN -> StringKeyMatchPart1.VARA_SUNDAY
            Planet.MOON -> StringKeyMatchPart1.VARA_MONDAY
            Planet.MARS -> StringKeyMatchPart1.VARA_TUESDAY
            Planet.MERCURY -> StringKeyMatchPart1.VARA_WEDNESDAY
            Planet.JUPITER -> StringKeyMatchPart1.VARA_THURSDAY
            Planet.VENUS -> StringKeyMatchPart1.VARA_FRIDAY
            Planet.SATURN -> StringKeyMatchPart1.VARA_SATURDAY
            else -> StringKeyAnalysisPart2.AUSPICIOUSNESS_NEUTRAL
        }
        return StringResources.get(key, language)
    }

    private fun getNakshatraIndication(nakshatra: com.astro.storm.core.model.Nakshatra, language: Language): String {
        return StringResources.get(StringKeyPrashnaPart1.PRASHNA_NAKSHATRA_IND_TEMPLATE, language).format(
            nakshatra.getLocalizedName(language),
            nakshatra.ruler.getLocalizedName(language)
        )
    }
}


