package com.astro.storm.ephemeris.varshaphala

import android.content.Context
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.varshaphala.SolarReturnCalculator.calculateSolarReturnChart
import com.astro.storm.ephemeris.varshaphala.SolarReturnCalculator.calculateSolarReturnTime
import com.astro.storm.ephemeris.varshaphala.VarshaphalaConstants.AYANAMSA_LAHIRI
import com.astro.storm.ephemeris.varshaphala.VarshaphalaEvaluator.calculateMuntha
import com.astro.storm.ephemeris.varshaphala.VarshaphalaEvaluator.calculateTriPatakiChakra
import com.astro.storm.ephemeris.varshaphala.VarshaphalaEvaluator.determineYearLord
import com.astro.storm.ephemeris.varshaphala.VarshaphalaHelpers.evaluatePlanetStrengthDescription
import swisseph.SwissEph
import java.time.LocalDateTime
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.HouseSystem
import com.astro.storm.ephemeris.varshaphala.TajikaYogaCalculator

class VarshaphalaCalculator(context: Context) {

    private val swissEph = SwissEph()
    private val ephemerisPath: String = context.filesDir.absolutePath + "/ephe"

    init {
        swissEph.swe_set_ephe_path(ephemerisPath)
        swissEph.swe_set_sid_mode(AYANAMSA_LAHIRI, 0.0, 0.0)
    }

    fun calculateVarshaphala(natalChart: VedicChart, year: Int, language: Language = Language.ENGLISH): VarshaphalaResult {
        val birthDateTime = natalChart.birthData.dateTime
        val age = year - birthDateTime.year
        require(age >= 0) { "Year must be after birth year" }

        val solarReturnTime = calculateSolarReturnTime(birthDateTime, year, natalChart.birthData.latitude, natalChart.birthData.longitude, natalChart.birthData.timezone, swissEph)
        val solarReturnChart = calculateSolarReturnChart(solarReturnTime, natalChart.birthData.latitude, natalChart.birthData.longitude, natalChart.birthData.timezone, year, swissEph)

        val panchaVargiyaBala = PanchaVargiyaBalaCalculator.calculateAllPanchaVargiyaBalas(solarReturnChart, language)
        val muntha = calculateMuntha(natalChart, age, solarReturnChart, language)
        val yearLord = determineYearLord(solarReturnChart, muntha, natalChart, panchaVargiyaBala)
        val yearLordHouse = solarReturnChart.planetPositions[yearLord]?.house ?: 1
        val yearLordStrength = evaluatePlanetStrengthDescription(yearLord, solarReturnChart, language)
        val yearLordDignity = getYearLordDignityDescription(yearLord, solarReturnChart, language)
        val triPatakiChakra = calculateTriPatakiChakra(solarReturnChart, language)
        val sahams = SahamCalculator.calculateSahams(solarReturnChart, language)
        val tajikaAspects = TajikaAspectAnalyzer.calculateTajikaAspects(solarReturnChart, language)
        val solarReturnVedicChart = createVedicChartFromSolarReturn(solarReturnChart, natalChart)
        val tajikaYogas = TajikaYogaCalculator.calculateTajikaYogas(solarReturnVedicChart, natalChart)
        val muddaDasha = MuddaDashaCalculator.calculateMuddaDasha(solarReturnChart, solarReturnTime.toLocalDate(), language)
        val housePredictions = VarshaphalaInterpretationGenerator.generateHousePredictions(solarReturnChart, muntha, yearLord, language)
        val majorThemes = VarshaphalaInterpretationGenerator.identifyMajorThemes(solarReturnChart, muntha, yearLord, housePredictions, triPatakiChakra, tajikaAspects, language)
        val (favorableMonths, challengingMonths) = calculateMonthlyInfluences(solarReturnChart, solarReturnTime)
        val keyDates = calculateKeyDates(solarReturnChart, solarReturnTime, muddaDasha, language)
        val overallPrediction = VarshaphalaInterpretationGenerator.generateOverallPrediction(solarReturnChart, yearLord, muntha, tajikaAspects, housePredictions, language)
        val yearRating = VarshaphalaInterpretationGenerator.calculateYearRating(solarReturnChart, yearLord, muntha, tajikaAspects, housePredictions, language)

        return VarshaphalaResult(
            natalChart, year, age, solarReturnChart, yearLord, yearLordStrength, yearLordHouse, yearLordDignity,
            muntha, panchaVargiyaBala, triPatakiChakra, sahams, tajikaAspects, tajikaYogas, muddaDasha, housePredictions,
            majorThemes, favorableMonths, challengingMonths, overallPrediction, yearRating, keyDates
        )
    }

    private fun getYearLordDignityDescription(planet: com.astro.storm.core.model.Planet, chart: SolarReturnChart, language: Language): String {
        val pos = chart.planetPositions[planet] ?: return ""
        return StringResources.get(StringKeyAnalysis.VARSHA_YEARLORD_DIGNITY_FORMAT, language, planet.getLocalizedName(language), pos.sign.getLocalizedName(language), pos.house)
    }

    private fun calculateMonthlyInfluences(chart: SolarReturnChart, solarReturnTime: LocalDateTime): Pair<List<Int>, List<Int>> {
        val fav = mutableListOf<Int>(); val chal = mutableListOf<Int>()
        val ylHouse = chart.planetPositions[chart.ascendant.ruler]?.house ?: 1
        for (i in 0..11) {
            val m = ((solarReturnTime.monthValue - 1 + i) % 12) + 1
            val h = (ylHouse + i - 1) % 12 + 1
            if (h in listOf(1, 2, 4, 5, 7, 9, 10, 11)) { if (fav.size < 4) fav.add(m) }
            else { if (chal.size < 3) chal.add(m) }
        }
        return Pair(fav, chal)
    }

    private fun calculateKeyDates(chart: SolarReturnChart, solarReturnTime: LocalDateTime, muddaDasha: List<MuddaDashaPeriod>, language: Language): List<KeyDate> {
        val dates = mutableListOf(KeyDate(solarReturnTime.toLocalDate(), StringResources.get(StringKeyAnalysis.VARSHA_EVENT_SOLAR_RETURN, language), KeyDateType.IMPORTANT, StringResources.get(StringKeyAnalysis.VARSHA_EVENT_SOLAR_RETURN_DESC, language)))
        muddaDasha.forEach { dates.add(KeyDate(it.startDate, StringResources.get(StringKeyAnalysis.VARSHA_EVENT_DASHA_BEGINS, language, it.planet.getLocalizedName(language)), if (it.planetStrength in listOf(StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_EXALTED, language), StringResources.get(StringKeyAnalysis.VARSHA_STRENGTH_STRONG, language))) KeyDateType.FAVORABLE else KeyDateType.IMPORTANT, StringResources.get(StringKeyAnalysis.VARSHA_EVENT_DASHA_BEGINS_DESC, language, it.planet.getLocalizedName(language), it.days))) }
        return dates.sortedBy { it.date }.take(15)
    }

    fun close() { swissEph.swe_close() }

    private fun createVedicChartFromSolarReturn(srChart: SolarReturnChart, natalChart: VedicChart): VedicChart {
        val mappedPositions = srChart.planetPositions.map { (planet, pos) ->
            val (nakshatra, _) = Nakshatra.fromLongitude(pos.longitude)
            val degreeInSign = pos.longitude % 30.0
            val d = degreeInSign.toInt().toDouble()
            val m = ((degreeInSign - d) * 60).toInt().toDouble()
            val s = ((((degreeInSign - d) * 60) - m) * 60).toInt().toDouble()

            PlanetPosition(
                planet = planet,
                longitude = pos.longitude,
                latitude = 0.0,
                distance = 0.0,
                speed = pos.speed,
                sign = pos.sign,
                degree = d,
                minutes = m,
                seconds = s,
                isRetrograde = pos.isRetrograde,
                nakshatra = nakshatra,
                nakshatraPada = pos.nakshatraPada,
                house = pos.house,
                isOnHouseCusp = false
            )
        }
        
        return VedicChart(
            id = 0,
            birthData = natalChart.birthData.copy(dateTime = srChart.solarReturnTime),
            julianDay = srChart.julianDay,
            ayanamsa = srChart.ayanamsa,
            ayanamsaName = "Lahiri",
            ascendant = srChart.ascendantDegree + (srChart.ascendant.ordinal * 30.0),
            midheaven = srChart.midheaven,
            planetPositions = mappedPositions,
            houseCusps = srChart.houseCusps,
            houseSystem = HouseSystem.PORPHYRIUS
        )
    }
}


