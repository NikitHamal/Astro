package com.astro.vajra.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyMatch
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.data.localization.currentLanguage
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.varshaphala.*
import com.astro.vajra.ephemeris.varshaphala.VarshaphalaCalculator
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.TabItem
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ephemeris.varshaphala.TajikaYogaCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DateTimeException
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.roundToInt
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import com.astro.vajra.ui.theme.PoppinsFontFamily
import com.astro.vajra.ui.components.common.vedicCornerMarkers

/**
 * VarshaphalaScreen - Annual Horoscope (Tajika System)
 *
 * This screen uses VarshaphalaCalculator for all astronomical calculations
 * and displays comprehensive annual horoscope with:
 * - Solar Return Chart
 * - Year Lord & Muntha
 * - Tajika Aspects & Yogas
 * - Sahams (Arabic Parts)
 * - Mudda Dasha (Annual Periods)
 * - House-wise Predictions
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VarshaphalaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val calculator = remember { VarshaphalaCalculator(context) }
    val zoneId = remember(chart) { resolveZoneId(chart?.birthData?.timezone) }

    val currentYear = LocalDate.now(zoneId).year
    val birthYear = chart?.birthData?.dateTime?.year ?: currentYear
    var selectedYear by remember { mutableIntStateOf(currentYear) }
    var varshaphalaResult by remember { mutableStateOf<VarshaphalaResult?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        stringResource(StringKeyMatch.TAB_OVERVIEW),
        stringResource(StringKeyMatch.TAB_TAJIKA),
        stringResource(StringKeyMatch.TAB_SAHAMS),
        stringResource(StringKeyMatch.TAB_DASHA),
        stringResource(StringKeyMatch.TAB_HOUSES)
    )

    val lang = currentLanguage()
    LaunchedEffect(chart, selectedYear, lang) {
        if (chart != null && selectedYear >= birthYear) {
            isLoading = true
            error = null
            withContext(Dispatchers.IO) {
                try {
                    varshaphalaResult = calculator.calculateVarshaphala(chart, selectedYear, lang)
                } catch (e: Exception) {
                    error = StringResources.get(StringKeyAnalysis.ERROR_CALCULATION, lang, e.message ?: "Unknown error")
                }
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyMatch.VARSHAPHALA_TITLE),
                subtitle = varshaphalaResult?.let { stringResource(StringKey.VARSHAPHALA_AGE, it.age) },
                onBack = onBack,
                actions = {
                    varshaphalaResult?.let { result ->
                        YearRatingBadge(result.yearRating)
                    }
                }
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { paddingValues ->
        if (chart == null) {
            EmptyState()
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            YearSelector(
                currentYear = selectedYear,
                birthYear = birthYear,
                referenceYear = currentYear,
                onYearChange = { selectedYear = it }
            )

            if (isLoading) {
                LoadingState()
                return@Scaffold
            }

            error?.let { errorMsg ->
                ErrorState(errorMsg) { selectedYear = currentYear }
                return@Scaffold
            }

            val tabItems = tabs.map { TabItem(title = it, accentColor = AppTheme.AccentPrimary) }
            
            ModernPillTabRow(
                tabs = tabItems,
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.3f))

            varshaphalaResult?.let { result ->
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        val direction = if (targetState > initialState) 1 else -1
                        slideInHorizontally { direction * it / 4 } + fadeIn() togetherWith
                                slideOutHorizontally { -direction * it / 4 } + fadeOut()
                    },
                    label = "tab_transition"
                ) { tab ->
                    when (tab) {
                        0 -> OverviewTab(result)
                        1 -> TajikaAspectsTab(result)
                        2 -> SahamsTab(result)
                        3 -> DashaTab(result)
                        4 -> HousesTab(result)
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// COMMON UI COMPONENTS
// -----------------------------------------------------------------------------

@Composable
private fun YearRatingBadge(rating: Float) {
    val color = when {
        rating >= 4.0f -> AppTheme.SuccessColor
        rating >= 3.0f -> AppTheme.AccentGold
        rating >= 2.0f -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }

    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Star,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                String.format("%.1f", rating),
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun YearSelector(
    currentYear: Int,
    birthYear: Int,
    referenceYear: Int,
    onYearChange: (Int) -> Unit
) {
    val maxYear = referenceYear + 10
    val years = (birthYear..maxYear).toList()
    val scrollState = rememberScrollState()
    val currentYearIndex = years.indexOf(currentYear)

    LaunchedEffect(currentYearIndex) {
        if (currentYearIndex >= 0) {
            scrollState.animateScrollTo(currentYearIndex * 80)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (currentYear > birthYear) onYearChange(currentYear - 1) },
                    enabled = currentYear > birthYear
                ) {
                    Icon(
                        Icons.Filled.ChevronLeft,
                        contentDescription = stringResource(StringKeyMatch.NAV_PREVIOUS_YEAR),
                        tint = if (currentYear > birthYear) AppTheme.TextPrimary else AppTheme.TextSubtle
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        stringResource(StringKeyAnalysis.VARSHAPHALA_YEAR_RANGE, currentYear, currentYear + 1),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S21,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        stringResource(StringKeyMatch.VARSHAPHALA_YEAR_OF_LIFE, currentYear - birthYear + 1),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                }

                IconButton(
                    onClick = { if (currentYear < maxYear) onYearChange(currentYear + 1) },
                    enabled = currentYear < maxYear
                ) {
                    Icon(
                        Icons.Filled.ChevronRight,
                        contentDescription = stringResource(StringKeyMatch.NAV_NEXT_YEAR),
                        tint = if (currentYear < maxYear) AppTheme.TextPrimary else AppTheme.TextSubtle
                    )
                }
            }

            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                years.forEach { year ->
                    val isSelected = year == currentYear
                    val isFuture = year > referenceYear

                    com.astro.vajra.ui.components.common.NeoVedicChoicePill(
                        selected = isSelected,
                        onClick = { onYearChange(year) },
                        label = {
                            Text(
                                year.toString(),
                                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.2f),
                            selectedLabelColor = AppTheme.AccentPrimary
                        ),
                        border = if (isFuture) FilterChipDefaults.filterChipBorder(
                            borderColor = AppTheme.AccentGold.copy(alpha = 0.5f),
                            enabled = true,
                            selected = isSelected
                        ) else null
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                Icons.Outlined.WbSunny,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(StringKeyAnalysis.VARSHAPHALA_NO_CHART),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(StringKeyAnalysis.VARSHAPHALA_NO_CHART_DESC),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun resolveZoneId(timezone: String?): ZoneId {
    if (timezone.isNullOrBlank()) return ZoneId.systemDefault()
    return try {
        ZoneId.of(timezone)
    } catch (_: DateTimeException) {
        val numericHours = timezone.trim().toDoubleOrNull()
        if (numericHours != null) {
            val totalSeconds = (numericHours * 3600.0).roundToInt()
            ZoneOffset.ofTotalSeconds(totalSeconds.coerceIn(-18 * 3600, 18 * 3600))
        } else {
            ZoneId.systemDefault()
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = AppTheme.AccentPrimary,
                modifier = Modifier.size(48.dp),
                strokeWidth = 3.dp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                stringResource(StringKey.VARSHAPHALA_COMPUTING),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(StringKey.VARSHAPHALA_COMPUTING_DESC),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                Icons.Filled.ErrorOutline,
                contentDescription = null,
                tint = AppTheme.ErrorColor,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(StringKey.VARSHAPHALA_ERROR),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.ErrorColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                message,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                onClick = onRetry,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppTheme.AccentPrimary)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(StringKeyAnalysis.VARSHA_RESET_YEAR))
            }
        }
    }
}

// -----------------------------------------------------------------------------
// OVERVIEW TAB
// -----------------------------------------------------------------------------

@Composable
private fun OverviewTab(result: VarshaphalaResult) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item { SolarReturnCard(result) }
        item { YearLordMunthaCard(result) }
        item { AnnualChartVisualization(result) }
        item { PanchaVargiyaBalaCard(result) }
        item { TriPatakiChakraCard(result) }
        item { MajorThemesCard(result) }
        item { MonthsCard(result) }
        item { KeyDatesCard(result) }
        item { OverallPredictionCard(result) }
    }
}

@Composable
private fun SolarReturnCard(result: VarshaphalaResult) {
    val locale = varshaphalaLocale()
    val dateFormatter = DateTimeFormatter.ofPattern(stringResource(StringKeyAnalysis.VARSHA_FORMAT_DATE_FULL), locale)
    val timeFormatter = DateTimeFormatter.ofPattern(stringResource(StringKeyAnalysis.VARSHA_FORMAT_TIME_FULL), locale)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    AppTheme.PlanetSun,
                                    AppTheme.PlanetSun.copy(alpha = 0.3f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.WbSunny,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(StringKey.VARSHAPHALA_SOLAR_RETURN),
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        stringResource(StringKeyAnalysis.VARSHAPHALA_SUN_RETURNS),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        stringResource(StringKeyAnalysis.VARSHAPHALA_AGE_FORMAT, result.age),
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S21,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.AccentPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(StringKeyAnalysis.VARSHAPHALA_RETURN_DATE),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        result.solarReturnChart.solarReturnTime.format(dateFormatter),
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        result.solarReturnChart.solarReturnTime.format(timeFormatter),
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                InfoChip(
                    label = stringResource(StringKey.CHART_ASCENDANT),
                    value = stringResource(
                        StringKeyAnalysis.VARSHA_SIGN_DEGREE_FORMAT,
                        result.solarReturnChart.ascendant.getLocalizedName(currentLanguage()),
                        result.solarReturnChart.ascendantDegree
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                InfoChip(
                    label = stringResource(StringKey.PLANET_MOON),
                    value = result.solarReturnChart.moonSign.getLocalizedName(currentLanguage()),
                    subValue = result.solarReturnChart.moonNakshatra,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun InfoChip(
    label: String,
    value: String,
    subValue: String? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = AppTheme.ChipBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                label,
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                color = AppTheme.TextMuted
            )
            Text(
                value,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            subValue?.let {
                Text(
                    it,
                    fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun YearLordMunthaCard(result: VarshaphalaResult) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .vedicCornerMarkers(color = AppTheme.AccentGold, 12.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Year Lord Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(getPlanetColor(result.yearLord).copy(alpha = 0.15f))
                            .border(
                                width = 3.dp,
                                color = getPlanetColor(result.yearLord),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            result.yearLord.symbol,
                            fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S21,
                            fontWeight = FontWeight.Bold,
                            color = getPlanetColor(result.yearLord)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            stringResource(StringKey.VARSHAPHALA_YEAR_LORD),
                            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = AppTheme.TextMuted
                        )
                        Text(
                            result.yearLord.getLocalizedName(currentLanguage()),
                            fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S21,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StrengthBadge(result.yearLordStrength, currentLanguage())
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(StringKeyAnalysis.VARSHAPHALA_IN_HOUSE, result.yearLordHouse),
                                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                color = AppTheme.TextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                result.yearLordDignity,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(20.dp))

            // Muntha Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    AppTheme.AccentGold,
                                    AppTheme.AccentGold.copy(alpha = 0.6f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Adjust,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                stringResource(StringKey.VARSHAPHALA_MUNTHA),
                                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                color = AppTheme.TextMuted
                            )
                            Text(
                                result.muntha.sign.getLocalizedName(currentLanguage()),
                                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S21,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.TextPrimary
                            )
                            Text(
                                stringResource(
                                    StringKeyAnalysis.VARSHA_MUNTHA_POS_FORMAT,
                                    result.muntha.house,
                                    result.muntha.degree
                                ),
                                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                color = AppTheme.TextSecondary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                stringResource(StringKeyAnalysis.VARSHAPHALA_LORD_PREFIX, result.muntha.lord.getLocalizedName(currentLanguage())),
                                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                color = getPlanetColor(result.muntha.lord),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                stringResource(StringKeyAnalysis.VARSHAPHALA_IN_HOUSE, result.muntha.lordHouse),
                                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                color = AppTheme.TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(result.muntha.themes) { theme ->
                            Surface(
                                color = AppTheme.AccentGold.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                            ) {
                                Text(
                                    theme,
                                    fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                    color = AppTheme.AccentGold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        result.muntha.interpretation,
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StrengthBadge(strength: String, language: com.astro.vajra.core.common.Language) {
    Surface(
        color = getStrengthColor(strength, language).copy(alpha = 0.15f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Text(
            strength,
            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
            fontWeight = FontWeight.SemiBold,
            color = getStrengthColor(strength, language),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
private fun AnnualChartVisualization(result: VarshaphalaResult) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .vedicCornerMarkers(color = AppTheme.AccentPrimary, 12.dp)
            .clickable { isExpanded = !isExpanded },
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.GridView,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        stringResource(StringKey.VARSHAPHALA_TAJIKA_CHART),
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }

                Icon(
                    if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    SouthIndianChart(
                        planetPositions = result.solarReturnChart.planetPositions,
                        ascendantSign = result.solarReturnChart.ascendant,
                        munthaSign = result.muntha.sign,
                        title = stringResource(StringKeyMatch.VARSHAPHALA_TITLE),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ChartLegendItem(stringResource(StringKeyAnalysis.VARSHAPHALA_CHART_LEGEND_ASC), AppTheme.AccentPrimary)
                        ChartLegendItem(stringResource(StringKeyAnalysis.VARSHAPHALA_CHART_LEGEND_MUNTHA), AppTheme.AccentGold)
                        ChartLegendItem(stringResource(StringKeyAnalysis.VARSHAPHALA_CHART_LEGEND_BENEFIC), AppTheme.SuccessColor)
                        ChartLegendItem(stringResource(StringKeyAnalysis.VARSHAPHALA_CHART_LEGEND_MALEFIC), AppTheme.ErrorColor)
                    }
                }
            }
        }
    }
}

@Composable
private fun SouthIndianChart(
    planetPositions: Map<Planet, SolarReturnPlanetPosition>,
    ascendantSign: ZodiacSign,
    munthaSign: ZodiacSign,
    title: String,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    // Read colors outside Canvas
    val cardBackgroundElevated = AppTheme.CardBackgroundElevated
    val dividerColor = AppTheme.DividerColor
    val textMuted = AppTheme.TextMuted
    val accentPrimary = AppTheme.AccentPrimary
    val accentGold = AppTheme.AccentGold
    val warningColor = AppTheme.WarningColor
    val cardBackground = AppTheme.CardBackground
    val textPrimary = AppTheme.TextPrimary

    // Precompute planet colors
    val planetColors = planetPositions.keys.associateWith { planet -> getPlanetColor(planet) }

    Canvas(modifier = modifier) {
        val cellSize = size.width / 4
        val strokeWidth = 2.dp.toPx()

        drawRect(
            color = cardBackgroundElevated,
            size = size
        )

        // Draw grid
        for (i in 0..4) {
            drawLine(
                color = dividerColor,
                start = Offset(i * cellSize, 0f),
                end = Offset(i * cellSize, size.height),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = dividerColor,
                start = Offset(0f, i * cellSize),
                end = Offset(size.width, i * cellSize),
                strokeWidth = strokeWidth
            )
        }

        // House positions for South Indian chart
        val housePositions = listOf(
            Pair(1, Offset(1.5f * cellSize, 0.5f * cellSize)),
            Pair(2, Offset(0.5f * cellSize, 0.5f * cellSize)),
            Pair(3, Offset(0.5f * cellSize, 1.5f * cellSize)),
            Pair(4, Offset(0.5f * cellSize, 2.5f * cellSize)),
            Pair(5, Offset(0.5f * cellSize, 3.5f * cellSize)),
            Pair(6, Offset(1.5f * cellSize, 3.5f * cellSize)),
            Pair(7, Offset(2.5f * cellSize, 3.5f * cellSize)),
            Pair(8, Offset(3.5f * cellSize, 3.5f * cellSize)),
            Pair(9, Offset(3.5f * cellSize, 2.5f * cellSize)),
            Pair(10, Offset(3.5f * cellSize, 1.5f * cellSize)),
            Pair(11, Offset(3.5f * cellSize, 0.5f * cellSize)),
            Pair(12, Offset(2.5f * cellSize, 0.5f * cellSize))
        )

        val signOrder = ZodiacSign.entries
        val ascIndex = signOrder.indexOf(ascendantSign)

        // Draw house signs and highlights
        housePositions.forEach { (house, position) ->
            val signIndex = (ascIndex + house - 1) % 12
            val sign = signOrder[signIndex]

            val textLayout = textMeasurer.measure(
                text = getZodiacSymbol(sign),
                style = TextStyle(
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = textMuted
                )
            )

            val textX = position.x - cellSize / 2 + 8.dp.toPx()
            val textY = position.y - cellSize / 2 + 8.dp.toPx()

            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(textX, textY)
            )

            if (sign == ascendantSign) {
                drawCircle(
                    color = accentPrimary.copy(alpha = 0.2f),
                    radius = cellSize / 3,
                    center = position
                )
            }

            if (sign == munthaSign) {
                drawCircle(
                    color = accentGold,
                    radius = 6.dp.toPx(),
                    center = Offset(position.x + cellSize / 3, position.y - cellSize / 3),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }

        // Draw planets
        planetPositions.forEach { (planet, pos) ->
            val houseIndex = pos.house - 1
            if (houseIndex in 0..11) {
                val basePosition = housePositions[houseIndex].second
                val planetIndex = planetPositions.filter { it.value.house == pos.house }
                    .keys.toList().indexOf(planet)

                // Arrange planets in a circle around the center of the cell
                val totalPlanetsInHouse = planetPositions.count { it.value.house == pos.house }
                
                // Offset calculation (circular distribution if multiple planets)
                val offsetX = if (totalPlanetsInHouse == 1) 0f else {
                    val angle = (planetIndex.toFloat() / totalPlanetsInHouse) * 2 * Math.PI
                    (Math.cos(angle) * 16.dp.toPx()).toFloat()
                }
                
                val offsetY = if (totalPlanetsInHouse == 1) 0f else {
                    val angle = (planetIndex.toFloat() / totalPlanetsInHouse) * 2 * Math.PI
                    (Math.sin(angle) * 16.dp.toPx()).toFloat()
                }

                val planetPos = Offset(
                    basePosition.x + offsetX,
                    basePosition.y + offsetY
                )

                val textLayout = textMeasurer.measure(
                    text = planet.symbol,
                    style = TextStyle(
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.Bold,
                        color = planetColors[planet] ?: textPrimary
                    )
                )

                if (pos.isRetrograde) {
                    drawCircle(
                        color = warningColor.copy(alpha = 0.3f),
                        radius = 12.dp.toPx(),
                        center = planetPos
                    )
                }

                drawText(
                    textLayoutResult = textLayout,
                    topLeft = Offset(
                        planetPos.x - textLayout.size.width / 2,
                        planetPos.y - textLayout.size.height / 2
                    )
                )
            }
        }

        // Draw center box
        drawRect(
            color = cardBackground,
            topLeft = Offset(cellSize, cellSize),
            size = Size(cellSize * 2, cellSize * 2)
        )

        val titleLayout = textMeasurer.measure(
            text = title,
            style = TextStyle(
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
        )
        drawText(
            textLayoutResult = titleLayout,
            topLeft = Offset(
                (size.width - titleLayout.size.width) / 2,
                size.height / 2 - titleLayout.size.height
            )
        )
    }
}

@Composable
private fun ChartLegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            label,
            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun PanchaVargiyaBalaCard(result: VarshaphalaResult) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Assessment,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            stringResource(StringKey.VARSHAPHALA_PANCHA_VARGIYA),
                            fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            stringResource(StringKeyAnalysis.VARSHAPHALA_FIVEFOLD_STRENGTH),
                            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Icon(
                    if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    result.panchaVargiyaBala.forEach { bala ->
                        PlanetBalaRow(bala)
                    }
                }
            }
        }
    }
}

@Composable
private fun PlanetBalaRow(bala: PanchaVargiyaBala) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            bala.planet.symbol,
            fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
            fontWeight = FontWeight.Bold,
            color = getPlanetColor(bala.planet),
            modifier = Modifier.width(32.dp)
        )

        Text(
            bala.planet.getLocalizedName(currentLanguage()),
            fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
            color = AppTheme.TextPrimary,
            modifier = Modifier.width(80.dp)
        )

        LinearProgressIndicator(
            progress = { (bala.total.toFloat() / 20f).coerceIn(0f, 1f) },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)),
            color = getStrengthColor(bala.category, currentLanguage()),
            trackColor = AppTheme.DividerColor
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            String.format("%.1f", bala.total),
            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.width(36.dp)
        )

        Surface(
            color = getStrengthColor(bala.category, currentLanguage()).copy(alpha = 0.15f),
            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
        ) {
            Text(
                bala.category.take(3),
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                color = getStrengthColor(bala.category, currentLanguage()),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun TriPatakiChakraCard(result: VarshaphalaResult) {
    var isExpanded by remember { mutableStateOf(false) }
    val chakra = result.triPatakiChakra

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Hub,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            stringResource(StringKey.VARSHAPHALA_TRI_PATAKI),
                            fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            stringResource(StringKeyAnalysis.VARSHAPHALA_THREE_FLAG),
                            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Icon(
                    if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = AppTheme.AccentPrimary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                Text(
                    chakra.dominantInfluence,
                    fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.AccentPrimary,
                    modifier = Modifier.padding(12.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    chakra.sectors.forEach { sector ->
                        TriPatakiSectorRow(sector)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        chakra.interpretation,
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun TriPatakiSectorRow(sector: TriPatakiSector) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.ChipBackground, RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius))
            .padding(12.dp)
    ) {
        Text(
            sector.name,
            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (sector.planets.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                sector.planets.forEach { planet ->
                    Text(
                        planet.symbol,
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = getPlanetColor(planet),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            Text(
                stringResource(StringKeyAnalysis.VARSHAPHALA_NO_PLANETS),
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                color = AppTheme.TextMuted
            )
        }

        Text(
            sector.influence,
            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
            color = AppTheme.TextSecondary
        )
    }
}

@Composable
private fun MajorThemesCard(result: VarshaphalaResult) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    stringResource(StringKey.VARSHAPHALA_MAJOR_THEMES),
                    fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            result.majorThemes.forEachIndexed { index, theme ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        color = AppTheme.AccentGold.copy(alpha = 0.15f),
                        shape = CircleShape,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(
                                "${index + 1}",
                                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.AccentGold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        theme,
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = AppTheme.TextPrimary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthsCard(result: VarshaphalaResult) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    stringResource(StringKey.VARSHAPHALA_MONTHLY_OUTLOOK),
                    fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(StringKey.VARSHAPHALA_FAVORABLE),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.SuccessColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(result.favorableMonths) { month ->
                            Surface(
                                color = AppTheme.SuccessColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                            ) {
                                Text(
                                    getMonthName(month),
                                    fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                    color = AppTheme.SuccessColor,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(StringKeyAnalysis.VARSHAPHALA_CHALLENGING),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.WarningColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(result.challengingMonths) { month ->
                            Surface(
                                color = AppTheme.WarningColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                            ) {
                                Text(
                                    getMonthName(month),
                                    fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                    color = AppTheme.WarningColor,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyDatesCard(result: VarshaphalaResult) {
    var isExpanded by remember { mutableStateOf(false) }
    val locale = varshaphalaLocale()
    val dateFormatter = DateTimeFormatter.ofPattern(stringResource(StringKeyAnalysis.VARSHA_FORMAT_DATE_FULL), locale)
    val shortDateFormatter = DateTimeFormatter.ofPattern(stringResource(StringKeyAnalysis.VARSHA_FORMAT_DATE_SHORT), locale)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Event,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            stringResource(StringKey.VARSHAPHALA_KEY_DATES),
                            fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            stringResource(StringKeyAnalysis.VARSHAPHALA_IMPORTANT_DATES, result.keyDates.size),
                            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Icon(
                    if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    result.keyDates.take(10).forEach { keyDate ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            val color = when (keyDate.type) {
                                KeyDateType.FAVORABLE -> AppTheme.SuccessColor
                                KeyDateType.CHALLENGING -> AppTheme.WarningColor
                                KeyDateType.IMPORTANT -> AppTheme.AccentPrimary
                                KeyDateType.TRANSIT -> AppTheme.AccentGold
                            }

                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(color, CircleShape)
                                    .align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    keyDate.event,
                                    fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                    fontWeight = FontWeight.Medium,
                                    color = AppTheme.TextPrimary
                                )
                                Text(
                                    keyDate.description,
                                    fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                    color = AppTheme.TextSecondary
                                )
                            }
                            Text(
                                keyDate.date.format(dateFormatter),
                                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                color = AppTheme.TextMuted
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun OverallPredictionCard(result: VarshaphalaResult) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Insights,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    stringResource(StringKey.VARSHAPHALA_OVERALL_PREDICTION),
                    fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                result.overallPrediction,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}

// -----------------------------------------------------------------------------
// TAJIKA ASPECTS TAB
// -----------------------------------------------------------------------------

@Composable
private fun TajikaAspectsTab(result: VarshaphalaResult) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            TajikaYogasCard(result.tajikaYogas)
        }
        item {
            TajikaAspectsHeader(result.tajikaAspects)
        }

        items(result.tajikaAspects) { aspect ->
            TajikaAspectCard(aspect)
        }
    }
}

@Composable
private fun TajikaAspectsHeader(aspects: List<TajikaAspectResult>) {
    val positive = aspects.count { it.type.isPositive }
    val challenging = aspects.size - positive

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                stringResource(StringKey.VARSHAPHALA_TAJIKA_SUMMARY),
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "$positive",
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S21,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.SuccessColor
                    )
                    Text(
                        stringResource(StringKey.VARSHAPHALA_FAVORABLE),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "$challenging",
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S21,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.WarningColor
                    )
                    Text(
                        stringResource(StringKeyAnalysis.VARSHAPHALA_CHALLENGING),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${aspects.size}",
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S21,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.AccentPrimary
                    )
                    Text(
                        stringResource(StringKeyAnalysis.VARSHAPHALA_TOTAL),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun TajikaAspectCard(aspect: TajikaAspectResult) {
    val language = currentLanguage()
    val color = if (aspect.type.isPositive) AppTheme.SuccessColor else AppTheme.WarningColor

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .vedicCornerMarkers(color = AppTheme.PlanetSun, 12.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        aspect.planet1.symbol,
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                        fontWeight = FontWeight.Bold,
                        color = getPlanetColor(aspect.planet1)
                    )
                    Text(
                        " \u2194 ",
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        aspect.planet2.symbol,
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                        fontWeight = FontWeight.Bold,
                        color = getPlanetColor(aspect.planet2)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Surface(
                        color = color.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                    ) {
                        Text(
                            aspect.type.getDisplayName(language),
                            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            fontWeight = FontWeight.SemiBold,
                            color = color,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Text(
                    stringResource(StringKeyAnalysis.VARSHA_SIGN_DEGREE_FORMAT, aspect.aspectAngle, aspect.orb),
                    fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                aspect.effectDescription,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(StringKeyAnalysis.VARSHAPHALA_HOUSES_PREFIX) + aspect.relatedHouses.joinToString(", "),
                    fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted
                )
                Text(
                    aspect.strength.getDisplayName(language),
                    fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    fontWeight = FontWeight.Medium,
                    color = getAspectStrengthColor(aspect.strength)
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SAHAMS TAB
// -----------------------------------------------------------------------------

@Composable
private fun SahamsTab(result: VarshaphalaResult) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            SahamsHeader()
        }

        items(result.sahams) { saham ->
            SahamCard(saham)
        }
    }
}

@Composable
private fun SahamsHeader() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Stars,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    stringResource(StringKey.VARSHAPHALA_SAHAMS_TITLE),
                    fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                stringResource(StringKey.VARSHAPHALA_SAHAMS_DESC),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun SahamCard(saham: SahamResult) {
    val isActive = saham.isActive

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        color = if (isActive) AppTheme.CardBackground else AppTheme.CardBackground.copy(alpha = 0.7f),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        saham.name,
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        saham.sanskritName,
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                }

                if (isActive) {
                    Surface(
                        color = AppTheme.SuccessColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                    ) {
                        Text(
                            stringResource(StringKeyAnalysis.VARSHAPHALA_ACTIVE),
                            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.SuccessColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                InfoChip(
                    label = stringResource(StringKey.VARSHAPHALA_POSITION),
                    value = stringResource(
                        StringKeyAnalysis.VARSHA_SIGN_DEGREE_FORMAT,
                        saham.sign.getLocalizedName(currentLanguage()),
                        saham.degree
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                InfoChip(
                    label = stringResource(StringKey.VARSHAPHALA_HOUSE),
                    value = "${saham.house}",
                    subValue = stringResource(StringKeyAnalysis.VARSHAPHALA_LORD_PREFIX, saham.lord.getLocalizedName(currentLanguage())),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                saham.interpretation,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

// -----------------------------------------------------------------------------
// DASHA TAB
// -----------------------------------------------------------------------------

@Composable
private fun DashaTab(result: VarshaphalaResult) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            DashaHeader()
        }

        items(result.muddaDasha) { period ->
            MuddaDashaPeriodCard(period)
        }
    }
}

@Composable
private fun DashaHeader() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Timeline,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    stringResource(StringKey.VARSHAPHALA_MUDDA_DASHA),
                    fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                stringResource(StringKey.VARSHAPHALA_MUDDA_DASHA_DESC),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun MuddaDashaPeriodCard(period: MuddaDashaPeriod) {
    val locale = varshaphalaLocale()
    val shortDateFormatter = DateTimeFormatter.ofPattern(stringResource(StringKeyAnalysis.VARSHA_FORMAT_DATE_SHORT), locale)
    var isExpanded by remember { mutableStateOf(period.isCurrent) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { isExpanded = !isExpanded },
        color = if (period.isCurrent)
                AppTheme.AccentPrimary.copy(alpha = 0.1f)
            else AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = if (period.isCurrent) androidx.compose.foundation.BorderStroke(
            1.dp, AppTheme.AccentPrimary.copy(alpha = 0.5f)
        ) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(getPlanetColor(period.planet).copy(alpha = 0.15f))
                        .border(2.dp, getPlanetColor(period.planet), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        period.planet.symbol,
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                        fontWeight = FontWeight.Bold,
                        color = getPlanetColor(period.planet)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            period.planet.getLocalizedName(currentLanguage()),
                            fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        if (period.isCurrent) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = AppTheme.AccentPrimary,
                                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                            ) {
                                Text(
                                    stringResource(StringKeyAnalysis.VARSHAPHALA_CURRENT),
                                    fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        "${period.startDate.format(shortDateFormatter)} - ${period.endDate.format(shortDateFormatter)}",
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        stringResource(StringKeyAnalysis.VARSHAPHALA_DAYS_FORMAT, period.days),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    StrengthBadge(period.planetStrength, currentLanguage())
                }
            }

            if (period.isCurrent) {
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { period.progressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                    color = AppTheme.AccentPrimary,
                    trackColor = AppTheme.DividerColor
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(period.keywords) { keyword ->
                            Surface(
                                color = getPlanetColor(period.planet).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                            ) {
                                Text(
                                    keyword,
                                    fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                    color = getPlanetColor(period.planet),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        period.prediction,
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary,
                        lineHeight = 18.sp
                    )

                    if (period.houseRuled.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(StringKeyAnalysis.VARSHAPHALA_RULES_HOUSES, period.houseRuled.joinToString(", ")),
                            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = AppTheme.TextMuted
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// HOUSES TAB
// -----------------------------------------------------------------------------

@Composable
private fun HousesTab(result: VarshaphalaResult) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        items(result.housePredictions) { prediction ->
            HousePredictionCard(prediction)
        }
    }
}

@Composable
private fun HousePredictionCard(prediction: HousePrediction) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { isExpanded = !isExpanded },
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(AppTheme.AccentPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "${prediction.house}",
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.AccentPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(StringKeyAnalysis.VARSHAPHALA_HOUSE_SIGN, prediction.house, prediction.signOnCusp.getLocalizedName(currentLanguage())),
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        stringResource(StringKeyAnalysis.VARSHAPHALA_LORD_IN_HOUSE, prediction.houseLord.getLocalizedName(currentLanguage()), prediction.lordPosition),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = if (index < prediction.rating)
                                    AppTheme.AccentGold
                                else AppTheme.DividerColor,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                    StrengthBadge(prediction.strength, currentLanguage())
                }
            }

            if (prediction.planetsInHouse.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        stringResource(StringKeyAnalysis.VARSHAPHALA_PLANETS),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                    prediction.planetsInHouse.forEach { planet ->
                        Text(
                            planet.symbol,
                            fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            fontWeight = FontWeight.Bold,
                            color = getPlanetColor(planet)
                        )
                    }
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(prediction.keywords) { keyword ->
                            Surface(
                                color = AppTheme.ChipBackground,
                                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
                            ) {
                                Text(
                                    keyword,
                                    fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                    color = AppTheme.TextSecondary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        prediction.prediction,
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary,
                        lineHeight = 18.sp
                    )

                    if (prediction.specificEvents.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(StringKeyAnalysis.VARSHAPHALA_SPECIFIC_INDICATIONS),
                            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        prediction.specificEvents.forEach { event ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    "\u2022 ",
                                    fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                    color = AppTheme.AccentPrimary
                                )
                                Text(
                                    event,
                                    fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// HELPER FUNCTIONS
// -----------------------------------------------------------------------------

@Composable
private fun getPlanetColor(planet: Planet): Color {
    return when (planet) {
        Planet.SUN -> AppTheme.PlanetSun
        Planet.MOON -> AppTheme.PlanetMoon
        Planet.MARS -> AppTheme.PlanetMars
        Planet.MERCURY -> AppTheme.PlanetMercury
        Planet.JUPITER -> AppTheme.PlanetJupiter
        Planet.VENUS -> AppTheme.PlanetVenus
        Planet.SATURN -> AppTheme.PlanetSaturn
        Planet.RAHU -> AppTheme.PlanetRahu
        Planet.KETU -> AppTheme.PlanetKetu
        else -> AppTheme.TextPrimary
    }
}

@Composable
private fun getStrengthColor(strength: String, language: com.astro.vajra.core.common.Language): Color {
    val excellent = stringResource(StringKeyAnalysis.VARSHA_STRENGTH_EXCELLENT)
    val strong = stringResource(StringKeyAnalysis.VARSHA_STRENGTH_STRONG)
    val exalted = stringResource(StringKeyAnalysis.VARSHA_STRENGTH_EXALTED)
    val moderate = stringResource(StringKeyAnalysis.VARSHA_STRENGTH_MODERATE)
    val weak = stringResource(StringKeyAnalysis.VARSHA_STRENGTH_WEAK)
    val challenged = stringResource(StringKeyAnalysis.VARSHA_STRENGTH_CHALLENGED)
    val debilitated = stringResource(StringKeyAnalysis.VARSHA_STRENGTH_DEBILITATED)

    return when {
        strength.equals(excellent, ignoreCase = true) || strength.equals(exalted, ignoreCase = true) -> AppTheme.SuccessColor
        strength.equals(strong, ignoreCase = true) -> AppTheme.SuccessColor
        strength.equals(moderate, ignoreCase = true) -> AppTheme.AccentGold
        strength.equals(weak, ignoreCase = true) -> AppTheme.WarningColor
        strength.equals(challenged, ignoreCase = true) || strength.equals(debilitated, ignoreCase = true) -> AppTheme.ErrorColor
        // Fallback for English literals if resources not matching exactly
        strength.contains("Excellent", ignoreCase = true) || strength.contains("Exalted", ignoreCase = true) -> AppTheme.SuccessColor
        strength.contains("Strong", ignoreCase = true) -> AppTheme.SuccessColor
        strength.contains("Moderate", ignoreCase = true) -> AppTheme.AccentGold
        strength.contains("Weak", ignoreCase = true) -> AppTheme.WarningColor
        strength.contains("Challenged", ignoreCase = true) || strength.contains("Debilitated", ignoreCase = true) -> AppTheme.ErrorColor
        else -> AppTheme.TextMuted
    }
}

@Composable
private fun getAspectStrengthColor(strength: AspectStrength): Color {
    return when (strength) {
        AspectStrength.VERY_STRONG -> AppTheme.SuccessColor
        AspectStrength.STRONG -> AppTheme.SuccessColor
        AspectStrength.MODERATE -> AppTheme.AccentGold
        AspectStrength.WEAK -> AppTheme.WarningColor
        AspectStrength.VERY_WEAK -> AppTheme.ErrorColor
    }
}

private fun getZodiacSymbol(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "\u2648"
        ZodiacSign.TAURUS -> "\u2649"
        ZodiacSign.GEMINI -> "\u264A"
        ZodiacSign.CANCER -> "\u264B"
        ZodiacSign.LEO -> "\u264C"
        ZodiacSign.VIRGO -> "\u264D"
        ZodiacSign.LIBRA -> "\u264E"
        ZodiacSign.SCORPIO -> "\u264F"
        ZodiacSign.SAGITTARIUS -> "\u2650"
        ZodiacSign.CAPRICORN -> "\u2651"
        ZodiacSign.AQUARIUS -> "\u2652"
        ZodiacSign.PISCES -> "\u2653"
    }
}

@Composable
private fun varshaphalaLocale(): Locale {
    val language = currentLanguage()
    return remember(language) {
        if (language == com.astro.vajra.core.common.Language.NEPALI) {
            Locale.forLanguageTag("ne-NP")
        } else {
            Locale.ENGLISH
        }
    }
}

@Composable
private fun getMonthName(month: Int): String {
    return when (month) {
        1 -> stringResource(StringKeyAnalysis.MONTH_JAN)
        2 -> stringResource(StringKeyAnalysis.MONTH_FEB)
        3 -> stringResource(StringKeyAnalysis.MONTH_MAR)
        4 -> stringResource(StringKeyAnalysis.MONTH_APR)
        5 -> stringResource(StringKeyAnalysis.MONTH_MAY)
        6 -> stringResource(StringKeyAnalysis.MONTH_JUN)
        7 -> stringResource(StringKeyAnalysis.MONTH_JUL)
        8 -> stringResource(StringKeyAnalysis.MONTH_AUG)
        9 -> stringResource(StringKeyAnalysis.MONTH_SEP)
        10 -> stringResource(StringKeyAnalysis.MONTH_OCT)
        11 -> stringResource(StringKeyAnalysis.MONTH_NOV)
        12 -> stringResource(StringKeyAnalysis.MONTH_DEC)
        else -> ""
    }
}

@Composable
private fun TajikaYogasCard(analysis: TajikaYogaCalculator.TajikaYogaAnalysis) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Stars,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            stringResource(StringKeyAnalysis.VARSHAPHALA_TAJIKA_YOGAS),
                            fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            if (analysis.yogasFound.isEmpty()) 
                                stringResource(StringKeyAnalysis.VARSHAPHALA_NO_MAJOR_YOGAS) 
                            else 
                                stringResource(StringKeyAnalysis.VARSHAPHALA_YOGAS_ACTIVE_FMT, analysis.yogasFound.size),
                            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = AppTheme.TextMuted
                        )
                    }
                }
                Icon(
                     if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                     contentDescription = null,
                     tint = AppTheme.TextMuted
                )
            }
            
            if (!isExpanded && analysis.yogasFound.isNotEmpty()) {
                 Spacer(modifier = Modifier.height(12.dp))
                 val topYoga = analysis.yogasFound.maxByOrNull { it.strength }
                 topYoga?.let { yoga ->
                     Text(
                         "${yoga.yogaType.displayName} (${String.format("%.0f", yoga.strength)})",
                         fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                         color = AppTheme.AccentPrimary
                     )
                 }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    if (analysis.yogasFound.isEmpty()) {
                        Text(
                            stringResource(StringKeyAnalysis.VARSHAPHALA_NO_YOGAS_DESC),
                            fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            color = AppTheme.TextSecondary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    } else {
                        analysis.yogasFound.sortedByDescending { it.strength }.forEachIndexed { index, yoga ->
                            TajikaYogaItem(yoga)
                            if (index < analysis.yogasFound.size - 1) {
                                HorizontalDivider(
                                     modifier = Modifier.padding(vertical = 12.dp),
                                     color = AppTheme.DividerColor.copy(alpha = 0.3f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TajikaYogaItem(yoga: TajikaYogaCalculator.TajikaYoga) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                yoga.yogaType.displayName,
                fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                fontWeight = FontWeight.Bold,
                color = if (yoga.strength >= 7) AppTheme.SuccessColor else AppTheme.TextPrimary
            )
            Surface(
                color = AppTheme.SurfaceColor,
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius)
            ) {
                 Text(
                     "${stringResource(StringKeyAnalysis.VARSHAPHALA_STRENGTH_SHORT)} ${String.format("%.1f", yoga.strength)}",
                     fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                     modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                     color = AppTheme.TextSecondary
                 )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
             "${yoga.primaryPlanet.symbol} ${yoga.aspect.displayName} ${yoga.secondaryPlanet.symbol}",   
             fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
             color = AppTheme.TextMuted
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            yoga.interpretation,
            fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
            color = AppTheme.TextSecondary,
            lineHeight = 18.sp
        )
    }
}








