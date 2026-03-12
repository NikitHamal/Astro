package com.astro.vajra.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyAdvanced
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.data.localization.LocalLanguage
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import com.astro.vajra.ephemeris.kp.KpAnalysisResult
import com.astro.vajra.ephemeris.kp.KpCuspDetail
import com.astro.vajra.ephemeris.kp.KpHoraryNumber
import com.astro.vajra.ephemeris.kp.KpHouseLink
import com.astro.vajra.ephemeris.kp.KpHouseSignificators
import com.astro.vajra.ephemeris.kp.KpLinkSource
import com.astro.vajra.ephemeris.kp.KpPlanetDetail
import com.astro.vajra.ephemeris.kp.KpRulingPlanets
import com.astro.vajra.ui.components.common.ExpandableCard
import com.astro.vajra.ui.components.common.ModernCard
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.NeoVedicEmptyState
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import com.astro.vajra.ui.components.common.NeoVedicStatRow
import com.astro.vajra.ui.components.common.NeoVedicStatusPill
import com.astro.vajra.ui.components.common.TabItem
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.viewmodel.KpSystemUiState
import com.astro.vajra.ui.viewmodel.KpSystemViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KpSystemScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    viewModel: KpSystemViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val zoneId = remember(chart?.birthData?.timezone) {
        com.astro.vajra.util.TimezoneSanitizer.resolveZoneIdOrNull(chart?.birthData?.timezone)
            ?: ZoneId.systemDefault()
    }

    var analysisMoment by remember(chart?.id, zoneId) {
        mutableStateOf(currentMoment(zoneId))
    }
    var selectedTab by rememberSaveable(chart?.id) { mutableIntStateOf(0) }
    var horaryInput by rememberSaveable(chart?.id) { mutableStateOf("1") }

    val requestKey = remember(chart?.id, analysisMoment) {
        buildString {
            append(chart?.id ?: -1L)
            append('|')
            append(analysisMoment)
        }
    }

    LaunchedEffect(requestKey) {
        viewModel.loadKpSystem(chart, analysisMoment)
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyAdvanced.KP_TITLE),
                subtitle = chart?.birthData?.name ?: stringResource(StringKey.PREDICTIONS_NO_CHART_SELECTED),
                onBack = onBack,
                actionIcon = Icons.Outlined.Refresh,
                onAction = { analysisMoment = currentMoment(zoneId) }
            )
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = uiState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "KpSystemState"
        ) { state ->
            when (state) {
                is KpSystemUiState.Idle -> {
                    NeoVedicEmptyState(
                        title = stringResource(StringKeyAdvanced.KP_TITLE),
                        subtitle = stringResource(StringKey.PREDICTIONS_SELECT_CHART_MESSAGE),
                        icon = Icons.Outlined.Tune,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
                is KpSystemUiState.Loading -> {
                    LoadingContent(paddingValues)
                }
                is KpSystemUiState.Error -> {
                    ErrorContent(
                        paddingValues = paddingValues,
                        message = state.message,
                        onRetry = { viewModel.loadKpSystem(chart, analysisMoment) }
                    )
                }
                is KpSystemUiState.Success -> {
                    val tabs = listOf(
                        TabItem(stringResource(StringKeyAdvanced.KP_TAB_OVERVIEW), Icons.Outlined.AutoAwesome, accentColor = AppTheme.AccentGold),
                        TabItem(stringResource(StringKeyAdvanced.KP_TAB_CUSPS), Icons.Outlined.GridView, accentColor = AppTheme.AccentPrimary),
                        TabItem(stringResource(StringKeyAdvanced.KP_TAB_PLANETS), Icons.Outlined.Public, accentColor = AppTheme.AccentTeal),
                        TabItem(stringResource(StringKeyAdvanced.KP_TAB_SIGNIFICATORS), Icons.Outlined.Home, accentColor = AppTheme.LifeAreaCareer),
                        TabItem(stringResource(StringKeyAdvanced.KP_TAB_NUMBERS), Icons.Outlined.Calculate, accentColor = AppTheme.LifeAreaSpiritual)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        ModernPillTabRow(
                            tabs = tabs,
                            selectedIndex = selectedTab,
                            onTabSelected = { selectedTab = it },
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        when (selectedTab) {
                            0 -> KpOverviewTab(state.result, analysisMoment, zoneId)
                            1 -> KpCuspsTab(state.result)
                            2 -> KpPlanetsTab(state.result)
                            3 -> KpSignificatorsTab(state.result)
                            else -> KpNumbersTab(
                                result = state.result,
                                input = horaryInput,
                                onInputChange = { horaryInput = it }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentGold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyAdvanced.KP_LOADING),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary
            )
        }
    }
}

@Composable
private fun ErrorContent(
    paddingValues: PaddingValues,
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(StringKeyAdvanced.KP_ERROR_TITLE),
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(text = stringResource(StringKeyAdvanced.COMMON_RETRY))
            }
        }
    }
}

@Composable
private fun KpOverviewTab(
    result: KpAnalysisResult,
    analysisMoment: LocalDateTime,
    zoneId: ZoneId
) {
    val language = LocalLanguage.current
    val moonDetail = result.planetDetails.firstOrNull { it.position.planet == Planet.MOON }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            ModernCard {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(StringKeyAdvanced.KP_PRESET_TITLE),
                        style = MaterialTheme.typography.titleMedium,
                        color = AppTheme.TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    NeoVedicStatRow(
                        label = stringResource(StringKeyAdvanced.KP_PRESET_AYANAMSA),
                        value = result.configuration.ayanamsa.name
                    )
                    NeoVedicStatRow(
                        label = stringResource(StringKeyAdvanced.KP_PRESET_HOUSES),
                        value = result.configuration.houseSystem.displayName
                    )
                    NeoVedicStatRow(
                        label = stringResource(StringKeyAdvanced.KP_PRESET_NODES),
                        value = result.configuration.nodeMode.name
                    )
                    NeoVedicStatRow(
                        label = stringResource(StringKeyAdvanced.KP_ANALYSIS_MOMENT),
                        value = formatMoment(analysisMoment, zoneId)
                    )
                }
            }
        }

        item {
            ModernCard {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(StringKeyAdvanced.KP_CHART_SUMMARY),
                        style = MaterialTheme.typography.titleMedium,
                        color = AppTheme.TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    NeoVedicStatRow(
                        label = stringResource(StringKeyAdvanced.KP_ASCENDANT),
                        value = formatLongitude(result.kpChart.ascendant)
                    )
                    NeoVedicStatRow(
                        label = stringResource(StringKeyAdvanced.KP_MIDHEAVEN),
                        value = formatLongitude(result.kpChart.midheaven)
                    )
                    NeoVedicStatRow(
                        label = stringResource(StringKeyAdvanced.KP_AYANAMSA_VALUE),
                        value = formatAbsoluteLongitude(result.kpChart.ayanamsa)
                    )
                    moonDetail?.let {
                        NeoVedicStatRow(
                            label = stringResource(StringKeyAdvanced.KP_MOON_STAR_SUB),
                            value = buildString {
                                append(it.subdivision.nakshatra.getLocalizedName(language))
                                append(" • ")
                                append(it.subdivision.starLord.getLocalizedName(language))
                                append(" / ")
                                append(it.subdivision.subLord.getLocalizedName(language))
                            }
                        )
                    }
                    NeoVedicStatRow(
                        label = stringResource(StringKeyAdvanced.KP_HORARY_RANGE),
                        value = "1-${result.horaryNumbers.size}"
                    )
                }
            }
        }

        item {
            RulingPlanetsCard(
                title = stringResource(StringKeyAdvanced.KP_BIRTH_RULING_PLANETS),
                rulingPlanets = result.birthRulingPlanets
            )
        }

        item {
            RulingPlanetsCard(
                title = stringResource(StringKeyAdvanced.KP_CURRENT_RULING_PLANETS),
                rulingPlanets = result.analysisRulingPlanets
            )
        }
    }
}

@Composable
private fun KpCuspsTab(result: KpAnalysisResult) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(result.cuspDetails, key = { it.house }) { cusp ->
            ExpandableCard(
                title = stringResource(StringKeyAdvanced.KP_CUSP_TITLE, cusp.house),
                subtitle = buildString {
                    append(formatLongitude(cusp.longitude))
                    append(" • ")
                    append(cusp.subdivision.starLord.getLocalizedName(LocalLanguage.current))
                    append(" / ")
                    append(cusp.subdivision.subLord.getLocalizedName(LocalLanguage.current))
                },
                icon = Icons.Outlined.GridView,
                iconTint = AppTheme.AccentPrimary,
                isExpandedInitially = cusp.house == 1
            ) {
                KpCuspContent(cusp)
            }
        }
    }
}

@Composable
private fun KpPlanetsTab(result: KpAnalysisResult) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(result.planetDetails, key = { it.position.planet }) { detail ->
            ExpandableCard(
                title = detail.position.planet.getLocalizedName(LocalLanguage.current),
                subtitle = buildString {
                    append(formatLongitude(detail.position.longitude))
                    append(" • ")
                    append(stringResource(StringKeyAdvanced.KP_HOUSE_LABEL, detail.position.house))
                },
                icon = Icons.Outlined.Public,
                iconTint = AppTheme.AccentTeal,
                isExpandedInitially = detail.position.planet == Planet.MOON
            ) {
                KpPlanetContent(detail)
            }
        }
    }
}

@Composable
private fun KpSignificatorsTab(result: KpAnalysisResult) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(result.houseSignificators, key = { it.house }) { significators ->
            ExpandableCard(
                title = stringResource(StringKeyAdvanced.KP_HOUSE_SIGNIFICATORS_TITLE, significators.house),
                subtitle = planetListText(significators.principalSignificators),
                icon = Icons.Outlined.Home,
                iconTint = AppTheme.LifeAreaCareer,
                isExpandedInitially = significators.house == 10
            ) {
                KpHouseSignificatorContent(significators)
            }
        }
    }
}

@Composable
private fun KpNumbersTab(
    result: KpAnalysisResult,
    input: String,
    onInputChange: (String) -> Unit
) {
    val selectedNumber = input.toIntOrNull()?.coerceIn(1, result.horaryNumbers.size)
    val selectedSegment = remember(selectedNumber, result.horaryNumbers) {
        selectedNumber?.let { number -> result.horaryNumbers.firstOrNull { it.number == number } }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            ModernCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = stringResource(StringKeyAdvanced.KP_NUMBER_LOOKUP_TITLE),
                        style = MaterialTheme.typography.titleMedium,
                        color = AppTheme.TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(StringKeyAdvanced.KP_NUMBER_LOOKUP_DESC),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextSecondary
                    )
                    OutlinedTextField(
                        value = input,
                        onValueChange = { value ->
                            onInputChange(value.filter { it.isDigit() }.take(3))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(StringKeyAdvanced.KP_NUMBER_INPUT_LABEL)) },
                        singleLine = true
                    )
                    selectedSegment?.let { segment ->
                        SelectedNumberCard(segment)
                    }
                }
            }
        }

        items(result.horaryNumbers, key = { it.number }) { number ->
            ExpandableCard(
                title = stringResource(StringKeyAdvanced.KP_NUMBER_TITLE, number.number),
                subtitle = buildString {
                    append(formatLongitude(number.startLongitude))
                    append(" → ")
                    append(formatLongitude(number.endLongitude.coerceAtMost(359.999999)))
                },
                icon = Icons.Outlined.Calculate,
                iconTint = if (selectedNumber == number.number) AppTheme.AccentGold else AppTheme.LifeAreaSpiritual,
                isExpandedInitially = selectedNumber == number.number
            ) {
                SelectedNumberCard(number)
            }
        }
    }
}

@Composable
private fun RulingPlanetsCard(
    title: String,
    rulingPlanets: KpRulingPlanets
) {
    val language = LocalLanguage.current
    ModernCard {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            NeoVedicStatRow(
                label = stringResource(StringKeyAdvanced.KP_WEEKDAY_LORD),
                value = rulingPlanets.weekdayLord.getLocalizedName(language)
            )
            NeoVedicStatRow(
                label = stringResource(StringKeyAdvanced.KP_MOON_RULERS),
                value = buildString {
                    append(rulingPlanets.moonSignLord.getLocalizedName(language))
                    append(" • ")
                    append(rulingPlanets.moonStarLord.getLocalizedName(language))
                    append(" • ")
                    append(rulingPlanets.moonSubLord.getLocalizedName(language))
                }
            )
            NeoVedicStatRow(
                label = stringResource(StringKeyAdvanced.KP_LAGNA_RULERS),
                value = buildString {
                    append(rulingPlanets.lagnaSignLord.getLocalizedName(language))
                    append(" • ")
                    append(rulingPlanets.lagnaStarLord.getLocalizedName(language))
                    append(" • ")
                    append(rulingPlanets.lagnaSubLord.getLocalizedName(language))
                }
            )
            Text(
                text = stringResource(StringKeyAdvanced.KP_ORDERED_RULING_PLANETS),
                style = MaterialTheme.typography.labelLarge,
                color = AppTheme.TextSecondary
            )
            PlanetPillRow(rulingPlanets.ordered)
        }
    }
}

@Composable
private fun KpCuspContent(cusp: KpCuspDetail) {
    val language = LocalLanguage.current
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        NeoVedicStatRow(
            label = stringResource(StringKeyAdvanced.KP_SIGN_LORD),
            value = cusp.signLord.getLocalizedName(language)
        )
        NeoVedicStatRow(
            label = stringResource(StringKeyAdvanced.KP_STAR_LORD),
            value = cusp.subdivision.starLord.getLocalizedName(language)
        )
        NeoVedicStatRow(
            label = stringResource(StringKeyAdvanced.KP_SUB_LORD),
            value = cusp.subdivision.subLord.getLocalizedName(language)
        )
        NeoVedicStatRow(
            label = stringResource(StringKeyAdvanced.KP_SUB_SUB_LORD),
            value = cusp.subdivision.subSubLord.getLocalizedName(language)
        )
        LabeledPlanetRow(
            label = stringResource(StringKeyAdvanced.KP_OCCUPANTS),
            planets = cusp.occupants
        )
        HousePillSection(
            label = stringResource(StringKeyAdvanced.KP_CUSP_SUB_LINKS),
            houses = cusp.subLordLinkedHouses
        )
    }
}

@Composable
private fun KpPlanetContent(detail: KpPlanetDetail) {
    val language = LocalLanguage.current
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        NeoVedicStatRow(
            label = stringResource(StringKeyAdvanced.KP_SIGN_LORD),
            value = detail.signLord.getLocalizedName(language)
        )
        NeoVedicStatRow(
            label = stringResource(StringKeyAdvanced.KP_STAR_LORD),
            value = detail.subdivision.starLord.getLocalizedName(language)
        )
        NeoVedicStatRow(
            label = stringResource(StringKeyAdvanced.KP_SUB_LORD),
            value = detail.subdivision.subLord.getLocalizedName(language)
        )
        NeoVedicStatRow(
            label = stringResource(StringKeyAdvanced.KP_SUB_SUB_LORD),
            value = detail.subdivision.subSubLord.getLocalizedName(language)
        )
        NeoVedicStatRow(
            label = stringResource(StringKeyAdvanced.KP_OWNED_HOUSES),
            value = houseListText(detail.ownedHouses)
        )
        HousePillSection(
            label = stringResource(StringKeyAdvanced.KP_STAR_LINKS),
            houses = detail.starLinkedHouses
        )
        HousePillSection(
            label = stringResource(StringKeyAdvanced.KP_SUB_LINKS),
            houses = detail.subLinkedHouses
        )
        Text(
            text = stringResource(StringKeyAdvanced.KP_FINAL_SIGNIFICATIONS),
            style = MaterialTheme.typography.labelLarge,
            color = AppTheme.TextSecondary
        )
        if (detail.finalSignifiedHouses.isEmpty()) {
            Text(
                text = stringResource(StringKeyAdvanced.KP_NONE),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary
            )
        } else {
            val language = LocalLanguage.current
            detail.finalSignifiedHouses.forEach { link ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(StringKeyAdvanced.KP_HOUSE_LABEL, link.house),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = link.sources.joinToString(" • ") { source -> sourceLabel(source, language) },
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.AccentGold
                    )
                }
            }
        }
    }
}

@Composable
private fun KpHouseSignificatorContent(significators: KpHouseSignificators) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        LabeledPlanetRow(
            label = stringResource(StringKeyAdvanced.KP_STAR_OF_OCCUPANTS),
            planets = significators.starOfOccupants
        )
        LabeledPlanetRow(
            label = stringResource(StringKeyAdvanced.KP_OCCUPANTS),
            planets = significators.occupants
        )
        LabeledPlanetRow(
            label = stringResource(StringKeyAdvanced.KP_STAR_OF_OWNERS),
            planets = significators.starOfOwners
        )
        LabeledPlanetRow(
            label = stringResource(StringKeyAdvanced.KP_OWNERS),
            planets = significators.owners
        )
        NeoVedicStatRow(
            label = stringResource(StringKeyAdvanced.KP_CUSP_STAR_LORD),
            value = significators.cuspStarLord.getLocalizedName(LocalLanguage.current)
        )
        NeoVedicStatRow(
            label = stringResource(StringKeyAdvanced.KP_CUSP_SUB_LORD),
            value = significators.cuspSubLord.getLocalizedName(LocalLanguage.current)
        )
        HousePillSection(
            label = stringResource(StringKeyAdvanced.KP_CUSP_SUB_LINKS),
            houses = significators.cuspSubLordLinkedHouses
        )
    }
}

@Composable
private fun SelectedNumberCard(number: KpHoraryNumber) {
    val language = LocalLanguage.current
    ModernCard {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = stringResource(StringKeyAdvanced.KP_NUMBER_TITLE, number.number),
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            NeoVedicStatRow(
                label = stringResource(StringKeyAdvanced.KP_NUMBER_RANGE),
                value = "${formatLongitude(number.startLongitude)} → ${formatLongitude(number.endLongitude.coerceAtMost(359.999999))}"
            )
            NeoVedicStatRow(
                label = stringResource(StringKeyAdvanced.KP_SIGN_LORD),
                value = number.subdivision.sign.ruler.getLocalizedName(language)
            )
            NeoVedicStatRow(
                label = stringResource(StringKeyAdvanced.KP_STAR_LORD),
                value = number.subdivision.starLord.getLocalizedName(language)
            )
            NeoVedicStatRow(
                label = stringResource(StringKeyAdvanced.KP_SUB_LORD),
                value = number.subdivision.subLord.getLocalizedName(language)
            )
            NeoVedicStatRow(
                label = stringResource(StringKeyAdvanced.KP_SUB_SUB_LORD),
                value = number.subdivision.subSubLord.getLocalizedName(language)
            )
            NeoVedicStatRow(
                label = stringResource(StringKeyAdvanced.KP_NAKSHATRA),
                value = number.subdivision.nakshatra.getLocalizedName(language)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PlanetPillRow(planets: List<Planet>) {
    val language = LocalLanguage.current
    if (planets.isEmpty()) {
        Text(
            text = stringResource(StringKeyAdvanced.KP_NONE),
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextSecondary
        )
        return
    }

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        planets.forEach { planet ->
            NeoVedicStatusPill(
                text = planet.getLocalizedName(language),
                textColor = AppTheme.AccentGold,
                containerColor = AppTheme.AccentGold.copy(alpha = 0.14f)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HousePillSection(
    label: String,
    houses: List<Int>
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = AppTheme.TextSecondary
        )
        if (houses.isEmpty()) {
            Text(
                text = stringResource(StringKeyAdvanced.KP_NONE),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary
            )
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                houses.distinct().sorted().forEach { house ->
                    NeoVedicStatusPill(
                        text = stringResource(StringKeyAdvanced.KP_HOUSE_LABEL, house),
                        textColor = AppTheme.AccentPrimary,
                        containerColor = AppTheme.AccentPrimary.copy(alpha = 0.12f)
                    )
                }
            }
        }
    }
}

@Composable
private fun LabeledPlanetRow(
    label: String,
    planets: List<Planet>
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = AppTheme.TextSecondary
        )
        PlanetPillRow(planets.distinct())
    }
}

private fun sourceLabel(
    source: KpLinkSource,
    language: Language
): String {
    return when (source) {
        KpLinkSource.STAR_OCCUPANT -> StringResources.get(StringKeyAdvanced.KP_SOURCE_STAR_OCCUPANT, language)
        KpLinkSource.OCCUPANT -> StringResources.get(StringKeyAdvanced.KP_SOURCE_OCCUPANT, language)
        KpLinkSource.STAR_OWNER -> StringResources.get(StringKeyAdvanced.KP_SOURCE_STAR_OWNER, language)
        KpLinkSource.OWNER -> StringResources.get(StringKeyAdvanced.KP_SOURCE_OWNER, language)
        KpLinkSource.SUB_LORD -> StringResources.get(StringKeyAdvanced.KP_SOURCE_SUB_LORD, language)
        KpLinkSource.NODE_SIGN_LORD -> StringResources.get(StringKeyAdvanced.KP_SOURCE_NODE_SIGN_LORD, language)
    }
}

@Composable
private fun planetListText(planets: List<Planet>): String {
    val language = LocalLanguage.current
    return if (planets.isEmpty()) {
        stringResource(StringKeyAdvanced.KP_NONE)
    } else {
        planets.distinct().joinToString(", ") { it.getLocalizedName(language) }
    }
}

@Composable
private fun houseListText(houses: List<Int>): String {
    return if (houses.isEmpty()) {
        stringResource(StringKeyAdvanced.KP_NONE)
    } else {
        houses.distinct().sorted().joinToString(", ") { it.toString() }
    }
}

private fun formatMoment(
    moment: LocalDateTime,
    zoneId: ZoneId
): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
    return moment.atZone(zoneId).format(formatter)
}

private fun currentMoment(zoneId: ZoneId): LocalDateTime {
    return ZonedDateTime.now(zoneId)
        .withSecond(0)
        .withNano(0)
        .toLocalDateTime()
}

private fun formatLongitude(longitude: Double): String {
    val normalized = VedicAstrologyUtils.normalizeLongitude(longitude)
    val sign = com.astro.vajra.core.model.ZodiacSign.fromLongitude(normalized)
    val degreeInSign = normalized % 30.0
    val degrees = degreeInSign.toInt()
    val minutes = ((degreeInSign - degrees) * 60.0).toInt()
    val seconds = ((((degreeInSign - degrees) * 60.0) - minutes) * 60.0).toInt()
    return "${sign.abbreviation} ${degrees}°${minutes}'${seconds}\""
}

private fun formatAbsoluteLongitude(longitude: Double): String {
    val normalized = VedicAstrologyUtils.normalizeLongitude(longitude)
    val degrees = normalized.toInt()
    val minutes = ((normalized - degrees) * 60.0).toInt()
    val seconds = ((((normalized - degrees) * 60.0) - minutes) * 60.0).toInt()
    return "${degrees}°${minutes}'${seconds}\""
}
