package com.astro.vajra.ui.screen.kp

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * KP Horary Screen - Number-based Prashna Analysis (1-249)
 *
 * The querent thinks of a number between 1 and 249.
 * This maps to a specific sub-lord division, which determines the horary ascendant.
 * Full KP analysis is then performed on the resulting horary chart.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KPHoraryScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var numberInput by remember { mutableStateOf("") }
    var horaryData by remember { mutableStateOf<KPHoraryData?>(null) }
    var horaryAnalysis by remember { mutableStateOf<KPAnalysisResult?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current

    fun analyzeHorary() {
        val number = numberInput.toIntOrNull()
        if (number == null || number !in 1..249) {
            errorMessage = StringResources.get(StringKeyKP.HORARY_INVALID_NUMBER, language)
            return
        }
        if (chart == null) {
            errorMessage = StringResources.get(StringKeyKP.NO_CHART_AVAILABLE, language)
            return
        }

        focusManager.clearFocus()
        errorMessage = null
        isLoading = true
    }

    // Perform analysis when loading
    LaunchedEffect(isLoading) {
        if (!isLoading) return@LaunchedEffect
        val number = numberInput.toIntOrNull() ?: return@LaunchedEffect
        if (chart == null) return@LaunchedEffect

        withContext(Dispatchers.Default) {
            try {
                horaryData = KPHoraryCalculator.getHoraryData(number)
                horaryAnalysis = KPHoraryCalculator.analyzeHorary(number, chart)
            } catch (e: Exception) {
                errorMessage = e.message ?: StringResources.get(StringKeyKP.ERROR_KP, language)
            }
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            NeoVedicPageHeader(
                title = StringResources.get(StringKeyKP.HORARY_TITLE, language),
                subtitle = StringResources.get(StringKeyKP.HORARY_SUBTITLE, language),
                onBack = onBack
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(NeoVedicTokens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Number Input Section
            item {
                NumberInputSection(
                    numberInput = numberInput,
                    onNumberChange = { newValue ->
                        numberInput = newValue.filter { it.isDigit() }.take(3)
                        errorMessage = null
                    },
                    onAnalyze = { analyzeHorary() },
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    language = language
                )
            }

            // Horary Result
            if (horaryData != null && horaryAnalysis != null && !isLoading) {
                val data = horaryData!!
                val analysis = horaryAnalysis!!

                // Ascendant Details
                item {
                    HoraryAscendantCard(data = data, language = language)
                }

                // Cusp Sub-Lords (all 12)
                item {
                    HorarySectionHeader(
                        title = StringResources.get(StringKeyKP.CUSP_HEADER, language),
                        icon = Icons.Outlined.GridView
                    )
                }

                items(analysis.cusps) { cusp ->
                    HoraryCuspRow(cusp = cusp, language = language)
                }

                // Planet Positions
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    HorarySectionHeader(
                        title = StringResources.get(StringKeyKP.PLANET_HEADER, language),
                        icon = Icons.Outlined.Brightness7
                    )
                }

                items(analysis.planets) { planet ->
                    HoraryPlanetRow(planet = planet, language = language)
                }

                // Significator Summary
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    HorarySectionHeader(
                        title = StringResources.get(StringKeyKP.SIGNIFICATOR_HEADER, language),
                        icon = Icons.Outlined.AccountTree
                    )
                }

                items(analysis.significatorTable.houses.entries.toList().sortedBy { it.key }) { (houseNum, sig) ->
                    HorarySignificatorRow(houseNumber = houseNum, significators = sig, language = language)
                }

                // Ruling Planets
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    HorarySectionHeader(
                        title = StringResources.get(StringKeyKP.RULING_HEADER, language),
                        icon = Icons.Outlined.Stars
                    )
                }

                item {
                    HoraryRulingPlanetsCard(rulingPlanets = analysis.rulingPlanets, language = language)
                }

                // Info card
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    HoraryInfoCard(language = language)
                }
            }
        }
    }
}

// ============================================================================
// NUMBER INPUT
// ============================================================================

@Composable
private fun NumberInputSection(
    numberInput: String,
    onNumberChange: (String) -> Unit,
    onAnalyze: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.AccentGold.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "1 – 249",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = CinzelDecorativeFamily,
                color = AppTheme.AccentGold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = StringResources.get(StringKeyKP.HORARY_NUMBER_LABEL, language),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Number input field
            OutlinedTextField(
                value = numberInput,
                onValueChange = onNumberChange,
                modifier = Modifier.width(160.dp),
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    textAlign = TextAlign.Center,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                ),
                placeholder = {
                    Text(
                        text = "108",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            textAlign = TextAlign.Center,
                            fontFamily = SpaceGroteskFamily,
                            color = AppTheme.TextMuted.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onAnalyze() }
                ),
                singleLine = true,
                shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.AccentGold,
                    unfocusedBorderColor = AppTheme.BorderColor,
                    cursorColor = AppTheme.AccentGold,
                    focusedContainerColor = AppTheme.InputBackground,
                    unfocusedContainerColor = AppTheme.InputBackground
                )
            )

            // Error message
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.ErrorColor,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Analyze button
            Button(
                onClick = onAnalyze,
                enabled = numberInput.isNotEmpty() && !isLoading,
                modifier = Modifier.fillMaxWidth(0.6f),
                shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.AccentGold,
                    contentColor = Color.White,
                    disabledContainerColor = AppTheme.AccentGold.copy(alpha = 0.3f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = StringResources.get(StringKeyKP.HORARY_ANALYZE, language),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ============================================================================
// HORARY RESULT COMPONENTS
// ============================================================================

@Composable
private fun HoraryAscendantCard(data: KPHoraryData, language: Language) {
    val pos = data.ascendantPosition

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.AccentGold.copy(alpha = 0.06f),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.AccentGold.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = StringResources.get(StringKeyKP.HORARY_RESULT_HEADER, language, data.horaryNumber.toString()),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = AppTheme.AccentGold
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Ascendant degree
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = StringResources.get(StringKeyKP.HORARY_ASC_DEGREE, language),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary
                )
                Text(
                    text = pos.formattedDegree,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = SpaceGroteskFamily,
                    color = AppTheme.AccentGold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lords
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HoraryLordChip(
                    label = StringResources.get(StringKeyKP.SIGN_LORD, language),
                    planet = pos.signLord,
                    language = language
                )
                HoraryLordChip(
                    label = StringResources.get(StringKeyKP.STAR_LORD, language),
                    planet = pos.starLord,
                    language = language
                )
                HoraryLordChip(
                    label = StringResources.get(StringKeyKP.SUB_LORD, language),
                    planet = pos.subLord,
                    language = language
                )
                HoraryLordChip(
                    label = StringResources.get(StringKeyKP.SUB_SUB_LORD, language),
                    planet = pos.subSubLord,
                    language = language
                )
            }
        }
    }
}

@Composable
private fun HoraryLordChip(
    label: String,
    planet: Planet,
    language: Language
) {
    val color = AppTheme.getPlanetColor(planet)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted,
            fontSize = 9.sp,
            maxLines = 1
        )
        Text(
            text = planet.getLocalizedName(language),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            fontFamily = SpaceGroteskFamily,
            color = color
        )
    }
}

@Composable
private fun HoraryCuspRow(cusp: KPCuspResult, language: Language) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cusp number
            Text(
                text = StringResources.get(StringKeyKP.CUSP_NUMBER, language, cusp.cuspNumber.toString()),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                fontFamily = SpaceGroteskFamily,
                color = AppTheme.TextPrimary,
                modifier = Modifier.width(52.dp)
            )
            // Degree
            Text(
                text = cusp.formattedDegree,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = SpaceGroteskFamily,
                color = AppTheme.AccentGold,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(90.dp)
            )
            // Lords: Sign / Star / Sub
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PlanetLabel(planet = cusp.signLord, language = language)
                PlanetLabel(planet = cusp.starLord, language = language)
                PlanetLabel(planet = cusp.subLord, language = language)
            }
        }
    }
}

@Composable
private fun HoraryPlanetRow(planet: KPPlanetResult, language: Language) {
    val planetColor = AppTheme.getPlanetColor(planet.planet)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Planet name with color
            Row(
                modifier = Modifier.width(80.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(planetColor)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = planet.planet.getLocalizedName(language),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (planet.isRetrograde) {
                    Text(
                        text = " R",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.ErrorColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            // Degree
            Text(
                text = planet.formattedDegree,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = SpaceGroteskFamily,
                color = AppTheme.AccentGold,
                modifier = Modifier.width(90.dp)
            )
            // Star / Sub / Sub-Sub
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PlanetLabel(planet = planet.starLord, language = language)
                PlanetLabel(planet = planet.subLord, language = language)
                PlanetLabel(planet = planet.subSubLord, language = language)
            }
        }
    }
}

@Composable
private fun PlanetLabel(planet: Planet, language: Language) {
    Text(
        text = planet.getLocalizedName(language),
        style = MaterialTheme.typography.labelSmall,
        color = AppTheme.getPlanetColor(planet),
        fontWeight = FontWeight.SemiBold,
        fontFamily = SpaceGroteskFamily,
        maxLines = 1
    )
}

@Composable
private fun HorarySignificatorRow(
    houseNumber: Int,
    significators: KPHouseSignificators,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = StringResources.get(StringKeyKP.HOUSE, language) + " $houseNumber",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                fontFamily = SpaceGroteskFamily,
                color = AppTheme.TextPrimary,
                modifier = Modifier.width(60.dp)
            )
            Text(
                text = significators.allSignificators.joinToString(", ") {
                    it.getLocalizedName(language)
                }.ifEmpty { StringResources.get(StringKeyKP.NO_PLANETS, language) },
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun HoraryRulingPlanetsCard(
    rulingPlanets: KPRulingPlanets,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            val entries = listOf(
                StringResources.get(StringKeyKP.DAY_LORD, language) to rulingPlanets.dayLord,
                StringResources.get(StringKeyKP.MOON_SIGN_LORD, language) to rulingPlanets.moonSignLord,
                StringResources.get(StringKeyKP.MOON_STAR_LORD, language) to rulingPlanets.moonStarLord,
                StringResources.get(StringKeyKP.MOON_SUB_LORD, language) to rulingPlanets.moonSubLord,
                StringResources.get(StringKeyKP.ASC_SIGN_LORD, language) to rulingPlanets.ascSignLord,
                StringResources.get(StringKeyKP.ASC_STAR_LORD, language) to rulingPlanets.ascStarLord,
                StringResources.get(StringKeyKP.ASC_SUB_LORD, language) to rulingPlanets.ascSubLord
            )

            entries.forEachIndexed { index, (label, planet) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = planet.getLocalizedName(language),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGroteskFamily,
                        color = AppTheme.getPlanetColor(planet)
                    )
                }
                if (index < entries.lastIndex) {
                    HorizontalDivider(
                        color = AppTheme.DividerColor,
                        thickness = 0.5.dp
                    )
                }
            }

            // Common rulers
            if (rulingPlanets.repeatedRulers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = AppTheme.DividerColor)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = StringResources.get(StringKeyKP.RULING_COMMON, language),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.AccentGold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    rulingPlanets.repeatedRulers.forEach { planet ->
                        Surface(
                            color = AppTheme.getPlanetColor(planet).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
                            border = BorderStroke(0.5.dp, AppTheme.getPlanetColor(planet).copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = planet.getLocalizedName(language),
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.getPlanetColor(planet),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HorarySectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.AccentPrimary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun HoraryInfoCard(language: Language) {
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
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = StringResources.get(StringKeyKP.HORARY_INFO_TITLE, language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = StringResources.get(StringKeyKP.HORARY_INFO_CONTENT, language),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
