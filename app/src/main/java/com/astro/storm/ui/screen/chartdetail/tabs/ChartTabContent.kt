package com.astro.storm.ui.screen.chartdetail.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.R
import com.astro.storm.data.model.PlanetPosition
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import com.astro.storm.ephemeris.DivisionalChartCalculator
import com.astro.storm.ephemeris.DivisionalChartData
import com.astro.storm.ephemeris.DivisionalChartType
import com.astro.storm.ui.chart.ChartRenderer
import com.astro.storm.ui.screen.chartdetail.ChartDetailColors
import com.astro.storm.util.formatter.ChartFormatter
import com.astro.storm.util.formatter.PlanetFormatter
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ChartTabContent(
    chart: VedicChart,
    chartRenderer: ChartRenderer,
    onChartClick: (String, DivisionalChartData?) -> Unit,
    onPlanetClick: (PlanetPosition) -> Unit,
    onHouseClick: (Int) -> Unit
) {
    val divisionalCharts = remember(chart) {
        DivisionalChartCalculator.calculateAllDivisionalCharts(chart)
    }

    var selectedChartType by rememberSaveable { mutableStateOf("D1") }
    val expandedCardTitles = remember { mutableStateListOf<String>() }

    val currentChartData = remember(selectedChartType, divisionalCharts) {
        getChartDataForType(selectedChartType, divisionalCharts)
    }

    val chartInfo = remember(selectedChartType) {
        getChartInfo(selectedChartType)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ChartTypeSelector(
                selectedType = selectedChartType,
                onTypeSelected = { selectedChartType = it }
            )
        }

        item {
            MainChartCard(
                chart = chart,
                chartRenderer = chartRenderer,
                chartInfo = chartInfo,
                selectedChartType = selectedChartType,
                currentChartData = currentChartData,
                onChartClick = onChartClick
            )
        }

        item {
            ChartDetailsCard(
                chart = chart,
                currentChartData = currentChartData,
                selectedChartType = selectedChartType,
                onPlanetClick = onPlanetClick
            )
        }

        item {
            HouseCuspsCard(
                chart = chart,
                onHouseClick = onHouseClick,
                isExpanded = "HouseCusps" in expandedCardTitles,
                onToggleExpand = {
                    if (it) {
                        expandedCardTitles.add("HouseCusps")
                    } else {
                        expandedCardTitles.remove("HouseCusps")
                    }
                }
            )
        }

        item {
            BirthDetailsCard(
                chart = chart,
                isExpanded = "BirthDetails" in expandedCardTitles,
                onToggleExpand = {
                    if (it) {
                        expandedCardTitles.add("BirthDetails")
                    } else {
                        expandedCardTitles.remove("BirthDetails")
                    }
                }
            )
        }

        item {
            AstronomicalDataCard(
                chart = chart,
                isExpanded = "AstronomicalData" in expandedCardTitles,
                onToggleExpand = {
                    if (it) {
                        expandedCardTitles.add("AstronomicalData")
                    } else {
                        expandedCardTitles.remove("AstronomicalData")
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private fun getChartDataForType(
    type: String,
    divisionalCharts: List<DivisionalChartData>
): DivisionalChartData? {
    return when (type) {
        "D1" -> null
        "D2" -> divisionalCharts.find { it.chartType == DivisionalChartType.D2_HORA }
        "D3" -> divisionalCharts.find { it.chartType == DivisionalChartType.D3_DREKKANA }
        "D4" -> divisionalCharts.find { it.chartType == DivisionalChartType.D4_CHATURTHAMSA }
        "D7" -> divisionalCharts.find { it.chartType == DivisionalChartType.D7_SAPTAMSA }
        "D9" -> divisionalCharts.find { it.chartType == DivisionalChartType.D9_NAVAMSA }
        "D10" -> divisionalCharts.find { it.chartType == DivisionalChartType.D10_DASAMSA }
        "D12" -> divisionalCharts.find { it.chartType == DivisionalChartType.D12_DWADASAMSA }
        "D16" -> divisionalCharts.find { it.chartType == DivisionalChartType.D16_SHODASAMSA }
        "D20" -> divisionalCharts.find { it.chartType == DivisionalChartType.D20_VIMSAMSA }
        "D24" -> divisionalCharts.find { it.chartType == DivisionalChartType.D24_CHATURVIMSAMSA }
        "D27" -> divisionalCharts.find { it.chartType == DivisionalChartType.D27_SAPTAVIMSAMSA }
        "D30" -> divisionalCharts.find { it.chartType == DivisionalChartType.D30_TRIMSAMSA }
        "D60" -> divisionalCharts.find { it.chartType == DivisionalChartType.D60_SHASHTIAMSA }
        else -> null
    }
}

@Composable
private fun getChartInfo(type: String): Triple<String, String, String> {
    val nameRes = when (type) {
        "D1" -> R.string.d_chart_d1_name
        "D2" -> R.string.d_chart_d2_name
        "D3" -> R.string.d_chart_d3_name
        "D4" -> R.string.d_chart_d4_name
        "D7" -> R.string.d_chart_d7_name
        "D9" -> R.string.d_chart_d9_name
        "D10" -> R.string.d_chart_d10_name
        "D12" -> R.string.d_chart_d12_name
        "D16" -> R.string.d_chart_d16_name
        "D20" -> R.string.d_chart_d20_name
        "D24" -> R.string.d_chart_d24_name
        "D27" -> R.string.d_chart_d27_name
        "D30" -> R.string.d_chart_d30_name
        "D60" -> R.string.d_chart_d60_name
        else -> R.string.d_chart_unknown_name
    }
    val purposeRes = when (type) {
        "D1" -> R.string.d_chart_d1_purpose
        "D2" -> R.string.d_chart_d2_purpose
        "D3" -> R.string.d_chart_d3_purpose
        "D4" -> R.string.d_chart_d4_purpose
        "D7" -> R.string.d_chart_d7_purpose
        "D9" -> R.string.d_chart_d9_purpose
        "D10" -> R.string.d_chart_d10_purpose
        "D12" -> R.string.d_chart_d12_purpose
        "D16" -> R.string.d_chart_d16_purpose
        "D20" -> R.string.d_chart_d20_purpose
        "D24" -> R.string.d_chart_d24_purpose
        "D27" -> R.string.d_chart_d27_purpose
        "D30" -> R.string.d_chart_d30_purpose
        "D60" -> R.string.d_chart_d60_purpose
        else -> R.string.d_chart_unknown_purpose
    }
    return Triple(stringResource(id = nameRes), stringResource(id = purposeRes), type)
}

@Composable
private fun ChartTypeSelector(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    val chartTypes = listOf(
        "D1" to R.string.d_chart_d1_short,
        "D2" to R.string.d_chart_d2_short,
        "D3" to R.string.d_chart_d3_short,
        "D4" to R.string.d_chart_d4_short,
        "D7" to R.string.d_chart_d7_short,
        "D9" to R.string.d_chart_d9_short,
        "D10" to R.string.d_chart_d10_short,
        "D12" to R.string.d_chart_d12_short,
        "D16" to R.string.d_chart_d16_short,
        "D20" to R.string.d_chart_d20_short,
        "D24" to R.string.d_chart_d24_short,
        "D27" to R.string.d_chart_d27_short,
        "D30" to R.string.d_chart_d30_short,
        "D60" to R.string.d_chart_d60_short
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(chartTypes) { (type, nameRes) ->
            FilterChip(
                selected = selectedType == type,
                onClick = { onTypeSelected(type) },
                label = { Text(text = stringResource(id = nameRes), fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ChartDetailColors.AccentGold.copy(alpha = 0.2f),
                    selectedLabelColor = ChartDetailColors.AccentGold,
                    containerColor = ChartDetailColors.CardBackground,
                    labelColor = ChartDetailColors.TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = ChartDetailColors.DividerColor,
                    selectedBorderColor = ChartDetailColors.AccentGold,
                    enabled = true,
                    selected = selectedType == type
                )
            )
        }
    }
}

@Composable
private fun MainChartCard(
    chart: VedicChart,
    chartRenderer: ChartRenderer,
    chartInfo: Triple<String, String, String>,
    selectedChartType: String,
    currentChartData: DivisionalChartData?,
    onChartClick: (String, DivisionalChartData?) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChartClick(chartInfo.first, currentChartData) },
        shape = RoundedCornerShape(16.dp),
        color = ChartDetailColors.CardBackground
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = chartInfo.first,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ChartDetailColors.AccentGold
                    )
                    if (chartInfo.second.isNotEmpty()) {
                        Text(
                            text = chartInfo.second,
                            fontSize = 12.sp,
                            color = ChartDetailColors.TextMuted
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Fullscreen,
                        contentDescription = stringResource(id = R.string.view_fullscreen),
                        tint = ChartDetailColors.TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ChartDetailColors.AccentGold.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = chartInfo.third,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChartDetailColors.AccentGold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    if (selectedChartType == "D1") {
                        chartRenderer.drawNorthIndianChart(
                            drawScope = this,
                            chart = chart,
                            size = size.minDimension,
                            chartTitle = "Lagna"
                        )
                    } else {
                        currentChartData?.let {
                            chartRenderer.drawDivisionalChart(
                                drawScope = this,
                                planetPositions = it.planetPositions,
                                ascendantLongitude = it.ascendantLongitude,
                                size = size.minDimension,
                                chartTitle = chartInfo.third,
                                originalChart = chart
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            ChartLegend()

            Text(
                text = stringResource(id = R.string.tap_chart_to_view_fullscreen),
                fontSize = 11.sp,
                color = ChartDetailColors.TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun ChartLegend() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = ChartDetailColors.ChartBackground
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextLegendItem(symbol = "*", label = stringResource(id = R.string.legend_retrograde), color = ChartDetailColors.AccentGold)
                TextLegendItem(symbol = "^", label = stringResource(id = R.string.legend_combust), color = ChartDetailColors.AccentGold)
                TextLegendItem(symbol = "\u00A4", label = stringResource(id = R.string.legend_vargottama), color = ChartDetailColors.AccentGold)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ArrowLegendItem(isExalted = true, label = stringResource(id = R.string.legend_exalted))
                ArrowLegendItem(isExalted = false, label = stringResource(id = R.string.legend_debilitated))
                ShapeLegendItem(isOwnSign = true, label = stringResource(id = R.string.legend_own_sign))
                ShapeLegendItem(isOwnSign = false, label = stringResource(id = R.string.legend_moola_trikona))
            }
        }
    }
}

@Composable
private fun TextLegendItem(
    symbol: String,
    label: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = symbol,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = ChartDetailColors.TextMuted
        )
    }
}

@Composable
private fun BirthDetailsCard(
    chart: VedicChart,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation"
    )

    val birthData = chart.birthData
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault()) }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()) }

    val formattedDate = remember(birthData.dateTime) {
        try { birthData.dateTime.format(dateFormatter) } catch (e: Exception) { "N/A" }
    }
    val formattedTime = remember(birthData.dateTime) {
        try { birthData.dateTime.format(timeFormatter) } catch (e: Exception) { "N/A" }
    }
    val formattedLocation = remember(birthData.location, birthData.latitude, birthData.longitude) {
        birthData.location.takeIf { it.isNotBlank() }
            ?: "${String.format(Locale.US, "%.3f", birthData.latitude)} / ${String.format(Locale.US, "%.3f", birthData.longitude)}"
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ChartDetailColors.CardBackground
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand(!isExpanded) },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null,
                        tint = ChartDetailColors.AccentPurple,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.birth_details_title),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ChartDetailColors.TextPrimary
                    )
                }
                Icon(
                    Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) stringResource(R.string.collapse) else stringResource(R.string.expand),
                    tint = ChartDetailColors.TextMuted,
                    modifier = Modifier.rotate(rotation)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            BirthDataItem(
                                icon = Icons.Outlined.CalendarMonth,
                                label = stringResource(id = R.string.birth_details_date),
                                value = formattedDate
                            )
                            BirthDataItem(
                                icon = Icons.Outlined.LocationOn,
                                label = stringResource(id = R.string.birth_details_location),
                                value = formattedLocation
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            BirthDataItem(
                                icon = Icons.Outlined.Schedule,
                                label = stringResource(id = R.string.birth_details_time),
                                value = formattedTime
                            )
                            BirthDataItem(
                                icon = Icons.Outlined.Star,
                                label = stringResource(id = R.string.birth_details_ayanamsa),
                                value = chart.ayanamsaName
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BirthDataItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = ChartDetailColors.TextMuted,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = ChartDetailColors.TextMuted,
                lineHeight = 12.sp
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = ChartDetailColors.TextPrimary,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun ArrowLegendItem(
    isExalted: Boolean,
    label: String
) {
    val color = if (isExalted) Color(0xFF1E8449) else Color(0xFFC0392B)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Canvas(modifier = Modifier.size(12.dp)) {
            val arrowSize = size.minDimension * 0.9f
            val centerX = size.width / 2
            val centerY = size.height / 2

            val path = Path().apply {
                if (isExalted) {
                    moveTo(centerX, centerY - arrowSize * 0.45f)
                    lineTo(centerX - arrowSize * 0.35f, centerY + arrowSize * 0.1f)
                    lineTo(centerX - arrowSize * 0.1f, centerY + arrowSize * 0.1f)
                    lineTo(centerX - arrowSize * 0.1f, centerY + arrowSize * 0.45f)
                    lineTo(centerX + arrowSize * 0.1f, centerY + arrowSize * 0.45f)
                    lineTo(centerX + arrowSize * 0.1f, centerY + arrowSize * 0.1f)
                    lineTo(centerX + arrowSize * 0.35f, centerY + arrowSize * 0.1f)
                } else {
                    moveTo(centerX, centerY + arrowSize * 0.45f)
                    lineTo(centerX - arrowSize * 0.35f, centerY - arrowSize * 0.1f)
                    lineTo(centerX - arrowSize * 0.1f, centerY - arrowSize * 0.1f)
                    lineTo(centerX - arrowSize * 0.1f, centerY - arrowSize * 0.45f)
                    lineTo(centerX + arrowSize * 0.1f, centerY - arrowSize * 0.45f)
                    lineTo(centerX + arrowSize * 0.1f, centerY - arrowSize * 0.1f)
                    lineTo(centerX + arrowSize * 0.35f, centerY - arrowSize * 0.1f)
                }
                close()
            }
            drawPath(path = path, color = color)
        }
        Text(
            text = label,
            fontSize = 10.sp,
            color = ChartDetailColors.TextMuted
        )
    }
}

@Composable
private fun ShapeLegendItem(
    isOwnSign: Boolean,
    label: String
) {
    val color = if (isOwnSign) Color(0xFF2874A6) else Color(0xFF6C3483)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Canvas(modifier = Modifier.size(12.dp)) {
            val shapeSize = size.minDimension * 0.85f
            val centerX = size.width / 2
            val centerY = size.height / 2

            val path = Path().apply {
                if (isOwnSign) {
                    moveTo(centerX - shapeSize * 0.4f, centerY + shapeSize * 0.35f)
                    lineTo(centerX - shapeSize * 0.4f, centerY - shapeSize * 0.15f)
                    lineTo(centerX - shapeSize * 0.2f, centerY - shapeSize * 0.35f)
                    lineTo(centerX, centerY - shapeSize * 0.45f)
                    lineTo(centerX + shapeSize * 0.2f, centerY - shapeSize * 0.35f)
                    lineTo(centerX + shapeSize * 0.4f, centerY - shapeSize * 0.15f)
                    lineTo(centerX + shapeSize * 0.4f, centerY + shapeSize * 0.35f)
                } else {
                    moveTo(centerX, centerY - shapeSize * 0.4f)
                    lineTo(centerX + shapeSize * 0.4f, centerY + shapeSize * 0.35f)
                    lineTo(centerX - shapeSize * 0.4f, centerY + shapeSize * 0.35f)
                }
                close()
            }
            drawPath(path = path, color = color)
        }
        Text(
            text = label,
            fontSize = 10.sp,
            color = ChartDetailColors.TextMuted
        )
    }
}

@Composable
private fun ChartDetailsCard(
    chart: VedicChart,
    currentChartData: DivisionalChartData?,
    selectedChartType: String,
    onPlanetClick: (PlanetPosition) -> Unit
) {
    val planetPositions = if (selectedChartType == "D1") {
        chart.planetPositions
    } else {
        currentChartData?.planetPositions ?: emptyList()
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ChartDetailColors.CardBackground
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    Icons.Outlined.Star,
                    contentDescription = null,
                    tint = ChartDetailColors.AccentTeal,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.planetary_positions_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ChartDetailColors.TextPrimary
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.tap_for_details),
                    fontSize = 11.sp,
                    color = ChartDetailColors.TextMuted
                )
            }

            if (selectedChartType == "D1") {
                AscendantRow(chart = chart)
                HorizontalDivider(
                    color = ChartDetailColors.DividerColor,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            planetPositions.forEach { position ->
                ClickablePlanetPositionRow(
                    position = position,
                    onClick = { onPlanetClick(position) }
                )
            }
        }
    }
}

@Composable
private fun AscendantRow(chart: VedicChart) {
    val ascSign = ZodiacSign.fromLongitude(chart.ascendant)
    val ascDegree = chart.ascendant % 30.0
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = ChartDetailColors.AccentGold.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.ascendant_lagna),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = ChartDetailColors.AccentGold
            )
            Row {
                Text(
                    text = PlanetFormatter.formatSignName(ascSign, context),
                    fontSize = 13.sp,
                    color = ChartDetailColors.AccentTeal
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${ascDegree.toInt()}°",
                    fontSize = 13.sp,
                    color = ChartDetailColors.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun ClickablePlanetPositionRow(
    position: PlanetPosition,
    onClick: () -> Unit
) {
    val color = ChartDetailColors.getPlanetColor(position.planet)
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(6.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = position.planet.stringRes),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = color,
                    modifier = Modifier.width(70.dp)
                )
                if (position.isRetrograde) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = ChartDetailColors.WarningColor.copy(alpha = 0.2f),
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Text(
                            text = "R",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChartDetailColors.WarningColor,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            Text(
                text = PlanetFormatter.formatSignName(position.sign, context),
                fontSize = 13.sp,
                color = ChartDetailColors.AccentTeal,
                modifier = Modifier.width(80.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "${(position.longitude % 30.0).toInt()}°",
                fontSize = 13.sp,
                color = ChartDetailColors.TextSecondary,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "H${position.house}",
                fontSize = 12.sp,
                color = ChartDetailColors.TextMuted,
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.End
            )

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "View details",
                tint = ChartDetailColors.TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun HouseCuspsCard(
    chart: VedicChart,
    onHouseClick: (Int) -> Unit,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ChartDetailColors.CardBackground
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand(!isExpanded) },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Home,
                        contentDescription = null,
                        tint = ChartDetailColors.AccentPurple,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.house_cusps_title),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ChartDetailColors.TextPrimary
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isExpanded) stringResource(id = R.string.tap_house_for_details) else stringResource(id = R.string.tap_to_expand),
                        fontSize = 11.sp,
                        color = ChartDetailColors.TextMuted
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) stringResource(id = R.string.collapse) else stringResource(id = R.string.expand),
                        tint = ChartDetailColors.TextMuted,
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    for (row in 0..5) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val house1 = row + 1
                            val house2 = row + 7

                            HouseCuspItem(
                                houseNumber = house1,
                                cusp = chart.houseCusps.getOrNull(house1 - 1) ?: 0.0,
                                modifier = Modifier.weight(1f),
                                onClick = { onHouseClick(house1) }
                            )
                            HouseCuspItem(
                                houseNumber = house2,
                                cusp = chart.houseCusps.getOrNull(house2 - 1) ?: 0.0,
                                modifier = Modifier.weight(1f),
                                onClick = { onHouseClick(house2) }
                            )
                        }
                        if (row < 5) Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun HouseCuspItem(
    houseNumber: Int,
    cusp: Double,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val sign = ZodiacSign.fromLongitude(cusp)
    val degreeInSign = cusp % 30.0

    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = ChartDetailColors.CardBackgroundElevated
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "H$houseNumber",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ChartDetailColors.AccentGold
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(id = sign.abbreviationRes),
                    fontSize = 12.sp,
                    color = ChartDetailColors.AccentTeal
                )
                Text(
                    text = "${degreeInSign.toInt()}°",
                    fontSize = 11.sp,
                    color = ChartDetailColors.TextMuted
                )
            }
        }
    }
}

@Composable
private fun AstronomicalDataCard(
    chart: VedicChart,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation"
    )
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ChartDetailColors.CardBackground
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand(!isExpanded) },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null,
                        tint = ChartDetailColors.AccentPurple,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.astronomical_data_title),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ChartDetailColors.TextPrimary
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isExpanded) "" else stringResource(id = R.string.tap_to_expand),
                        fontSize = 11.sp,
                        color = ChartDetailColors.TextMuted
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) stringResource(id = R.string.collapse) else stringResource(id = R.string.expand),
                        tint = ChartDetailColors.TextMuted,
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    InfoRow(stringResource(id = R.string.info_julian_day), String.format("%.6f", chart.julianDay))
                    InfoRow(stringResource(id = R.string.info_ayanamsa), "${chart.ayanamsaName} (${ChartFormatter.formatDegree(chart.ayanamsa)})")
                    InfoRow(stringResource(id = R.string.info_ascendant), ChartFormatter.formatDegree(chart.ascendant))
                    InfoRow(stringResource(id = R.string.info_midheaven), ChartFormatter.formatDegree(chart.midheaven))
                    InfoRow(stringResource(id = R.string.info_house_system), stringResource(id = chart.houseSystem.stringRes))
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = ChartDetailColors.TextMuted
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = ChartDetailColors.TextPrimary
        )
    }
}
