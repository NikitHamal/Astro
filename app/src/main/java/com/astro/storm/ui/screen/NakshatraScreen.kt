package com.astro.storm.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import com.astro.storm.ui.components.common.NeoVedicPageHeader
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringKeyNakshatra
import com.astro.storm.core.common.StringResources
import com.astro.storm.data.localization.currentLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ui.theme.AppTheme

/**
 * Nakshatra Analysis Screen
 *
 * Comprehensive Vedic astrology nakshatra (lunar mansion) analysis featuring:
 * - Birth nakshatra details with deity, ruler, and characteristics
 * - All planetary nakshatra placements
 * - Nakshatra compatibility analysis (Tarabala, Chandrabala)
 * - Personalized nakshatra-based remedies
 * - Detailed nakshatra attributes and symbolism
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NakshatraScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(
            title = stringResource(StringKeyDosha.NAKSHATRA_TITLE),
            message = stringResource(StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    val language = currentLanguage()
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var expandedPlanet by remember { mutableStateOf<Planet?>(null) }

    val tabs = listOf(
        stringResource(StringKeyDosha.NAKSHATRA_OVERVIEW),
        stringResource(StringKeyDosha.NAKSHATRA_DETAILS),
        stringResource(StringKeyDosha.NAKSHATRA_COMPATIBILITY),
        stringResource(StringKeyDosha.NAKSHATRA_REMEDIES)
    )

    // Calculate nakshatra analysis
    val nakshatraAnalysis = remember(chart, language) {
        calculateNakshatraAnalysis(chart, language)
    }

    if (showInfoDialog) {
        NakshatraInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.NAKSHATRA_TITLE),
                subtitle = chart.birthData.name,
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyDosha.NAKSHATRA_INFO_TITLE),
                            tint = AppTheme.TextPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppTheme.ScreenBackground),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Tab selector
            item {
                NakshatraTabSelector(
                    tabs = tabs,
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }

            // Tab content
            when (selectedTab) {
                0 -> item { NakshatraOverviewTab(analysis = nakshatraAnalysis, chart = chart, language = language) }
                1 -> item { NakshatraDetailsTab(analysis = nakshatraAnalysis, expandedPlanet = expandedPlanet, onExpandPlanet = { expandedPlanet = if (expandedPlanet == it) null else it }, language = language) }
                2 -> item { NakshatraCompatibilityTab(analysis = nakshatraAnalysis, language = language) }
                3 -> item { NakshatraRemediesTab(analysis = nakshatraAnalysis, language = language) }
            }
        }
    }
}

// ============================================
// Data Classes for Nakshatra Analysis
// ============================================

data class NakshatraDetails(
    val nakshatra: Nakshatra,
    val pada: Int,
    val degreeInNakshatra: Double,
    val deity: String,
    val symbol: String,
    val nature: NakshatraNature,
    val gana: NakshatraGana,
    val animal: String,
    val element: NakshatraElement,
    val caste: NakshatraCaste,
    val direction: String,
    val bodyPart: String,
    val gender: NakshatraGender,
    val dosha: NakshatraDosha,
    val qualities: List<String>,
    val strengths: List<String>,
    val weaknesses: List<String>,
    val careerAptitudes: List<String>,
    val mantra: String
)

enum class NakshatraNature(val key: StringKeyDosha) {
    FIXED(StringKeyDosha.NAKSHATRA_NATURE_FIXED),
    MOVEABLE(StringKeyDosha.NAKSHATRA_NATURE_MOVEABLE),
    SHARP(StringKeyDosha.NAKSHATRA_NATURE_SHARP),
    SOFT(StringKeyDosha.NAKSHATRA_NATURE_SOFT),
    MIXED(StringKeyDosha.NAKSHATRA_NATURE_MIXED),
    LIGHT(StringKeyDosha.NAKSHATRA_NATURE_LIGHT),
    FIERCE(StringKeyDosha.NAKSHATRA_NATURE_FIERCE);

    fun getLocalizedName(language: Language): String = StringResources.get(key, language)
}

enum class NakshatraGana(val key: StringKeyDosha) {
    DEVA(StringKeyDosha.NAKSHATRA_GANA_DEVA),
    MANUSHYA(StringKeyDosha.NAKSHATRA_GANA_MANUSHYA),
    RAKSHASA(StringKeyDosha.NAKSHATRA_GANA_RAKSHASA);

    fun getLocalizedName(language: Language): String = StringResources.get(key, language)
}

enum class NakshatraElement(val key: StringKeyDosha) {
    FIRE(StringKeyDosha.NAKSHATRA_ELEMENT_FIRE),
    EARTH(StringKeyDosha.NAKSHATRA_ELEMENT_EARTH),
    AIR(StringKeyDosha.NAKSHATRA_ELEMENT_AIR),
    WATER(StringKeyDosha.NAKSHATRA_ELEMENT_WATER),
    ETHER(StringKeyDosha.NAKSHATRA_ELEMENT_ETHER);

    fun getLocalizedName(language: Language): String = StringResources.get(key, language)
}

enum class NakshatraCaste(val key: StringKeyDosha) {
    BRAHMIN(StringKeyDosha.NAKSHATRA_CASTE_BRAHMIN),
    KSHATRIYA(StringKeyDosha.NAKSHATRA_CASTE_KSHATRIYA),
    VAISHYA(StringKeyDosha.NAKSHATRA_CASTE_VAISHYA),
    SHUDRA(StringKeyDosha.NAKSHATRA_CASTE_SHUDRA);

    fun getLocalizedName(language: Language): String = StringResources.get(key, language)
}

enum class NakshatraGender(val key: StringKeyDosha) {
    MALE(StringKeyDosha.NAKSHATRA_GENDER_MALE),
    FEMALE(StringKeyDosha.NAKSHATRA_GENDER_FEMALE),
    NEUTRAL(StringKeyDosha.NAKSHATRA_GENDER_NEUTRAL);

    fun getLocalizedName(language: Language): String = StringResources.get(key, language)
}

enum class NakshatraDosha(val key: StringKeyDosha) {
    VATA(StringKeyDosha.NAKSHATRA_DOSHA_VATA),
    PITTA(StringKeyDosha.NAKSHATRA_DOSHA_PITTA),
    KAPHA(StringKeyDosha.NAKSHATRA_DOSHA_KAPHA);

    fun getLocalizedName(language: Language): String = StringResources.get(key, language)
}

data class PlanetaryNakshatra(
    val planet: Planet,
    val position: PlanetPosition,
    val details: NakshatraDetails
)

data class TarabalaResult(
    val nakshatraName: String,
    val taraNumber: Int,
    val taraName: String,
    val isFavorable: Boolean,
    val description: String
)

data class NakshatraCompatibility(
    val vedhaPartners: List<Nakshatra>,
    val rajjuType: RajjuType,
    val favorableNakshatras: List<Nakshatra>,
    val unfavorableNakshatras: List<Nakshatra>,
    val tarabalaResults: List<TarabalaResult>
)

enum class RajjuType(val key: StringKeyDosha, val bodyPartKey: StringKeyDosha) {
    PAADA(StringKeyDosha.NAKSHATRA_RAJJU_PAADA, StringKeyDosha.NAKSHATRA_BODY_PART_FEET),
    KATI(StringKeyDosha.NAKSHATRA_RAJJU_KATI, StringKeyDosha.NAKSHATRA_BODY_PART_WAIST),
    NABHI(StringKeyDosha.NAKSHATRA_RAJJU_NABHI, StringKeyDosha.NAKSHATRA_BODY_PART_NAVEL),
    KANTHA(StringKeyDosha.NAKSHATRA_RAJJU_KANTHA, StringKeyDosha.NAKSHATRA_BODY_PART_NECK),
    SHIRO(StringKeyDosha.NAKSHATRA_RAJJU_SHIRO, StringKeyDosha.NAKSHATRA_BODY_PART_HEAD);

    fun getLocalizedName(language: Language): String = StringResources.get(key, language)
    fun getLocalizedBodyPart(language: Language): String = StringResources.get(bodyPartKey, language)
}

data class NakshatraRemedy(
    val title: String,
    val description: String,
    val mantra: String,
    val timing: String,
    val gemstone: String?,
    val deity: String,
    val favorableDays: List<String>,
    val luckyNumbers: List<Int>,
    val luckyColors: List<String>
)

data class NakshatraAnalysis(
    val birthNakshatra: NakshatraDetails,
    val moonNakshatra: NakshatraDetails,
    val planetaryNakshatras: List<PlanetaryNakshatra>,
    val compatibility: NakshatraCompatibility,
    val remedy: NakshatraRemedy
)

// ============================================
// Nakshatra Calculation Engine
// ============================================

private fun calculateNakshatraAnalysis(chart: VedicChart, language: Language): NakshatraAnalysis {
    val moonPosition = chart.planetPositions.find { it.planet == Planet.MOON }
        ?: throw IllegalStateException("Moon position not found in chart")

    val birthNakshatra = calculateNakshatraDetails(moonPosition.nakshatra, moonPosition.nakshatraPada, moonPosition.longitude, language)
    val moonNakshatra = birthNakshatra // Same as birth nakshatra for moon

    val planetaryNakshatras = chart.planetPositions.map { pos ->
        PlanetaryNakshatra(
            planet = pos.planet,
            position = pos,
            details = calculateNakshatraDetails(pos.nakshatra, pos.nakshatraPada, pos.longitude, language)
        )
    }

    val compatibility = calculateNakshatraCompatibility(birthNakshatra.nakshatra, language)
    val remedy = calculateNakshatraRemedy(birthNakshatra, language)

    return NakshatraAnalysis(
        birthNakshatra = birthNakshatra,
        moonNakshatra = moonNakshatra,
        planetaryNakshatras = planetaryNakshatras,
        compatibility = compatibility,
        remedy = remedy
    )
}

private fun calculateNakshatraDetails(nakshatra: Nakshatra, pada: Int, longitude: Double, language: Language): NakshatraDetails {
    val nakshatraSpan = 360.0 / 27.0
    val normalizedLong = ((longitude % 360.0) + 360.0) % 360.0
    val degreeInNakshatra = (normalizedLong % nakshatraSpan)

    return NakshatraDetails(
        nakshatra = nakshatra,
        pada = pada,
        degreeInNakshatra = degreeInNakshatra,
        deity = getNakshatraDeity(nakshatra, language),
        symbol = getNakshatraSymbol(nakshatra, language),
        nature = getNakshatraNature(nakshatra),
        gana = getNakshatraGana(nakshatra),
        animal = getNakshatraAnimal(nakshatra, language),
        element = getNakshatraElement(nakshatra),
        caste = getNakshatraCaste(nakshatra),
        direction = getNakshatraDirection(nakshatra, language),
        bodyPart = getNakshatraBodyPart(nakshatra, language),
        gender = getNakshatraGender(nakshatra),
        dosha = getNakshatraDosha(nakshatra),
        qualities = getNakshatraQualities(nakshatra, language),
        strengths = getNakshatraStrengths(nakshatra, language),
        weaknesses = getNakshatraWeaknesses(nakshatra, language),
        careerAptitudes = getNakshatraCareerAptitudes(nakshatra, language),
        mantra = getNakshatraMantra(nakshatra, language)
    )
}

private fun getNakshatraDeity(nakshatra: Nakshatra, language: Language): String = when (nakshatra) {
    Nakshatra.ASHWINI -> StringResources.get(StringKeyNakshatra.DEITY_ASHWINI, language)
    Nakshatra.BHARANI -> StringResources.get(StringKeyNakshatra.DEITY_BHARANI, language)
    Nakshatra.KRITTIKA -> StringResources.get(StringKeyNakshatra.DEITY_KRITTIKA, language)
    Nakshatra.ROHINI -> StringResources.get(StringKeyNakshatra.DEITY_ROHINI, language)
    Nakshatra.MRIGASHIRA -> StringResources.get(StringKeyNakshatra.DEITY_MRIGASHIRA, language)
    Nakshatra.ARDRA -> StringResources.get(StringKeyNakshatra.DEITY_ARDRA, language)
    Nakshatra.PUNARVASU -> StringResources.get(StringKeyNakshatra.DEITY_PUNARVASU, language)
    Nakshatra.PUSHYA -> StringResources.get(StringKeyNakshatra.DEITY_PUSHYA, language)
    Nakshatra.ASHLESHA -> StringResources.get(StringKeyNakshatra.DEITY_ASHLESHA, language)
    Nakshatra.MAGHA -> StringResources.get(StringKeyNakshatra.DEITY_MAGHA, language)
    Nakshatra.PURVA_PHALGUNI -> StringResources.get(StringKeyNakshatra.DEITY_PURVA_PHALGUNI, language)
    Nakshatra.UTTARA_PHALGUNI -> StringResources.get(StringKeyNakshatra.DEITY_UTTARA_PHALGUNI, language)
    Nakshatra.HASTA -> StringResources.get(StringKeyNakshatra.DEITY_HASTA, language)
    Nakshatra.CHITRA -> StringResources.get(StringKeyNakshatra.DEITY_CHITRA, language)
    Nakshatra.SWATI -> StringResources.get(StringKeyNakshatra.DEITY_SWATI, language)
    Nakshatra.VISHAKHA -> StringResources.get(StringKeyNakshatra.DEITY_VISHAKHA, language)
    Nakshatra.ANURADHA -> StringResources.get(StringKeyNakshatra.DEITY_ANURADHA, language)
    Nakshatra.JYESHTHA -> StringResources.get(StringKeyNakshatra.DEITY_JYESHTHA, language)
    Nakshatra.MULA -> StringResources.get(StringKeyNakshatra.DEITY_MULA, language)
    Nakshatra.PURVA_ASHADHA -> StringResources.get(StringKeyNakshatra.DEITY_PURVA_ASHADHA, language)
    Nakshatra.UTTARA_ASHADHA -> StringResources.get(StringKeyNakshatra.DEITY_UTTARA_ASHADHA, language)
    Nakshatra.SHRAVANA -> StringResources.get(StringKeyNakshatra.DEITY_SHRAVANA, language)
    Nakshatra.DHANISHTHA -> StringResources.get(StringKeyNakshatra.DEITY_DHANISHTHA, language)
    Nakshatra.SHATABHISHA -> StringResources.get(StringKeyNakshatra.DEITY_SHATABHISHA, language)
    Nakshatra.PURVA_BHADRAPADA -> StringResources.get(StringKeyNakshatra.DEITY_PURVA_BHADRAPADA, language)
    Nakshatra.UTTARA_BHADRAPADA -> StringResources.get(StringKeyNakshatra.DEITY_UTTARA_BHADRAPADA, language)
    Nakshatra.REVATI -> StringResources.get(StringKeyNakshatra.DEITY_REVATI, language)
}

private fun getNakshatraSymbol(nakshatra: Nakshatra, language: Language): String = when (nakshatra) {
    Nakshatra.ASHWINI -> StringResources.get(StringKeyNakshatra.SYMBOL_ASHWINI, language)
    Nakshatra.BHARANI -> StringResources.get(StringKeyNakshatra.SYMBOL_BHARANI, language)
    Nakshatra.KRITTIKA -> StringResources.get(StringKeyNakshatra.SYMBOL_KRITTIKA, language)
    Nakshatra.ROHINI -> StringResources.get(StringKeyNakshatra.SYMBOL_ROHINI, language)
    Nakshatra.MRIGASHIRA -> StringResources.get(StringKeyNakshatra.SYMBOL_MRIGASHIRA, language)
    Nakshatra.ARDRA -> StringResources.get(StringKeyNakshatra.SYMBOL_ARDRA, language)
    Nakshatra.PUNARVASU -> StringResources.get(StringKeyNakshatra.SYMBOL_PUNARVASU, language)
    Nakshatra.PUSHYA -> StringResources.get(StringKeyNakshatra.SYMBOL_PUSHYA, language)
    Nakshatra.ASHLESHA -> StringResources.get(StringKeyNakshatra.SYMBOL_ASHLESHA, language)
    Nakshatra.MAGHA -> StringResources.get(StringKeyNakshatra.SYMBOL_MAGHA, language)
    Nakshatra.PURVA_PHALGUNI -> StringResources.get(StringKeyNakshatra.SYMBOL_PURVA_PHALGUNI, language)
    Nakshatra.UTTARA_PHALGUNI -> StringResources.get(StringKeyNakshatra.SYMBOL_UTTARA_PHALGUNI, language)
    Nakshatra.HASTA -> StringResources.get(StringKeyNakshatra.SYMBOL_HASTA, language)
    Nakshatra.CHITRA -> StringResources.get(StringKeyNakshatra.SYMBOL_CHITRA, language)
    Nakshatra.SWATI -> StringResources.get(StringKeyNakshatra.SYMBOL_SWATI, language)
    Nakshatra.VISHAKHA -> StringResources.get(StringKeyNakshatra.SYMBOL_VISHAKHA, language)
    Nakshatra.ANURADHA -> StringResources.get(StringKeyNakshatra.SYMBOL_ANURADHA, language)
    Nakshatra.JYESHTHA -> StringResources.get(StringKeyNakshatra.SYMBOL_JYESHTHA, language)
    Nakshatra.MULA -> StringResources.get(StringKeyNakshatra.SYMBOL_MULA, language)
    Nakshatra.PURVA_ASHADHA -> StringResources.get(StringKeyNakshatra.SYMBOL_PURVA_ASHADHA, language)
    Nakshatra.UTTARA_ASHADHA -> StringResources.get(StringKeyNakshatra.SYMBOL_UTTARA_ASHADHA, language)
    Nakshatra.SHRAVANA -> StringResources.get(StringKeyNakshatra.SYMBOL_SHRAVANA, language)
    Nakshatra.DHANISHTHA -> StringResources.get(StringKeyNakshatra.SYMBOL_DHANISHTHA, language)
    Nakshatra.SHATABHISHA -> StringResources.get(StringKeyNakshatra.SYMBOL_SHATABHISHA, language)
    Nakshatra.PURVA_BHADRAPADA -> StringResources.get(StringKeyNakshatra.SYMBOL_PURVA_BHADRAPADA, language)
    Nakshatra.UTTARA_BHADRAPADA -> StringResources.get(StringKeyNakshatra.SYMBOL_UTTARA_BHADRAPADA, language)
    Nakshatra.REVATI -> StringResources.get(StringKeyNakshatra.SYMBOL_REVATI, language)
}

private fun getNakshatraNature(nakshatra: Nakshatra): NakshatraNature = when (nakshatra) {
    Nakshatra.ROHINI, Nakshatra.UTTARA_PHALGUNI, Nakshatra.UTTARA_ASHADHA, Nakshatra.UTTARA_BHADRAPADA -> NakshatraNature.FIXED
    Nakshatra.PUNARVASU, Nakshatra.SWATI, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.SHATABHISHA -> NakshatraNature.MOVEABLE
    Nakshatra.ARDRA, Nakshatra.ASHLESHA, Nakshatra.JYESHTHA, Nakshatra.MULA -> NakshatraNature.SHARP
    Nakshatra.MRIGASHIRA, Nakshatra.CHITRA, Nakshatra.ANURADHA, Nakshatra.REVATI -> NakshatraNature.SOFT
    Nakshatra.KRITTIKA, Nakshatra.VISHAKHA -> NakshatraNature.MIXED
    Nakshatra.ASHWINI, Nakshatra.PUSHYA, Nakshatra.HASTA -> NakshatraNature.LIGHT
    Nakshatra.BHARANI, Nakshatra.MAGHA, Nakshatra.PURVA_PHALGUNI, Nakshatra.PURVA_ASHADHA, Nakshatra.PURVA_BHADRAPADA -> NakshatraNature.FIERCE
}

private fun getNakshatraGana(nakshatra: Nakshatra): NakshatraGana = when (nakshatra) {
    Nakshatra.ASHWINI, Nakshatra.MRIGASHIRA, Nakshatra.PUNARVASU, Nakshatra.PUSHYA,
    Nakshatra.HASTA, Nakshatra.SWATI, Nakshatra.ANURADHA, Nakshatra.SHRAVANA, Nakshatra.REVATI -> NakshatraGana.DEVA
    Nakshatra.BHARANI, Nakshatra.ROHINI, Nakshatra.ARDRA, Nakshatra.PURVA_PHALGUNI,
    Nakshatra.UTTARA_PHALGUNI, Nakshatra.PURVA_ASHADHA, Nakshatra.UTTARA_ASHADHA,
    Nakshatra.PURVA_BHADRAPADA, Nakshatra.UTTARA_BHADRAPADA -> NakshatraGana.MANUSHYA
    Nakshatra.KRITTIKA, Nakshatra.ASHLESHA, Nakshatra.MAGHA, Nakshatra.CHITRA,
    Nakshatra.VISHAKHA, Nakshatra.JYESHTHA, Nakshatra.MULA, Nakshatra.DHANISHTHA, Nakshatra.SHATABHISHA -> NakshatraGana.RAKSHASA
}

private fun getNakshatraAnimal(nakshatra: Nakshatra, language: Language): String = when (nakshatra) {
    Nakshatra.ASHWINI, Nakshatra.SHATABHISHA -> StringResources.get(StringKeyNakshatra.ANIMAL_HORSE, language)
    Nakshatra.BHARANI, Nakshatra.REVATI -> StringResources.get(StringKeyNakshatra.ANIMAL_ELEPHANT, language)
    Nakshatra.KRITTIKA, Nakshatra.PUSHYA -> StringResources.get(StringKeyNakshatra.ANIMAL_SHEEP, language)
    Nakshatra.ROHINI, Nakshatra.MRIGASHIRA -> StringResources.get(StringKeyNakshatra.ANIMAL_SERPENT, language)
    Nakshatra.ARDRA, Nakshatra.MULA -> StringResources.get(StringKeyNakshatra.ANIMAL_DOG, language)
    Nakshatra.PUNARVASU, Nakshatra.ASHLESHA -> StringResources.get(StringKeyNakshatra.ANIMAL_CAT, language)
    Nakshatra.MAGHA, Nakshatra.PURVA_PHALGUNI -> StringResources.get(StringKeyNakshatra.ANIMAL_RAT, language)
    Nakshatra.UTTARA_PHALGUNI, Nakshatra.UTTARA_BHADRAPADA -> StringResources.get(StringKeyNakshatra.ANIMAL_COW, language)
    Nakshatra.HASTA, Nakshatra.SWATI -> StringResources.get(StringKeyNakshatra.ANIMAL_BUFFALO, language)
    Nakshatra.CHITRA, Nakshatra.VISHAKHA -> StringResources.get(StringKeyNakshatra.ANIMAL_TIGER, language)
    Nakshatra.ANURADHA, Nakshatra.JYESHTHA -> StringResources.get(StringKeyNakshatra.ANIMAL_DEER, language)
    Nakshatra.PURVA_ASHADHA, Nakshatra.SHRAVANA -> StringResources.get(StringKeyNakshatra.ANIMAL_MONKEY, language)
    Nakshatra.UTTARA_ASHADHA, Nakshatra.PURVA_BHADRAPADA -> StringResources.get(StringKeyNakshatra.ANIMAL_MONGOOSE, language)
    Nakshatra.DHANISHTHA -> StringResources.get(StringKeyNakshatra.ANIMAL_LION, language)
}

private fun getNakshatraElement(nakshatra: Nakshatra): NakshatraElement = when (nakshatra) {
    Nakshatra.KRITTIKA, Nakshatra.ASHWINI, Nakshatra.BHARANI -> NakshatraElement.FIRE
    Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.HASTA, Nakshatra.UTTARA_PHALGUNI, Nakshatra.UTTARA_ASHADHA, Nakshatra.UTTARA_BHADRAPADA -> NakshatraElement.EARTH
    Nakshatra.ARDRA, Nakshatra.SWATI, Nakshatra.PUNARVASU, Nakshatra.DHANISHTHA, Nakshatra.SHATABHISHA -> NakshatraElement.AIR
    Nakshatra.PUSHYA, Nakshatra.ASHLESHA, Nakshatra.ANURADHA, Nakshatra.JYESHTHA, Nakshatra.PURVA_ASHADHA, Nakshatra.REVATI -> NakshatraElement.WATER
    else -> NakshatraElement.ETHER
}

private fun getNakshatraCaste(nakshatra: Nakshatra): NakshatraCaste = when (nakshatra) {
    Nakshatra.KRITTIKA, Nakshatra.PUSHYA, Nakshatra.UTTARA_PHALGUNI, Nakshatra.VISHAKHA, Nakshatra.UTTARA_ASHADHA, Nakshatra.SHRAVANA, Nakshatra.UTTARA_BHADRAPADA -> NakshatraCaste.BRAHMIN
    Nakshatra.ASHWINI, Nakshatra.PURVA_PHALGUNI, Nakshatra.MULA, Nakshatra.PURVA_ASHADHA, Nakshatra.DHANISHTHA -> NakshatraCaste.KSHATRIYA
    Nakshatra.BHARANI, Nakshatra.ROHINI, Nakshatra.CHITRA, Nakshatra.SWATI, Nakshatra.PURVA_BHADRAPADA -> NakshatraCaste.VAISHYA
    else -> NakshatraCaste.SHUDRA
}

private fun getNakshatraDirection(nakshatra: Nakshatra, language: Language): String = when (nakshatra.number % 4) {
    1 -> StringResources.get(StringKeyAnalysis.PRASHNA_DIR_EAST, language)
    2 -> StringResources.get(StringKeyAnalysis.PRASHNA_DIR_SOUTH, language)
    3 -> StringResources.get(StringKeyAnalysis.PRASHNA_DIR_WEST, language)
    0 -> StringResources.get(StringKeyAnalysis.PRASHNA_DIR_NORTH, language)
    else -> StringResources.get(StringKeyAnalysis.PRASHNA_DIR_NORTH, language)
}

private fun getNakshatraBodyPart(nakshatra: Nakshatra, language: Language): String = when (nakshatra) {
    Nakshatra.ASHWINI, Nakshatra.BHARANI, Nakshatra.KRITTIKA -> StringResources.get(StringKeyNakshatra.BODY_PART_HEAD, language)
    Nakshatra.ROHINI, Nakshatra.MRIGASHIRA, Nakshatra.ARDRA -> StringResources.get(StringKeyNakshatra.BODY_PART_FACE_EYES, language)
    Nakshatra.PUNARVASU, Nakshatra.PUSHYA, Nakshatra.ASHLESHA -> StringResources.get(StringKeyNakshatra.BODY_PART_NECK_SHOULDERS, language)
    Nakshatra.MAGHA, Nakshatra.PURVA_PHALGUNI, Nakshatra.UTTARA_PHALGUNI -> StringResources.get(StringKeyNakshatra.BODY_PART_HEART_BACK, language)
    Nakshatra.HASTA, Nakshatra.CHITRA, Nakshatra.SWATI -> StringResources.get(StringKeyNakshatra.BODY_PART_HANDS_STOMACH, language)
    Nakshatra.VISHAKHA, Nakshatra.ANURADHA, Nakshatra.JYESHTHA -> StringResources.get(StringKeyNakshatra.BODY_PART_LOWER_ABDOMEN, language)
    Nakshatra.MULA, Nakshatra.PURVA_ASHADHA, Nakshatra.UTTARA_ASHADHA -> StringResources.get(StringKeyNakshatra.BODY_PART_THIGHS, language)
    Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.SHATABHISHA -> StringResources.get(StringKeyNakshatra.BODY_PART_KNEES_LEGS, language)
    Nakshatra.PURVA_BHADRAPADA, Nakshatra.UTTARA_BHADRAPADA, Nakshatra.REVATI -> StringResources.get(StringKeyNakshatra.BODY_PART_FEET_ANKLES, language)
}

private fun getNakshatraGender(nakshatra: Nakshatra): NakshatraGender = when (nakshatra.number % 2) {
    1 -> NakshatraGender.MALE
    else -> NakshatraGender.FEMALE
}

private fun getNakshatraDosha(nakshatra: Nakshatra): NakshatraDosha = when (nakshatra) {
    Nakshatra.ASHWINI, Nakshatra.BHARANI, Nakshatra.MRIGASHIRA, Nakshatra.ARDRA,
    Nakshatra.PUNARVASU, Nakshatra.SWATI, Nakshatra.SHRAVANA, Nakshatra.DHANISHTHA, Nakshatra.SHATABHISHA -> NakshatraDosha.VATA
    Nakshatra.KRITTIKA, Nakshatra.ROHINI, Nakshatra.MAGHA, Nakshatra.PURVA_PHALGUNI,
    Nakshatra.CHITRA, Nakshatra.MULA, Nakshatra.PURVA_ASHADHA, Nakshatra.PURVA_BHADRAPADA -> NakshatraDosha.PITTA
    else -> NakshatraDosha.KAPHA
}

private fun getNakshatraQualities(nakshatra: Nakshatra, language: Language): List<String> = when (nakshatra) {
    Nakshatra.ASHWINI -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_QUICK_THINKING, language), StringResources.get(StringKeyNakshatra.TRAIT_HEALING_ABILITIES, language), StringResources.get(StringKeyNakshatra.TRAIT_PIONEERING_SPIRIT, language), StringResources.get(StringKeyNakshatra.TRAIT_YOUTHFUL_ENERGY, language))
    Nakshatra.BHARANI -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_TRANSFORMATIVE, language), StringResources.get(StringKeyNakshatra.TRAIT_CREATIVE, language), StringResources.get(StringKeyNakshatra.TRAIT_STRONG_WILLPOWER, language), StringResources.get(StringKeyNakshatra.TRAIT_BEARING_RESPONSIBILITY, language))
    Nakshatra.KRITTIKA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_FIERY_DETERMINATION, language), StringResources.get(StringKeyNakshatra.TRAIT_SHARP_INTELLECT, language), StringResources.get(StringKeyNakshatra.TRAIT_PURIFYING_NATURE, language), StringResources.get(StringKeyNakshatra.TRAIT_LEADERSHIP, language))
    Nakshatra.ROHINI -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_CREATIVE, language), StringResources.get(StringKeyNakshatra.TRAIT_ARTISTIC, language), StringResources.get(StringKeyNakshatra.TRAIT_MAGNETIC_PERSONALITY, language), StringResources.get(StringKeyNakshatra.TRAIT_MATERIAL_ABUNDANCE, language))
    Nakshatra.MRIGASHIRA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_CURIOUS_MIND, language), StringResources.get(StringKeyNakshatra.TRAIT_SEARCHING_NATURE, language), StringResources.get(StringKeyNakshatra.TRAIT_GENTLE_DEMEANOR, language), StringResources.get(StringKeyNakshatra.TRAIT_ADAPTABLE, language))
    Nakshatra.ARDRA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_TRANSFORMATIVE, language), StringResources.get(StringKeyNakshatra.TRAIT_INTENSE_EMOTIONS, language), StringResources.get(StringKeyNakshatra.TRAIT_DESTRUCTIVE_CREATIVE, language), StringResources.get(StringKeyNakshatra.TRAIT_RESEARCH_ORIENTED, language))
    Nakshatra.PUNARVASU -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_RENEWAL, language), StringResources.get(StringKeyNakshatra.TRAIT_PROSPERITY, language), StringResources.get(StringKeyNakshatra.TRAIT_SPIRITUAL_WISDOM, language), StringResources.get(StringKeyNakshatra.TRAIT_RETURN_TO_ROOTS, language))
    Nakshatra.PUSHYA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_NOURISHING, language), StringResources.get(StringKeyNakshatra.TRAIT_PROTECTIVE, language), StringResources.get(StringKeyNakshatra.TRAIT_SPIRITUAL_GROWTH, language), StringResources.get(StringKeyNakshatra.TRAIT_TEACHING_ABILITY, language))
    Nakshatra.ASHLESHA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_MYSTICAL_WISDOM, language), StringResources.get(StringKeyNakshatra.TRAIT_PSYCHIC_ABILITIES, language), StringResources.get(StringKeyNakshatra.TRAIT_SECRETIVE_NATURE, language), StringResources.get(StringKeyNakshatra.TRAIT_HYPNOTIC_CHARM, language))
    Nakshatra.MAGHA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_ROYAL_BEARING, language), StringResources.get(StringKeyNakshatra.TRAIT_ANCESTRAL_PRIDE, language), StringResources.get(StringKeyNakshatra.TRAIT_LEADERSHIP, language), StringResources.get(StringKeyNakshatra.TRAIT_TRADITIONALIST, language))
    Nakshatra.PURVA_PHALGUNI -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_CREATIVE, language), StringResources.get(StringKeyNakshatra.TRAIT_PLEASURE_SEEKING, language), StringResources.get(StringKeyNakshatra.TRAIT_AFFECTIONATE, language), StringResources.get(StringKeyNakshatra.TRAIT_ARTISTIC_TALENT, language))
    Nakshatra.UTTARA_PHALGUNI -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_SERVICE_ORIENTED, language), StringResources.get(StringKeyNakshatra.TRAIT_HELPFUL_NATURE, language), StringResources.get(StringKeyNakshatra.TRAIT_GOOD_ORGANIZER, language), StringResources.get(StringKeyNakshatra.TRAIT_CONTRACT_KEEPER, language))
    Nakshatra.HASTA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_SKILLED_HANDS, language), StringResources.get(StringKeyNakshatra.TRAIT_CLEVER, language), StringResources.get(StringKeyNakshatra.TRAIT_RESOURCEFUL, language), StringResources.get(StringKeyNakshatra.TRAIT_HEALING_TOUCH, language))
    Nakshatra.CHITRA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_BRILLIANT, language), StringResources.get(StringKeyNakshatra.TRAIT_CREATIVE_GENIUS, language), StringResources.get(StringKeyNakshatra.TRAIT_MAGNETIC, language), StringResources.get(StringKeyNakshatra.TRAIT_ARTISTIC_MASTERY, language))
    Nakshatra.SWATI -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_INDEPENDENT, language), StringResources.get(StringKeyNakshatra.TRAIT_ADAPTABLE, language), StringResources.get(StringKeyNakshatra.TRAIT_BUSINESS_ACUMEN, language), StringResources.get(StringKeyNakshatra.TRAIT_DIPLOMATIC, language))
    Nakshatra.VISHAKHA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_GOAL_ORIENTED, language), StringResources.get(StringKeyNakshatra.TRAIT_DETERMINED, language), StringResources.get(StringKeyNakshatra.TRAIT_AMBITIOUS, language), StringResources.get(StringKeyNakshatra.TRAIT_SINGLE_MINDED_FOCUS, language))
    Nakshatra.ANURADHA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_DEVOTED, language), StringResources.get(StringKeyNakshatra.TRAIT_FRIENDLY, language), StringResources.get(StringKeyNakshatra.TRAIT_ORGANIZATIONAL_ABILITY, language), StringResources.get(StringKeyNakshatra.TRAIT_TRAVEL_LOVING, language))
    Nakshatra.JYESHTHA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_PROTECTIVE, language), StringResources.get(StringKeyNakshatra.TRAIT_ELDER_BROTHER_ENERGY, language), StringResources.get(StringKeyNakshatra.TRAIT_LEADERSHIP, language), StringResources.get(StringKeyNakshatra.TRAIT_RESPONSIBILITY, language))
    Nakshatra.MULA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_INVESTIGATIVE, language), StringResources.get(StringKeyNakshatra.TRAIT_ROOT_SEEKING, language), StringResources.get(StringKeyNakshatra.TRAIT_SPIRITUAL_DEPTH, language), StringResources.get(StringKeyNakshatra.TRAIT_TRANSFORMATION, language))
    Nakshatra.PURVA_ASHADHA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_INVINCIBLE_SPIRIT, language), StringResources.get(StringKeyNakshatra.TRAIT_PHILOSOPHICAL, language), StringResources.get(StringKeyNakshatra.TRAIT_OPTIMISTIC, language), StringResources.get(StringKeyNakshatra.TRAIT_PURIFYING, language))
    Nakshatra.UTTARA_ASHADHA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_FINAL_VICTORY, language), StringResources.get(StringKeyNakshatra.TRAIT_LEADERSHIP, language), StringResources.get(StringKeyNakshatra.TRAIT_INTEGRITY, language), StringResources.get(StringKeyNakshatra.TRAIT_UNIVERSAL_VALUES, language))
    Nakshatra.SHRAVANA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_GOOD_LISTENER, language), StringResources.get(StringKeyNakshatra.TRAIT_LEARNING_ABILITY, language), StringResources.get(StringKeyNakshatra.TRAIT_CONNECTION_TO_TRUTH, language), StringResources.get(StringKeyNakshatra.TRAIT_PRESERVATION, language))
    Nakshatra.DHANISHTHA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_MUSICAL_TALENT, language), StringResources.get(StringKeyNakshatra.TRAIT_WEALTH_GIVING, language), StringResources.get(StringKeyNakshatra.TRAIT_MARCHING_FORWARD, language), StringResources.get(StringKeyNakshatra.TRAIT_GROUP_ACTIVITIES, language))
    Nakshatra.SHATABHISHA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_HEALING_POWERS, language), StringResources.get(StringKeyNakshatra.TRAIT_SECRETIVE_WISDOM, language), StringResources.get(StringKeyNakshatra.TRAIT_RESEARCH_ORIENTED, language), StringResources.get(StringKeyNakshatra.TRAIT_SELF_SUFFICIENT, language))
    Nakshatra.PURVA_BHADRAPADA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_FIERY_PASSION, language), StringResources.get(StringKeyNakshatra.TRAIT_SPIRITUAL_WARRIOR, language), StringResources.get(StringKeyNakshatra.TRAIT_TRANSFORMATION, language), StringResources.get(StringKeyNakshatra.TRAIT_ASCETICISM, language))
    Nakshatra.UTTARA_BHADRAPADA -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_DEEP_MEDITATION, language), StringResources.get(StringKeyNakshatra.TRAIT_KUNDALINI_ENERGY, language), StringResources.get(StringKeyNakshatra.TRAIT_WISDOM, language), StringResources.get(StringKeyNakshatra.TRAIT_CONTROL_OVER_DESIRES, language))
    Nakshatra.REVATI -> listOf(StringResources.get(StringKeyNakshatra.TRAIT_NOURISHING, language), StringResources.get(StringKeyNakshatra.TRAIT_PROTECTIVE_OF_OTHERS, language), StringResources.get(StringKeyNakshatra.TRAIT_JOURNEY_COMPLETION, language), StringResources.get(StringKeyNakshatra.TRAIT_WEALTH_GIVING, language))
}

private fun getNakshatraStrengths(nakshatra: Nakshatra, language: Language): List<String> = when (nakshatra) {
    Nakshatra.ASHWINI -> listOf(StringResources.get(StringKeyNakshatra.STRENGTH_QUICK_HEALER, language), StringResources.get(StringKeyNakshatra.STRENGTH_INITIATIVE, language), StringResources.get(StringKeyNakshatra.STRENGTH_COURAGE, language), StringResources.get(StringKeyNakshatra.STRENGTH_REJUVENATION, language))
    Nakshatra.BHARANI -> listOf(StringResources.get(StringKeyNakshatra.STRENGTH_STRONG_WILL, language), StringResources.get(StringKeyNakshatra.TRAIT_CREATIVE, language), StringResources.get(StringKeyNakshatra.STRENGTH_SELF_DISCIPLINE, language), StringResources.get(StringKeyNakshatra.STRENGTH_ENDURANCE, language))
    Nakshatra.KRITTIKA -> listOf(StringResources.get(StringKeyNakshatra.STRENGTH_SHARP_MIND, language), StringResources.get(StringKeyNakshatra.STRENGTH_PURIFYING_INFLUENCE, language), StringResources.get(StringKeyNakshatra.STRENGTH_COURAGE, language), StringResources.get(StringKeyNakshatra.STRENGTH_FAME_POTENTIAL, language))
    else -> listOf(StringResources.get(StringKeyNakshatra.STRENGTH_DETERMINATION, language), StringResources.get(StringKeyNakshatra.STRENGTH_INTUITION, language), StringResources.get(StringKeyNakshatra.TRAIT_ADAPTABLE, language), StringResources.get(StringKeyNakshatra.STRENGTH_INNER_STRENGTH, language))
}

private fun getNakshatraWeaknesses(nakshatra: Nakshatra, language: Language): List<String> = when (nakshatra) {
    Nakshatra.ASHWINI -> listOf(StringResources.get(StringKeyNakshatra.WEAKNESS_IMPATIENCE, language), StringResources.get(StringKeyNakshatra.WEAKNESS_RECKLESSNESS, language), StringResources.get(StringKeyNakshatra.WEAKNESS_OVERCONFIDENCE, language), StringResources.get(StringKeyNakshatra.WEAKNESS_RESTLESSNESS, language))
    Nakshatra.BHARANI -> listOf(StringResources.get(StringKeyNakshatra.WEAKNESS_STUBBORNNESS, language), StringResources.get(StringKeyNakshatra.WEAKNESS_POSSESSIVENESS, language), StringResources.get(StringKeyNakshatra.WEAKNESS_JEALOUSY, language), StringResources.get(StringKeyNakshatra.WEAKNESS_EXTREMISM, language))
    Nakshatra.KRITTIKA -> listOf(StringResources.get(StringKeyNakshatra.WEAKNESS_HARSH_SPEECH, language), StringResources.get(StringKeyNakshatra.WEAKNESS_ANGER_ISSUES, language), StringResources.get(StringKeyNakshatra.WEAKNESS_CRITICAL_NATURE, language), StringResources.get(StringKeyNakshatra.WEAKNESS_DOMINATING, language))
    else -> listOf(StringResources.get(StringKeyNakshatra.WEAKNESS_OVERTHINKING, language), StringResources.get(StringKeyNakshatra.WEAKNESS_ANXIETY, language), StringResources.get(StringKeyNakshatra.WEAKNESS_SENSITIVITY, language), StringResources.get(StringKeyNakshatra.WEAKNESS_PERFECTIONISM, language))
}

private fun getNakshatraCareerAptitudes(nakshatra: Nakshatra, language: Language): List<String> = when (nakshatra) {
    Nakshatra.ASHWINI -> listOf(StringResources.get(StringKeyNakshatra.CAREER_MEDICINE, language), StringResources.get(StringKeyNakshatra.CAREER_SPORTS, language), StringResources.get(StringKeyNakshatra.CAREER_EMERGENCY_SERVICES, language), StringResources.get(StringKeyNakshatra.CAREER_TRANSPORT, language))
    Nakshatra.BHARANI -> listOf(StringResources.get(StringKeyNakshatra.CAREER_ARTS, language), StringResources.get(StringKeyNakshatra.CAREER_ENTERTAINMENT, language), StringResources.get(StringKeyNakshatra.CAREER_LEGAL_PROFESSION, language), StringResources.get(StringKeyNakshatra.CAREER_PUBLISHING, language))
    Nakshatra.KRITTIKA -> listOf(StringResources.get(StringKeyNakshatra.CAREER_MILITARY, language), StringResources.get(StringKeyNakshatra.CAREER_COOKING, language), StringResources.get(StringKeyNakshatra.CAREER_ENGINEERING, language), StringResources.get(StringKeyNakshatra.CAREER_SURGERY, language))
    Nakshatra.ROHINI -> listOf(StringResources.get(StringKeyNakshatra.CAREER_AGRICULTURE, language), StringResources.get(StringKeyNakshatra.CAREER_FASHION, language), StringResources.get(StringKeyNakshatra.CAREER_BEAUTY_INDUSTRY, language), StringResources.get(StringKeyNakshatra.CAREER_REAL_ESTATE, language))
    Nakshatra.PUSHYA -> listOf(StringResources.get(StringKeyNakshatra.CAREER_TEACHING, language), StringResources.get(StringKeyNakshatra.CAREER_COUNSELING, language), StringResources.get(StringKeyNakshatra.CAREER_CLERGY, language), StringResources.get(StringKeyNakshatra.CAREER_FOOD_INDUSTRY, language))
    else -> listOf(StringResources.get(StringKeyNakshatra.CAREER_MANAGEMENT, language), StringResources.get(StringKeyNakshatra.CAREER_CONSULTING, language), StringResources.get(StringKeyNakshatra.CAREER_TECHNOLOGY, language), StringResources.get(StringKeyNakshatra.CAREER_RESEARCH, language))
}

private fun getNakshatraMantra(nakshatra: Nakshatra, language: Language): String = when (nakshatra) {
    Nakshatra.ASHWINI -> StringResources.get(StringKeyNakshatra.MANTRA_ASHWINI, language)
    Nakshatra.BHARANI -> StringResources.get(StringKeyNakshatra.MANTRA_BHARANI, language)
    Nakshatra.KRITTIKA -> StringResources.get(StringKeyNakshatra.MANTRA_KRITTIKA, language)
    Nakshatra.ROHINI -> StringResources.get(StringKeyNakshatra.MANTRA_ROHINI, language)
    Nakshatra.MRIGASHIRA -> StringResources.get(StringKeyNakshatra.MANTRA_MRIGASHIRA, language)
    Nakshatra.ARDRA -> StringResources.get(StringKeyNakshatra.MANTRA_ARDRA, language)
    Nakshatra.PUNARVASU -> StringResources.get(StringKeyNakshatra.MANTRA_PUNARVASU, language)
    Nakshatra.PUSHYA -> StringResources.get(StringKeyNakshatra.MANTRA_PUSHYA, language)
    Nakshatra.ASHLESHA -> StringResources.get(StringKeyNakshatra.MANTRA_ASHLESHA, language)
    Nakshatra.MAGHA -> StringResources.get(StringKeyNakshatra.MANTRA_MAGHA, language)
    Nakshatra.PURVA_PHALGUNI -> StringResources.get(StringKeyNakshatra.MANTRA_PURVA_PHALGUNI, language)
    Nakshatra.UTTARA_PHALGUNI -> StringResources.get(StringKeyNakshatra.MANTRA_UTTARA_PHALGUNI, language)
    Nakshatra.HASTA -> StringResources.get(StringKeyNakshatra.MANTRA_HASTA, language)
    Nakshatra.CHITRA -> StringResources.get(StringKeyNakshatra.MANTRA_CHITRA, language)
    Nakshatra.SWATI -> StringResources.get(StringKeyNakshatra.MANTRA_SWATI, language)
    Nakshatra.VISHAKHA -> StringResources.get(StringKeyNakshatra.MANTRA_VISHAKHA, language)
    Nakshatra.ANURADHA -> StringResources.get(StringKeyNakshatra.MANTRA_ANURADHA, language)
    Nakshatra.JYESHTHA -> StringResources.get(StringKeyNakshatra.MANTRA_JYESHTHA, language)
    Nakshatra.MULA -> StringResources.get(StringKeyNakshatra.MANTRA_MULA, language)
    Nakshatra.PURVA_ASHADHA -> StringResources.get(StringKeyNakshatra.MANTRA_PURVA_ASHADHA, language)
    Nakshatra.UTTARA_ASHADHA -> StringResources.get(StringKeyNakshatra.MANTRA_UTTARA_ASHADHA, language)
    Nakshatra.SHRAVANA -> StringResources.get(StringKeyNakshatra.MANTRA_SHRAVANA, language)
    Nakshatra.DHANISHTHA -> StringResources.get(StringKeyNakshatra.MANTRA_DHANISHTHA, language)
    Nakshatra.SHATABHISHA -> StringResources.get(StringKeyNakshatra.MANTRA_SHATABHISHA, language)
    Nakshatra.PURVA_BHADRAPADA -> StringResources.get(StringKeyNakshatra.MANTRA_PURVA_BHADRAPADA, language)
    Nakshatra.UTTARA_BHADRAPADA -> StringResources.get(StringKeyNakshatra.MANTRA_UTTARA_BHADRAPADA, language)
    Nakshatra.REVATI -> StringResources.get(StringKeyNakshatra.MANTRA_REVATI, language)
}

private fun calculateNakshatraCompatibility(nakshatra: Nakshatra, language: Language): NakshatraCompatibility {
    // Vedha pairs - nakshatras that create obstruction when used together
    val vedhaPairs = getVedhaPairs(nakshatra)

    // Rajju classification
    val rajjuType = getRajjuType(nakshatra)

    // Calculate Tarabala for all 27 nakshatras
    val tarabalaResults = Nakshatra.entries.map { targetNakshatra ->
        calculateTarabala(nakshatra, targetNakshatra, language)
    }

    val favorable = tarabalaResults.filter { it.isFavorable }.map {
        Nakshatra.entries.find { n -> n.displayName == it.nakshatraName } ?: Nakshatra.ASHWINI
    }

    val unfavorable = tarabalaResults.filter { !it.isFavorable }.map {
        Nakshatra.entries.find { n -> n.displayName == it.nakshatraName } ?: Nakshatra.ASHWINI
    }

    return NakshatraCompatibility(
        vedhaPartners = vedhaPairs,
        rajjuType = rajjuType,
        favorableNakshatras = favorable,
        unfavorableNakshatras = unfavorable,
        tarabalaResults = tarabalaResults
    )
}

private fun getVedhaPairs(nakshatra: Nakshatra): List<Nakshatra> = when (nakshatra) {
    Nakshatra.ASHWINI -> listOf(Nakshatra.JYESHTHA)
    Nakshatra.BHARANI -> listOf(Nakshatra.ANURADHA)
    Nakshatra.KRITTIKA -> listOf(Nakshatra.VISHAKHA)
    Nakshatra.ROHINI -> listOf(Nakshatra.SWATI)
    Nakshatra.ARDRA -> listOf(Nakshatra.SHRAVANA)
    Nakshatra.PUNARVASU -> listOf(Nakshatra.UTTARA_ASHADHA)
    Nakshatra.PUSHYA -> listOf(Nakshatra.PURVA_ASHADHA)
    Nakshatra.ASHLESHA -> listOf(Nakshatra.MULA)
    Nakshatra.MAGHA -> listOf(Nakshatra.REVATI)
    Nakshatra.PURVA_PHALGUNI -> listOf(Nakshatra.UTTARA_BHADRAPADA)
    Nakshatra.UTTARA_PHALGUNI -> listOf(Nakshatra.PURVA_BHADRAPADA)
    Nakshatra.HASTA -> listOf(Nakshatra.SHATABHISHA)
    Nakshatra.CHITRA -> listOf(Nakshatra.DHANISHTHA)
    Nakshatra.SWATI -> listOf(Nakshatra.ROHINI)
    Nakshatra.VISHAKHA -> listOf(Nakshatra.KRITTIKA)
    Nakshatra.ANURADHA -> listOf(Nakshatra.BHARANI)
    Nakshatra.JYESHTHA -> listOf(Nakshatra.ASHWINI)
    Nakshatra.MULA -> listOf(Nakshatra.ASHLESHA)
    Nakshatra.PURVA_ASHADHA -> listOf(Nakshatra.PUSHYA)
    Nakshatra.UTTARA_ASHADHA -> listOf(Nakshatra.PUNARVASU)
    Nakshatra.SHRAVANA -> listOf(Nakshatra.ARDRA)
    Nakshatra.DHANISHTHA -> listOf(Nakshatra.CHITRA)
    Nakshatra.SHATABHISHA -> listOf(Nakshatra.HASTA)
    Nakshatra.PURVA_BHADRAPADA -> listOf(Nakshatra.UTTARA_PHALGUNI)
    Nakshatra.UTTARA_BHADRAPADA -> listOf(Nakshatra.PURVA_PHALGUNI)
    Nakshatra.REVATI -> listOf(Nakshatra.MAGHA)
    Nakshatra.MRIGASHIRA -> emptyList()
}

private fun getRajjuType(nakshatra: Nakshatra): RajjuType = when (nakshatra) {
    Nakshatra.ASHWINI, Nakshatra.ASHLESHA, Nakshatra.MAGHA, Nakshatra.JYESHTHA, Nakshatra.MULA, Nakshatra.REVATI -> RajjuType.PAADA
    Nakshatra.BHARANI, Nakshatra.PUSHYA, Nakshatra.PURVA_PHALGUNI, Nakshatra.ANURADHA, Nakshatra.PURVA_ASHADHA, Nakshatra.UTTARA_BHADRAPADA -> RajjuType.KATI
    Nakshatra.KRITTIKA, Nakshatra.PUNARVASU, Nakshatra.UTTARA_PHALGUNI, Nakshatra.VISHAKHA, Nakshatra.UTTARA_ASHADHA, Nakshatra.PURVA_BHADRAPADA -> RajjuType.NABHI
    Nakshatra.ROHINI, Nakshatra.ARDRA, Nakshatra.HASTA, Nakshatra.SWATI, Nakshatra.SHRAVANA, Nakshatra.SHATABHISHA -> RajjuType.KANTHA
    Nakshatra.MRIGASHIRA, Nakshatra.CHITRA, Nakshatra.DHANISHTHA -> RajjuType.SHIRO
}

private fun calculateTarabala(birthNakshatra: Nakshatra, targetNakshatra: Nakshatra, language: Language): TarabalaResult {
    val birthNum = birthNakshatra.number
    val targetNum = targetNakshatra.number

    // Calculate tara number (1-9 cycle)
    var diff = targetNum - birthNum
    if (diff < 0) diff += 27
    val taraNumber = (diff % 9) + 1

    val (taraName, isFavorable, description) = when (taraNumber) {
        1 -> Triple(StringResources.get(StringKeyDosha.TARABALA_JANMA, language), false, StringResources.get(StringKeyDosha.TARABALA_DESC_JANMA, language))
        2 -> Triple(StringResources.get(StringKeyDosha.TARABALA_SAMPAT, language), true, StringResources.get(StringKeyDosha.TARABALA_DESC_SAMPAT, language))
        3 -> Triple(StringResources.get(StringKeyDosha.TARABALA_VIPAT, language), false, StringResources.get(StringKeyDosha.TARABALA_DESC_VIPAT, language))
        4 -> Triple(StringResources.get(StringKeyDosha.TARABALA_KSHEMA, language), true, StringResources.get(StringKeyDosha.TARABALA_DESC_KSHEMA, language))
        5 -> Triple(StringResources.get(StringKeyDosha.TARABALA_PRATYARI, language), false, StringResources.get(StringKeyDosha.TARABALA_DESC_PRATYARI, language))
        6 -> Triple(StringResources.get(StringKeyDosha.TARABALA_SADHAKA, language), true, StringResources.get(StringKeyDosha.TARABALA_DESC_SADHAKA, language))
        7 -> Triple(StringResources.get(StringKeyDosha.TARABALA_VADHA, language), false, StringResources.get(StringKeyDosha.TARABALA_DESC_VADHA, language))
        8 -> Triple(StringResources.get(StringKeyDosha.TARABALA_MITRA, language), true, StringResources.get(StringKeyDosha.TARABALA_DESC_MITRA, language))
        9 -> Triple(StringResources.get(StringKeyDosha.TARABALA_PARAMA_MITRA, language), true, StringResources.get(StringKeyDosha.TARABALA_DESC_PARAMA_MITRA, language))
        else -> Triple("Unknown", false, "")
    }

    return TarabalaResult(
        nakshatraName = targetNakshatra.displayName, // Nakshatra display name is usually generic, but we may want localized. Keeping displayName for now.
        taraNumber = taraNumber,
        taraName = taraName, // This is English hardcoded in Triple if we use Language.ENGLISH above.
        isFavorable = isFavorable,
        description = description
    )
}

private fun calculateNakshatraRemedy(details: NakshatraDetails, language: Language): NakshatraRemedy {
    val nakshatra = details.nakshatra

    val gemstone = when (nakshatra.ruler) {
        Planet.SUN -> StringResources.get(StringKeyDosha.GEM_RUBY, language)
        Planet.MOON -> StringResources.get(StringKeyDosha.GEM_PEARL, language)
        Planet.MARS -> StringResources.get(StringKeyDosha.GEM_RED_CORAL, language)
        Planet.MERCURY -> StringResources.get(StringKeyDosha.GEM_EMERALD, language)
        Planet.JUPITER -> StringResources.get(StringKeyDosha.GEM_YELLOW_SAPPHIRE, language)
        Planet.VENUS -> StringResources.get(StringKeyDosha.GEM_DIAMOND, language)
        Planet.SATURN -> StringResources.get(StringKeyDosha.GEM_BLUE_SAPPHIRE, language)
        Planet.RAHU -> StringResources.get(StringKeyDosha.GEM_HESSONITE, language)
        Planet.KETU -> StringResources.get(StringKeyDosha.GEM_CATS_EYE, language)
        else -> null
    }

    val favorableDays = when (nakshatra.ruler) {
        Planet.SUN -> listOf(StringResources.get(StringKeyDosha.DAY_SUNDAY, language))
        Planet.MOON -> listOf(StringResources.get(StringKeyDosha.DAY_MONDAY, language))
        Planet.MARS -> listOf(StringResources.get(StringKeyDosha.DAY_TUESDAY, language))
        Planet.MERCURY -> listOf(StringResources.get(StringKeyDosha.DAY_WEDNESDAY, language))
        Planet.JUPITER -> listOf(StringResources.get(StringKeyDosha.DAY_THURSDAY, language))
        Planet.VENUS -> listOf(StringResources.get(StringKeyDosha.DAY_FRIDAY, language))
        Planet.SATURN -> listOf(StringResources.get(StringKeyDosha.DAY_SATURDAY, language))
        Planet.RAHU -> listOf(StringResources.get(StringKeyDosha.DAY_SATURDAY, language))
        Planet.KETU -> listOf(StringResources.get(StringKeyDosha.DAY_TUESDAY, language))
        else -> listOf(StringResources.get(StringKeyDosha.DAY_ANY, language))
    }

    // Format title and description with localized values
    val title = String.format(StringResources.get(StringKeyDosha.NAKSHATRA_REMEDY_TITLE, language), nakshatra.getLocalizedName(language))
    val description = StringResources.get(StringKeyDosha.NAKSHATRA_REMEDY_DESC, language)
    val timing = String.format(
        StringResources.get(StringKeyDosha.NAKSHATRA_REMEDY_TIMING, language),
        nakshatra.ruler.getLocalizedName(language),
        favorableDays.first()
    )

    return NakshatraRemedy(
        title = title,
        description = description,
        mantra = details.mantra,
        timing = timing,
        gemstone = gemstone,
        deity = details.deity,
        favorableDays = favorableDays,
        luckyNumbers = getLuckyNumbers(nakshatra),
        luckyColors = getLuckyColors(nakshatra, language)
    )
}

private fun getLuckyNumbers(nakshatra: Nakshatra): List<Int> = when (nakshatra.ruler) {
    Planet.SUN -> listOf(1, 4, 10)
    Planet.MOON -> listOf(2, 7, 11)
    Planet.MARS -> listOf(3, 9, 18)
    Planet.MERCURY -> listOf(5, 14, 23)
    Planet.JUPITER -> listOf(3, 12, 21)
    Planet.VENUS -> listOf(6, 15, 24)
    Planet.SATURN -> listOf(8, 17, 26)
    Planet.RAHU -> listOf(4, 13, 22)
    Planet.KETU -> listOf(7, 16, 25)
    else -> listOf(1, 5, 9)
}

private fun getLuckyColors(nakshatra: Nakshatra, language: Language): List<String> = when (nakshatra.ruler) {
    Planet.SUN -> listOf(StringResources.get(StringKeyDosha.COLOR_ORANGE, language), StringResources.get(StringKeyDosha.COLOR_GOLD, language), StringResources.get(StringKeyDosha.COLOR_RED, language))
    Planet.MOON -> listOf(StringResources.get(StringKeyDosha.COLOR_WHITE, language), StringResources.get(StringKeyDosha.COLOR_SILVER, language), StringResources.get(StringKeyDosha.COLOR_PEARL, language)) // Note: Pearl used as color too
    Planet.MARS -> listOf(StringResources.get(StringKeyDosha.COLOR_RED, language), StringResources.get(StringKeyDosha.COLOR_CORAL, language), StringResources.get(StringKeyDosha.COLOR_SCARLET, language))
    Planet.MERCURY -> listOf(StringResources.get(StringKeyDosha.COLOR_GREEN, language), StringResources.get(StringKeyDosha.COLOR_EMERALD, language), StringResources.get(StringKeyDosha.COLOR_TURQUOISE, language))
    Planet.JUPITER -> listOf(StringResources.get(StringKeyDosha.COLOR_YELLOW, language), StringResources.get(StringKeyDosha.COLOR_GOLD, language), StringResources.get(StringKeyDosha.COLOR_ORANGE, language))
    Planet.VENUS -> listOf(StringResources.get(StringKeyDosha.COLOR_WHITE, language), StringResources.get(StringKeyDosha.COLOR_PINK, language), StringResources.get(StringKeyDosha.COLOR_PASTEL, language))
    Planet.SATURN -> listOf(StringResources.get(StringKeyDosha.COLOR_BLUE, language), StringResources.get(StringKeyDosha.COLOR_BLACK, language), StringResources.get(StringKeyDosha.COLOR_DARK, language))
    Planet.RAHU -> listOf(StringResources.get(StringKeyDosha.COLOR_SMOKY, language), StringResources.get(StringKeyDosha.COLOR_GREY, language), StringResources.get(StringKeyDosha.COLOR_MIXED, language))
    Planet.KETU -> listOf(StringResources.get(StringKeyDosha.COLOR_GREY, language), StringResources.get(StringKeyDosha.COLOR_BROWN, language), StringResources.get(StringKeyDosha.COLOR_MULTI, language))
    else -> listOf(StringResources.get(StringKeyDosha.COLOR_WHITE, language), StringResources.get(StringKeyDosha.COLOR_YELLOW, language), StringResources.get(StringKeyDosha.COLOR_BLUE, language))
}

// ============================================
// UI Components
// ============================================

@Composable
private fun NakshatraTabSelector(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tabs.size) { index ->
            val isSelected = selectedTab == index
            com.astro.storm.ui.components.common.NeoVedicChoicePill(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                label = {
                    Text(
                        tabs[index],
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.15f),
                    selectedLabelColor = AppTheme.AccentPrimary,
                    containerColor = AppTheme.ChipBackground,
                    labelColor = AppTheme.TextSecondary
                )
            )
        }
    }
}

@Composable
private fun NakshatraOverviewTab(
    analysis: NakshatraAnalysis,
    chart: VedicChart,
    language: Language
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Birth Nakshatra Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(StringKeyDosha.NAKSHATRA_BIRTH_STAR),
                    style = MaterialTheme.typography.titleSmall,
                    color = AppTheme.TextMuted
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    analysis.birthNakshatra.nakshatra.getLocalizedName(language),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )

                Text(
                    "${stringResource(StringKeyAnalysis.NAKSHATRA_PADA)} ${analysis.birthNakshatra.pada}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppTheme.AccentPrimary
                )

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(color = AppTheme.BorderColor.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NakshatraInfoChip(
                        label = stringResource(StringKeyDosha.NAKSHATRA_RULER),
                        value = analysis.birthNakshatra.nakshatra.ruler.getLocalizedName(language),
                        icon = Icons.Filled.Star
                    )
                    NakshatraInfoChip(
                        label = stringResource(StringKeyAnalysis.NAKSHATRA_GANA),
                        value = stringResource(analysis.birthNakshatra.gana.key),
                        icon = Icons.Filled.Groups
                    )
                    NakshatraInfoChip(
                        label = stringResource(StringKeyDosha.NAKSHATRA_ELEMENT),
                        value = analysis.birthNakshatra.element.getLocalizedName(language),
                        icon = Icons.Filled.Public
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Deity and Symbol Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardElevated),
            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                NakshatraDetailRow(
                    label = stringResource(StringKeyAnalysis.NAKSHATRA_DEITY),
                    value = analysis.birthNakshatra.deity
                )
                Spacer(modifier = Modifier.height(8.dp))
                NakshatraDetailRow(
                    label = stringResource(StringKeyAnalysis.NAKSHATRA_SYMBOL),
                    value = analysis.birthNakshatra.symbol
                )
                Spacer(modifier = Modifier.height(8.dp))
                NakshatraDetailRow(
                    label = stringResource(StringKeyAnalysis.NAKSHATRA_NATURE),
                    value = analysis.birthNakshatra.nature.getLocalizedName(language)
                )
                Spacer(modifier = Modifier.height(8.dp))
                NakshatraDetailRow(
                    label = stringResource(StringKeyAnalysis.NAKSHATRA_ANIMAL),
                    value = analysis.birthNakshatra.animal
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Characteristics
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    stringResource(StringKeyDosha.NAKSHATRA_CHARACTERISTICS),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                analysis.birthNakshatra.qualities.forEach { quality ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = AppTheme.SuccessColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            quality,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NakshatraInfoChip(
    label: String,
    value: String,
    icon: ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(AppTheme.AccentPrimary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = AppTheme.AccentPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextSubtle
        )
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun NakshatraDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextMuted
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun NakshatraDetailsTab(
    analysis: NakshatraAnalysis,
    expandedPlanet: Planet?,
    onExpandPlanet: (Planet) -> Unit,
    language: Language
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            stringResource(StringKeyDosha.NAKSHATRA_ALL_PLANETS),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        analysis.planetaryNakshatras.forEach { planetary ->
            PlanetaryNakshatraCard(
                planetary = planetary,
                isExpanded = expandedPlanet == planetary.planet,
                onToggle = { onExpandPlanet(planetary.planet) },
                language = language
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PlanetaryNakshatraCard(
    planetary: PlanetaryNakshatra,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    language: Language
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300),
        label = "rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            ),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AppTheme.getPlanetColor(planetary.planet).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            planetary.planet.symbol,
                            style = MaterialTheme.typography.titleMedium,
                            color = AppTheme.getPlanetColor(planetary.planet)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            planetary.planet.getLocalizedName(language),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            "${planetary.details.nakshatra.getLocalizedName(language)} (Pada ${planetary.details.pada})",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Icon(
                    Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotationAngle)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = AppTheme.BorderColor.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    NakshatraDetailRow(
                        label = stringResource(StringKeyAnalysis.NAKSHATRA_DEITY),
                        value = planetary.details.deity
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    NakshatraDetailRow(
                        label = stringResource(StringKeyDosha.NAKSHATRA_DASHA_LORD),
                        value = planetary.details.nakshatra.ruler.getLocalizedName(language)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    NakshatraDetailRow(
                        label = stringResource(StringKeyDosha.NAKSHATRA_DEGREE_IN),
                        value = String.format("%.2f\u00B0", planetary.details.degreeInNakshatra)
                    )
                }
            }
        }
    }
}

@Composable
private fun NakshatraCompatibilityTab(
    analysis: NakshatraAnalysis,
    language: Language
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Tarabala section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    stringResource(StringKeyDosha.NAKSHATRA_TARABALA),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Show favorable and unfavorable count
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TarabalaSummaryChip(
                        label = stringResource(StringKeyDosha.NAKSHATRA_COMPATIBLE_WITH),
                        count = analysis.compatibility.favorableNakshatras.size,
                        color = AppTheme.SuccessColor
                    )
                    TarabalaSummaryChip(
                        label = stringResource(StringKeyDosha.NAKSHATRA_INCOMPATIBLE_WITH),
                        count = analysis.compatibility.unfavorableNakshatras.size,
                        color = AppTheme.WarningColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rajju
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardElevated),
            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    stringResource(StringKeyDosha.NAKSHATRA_RAJJU_TYPE),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AppTheme.AccentTeal.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = null,
                            tint = AppTheme.AccentTeal
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            analysis.compatibility.rajjuType.getLocalizedName(language),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            "${stringResource(StringKeyDosha.NAKSHATRA_BODY_PART_HEAD)}: ${analysis.compatibility.rajjuType.getLocalizedBodyPart(language)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Vedha Pairs
        if (analysis.compatibility.vedhaPartners.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.WarningColor.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = null,
                            tint = AppTheme.WarningColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(StringKeyDosha.NAKSHATRA_VEDHA_PAIRS),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "Avoid pairing with: ${analysis.compatibility.vedhaPartners.joinToString { it.getLocalizedName(language) }}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun TarabalaSummaryChip(
    label: String,
    count: Int,
    color: Color
) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                count.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = color.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun NakshatraRemediesTab(
    analysis: NakshatraAnalysis,
    language: Language
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Mantra Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.AccentGold.copy(alpha = 0.08f)),
            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    stringResource(StringKeyDosha.NAKSHATRA_MANTRA),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    analysis.remedy.mantra,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.AccentGold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    analysis.remedy.timing,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gemstone
        if (analysis.remedy.gemstone != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(AppTheme.AccentPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Diamond,
                            contentDescription = null,
                            tint = AppTheme.AccentPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            stringResource(StringKeyDosha.NAKSHATRA_LUCKY_STONES),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                        Text(
                            analysis.remedy.gemstone,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lucky Numbers and Colors
        Row(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardElevated),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        stringResource(StringKeyDosha.NAKSHATRA_LUCKY_NUMBERS),
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        analysis.remedy.luckyNumbers.joinToString(", "),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardElevated),
                shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        stringResource(StringKeyDosha.NAKSHATRA_LUCKY_COLORS),
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        analysis.remedy.luckyColors.take(2).joinToString(", "),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Favorable Days
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    stringResource(StringKeyDosha.NAKSHATRA_FAVORABLE_DAYS),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    analysis.remedy.favorableDays.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }
        }
    }
}

@Composable
private fun NakshatraInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(StringKeyDosha.NAKSHATRA_INFO_TITLE),
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                stringResource(StringKeyDosha.NAKSHATRA_INFO_DESC),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(StringKey.BTN_CLOSE), color = AppTheme.AccentGold)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}







