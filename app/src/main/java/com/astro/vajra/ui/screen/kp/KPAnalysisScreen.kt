package com.astro.vajra.ui.screen.kp

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKeyKP
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.data.localization.LocalLanguage
import com.astro.vajra.ephemeris.kp.*
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import com.astro.vajra.ui.components.common.TabItem
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.PoppinsFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * KP Analysis Screen - Comprehensive Krishnamurti Paddhati Analysis
 *
 * Displays cusp sub-lords, planet positions with star/sub analysis,
 * 4-step significator table, and ruling planets.
 *
 * Follows the app's neo-vedic "Ethereal Vedic Grid" design system with
 * sharp corners, flat elevation, parchment-ink aesthetic, and data-forward layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KPAnalysisScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var analysis by remember { mutableStateOf<KPAnalysisResult?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(chart, language) {
        if (chart == null) {
            isLoading = false
            errorMessage = StringResources.get(StringKeyKP.NO_CHART_AVAILABLE, language)
            return@LaunchedEffect
        }

        isLoading = true
        errorMessage = null

        withContext(Dispatchers.Default) {
            try {
                analysis = KPCalculator.analyze(chart)
            } catch (e: Exception) {
                errorMessage = e.message ?: StringResources.get(StringKeyKP.ERROR_KP, language)
            }
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            NeoVedicPageHeader(
                title = StringResources.get(StringKeyKP.TITLE, language),
                subtitle = StringResources.get(StringKeyKP.SUBTITLE, language),
                onBack = onBack
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { padding ->
        when {
            isLoading -> KPLoadingState(modifier = Modifier.padding(padding))
            errorMessage != null -> KPErrorState(errorMessage!!, modifier = Modifier.padding(padding))
            analysis != null -> KPAnalysisContent(
                analysis = analysis!!,
                language = language,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun KPAnalysisContent(
    analysis: KPAnalysisResult,
    language: Language,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        StringResources.get(StringKeyKP.TAB_CUSPS, language),
        StringResources.get(StringKeyKP.TAB_PLANETS, language),
        StringResources.get(StringKeyKP.TAB_SIGNIFICATORS, language),
        StringResources.get(StringKeyKP.TAB_RULING_PLANETS, language)
    )
    val tabItems = tabs.mapIndexed { index, title ->
        TabItem(
            title = title,
            accentColor = when (index) {
                0 -> AppTheme.AccentGold
                1 -> AppTheme.AccentTeal
                2 -> AppTheme.AccentPrimary
                else -> Color(0xFF8B7EC8)
            }
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        ModernPillTabRow(
            tabs = tabItems,
            selectedIndex = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier.padding(
                horizontal = NeoVedicTokens.ScreenPadding,
                vertical = NeoVedicTokens.SpaceXS
            )
        )

        when (selectedTab) {
            0 -> CuspsTab(analysis, language)
            1 -> PlanetsTab(analysis, language)
            2 -> SignificatorsTab(analysis, language)
            3 -> RulingPlanetsTab(analysis, language)
        }
    }
}

// ============================================================================
// CUSPS TAB
// ============================================================================

@Composable
private fun CuspsTab(analysis: KPAnalysisResult, language: Language) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            KPSectionHeader(
                title = StringResources.get(StringKeyKP.CUSP_HEADER, language),
                subtitle = StringResources.get(StringKeyKP.CUSP_SUBTITLE, language),
                icon = Icons.Outlined.GridView
            )
        }

        // Ayanamsa info card
        item {
            KPInfoChip(
                label = StringResources.get(StringKeyKP.KP_AYANAMSA, language),
                value = "%.6f°".format(analysis.ayanamsaValue)
            )
        }

        items(analysis.cusps) { cusp ->
            CuspCard(cusp = cusp, language = language)
        }
    }
}

@Composable
private fun CuspCard(cusp: KPCuspResult, language: Language) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Header row: Cusp number + degree
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = StringResources.get(StringKeyKP.CUSP_NUMBER, language, cusp.cuspNumber.toString()),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = cusp.formattedDegree,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.AccentGold,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sign and lords grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                KPLordChip(
                    label = StringResources.get(StringKeyKP.SIGN_LORD, language),
                    planet = cusp.signLord,
                    color = AppTheme.getPlanetColor(cusp.signLord),
                    modifier = Modifier.weight(1f),
                    language = language
                )
                Spacer(modifier = Modifier.width(6.dp))
                KPLordChip(
                    label = StringResources.get(StringKeyKP.STAR_LORD, language),
                    planet = cusp.starLord,
                    color = AppTheme.getPlanetColor(cusp.starLord),
                    modifier = Modifier.weight(1f),
                    language = language
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                KPLordChip(
                    label = StringResources.get(StringKeyKP.SUB_LORD, language),
                    planet = cusp.subLord,
                    color = AppTheme.getPlanetColor(cusp.subLord),
                    modifier = Modifier.weight(1f),
                    language = language
                )
                Spacer(modifier = Modifier.width(6.dp))
                KPLordChip(
                    label = StringResources.get(StringKeyKP.SUB_SUB_LORD, language),
                    planet = cusp.subSubLord,
                    color = AppTheme.getPlanetColor(cusp.subSubLord),
                    modifier = Modifier.weight(1f),
                    language = language
                )
            }
        }
    }
}

// ============================================================================
// PLANETS TAB
// ============================================================================

@Composable
private fun PlanetsTab(analysis: KPAnalysisResult, language: Language) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            KPSectionHeader(
                title = StringResources.get(StringKeyKP.PLANET_HEADER, language),
                subtitle = StringResources.get(StringKeyKP.PLANET_SUBTITLE, language),
                icon = Icons.Outlined.Brightness7
            )
        }

        items(analysis.planets) { planet ->
            PlanetCard(planet = planet, language = language)
        }
    }
}

@Composable
private fun PlanetCard(planet: KPPlanetResult, language: Language) {
    val planetColor = AppTheme.getPlanetColor(planet.planet)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Header: Planet name + degree
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Planet color indicator
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(planetColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = planet.planet.getLocalizedName(language),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGroteskFamily,
                        color = AppTheme.TextPrimary
                    )
                    if (planet.isRetrograde) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = StringResources.get(StringKeyKP.RETROGRADE_LABEL, language),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.ErrorColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = planet.formattedDegree,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = SpaceGroteskFamily,
                        color = AppTheme.AccentGold,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${StringResources.get(StringKeyKP.HOUSE, language)} ${planet.house}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sign, Nakshatra
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                KPDataField(
                    label = StringResources.get(StringKeyKP.PLANET_SIGN, language),
                    value = planet.sign.getLocalizedName(language),
                    modifier = Modifier.weight(1f)
                )
                KPDataField(
                    label = StringResources.get(StringKeyKP.PLANET_NAKSHATRA, language),
                    value = planet.nakshatra.getLocalizedName(language),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lords row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                KPLordChip(
                    label = StringResources.get(StringKeyKP.STAR_LORD, language),
                    planet = planet.starLord,
                    color = AppTheme.getPlanetColor(planet.starLord),
                    modifier = Modifier.weight(1f),
                    language = language
                )
                Spacer(modifier = Modifier.width(6.dp))
                KPLordChip(
                    label = StringResources.get(StringKeyKP.SUB_LORD, language),
                    planet = planet.subLord,
                    color = AppTheme.getPlanetColor(planet.subLord),
                    modifier = Modifier.weight(1f),
                    language = language
                )
                Spacer(modifier = Modifier.width(6.dp))
                KPLordChip(
                    label = StringResources.get(StringKeyKP.SUB_SUB_LORD, language),
                    planet = planet.subSubLord,
                    color = AppTheme.getPlanetColor(planet.subSubLord),
                    modifier = Modifier.weight(1f),
                    language = language
                )
            }
        }
    }
}

// ============================================================================
// SIGNIFICATORS TAB
// ============================================================================

@Composable
private fun SignificatorsTab(analysis: KPAnalysisResult, language: Language) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            KPSectionHeader(
                title = StringResources.get(StringKeyKP.SIGNIFICATOR_HEADER, language),
                subtitle = StringResources.get(StringKeyKP.SIGNIFICATOR_SUBTITLE, language),
                icon = Icons.Outlined.AccountTree
            )
        }

        items(analysis.significatorTable.houses.entries.toList().sortedBy { it.key }) { (houseNum, sig) ->
            SignificatorCard(houseNumber = houseNum, significators = sig, language = language)
        }
    }
}

@Composable
private fun SignificatorCard(
    houseNumber: Int,
    significators: KPHouseSignificators,
    language: Language
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = StringResources.get(StringKeyKP.HOUSE_SIGNIFICATORS, language, houseNumber.toString()),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.TextPrimary
                )
                // Strongest significators summary
                Text(
                    text = significators.strongestSignificators.joinToString(", ") {
                        it.getLocalizedName(language)
                    }.ifEmpty { StringResources.get(StringKeyKP.NO_PLANETS, language) },
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.AccentGold,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false).padding(start = 8.dp)
                )
                Icon(
                    imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))

                // Step 1
                SignificatorStepRow(
                    stepLabel = StringResources.get(StringKeyKP.SIGNIFICATOR_LEVEL_1, language),
                    planets = significators.starlordOfOccupants,
                    language = language,
                    stepColor = AppTheme.AccentGold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Step 2
                SignificatorStepRow(
                    stepLabel = StringResources.get(StringKeyKP.SIGNIFICATOR_LEVEL_2, language),
                    planets = significators.occupants,
                    language = language,
                    stepColor = AppTheme.AccentTeal
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Step 3
                SignificatorStepRow(
                    stepLabel = StringResources.get(StringKeyKP.SIGNIFICATOR_LEVEL_3, language),
                    planets = significators.starlordOfOwner,
                    language = language,
                    stepColor = AppTheme.AccentPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Step 4
                SignificatorStepRow(
                    stepLabel = StringResources.get(StringKeyKP.SIGNIFICATOR_LEVEL_4, language),
                    planets = listOf(significators.owner),
                    language = language,
                    stepColor = AppTheme.TextSecondary
                )

                // Conjoined/Aspected
                if (significators.conjoinedAspected.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    SignificatorStepRow(
                        stepLabel = StringResources.get(StringKeyKP.CONJOINED_ASPECTED, language),
                        planets = significators.conjoinedAspected,
                        language = language,
                        stepColor = AppTheme.WarningColor
                    )
                }
            }
        }
    }
}

@Composable
private fun SignificatorStepRow(
    stepLabel: String,
    planets: List<Planet>,
    language: Language,
    stepColor: Color
) {
    Column {
        Text(
            text = stepLabel,
            style = MaterialTheme.typography.labelSmall,
            color = stepColor,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (planets.isEmpty()) {
            Text(
                text = StringResources.get(StringKeyKP.NO_PLANETS, language),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                planets.forEach { planet ->
                    Surface(
                        color = AppTheme.getPlanetColor(planet).copy(alpha = 0.12f),
                        shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
                        border = BorderStroke(0.5.dp, AppTheme.getPlanetColor(planet).copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = planet.getLocalizedName(language),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.getPlanetColor(planet),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// RULING PLANETS TAB
// ============================================================================

@Composable
private fun RulingPlanetsTab(analysis: KPAnalysisResult, language: Language) {
    val rp = analysis.rulingPlanets

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            KPSectionHeader(
                title = StringResources.get(StringKeyKP.RULING_HEADER, language),
                subtitle = StringResources.get(StringKeyKP.RULING_SUBTITLE, language),
                icon = Icons.Outlined.Stars
            )
        }

        // Individual ruling planet cards
        item {
            RulingPlanetRow(
                label = StringResources.get(StringKeyKP.DAY_LORD, language),
                planet = rp.dayLord,
                language = language
            )
        }
        item {
            RulingPlanetRow(
                label = StringResources.get(StringKeyKP.MOON_SIGN_LORD, language),
                planet = rp.moonSignLord,
                language = language
            )
        }
        item {
            RulingPlanetRow(
                label = StringResources.get(StringKeyKP.MOON_STAR_LORD, language),
                planet = rp.moonStarLord,
                language = language
            )
        }
        item {
            RulingPlanetRow(
                label = StringResources.get(StringKeyKP.MOON_SUB_LORD, language),
                planet = rp.moonSubLord,
                language = language
            )
        }
        item {
            RulingPlanetRow(
                label = StringResources.get(StringKeyKP.ASC_SIGN_LORD, language),
                planet = rp.ascSignLord,
                language = language
            )
        }
        item {
            RulingPlanetRow(
                label = StringResources.get(StringKeyKP.ASC_STAR_LORD, language),
                planet = rp.ascStarLord,
                language = language
            )
        }
        item {
            RulingPlanetRow(
                label = StringResources.get(StringKeyKP.ASC_SUB_LORD, language),
                planet = rp.ascSubLord,
                language = language
            )
        }

        // Repeated rulers (common/strong)
        if (rp.repeatedRulers.isNotEmpty()) {
            item { Spacer(modifier = Modifier.height(4.dp)) }
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = AppTheme.AccentGold.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                    border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.AccentGold.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = StringResources.get(StringKeyKP.RULING_COMMON, language),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.AccentGold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rp.repeatedRulers.forEach { planet ->
                                Surface(
                                    color = AppTheme.getPlanetColor(planet).copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
                                    border = BorderStroke(0.5.dp, AppTheme.getPlanetColor(planet).copy(alpha = 0.3f))
                                ) {
                                    Text(
                                        text = planet.getLocalizedName(language),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = AppTheme.getPlanetColor(planet),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Info card
        item { Spacer(modifier = Modifier.height(4.dp)) }
        item {
            KPInfoCard(
                title = StringResources.get(StringKeyKP.RULING_INFO_TITLE, language),
                content = StringResources.get(StringKeyKP.RULING_INFO_CONTENT, language),
                icon = Icons.Outlined.Info
            )
        }
    }
}

@Composable
private fun RulingPlanetRow(
    label: String,
    planet: Planet,
    language: Language
) {
    val planetColor = AppTheme.getPlanetColor(planet)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(planetColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = planet.getLocalizedName(language),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = SpaceGroteskFamily,
                    color = planetColor
                )
            }
        }
    }
}

// ============================================================================
// SHARED COMPONENTS
// ============================================================================

@Composable
private fun KPLordChip(
    label: String,
    planet: Planet,
    color: Color,
    modifier: Modifier = Modifier,
    language: Language
) {
    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.06f),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(0.5.dp, color.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted,
                fontSize = 9.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = planet.getLocalizedName(language),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                fontFamily = SpaceGroteskFamily,
                color = color,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun KPDataField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted,
            fontSize = 10.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun KPSectionHeader(
    title: String,
    subtitle: String,
    icon: ImageVector
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.AccentPrimary.copy(alpha = 0.06f),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.AccentPrimary.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.AccentPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun KPInfoChip(label: String, value: String) {
    Surface(
        color = AppTheme.ChipBackground,
        shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = SpaceGroteskFamily,
                color = AppTheme.AccentGold
            )
        }
    }
}

@Composable
private fun KPInfoCard(
    title: String,
    content: String,
    icon: ImageVector
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ============================================================================
// LOADING & ERROR STATES
// ============================================================================

@Composable
private fun KPLoadingState(modifier: Modifier = Modifier) {
    val language = LocalLanguage.current
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = AppTheme.AccentPrimary,
                modifier = Modifier.size(36.dp),
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = StringResources.get(StringKeyKP.LOADING_KP, language),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun KPErrorState(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Error,
                contentDescription = null,
                tint = AppTheme.ErrorColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}
