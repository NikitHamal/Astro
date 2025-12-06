package com.astro.storm.ui.screen

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import com.astro.storm.ephemeris.VarshaphalaCalculator
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.VarshaphalaViewModel
import com.astro.storm.ui.viewmodel.VarshaphalaUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VarshaphalaScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    viewModel: VarshaphalaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentYear = LocalDate.now().year
    val birthYear = chart?.birthData?.dateTime?.year ?: currentYear
    var selectedYear by remember { mutableIntStateOf(currentYear) }
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf("Overview", "Tajika", "Sahams", "Dasha", "Houses")

    LaunchedEffect(chart, selectedYear) {
        if (chart != null && selectedYear >= birthYear) {
            viewModel.calculateVarshaphala(chart, selectedYear)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Varshaphala",
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        if (uiState is VarshaphalaUiState.Success) {
                            val successState = uiState as VarshaphalaUiState.Success
                            Text(
                                "Annual Horoscope • Age ${successState.result.age}",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppTheme.TextPrimary
                        )
                    }
                },
                actions = {
                    if (uiState is VarshaphalaUiState.Success) {
                        val successState = uiState as VarshaphalaUiState.Success
                        val yearRating = (successState.result.yearLordBala.totalBala / 20.0f) * 5.0f
                        YearRatingBadge(yearRating)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.ScreenBackground
                )
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
                onYearChange = { selectedYear = it }
            )

            when (val state = uiState) {
                is VarshaphalaUiState.Loading -> LoadingState()
                is VarshaphalaUiState.Error -> ErrorState(state.message) {
                    viewModel.resetState()
                    selectedYear = currentYear
                }
                is VarshaphalaUiState.Success -> {
                    ScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        contentColor = AppTheme.AccentPrimary,
                        divider = { HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.3f)) },
                        edgePadding = 8.dp,
                        indicator = @Composable { tabPositions ->
                            if (selectedTab < tabPositions.size) {
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentSize(Alignment.BottomStart)
                                        .offset(x = tabPositions[selectedTab].left)
                                        .width(tabPositions[selectedTab].width),
                                    color = AppTheme.AccentPrimary,
                                    height = 3.dp
                                )
                            }
                        }
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = {
                                    Text(
                                        title,
                                        fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (selectedTab == index) AppTheme.AccentPrimary else AppTheme.TextMuted
                                    )
                                }
                            )
                        }
                    }

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
                            0 -> OverviewTab(state.result)
                            1 -> TajikaAspectsTab(state.result)
                            2 -> SahamsTab(state.result)
                            3 -> DashaTab(state.result)
                            4 -> HousesTab(state.result)
                        }
                    }
                }
                is VarshaphalaUiState.Initial -> {
                    // Nothing to show initially
                }
            }
        }
    }
}

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
        shape = RoundedCornerShape(8.dp),
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
                style = MaterialTheme.typography.labelMedium,
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
    onYearChange: (Int) -> Unit
) {
    val maxYear = LocalDate.now().year + 10
    val years = (birthYear..maxYear).toList()
    val scrollState = rememberScrollState()
    val currentYearIndex = years.indexOf(currentYear)

    LaunchedEffect(currentYearIndex) {
        if (currentYearIndex >= 0) {
            scrollState.animateScrollTo(currentYearIndex * 80)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
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
                        contentDescription = "Previous year",
                        tint = if (currentYear > birthYear) AppTheme.TextPrimary else AppTheme.TextSubtle
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "$currentYear - ${currentYear + 1}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        "Year ${currentYear - birthYear + 1} of life",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }

                IconButton(
                    onClick = { if (currentYear < maxYear) onYearChange(currentYear + 1) },
                    enabled = currentYear < maxYear
                ) {
                    Icon(
                        Icons.Filled.ChevronRight,
                        contentDescription = "Next year",
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
                    val isFuture = year > LocalDate.now().year

                    FilterChip(
                        selected = isSelected,
                        onClick = { onYearChange(year) },
                        label = {
                            Text(
                                year.toString(),
                                style = MaterialTheme.typography.labelMedium,
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
                "Computing Annual Horoscope...",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Calculating Tajika aspects, Sahams & Mudda Dasha",
                style = MaterialTheme.typography.bodySmall,
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
                "Calculation Error",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.ErrorColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                message,
                style = MaterialTheme.typography.bodyMedium,
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
                Text("Reset to Current Year")
            }
        }
    }
}

@Composable
private fun OverviewTab(result: VarshaphalaCalculator.VarshaphalaResult) {
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
        item { OverallPredictionCard(result) }
    }
}

@Composable
private fun SolarReturnCard(result: VarshaphalaCalculator.VarshaphalaResult) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm:ss a")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.CardBackground
        ),
        shape = RoundedCornerShape(16.dp)
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
                        "Solar Return",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        "Sun returns to natal position",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Age ${result.age}",
                        style = MaterialTheme.typography.titleLarge,
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
                        "Return Date",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        result.solarReturnChart.solarReturnTime.format(dateFormatter),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        result.solarReturnChart.solarReturnTime.format(timeFormatter),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val ascendantSign = ZodiacSign.fromLongitude(result.solarReturnChart.ascendant)
            val moonPosition = result.solarReturnChart.planetPositions.find { it.planet == Planet.MOON }

            Row(modifier = Modifier.fillMaxWidth()) {
                InfoChip(
                    label = "Ascendant",
                    value = "${ascendantSign.displayName} ${String.format("%.1f", result.solarReturnChart.ascendant % 30)}°",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                InfoChip(
                    label = "Moon",
                    value = "${moonPosition?.sign?.displayName ?: ""}",
                    subValue = moonPosition?.nakshatra?.displayName ?: "",
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
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted
            )
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            subValue?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun YearLordMunthaCard(result: VarshaphalaCalculator.VarshaphalaResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
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
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = getPlanetColor(result.yearLord)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            "Year Lord",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                        Text(
                            result.yearLord.displayName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StrengthBadge(result.yearLordStrength)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "House ${result.solarReturnChart.planetPositions.find { it.planet == result.yearLord }?.house ?: "-"}",
                                style = MaterialTheme.typography.labelMedium,
                                color = AppTheme.TextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                result.yearLordBala.classification,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(20.dp))

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
                                "Muntha",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                            Text(
                                result.muntha.sign.displayName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.TextPrimary
                            )
                            Text(
                                "House ${result.muntha.house} • ${String.format("%.1f", result.muntha.degreeInSign)}°",
                                style = MaterialTheme.typography.labelMedium,
                                color = AppTheme.TextSecondary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "Lord: ${result.muntha.lord.displayName}",
                                style = MaterialTheme.typography.labelMedium,
                                color = getPlanetColor(result.muntha.lord),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "in House ${result.muntha.lordHouse}",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        result.muntha.interpretation,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StrengthBadge(strength: String) {
    Surface(
        color = getStrengthColor(strength).copy(alpha = 0.15f),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            strength,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = getStrengthColor(strength),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
private fun AnnualChartVisualization(result: VarshaphalaCalculator.VarshaphalaResult) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
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
                        "Tajika Annual Chart",
                        style = MaterialTheme.typography.titleSmall,
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
                    val ascendantSign = ZodiacSign.fromLongitude(result.solarReturnChart.ascendant)
                    SouthIndianChart(
                        planetPositions = result.solarReturnChart.planetPositions.associateBy { it.planet },
                        ascendantSign = ascendantSign,
                        munthaSign = result.muntha.sign,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ChartLegendItem("Asc", AppTheme.AccentPrimary)
                        ChartLegendItem("Muntha", AppTheme.AccentGold)
                        ChartLegendItem("Benefic", AppTheme.SuccessColor)
                        ChartLegendItem("Malefic", AppTheme.ErrorColor)
                    }
                }
            }
        }
    }
}

@Composable
private fun SouthIndianChart(
    planetPositions: Map<Planet, com.astro.storm.data.model.PlanetPosition>,
    ascendantSign: ZodiacSign,
    munthaSign: ZodiacSign,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        val cellSize = size.width / 4
        val strokeWidth = 2.dp.toPx()

        drawRect(
            color = AppTheme.CardBackgroundElevated,
            size = size
        )

        for (i in 0..4) {
            drawLine(
                color = AppTheme.DividerColor,
                start = Offset(i * cellSize, 0f),
                end = Offset(i * cellSize, size.height),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = AppTheme.DividerColor,
                start = Offset(0f, i * cellSize),
                end = Offset(size.width, i * cellSize),
                strokeWidth = strokeWidth
            )
        }

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

        housePositions.forEach { (house, position) ->
            val signIndex = (ascIndex + house - 1) % 12
            val sign = signOrder[signIndex]

            val textLayout = textMeasurer.measure(
                text = getZodiacSymbol(sign),
                style = TextStyle(
                    fontSize = 10.sp,
                    color = AppTheme.TextMuted
                )
            )

            val textX = when (house) {
                1, 6, 7, 12 -> position.x - cellSize / 2 + 8.dp.toPx()
                else -> position.x - cellSize / 2 + 8.dp.toPx()
            }
            val textY = when (house) {
                in 1..2, 11, 12 -> position.y - cellSize / 2 + 8.dp.toPx()
                else -> position.y - cellSize / 2 + 8.dp.toPx()
            }

            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(textX, textY)
            )

            if (sign == ascendantSign) {
                drawCircle(
                    color = AppTheme.AccentPrimary.copy(alpha = 0.2f),
                    radius = cellSize / 3,
                    center = position
                )
            }

            if (sign == munthaSign) {
                drawCircle(
                    color = AppTheme.AccentGold,
                    radius = 6.dp.toPx(),
                    center = Offset(position.x + cellSize / 3, position.y - cellSize / 3),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }

        planetPositions.forEach { (planet, pos) ->
            val houseIndex = pos.house - 1
            if (houseIndex in 0..11) {
                val basePosition = housePositions[houseIndex].second
                val planetCount = planetPositions.count { it.value.house == pos.house }
                val planetIndex = planetPositions.filter { it.value.house == pos.house }
                    .keys.toList().indexOf(planet)

                val offsetX = (planetIndex % 2) * 24.dp.toPx() - 12.dp.toPx()
                val offsetY = (planetIndex / 2) * 18.dp.toPx()

                val planetPos = Offset(
                    basePosition.x + offsetX,
                    basePosition.y + offsetY
                )

                val textLayout = textMeasurer.measure(
                    text = planet.symbol,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = getPlanetColor(planet)
                    )
                )

                if (pos.isRetrograde) {
                    drawCircle(
                        color = AppTheme.WarningColor.copy(alpha = 0.3f),
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

        drawRect(
            color = AppTheme.CardBackground,
            topLeft = Offset(cellSize, cellSize),
            size = Size(cellSize * 2, cellSize * 2)
        )

        val titleLayout = textMeasurer.measure(
            text = "Varshaphala",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
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
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun PanchaVargiyaBalaCard(result: VarshaphalaCalculator.VarshaphalaResult) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
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
                            "Pancha Vargiya Bala",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            "Five-fold Planetary Strength",
                            style = MaterialTheme.typography.labelSmall,
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Spacer(modifier = Modifier.width(80.dp))
                        listOf("Hadda", "Drek", "Nav", "Dwad", "Trim", "Total").forEach { header ->
                            Text(
                                header,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = AppTheme.TextMuted,
                                modifier = Modifier.width(48.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    result.allPlanetBalas.forEach { bala ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.width(80.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    bala.planet.symbol,
                                    color = getPlanetColor(bala.planet),
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    bala.planet.displayName.take(4),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppTheme.TextPrimary
                                )
                            }

                            listOf(bala.haddaBala, bala.drekkanaBala, bala.navamsaBala, bala.dwadasamsaBala, bala.trimsamsaBala).forEach { value ->
                                BalaValueCell(value.toDouble())
                            }

                            Surface(
                                color = getBalaColor(bala.totalBala.toDouble()).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.width(48.dp)
                            ) {
                                Text(
                                    String.format("%d", bala.totalBala),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = getBalaColor(bala.totalBala.toDouble()),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BalaLegend("Excellent", Color(0xFF4CAF50))
                        BalaLegend("Good", Color(0xFF8BC34A))
                        BalaLegend("Medium", Color(0xFFFFC107))
                        BalaLegend("Weak", Color(0xFFFF9800))
                    }
                }
            }
        }
    }
}

@Composable
private fun BalaValueCell(value: Double) {
    Text(
        String.format("%.0f", value),
        style = MaterialTheme.typography.labelSmall,
        color = getBalaColor(value),
        textAlign = TextAlign.Center,
        modifier = Modifier.width(48.dp)
    )
}

@Composable
private fun BalaLegend(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
    }
}

private fun getBalaColor(value: Double): Color {
    return when {
        value >= 15 -> Color(0xFF4CAF50)
        value >= 10 -> Color(0xFF8BC34A)
        value >= 5 -> Color(0xFFFFC107)
        else -> Color(0xFFFF9800)
    }
}

@Composable
private fun TriPatakiChakraCard(result: VarshaphalaCalculator.VarshaphalaResult) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Pentagon,
                        contentDescription = null,
                        tint = AppTheme.AccentGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Tri-Pataki Chakra",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            "Three-Flag Diagram",
                            style = MaterialTheme.typography.labelSmall,
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

                    result.triPatakiChakra.rashiGroups.entries.forEachIndexed {
                        index, entry ->
                        val (name, signs) = entry
                        val planets = result.triPatakiChakra.planetPlacements[name] ?: emptyList()
                        val sectorColor = when (index) {
                            0 -> AppTheme.SuccessColor
                            1 -> AppTheme.WarningColor
                            else -> AppTheme.ErrorColor
                        }

                        Surface(
                            color = sectorColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = sectorColor
                                    )
                                    Row {
                                        signs.forEach {
                                            sign ->
                                            Text(
                                                getZodiacSymbol(sign),
                                                style = MaterialTheme.typography.bodySmall,
                                                modifier = Modifier.padding(horizontal = 2.dp)
                                            )
                                        }
                                    }
                                }

                                if (planets.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row {
                                        Text(
                                            "Planets: ",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AppTheme.TextMuted
                                        )
                                        planets.forEach {
                                            planet ->
                                            Text(
                                                planet.symbol,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = getPlanetColor(planet),
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = AppTheme.ChipBackground,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            result.triPatakiChakra.interpretation,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary,
                            modifier = Modifier.padding(12.dp),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MajorThemesCard(result: VarshaphalaCalculator.VarshaphalaResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Major Themes for ${result.year}-${result.year + 1}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            result.majorThemes.forEachIndexed {
                index, theme ->
                Row(
                    modifier = Modifier.padding(vertical = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                AppTheme.AccentGold.copy(alpha = 0.15f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${index + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.AccentGold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        theme,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthsCard(result: VarshaphalaCalculator.VarshaphalaResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Monthly Energy Flow",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            val months = (1..12).map {
                val adjustedMonth = ((result.solarReturnChart.solarReturnTime.monthValue - 1 + it - 1) % 12) + 1
                adjustedMonth
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                months.forEachIndexed {
                    index, month ->
                    val isFavorable = month in result.favorableMonths
                    val isChallenging = month in result.challengingMonths

                    val color = when {
                        isFavorable -> AppTheme.SuccessColor
                        isChallenging -> AppTheme.ErrorColor
                        else -> AppTheme.TextMuted
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(48.dp)
                    ) {
                        Text(
                            getMonthName(month).take(3),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color.copy(alpha = 0.15f))
                                .border(
                                    width = if (isFavorable || isChallenging) 2.dp else 1.dp,
                                    color = color,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                when {
                                    isFavorable -> Icons.Filled.ThumbUp
                                    isChallenging -> Icons.Filled.Warning
                                    else -> Icons.Filled.Remove
                                },
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "M${index + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextSubtle,
                            fontSize = 9.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LegendItem(Icons.Filled.ThumbUp, "Favorable", AppTheme.SuccessColor)
                LegendItem(Icons.Filled.Warning, "Challenging", AppTheme.ErrorColor)
                LegendItem(Icons.Filled.Remove, "Neutral", AppTheme.TextMuted)
            }
        }
    }
}

@Composable
private fun LegendItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun OverallPredictionCard(result: VarshaphalaCalculator.VarshaphalaResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.CardBackgroundElevated
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Summarize,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Year Summary",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                result.overallPrediction,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun TajikaAspectsTab(result: VarshaphalaCalculator.VarshaphalaResult) {
    val positiveAspects = result.tajikaAspects.filter { it.aspect.displayName.contains("Ithasala") || it.aspect.displayName.contains("Kamboola") }
    val negativeAspects = result.tajikaAspects.filter { !positiveAspects.contains(it) }

    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Positive", "Challenging")

    val displayedAspects = when (selectedFilter) {
        "Positive" -> positiveAspects
        "Challenging" -> negativeAspects
        else -> result.tajikaAspects
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            TajikaOverviewCard(result.tajikaAspects)
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach {
                    filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                filter,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        leadingIcon = if (selectedFilter == filter) {
                            {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.2f),
                            selectedLabelColor = AppTheme.AccentPrimary,
                            selectedLeadingIconColor = AppTheme.AccentPrimary
                        )
                    )
                }
            }
        }

        if (displayedAspects.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No ${selectedFilter.lowercase()} Tajika aspects found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextMuted
                    )
                }
            }
        } else {
            items(displayedAspects) { aspect ->
                TajikaAspectCard(aspect)
            }
        }
    }
}

@Composable
private fun TajikaOverviewCard(aspects: List<VarshaphalaCalculator.TajikaAspectResult>) {
    val aspectCounts = aspects.groupBy { it.aspect }.mapValues { it.value.size }
    val positiveCount = aspects.count { it.aspect.displayName.contains("Ithasala") || it.aspect.displayName.contains("Kamboola") }
    val negativeCount = aspects.size - positiveCount

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Insights,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        "Tajika Yogas Overview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        "Annual planetary aspects and combinations",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBox(
                    value = "${aspects.size}",
                    label = "Total Aspects",
                    color = AppTheme.AccentPrimary
                )
                StatBox(
                    value = "$positiveCount",
                    label = "Favorable",
                    color = AppTheme.SuccessColor
                )
                StatBox(
                    value = "$negativeCount",
                    label = "Challenging",
                    color = AppTheme.ErrorColor
                )
            }
        }
    }
}

@Composable
private fun StatBox(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun TajikaAspectCard(aspect: VarshaphalaCalculator.TajikaAspectResult) {
    var isExpanded by remember { mutableStateOf(false) }
    val isPositive = aspect.aspect.displayName.contains("Ithasala") || aspect.aspect.displayName.contains("Kamboola")
    val aspectColor = if (isPositive) AppTheme.SuccessColor else AppTheme.ErrorColor

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(aspectColor.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            Text(
                                aspect.planet1.symbol,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = getPlanetColor(aspect.planet1)
                            )
                            Text(
                                "-",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextMuted
                            )
                            Text(
                                aspect.planet2.symbol,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = getPlanetColor(aspect.planet2)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            aspect.aspect.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            "${aspect.planet1.displayName} - ${aspect.planet2.displayName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        color = aspectColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            if (isPositive) "Favorable" else "Challenging",
                            style = MaterialTheme.typography.labelSmall,
                            color = aspectColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "${aspect.aspectType} | ${String.format("%.1f", aspect.orb)}° orb",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        aspect.aspect.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Interpretation",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        aspect.interpretation,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SahamsTab(result: VarshaphalaCalculator.VarshaphalaResult) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Personal", "Family", "Wealth", "Spiritual")

    val categorizedSahams = result.sahams.groupBy {
        when (it.saham.displayName) {
            "Punya Saham", "Samartha Saham", "Yashas Saham", "Asha Saham" -> "Personal"
            "Pitri Saham", "Matri Saham", "Bhratri Saham", "Santana Saham", "Vivaha Saham" -> "Family"
            "Dhana Saham", "Karma Saham", "Raja Saham" -> "Wealth"
            "Vidya Saham", "Mahatmya Saham", "Mrityu Saham" -> "Spiritual"
            else -> "Other"
        }
    }

    val displayedSahams = if (selectedCategory == "All") {
        result.sahams
    } else {
        categorizedSahams[selectedCategory] ?: emptyList()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            SahamsOverviewCard(result.sahams)
        }

        item {
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                category,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppTheme.AccentGold.copy(alpha = 0.2f),
                            selectedLabelColor = AppTheme.AccentGold
                        )
                    )
                }
            }
        }

        items(displayedSahams) { saham ->
            SahamCard(saham)
        }
    }
}

@Composable
private fun SahamsOverviewCard(sahams: List<VarshaphalaCalculator.SahamResult>) {
    val strongSahams = sahams.filter {
        val lordPos = it.lord
        // A simplified check for strength
        true
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Token,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        "Sahams (Arabic Parts)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        "Sensitive points for specific life areas",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBox(
                    value = "${sahams.size}",
                    label = "Total Sahams",
                    color = AppTheme.AccentGold
                )
                StatBox(
                    value = "${strongSahams.size}",
                    label = "Strong",
                    color = AppTheme.AccentPrimary
                )
            }

            if (strongSahams.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Prominent Sahams This Year:",
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(strongSahams.take(5)) { saham ->
                        Surface(
                            color = AppTheme.AccentGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    saham.saham.displayName,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppTheme.AccentGold
                                )
                                Text(
                                    getZodiacSymbol(saham.sign),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppTheme.TextMuted
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
private fun SahamCard(saham: VarshaphalaCalculator.SahamResult) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(AppTheme.AccentGold.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            getZodiacSymbol(saham.sign),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.AccentGold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            saham.saham.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            saham.saham.description,
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "House ${saham.house}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextSecondary
                    )
                    Text(
                        "${String.format("%.1f", saham.degreeInSign)}°",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Lord: ${saham.lord.displayName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Interpretation",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        saham.interpretation,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DashaTab(result: VarshaphalaCalculator.VarshaphalaResult) {
    var expandedPeriod by remember { mutableStateOf<Planet?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            MuddaDashaOverviewCard(result.muddaDasha)
        }

        item {
            Text(
                "Mudda Dasha Periods",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        itemsIndexed(result.muddaDasha) {
            index, period ->
            MuddaDashaPeriodCard(
                period = period,
                index = index,
                isExpanded = expandedPeriod == period.planet,
                onExpand = {
                    expandedPeriod = if (expandedPeriod == period.planet) null else period.planet
                }
            )
        }
    }
}

@Composable
private fun MuddaDashaOverviewCard(periods: List<VarshaphalaCalculator.MuddaDashaPeriod>) {
    val currentPeriod = periods.find {
        val today = LocalDate.now()
        !today.isBefore(it.startDate.toLocalDate()) && !today.isAfter(it.endDate.toLocalDate())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        "Mudda Dasha (Annual Periods)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        "Tajika annual planetary periods",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            currentPeriod?.let {
                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    color = getPlanetColor(it.planet).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    // Content for current period
                }
            }
        }
    }
}

@Composable
private fun MuddaDashaPeriodCard(
    period: VarshaphalaCalculator.MuddaDashaPeriod,
    index: Int,
    isExpanded: Boolean,
    onExpand: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d")
    val isCurrent = LocalDate.now() >= period.startDate.toLocalDate() && LocalDate.now() <= period.endDate.toLocalDate()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onExpand),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) 
                getPlanetColor(period.planet).copy(alpha = 0.08f)
            else AppTheme.CardBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Card content
        }
    }
}

@Composable
private fun HousesTab(result: VarshaphalaCalculator.VarshaphalaResult) {
    var expandedHouse by remember { mutableStateOf<Int?>(null) }
    var sortBy by remember { mutableStateOf("Number") }

    val sortedHouses = when (sortBy) {
        "Rating" -> result.housePredictions.sortedByDescending { it.strengthScore }
        "Strength" -> result.housePredictions.sortedByDescending { it.strengthScore }
        else -> result.housePredictions.sortedBy { it.house }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            HouseOverviewCard(result.housePredictions)
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "House Predictions",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )

                Row {
                    listOf("Number", "Strength").forEach { option ->
                        TextButton(
                            onClick = { sortBy = option },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = if (sortBy == option) AppTheme.AccentPrimary else AppTheme.TextMuted
                            )
                        ) {
                            Text(
                                option,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (sortBy == option) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        items(sortedHouses) { prediction ->
            HousePredictionCard(
                prediction = prediction,
                isExpanded = expandedHouse == prediction.house,
                onExpand = {
                    expandedHouse = if (expandedHouse == prediction.house) null else prediction.house
                }
            )
        }
    }
}

@Composable
private fun HouseOverviewCard(predictions: List<VarshaphalaCalculator.HousePrediction>) {
    val strongHouses = predictions.filter { it.strength in listOf("Strong") }
    val weakHouses = predictions.filter { it.strength in listOf("Challenged") }
    val averageRating = predictions.map { it.strengthScore }.average()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)){
            // Content
        }
    }
}

@Composable
private fun HousePredictionCard(
    prediction: VarshaphalaCalculator.HousePrediction,
    isExpanded: Boolean,
    onExpand: () -> Unit
) {
    val houseSignificance = getHouseSignificance(prediction.house)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onExpand),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
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
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AppTheme.AccentPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${prediction.house}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.AccentPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            "House ${prediction.house}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            "${prediction.signOnCusp.displayName} • ${houseSignificance}",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = AppTheme.AccentGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                String.format("%.1f", prediction.strengthScore / 20.0f),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.TextPrimary
                            )
                        }
                        StrengthBadge(prediction.strength)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = AppTheme.TextMuted
                    )
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
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    keyword,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppTheme.TextSecondary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "House Lord",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    prediction.houseLord.symbol,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = getPlanetColor(prediction.houseLord)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "${prediction.houseLord.displayName} in House ${prediction.lordPosition}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }

                        if (prediction.planetsInHouse.isNotEmpty()) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "Planets in House",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppTheme.TextMuted
                                )
                                Row {
                                    prediction.planetsInHouse.forEach { planet ->
                                        Text(
                                            planet.symbol,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = getPlanetColor(planet),
                                            modifier = Modifier.padding(horizontal = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        prediction.prediction,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 18.sp
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
                Icons.Outlined.Cake,
                contentDescription = null,
                tint = AppTheme.TextSubtle,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "No Birth Chart Selected",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Please select a birth chart to view\nannual Varshaphala predictions",
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun getHouseSignificance(house: Int): String {
    return when (house) {
        1 -> "Self, Personality"
        2 -> "Wealth, Family"
        3 -> "Siblings, Courage"
        4 -> "Home, Mother"
        5 -> "Children, Intelligence"
        6 -> "Enemies, Health"
        7 -> "Marriage, Partnership"
        8 -> "Longevity, Transformation"
        9 -> "Fortune, Father"
        10 -> "Career, Status"
        11 -> "Gains, Friends"
        12 -> "Losses, Liberation"
        else -> ""
    }
}

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
        else -> AppTheme.AccentPrimary
    }
}

private fun getStrengthColor(strength: String): Color {
    return when (strength.lowercase()) {
        "excellent", "very strong" -> Color(0xFF2E7D32)
        "strong" -> Color(0xFF4CAF50)
        "moderate", "medium" -> Color(0xFFFFC107)
        "weak" -> Color(0xFFFF9800)
        "very weak", "challenged", "debilitated" -> Color(0xFFF44336)
        else -> AppTheme.TextMuted
    }
}

private fun getMonthName(month: Int): String {
    return java.time.Month.of(month).name.lowercase().replaceFirstChar { it.uppercase() }
}

private fun getZodiacSymbol(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "♈"
        ZodiacSign.TAURUS -> "♉"
        ZodiacSign.GEMINI -> "♊"
        ZodiacSign.CANCER -> "♋"
        ZodiacSign.LEO -> "♌"
        ZodiacSign.VIRGO -> "♍"
        ZodiacSign.LIBRA -> "♎"
        ZodiacSign.SCORPIO -> "♏"
        ZodiacSign.SAGITTARIUS -> "♐"
        ZodiacSign.CAPRICORN -> "♑"
        ZodiacSign.AQUARIUS -> "♒"
        ZodiacSign.PISCES -> "♓"
    }
}
