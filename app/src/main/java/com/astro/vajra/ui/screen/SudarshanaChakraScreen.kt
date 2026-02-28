package com.astro.vajra.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyDosha
import com.astro.vajra.core.common.StringKeyUICommon
import com.astro.vajra.core.common.StringKeyUIExtra
import com.astro.vajra.data.localization.currentLanguage
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.SudarshanaChakraDashaCalculator
import com.astro.vajra.ephemeris.SudarshanaChakraResult
import com.astro.vajra.ephemeris.ChakraAnalysis
import com.astro.vajra.ephemeris.SudarshanaReference
import com.astro.vajra.ephemeris.StrengthLevel
import com.astro.vajra.ephemeris.InfluenceNature
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import com.astro.vajra.ui.components.common.TabItem
import com.astro.vajra.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.DateTimeException
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.math.roundToInt

/**
 * Sudarshana Chakra Dasha Screen
 *
 * Comprehensive triple-reference timing system display showing:
 * - Lagna Chakra: Physical body, self, and initiative
 * - Chandra Chakra: Mind, emotions, and public image
 * - Surya Chakra: Soul, authority, and recognition
 *
 * Each reference progresses one house per year, creating a synchronized
 * triple view of life events based on classical Sudarshana Chakra principles.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudarshanaChakraScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(
            title = stringResource(StringKeyDosha.SUDARSHANA_TITLE),
            message = stringResource(StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    val language = currentLanguage()
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var isCalculating by remember { mutableStateOf(true) }
    var chakraResult by remember { mutableStateOf<SudarshanaChakraResult?>(null) }
    var selectedAge by remember { mutableIntStateOf(0) }
    val zoneId = remember(chart) { resolveZoneId(chart.birthData.timezone) }

    // Calculate current age
    val currentAge = remember(chart, zoneId) {
        Period.between(chart.birthData.dateTime.toLocalDate(), LocalDate.now(zoneId)).years
    }

    // Initialize selectedAge with current year of life (age + 1)
    LaunchedEffect(currentAge) {
        selectedAge = currentAge + 1
    }

    val tabs = listOf(
        stringResource(StringKeyDosha.SUDARSHANA_TRIPLE_VIEW),
        stringResource(StringKeyDosha.SCREEN_TIMELINE),
        stringResource(StringKeyDosha.SCREEN_INTERPRETATION)
    )

    // Calculate Sudarshana Chakra
    LaunchedEffect(chart, selectedAge) {
        if (selectedAge < 1) return@LaunchedEffect
        isCalculating = true
        delay(200)
        try {
            chakraResult = withContext(Dispatchers.Default) {
                SudarshanaChakraDashaCalculator.calculateSudarshanaChakra(chart, selectedAge, language)
            }
        } catch (e: Exception) {
            // Handle error silently
        }
        isCalculating = false
    }

    if (showInfoDialog) {
        SudarshanaInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyDosha.SUDARSHANA_TITLE),
                subtitle = chart.birthData.name,
                onBack = onBack,
                actionIcon = Icons.Outlined.Info,
                onAction = { showInfoDialog = true }
            )
        }
    ) { paddingValues ->
        if (isCalculating) {
            LoadingContent(paddingValues)
        } else if (chakraResult != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Age Selector
                AgeSelector(
                    currentAge = currentAge,
                    selectedAge = selectedAge,
                    onAgeSelected = { selectedAge = it }
                )

                // Tab selector
                TabSelector(
                    tabs = tabs,
                    selectedIndex = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                // Content based on selected tab
                when (selectedTab) {
                    0 -> TripleViewContent(chakraResult!!)
                    1 -> TimelineContent(chart, currentAge)
                    2 -> SynthesisContent(chakraResult!!)
                }
            }
        } else {
            ErrorContent(paddingValues, stringResource(StringKeyDosha.SCREEN_ERROR_CALCULATION))
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
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(StringKeyDosha.SCREEN_CALCULATING),
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun ErrorContent(paddingValues: PaddingValues, message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = AppTheme.ErrorColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(message, color = AppTheme.TextMuted)
        }
    }
}

@Composable
private fun AgeSelector(
    currentAge: Int,
    selectedAge: Int,
    onAgeSelected: (Int) -> Unit
) {
    val language = currentLanguage()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        stringResource(StringKeyDosha.SUDARSHANA_AGE),
                        color = AppTheme.TextMuted,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12
                    )
                    Text(
                        "${stringResource(StringKeyDosha.CURRENT_LABEL)}: $currentAge ${stringResource(StringKey.YEARS)}",
                        color = AppTheme.AccentPrimary,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    String.format(stringResource(StringKeyDosha.SUDARSHANA_YEAR_ANALYSIS), selectedAge),
                    color = AppTheme.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Age slider
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (selectedAge > 1) onAgeSelected(selectedAge - 1) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Filled.Remove,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary
                    )
                }

                Slider(
                    value = selectedAge.toFloat().coerceIn(1f, 100f),
                    onValueChange = { onAgeSelected(it.toInt().coerceAtLeast(1)) },
                    valueRange = 1f..100f,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = AppTheme.AccentPrimary,
                        activeTrackColor = AppTheme.AccentPrimary,
                        inactiveTrackColor = AppTheme.AccentPrimary.copy(alpha = 0.2f)
                    )
                )

                IconButton(
                    onClick = { if (selectedAge < 100) onAgeSelected(selectedAge + 1) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary
                    )
                }
            }

            // Quick jump buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = { onAgeSelected(currentAge + 1) },
                    enabled = selectedAge != (currentAge + 1)
                ) {
                    Text(
                        stringResource(StringKeyDosha.CURRENT_LABEL),
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                TextButton(
                    onClick = { onAgeSelected(1) },
                    enabled = selectedAge != 1
                ) {
                    Text(
                        stringResource(StringKeyUIExtra.BIRTH_1ST_YR),
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12
                    )
                }
            }
        }
    }
}

@Composable
private fun TabSelector(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    ModernPillTabRow(
        tabs = tabs.mapIndexed { index, title ->
            TabItem(
                title = title,
                accentColor = if (selectedIndex == index) AppTheme.AccentPrimary else Color.Unspecified
            )
        },
        selectedIndex = selectedIndex,
        onTabSelected = onTabSelected,
        modifier = Modifier.padding(
            horizontal = com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding,
            vertical = com.astro.vajra.ui.theme.NeoVedicTokens.SpaceXXS
        )
    )
}

@Composable
private fun TripleViewContent(result: SudarshanaChakraResult) {
    val language = currentLanguage()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(com.astro.vajra.ui.theme.NeoVedicTokens.SpaceMD)
    ) {
        // Current Signs Overview
        item {
            CurrentSignsCard(result)
        }

        // Lagna Chakra
        item {
            ChakraCard(
                chakra = result.lagnaChakra,
                title = stringResource(StringKeyDosha.SUDARSHANA_LAGNA_CHAKRA),
                subtitle = stringResource(StringKeyDosha.SUDARSHANA_LAGNA_INFLUENCE),
                color = com.astro.vajra.ui.theme.PlanetRahu
            )
        }

        // Chandra Chakra
        item {
            ChakraCard(
                chakra = result.chandraChakra,
                title = stringResource(StringKeyDosha.SUDARSHANA_CHANDRA_CHAKRA),
                subtitle = stringResource(StringKeyDosha.SUDARSHANA_CHANDRA_INFLUENCE),
                color = com.astro.vajra.ui.theme.PlanetMercury
            )
        }

        // Surya Chakra
        item {
            ChakraCard(
                chakra = result.suryaChakra,
                title = stringResource(StringKeyDosha.SUDARSHANA_SURYA_CHAKRA),
                subtitle = stringResource(StringKeyDosha.SUDARSHANA_SURYA_INFLUENCE),
                color = com.astro.vajra.ui.theme.PlanetSun
            )
        }

        // Convergence Analysis
        item {
            ConvergenceCard(result)
        }
    }
}

@Composable
private fun CurrentSignsCard(result: SudarshanaChakraResult) {
    val language = currentLanguage()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.AccentPrimary.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Visibility,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(StringKeyDosha.SUDARSHANA_CURRENT_SIGNS),
                    fontWeight = FontWeight.Bold,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(com.astro.vajra.ui.theme.NeoVedicTokens.SpaceMD))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SignIndicator(
                    label = stringResource(StringKeyDosha.LAGNA_LABEL),
                    sign = result.lagnaChakra.activeSign,
                    house = result.lagnaChakra.activeHouse,
                    color = com.astro.vajra.ui.theme.PlanetRahu
                )
                SignIndicator(
                    label = stringResource(StringKeyDosha.MOON_LABEL),
                    sign = result.chandraChakra.activeSign,
                    house = result.chandraChakra.activeHouse,
                    color = com.astro.vajra.ui.theme.PlanetMercury
                )
                SignIndicator(
                    label = stringResource(StringKeyDosha.SUN_LABEL),
                    sign = result.suryaChakra.activeSign,
                    house = result.suryaChakra.activeHouse,
                    color = com.astro.vajra.ui.theme.PlanetSun
                )
            }
        }
    }
}

@Composable
private fun SignIndicator(
    label: String,
    sign: ZodiacSign,
    house: Int,
    color: Color
) {
    val language = currentLanguage()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(color.copy(alpha = 0.2f), CircleShape)
                .border(2.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                sign.symbol,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            label,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
            color = AppTheme.TextMuted
        )
        Text(
            sign.getLocalizedName(language),
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextPrimary
        )
        Text(
            stringResource(StringKeyUIExtra.HOUSE_PREFIX_SHORT) + house,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
            color = color
        )
    }
}

@Composable
private fun ChakraCard(
    chakra: ChakraAnalysis,
    title: String,
    subtitle: String,
    color: Color
) {
    val language = currentLanguage()
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(color, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            subtitle,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                            color = AppTheme.TextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                StrengthBadge(chakra.strength.level, color)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign and Lord Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoColumn(
                    label = stringResource(StringKeyDosha.SIGN_LABEL),
                    value = chakra.activeSign.getLocalizedName(language)
                )
                InfoColumn(
                    label = stringResource(StringKeyDosha.SUDARSHANA_SIGN_LORD),
                    value = chakra.signLord.getLocalizedName(language)
                )
                InfoColumn(
                    label = stringResource(StringKeyDosha.HOUSE_LABEL),
                    value = stringResource(StringKeyUIExtra.HOUSE_PREFIX_SHORT) + chakra.activeHouse
                )
            }

            // Strength Progress
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(StringKeyDosha.STRENGTH_LABEL),
                    color = AppTheme.TextMuted,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12
                )
                Text(
                    "${chakra.strength.score.toInt()}%",
                    color = color,
                    fontWeight = FontWeight.Medium,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { (chakra.strength.score / 100).toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                color = color,
                trackColor = color.copy(alpha = 0.2f)
            )

            // Expanded content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    // House Significance
                    Text(
                        stringResource(StringKeyDosha.SUDARSHANA_HOUSE_SIGNIFICATIONS),
                        fontWeight = FontWeight.Medium,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                        color = AppTheme.TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        chakra.houseSignificance.description,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                        color = AppTheme.TextMuted,
                        lineHeight = 20.sp
                    )

                    // Planets in Sign
                    if (chakra.planetsInSign.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(StringKeyDosha.SUDARSHANA_PLANETS_IN_SIGN),
                            fontWeight = FontWeight.Medium,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                            color = AppTheme.TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(chakra.planetsInSign) { position ->
                                PlanetChip(position.planet)
                            }
                        }
                    }

                    // Aspecting Planets
                    if (chakra.aspectingPlanets.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(StringKeyDosha.SUDARSHANA_ASPECTS_RECEIVED),
                            fontWeight = FontWeight.Medium,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                            color = AppTheme.TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        chakra.aspectingPlanets.forEach { aspect ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Text(
                                    stringResource(StringKeyUIExtra.BULLET_SPACE) + aspect.planet.getLocalizedName(language),
                                    color = AppTheme.TextMuted,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12
                                )
                                Text(
                                    stringResource(StringKeyUIExtra.LABEL_FROM_COLON) + aspect.fromSign.getLocalizedName(language),
                                    color = AppTheme.TextMuted,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12
                                )
                            }
                        }
                    }

                    // Sign Effects
                    if (chakra.signEffects.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(StringKeyDosha.EFFECTS_LABEL),
                            fontWeight = FontWeight.Medium,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                            color = AppTheme.TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        chakra.signEffects.forEach { effect ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Text(stringResource(StringKeyUIExtra.BULLET_SPACE), color = color)
                                Text(
                                    effect,
                                    color = AppTheme.TextMuted,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            // Expand/Collapse indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
            color = AppTheme.TextMuted
        )
        Text(
            value,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun StrengthBadge(level: StrengthLevel, color: Color) {
    val (text, badgeColor) = when (level) {
        StrengthLevel.EXCELLENT -> stringResource(StringKeyDosha.PANCHA_EXCELLENT) to AppTheme.SuccessColor
        StrengthLevel.GOOD -> stringResource(StringKeyDosha.PANCHA_GOOD) to AppTheme.SuccessColor.copy(alpha = 0.8f)
        StrengthLevel.MODERATE -> stringResource(StringKeyDosha.PANCHA_AVERAGE) to AppTheme.WarningColor
        StrengthLevel.WEAK -> stringResource(StringKeyDosha.PANCHA_BELOW_AVERAGE) to AppTheme.WarningColor.copy(alpha = 0.8f)
        StrengthLevel.VERY_WEAK -> stringResource(StringKeyDosha.PANCHA_WEAK) to AppTheme.ErrorColor
    }

    Surface(
        color = badgeColor.copy(alpha = 0.15f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = badgeColor,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PlanetChip(planet: Planet) {
    val language = currentLanguage()

    Surface(
        color = getPlanetColor(planet).copy(alpha = 0.15f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Text(
            planet.getLocalizedName(language),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = getPlanetColor(planet),
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ConvergenceCard(result: SudarshanaChakraResult) {
    val language = currentLanguage()
    val synthesis = result.synthesis

    val convergenceStrength = when {
        synthesis.combinedStrengthScore >= 65 -> stringResource(StringKeyDosha.SUDARSHANA_STRONG_CONVERGENCE)
        else -> stringResource(StringKeyDosha.SUDARSHANA_WEAK_CONVERGENCE)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (synthesis.combinedStrengthScore >= 60)
                AppTheme.SuccessColor.copy(alpha = 0.1f)
            else
                AppTheme.WarningColor.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Hub,
                    contentDescription = null,
                    tint = if (synthesis.combinedStrengthScore >= 60) AppTheme.SuccessColor else AppTheme.WarningColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(StringKeyDosha.SUDARSHANA_CONVERGENCE),
                    fontWeight = FontWeight.Bold,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(com.astro.vajra.ui.theme.NeoVedicTokens.SpaceMD))

            // Convergence status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    convergenceStrength,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
                Text(
                    "${synthesis.combinedStrengthScore.toInt()}%",
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                    fontWeight = FontWeight.Bold,
                    color = if (synthesis.combinedStrengthScore >= 60) AppTheme.SuccessColor else AppTheme.WarningColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Common themes
            if (synthesis.commonThemes.isNotEmpty()) {
                synthesis.commonThemes.forEach { theme ->
                    Row(modifier = Modifier.padding(vertical = 2.dp)) {
                        Text(stringResource(StringKeyUIExtra.CHECKMARK_SPACE), color = AppTheme.SuccessColor)
                        Text(
                            theme,
                            color = AppTheme.TextSecondary,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13
                        )
                    }
                }
            }

            // Conflicting themes
            if (synthesis.conflictingThemes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                synthesis.conflictingThemes.forEach { theme ->
                    Row(modifier = Modifier.padding(vertical = 2.dp)) {
                        Text(stringResource(StringKeyUIExtra.WARNING_SPACE), color = AppTheme.WarningColor)
                        Text(
                            theme,
                            color = AppTheme.TextSecondary,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelineContent(chart: VedicChart, currentAge: Int) {
    val language = currentLanguage()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Show a range of years around current age
        val startAge = (currentAge - 5).coerceAtLeast(1)
        val endAge = (currentAge + 10).coerceAtMost(100)

        items(endAge - startAge + 1) { index ->
            val age = startAge + index
            val isCurrent = age == currentAge

            TimelineYearCard(
                chart = chart,
                age = age,
                isCurrent = isCurrent
            )
        }
    }
}

@Composable
private fun TimelineYearCard(
    chart: VedicChart,
    age: Int,
    isCurrent: Boolean
) {
    val language = currentLanguage()
    var result by remember { mutableStateOf<SudarshanaChakraResult?>(null) }

    LaunchedEffect(age) {
        result = try {
            SudarshanaChakraDashaCalculator.calculateSudarshanaChakra(chart, age, language)
        } catch (e: Exception) {
            null
        }
    }

    result?.let { chakraResult ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isCurrent)
                    AppTheme.AccentPrimary.copy(alpha = 0.15f)
                else
                    AppTheme.CardBackground
            ),
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Age indicator
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(48.dp)
                ) {
                    Text(
                        "$age",
                        fontWeight = FontWeight.Bold,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                        color = if (isCurrent) AppTheme.AccentPrimary else AppTheme.TextPrimary
                    )
                    Text(
                        stringResource(StringKey.YEARS),
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                    if (isCurrent) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(AppTheme.SuccessColor, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(com.astro.vajra.ui.theme.NeoVedicTokens.SpaceMD))

                // Signs
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SignSmall("L", chakraResult.lagnaChakra.activeSign, com.astro.vajra.ui.theme.PlanetRahu)
                        SignSmall("M", chakraResult.chandraChakra.activeSign, com.astro.vajra.ui.theme.PlanetMercury)
                        SignSmall("S", chakraResult.suryaChakra.activeSign, com.astro.vajra.ui.theme.PlanetSun)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Strength bar
                    LinearProgressIndicator(
                        progress = { (chakraResult.synthesis.combinedStrengthScore / 100).toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                        color = getStrengthColor(chakraResult.synthesis.combinedStrengthScore),
                        trackColor = AppTheme.DividerColor
                    )
                }
            }
        }
    }
}

@Composable
private fun SignSmall(prefix: String, sign: ZodiacSign, color: Color) {
    val language = currentLanguage()

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            prefix,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            stringResource(StringKeyUICommon.COLON),
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
            color = AppTheme.TextMuted
        )
        Text(
            sign.abbreviation,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            color = AppTheme.TextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun SynthesisContent(result: SudarshanaChakraResult) {
    val language = currentLanguage()
    val synthesis = result.synthesis

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(com.astro.vajra.ui.theme.NeoVedicTokens.SpaceMD)
    ) {
        // Overall Assessment
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(com.astro.vajra.ui.theme.NeoVedicTokens.ScreenPadding)) {
                    Text(
                        stringResource(StringKeyDosha.SUDARSHANA_COMBINED_ANALYSIS),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        synthesis.overallAssessment,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = AppTheme.TextSecondary,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Three Chakra Contributions
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        stringResource(StringKeyDosha.SUDARSHANA_TRIPLE_VIEW),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    ContributionRow(
                        color = com.astro.vajra.ui.theme.PlanetRahu,
                        label = stringResource(StringKeyDosha.SUDARSHANA_LAGNA_CHAKRA),
                        contribution = synthesis.lagnaContribution
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ContributionRow(
                        color = com.astro.vajra.ui.theme.PlanetMercury,
                        label = stringResource(StringKeyDosha.SUDARSHANA_CHANDRA_CHAKRA),
                        contribution = synthesis.chandraContribution
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ContributionRow(
                        color = com.astro.vajra.ui.theme.PlanetSun,
                        label = stringResource(StringKeyDosha.SUDARSHANA_SURYA_CHAKRA),
                        contribution = synthesis.suryaContribution
                    )
                }
            }
        }

        // Primary and Secondary Focus
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.AccentPrimary.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        stringResource(StringKeyDosha.KEY_THEMES),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(
                            "${stringResource(StringKeyDosha.PRIMARY_LABEL)}: ",
                            color = AppTheme.AccentPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13
                        )
                        Text(
                            synthesis.primaryFocus,
                            color = AppTheme.TextSecondary,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13
                        )
                    }

                    if (synthesis.secondaryFocus.isNotBlank()) {
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                "${stringResource(StringKeyDosha.SECONDARY_LABEL)}: ",
                                color = AppTheme.AccentSecondary,
                                fontWeight = FontWeight.Medium,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13
                            )
                            Text(
                                synthesis.secondaryFocus,
                                color = AppTheme.TextSecondary,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13
                            )
                        }
                    }
                }
            }
        }

        // Recommendations
        if (result.recommendations.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.SuccessColor.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Lightbulb,
                                contentDescription = null,
                                tint = AppTheme.SuccessColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(StringKeyDosha.SCREEN_RECOMMENDATIONS),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                                color = AppTheme.TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        result.recommendations.forEachIndexed { index, rec ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(
                                    "${index + 1}. ",
                                    color = AppTheme.SuccessColor,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    rec,
                                    color = AppTheme.TextSecondary,
                                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Yearly Progression
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        stringResource(StringKeyDosha.SCREEN_TIMELINE),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    result.yearlyProgression.previousYearSummary?.let { prev ->
                        ProgressionRow(
                            label = stringResource(StringKeyDosha.PREVIOUS_LABEL),
                            summary = prev,
                            isPast = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    ProgressionRow(
                        label = stringResource(StringKeyDosha.CURRENT_LABEL),
                        summary = result.yearlyProgression.currentYearSummary,
                        isCurrent = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ProgressionRow(
                        label = stringResource(StringKeyDosha.NEXT_LABEL),
                        summary = result.yearlyProgression.nextYearSummary,
                        isFuture = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        result.yearlyProgression.trend,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                        color = AppTheme.TextMuted,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}

@Composable
private fun ContributionRow(color: Color, label: String, contribution: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .offset(y = 6.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                label,
                fontWeight = FontWeight.Medium,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                color = color
            )
            Text(
                contribution,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun ProgressionRow(
    label: String,
    summary: String,
    isPast: Boolean = false,
    isCurrent: Boolean = false,
    isFuture: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isCurrent) AppTheme.AccentPrimary.copy(alpha = 0.1f) else Color.Transparent,
                RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            label,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            color = when {
                isCurrent -> AppTheme.AccentPrimary
                isPast -> AppTheme.TextMuted
                else -> AppTheme.TextSecondary
            },
            modifier = Modifier.width(60.dp)
        )
        Text(
            summary,
            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            color = if (isCurrent) AppTheme.TextPrimary else AppTheme.TextMuted,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun SudarshanaInfoDialog(onDismiss: () -> Unit) {
    val language = currentLanguage()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.CardBackground,
        title = {
            Text(
                stringResource(StringKeyDosha.SUDARSHANA_ABOUT),
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                stringResource(StringKeyDosha.SUDARSHANA_ABOUT_DESC),
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(StringKey.BTN_CLOSE), color = AppTheme.AccentPrimary)
            }
        }
    )
}

// Helper functions
private fun getPlanetColor(planet: Planet): Color {
    return when (planet) {
        Planet.SUN -> com.astro.vajra.ui.theme.PlanetSun
        Planet.MOON -> com.astro.vajra.ui.theme.PlanetMercury
        Planet.MARS -> com.astro.vajra.ui.theme.MarsRed
        Planet.MERCURY -> com.astro.vajra.ui.theme.PlanetMercury
        Planet.JUPITER -> com.astro.vajra.ui.theme.VedicGold
        Planet.VENUS -> com.astro.vajra.ui.theme.PlanetVenus
        Planet.SATURN -> com.astro.vajra.ui.theme.SlateDark
        Planet.RAHU -> com.astro.vajra.ui.theme.PlanetRahu
        Planet.KETU -> com.astro.vajra.ui.theme.PlanetVenus
        else -> com.astro.vajra.ui.theme.SlateMuted
    }
}

private fun getStrengthColor(score: Double): Color {
    return when {
        score >= 70 -> com.astro.vajra.ui.theme.PlanetMercury
        score >= 50 -> com.astro.vajra.ui.theme.VedicGold
        else -> com.astro.vajra.ui.theme.MarsRed
    }
}

private fun resolveZoneId(timezone: String): ZoneId {
    return try {
        ZoneId.of(timezone)
    } catch (_: DateTimeException) {
        val trimmed = timezone.trim()
        val numericHours = trimmed.toDoubleOrNull()
        if (numericHours != null) {
            val totalSeconds = (numericHours * 3600.0).roundToInt()
            ZoneOffset.ofTotalSeconds(totalSeconds.coerceIn(-18 * 3600, 18 * 3600))
        } else {
            ZoneId.systemDefault()
        }
    }
}




