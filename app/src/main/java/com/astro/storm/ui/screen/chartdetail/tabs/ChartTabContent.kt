package com.astro.storm.ui.screen.chartdetail.tabs

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.StringKey
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ui.chart.ChartRenderer
import com.astro.storm.ui.theme.*
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ChartTabContent(
    chart: VedicChart,
    chartRenderer: ChartRenderer,
    onChartClick: (String, com.astro.storm.ephemeris.DivisionalChartData?) -> Unit,
    onPlanetClick: (PlanetPosition) -> Unit,
    onHouseClick: (Int) -> Unit
) {
    val language = LocalLanguage.current

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Vellum),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Hero Chart Section
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "Rasi Chart",
                        style = MaterialTheme.typography.titleLarge,
                        color = CosmicIndigo,
                        fontFamily = CinzelDecorativeFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = VedicGold.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, VedicGold),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            "SOUTH INDIAN",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = VedicGold,
                            fontFamily = SpaceGroteskFontFamily
                        )
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    color = Vellum,
                    border = BorderStroke(1.5.dp, CosmicIndigo),
                    shadowElevation = 2.dp
                ) {
                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                        chartRenderer.drawSouthIndianChart(this, chart, size.minDimension, language)
                    }
                }

                Text(
                    "Tap any house to reveal detailed bhava analysis.",
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateMuted,
                    fontFamily = CormorantGaramondFontFamily,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }

        // Planetary Positions Table
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(
                        "Planetary Positions",
                        style = MaterialTheme.typography.titleLarge,
                        color = CosmicIndigo,
                        fontFamily = CinzelDecorativeFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f).height(1.dp).background(BorderSubtle))
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Paper,
                    border = BorderStroke(1.dp, BorderSubtle),
                    shape = RoundedCornerShape(2.dp)
                ) {
                    Column {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth().background(CosmicIndigo.copy(alpha = 0.05f)).padding(12.dp)
                        ) {
                            Text("PLANET", modifier = Modifier.weight(4f), style = MaterialTheme.typography.labelSmall, color = SlateMuted, fontFamily = SpaceGroteskFontFamily, letterSpacing = 1.sp)
                            Text("DEGREE", modifier = Modifier.weight(3f), textAlign = TextAlign.End, style = MaterialTheme.typography.labelSmall, color = SlateMuted, fontFamily = SpaceGroteskFontFamily, letterSpacing = 1.sp)
                            Text("NAKSHATRA", modifier = Modifier.weight(5f), textAlign = TextAlign.End, style = MaterialTheme.typography.labelSmall, color = SlateMuted, fontFamily = SpaceGroteskFontFamily, letterSpacing = 1.sp)
                        }

                        // Ascendant Row
                        val ascSign = ZodiacSign.fromLongitude(chart.ascendant)
                        val ascDegree = chart.ascendant % 30.0
                        PlanetRow(
                            name = "Ascendant",
                            degree = formatDegree(ascDegree),
                            nakshatra = chart.nakshatra.getLocalizedName(language),
                            pada = "Pada ${chart.pada}",
                            accentColor = VedicGold,
                            isAscendant = true
                        )

                        // Planet Rows
                        chart.planetPositions.forEach { position ->
                            PlanetRow(
                                name = position.planet.getLocalizedName(language),
                                degree = formatDegree(position.longitude % 30.0),
                                nakshatra = position.nakshatra.getLocalizedName(language),
                                pada = "Pada ${position.pada}",
                                isRetrograde = position.isRetrograde,
                                icon = getPlanetIcon(position.planet)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlanetRow(
    name: String,
    degree: String,
    nakshatra: String,
    pada: String,
    accentColor: Color? = null,
    isAscendant: Boolean = false,
    isRetrograde: Boolean = false,
    icon: ImageVector? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 0.5.dp, color = BorderSubtle.copy(alpha = 0.5f))
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Name & Icon
        Row(modifier = Modifier.weight(4f), verticalAlignment = Alignment.CenterVertically) {
            if (isAscendant) {
                Box(modifier = Modifier.width(3.dp).height(24.dp).background(VedicGold, RoundedCornerShape(2.dp)))
                Spacer(modifier = Modifier.width(8.dp))
            } else if (icon != null) {
                Icon(icon, null, tint = accentColor ?: CosmicIndigo, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                name,
                style = MaterialTheme.typography.titleMedium,
                color = CosmicIndigo,
                fontFamily = CormorantGaramondFontFamily,
                fontWeight = FontWeight.Bold
            )
            if (isRetrograde) {
                Spacer(modifier = Modifier.width(4.dp))
                Surface(color = MarsRed.copy(alpha = 0.1f), shape = RoundedCornerShape(2.dp), border = BorderStroke(0.5.dp, MarsRed.copy(alpha = 0.2f))) {
                    Text("R", modifier = Modifier.padding(horizontal = 4.dp), style = MaterialTheme.typography.labelSmall, color = MarsRed, fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Degree
        Text(
            degree,
            modifier = Modifier.weight(3f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium,
            color = CosmicIndigo,
            fontFamily = SpaceGroteskFontFamily
        )

        // Nakshatra
        Column(modifier = Modifier.weight(5f), horizontalAlignment = Alignment.End) {
            Text(
                nakshatra,
                style = MaterialTheme.typography.bodyMedium,
                color = CosmicIndigo,
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Medium
            )
            Text(
                pada,
                style = MaterialTheme.typography.labelSmall,
                color = SlateMuted,
                fontFamily = SpaceGroteskFontFamily,
                fontSize = 8.sp
            )
        }
    }
}

private fun formatDegree(degree: Double): String {
    val d = degree.toInt()
    val m = ((degree - d) * 60).toInt()
    return String.format(Locale.US, "%02d° %02d'", d, m)
}

private fun getPlanetIcon(planet: com.astro.storm.core.model.Planet): ImageVector {
    return when (planet) {
        com.astro.storm.core.model.Planet.SUN -> Icons.Outlined.Sunny
        com.astro.storm.core.model.Planet.MOON -> Icons.Outlined.Bedtime
        com.astro.storm.core.model.Planet.MARS -> Icons.Outlined.RadioButtonChecked
        com.astro.storm.core.model.Planet.MERCURY -> Icons.Outlined.Circle
        com.astro.storm.core.model.Planet.JUPITER -> Icons.Outlined.Flare
        com.astro.storm.core.model.Planet.SATURN -> Icons.Outlined.BlurOn
        else -> Icons.Outlined.Star
    }
}
