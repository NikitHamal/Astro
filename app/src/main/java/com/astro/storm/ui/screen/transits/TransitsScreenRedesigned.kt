package com.astro.storm.ui.screen.transits

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.TransitAnalyzer
import com.astro.storm.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.getLocalizedName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransitsScreenRedesigned(
    chart: VedicChart?,
    transitAnalyzer: TransitAnalyzer,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    val now = LocalDateTime.now()

    val transitAnalysis = remember(chart) {
        chart?.let {
            try { transitAnalyzer.analyzeTransits(it, now) } catch (e: Exception) { null }
        }
    }

    Scaffold(
        containerColor = Vellum,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Ephemeris",
                            style = MaterialTheme.typography.titleLarge,
                            color = CosmicIndigo,
                            fontFamily = CinzelDecorativeFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Live Celestial Movements".uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = VedicGold,
                            fontFamily = SpaceGroteskFontFamily,
                            letterSpacing = 1.5.sp,
                            fontSize = 8.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, "Back", tint = CosmicIndigo)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Calendar */ }) {
                        Icon(Icons.Outlined.DateRange, "Calendar", tint = CosmicIndigo)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Vellum.copy(alpha = 0.95f)
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            com.astro.storm.ui.screen.main.GrainTextureOverlay()

            if (transitAnalysis == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Calculating Celestial Movements...", color = SlateMuted)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Date Group: Today
                    item {
                        DateHeader(now.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US)).uppercase(), "Today")
                    }

                    items(transitAnalysis.transitPositions) { position ->
                        TransitItem(
                            time = now.format(DateTimeFormatter.ofPattern("hh:mm a")),
                            icon1 = position.planet.symbol,
                            title = "${position.planet.getLocalizedName(language)} in ${position.houseTransit}H",
                            subtitle = "${position.sign.getLocalizedName(language)} ${String.format(Locale.US, "%.1f°", position.longitude % 30.0)}",
                            dotColor = if (position.isRetrograde) MarsRed else VedicGold,
                            isWarning = position.isRetrograde,
                            isRetrograde = position.isRetrograde
                        )
                    }

                    // Upcoming Group
                    if (transitAnalysis.significantPeriods.isNotEmpty()) {
                        item {
                            DateHeader("Upcoming Events", "Future")
                        }

                        items(transitAnalysis.significantPeriods.take(10)) { period ->
                            TransitItem(
                                time = period.startDate.format(DateTimeFormatter.ofPattern("MMM dd")),
                                icon1 = period.planets.firstOrNull()?.symbol ?: "✦",
                                title = period.description,
                                subtitle = "Intensity: ${period.intensity}/5",
                                dotColor = VedicGold
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }
            }
        }
    }
}

@Composable
private fun DateHeader(date: String, label: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Paper.copy(alpha = 0.95f),
        border = BorderStroke(width = 0.5.dp, color = BorderSubtle.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                date,
                style = MaterialTheme.typography.labelMedium,
                color = CosmicIndigo,
                fontFamily = CinzelDecorativeFontFamily,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = VedicGold,
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TransitItem(
    time: String,
    icon1: String,
    icon2: String? = null,
    isRetrograde: Boolean = false,
    title: String,
    subtitle: String,
    dotColor: Color,
    isWarning: Boolean = false
) {
    val bgColor = if (isWarning) MarsRed.copy(alpha = 0.05f) else Color.Transparent

    Box(modifier = Modifier.fillMaxWidth().background(bgColor)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(start = 18.5.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Timeline Dot
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(Vellum, CircleShape)
                    .border(2.dp, if (dotColor == Color.White) BorderSubtle else dotColor, CircleShape)
                    .padding(2.dp)
            ) {
                if (dotColor != Color.White) {
                    Box(modifier = Modifier.fillMaxSize().background(dotColor, CircleShape))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Time
            Text(
                time,
                modifier = Modifier.width(70.dp),
                style = MaterialTheme.typography.labelMedium,
                color = if (isWarning) MarsRed else SlateMuted,
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = if (isWarning) FontWeight.Bold else FontWeight.Medium
            )

            // Icons
            Row(
                modifier = Modifier.width(80.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    icon1,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (isWarning) MarsRed else CosmicIndigo,
                    fontFamily = CinzelDecorativeFontFamily
                )
                if (isRetrograde) {
                    Surface(
                        modifier = Modifier.padding(start = 4.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, MarsRed),
                        shape = RoundedCornerShape(2.dp)
                    ) {
                        Text("R", modifier = Modifier.padding(horizontal = 2.dp), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = MarsRed, fontFamily = SpaceGroteskFontFamily)
                    }
                }
            }

            // Description
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isWarning) MarsRed else CosmicIndigo,
                    fontFamily = CormorantGaramondFontFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    subtitle.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isWarning) MarsRed.copy(alpha = 0.7f) else SlateMuted,
                    fontFamily = SpaceGroteskFontFamily,
                    letterSpacing = 1.sp,
                    fontSize = 8.sp,
                    textAlign = TextAlign.End
                )
            }
        }

        HorizontalDivider(modifier = Modifier.align(Alignment.BottomCenter).padding(start = 64.dp), color = BorderSubtle.copy(alpha = 0.3f))
    }
}
