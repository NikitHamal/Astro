package com.astro.storm.ephemeris

import android.content.Context
import android.util.Log
import com.astro.storm.core.common.*
import com.astro.storm.core.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.DateTimeException
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Singleton
class HoroscopeCalculator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ephemerisEngine: SwissEphemerisEngine
) : AutoCloseable {

    private val transitCache = LRUCache<TransitCacheKey, VedicChart>(MAX_TRANSIT_CACHE_SIZE)
    private val dailyHoroscopeCache = LRUCache<DailyHoroscopeCacheKey, DailyHoroscope>(MAX_HOROSCOPE_CACHE_SIZE)
    private val natalDataCache = LRUCache<String, NatalChartCachedData>(MAX_NATAL_CACHE_SIZE)

    private val isClosed = AtomicBoolean(false)

    private data class TransitCacheKey(
        val date: LocalDate,
        val latitudeInt: Int,
        val longitudeInt: Int,
        val timezone: String
    ) {
        companion object {
            fun from(date: LocalDate, latitude: Double, longitude: Double, timezone: String): TransitCacheKey {
                return TransitCacheKey(
                    date = date,
                    latitudeInt = (latitude * 1000).toInt(),
                    longitudeInt = (longitude * 1000).toInt(),
                    timezone = timezone
                )
            }
        }
    }

    private data class DailyHoroscopeCacheKey(
        val chartId: String,
        val date: LocalDate,
        val language: Language
    )

    private data class NatalChartCachedData(
        val planetMap: Map<Planet, PlanetPosition>,
        val moonSign: ZodiacSign,
        val moonHouse: Int,
        val ascendantSign: ZodiacSign,
        val ashtakavarga: AshtakavargaCalculator.AshtakavargaAnalysis?,
        val dashaTimeline: DashaCalculator.DashaTimeline
    )

    data class DailyHoroscope(
        val date: LocalDate,
        val theme: String, // Keeping for compatibility
        val themeKey: StringKey,
        val themeDescription: String, // Keeping for compatibility
        val themeDescriptionKey: StringKey,
        val overallEnergy: Int,
        val moonSign: ZodiacSign,
        val moonNakshatra: Nakshatra,
        val activeDasha: String,
        val lifeAreas: List<LifeAreaPrediction>,
        val luckyElements: LuckyElements,
        val planetaryInfluences: List<PlanetaryInfluence>,
        val recommendations: List<StringKey>,
        val cautions: List<StringKey>,
        val affirmation: String, // Keeping for compatibility
        val affirmationKey: StringKey
    )

    data class LifeAreaPrediction(
        val area: LifeArea,
        val rating: Int,
        val prediction: String, // These are generated, difficult to use keys without major changes
        val advice: String      // These too
    )

    enum class LifeArea(val displayName: String, val displayNameKey: StringKey, val houses: List<Int>) {
        CAREER("Career", StringKey.LIFE_AREA_CAREER, listOf(10, 6, 2)),
        LOVE("Love & Relationships", StringKey.LIFE_AREA_LOVE, listOf(7, 5, 11)),
        HEALTH("Health & Vitality", StringKey.LIFE_AREA_HEALTH, listOf(1, 6, 8)),
        FINANCE("Finance & Wealth", StringKey.LIFE_AREA_FINANCE, listOf(2, 11, 5)),
        FAMILY("Family & Home", StringKey.LIFE_AREA_FAMILY, listOf(4, 2, 12)),
        SPIRITUALITY("Spiritual Growth", StringKey.LIFE_AREA_SPIRITUALITY, listOf(9, 12, 5))
    }

    data class LuckyElements(
        val number: Int,
        val color: String,
        val direction: String,
        val time: String,
        val gemstone: String
    )

    data class PlanetaryInfluence(
        val planet: Planet,
        val influence: String, // Dynamic, keep as String for now
        val strength: Int,
        val isPositive: Boolean
    )

    data class WeeklyHoroscope(
        val startDate: LocalDate,
        val endDate: LocalDate,
        val weeklyTheme: String, // Keeping for compatibility
        val weeklyThemeKey: StringKey,
        val weeklyOverview: String,
        val keyDates: List<KeyDate>,
        val weeklyPredictions: Map<LifeArea, String>,
        val dailyHighlights: List<DailyHighlight>,
        val weeklyAdvice: String
    )

    data class KeyDate(
        val date: LocalDate,
        val event: String,
        val significance: String,
        val isPositive: Boolean
    )

    data class DailyHighlight(
        val date: LocalDate,
        val dayOfWeek: java.time.DayOfWeek,
        val energy: Int,
        val focusKey: StringKey,
        val brief: String
    )

    sealed class HoroscopeResult<out T> {
        data class Success<T>(val data: T) : HoroscopeResult<T>()
        data class Error(val message: String, val cause: Throwable? = null) : HoroscopeResult<Nothing>()
    }

    private data class VedhaInfo(
        val hasVedha: Boolean,
        val obstructingPlanet: Planet? = null,
        val obstructingHouse: Int? = null
    )

    private fun ensureNotClosed() {
        if (isClosed.get()) {
            throw IllegalStateException("HoroscopeCalculator has been closed")
        }
    }

    private fun getChartId(chart: VedicChart): String {
        val birthData = chart.birthData
        return "${birthData.name}_${birthData.dateTime}_${birthData.latitude}_${birthData.longitude}"
    }

    private fun getOrComputeNatalData(chart: VedicChart): NatalChartCachedData {
        val chartId = getChartId(chart)
        
        natalDataCache[chartId]?.let { return it }

        val planetMap = chart.planetPositions.associateBy { it.planet }
        val moonPosition = planetMap[Planet.MOON]
        val moonSign = moonPosition?.sign ?: ZodiacSign.ARIES
        val moonHouse = moonPosition?.house ?: 1
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)

        val ashtakavarga = try {
            AshtakavargaCalculator.calculateAshtakavarga(chart)
        } catch (e: Exception) {
            Log.w(TAG, "Ashtakavarga calculation failed", e)
            null
        }

        val dashaTimeline = DashaCalculator.calculateDashaTimeline(chart)

        return NatalChartCachedData(
            planetMap = planetMap,
            moonSign = moonSign,
            moonHouse = moonHouse,
            ascendantSign = ascendantSign,
            ashtakavarga = ashtakavarga,
            dashaTimeline = dashaTimeline
        ).also { natalDataCache[chartId] = it }
    }

    fun calculateDailyHoroscope(chart: VedicChart, date: LocalDate? = null, language: Language = Language.ENGLISH): DailyHoroscope {
        ensureNotClosed()
        val effectiveDate = date ?: LocalDate.now(resolveZoneId(chart.birthData.timezone))

        val chartId = getChartId(chart)
        val cacheKey = DailyHoroscopeCacheKey(chartId, effectiveDate, language)
        dailyHoroscopeCache[cacheKey]?.let { return it }

        val natalData = getOrComputeNatalData(chart)
        val dashaAtDate = natalData.dashaTimeline.getDashaAtDate(effectiveDate.atTime(LocalTime.NOON))
        val transitChart = getOrCalculateTransitChart(chart.birthData, effectiveDate)
        val transitPlanetMap = transitChart.planetPositions.associateBy { it.planet }

        val transitMoon = transitPlanetMap[Planet.MOON]
        val moonSign = transitMoon?.sign ?: natalData.moonSign
        val moonNakshatra = transitMoon?.nakshatra ?: Nakshatra.ASHWINI

        val planetaryInfluences = analyzePlanetaryInfluences(
            natalData = natalData,
            transitPlanetMap = transitPlanetMap,
            language = language
        )

        val lifeAreaPredictions = calculateLifeAreaPredictions(
            natalData = natalData,
            transitPlanetMap = transitPlanetMap,
            dashaAtDate = dashaAtDate,
            date = effectiveDate,
            language = language
        )

        val overallEnergy = calculateOverallEnergy(planetaryInfluences, lifeAreaPredictions, dashaAtDate.mahadasha?.planet)
        val (themeKey, themeDescriptionKey) = calculateDailyTheme(transitPlanetMap, dashaAtDate.mahadasha?.planet)
        val luckyElements = calculateLuckyElements(natalData, transitPlanetMap, effectiveDate, language)
        val activeDasha = formatActiveDasha(dashaAtDate, language)
        val recommendations = generateRecommendations(dashaAtDate, lifeAreaPredictions, transitPlanetMap)
        val cautions = generateCautions(transitPlanetMap, planetaryInfluences)
        val affirmationKey = generateAffirmationKey(dashaAtDate.mahadasha?.planet)

        return DailyHoroscope(
            date = effectiveDate,
            theme = StringResources.get(themeKey, language),
            themeKey = themeKey,
            themeDescription = StringResources.get(themeDescriptionKey, language),
            themeDescriptionKey = themeDescriptionKey,
            overallEnergy = overallEnergy,
            moonSign = moonSign,
            moonNakshatra = moonNakshatra,
            activeDasha = activeDasha,
            lifeAreas = lifeAreaPredictions,
            luckyElements = luckyElements,
            planetaryInfluences = planetaryInfluences,
            recommendations = recommendations,
            cautions = cautions,
            affirmation = StringResources.get(affirmationKey, language),
            affirmationKey = affirmationKey
        ).also { dailyHoroscopeCache[cacheKey] = it }
    }

    fun calculateDailyHoroscopeSafe(chart: VedicChart, date: LocalDate? = null, language: Language = Language.ENGLISH): HoroscopeResult<DailyHoroscope> {
        val effectiveDate = date ?: LocalDate.now(resolveZoneId(chart.birthData.timezone))
        return try {
            HoroscopeResult.Success(calculateDailyHoroscope(chart, effectiveDate, language))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to calculate daily horoscope for $effectiveDate", e)
            HoroscopeResult.Error("Unable to calculate horoscope for $effectiveDate: ${e.message}", e)
        }
    }

    fun calculateWeeklyHoroscope(chart: VedicChart, startDate: LocalDate? = null, language: Language = Language.ENGLISH): WeeklyHoroscope {
        ensureNotClosed()
        val effectiveStartDate = startDate ?: LocalDate.now(resolveZoneId(chart.birthData.timezone))

        val endDate = effectiveStartDate.plusDays(6)
        val natalData = getOrComputeNatalData(chart)
        val startDasha = natalData.dashaTimeline.getDashaAtDate(effectiveStartDate.atTime(LocalTime.NOON))
        val endDasha = natalData.dashaTimeline.getDashaAtDate(endDate.atTime(LocalTime.NOON))
        val hasDashaShift = startDasha.mahadasha?.planet != endDasha.mahadasha?.planet ||
                startDasha.antardasha?.planet != endDasha.antardasha?.planet

        val dailyHoroscopes = (0 until 7).mapNotNull { dayOffset ->
            val date = effectiveStartDate.plusDays(dayOffset.toLong())
            try {
                calculateDailyHoroscope(chart, date, language)
            } catch (e: Exception) {
                Log.w(TAG, "Failed to calculate horoscope for $date", e)
                null
            }
        }

        val dailyHighlights = if (dailyHoroscopes.isNotEmpty()) {
            dailyHoroscopes.map { horoscope ->
                DailyHighlight(
                    date = horoscope.date,
                    dayOfWeek = horoscope.date.dayOfWeek,
                    energy = horoscope.overallEnergy,
                    focusKey = horoscope.themeKey,
                    brief = StringResources.get(horoscope.themeDescriptionKey, language).take(100).let {
                        if (it.length >= 100) "$it..." else it
                    }
                )
            }
        } else {
            (0 until 7).map { dayOffset ->
                val date = effectiveStartDate.plusDays(dayOffset.toLong())
                DailyHighlight(
                    date = date,
                    dayOfWeek = date.dayOfWeek,
                    energy = 5,
                    focusKey = StringKey.HOROSCOPE_BALANCE,
                    brief = StringResources.get(StringKey.HOROSCOPE_STEADY_ENERGY, language)
                )
            }
        }

        val keyDates = calculateKeyDates(effectiveStartDate, endDate, language)
        val weeklyPredictions = calculateWeeklyPredictions(dailyHoroscopes, natalData.dashaTimeline, language)
        val (weeklyThemeKey, weeklyOverview) = calculateWeeklyTheme(
            dashaTimeline = natalData.dashaTimeline,
            dailyHighlights = dailyHighlights,
            hasDashaShift = hasDashaShift,
            language = language
        )
        val weeklyAdvice = generateWeeklyAdvice(natalData.dashaTimeline, keyDates, language)

        return WeeklyHoroscope(
            startDate = effectiveStartDate,
            endDate = endDate,
            weeklyTheme = StringResources.get(weeklyThemeKey, language),
            weeklyThemeKey = weeklyThemeKey,
            weeklyOverview = weeklyOverview,
            keyDates = keyDates,
            weeklyPredictions = weeklyPredictions,
            dailyHighlights = dailyHighlights,
            weeklyAdvice = weeklyAdvice
        )
    }

    fun calculateWeeklyHoroscopeSafe(chart: VedicChart, startDate: LocalDate? = null, language: Language = Language.ENGLISH): HoroscopeResult<WeeklyHoroscope> {
        val effectiveStartDate = startDate ?: LocalDate.now(resolveZoneId(chart.birthData.timezone))
        return try {
            HoroscopeResult.Success(calculateWeeklyHoroscope(chart, effectiveStartDate, language))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to calculate weekly horoscope starting $effectiveStartDate", e)
            HoroscopeResult.Error("Unable to calculate weekly horoscope: ${e.message}", e)
        }
    }

    private fun resolveZoneId(timezone: String?): ZoneId {
        if (timezone.isNullOrBlank()) return ZoneOffset.UTC
        return try {
            ZoneId.of(timezone.trim())
        } catch (_: DateTimeException) {
            val normalized = timezone.trim()
                .replace("UTC", "", ignoreCase = true)
                .replace("GMT", "", ignoreCase = true)
                .trim()
            if (normalized.isNotEmpty()) {
                runCatching { ZoneId.of("UTC$normalized") }.getOrElse { ZoneOffset.UTC }
            } else {
                ZoneOffset.UTC
            }
        }
    }

    private fun getOrCalculateTransitChart(birthData: BirthData, date: LocalDate): VedicChart {
        val cacheKey = TransitCacheKey.from(date, birthData.latitude, birthData.longitude, birthData.timezone)

        transitCache[cacheKey]?.let { return it }

        val transitDateTime = LocalDateTime.of(date, LocalTime.of(6, 0))
        val transitBirthData = BirthData(
            name = "Transit",
            dateTime = transitDateTime,
            latitude = birthData.latitude,
            longitude = birthData.longitude,
            timezone = birthData.timezone,
            location = birthData.location
        )

        return try {
            ephemerisEngine.calculateVedicChart(transitBirthData).also {
                transitCache[cacheKey] = it
            }
        } catch (e: Exception) {
            Log.e(TAG, "Transit calculation failed for $date", e)
            throw HoroscopeCalculationException(
                "Unable to calculate planetary positions for $date. This may be due to ephemeris data limitations.",
                e
            )
        }
    }

    private fun formatActiveDasha(periodInfo: DashaCalculator.DashaPeriodInfo, language: Language): String {
        val md = periodInfo.mahadasha ?: return StringResources.get(StringKey.DASHA_CALCULATING, language)
        val ad = periodInfo.antardasha
        return if (ad != null) {
            "${md.planet.getLocalizedName(language)}-${ad.planet.getLocalizedName(language)}"
        } else {
            md.planet.getLocalizedName(language)
        }
    }

    private fun analyzePlanetaryInfluences(
        natalData: NatalChartCachedData,
        transitPlanetMap: Map<Planet, PlanetPosition>,
        language: Language
    ): List<PlanetaryInfluence> {
        return Planet.MAIN_PLANETS.mapNotNull { planet ->
            val transitPos = transitPlanetMap[planet] ?: return@mapNotNull null
            val natalPos = natalData.planetMap[planet]
            val houseFromMoon = calculateHouseFromMoon(transitPos.sign, natalData.moonSign)

            val vedhaInfo = checkGocharaVedha(planet, houseFromMoon, transitPlanetMap, natalData.moonSign)
            val ashtakavargaScore = getAshtakavargaTransitScore(planet, transitPos.sign, natalData.ashtakavarga)

            val (influence, strength, isPositive) = analyzeGocharaEffect(
                planet = planet,
                houseFromMoon = houseFromMoon,
                isRetrograde = transitPos.isRetrograde,
                natalPosition = natalPos,
                vedhaInfo = vedhaInfo,
                ashtakavargaScore = ashtakavargaScore,
                transitSign = transitPos.sign,
                language = language
            )

            PlanetaryInfluence(
                planet = planet,
                influence = influence,
                strength = strength,
                isPositive = isPositive
            )
        }.sortedByDescending { it.strength }
    }

    private fun checkGocharaVedha(
        planet: Planet,
        houseFromMoon: Int,
        transitPlanetMap: Map<Planet, PlanetPosition>,
        natalMoonSign: ZodiacSign
    ): VedhaInfo {
        val vedhaPairs = GOCHARA_VEDHA_PAIRS[planet] ?: return VedhaInfo(false)
        val vedhaHouse = vedhaPairs[houseFromMoon] ?: return VedhaInfo(false)

        for ((otherPlanet, otherPos) in transitPlanetMap) {
            if (otherPlanet == planet || otherPlanet !in Planet.MAIN_PLANETS) continue
            
            val otherHouseFromMoon = calculateHouseFromMoon(otherPos.sign, natalMoonSign)
            if (otherHouseFromMoon == vedhaHouse) {
                return VedhaInfo(
                    hasVedha = true,
                    obstructingPlanet = otherPlanet,
                    obstructingHouse = vedhaHouse
                )
            }
        }

        return VedhaInfo(false)
    }

    private fun getAshtakavargaTransitScore(
        planet: Planet,
        transitSign: ZodiacSign,
        ashtakavarga: AshtakavargaCalculator.AshtakavargaAnalysis?
    ): Int? {
        if (ashtakavarga == null) return null
        val bav = ashtakavarga.bhinnashtakavarga[planet] ?: return null
        return bav.getBindusForSign(transitSign)
    }

    private fun calculateHouseFromMoon(transitSign: ZodiacSign, natalMoonSign: ZodiacSign): Int {
        return ((transitSign.ordinal - natalMoonSign.ordinal + 12) % 12) + 1
    }

    private fun analyzeGocharaEffect(
        planet: Planet,
        houseFromMoon: Int,
        isRetrograde: Boolean,
        natalPosition: PlanetPosition?,
        vedhaInfo: VedhaInfo,
        ashtakavargaScore: Int?,
        transitSign: ZodiacSign,
        language: Language
    ): Triple<String, Int, Boolean> {
        val favorableHouses = GOCHARA_FAVORABLE_HOUSES[planet] ?: emptyList()
        var isFavorable = houseFromMoon in favorableHouses

        val influenceBuilder = StringBuilder()
        var (baseInfluenceKey, baseStrength) = getGocharaInfluenceKey(planet, houseFromMoon, isFavorable)
        
        val localizedBaseInfluence = if (baseInfluenceKey == StringKey.HOROSCOPE_FAVORABLE_TRANSIT || 
            baseInfluenceKey == StringKey.HOROSCOPE_UNFAVORABLE_TRANSIT) {
            StringResources.get(baseInfluenceKey, language, planet.getLocalizedName(language), houseFromMoon)
        } else {
            StringResources.get(baseInfluenceKey, language)
        }
        influenceBuilder.append(localizedBaseInfluence)

        if (vedhaInfo.hasVedha && isFavorable) {
            val vedhaText = StringResources.get(StringKey.HOROSCOPE_VEDHA_OBSTRUCTION, language, vedhaInfo.obstructingPlanet?.getLocalizedName(language) ?: "")
            influenceBuilder.append(vedhaText)
            baseStrength = (baseStrength * 0.5).toInt().coerceAtLeast(2)
            if (baseStrength <= 3) isFavorable = false
        }

        ashtakavargaScore?.let { score ->
            when {
                score >= 5 -> {
                    influenceBuilder.append(StringResources.get(StringKey.HOROSCOPE_ASHTAKAVARGA_STRONG, language, score))
                    baseStrength = (baseStrength * 1.3).toInt()
                }
                score in 2..3 -> {
                    influenceBuilder.append(StringResources.get(StringKey.HOROSCOPE_ASHTAKAVARGA_MODERATE, language, score))
                    baseStrength = (baseStrength * 0.85).toInt()
                }
                score < 2 -> {
                    influenceBuilder.append(StringResources.get(StringKey.HOROSCOPE_ASHTAKAVARGA_WEAK, language, score))
                    if (isFavorable) isFavorable = false
                    baseStrength = (baseStrength * 0.6).toInt()
                }
            }
        }

        val retrogradeAdjustment = when {
            isRetrograde && isFavorable -> {
                influenceBuilder.append(StringResources.get(StringKey.HOROSCOPE_RETROGRADE_DELAY, language, planet.getLocalizedName(language)))
                -1
            }
            isRetrograde && !isFavorable -> {
                influenceBuilder.append(StringResources.get(StringKey.HOROSCOPE_RETROGRADE_RELIEF, language, planet.getLocalizedName(language)))
                1
            }
            else -> 0
        }

        val dignityModifier = when {
            isInOwnSign(planet, transitSign) -> {
                influenceBuilder.append(StringResources.get(StringKey.HOROSCOPE_OWN_SIGN, language))
                if (isFavorable) 2 else 0
            }
            isExalted(planet, transitSign) -> {
                influenceBuilder.append(StringResources.get(StringKey.HOROSCOPE_EXALTED, language))
                if (isFavorable) 3 else 1
            }
            isDebilitated(planet, transitSign) -> {
                influenceBuilder.append(StringResources.get(StringKey.HOROSCOPE_DEBILITATED, language))
                if (isFavorable) -2 else -1
            }
            else -> 0
        }

        val adjustedStrength = (baseStrength + retrogradeAdjustment + dignityModifier).coerceIn(1, 10)

        return Triple(influenceBuilder.toString(), adjustedStrength, isFavorable)
    }

    private fun getGocharaInfluenceKey(planet: Planet, house: Int, isFavorable: Boolean): Pair<com.astro.storm.core.common.StringKeyInterface, Int> {
        // Special mapping to keys in StringKeyHoroscope
        val keyName = "GOCHARA_${planet.name}_$house"
        val key = try {
            com.astro.storm.core.common.StringKeyHoroscope.valueOf(keyName)
        } catch (e: Exception) {
            if (isFavorable) StringKey.HOROSCOPE_FAVORABLE_TRANSIT else StringKey.HOROSCOPE_UNFAVORABLE_TRANSIT
        }
        
        val strength = if (isFavorable) 7 else 4
        return key to strength
    }

    private fun isInOwnSign(planet: Planet, sign: ZodiacSign): Boolean {
        return PLANET_OWN_SIGNS[planet]?.contains(sign) == true
    }

    private fun isExalted(planet: Planet, sign: ZodiacSign): Boolean {
        return PLANET_EXALTATION[planet] == sign
    }

    private fun isDebilitated(planet: Planet, sign: ZodiacSign): Boolean {
        return PLANET_DEBILITATION[planet] == sign
    }

    private fun calculateLifeAreaPredictions(
        natalData: NatalChartCachedData,
        transitPlanetMap: Map<Planet, PlanetPosition>,
        dashaAtDate: DashaCalculator.DashaPeriodInfo,
        date: LocalDate,
        language: Language
    ): List<LifeAreaPrediction> {
        val dashaLordName = dashaAtDate.mahadasha?.planet?.getLocalizedName(language) ?: "current"
        
        return LifeArea.entries.map { area ->
            val dashaInfluence = calculateDashaInfluenceOnArea(
                dashaAtDate = dashaAtDate,
                area = area,
                planetMap = natalData.planetMap,
                ascendantSign = natalData.ascendantSign,
                moonSign = natalData.moonSign
            )
            val transitInfluence = calculateTransitInfluenceOnArea(
                natalMoonSign = natalData.moonSign,
                natalAscendantSign = natalData.ascendantSign,
                ashtakavarga = natalData.ashtakavarga,
                transitPlanetMap = transitPlanetMap,
                area = area
            )
            val dailyRhythmBoost = calculateDailyRhythmBoost(date, area)
            val rating = (dashaInfluence * 0.55 + transitInfluence * 0.45 + dailyRhythmBoost)
                .roundToInt()
                .coerceIn(1, 5)

            val predictionKeyName = "PRED_${area.name}_$rating"
            val predictionKey = try {
                com.astro.storm.core.common.StringKeyHoroscope.valueOf(predictionKeyName)
            } catch (e: Exception) {
                StringKey.HOROSCOPE_BALANCED_ENERGY
            }
            
            val prediction = StringResources.get(predictionKey, language, dashaLordName)
            
            // For now, let's use the AREA_REC keys from StringKey.kt
            val areaRecKey = try {
                StringKey.valueOf("AREA_REC_${area.name}")
            } catch (e: Exception) {
                StringKey.ADVICE_GENERAL
            }
            
            val advice = StringResources.get(areaRecKey, language)

            LifeAreaPrediction(area = area, rating = rating, prediction = prediction, advice = advice)
        }
    }

    private fun calculateDashaInfluenceOnArea(
        dashaAtDate: DashaCalculator.DashaPeriodInfo,
        area: LifeArea,
        planetMap: Map<Planet, PlanetPosition>,
        ascendantSign: ZodiacSign,
        moonSign: ZodiacSign
    ): Double {
        val currentMahadasha = dashaAtDate.mahadasha ?: return 3.0
        val currentAntardasha = dashaAtDate.antardasha

        val mahaScore = scoreDashaPlanetForArea(
            planet = currentMahadasha.planet,
            position = planetMap[currentMahadasha.planet],
            area = area,
            ascendantSign = ascendantSign,
            moonSign = moonSign
        )

        val antarScore = currentAntardasha?.planet?.let { antarPlanet ->
            scoreDashaPlanetForArea(
                planet = antarPlanet,
                position = planetMap[antarPlanet],
                area = area,
                ascendantSign = ascendantSign,
                moonSign = moonSign
            )
        } ?: mahaScore

        var score = (mahaScore * 0.65) + (antarScore * 0.35)

        if (currentMahadasha.planet in getAreaKarakas(area)) score += 0.25
        if (currentAntardasha?.planet in getAreaKarakas(area)) score += 0.15

        return score.coerceIn(1.0, 5.0)
    }

    private fun scoreDashaPlanetForArea(
        planet: Planet,
        position: PlanetPosition?,
        area: LifeArea,
        ascendantSign: ZodiacSign,
        moonSign: ZodiacSign
    ): Double {
        if (position == null) return 3.0

        val houseFromAsc = position.house
        val houseFromMoon = calculateHouseFromMoon(position.sign, moonSign)
        val areaHouses = area.houses.toSet()

        var score = 3.0
        if (houseFromAsc in areaHouses) score += 1.1
        if (houseFromMoon in areaHouses) score += 0.6

        score += when {
            planet in NATURAL_BENEFICS -> 0.45
            planet in NATURAL_MALEFICS -> -0.35
            else -> 0.0
        }

        score += when {
            isExalted(planet, position.sign) -> 0.9
            isInOwnSign(planet, position.sign) -> 0.6
            isDebilitated(planet, position.sign) -> -0.9
            else -> 0.0
        }

        if (houseFromAsc in setOf(6, 8, 12) && houseFromAsc !in areaHouses) score -= 0.35
        if (position.isRetrograde && planet in NATURAL_BENEFICS) score -= 0.2
        if (position.isRetrograde && planet in NATURAL_MALEFICS) score += 0.1

        // Ascendant compatibility provides a small stabilizing nudge for the active dasha planet.
        val planetFromAsc = ((position.sign.ordinal - ascendantSign.ordinal + 12) % 12) + 1
        if (planetFromAsc in setOf(1, 5, 9, 10, 11)) score += 0.2

        return score.coerceIn(1.0, 5.0)
    }

    private fun calculateTransitInfluenceOnArea(
        natalMoonSign: ZodiacSign,
        natalAscendantSign: ZodiacSign,
        ashtakavarga: AshtakavargaCalculator.AshtakavargaAnalysis?,
        transitPlanetMap: Map<Planet, PlanetPosition>,
        area: LifeArea
    ): Double {
        var weightedScore = 0.0
        var totalWeight = 0.0

        for ((planet, position) in transitPlanetMap) {
            if (planet !in Planet.MAIN_PLANETS) continue

            val planetWeight = TRANSIT_PLANET_WEIGHTS[planet] ?: 1.0
            val houseFromMoon = calculateHouseFromMoon(position.sign, natalMoonSign)
            val houseFromAsc = ((position.sign.ordinal - natalAscendantSign.ordinal + 12) % 12) + 1
            val areaRelevant = houseFromMoon in area.houses || houseFromAsc in area.houses || planet in getAreaKarakas(area)

            if (!areaRelevant) continue

            val favorableMoonTransit = houseFromMoon in (GOCHARA_FAVORABLE_HOUSES[planet] ?: emptyList())

            var contribution = when {
                favorableMoonTransit -> 0.75
                else -> -0.55
            }

            if (houseFromMoon in area.houses) contribution += 0.25
            if (houseFromAsc in area.houses) {
                contribution += if (planet in NATURAL_BENEFICS) 0.25 else -0.2
            }

            contribution += when {
                isExalted(planet, position.sign) -> 0.35
                isInOwnSign(planet, position.sign) -> 0.2
                isDebilitated(planet, position.sign) -> -0.35
                else -> 0.0
            }

            val ashtakavargaScore = getAshtakavargaTransitScore(planet, position.sign, ashtakavarga)
            contribution += when {
                ashtakavargaScore == null -> 0.0
                ashtakavargaScore >= 5 -> 0.2
                ashtakavargaScore == 4 -> 0.1
                ashtakavargaScore in 2..3 -> -0.1
                else -> -0.25
            }

            if (position.isRetrograde) {
                contribution *= if (planet in NATURAL_BENEFICS) 0.85 else 0.95
            }

            weightedScore += contribution * planetWeight
            totalWeight += planetWeight
        }

        if (totalWeight <= 0.0) return 3.0

        val normalizedShift = weightedScore / totalWeight
        return (3.0 + normalizedShift * 1.8).coerceIn(1.0, 5.0)
    }

    private fun getRatingCategory(rating: Int): String = when {
        rating >= 4 -> "high"
        rating >= 3 -> "medium"
        else -> "low"
    }

    private fun calculateOverallEnergy(
        influences: List<PlanetaryInfluence>,
        lifeAreas: List<LifeAreaPrediction>,
        mahadashaPlanet: Planet?
    ): Int {
        val planetaryAvg = if (influences.isNotEmpty()) {
            influences.sumOf { it.strength }.toDouble() / influences.size
        } else 5.0

        val lifeAreaAvg = if (lifeAreas.isNotEmpty()) {
            lifeAreas.sumOf { it.rating * 2 }.toDouble() / lifeAreas.size
        } else 5.0

        val dashaBonus = DASHA_ENERGY_MODIFIERS[mahadashaPlanet] ?: 0.0
        val rawEnergy = (planetaryAvg * 0.4) + (lifeAreaAvg * 0.4) + (5.0 + dashaBonus) * 0.2

        return rawEnergy.roundToInt().coerceIn(1, 10)
    }

    private fun calculateDailyTheme(
        transitPlanetMap: Map<Planet, PlanetPosition>,
        mahadashaPlanet: Planet?
    ): Pair<StringKey, StringKey> {
        val moonSign = transitPlanetMap[Planet.MOON]?.sign ?: ZodiacSign.ARIES

        val themeKey = determineTheme(moonSign, mahadashaPlanet)
        val descriptionKey = THEME_DESCRIPTIONS_KEYS[themeKey] ?: StringKey.THEME_DESC_BALANCE_EQUILIBRIUM

        return Pair(themeKey, descriptionKey)
    }

    private fun determineTheme(moonSign: ZodiacSign, dashaLord: Planet?): StringKey {
        val moonElement = moonSign.element

        if (dashaLord == null) {
            return when (moonElement) {
                "Fire" -> StringKey.THEME_DYNAMIC_ACTION
                "Earth" -> StringKey.THEME_PRACTICAL_PROGRESS
                "Air" -> StringKey.THEME_SOCIAL_CONNECTIONS
                "Water" -> StringKey.THEME_EMOTIONAL_INSIGHT
                else -> StringKey.THEME_BALANCE_EQUILIBRIUM
            }
        }

        return when {
            moonElement == "Fire" && dashaLord in FIRE_PLANETS -> StringKey.THEME_DYNAMIC_ACTION
            moonElement == "Earth" && dashaLord in EARTH_PLANETS -> StringKey.THEME_PRACTICAL_PROGRESS
            moonElement == "Air" && dashaLord in AIR_PLANETS -> StringKey.THEME_SOCIAL_CONNECTIONS
            moonElement == "Water" && dashaLord in WATER_PLANETS -> StringKey.THEME_EMOTIONAL_INSIGHT
            else -> DASHA_LORD_THEMES_KEYS[dashaLord] ?: StringKey.THEME_BALANCE_EQUILIBRIUM
        }
    }

    private fun calculateLuckyElements(
        natalData: NatalChartCachedData,
        transitPlanetMap: Map<Planet, PlanetPosition>,
        date: LocalDate,
        language: Language
    ): LuckyElements {
        val moonSign = transitPlanetMap[Planet.MOON]?.sign ?: ZodiacSign.ARIES
        val dayOfWeek = date.dayOfWeek.value
        val ascRuler = natalData.ascendantSign.ruler

        val luckyNumber = ((dayOfWeek + moonSign.ordinal) % 9) + 1
        
        val luckyColorKey = try {
            com.astro.storm.core.common.StringKeyHoroscope.valueOf("LUCKY_COLOR_${moonSign.element.uppercase()}")
        } catch (e: Exception) {
            StringKey.LUCKY_COLOR_FIRE
        }
        val luckyColor = StringResources.get(luckyColorKey, language)
        
        val luckyDirectionKey = try {
            com.astro.storm.core.common.StringKeyHoroscope.valueOf("LUCKY_DIR_${PLANET_DIRECTIONS_MAP[ascRuler] ?: "EAST"}")
        } catch (e: Exception) {
            StringKey.LUCKY_DIRECTION_EAST
        }
        val luckyDirection = StringResources.get(luckyDirectionKey, language)
        
        val luckyTimeKey = when (dayOfWeek) {
            1 -> StringKeyHoroscope.HORA_SUN
            2 -> StringKeyHoroscope.HORA_MOON
            3 -> StringKeyHoroscope.HORA_MARS
            4 -> StringKeyHoroscope.HORA_MERCURY
            5 -> StringKeyHoroscope.HORA_JUPITER
            6 -> StringKeyHoroscope.HORA_VENUS
            else -> StringKeyHoroscope.HORA_SATURN
        }
        val luckyTime = StringResources.get(luckyTimeKey, language)
        
        val luckyGemstoneKey = try {
            StringKey.valueOf("GEMSTONE_${PLANET_GEMSTONES_MAP[ascRuler] ?: "RUBY"}")
        } catch (e: Exception) {
            StringKey.GEMSTONE_RUBY
        }
        val gemstone = StringResources.get(luckyGemstoneKey, language)

        return LuckyElements(
            number = luckyNumber,
            color = luckyColor,
            direction = luckyDirection,
            time = luckyTime,
            gemstone = gemstone
        )
    }

    private fun generateRecommendations(
        dashaAtDate: DashaCalculator.DashaPeriodInfo,
        lifeAreas: List<LifeAreaPrediction>,
        transitPlanetMap: Map<Planet, PlanetPosition>
    ): List<StringKey> {
        val recommendations = ArrayList<StringKey>(3)

        dashaAtDate.mahadasha?.planet?.let { dashaLord ->
            DASHA_RECOMMENDATIONS_KEYS[dashaLord]?.let { recommendations.add(it) }
        }

        lifeAreas.maxByOrNull { it.rating }?.let { bestArea ->
            BEST_AREA_RECOMMENDATIONS_KEYS[bestArea.area]?.let { recommendations.add(it) }
        }

        transitPlanetMap[Planet.MOON]?.let { moon ->
            ELEMENT_RECOMMENDATIONS_KEYS[moon.sign.element]?.let { recommendations.add(it) }
        }

        return recommendations.take(3)
    }

    private fun generateCautions(
        transitPlanetMap: Map<Planet, PlanetPosition>,
        influences: List<PlanetaryInfluence>
    ): List<StringKey> {
        val cautions = ArrayList<StringKey>(3)

        influences.asSequence()
            .filter { !it.isPositive && it.strength <= 4 }
            .take(2)
            .forEach { influence ->
                PLANET_CAUTIONS_KEYS[influence.planet]?.let { cautions.add(it) }
            }

        return cautions.take(2)
    }

    private fun generateAffirmationKey(dashaLord: Planet?): StringKey {
        return DASHA_AFFIRMATIONS_KEYS[dashaLord] ?: StringKey.ENERGY_BALANCED
    }

    private fun calculateKeyDates(startDate: LocalDate, endDate: LocalDate, language: Language): List<KeyDate> {
        val keyDates = ArrayList<KeyDate>(6)

        LUNAR_PHASES_KEYS.forEach { (dayOffset, eventKey, significanceKey) ->
            val date = startDate.plusDays(dayOffset.toLong())
            if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                keyDates.add(KeyDate(
                    date = date, 
                    event = StringResources.get(eventKey, language), 
                    significance = StringResources.get(significanceKey, language), 
                    isPositive = true
                ))
            }
        }

        for (offset in 0 until 7) {
            val date = startDate.plusDays(offset.toLong())
            FAVORABLE_DAYS_KEYS[date.dayOfWeek]?.let { descKey ->
                keyDates.add(KeyDate(
                    date = date,
                    event = if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(date.dayOfWeek.value.toString()) // Placeholder for localized day
                            else date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
                    significance = StringResources.get(descKey, language),
                    isPositive = true
                ))
            }
        }

        return keyDates.distinctBy { it.date }.take(4)
    }

    private fun calculateWeeklyPredictions(
        dailyHoroscopes: List<DailyHoroscope>,
        dashaTimeline: DashaCalculator.DashaTimeline,
        language: Language
    ): Map<LifeArea, String> {
        val currentDashaLord = dashaTimeline.currentMahadasha?.planet?.getLocalizedName(language)
            ?: StringResources.get(StringKey.DASHA_PERIOD, language)

        return LifeArea.entries.associateWith { area ->
            val weeklyRatings = dailyHoroscopes.mapNotNull { horoscope ->
                horoscope.lifeAreas.find { it.area == area }?.rating
            }
            val avgRating = if (weeklyRatings.isNotEmpty()) weeklyRatings.average() else 3.0
            val trend = if (weeklyRatings.size >= 2) weeklyRatings.last() - weeklyRatings.first() else 0
            val volatility = calculateVolatility(weeklyRatings)
            val weightedRating = avgRating + (trend * 0.35) - (volatility * 0.2)

            val ratingCategory = when {
                weightedRating >= 4.15 -> "EXCELLENT"
                weightedRating >= 2.85 -> "STEADY"
                else -> "CHALLENGING"
            }

            val predictionKeyName = "WEEKLY_${area.name}_$ratingCategory"
            val predictionKey = try {
                com.astro.storm.core.common.StringKeyHoroscope.valueOf(predictionKeyName)
            } catch (e: Exception) {
                StringKey.HOROSCOPE_BALANCED_ENERGY
            }

            StringResources.get(predictionKey, language, currentDashaLord)
        }
    }

    private fun calculateWeeklyTheme(
        dashaTimeline: DashaCalculator.DashaTimeline,
        dailyHighlights: List<DailyHighlight>,
        hasDashaShift: Boolean,
        language: Language
    ): Pair<StringKey, String> {
        val avgEnergy = if (dailyHighlights.isNotEmpty()) {
            dailyHighlights.sumOf { it.energy }.toDouble() / dailyHighlights.size
        } else 5.0
        val energyTrend = if (dailyHighlights.size >= 2) {
            dailyHighlights.last().energy - dailyHighlights.first().energy
        } else 0
        val energyVolatility = calculateVolatility(dailyHighlights.map { it.energy })

        val currentDashaLord = dashaTimeline.currentMahadasha?.planet

        val themeKey = when {
            hasDashaShift && avgEnergy < 7.2 -> StringKey.THEME_WEEK_MINDFUL_NAVIGATION
            avgEnergy >= 6.8 && energyTrend >= 0 -> StringKey.THEME_WEEK_OPPORTUNITIES
            avgEnergy >= 5.0 && energyVolatility <= 2.2 -> StringKey.THEME_WEEK_STEADY_PROGRESS
            avgEnergy <= 4.8 || energyVolatility > 2.2 -> StringKey.THEME_WEEK_MINDFUL_NAVIGATION
            else -> StringKey.THEME_WEEK_STEADY_PROGRESS
        }

        val overview = buildWeeklyOverview(currentDashaLord, avgEnergy, dailyHighlights, language)
        return Pair(themeKey, overview)
    }

    private fun calculateDailyRhythmBoost(date: LocalDate, area: LifeArea): Double {
        val dayLord = when (date.dayOfWeek.value) {
            1 -> Planet.MOON
            2 -> Planet.MARS
            3 -> Planet.MERCURY
            4 -> Planet.JUPITER
            5 -> Planet.VENUS
            6 -> Planet.SATURN
            else -> Planet.SUN
        }

        return when {
            area in DAY_LORD_AREA_FAVORABILITY[dayLord].orEmpty() -> 0.35
            area in DAY_LORD_AREA_CHALLENGES[dayLord].orEmpty() -> -0.2
            else -> 0.0
        }
    }

    private fun getAreaKarakas(area: LifeArea): Set<Planet> = when (area) {
        LifeArea.CAREER -> setOf(Planet.SUN, Planet.SATURN, Planet.MERCURY, Planet.JUPITER)
        LifeArea.LOVE -> setOf(Planet.VENUS, Planet.MOON, Planet.JUPITER)
        LifeArea.HEALTH -> setOf(Planet.SUN, Planet.MARS, Planet.SATURN)
        LifeArea.FINANCE -> setOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY)
        LifeArea.FAMILY -> setOf(Planet.MOON, Planet.VENUS, Planet.JUPITER)
        LifeArea.SPIRITUALITY -> setOf(Planet.JUPITER, Planet.KETU, Planet.SATURN, Planet.MOON)
    }

    private fun calculateVolatility(values: List<Int>): Double {
        if (values.size <= 1) return 0.0
        val mean = values.average()
        val variance = values.map { (it - mean) * (it - mean) }.average()
        return sqrt(variance)
    }

    private fun buildWeeklyOverview(
        dashaLord: Planet?,
        avgEnergy: Double,
        dailyHighlights: List<DailyHighlight>,
        language: Language
    ): String {
        val builder = StringBuilder()

        val dashaLabel = dashaLord?.getLocalizedName(language)
            ?: StringResources.get(StringKey.DASHA_PERIOD, language)
        builder.append(StringResources.get(StringKeyHoroscope.WEEKLY_OVERVIEW_PREFIX, language, dashaLabel))

        when {
            avgEnergy >= 7 -> builder.append(StringResources.get(StringKeyHoroscope.WEEKLY_OVERVIEW_HIGH, language))
            avgEnergy >= 5 -> builder.append(StringResources.get(StringKeyHoroscope.WEEKLY_OVERVIEW_MED, language))
            else -> builder.append(StringResources.get(StringKeyHoroscope.WEEKLY_OVERVIEW_LOW, language))
        }

        dailyHighlights.maxByOrNull { it.energy }?.let {
            val dayName = if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(it.dayOfWeek.value.toString()) // Placeholder
                          else it.dayOfWeek.name
            builder.append(StringResources.get(StringKeyHoroscope.WEEKLY_OVERVIEW_FAVORABLE, language, dayName))
        }

        builder.append(StringResources.get(StringKeyHoroscope.WEEKLY_OVERVIEW_SUFFIX, language))

        return builder.toString()
    }

    private fun generateWeeklyAdvice(
        dashaTimeline: DashaCalculator.DashaTimeline,
        keyDates: List<KeyDate>,
        language: Language
    ): String {
        val currentDashaLord = dashaTimeline.currentMahadasha?.planet
        val builder = StringBuilder()

        val dashaLabel = currentDashaLord?.getLocalizedName(language)
            ?: StringResources.get(StringKey.DASHA_PERIOD, language)
        builder.append(StringResources.get(StringKeyHoroscope.WEEKLY_ADVICE_PREFIX, language, dashaLabel))

        val adviceKey = currentDashaLord?.let { DASHA_WEEKLY_ADVICE_KEYS[it] } ?: StringKey.ADVICE_GENERAL
        builder.append(StringResources.get(adviceKey, language))

        keyDates.firstOrNull { it.isPositive }?.let {
            val dateStr = if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(it.date.dayOfMonth.toString())
                          else it.date.format(DATE_FORMATTER)
            builder.append(StringResources.get(StringKeyHoroscope.WEEKLY_ADVICE_DATE, language, dateStr))
        }

        return builder.toString()
    }

    fun clearCache() {
        transitCache.clear()
        dailyHoroscopeCache.clear()
        natalDataCache.clear()
    }

    override fun close() {
        if (isClosed.compareAndSet(false, true)) {
            try {
                clearCache()
                ephemerisEngine.close()
            } catch (e: Exception) {
                Log.w(TAG, "Error closing ephemeris engine", e)
            }
        }
    }

    class HoroscopeCalculationException(message: String, cause: Throwable? = null) : Exception(message, cause)

    private class LRUCache<K, V>(private val maxSize: Int) {
        private val cache = object : LinkedHashMap<K, V>(maxSize, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
                return size > maxSize
            }
        }

        @Synchronized
        operator fun get(key: K): V? = cache[key]

        @Synchronized
        operator fun set(key: K, value: V) {
            cache[key] = value
        }

        @Synchronized
        fun clear() = cache.clear()
    }

    companion object {
        private const val TAG = "HoroscopeCalculator"
        private const val MAX_TRANSIT_CACHE_SIZE = 30
        private const val MAX_HOROSCOPE_CACHE_SIZE = 50
        private const val MAX_NATAL_CACHE_SIZE = 10

        private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d")

        private val NATURAL_BENEFICS = setOf(Planet.JUPITER, Planet.VENUS, Planet.MERCURY, Planet.MOON)
        private val NATURAL_MALEFICS = setOf(Planet.SATURN, Planet.MARS, Planet.RAHU, Planet.KETU)

        private val FIRE_PLANETS = setOf(Planet.SUN, Planet.MARS, Planet.JUPITER)
        private val EARTH_PLANETS = setOf(Planet.VENUS, Planet.MERCURY, Planet.SATURN)
        private val AIR_PLANETS = setOf(Planet.MERCURY, Planet.VENUS, Planet.SATURN)
        private val WATER_PLANETS = setOf(Planet.MOON, Planet.MARS, Planet.JUPITER)

        private val PLANET_OWN_SIGNS = mapOf(
            Planet.SUN to setOf(ZodiacSign.LEO),
            Planet.MOON to setOf(ZodiacSign.CANCER),
            Planet.MARS to setOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO),
            Planet.MERCURY to setOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO),
            Planet.JUPITER to setOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES),
            Planet.VENUS to setOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA),
            Planet.SATURN to setOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS)
        )

        private val PLANET_EXALTATION = mapOf(
            Planet.SUN to ZodiacSign.ARIES,
            Planet.MOON to ZodiacSign.TAURUS,
            Planet.MARS to ZodiacSign.CAPRICORN,
            Planet.MERCURY to ZodiacSign.VIRGO,
            Planet.JUPITER to ZodiacSign.CANCER,
            Planet.VENUS to ZodiacSign.PISCES,
            Planet.SATURN to ZodiacSign.LIBRA
        )

        private val PLANET_DEBILITATION = mapOf(
            Planet.SUN to ZodiacSign.LIBRA,
            Planet.MOON to ZodiacSign.SCORPIO,
            Planet.MARS to ZodiacSign.CANCER,
            Planet.MERCURY to ZodiacSign.PISCES,
            Planet.JUPITER to ZodiacSign.CAPRICORN,
            Planet.VENUS to ZodiacSign.VIRGO,
            Planet.SATURN to ZodiacSign.ARIES
        )

        private val GOCHARA_FAVORABLE_HOUSES = mapOf(
            Planet.SUN to listOf(3, 6, 10, 11),
            Planet.MOON to listOf(1, 3, 6, 7, 10, 11),
            Planet.MARS to listOf(3, 6, 11),
            Planet.MERCURY to listOf(2, 4, 6, 8, 10, 11),
            Planet.JUPITER to listOf(2, 5, 7, 9, 11),
            Planet.VENUS to listOf(1, 2, 3, 4, 5, 8, 9, 11, 12),
            Planet.SATURN to listOf(3, 6, 11),
            Planet.RAHU to listOf(3, 6, 10, 11),
            Planet.KETU to listOf(3, 6, 9, 11)
        )

        private val GOCHARA_VEDHA_PAIRS = mapOf(
            Planet.SUN to mapOf(3 to 9, 6 to 12, 10 to 4, 11 to 5),
            Planet.MOON to mapOf(1 to 5, 3 to 9, 6 to 12, 7 to 2, 10 to 4, 11 to 8),
            Planet.MARS to mapOf(3 to 12, 6 to 9, 11 to 5),
            Planet.MERCURY to mapOf(2 to 5, 4 to 3, 6 to 9, 8 to 1, 10 to 8, 11 to 12),
            Planet.JUPITER to mapOf(2 to 12, 5 to 4, 7 to 3, 9 to 10, 11 to 8),
            Planet.VENUS to mapOf(1 to 8, 2 to 7, 3 to 1, 4 to 10, 5 to 9, 8 to 5, 9 to 11, 11 to 6, 12 to 3),
            Planet.SATURN to mapOf(3 to 12, 6 to 9, 11 to 5),
            Planet.RAHU to mapOf(3 to 12, 6 to 9, 10 to 4, 11 to 5),
            Planet.KETU to mapOf(3 to 12, 6 to 9, 9 to 10, 11 to 5)
        )

        private val TRANSIT_PLANET_WEIGHTS = mapOf(
            Planet.SUN to 1.1,
            Planet.MOON to 1.4,
            Planet.MARS to 1.0,
            Planet.MERCURY to 1.0,
            Planet.JUPITER to 1.3,
            Planet.VENUS to 1.1,
            Planet.SATURN to 1.2,
            Planet.RAHU to 0.9,
            Planet.KETU to 0.9
        )

        private val DAY_LORD_AREA_FAVORABILITY = mapOf(
            Planet.SUN to setOf(LifeArea.CAREER, LifeArea.HEALTH),
            Planet.MOON to setOf(LifeArea.FAMILY, LifeArea.LOVE),
            Planet.MARS to setOf(LifeArea.HEALTH, LifeArea.CAREER),
            Planet.MERCURY to setOf(LifeArea.CAREER, LifeArea.FINANCE),
            Planet.JUPITER to setOf(LifeArea.SPIRITUALITY, LifeArea.FINANCE, LifeArea.FAMILY),
            Planet.VENUS to setOf(LifeArea.LOVE, LifeArea.FINANCE),
            Planet.SATURN to setOf(LifeArea.CAREER, LifeArea.SPIRITUALITY)
        )

        private val DAY_LORD_AREA_CHALLENGES = mapOf(
            Planet.SUN to setOf(LifeArea.LOVE),
            Planet.MOON to setOf(LifeArea.CAREER),
            Planet.MARS to setOf(LifeArea.LOVE, LifeArea.FAMILY),
            Planet.MERCURY to setOf(LifeArea.HEALTH),
            Planet.JUPITER to setOf(LifeArea.HEALTH),
            Planet.VENUS to setOf(LifeArea.HEALTH),
            Planet.SATURN to setOf(LifeArea.LOVE, LifeArea.FAMILY)
        )

        private val FAVORABLE_GOCHARA_EFFECTS_DETAILED = mapOf(
            Planet.SUN to mapOf(
                3 to ("Courage and valor increase. Victory over rivals." to 8),
                6 to ("Destruction of enemies. Health improves. Debts decrease." to 8),
                10 to ("Professional success and recognition. Authority increases." to 9),
                11 to ("Gains of wealth. Fulfillment of desires. Success in endeavors." to 9)
            ),
            Planet.MOON to mapOf(
                1 to ("Mental peace and satisfaction. Good health and comforts." to 8),
                3 to ("Courage increases. Success in short journeys. Good relations with siblings." to 7),
                6 to ("Victory over enemies. Relief from debts and diseases." to 8),
                7 to ("Pleasure through spouse. Partnership gains. Social happiness." to 8),
                10 to ("Success in profession. Recognition from superiors." to 8),
                11 to ("Financial gains. Fulfillment of desires. Social success." to 9)
            ),
            Planet.MARS to mapOf(
                3 to ("Courage and determination. Victory in competitions. Energy for initiatives." to 8),
                6 to ("Defeat of enemies. Success through effort. Good for legal matters." to 8),
                11 to ("Financial gains through effort. Achievement of goals. Success in ventures." to 8)
            ),
            Planet.MERCURY to mapOf(
                2 to ("Gains through speech and intellect. Family harmony. Financial gains." to 8),
                4 to ("Domestic happiness. Property matters favorable. Mental peace." to 7),
                6 to ("Victory over competitors. Success in studies. Sharp intellect." to 8),
                8 to ("Gains through research. Understanding occult matters." to 7),
                10 to ("Professional success. Recognition for intelligence. Business growth." to 8),
                11 to ("Financial gains through communication. Network expansion." to 8)
            ),
            Planet.JUPITER to mapOf(
                2 to ("Wealth increases. Family harmony. Sweet speech. Good food." to 9),
                5 to ("Intelligence flourishes. Good for children. Romance. Creativity." to 9),
                7 to ("Partnership success. Marriage prospects. Business partnerships." to 8),
                9 to ("Spiritual growth. Luck and fortune. Father's blessings. Pilgrimage." to 10),
                11 to ("Major gains. Fulfillment of desires. Eldest sibling's success." to 9)
            ),
            Planet.VENUS to mapOf(
                1 to ("Personal charm increases. Attraction and luxury. Good health." to 8),
                2 to ("Wealth and family happiness. Good food and comforts." to 8),
                3 to ("Artistic talents shine. Harmonious relations with siblings." to 7),
                4 to ("Domestic bliss. Vehicle and property gains. Mother's blessings." to 8),
                5 to ("Romance and creativity. Pleasure through children. Entertainment." to 9),
                8 to ("Unexpected gains. Inheritance matters favorable." to 7),
                9 to ("Fortune through relationships. Spiritual partnerships." to 8),
                11 to ("Major gains through arts/finance. Social success." to 9),
                12 to ("Pleasures of bed. Foreign connections favorable." to 7)
            ),
            Planet.SATURN to mapOf(
                3 to ("Perseverance pays off. Courage through discipline. Victory through patience." to 7),
                6 to ("Victory over enemies through persistence. Health through discipline." to 8),
                11 to ("Long-term gains materialize. Slow but steady prosperity." to 8)
            ),
            Planet.RAHU to mapOf(
                3 to ("Courage for unconventional paths. Success through innovation." to 7),
                6 to ("Victory over hidden enemies. Overcoming obstacles." to 7),
                10 to ("Sudden rise in career. Foreign opportunities." to 8),
                11 to ("Unexpected gains. Fulfillment of unusual desires." to 8)
            ),
            Planet.KETU to mapOf(
                3 to ("Spiritual courage. Success in research." to 7),
                6 to ("Healing abilities increase. Victory through spiritual means." to 7),
                9 to ("Spiritual insights. Pilgrimage. Blessings from teachers." to 8),
                11 to ("Gains through spiritual pursuits. Liberation from desires." to 7)
            )
        )

        private val UNFAVORABLE_GOCHARA_EFFECTS_DETAILED = mapOf(
            Planet.SUN to mapOf(
                1 to ("Health issues. Ego challenges. Conflicts with authority." to 4),
                2 to ("Financial difficulties. Family disputes. Speech issues." to 4),
                4 to ("Domestic unrest. Mental worry. Vehicle problems." to 4),
                5 to ("Obstacles to children. Poor decisions. Speculation loss." to 4),
                7 to ("Relationship strain. Partnership challenges." to 4),
                8 to ("Health concerns. Unexpected problems. Hidden enemies." to 3),
                9 to ("Obstacles in luck. Difficulties with father/teacher." to 4),
                12 to ("Expenses increase. Sleep disturbances. Hidden losses." to 4)
            ),
            Planet.MOON to mapOf(
                2 to ("Financial fluctuations. Emotional eating issues." to 4),
                4 to ("Mental restlessness. Domestic worries." to 4),
                5 to ("Emotional challenges with children. Poor speculation." to 4),
                8 to ("Emotional turmoil. Hidden anxieties. Health vulnerabilities." to 3),
                9 to ("Spiritual doubts. Emotional distance from teachers." to 4),
                12 to ("Sleep issues. Expenses. Emotional withdrawal." to 4)
            ),
            Planet.MARS to mapOf(
                1 to ("Impulsive actions. Accidents. Health issues. Anger." to 4),
                2 to ("Financial losses through haste. Family arguments." to 4),
                4 to ("Domestic conflicts. Property disputes. Mother's health." to 4),
                5 to ("Children's issues. Poor decisions. Speculation loss." to 4),
                7 to ("Relationship conflicts. Partnership disputes." to 3),
                8 to ("Accidents. Surgeries. Hidden enemies active." to 3),
                9 to ("Conflicts with teachers. Father's health." to 4),
                10 to ("Professional conflicts. Authority issues." to 4),
                12 to ("Hidden enemies. Expenses. Hospitalization risk." to 3)
            ),
            Planet.MERCURY to mapOf(
                1 to ("Nervous tension. Skin issues. Restless mind." to 4),
                3 to ("Communication problems. Sibling issues. Short trips troubled." to 4),
                5 to ("Poor decisions. Learning difficulties." to 4),
                7 to ("Partnership misunderstandings. Contract issues." to 4),
                9 to ("Educational obstacles. Communication with father strained." to 4),
                12 to ("Mental anxieties. Hidden worries. Poor sleep." to 4)
            ),
            Planet.JUPITER to mapOf(
                1 to ("Weight gain. Overconfidence. Health issues." to 4),
                3 to ("Reduced courage. Sibling issues." to 5),
                4 to ("Domestic expansion issues. Property disputes." to 4),
                6 to ("Debts may increase. Enemy problems." to 4),
                8 to ("Unexpected expenses. Health vulnerabilities." to 4),
                10 to ("Professional setbacks. Reputation challenges." to 4),
                12 to ("Expenses. Foreign troubles. Spiritual doubts." to 4)
            ),
            Planet.SATURN to mapOf(
                1 to ("Health issues. Depression. Physical weakness." to 3),
                2 to ("Financial constraints. Family separation. Speech issues." to 4),
                4 to ("Domestic stress. Mother's health. Property issues." to 3),
                5 to ("Children's problems. Poor decisions. Mental worry." to 4),
                7 to ("Relationship strain. Partnership challenges. Delays in marriage." to 3),
                8 to ("Chronic health issues. Hidden problems. Accidents." to 2),
                9 to ("Father's troubles. Spiritual obstacles. Bad luck phase." to 3),
                10 to ("Career setbacks. Authority conflicts. Reputation damage." to 4),
                12 to ("Isolation. Expenses. Sleep issues. Foreign troubles." to 3)
            ),
            Planet.RAHU to mapOf(
                1 to ("Confusion. Wrong decisions. Health anxieties." to 4),
                2 to ("Financial deception. Family disharmony." to 4),
                4 to ("Mental confusion. Domestic issues." to 4),
                5 to ("Children's concerns. Poor speculation." to 4),
                7 to ("Relationship deceptions. Partnership frauds." to 3),
                8 to ("Sudden problems. Hidden enemies. Health scares." to 3),
                9 to ("Spiritual confusion. Issues with teachers." to 4),
                12 to ("Hidden enemies. Expenses. Foreign troubles." to 3)
            ),
            Planet.KETU to mapOf(
                1 to ("Health vulnerabilities. Lack of direction." to 4),
                2 to ("Financial losses. Family separation." to 4),
                4 to ("Domestic detachment. Property losses." to 4),
                5 to ("Children's issues. Poor decisions." to 4),
                7 to ("Relationship detachment. Partnership dissolution." to 3),
                8 to ("Sudden health issues. Accidents. Hidden problems." to 3),
                10 to ("Career confusion. Direction loss." to 4),
                12 to ("Expenses. Spiritual confusion. Isolation." to 4)
            )
        )

        private val DASHA_ENERGY_MODIFIERS = mapOf(
            Planet.JUPITER to 1.5,
            Planet.VENUS to 1.5,
            Planet.MERCURY to 1.0,
            Planet.MOON to 1.0,
            Planet.SUN to 0.5,
            Planet.SATURN to -0.5,
            Planet.MARS to -0.5,
            Planet.RAHU to -1.0,
            Planet.KETU to -1.0
        )

        private val DASHA_LORD_THEMES_KEYS = mapOf(
            Planet.JUPITER to StringKey.THEME_EXPANSION_WISDOM,
            Planet.VENUS to StringKey.THEME_HARMONY_BEAUTY,
            Planet.SATURN to StringKey.THEME_DISCIPLINE_GROWTH,
            Planet.MERCURY to StringKey.THEME_COMMUNICATION_LEARNING,
            Planet.MARS to StringKey.THEME_ENERGY_INITIATIVE,
            Planet.SUN to StringKey.THEME_SELF_EXPRESSION,
            Planet.MOON to StringKey.THEME_INTUITION_NURTURING,
            Planet.RAHU to StringKey.THEME_TRANSFORMATION,
            Planet.KETU to StringKey.THEME_SPIRITUAL_LIBERATION
        )

        private val THEME_DESCRIPTIONS_KEYS = mapOf(
            StringKey.THEME_DYNAMIC_ACTION to StringKey.THEME_DESC_DYNAMIC_ACTION,
            StringKey.THEME_PRACTICAL_PROGRESS to StringKey.THEME_DESC_PRACTICAL_PROGRESS,
            StringKey.THEME_SOCIAL_CONNECTIONS to StringKey.THEME_DESC_SOCIAL_CONNECTIONS,
            StringKey.THEME_EMOTIONAL_INSIGHT to StringKey.THEME_DESC_EMOTIONAL_INSIGHT,
            StringKey.THEME_EXPANSION_WISDOM to StringKey.THEME_DESC_EXPANSION_WISDOM,
            StringKey.THEME_HARMONY_BEAUTY to StringKey.THEME_DESC_HARMONY_BEAUTY,
            StringKey.THEME_DISCIPLINE_GROWTH to StringKey.THEME_DESC_DISCIPLINE_GROWTH,
            StringKey.THEME_COMMUNICATION_LEARNING to StringKey.THEME_DESC_COMMUNICATION_LEARNING,
            StringKey.THEME_ENERGY_INITIATIVE to StringKey.THEME_DESC_ENERGY_INITIATIVE,
            StringKey.THEME_SELF_EXPRESSION to StringKey.THEME_DESC_SELF_EXPRESSION,
            StringKey.THEME_INTUITION_NURTURING to StringKey.THEME_DESC_INTUITION_NURTURING,
            StringKey.THEME_TRANSFORMATION to StringKey.THEME_DESC_TRANSFORMATION,
            StringKey.THEME_SPIRITUAL_LIBERATION to StringKey.THEME_DESC_SPIRITUAL_LIBERATION,
            StringKey.THEME_BALANCE_EQUILIBRIUM to StringKey.THEME_DESC_BALANCE_EQUILIBRIUM
        )

        private val ELEMENT_COLORS = mapOf(
            "Fire" to "Red, Orange, or Gold",
            "Earth" to "Green, Brown, or White",
            "Air" to "Blue, Light Blue, or Silver",
            "Water" to "White, Cream, or Sea Green"
        )

        private val PLANET_DIRECTIONS = mapOf(
            Planet.SUN to "East",
            Planet.MARS to "East",
            Planet.MOON to "North-West",
            Planet.VENUS to "South-East",
            Planet.MERCURY to "North",
            Planet.JUPITER to "North-East",
            Planet.SATURN to "West",
            Planet.RAHU to "South-West",
            Planet.KETU to "North-West"
        )

        private val DAY_HORA_TIMES = mapOf(
            1 to "6:00 AM - 7:00 AM (Sun Hora)",
            2 to "7:00 AM - 8:00 AM (Moon Hora)",
            3 to "8:00 AM - 9:00 AM (Mars Hora)",
            4 to "9:00 AM - 10:00 AM (Mercury Hora)",
            5 to "10:00 AM - 11:00 AM (Jupiter Hora)",
            6 to "11:00 AM - 12:00 PM (Venus Hora)",
            7 to "5:00 PM - 6:00 PM (Saturn Hora)"
        )

        private val PLANET_GEMSTONES = mapOf(
            Planet.SUN to "Ruby",
            Planet.MOON to "Pearl",
            Planet.MARS to "Red Coral",
            Planet.MERCURY to "Emerald",
            Planet.JUPITER to "Yellow Sapphire",
            Planet.VENUS to "Diamond or White Sapphire",
            Planet.SATURN to "Blue Sapphire",
            Planet.RAHU to "Hessonite",
            Planet.KETU to "Cat's Eye"
        )

        private val DASHA_RECOMMENDATIONS_KEYS = mapOf(
            Planet.SUN to StringKey.ADVICE_SUN,
            Planet.MOON to StringKey.ADVICE_MOON,
            Planet.MARS to StringKey.ADVICE_MARS,
            Planet.MERCURY to StringKey.ADVICE_MERCURY,
            Planet.JUPITER to StringKey.ADVICE_JUPITER,
            Planet.VENUS to StringKey.ADVICE_VENUS,
            Planet.SATURN to StringKey.ADVICE_SATURN,
            Planet.RAHU to StringKey.ADVICE_RAHU,
            Planet.KETU to StringKey.ADVICE_KETU
        )

        private val BEST_AREA_RECOMMENDATIONS_KEYS = mapOf(
            LifeArea.CAREER to StringKey.AREA_REC_CAREER,
            LifeArea.LOVE to StringKey.AREA_REC_LOVE,
            LifeArea.HEALTH to StringKey.AREA_REC_HEALTH,
            LifeArea.FINANCE to StringKey.AREA_REC_FINANCE,
            LifeArea.FAMILY to StringKey.AREA_REC_FAMILY,
            LifeArea.SPIRITUALITY to StringKey.AREA_REC_SPIRITUALITY
        )

        private val ELEMENT_RECOMMENDATIONS_KEYS = mapOf(
            "Fire" to StringKey.ELEMENT_REC_FIRE,
            "Earth" to StringKey.ELEMENT_REC_EARTH,
            "Air" to StringKey.ELEMENT_REC_AIR,
            "Water" to StringKey.ELEMENT_REC_WATER
        )

        private val PLANET_CAUTIONS_KEYS = mapOf(
            Planet.SATURN to StringKey.CAUTION_SATURN,
            Planet.MARS to StringKey.CAUTION_MARS,
            Planet.RAHU to StringKey.CAUTION_RAHU,
            Planet.KETU to StringKey.CAUTION_KETU
        )

        private val DASHA_AFFIRMATIONS_KEYS = mapOf(
            Planet.SUN to StringKey.THEME_SELF_EXPRESSION,
            Planet.MOON to StringKey.THEME_INTUITION_NURTURING,
            Planet.MARS to StringKey.THEME_ENERGY_INITIATIVE,
            Planet.MERCURY to StringKey.THEME_COMMUNICATION_LEARNING,
            Planet.JUPITER to StringKey.THEME_EXPANSION_WISDOM,
            Planet.VENUS to StringKey.THEME_HARMONY_BEAUTY,
            Planet.SATURN to StringKey.THEME_DISCIPLINE_GROWTH,
            Planet.RAHU to StringKey.THEME_TRANSFORMATION,
            Planet.KETU to StringKey.THEME_SPIRITUAL_LIBERATION
        )

    private val LUNAR_PHASES_KEYS: List<Triple<Int, StringKeyInterface, StringKeyInterface>> = listOf(
        Triple(0, StringKey.PERIOD_TODAY, StringKey.MSG_MAY_TAKE_MOMENT),
        Triple(7, StringKeyHoroscope.LUNAR_FIRST_QUARTER, StringKeyHoroscope.LUNAR_ACTION),
        Triple(14, StringKeyHoroscope.LUNAR_FULL_MOON, StringKeyHoroscope.LUNAR_COMPLETION)
    )

    private val FAVORABLE_DAYS_KEYS = mapOf(
        java.time.DayOfWeek.THURSDAY to StringKeyHoroscope.DAY_JUPITER,
        java.time.DayOfWeek.FRIDAY to StringKeyHoroscope.DAY_VENUS
    )

        private val DASHA_WEEKLY_ADVICE_KEYS = mapOf(
            Planet.JUPITER to StringKey.ADVICE_JUPITER,
            Planet.VENUS to StringKey.ADVICE_VENUS,
            Planet.SATURN to StringKey.ADVICE_SATURN,
            Planet.MERCURY to StringKey.ADVICE_MERCURY,
            Planet.MARS to StringKey.ADVICE_MARS,
            Planet.SUN to StringKey.ADVICE_SUN,
            Planet.MOON to StringKey.ADVICE_MOON,
            Planet.RAHU to StringKey.ADVICE_RAHU,
            Planet.KETU to StringKey.ADVICE_KETU
        )

        private val PLANET_DIRECTIONS_MAP = mapOf(
            Planet.SUN to "EAST",
            Planet.MARS to "EAST",
            Planet.MOON to "NORTHWEST",
            Planet.VENUS to "SOUTHEAST",
            Planet.MERCURY to "NORTH",
            Planet.JUPITER to "NORTHEAST",
            Planet.SATURN to "WEST",
            Planet.RAHU to "SOUTHWEST",
            Planet.KETU to "NORTHWEST"
        )

        private val PLANET_GEMSTONES_MAP = mapOf(
            Planet.SUN to "RUBY",
            Planet.MOON to "PEARL",
            Planet.MARS to "RED_CORAL",
            Planet.MERCURY to "EMERALD",
            Planet.JUPITER to "YELLOW_SAPPHIRE",
            Planet.VENUS to "DIAMOND",
            Planet.SATURN to "BLUE_SAPPHIRE",
            Planet.RAHU to "HESSONITE",
            Planet.KETU to "CATS_EYE"
        )
    }
}
