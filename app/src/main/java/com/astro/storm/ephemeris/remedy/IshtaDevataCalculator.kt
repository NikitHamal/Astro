package com.astro.storm.ephemeris.remedy

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyRemedy
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.CharaDashaCalculator
import com.astro.storm.ephemeris.DivisionalChartCalculator

object IshtaDevataCalculator {

    data class IshtaDevataProfile(
        val atmakaraka: Planet,
        val karakamshaSign: ZodiacSign,
        val ishtaSign: ZodiacSign,
        val ishtaPlanet: Planet,
        val deityName: String,
        val description: String
    )

    fun calculate(chart: VedicChart, language: Language): IshtaDevataProfile {
        val karakas = CharaDashaCalculator.calculateCharaKarakas(chart)
        val atmakaraka = karakas.atmakaraka.planet
        val d9 = DivisionalChartCalculator.calculateNavamsa(chart)
        val akPosition = d9.planetPositions.firstOrNull { it.planet == atmakaraka }
        val karakamshaSign = akPosition?.sign ?: ZodiacSign.fromLongitude(d9.ascendantLongitude)
        val ishtaSign = getTwelfthFrom(karakamshaSign)
        val ishtaPlanet = ishtaSign.ruler

        val deityKey = StringKeyRemedy.valueOf("DEITY_${ishtaPlanet.name}_PRIM")
        val deityName = StringResources.get(deityKey, language)
        val description = StringResources.get(
            StringKeyRemedy.ISHTA_DEVATA_DESC,
            language,
            atmakaraka.getLocalizedName(language),
            karakamshaSign.getLocalizedName(language),
            ishtaSign.getLocalizedName(language),
            deityName
        )

        return IshtaDevataProfile(
            atmakaraka = atmakaraka,
            karakamshaSign = karakamshaSign,
            ishtaSign = ishtaSign,
            ishtaPlanet = ishtaPlanet,
            deityName = deityName,
            description = description
        )
    }

    private fun getTwelfthFrom(sign: ZodiacSign): ZodiacSign {
        val number = (sign.number + 10) % 12
        val normalized = if (number == 0) 12 else number
        return ZodiacSign.entries.first { it.number == normalized }
    }
}
