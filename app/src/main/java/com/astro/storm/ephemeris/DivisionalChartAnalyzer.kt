package com.astro.storm.ephemeris

import com.astro.storm.R
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import com.astro.storm.util.LocalizableString

/**
 * Comprehensive Divisional Chart Analyzer
 * Provides detailed interpretations for all 16 Varga charts per BPHS
 */
object DivisionalChartAnalyzer {

    // Hora Chart (D-2) Analysis for Wealth
    fun analyzeHora(chart: VedicChart): HoraAnalysis {
        val horaChart = DivisionalChartCalculator.calculateHora(chart)
        val sunHoraPlanets = mutableListOf<Planet>()
        val moonHoraPlanets = mutableListOf<Planet>()

        horaChart.planetPositions.forEach { position ->
            when (position.sign) {
                ZodiacSign.LEO -> sunHoraPlanets.add(position.planet)
                ZodiacSign.CANCER -> moonHoraPlanets.add(position.planet)
                else -> {}
            }
        }

        val secondLordD1 = getHouseLord(chart, 2)
        val eleventhLordD1 = getHouseLord(chart, 11)
        val secondLordInHora = horaChart.planetPositions.find { it.planet == secondLordD1 }
        val eleventhLordInHora = horaChart.planetPositions.find { it.planet == eleventhLordD1 }

        val wealthIndicators = mutableListOf<WealthIndicator>()

        // Sun Hora planets indicate self-earned wealth
        sunHoraPlanets.forEach { planet ->
            wealthIndicators.add(
                WealthIndicator(
                    planet = planet,
                    type = WealthType.SELF_EARNED,
                    strength = calculateHoraStrength(planet, ZodiacSign.LEO, chart),
                    sources = getSunHoraWealthSources(planet)
                )
            )
        }

        // Moon Hora planets indicate inherited/liquid wealth
        moonHoraPlanets.forEach { planet ->
            wealthIndicators.add(
                WealthIndicator(
                    planet = planet,
                    type = WealthType.INHERITED_LIQUID,
                    strength = calculateHoraStrength(planet, ZodiacSign.CANCER, chart),
                    sources = getMoonHoraWealthSources(planet)
                )
            )
        }

        val overallWealthPotential = calculateOverallWealthPotential(
            sunHoraPlanets, moonHoraPlanets, secondLordInHora, eleventhLordInHora, chart
        )

        val wealthTimingPeriods = calculateWealthTimingFromHora(chart, wealthIndicators)

        return HoraAnalysis(
            sunHoraPlanets = sunHoraPlanets,
            moonHoraPlanets = moonHoraPlanets,
            wealthIndicators = wealthIndicators,
            overallWealthPotential = overallWealthPotential,
            secondLordHoraSign = secondLordInHora?.sign,
            eleventhLordHoraSign = eleventhLordInHora?.sign,
            wealthTimingPeriods = wealthTimingPeriods,
            recommendations = generateHoraRecommendations(wealthIndicators, overallWealthPotential)
        )
    }

    // Drekkana Chart (D-3) Analysis for Siblings and Courage
    fun analyzeDrekkana(chart: VedicChart): DrekkanaAnalysis {
        val drekkanaChart = DivisionalChartCalculator.calculateDrekkana(chart)
        val marsPosition = drekkanaChart.planetPositions.find { it.planet == Planet.MARS }
        val thirdLordD1 = getHouseLord(chart, 3)
        val thirdLordInDrekkana = drekkanaChart.planetPositions.find { it.planet == thirdLordD1 }

        val siblingIndicators = analyzeSiblings(drekkanaChart, chart)
        val courageAnalysis = analyzeCourage(marsPosition, thirdLordInDrekkana, drekkanaChart)
        val communicationAnalysis = analyzeCommunication(drekkanaChart, chart)

        val thirdHousePlanetsD3 = drekkanaChart.planetPositions.filter { it.house == 3 }
        val eleventhHousePlanetsD3 = drekkanaChart.planetPositions.filter { it.house == 11 }

        return DrekkanaAnalysis(
            marsInDrekkana = marsPosition,
            thirdLordPosition = thirdLordInDrekkana,
            siblingIndicators = siblingIndicators,
            courageAnalysis = courageAnalysis,
            communicationSkills = communicationAnalysis,
            thirdHousePlanets = thirdHousePlanetsD3,
            eleventhHousePlanets = eleventhHousePlanetsD3,
            shortJourneyIndicators = analyzeShortJourneys(drekkanaChart),
            recommendations = generateDrekkanaRecommendations(courageAnalysis, siblingIndicators)
        )
    }

    // Navamsa (D-9) Marriage Timing Analysis
    fun analyzeNavamsaForMarriage(chart: VedicChart): NavamsaMarriageAnalysis {
        val navamsaChart = DivisionalChartCalculator.calculateNavamsa(chart)
        val venusD9 = navamsaChart.planetPositions.find { it.planet == Planet.VENUS }
        val jupiterD9 = navamsaChart.planetPositions.find { it.planet == Planet.JUPITER }
        val seventhLordD1 = getHouseLord(chart, 7)
        val seventhLordD9 = navamsaChart.planetPositions.find { it.planet == seventhLordD1 }
        val navamsaLagnaLord = navamsaChart.ascendantSign.ruler
        val navamsaLagnaLordPosition = navamsaChart.planetPositions.find { it.planet == navamsaLagnaLord }

        // Calculate Upapada (A2) - Arudha of 2nd house
        val upapadaSign = calculateUpapada(chart)
        val upapadaLord = upapadaSign.ruler
        val upapadaLordD9 = navamsaChart.planetPositions.find { it.planet == upapadaLord }

        // Calculate Darakaraka (planet with lowest degree excluding Rahu/Ketu)
        val darakaraka = calculateDarakaraka(chart)
        val darakarakaD9 = navamsaChart.planetPositions.find { it.planet == darakaraka }

        val marriageTimingFactors = analyzeMarriageTimingFactors(
            chart, navamsaChart, venusD9, jupiterD9, seventhLordD9, darakarakaD9
        )

        val spouseCharacteristics = analyzeSpouseCharacteristics(
            seventhLordD9, venusD9, darakarakaD9, upapadaLordD9, navamsaChart
        )

        val spouseDirection = calculateSpouseDirection(seventhLordD9, darakarakaD9)

        val multipleMarriageIndicators = analyzeMultipleMarriageFactors(chart, navamsaChart)

        return NavamsaMarriageAnalysis(
            venusInNavamsa = venusD9,
            jupiterInNavamsa = jupiterD9,
            seventhLordNavamsa = seventhLordD9,
            navamsaLagnaLordPosition = navamsaLagnaLordPosition,
            upapadaSign = upapadaSign,
            upapadaLordNavamsa = upapadaLordD9,
            darakaraka = darakaraka,
            darakarakaNavamsa = darakarakaD9,
            marriageTimingFactors = marriageTimingFactors,
            spouseCharacteristics = spouseCharacteristics,
            spouseDirection = spouseDirection,
            multipleMarriageIndicators = multipleMarriageIndicators,
            marriageMuhurtaCompatibility = analyzeMarriageMuhurtaCompatibility(chart, navamsaChart),
            recommendations = generateMarriageRecommendations(marriageTimingFactors)
        )
    }

    // Dashamsa (D-10) Career Guidance Analysis
    fun analyzeDashamsa(chart: VedicChart): DashamsaAnalysis {
        val dashamsaChart = DivisionalChartCalculator.calculateDasamsa(chart)
        val tenthLordD1 = getHouseLord(chart, 10)
        val tenthLordD10 = dashamsaChart.planetPositions.find { it.planet == tenthLordD1 }
        val sunD10 = dashamsaChart.planetPositions.find { it.planet == Planet.SUN }
        val saturnD10 = dashamsaChart.planetPositions.find { it.planet == Planet.SATURN }
        val mercuryD10 = dashamsaChart.planetPositions.find { it.planet == Planet.MERCURY }

        val careerType = determineCareerType(dashamsaChart, chart)
        val industryMappings = mapPlanetsToIndustries(dashamsaChart)
        val governmentServiceIndicators = analyzeGovernmentServicePotential(sunD10, dashamsaChart)
        val businessVsService = analyzeBusinessVsService(dashamsaChart, chart)
        val careerPeakTiming = calculateCareerPeakTiming(chart, dashamsaChart)
        val multipleCareerIndicators = analyzeMultipleCareers(dashamsaChart)

        return DashamsaAnalysis(
            tenthLordInDashamsa = tenthLordD10,
            sunInDashamsa = sunD10,
            saturnInDashamsa = saturnD10,
            mercuryInDashamsa = mercuryD10,
            dashamsaLagna = dashamsaChart.ascendantSign,
            careerTypes = careerType,
            industryMappings = industryMappings,
            governmentServicePotential = governmentServiceIndicators,
            businessVsServiceAptitude = businessVsService,
            careerPeakPeriods = careerPeakTiming,
            multipleCareerIndicators = multipleCareerIndicators,
            professionalStrengths = analyzeProfessionalStrengths(dashamsaChart),
            recommendations = generateCareerRecommendations(careerType, industryMappings)
        )
    }

    // Dwadasamsa (D-12) Parental Analysis
    fun analyzeDwadasamsa(chart: VedicChart): DwadasamsaAnalysis {
        val dwadasamsaChart = DivisionalChartCalculator.calculateDwadasamsa(chart)
        val sunD12 = dwadasamsaChart.planetPositions.find { it.planet == Planet.SUN }
        val moonD12 = dwadasamsaChart.planetPositions.find { it.planet == Planet.MOON }
        val ninthLordD1 = getHouseLord(chart, 9)
        val fourthLordD1 = getHouseLord(chart, 4)
        val ninthLordD12 = dwadasamsaChart.planetPositions.find { it.planet == ninthLordD1 }
        val fourthLordD12 = dwadasamsaChart.planetPositions.find { it.planet == fourthLordD1 }

        val fatherAnalysis = analyzeFatherIndicators(sunD12, ninthLordD12, dwadasamsaChart)
        val motherAnalysis = analyzeMotherIndicators(moonD12, fourthLordD12, dwadasamsaChart)
        val inheritanceAnalysis = analyzeInheritance(dwadasamsaChart, chart)
        val ancestralPropertyAnalysis = analyzeAncestralProperty(dwadasamsaChart)
        val familyLineageAnalysis = analyzeFamilyLineage(dwadasamsaChart)

        return DwadasamsaAnalysis(
            sunInDwadasamsa = sunD12,
            moonInDwadasamsa = moonD12,
            ninthLordPosition = ninthLordD12,
            fourthLordPosition = fourthLordD12,
            fatherAnalysis = fatherAnalysis,
            motherAnalysis = motherAnalysis,
            inheritanceIndicators = inheritanceAnalysis,
            ancestralPropertyIndicators = ancestralPropertyAnalysis,
            familyLineageInsights = familyLineageAnalysis,
            parentalLongevityIndicators = analyzeParentalLongevity(dwadasamsaChart, chart),
            recommendations = generateParentalRecommendations(fatherAnalysis, motherAnalysis)
        )
    }

    // Helper functions
    private fun getHouseLord(chart: VedicChart, house: Int): Planet {
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseSign = ZodiacSign.entries[(ascendantSign.ordinal + house - 1) % 12]
        return houseSign.ruler
    }

    private fun calculateHoraStrength(planet: Planet, horaSign: ZodiacSign, chart: VedicChart): Double {
        var strength = 50.0

        // Check if planet is naturally suited for wealth
        if (planet in listOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)) {
            strength += 15.0
        }

        // Sun Hora (Leo) favors Sun, Mars, Jupiter
        if (horaSign == ZodiacSign.LEO && planet in listOf(Planet.SUN, Planet.MARS, Planet.JUPITER)) {
            strength += 20.0
        }

        // Moon Hora (Cancer) favors Moon, Venus, Jupiter
        if (horaSign == ZodiacSign.CANCER && planet in listOf(Planet.MOON, Planet.VENUS, Planet.JUPITER)) {
            strength += 20.0
        }

        // Check D1 position dignity
        val d1Position = chart.planetPositions.find { it.planet == planet }
        if (d1Position != null) {
            if (VedicAstrologyUtils.isExalted(d1Position)) strength += 15.0
            if (VedicAstrologyUtils.isInOwnSign(d1Position)) strength += 10.0
            if (VedicAstrologyUtils.isDebilitated(d1Position)) strength -= 20.0
        }

        return strength.coerceIn(0.0, 100.0)
    }

    private fun getSunHoraWealthSources(planet: Planet): List<LocalizableString> {
        val resId = when (planet) {
            Planet.SUN -> R.string.sun_hora_source_sun
            Planet.MARS -> R.string.sun_hora_source_mars
            Planet.JUPITER -> R.string.sun_hora_source_jupiter
            Planet.SATURN -> R.string.sun_hora_source_saturn
            Planet.MERCURY -> R.string.sun_hora_source_mercury
            Planet.VENUS -> R.string.sun_hora_source_venus
            Planet.MOON -> R.string.sun_hora_source_moon
            Planet.RAHU -> R.string.sun_hora_source_rahu
            Planet.KETU -> R.string.sun_hora_source_ketu
            else -> return emptyList()
        }
        return listOf(LocalizableString.Res(resId))
    }

    private fun getMoonHoraWealthSources(planet: Planet): List<LocalizableString> {
        val resId = when (planet) {
            Planet.MOON -> R.string.moon_hora_source_moon
            Planet.VENUS -> R.string.moon_hora_source_venus
            Planet.JUPITER -> R.string.moon_hora_source_jupiter
            Planet.MERCURY -> R.string.moon_hora_source_mercury
            Planet.SUN -> R.string.moon_hora_source_sun
            Planet.MARS -> R.string.moon_hora_source_mars
            Planet.SATURN -> R.string.moon_hora_source_saturn
            Planet.RAHU -> R.string.moon_hora_source_rahu
            Planet.KETU -> R.string.moon_hora_source_ketu
            else -> return emptyList()
        }
        return listOf(LocalizableString.Res(resId))
    }

    private fun calculateOverallWealthPotential(
        sunHoraPlanets: List<Planet>,
        moonHoraPlanets: List<Planet>,
        secondLord: PlanetPosition?,
        eleventhLord: PlanetPosition?,
        chart: VedicChart
    ): WealthPotential {
        var score = 50.0

        // Benefics in hora increase wealth
        val beneficsInSunHora = sunHoraPlanets.count { VedicAstrologyUtils.isNaturalBenefic(it) }
        val beneficsInMoonHora = moonHoraPlanets.count { VedicAstrologyUtils.isNaturalBenefic(it) }
        score += (beneficsInSunHora + beneficsInMoonHora) * 8.0

        // 2nd and 11th lords well placed
        secondLord?.let {
            if (it.sign == ZodiacSign.LEO || it.sign == ZodiacSign.CANCER) score += 10.0
        }
        eleventhLord?.let {
            if (it.sign == ZodiacSign.LEO || it.sign == ZodiacSign.CANCER) score += 10.0
        }

        // Jupiter in good hora is excellent
        if (Planet.JUPITER in sunHoraPlanets || Planet.JUPITER in moonHoraPlanets) {
            score += 15.0
        }

        return when {
            score >= 85 -> WealthPotential.EXCEPTIONAL
            score >= 70 -> WealthPotential.HIGH
            score >= 55 -> WealthPotential.MODERATE
            score >= 40 -> WealthPotential.AVERAGE
            else -> WealthPotential.LOW
        }
    }

    private fun calculateWealthTimingFromHora(
        chart: VedicChart,
        indicators: List<WealthIndicator>
    ): List<WealthTimingPeriod> {
        val periods = mutableListOf<WealthTimingPeriod>()
        val strongIndicators = indicators.filter { it.strength > 60 }

        strongIndicators.forEach { indicator ->
            periods.add(
                WealthTimingPeriod(
                    planet = indicator.planet,
                    type = indicator.type,
                    periodDescription = LocalizableString.Chain(
                        listOf(
                            indicator.planet.displayName,
                            LocalizableString.Raw(" Mahadasha/Antardasha")
                        )
                    ),
                    favorableForWealth = indicator.strength > 65,
                    wealthSources = indicator.sources
                )
            )
        }

        return periods
    }

    private fun generateHoraRecommendations(
        indicators: List<WealthIndicator>,
        potential: WealthPotential
    ): List<LocalizableString> {
        val recommendations = mutableListOf<LocalizableString>()

        val resId = when (potential) {
            WealthPotential.EXCEPTIONAL, WealthPotential.HIGH -> R.string.hora_rec_high_potential
            WealthPotential.MODERATE -> R.string.hora_rec_moderate_potential
            WealthPotential.AVERAGE, WealthPotential.LOW -> R.string.hora_rec_low_potential
        }
        recommendations.add(LocalizableString.Res(resId))

        indicators.filter { it.strength > 70 }.forEach { indicator ->
            recommendations.add(
                LocalizableString.ResWithArgs(
                    R.string.hora_rec_capitalize_strength,
                    listOf(indicator.planet.displayName, indicator.sources.firstOrNull() ?: LocalizableString.Raw("indicated area"))
                )
            )
        }

        return recommendations
    }

    private fun analyzeSiblings(
        drekkanaChart: DivisionalChartCalculator.DivisionalChartData,
        chart: VedicChart
    ): SiblingIndicators {
        val thirdHouseD3 = drekkanaChart.planetPositions.filter { it.house == 3 }
        val eleventhHouseD3 = drekkanaChart.planetPositions.filter { it.house == 11 }
        val marsD3 = drekkanaChart.planetPositions.find { it.planet == Planet.MARS }

        // Elder siblings from 11th house
        val elderSiblingCount = estimateSiblingCount(eleventhHouseD3)
        // Younger siblings from 3rd house
        val youngerSiblingCount = estimateSiblingCount(thirdHouseD3)

        val siblingRelationshipQuality = assessSiblingRelationship(thirdHouseD3, eleventhHouseD3, marsD3)

        return SiblingIndicators(
            estimatedYoungerSiblings = youngerSiblingCount,
            estimatedElderSiblings = elderSiblingCount,
            relationshipQuality = siblingRelationshipQuality,
            youngerSiblingPlanets = thirdHouseD3.map { it.planet },
            elderSiblingPlanets = eleventhHouseD3.map { it.planet },
            siblingWelfareIndicators = assessSiblingWelfare(drekkanaChart)
        )
    }

    private fun estimateSiblingCount(housePlanets: List<PlanetPosition>): IntRange {
        return when {
            housePlanets.isEmpty() -> 0..1
            housePlanets.size == 1 -> 1..2
            housePlanets.size == 2 -> 2..3
            else -> 3..5
        }
    }

    private fun assessSiblingRelationship(
        thirdHouse: List<PlanetPosition>,
        eleventhHouse: List<PlanetPosition>,
        mars: PlanetPosition?
    ): RelationshipQuality {
        val allPlanets = thirdHouse + eleventhHouse
        val beneficCount = allPlanets.count { VedicAstrologyUtils.isNaturalBenefic(it.planet) }
        val maleficCount = allPlanets.count { VedicAstrologyUtils.isNaturalMalefic(it.planet) }

        return when {
            beneficCount > maleficCount + 1 -> RelationshipQuality.EXCELLENT
            beneficCount > maleficCount -> RelationshipQuality.GOOD
            beneficCount == maleficCount -> RelationshipQuality.NEUTRAL
            maleficCount > beneficCount -> RelationshipQuality.CHALLENGING
            else -> RelationshipQuality.DIFFICULT
        }
    }

    private fun assessSiblingWelfare(chart: DivisionalChartCalculator.DivisionalChartData): List<LocalizableString> {
        val insights = mutableListOf<LocalizableString>()
        val thirdHousePlanets = chart.planetPositions.filter { it.house == 3 }

        if (Planet.JUPITER in thirdHousePlanets.map { it.planet }) {
            insights.add(LocalizableString.Res(R.string.sibling_welfare_jupiter))
        }
        if (Planet.SATURN in thirdHousePlanets.map { it.planet }) {
            insights.add(LocalizableString.Res(R.string.sibling_welfare_saturn))
        }
        if (Planet.MARS in thirdHousePlanets.map { it.planet }) {
            insights.add(LocalizableString.Res(R.string.sibling_welfare_mars))
        }

        return insights
    }

    private fun analyzeCourage(
        mars: PlanetPosition?,
        thirdLord: PlanetPosition?,
        chart: DivisionalChartCalculator.DivisionalChartData
    ): CourageAnalysis {
        var courageScore = 50.0

        mars?.let {
            // Mars in good dignity increases courage
            if (VedicAstrologyUtils.isExalted(it)) courageScore += 25.0
            if (VedicAstrologyUtils.isInOwnSign(it)) courageScore += 20.0
            if (VedicAstrologyUtils.isDebilitated(it)) courageScore -= 20.0

            // Mars in 3rd house is excellent for courage
            if (it.house == 3) courageScore += 15.0
        }

        thirdLord?.let {
            // Well-placed 3rd lord
            if (it.house in VedicAstrologyUtils.KENDRA_HOUSES) courageScore += 10.0
            if (it.house in VedicAstrologyUtils.TRIKONA_HOUSES) courageScore += 10.0
        }

        val courageLevel = when {
            courageScore >= 80 -> CourageLevel.EXCEPTIONAL
            courageScore >= 65 -> CourageLevel.HIGH
            courageScore >= 50 -> CourageLevel.MODERATE
            courageScore >= 35 -> CourageLevel.LOW
            else -> CourageLevel.VERY_LOW
        }

        return CourageAnalysis(
            overallCourageLevel = courageLevel,
            marsStrength = mars?.let { calculatePlanetStrengthInVarga(it) } ?: 0.0,
            initiativeAbility = assessInitiativeAbility(mars, thirdLord),
            physicalCourage = assessPhysicalCourage(mars),
            mentalCourage = assessMentalCourage(thirdLord, chart)
        )
    }

    private fun calculatePlanetStrengthInVarga(position: PlanetPosition): Double {
        var strength = 50.0
        if (VedicAstrologyUtils.isExalted(position)) strength += 30.0
        if (VedicAstrologyUtils.isInOwnSign(position)) strength += 20.0
        if (VedicAstrologyUtils.isDebilitated(position)) strength -= 25.0
        return strength.coerceIn(0.0, 100.0)
    }

    private fun assessInitiativeAbility(mars: PlanetPosition?, thirdLord: PlanetPosition?): LocalizableString {
        val marsStrong = mars?.let {
            VedicAstrologyUtils.isExalted(it) || VedicAstrologyUtils.isInOwnSign(it)
        } ?: false

        return if (marsStrong) {
            LocalizableString.Res(R.string.initiative_ability_strong)
        } else {
            LocalizableString.Res(R.string.initiative_ability_moderate)
        }
    }

    private fun assessPhysicalCourage(mars: PlanetPosition?): LocalizableString {
        return when {
            mars == null -> LocalizableString.Res(R.string.physical_courage_depends_on_factors)
            VedicAstrologyUtils.isExalted(mars) -> LocalizableString.Res(R.string.physical_courage_exceptional)
            VedicAstrologyUtils.isInOwnSign(mars) -> LocalizableString.Res(R.string.physical_courage_strong)
            VedicAstrologyUtils.isDebilitated(mars) -> LocalizableString.Res(R.string.physical_courage_needs_development)
            else -> LocalizableString.Res(R.string.physical_courage_adequate)
        }
    }

    private fun assessMentalCourage(thirdLord: PlanetPosition?, chart: DivisionalChartCalculator.DivisionalChartData): LocalizableString {
        val mercuryD3 = chart.planetPositions.find { it.planet == Planet.MERCURY }
        val jupiterD3 = chart.planetPositions.find { it.planet == Planet.JUPITER }

        return when {
            jupiterD3?.house == 3 -> LocalizableString.Res(R.string.mental_courage_wisdom)
            mercuryD3?.house == 3 -> LocalizableString.Res(R.string.mental_courage_quick_decision)
            thirdLord?.house in VedicAstrologyUtils.KENDRA_HOUSES -> LocalizableString.Res(R.string.mental_courage_stable)
            else -> LocalizableString.Res(R.string.mental_courage_develops)
        }
    }

    private fun analyzeCommunication(chart: DivisionalChartCalculator.DivisionalChartData, d1Chart: VedicChart): CommunicationAnalysis {
        val mercuryD3 = chart.planetPositions.find { it.planet == Planet.MERCURY }
        val thirdHousePlanets = chart.planetPositions.filter { it.house == 3 }

        val writingAbility = assessWritingAbility(mercuryD3, thirdHousePlanets)
        val speakingAbility = assessSpeakingAbility(mercuryD3, chart)
        val artisticTalents = assessArtisticTalents(chart)

        return CommunicationAnalysis(
            overallSkillLevel = calculateCommunicationLevel(mercuryD3, thirdHousePlanets),
            writingAbility = writingAbility,
            speakingAbility = speakingAbility,
            artisticTalents = artisticTalents,
            mercuryStrength = mercuryD3?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0
        )
    }

    private fun calculateCommunicationLevel(mercury: PlanetPosition?, thirdHouse: List<PlanetPosition>): LocalizableString {
        val mercuryStrong = mercury?.let {
            VedicAstrologyUtils.isExalted(it) || VedicAstrologyUtils.isInOwnSign(it)
        } ?: false

        return when {
            mercuryStrong && thirdHouse.any { it.planet == Planet.JUPITER } -> LocalizableString.Res(R.string.communication_level_exceptional)
            mercuryStrong -> LocalizableString.Res(R.string.communication_level_very_good)
            thirdHouse.any { VedicAstrologyUtils.isNaturalBenefic(it.planet) } -> LocalizableString.Res(R.string.communication_level_good)
            else -> LocalizableString.Res(R.string.communication_level_average)
        }
    }

    private fun assessWritingAbility(mercury: PlanetPosition?, thirdHouse: List<PlanetPosition>): LocalizableString {
        return when {
            mercury?.house == 3 && VedicAstrologyUtils.isInOwnSign(mercury) ->
                LocalizableString.Res(R.string.writing_ability_exceptional)
            thirdHouse.any { it.planet == Planet.JUPITER } ->
                LocalizableString.Res(R.string.writing_ability_strong)
            thirdHouse.any { it.planet == Planet.MERCURY } ->
                LocalizableString.Res(R.string.writing_ability_good)
            else -> LocalizableString.Res(R.string.writing_ability_average)
        }
    }

    private fun assessSpeakingAbility(mercury: PlanetPosition?, chart: DivisionalChartCalculator.DivisionalChartData): LocalizableString {
        val sunD3 = chart.planetPositions.find { it.planet == Planet.SUN }
        val jupiterD3 = chart.planetPositions.find { it.planet == Planet.JUPITER }

        return when {
            mercury != null && jupiterD3?.house == 2 -> LocalizableString.Res(R.string.speaking_ability_eloquent)
            sunD3?.house == 3 -> LocalizableString.Res(R.string.speaking_ability_authoritative)
            mercury?.house == 3 -> LocalizableString.Res(R.string.speaking_ability_articulate)
            else -> LocalizableString.Res(R.string.speaking_ability_normal)
        }
    }

    private fun assessArtisticTalents(chart: DivisionalChartCalculator.DivisionalChartData): List<LocalizableString> {
        val talents = mutableListOf<LocalizableString>()
        val venusD3 = chart.planetPositions.find { it.planet == Planet.VENUS }
        val moonD3 = chart.planetPositions.find { it.planet == Planet.MOON }

        venusD3?.let {
            if (it.house == 3 || it.house == 5) talents.add(LocalizableString.Res(R.string.artistic_talent_visual))
        }
        moonD3?.let {
            if (it.house == 3) talents.add(LocalizableString.Res(R.string.artistic_talent_music))
        }

        return talents
    }

    private fun analyzeShortJourneys(chart: DivisionalChartCalculator.DivisionalChartData): List<LocalizableString> {
        val indicators = mutableListOf<LocalizableString>()
        val thirdHousePlanets = chart.planetPositions.filter { it.house == 3 }

        if (thirdHousePlanets.any { it.planet == Planet.MOON }) {
            indicators.add(LocalizableString.Res(R.string.short_journey_frequent))
        }
        if (thirdHousePlanets.any { it.planet == Planet.MERCURY }) {
            indicators.add(LocalizableString.Res(R.string.short_journey_business))
        }
        if (thirdHousePlanets.any { it.planet == Planet.RAHU }) {
            indicators.add(LocalizableString.Res(R.string.short_journey_unusual))
        }

        return indicators
    }

    private fun generateDrekkanaRecommendations(
        courage: CourageAnalysis,
        siblings: SiblingIndicators
    ): List<LocalizableString> {
        val recommendations = mutableListOf<LocalizableString>()

        when (courage.overallCourageLevel) {
            CourageLevel.EXCEPTIONAL, CourageLevel.HIGH -> {
                recommendations.add(LocalizableString.Res(R.string.drekkana_rec_courage_high))
            }
            CourageLevel.LOW, CourageLevel.VERY_LOW -> {
                recommendations.add(LocalizableString.Res(R.string.drekkana_rec_courage_low))
            }
            else -> {
                recommendations.add(LocalizableString.Res(R.string.drekkana_rec_courage_moderate))
            }
        }

        when (siblings.relationshipQuality) {
            RelationshipQuality.CHALLENGING, RelationshipQuality.DIFFICULT -> {
                recommendations.add(LocalizableString.Res(R.string.drekkana_rec_siblings_challenging))
            }
            else -> {}
        }

        return recommendations
    }

    // Navamsa helper functions
    private fun calculateUpapada(chart: VedicChart): ZodiacSign {
        val secondLord = getHouseLord(chart, 2)
        val secondLordPosition = chart.planetPositions.find { it.planet == secondLord }
        val secondLordHouse = secondLordPosition?.house ?: 2

        // Upapada = As far from 2nd house as 2nd lord is from 2nd house
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val offset = (secondLordHouse - 2 + 12) % 12
        val upapadaHouse = (2 + offset - 1 + 12) % 12 + 1

        return ZodiacSign.entries[(ascendantSign.ordinal + upapadaHouse - 1) % 12]
    }

    private fun calculateDarakaraka(chart: VedicChart): Planet {
        val eligiblePlanets = chart.planetPositions
            .filter { it.planet !in listOf(Planet.RAHU, Planet.KETU, Planet.URANUS, Planet.NEPTUNE, Planet.PLUTO) }

        // Darakaraka is the planet with the lowest degree in its sign
        return eligiblePlanets
            .minByOrNull { it.longitude % 30.0 }
            ?.planet ?: Planet.VENUS
    }

    private fun analyzeMarriageTimingFactors(
        chart: VedicChart,
        navamsaChart: DivisionalChartCalculator.DivisionalChartData,
        venus: PlanetPosition?,
        jupiter: PlanetPosition?,
        seventhLord: PlanetPosition?,
        darakaraka: PlanetPosition?
    ): MarriageTimingFactors {
        val favorablePlanets = mutableListOf<Planet>()

        venus?.let {
            if (it.house in listOf(1, 5, 7, 9, 11)) favorablePlanets.add(Planet.VENUS)
        }
        jupiter?.let {
            if (it.house in listOf(1, 5, 7, 9, 11)) favorablePlanets.add(Planet.JUPITER)
        }
        seventhLord?.let {
            favorablePlanets.add(it.planet)
        }

        return MarriageTimingFactors(
            favorableDashaPlanets = favorablePlanets,
            venusNavamsaStrength = venus?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0,
            seventhLordStrength = seventhLord?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0,
            darakarakaStrength = darakaraka?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0,
            transitConsiderations = LocalizableString.Res(R.string.marriage_transit_considerations)
        )
    }

    private fun analyzeSpouseCharacteristics(
        seventhLord: PlanetPosition?,
        venus: PlanetPosition?,
        darakaraka: PlanetPosition?,
        upapadaLord: PlanetPosition?,
        navamsaChart: DivisionalChartCalculator.DivisionalChartData
    ): SpouseCharacteristics {
        val spouseNature = determineSpouseNature(seventhLord, venus)
        val spouseAppearance = determineSpouseAppearance(darakaraka, venus)
        val spouseProfession = estimateSpouseProfession(seventhLord, navamsaChart)

        return SpouseCharacteristics(
            generalNature = spouseNature,
            physicalTraits = spouseAppearance,
            probableProfessions = spouseProfession,
            familyBackground = determineSpouseFamilyBackground(upapadaLord)
        )
    }

    private fun determineSpouseNature(seventhLord: PlanetPosition?, venus: PlanetPosition?): LocalizableString {
        val dominantPlanet = seventhLord?.planet ?: venus?.planet ?: Planet.VENUS

        val resId = when (dominantPlanet) {
            Planet.SUN -> R.string.spouse_nature_sun
            Planet.MOON -> R.string.spouse_nature_moon
            Planet.MARS -> R.string.spouse_nature_mars
            Planet.MERCURY -> R.string.spouse_nature_mercury
            Planet.JUPITER -> R.string.spouse_nature_jupiter
            Planet.VENUS -> R.string.spouse_nature_venus
            Planet.SATURN -> R.string.spouse_nature_saturn
            Planet.RAHU -> R.string.spouse_nature_rahu
            Planet.KETU -> R.string.spouse_nature_ketu
            else -> R.string.spouse_nature_mixed
        }
        return LocalizableString.Res(resId)
    }

    private fun determineSpouseAppearance(darakaraka: PlanetPosition?, venus: PlanetPosition?): LocalizableString {
        val planet = darakaraka?.planet ?: venus?.planet ?: Planet.VENUS

        val resId = when (planet) {
            Planet.SUN -> R.string.spouse_appearance_sun
            Planet.MOON -> R.string.spouse_appearance_moon
            Planet.MARS -> R.string.spouse_appearance_mars
            Planet.MERCURY -> R.string.spouse_appearance_mercury
            Planet.JUPITER -> R.string.spouse_appearance_jupiter
            Planet.VENUS -> R.string.spouse_appearance_venus
            Planet.SATURN -> R.string.spouse_appearance_saturn
            else -> R.string.spouse_appearance_varies
        }
        return LocalizableString.Res(resId)
    }

    private fun estimateSpouseProfession(seventhLord: PlanetPosition?, chart: DivisionalChartCalculator.DivisionalChartData): List<LocalizableString> {
        val planet = seventhLord?.planet ?: return listOf(LocalizableString.Res(R.string.spouse_profession_various))

        val resId = when (planet) {
            Planet.SUN -> R.string.spouse_profession_sun
            Planet.MOON -> R.string.spouse_profession_moon
            Planet.MARS -> R.string.spouse_profession_mars
            Planet.MERCURY -> R.string.spouse_profession_mercury
            Planet.JUPITER -> R.string.spouse_profession_jupiter
            Planet.VENUS -> R.string.spouse_profession_venus
            Planet.SATURN -> R.string.spouse_profession_saturn
            else -> R.string.spouse_profession_other
        }
        return listOf(LocalizableString.Res(resId))
    }

    private fun determineSpouseFamilyBackground(upapadaLord: PlanetPosition?): LocalizableString {
        val resId = when (upapadaLord?.planet) {
            Planet.JUPITER -> R.string.spouse_family_jupiter
            Planet.VENUS -> R.string.spouse_family_venus
            Planet.SUN -> R.string.spouse_family_sun
            Planet.MOON -> R.string.spouse_family_moon
            Planet.SATURN -> R.string.spouse_family_saturn
            else -> R.string.spouse_family_varies
        }
        return LocalizableString.Res(resId)
    }

    private fun calculateSpouseDirection(seventhLord: PlanetPosition?, darakaraka: PlanetPosition?): LocalizableString {
        val sign = seventhLord?.sign ?: darakaraka?.sign ?: return LocalizableString.Res(R.string.spouse_direction_unclear)

        val resId = when (sign) {
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> R.string.spouse_direction_east
            ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> R.string.spouse_direction_south
            ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> R.string.spouse_direction_west
            ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES -> R.string.spouse_direction_north
        }
        return LocalizableString.Res(resId)
    }

    private fun analyzeMultipleMarriageFactors(
        chart: VedicChart,
        navamsaChart: DivisionalChartCalculator.DivisionalChartData
    ): MultipleMarriageIndicators {
        val indicators = mutableListOf<LocalizableString>()
        var risk = 0

        val seventhLordD1 = getHouseLord(chart, 7)
        val seventhLordPositionD1 = chart.planetPositions.find { it.planet == seventhLordD1 }
        val marsD7 = chart.planetPositions.find { it.planet == Planet.MARS }
        val venusD1 = chart.planetPositions.find { it.planet == Planet.VENUS }
        val seventhHousePlanets = chart.planetPositions.filter {
            ((it.planet.ordinal - ZodiacSign.fromLongitude(chart.ascendant).ordinal + 12) % 12) + 1 == 7
        }

        // Check for multiple marriage indicators
        if (seventhHousePlanets.size >= 2) {
            indicators.add(LocalizableString.Res(R.string.multiple_marriage_risk_multiple_planets))
            risk++
        }

        if (venusD1?.isRetrograde == true) {
            indicators.add(LocalizableString.Res(R.string.multiple_marriage_risk_retro_venus))
            risk++
        }

        if (seventhLordPositionD1?.house in VedicAstrologyUtils.DUSTHANA_HOUSES) {
            indicators.add(LocalizableString.Res(R.string.multiple_marriage_risk_7th_lord_dusthana))
            risk++
        }

        val hasRisk = risk >= 2

        return MultipleMarriageIndicators(
            hasStrongIndicators = hasRisk,
            riskFactors = indicators,
            mitigatingFactors = if (!hasRisk) listOf(LocalizableString.Res(R.string.multiple_marriage_mitigation_none)) else emptyList()
        )
    }

    private fun analyzeMarriageMuhurtaCompatibility(
        chart: VedicChart,
        navamsaChart: DivisionalChartCalculator.DivisionalChartData
    ): LocalizableString {
        val moonSign = chart.planetPositions.find { it.planet == Planet.MOON }?.sign
        return LocalizableString.ResWithArgs(R.string.marriage_muhurta_compatibility, listOf(moonSign?.displayName ?: LocalizableString.Raw("")))
    }

    private fun generateMarriageRecommendations(factors: MarriageTimingFactors): List<LocalizableString> {
        val recommendations = mutableListOf<LocalizableString>()

        if (factors.venusNavamsaStrength > 70) {
            recommendations.add(LocalizableString.Res(R.string.marriage_rec_venus_strong))
        }
        if (factors.venusNavamsaStrength < 50) {
            recommendations.add(LocalizableString.Res(R.string.marriage_rec_venus_weak))
        }

        val favorablePlanets = factors.favorableDashaPlanets.joinToString(", ") { it.name }
        recommendations.add(LocalizableString.ResWithArgs(R.string.marriage_rec_timing, listOf(favorablePlanets)))
        recommendations.add(factors.transitConsiderations)

        return recommendations
    }

    // Dashamsa helper functions
    private fun determineCareerType(dashamsaChart: DivisionalChartCalculator.DivisionalChartData, chart: VedicChart): List<CareerType> {
        val careerTypes = mutableListOf<CareerType>()
        val tenthHouseD10 = dashamsaChart.planetPositions.filter { it.house == 10 }
        val lagnaD10 = dashamsaChart.ascendantSign

        // Analyze 10th house planets in D10
        tenthHouseD10.forEach { position ->
            careerTypes.add(getCareerTypeFromPlanet(position.planet))
        }

        // Analyze D10 lagna lord
        val lagnaLord = lagnaD10.ruler
        val lagnaLordPosition = dashamsaChart.planetPositions.find { it.planet == lagnaLord }
        lagnaLordPosition?.let {
            careerTypes.add(getCareerTypeFromPlanet(it.planet))
        }

        return careerTypes.distinct()
    }

    private fun getCareerTypeFromPlanet(planet: Planet): CareerType {
        val (nameRes, industriesRes) = when (planet) {
            Planet.SUN -> R.string.career_type_admin to R.string.spouse_profession_sun
            Planet.MOON -> R.string.career_type_public_service to R.string.spouse_profession_moon
            Planet.MARS -> R.string.career_type_technical to R.string.spouse_profession_mars
            Planet.MERCURY -> R.string.career_type_communication to R.string.spouse_profession_mercury
            Planet.JUPITER -> R.string.career_type_advisory to R.string.spouse_profession_jupiter
            Planet.VENUS -> R.string.career_type_creative to R.string.spouse_profession_venus
            Planet.SATURN -> R.string.career_type_labor to R.string.spouse_profession_saturn
            Planet.RAHU -> R.string.career_type_unconventional to R.string.sun_hora_source_rahu
            Planet.KETU -> R.string.career_type_spiritual to R.string.sun_hora_source_ketu
            else -> R.string.career_type_general to R.string.spouse_profession_various
        }
        return CareerType(
            name = LocalizableString.Res(nameRes),
            industries = listOf(LocalizableString.Res(industriesRes)),
            suitability = LocalizableString.Raw("") // This can be improved
        )
    }

    private fun mapPlanetsToIndustries(chart: DivisionalChartCalculator.DivisionalChartData): Map<Planet, List<LocalizableString>> {
        val mapping = mutableMapOf<Planet, List<LocalizableString>>()

        chart.planetPositions.filter { it.house in listOf(1, 10) }.forEach { position ->
            mapping[position.planet] = getIndustriesForPlanet(position.planet)
        }

        return mapping
    }

    private fun getIndustriesForPlanet(planet: Planet): List<LocalizableString> {
        val resId = when (planet) {
            Planet.SUN -> R.string.spouse_profession_sun
            Planet.MOON -> R.string.spouse_profession_moon
            Planet.MARS -> R.string.spouse_profession_mars
            Planet.MERCURY -> R.string.spouse_profession_mercury
            Planet.JUPITER -> R.string.spouse_profession_jupiter
            Planet.VENUS -> R.string.spouse_profession_venus
            Planet.SATURN -> R.string.spouse_profession_saturn
            Planet.RAHU -> R.string.sun_hora_source_rahu
            Planet.KETU -> R.string.sun_hora_source_ketu
            else -> return emptyList()
        }
        return listOf(LocalizableString.Res(resId))
    }

    private fun analyzeGovernmentServicePotential(
        sun: PlanetPosition?,
        chart: DivisionalChartCalculator.DivisionalChartData
    ): GovernmentServiceAnalysis {
        var potential = 50.0

        sun?.let {
            if (it.house == 10) potential += 25.0
            if (it.house == 1) potential += 15.0
            if (VedicAstrologyUtils.isExalted(it)) potential += 20.0
            if (VedicAstrologyUtils.isInOwnSign(it)) potential += 15.0
        }

        val saturnD10 = chart.planetPositions.find { it.planet == Planet.SATURN }
        saturnD10?.let {
            if (it.house in listOf(1, 10)) potential += 10.0
        }

        val potentialRes = when {
            potential >= 80 -> R.string.government_service_potential_very_high
            potential >= 65 -> R.string.government_service_potential_high
            potential >= 50 -> R.string.government_service_potential_moderate
            else -> R.string.government_service_potential_low
        }

        return GovernmentServiceAnalysis(
            potential = LocalizableString.Res(potentialRes),
            favorableFactors = buildList {
                sun?.let {
                    if (it.house == 10) add(LocalizableString.Res(R.string.government_factor_sun_in_10th))
                    if (VedicAstrologyUtils.isExalted(it)) add(LocalizableString.Res(R.string.government_factor_exalted_sun))
                }
            },
            recommendedDepartments = if (potential > 60) {
                listOf(LocalizableString.Res(R.string.career_type_admin))
            } else emptyList()
        )
    }

    private fun analyzeBusinessVsService(
        dashamsaChart: DivisionalChartCalculator.DivisionalChartData,
        chart: VedicChart
    ): BusinessVsServiceAnalysis {
        var businessScore = 0
        var serviceScore = 0

        val mercuryD10 = dashamsaChart.planetPositions.find { it.planet == Planet.MERCURY }
        val saturnD10 = dashamsaChart.planetPositions.find { it.planet == Planet.SATURN }
        val rahuD10 = dashamsaChart.planetPositions.find { it.planet == Planet.RAHU }

        mercuryD10?.let {
            if (it.house in listOf(1, 7, 10)) businessScore += 2
        }
        rahuD10?.let {
            if (it.house in listOf(1, 7, 10)) businessScore += 2
        }
        saturnD10?.let {
            if (it.house in listOf(1, 6, 10)) serviceScore += 2
        }

        val tenthHousePlanets = dashamsaChart.planetPositions.filter { it.house == 10 }
        if (tenthHousePlanets.any { it.planet in listOf(Planet.MERCURY, Planet.VENUS, Planet.RAHU) }) {
            businessScore++
        }
        if (tenthHousePlanets.any { it.planet in listOf(Planet.SUN, Planet.SATURN, Planet.MOON) }) {
            serviceScore++
        }

        val recRes = when {
            businessScore > serviceScore + 1 -> R.string.business_vs_service_business
            serviceScore > businessScore + 1 -> R.string.business_vs_service_service
            else -> R.string.business_vs_service_both
        }

        return BusinessVsServiceAnalysis(
            businessAptitude = businessScore,
            serviceAptitude = serviceScore,
            recommendation = LocalizableString.Res(recRes),
            businessSectors = if (businessScore > serviceScore) {
                getIndustriesForPlanet(mercuryD10?.planet ?: Planet.MERCURY)
            } else emptyList()
        )
    }

    private fun calculateCareerPeakTiming(
        chart: VedicChart,
        dashamsaChart: DivisionalChartCalculator.DivisionalChartData
    ): List<CareerPeakPeriod> {
        val periods = mutableListOf<CareerPeakPeriod>()

        val tenthLordD1 = getHouseLord(chart, 10)
        val strongD10Planets = dashamsaChart.planetPositions.filter { position ->
            position.house in listOf(1, 10) ||
                    VedicAstrologyUtils.isExalted(position) ||
                    VedicAstrologyUtils.isInOwnSign(position)
        }

        periods.add(CareerPeakPeriod(
            planet = tenthLordD1,
            description = LocalizableString.Res(R.string.career_peak_10th_lord),
            significance = LocalizableString.Res(R.string.career_peak_significance_major)
        ))

        strongD10Planets.forEach { position ->
            periods.add(CareerPeakPeriod(
                planet = position.planet,
                description = LocalizableString.ResWithArgs(R.string.career_peak_planet_dasha, listOf(position.planet.displayName)),
                significance = LocalizableString.ResWithArgs(
                    R.string.career_peak_significance_opportunities,
                    listOf(getIndustriesForPlanet(position.planet).firstOrNull() ?: LocalizableString.Raw("related field"))
                )
            ))
        }

        return periods
    }

    private fun analyzeMultipleCareers(chart: DivisionalChartCalculator.DivisionalChartData): List<LocalizableString> {
        val indicators = mutableListOf<LocalizableString>()
        val tenthHousePlanets = chart.planetPositions.filter { it.house == 10 }

        if (tenthHousePlanets.size >= 2) {
            indicators.add(LocalizableString.Res(R.string.multiple_careers_multiple_planets))
        }

        val mercuryD10 = chart.planetPositions.find { it.planet == Planet.MERCURY }
        if (mercuryD10?.house == 10 || mercuryD10?.house == 1) {
            indicators.add(LocalizableString.Res(R.string.multiple_careers_mercury))
        }

        val rahuD10 = chart.planetPositions.find { it.planet == Planet.RAHU }
        if (rahuD10?.house in listOf(1, 10)) {
            indicators.add(LocalizableString.Res(R.string.multiple_careers_rahu))
        }

        return indicators
    }

    private fun analyzeProfessionalStrengths(chart: DivisionalChartCalculator.DivisionalChartData): List<LocalizableString> {
        val strengths = mutableListOf<LocalizableString>()
        val lagnaLord = chart.ascendantSign.ruler
        val lagnaLordPosition = chart.planetPositions.find { it.planet == lagnaLord }

        lagnaLordPosition?.let {
            if (it.house in VedicAstrologyUtils.KENDRA_HOUSES) {
                strengths.add(LocalizableString.Res(R.string.professional_strength_identity))
            }
            if (VedicAstrologyUtils.isExalted(it)) {
                strengths.add(LocalizableString.Res(R.string.professional_strength_exceptional_abilities))
            }
        }

        val tenthHousePlanets = chart.planetPositions.filter { it.house == 10 }
        tenthHousePlanets.forEach { position ->
            val strengthRes = when (position.planet) {
                Planet.SUN -> R.string.professional_strength_leadership
                Planet.MOON -> R.string.professional_strength_public_appeal
                Planet.MARS -> R.string.professional_strength_drive
                Planet.MERCURY -> R.string.professional_strength_communication
                Planet.JUPITER -> R.string.professional_strength_wisdom
                Planet.VENUS -> R.string.professional_strength_creativity
                Planet.SATURN -> R.string.professional_strength_persistence
                else -> null
            }
            strengthRes?.let { strengths.add(LocalizableString.Res(it)) }
        }

        return strengths
    }

    private fun generateCareerRecommendations(
        careerTypes: List<CareerType>,
        industryMappings: Map<Planet, List<LocalizableString>>
    ): List<LocalizableString> {
        val recommendations = mutableListOf<LocalizableString>()

        if (careerTypes.isNotEmpty()) {
            recommendations.add(LocalizableString.ResWithArgs(R.string.career_rec_primary_focus, listOf(careerTypes.firstOrNull()?.name ?: LocalizableString.Raw("Various options"))))
            careerTypes.firstOrNull()?.industries?.take(3)?.let { industries ->
                recommendations.add(LocalizableString.ResWithArgs(R.string.career_rec_top_industries, listOf(LocalizableString.Chain(industries))))
            }
        }

        industryMappings.entries.take(2).forEach { (planet, industries) ->
            recommendations.add(
                LocalizableString.ResWithArgs(
                    R.string.career_rec_planet_favors,
                    listOf(planet.displayName, industries.firstOrNull() ?: LocalizableString.Raw("general work"))
                )
            )
        }

        return recommendations
    }

    // Dwadasamsa helper functions
    private fun analyzeFatherIndicators(
        sun: PlanetPosition?,
        ninthLord: PlanetPosition?,
        chart: DivisionalChartCalculator.DivisionalChartData
    ): ParentAnalysis {
        var wellbeingScore = 50.0

        sun?.let {
            if (VedicAstrologyUtils.isExalted(it)) wellbeingScore += 20.0
            if (VedicAstrologyUtils.isInOwnSign(it)) wellbeingScore += 15.0
            if (VedicAstrologyUtils.isDebilitated(it)) wellbeingScore -= 20.0
            if (it.house in VedicAstrologyUtils.DUSTHANA_HOUSES) wellbeingScore -= 15.0
        }

        ninthLord?.let {
            if (it.house in VedicAstrologyUtils.KENDRA_HOUSES) wellbeingScore += 10.0
            if (it.house in VedicAstrologyUtils.TRIKONA_HOUSES) wellbeingScore += 10.0
        }

        val wellbeingRes = when {
            wellbeingScore >= 70 -> R.string.parental_wellbeing_good_father
            wellbeingScore >= 50 -> R.string.parental_wellbeing_moderate
            else -> R.string.parental_wellbeing_challenging_father
        }

        return ParentAnalysis(
            parent = LocalizableString.Res(R.string.parent_father),
            significatorStrength = sun?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0,
            houseLordStrength = ninthLord?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0,
            overallWellbeing = LocalizableString.Res(wellbeingRes),
            characteristics = determineFatherCharacteristics(sun, ninthLord),
            relationship = assessParentRelationship(sun)
        )
    }

    private fun analyzeMotherIndicators(
        moon: PlanetPosition?,
        fourthLord: PlanetPosition?,
        chart: DivisionalChartCalculator.DivisionalChartData
    ): ParentAnalysis {
        var wellbeingScore = 50.0

        moon?.let {
            if (VedicAstrologyUtils.isExalted(it)) wellbeingScore += 20.0
            if (VedicAstrologyUtils.isInOwnSign(it)) wellbeingScore += 15.0
            if (VedicAstrologyUtils.isDebilitated(it)) wellbeingScore -= 20.0
            if (it.house in VedicAstrologyUtils.DUSTHANA_HOUSES) wellbeingScore -= 15.0
        }

        fourthLord?.let {
            if (it.house in VedicAstrologyUtils.KENDRA_HOUSES) wellbeingScore += 10.0
            if (it.house in VedicAstrologyUtils.TRIKONA_HOUSES) wellbeingScore += 10.0
        }

        val wellbeingRes = when {
            wellbeingScore >= 70 -> R.string.parental_wellbeing_good_mother
            wellbeingScore >= 50 -> R.string.parental_wellbeing_moderate
            else -> R.string.parental_wellbeing_challenging_mother
        }

        return ParentAnalysis(
            parent = LocalizableString.Res(R.string.parent_mother),
            significatorStrength = moon?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0,
            houseLordStrength = fourthLord?.let { calculatePlanetStrengthInVarga(it) } ?: 50.0,
            overallWellbeing = LocalizableString.Res(wellbeingRes),
            characteristics = determineMotherCharacteristics(moon, fourthLord),
            relationship = assessParentRelationship(moon)
        )
    }

    private fun determineFatherCharacteristics(sun: PlanetPosition?, ninthLord: PlanetPosition?): LocalizableString {
        val resId = when {
            sun != null && VedicAstrologyUtils.isExalted(sun) -> R.string.father_char_authoritative
            sun?.house == 10 -> R.string.father_char_career_focused
            ninthLord?.planet == Planet.JUPITER -> R.string.father_char_religious
            else -> R.string.father_char_varies
        }
        return LocalizableString.Res(resId)
    }

    private fun determineMotherCharacteristics(moon: PlanetPosition?, fourthLord: PlanetPosition?): LocalizableString {
        val resId = when {
            moon != null && VedicAstrologyUtils.isExalted(moon) -> R.string.mother_char_nurturing
            moon?.house == 4 -> R.string.mother_char_home_oriented
            fourthLord?.planet == Planet.VENUS -> R.string.mother_char_artistic
            else -> R.string.mother_char_varies
        }
        return LocalizableString.Res(resId)
    }

    private fun assessParentRelationship(significator: PlanetPosition?): LocalizableString {
        val resId = when {
            significator == null -> R.string.parent_relationship_depends
            significator.house in VedicAstrologyUtils.KENDRA_HOUSES -> R.string.parent_relationship_strong
            significator.house in VedicAstrologyUtils.TRIKONA_HOUSES -> R.string.parent_relationship_harmonious
            significator.house in VedicAstrologyUtils.DUSTHANA_HOUSES -> R.string.parent_relationship_challenging
            else -> R.string.parent_relationship_moderate
        }
        return LocalizableString.Res(resId)
    }

    private fun analyzeInheritance(
        dwadasamsaChart: DivisionalChartCalculator.DivisionalChartData,
        chart: VedicChart
    ): InheritanceAnalysis {
        val secondHouseD12 = dwadasamsaChart.planetPositions.filter { it.house == 2 }
        val fourthHouseD12 = dwadasamsaChart.planetPositions.filter { it.house == 4 }
        val eighthHouseD12 = dwadasamsaChart.planetPositions.filter { it.house == 8 }

        var inheritanceScore = 50.0

        if (Planet.JUPITER in secondHouseD12.map { it.planet }) inheritanceScore += 20.0
        if (Planet.VENUS in fourthHouseD12.map { it.planet }) inheritanceScore += 15.0
        if (fourthHouseD12.any { VedicAstrologyUtils.isNaturalBenefic(it.planet) }) inheritanceScore += 10.0

        val potentialRes = when {
            inheritanceScore >= 70 -> R.string.inheritance_potential_good
            inheritanceScore >= 50 -> R.string.inheritance_potential_moderate
            else -> R.string.inheritance_potential_limited
        }

        return InheritanceAnalysis(
            potential = LocalizableString.Res(potentialRes),
            sources = buildList {
                if (secondHouseD12.isNotEmpty()) add(LocalizableString.Res(R.string.inheritance_source_family))
                if (fourthHouseD12.isNotEmpty()) add(LocalizableString.Res(R.string.inheritance_source_property))
                if (eighthHouseD12.any { VedicAstrologyUtils.isNaturalBenefic(it.planet) }) add(
                    LocalizableString.Res(
                        R.string.inheritance_source_unexpected
                    )
                )
            },
            timing = LocalizableString.Res(R.string.inheritance_timing)
        )
    }

    private fun analyzeAncestralProperty(chart: DivisionalChartCalculator.DivisionalChartData): List<LocalizableString> {
        val indicators = mutableListOf<LocalizableString>()
        val fourthHousePlanets = chart.planetPositions.filter { it.house == 4 }

        if (Planet.SATURN in fourthHousePlanets.map { it.planet }) {
            indicators.add(LocalizableString.Res(R.string.ancestral_property_old))
        }
        if (Planet.MARS in fourthHousePlanets.map { it.planet }) {
            indicators.add(LocalizableString.Res(R.string.ancestral_property_land))
        }
        if (Planet.JUPITER in fourthHousePlanets.map { it.planet }) {
            indicators.add(LocalizableString.Res(R.string.ancestral_property_religious))
        }

        return indicators
    }

    private fun analyzeFamilyLineage(chart: DivisionalChartCalculator.DivisionalChartData): List<LocalizableString> {
        val insights = mutableListOf<LocalizableString>()
        val lagnaLord = chart.ascendantSign.ruler
        val lagnaLordPosition = chart.planetPositions.find { it.planet == lagnaLord }

        lagnaLordPosition?.let {
            val resId = when {
                VedicAstrologyUtils.isExalted(it) -> R.string.family_lineage_noble
                it.house in listOf(9, 10) -> R.string.family_lineage_dharma
                it.house in listOf(5, 11) -> R.string.family_lineage_creative
                else -> R.string.family_lineage_depends
            }
            insights.add(LocalizableString.Res(resId))
        }

        return insights
    }

    private fun analyzeParentalLongevity(
        dwadasamsaChart: DivisionalChartCalculator.DivisionalChartData,
        chart: VedicChart
    ): ParentalLongevityIndicators {
        val sunD12 = dwadasamsaChart.planetPositions.find { it.planet == Planet.SUN }
        val moonD12 = dwadasamsaChart.planetPositions.find { it.planet == Planet.MOON }

        val fatherLongevityRes = when {
            sunD12 != null && VedicAstrologyUtils.isExalted(sunD12) -> R.string.parental_longevity_long
            sunD12?.house in VedicAstrologyUtils.DUSTHANA_HOUSES -> R.string.parental_longevity_attention_needed
            else -> R.string.parental_longevity_moderate
        }

        val motherLongevityRes = when {
            moonD12 != null && VedicAstrologyUtils.isExalted(moonD12) -> R.string.parental_longevity_long
            moonD12?.house in VedicAstrologyUtils.DUSTHANA_HOUSES -> R.string.parental_longevity_attention_needed
            else -> R.string.parental_longevity_moderate
        }

        return ParentalLongevityIndicators(
            fatherLongevity = LocalizableString.Res(fatherLongevityRes),
            motherLongevity = LocalizableString.Res(motherLongevityRes),
            healthConcerns = buildList {
                if (sunD12?.house in VedicAstrologyUtils.DUSTHANA_HOUSES) add(LocalizableString.Res(R.string.parental_health_father))
                if (moonD12?.house in VedicAstrologyUtils.DUSTHANA_HOUSES) add(LocalizableString.Res(R.string.parental_health_mother))
            }
        )
    }

    private fun generateParentalRecommendations(
        fatherAnalysis: ParentAnalysis,
        motherAnalysis: ParentAnalysis
    ): List<LocalizableString> {
        val recommendations = mutableListOf<LocalizableString>()

        if (fatherAnalysis.significatorStrength < 50) {
            recommendations.add(LocalizableString.Res(R.string.parental_rec_father))
        }

        if (motherAnalysis.significatorStrength < 50) {
            recommendations.add(LocalizableString.Res(R.string.parental_rec_mother))
        }

        recommendations.add(LocalizableString.Res(R.string.parental_rec_ancestral))

        return recommendations
    }
}

// Data classes for analysis results
data class HoraAnalysis(
    val sunHoraPlanets: List<Planet>,
    val moonHoraPlanets: List<Planet>,
    val wealthIndicators: List<WealthIndicator>,
    val overallWealthPotential: WealthPotential,
    val secondLordHoraSign: ZodiacSign?,
    val eleventhLordHoraSign: ZodiacSign?,
    val wealthTimingPeriods: List<WealthTimingPeriod>,
    val recommendations: List<LocalizableString>
)

data class WealthIndicator(
    val planet: Planet,
    val type: WealthType,
    val strength: Double,
    val sources: List<LocalizableString>
)

enum class WealthType { SELF_EARNED, INHERITED_LIQUID }
enum class WealthPotential { EXCEPTIONAL, HIGH, MODERATE, AVERAGE, LOW }

data class WealthTimingPeriod(
    val planet: Planet,
    val type: WealthType,
    val periodDescription: LocalizableString,
    val favorableForWealth: Boolean,
    val wealthSources: List<LocalizableString>
)

data class DrekkanaAnalysis(
    val marsInDrekkana: PlanetPosition?,
    val thirdLordPosition: PlanetPosition?,
    val siblingIndicators: SiblingIndicators,
    val courageAnalysis: CourageAnalysis,
    val communicationSkills: CommunicationAnalysis,
    val thirdHousePlanets: List<PlanetPosition>,
    val eleventhHousePlanets: List<PlanetPosition>,
    val shortJourneyIndicators: List<LocalizableString>,
    val recommendations: List<LocalizableString>
)

data class SiblingIndicators(
    val estimatedYoungerSiblings: IntRange,
    val estimatedElderSiblings: IntRange,
    val relationshipQuality: RelationshipQuality,
    val youngerSiblingPlanets: List<Planet>,
    val elderSiblingPlanets: List<Planet>,
    val siblingWelfareIndicators: List<LocalizableString>
)

enum class RelationshipQuality { EXCELLENT, GOOD, NEUTRAL, CHALLENGING, DIFFICULT }

data class CourageAnalysis(
    val overallCourageLevel: CourageLevel,
    val marsStrength: Double,
    val initiativeAbility: LocalizableString,
    val physicalCourage: LocalizableString,
    val mentalCourage: LocalizableString
)

enum class CourageLevel { EXCEPTIONAL, HIGH, MODERATE, LOW, VERY_LOW }

data class CommunicationAnalysis(
    val overallSkillLevel: LocalizableString,
    val writingAbility: LocalizableString,
    val speakingAbility: LocalizableString,
    val artisticTalents: List<LocalizableString>,
    val mercuryStrength: Double
)

data class NavamsaMarriageAnalysis(
    val venusInNavamsa: PlanetPosition?,
    val jupiterInNavamsa: PlanetPosition?,
    val seventhLordNavamsa: PlanetPosition?,
    val navamsaLagnaLordPosition: PlanetPosition?,
    val upapadaSign: ZodiacSign,
    val upapadaLordNavamsa: PlanetPosition?,
    val darakaraka: Planet,
    val darakarakaNavamsa: PlanetPosition?,
    val marriageTimingFactors: MarriageTimingFactors,
    val spouseCharacteristics: SpouseCharacteristics,
    val spouseDirection: LocalizableString,
    val multipleMarriageIndicators: MultipleMarriageIndicators,
    val marriageMuhurtaCompatibility: LocalizableString,
    val recommendations: List<LocalizableString>
)

data class MarriageTimingFactors(
    val favorableDashaPlanets: List<Planet>,
    val venusNavamsaStrength: Double,
    val seventhLordStrength: Double,
    val darakarakaStrength: Double,
    val transitConsiderations: LocalizableString
)

data class SpouseCharacteristics(
    val generalNature: LocalizableString,
    val physicalTraits: LocalizableString,
    val probableProfessions: List<LocalizableString>,
    val familyBackground: LocalizableString
)

data class MultipleMarriageIndicators(
    val hasStrongIndicators: Boolean,
    val riskFactors: List<LocalizableString>,
    val mitigatingFactors: List<LocalizableString>
)

data class DashamsaAnalysis(
    val tenthLordInDashamsa: PlanetPosition?,
    val sunInDashamsa: PlanetPosition?,
    val saturnInDashamsa: PlanetPosition?,
    val mercuryInDashamsa: PlanetPosition?,
    val dashamsaLagna: ZodiacSign,
    val careerTypes: List<CareerType>,
    val industryMappings: Map<Planet, List<LocalizableString>>,
    val governmentServicePotential: GovernmentServiceAnalysis,
    val businessVsServiceAptitude: BusinessVsServiceAnalysis,
    val careerPeakPeriods: List<CareerPeakPeriod>,
    val multipleCareerIndicators: List<LocalizableString>,
    val professionalStrengths: List<LocalizableString>,
    val recommendations: List<LocalizableString>
)

data class CareerType(
    val name: LocalizableString,
    val industries: List<LocalizableString>,
    val suitability: LocalizableString
)

data class GovernmentServiceAnalysis(
    val potential: LocalizableString,
    val favorableFactors: List<LocalizableString>,
    val recommendedDepartments: List<LocalizableString>
)

data class BusinessVsServiceAnalysis(
    val businessAptitude: Int,
    val serviceAptitude: Int,
    val recommendation: LocalizableString,
    val businessSectors: List<LocalizableString>
)

data class CareerPeakPeriod(
    val planet: Planet,
    val description: LocalizableString,
    val significance: LocalizableString
)

data class DwadasamsaAnalysis(
    val sunInDwadasamsa: PlanetPosition?,
    val moonInDwadasamsa: PlanetPosition?,
    val ninthLordPosition: PlanetPosition?,
    val fourthLordPosition: PlanetPosition?,
    val fatherAnalysis: ParentAnalysis,
    val motherAnalysis: ParentAnalysis,
    val inheritanceIndicators: InheritanceAnalysis,
    val ancestralPropertyIndicators: List<LocalizableString>,
    val familyLineageInsights: List<LocalizableString>,
    val parentalLongevityIndicators: ParentalLongevityIndicators,
    val recommendations: List<LocalizableString>
)

data class ParentAnalysis(
    val parent: LocalizableString,
    val significatorStrength: Double,
    val houseLordStrength: Double,
    val overallWellbeing: LocalizableString,
    val characteristics: LocalizableString,
    val relationship: LocalizableString
)

data class InheritanceAnalysis(
    val potential: LocalizableString,
    val sources: List<LocalizableString>,
    val timing: LocalizableString
)

data class ParentalLongevityIndicators(
    val fatherLongevity: LocalizableString,
    val motherLongevity: LocalizableString,
    val healthConcerns: List<LocalizableString>
)
