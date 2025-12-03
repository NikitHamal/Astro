package com.astro.storm.ui.screen.main

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ephemeris.HoroscopeCalculator
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Insights Tab - Planetary Periods, Transits & Chart Analysis Options
 *
 * Displays:
 * - Current planetary period (Dasha) with progress
 * - Upcoming transits and their effects
 * - Quick access to detailed chart analysis features
 * - Feature grid for various astrological tools
 */
@Composable
fun InsightsTab(
    chart: VedicChart?,
    onNavigateToChartAnalysis: () -> Unit,
    onFeatureClick: (InsightFeature) -> Unit
) {
    val context = LocalContext.current
    var dashaTimeline by remember { mutableStateOf<DashaCalculator.DashaTimeline?>(null) }
    var planetaryInfluences by remember { mutableStateOf<List<HoroscopeCalculator.PlanetaryInfluence>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // Calculate insights when chart changes
    LaunchedEffect(chart) {
        if (chart != null) {
            isLoading = true
            withContext(Dispatchers.Default) {
                dashaTimeline = DashaCalculator.calculateDashaTimeline(chart)
                val calculator = HoroscopeCalculator(context)
                try {
                    val horoscope = calculator.calculateDailyHoroscope(chart)
                    planetaryInfluences = horoscope.planetaryInfluences
                } finally {
                    calculator.close()
                }
            }
            isLoading = false
        }
    }

    if (chart == null) {
        EmptyInsightsState()
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Loading state
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = AppTheme.AccentPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            return@LazyColumn
        }

        // Current Dasha Period
        item {
            dashaTimeline?.let { timeline ->
                CurrentDashaCard(timeline)
            }
        }

        // Dasha Timeline Preview
        item {
            dashaTimeline?.let { timeline ->
                DashaTimelinePreview(timeline)
            }
        }

        // Planetary Transits
        item {
            if (planetaryInfluences.isNotEmpty()) {
                PlanetaryTransitsSection(planetaryInfluences)
            }
        }

        // Quick Actions Header
        item {
            Text(
                text = "Chart Analysis",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        // Feature Grid - Implemented features
        item {
            FeatureGrid(
                features = InsightFeature.implementedFeatures,
                onFeatureClick = onFeatureClick
            )
        }

        // Coming Soon Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Coming Soon",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextMuted,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Coming Soon Features
        item {
            FeatureGrid(
                features = InsightFeature.comingSoonFeatures,
                onFeatureClick = { /* Non-functional */ },
                isDisabled = true
            )
        }
    }
}

@Composable
private fun CurrentDashaCard(timeline: DashaCalculator.DashaTimeline) {
    val currentMahadasha = timeline.currentMahadasha
    val currentAntardasha = timeline.currentAntardasha

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current Planetary Period",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )

                // Status indicator
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(AppTheme.SuccessColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Active",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.SuccessColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            currentMahadasha?.let { mahadasha ->
                // Mahadasha
                DashaPeriodRow(
                    label = "Mahadasha",
                    planet = mahadasha.planet,
                    startDate = mahadasha.startDate,
                    endDate = mahadasha.endDate,
                    isPrimary = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Progress bar for Mahadasha
                val mahadashaProgress = calculateProgress(mahadasha.startDate, mahadasha.endDate)
                DashaProgressBar(
                    progress = mahadashaProgress,
                    color = getPlanetColor(mahadasha.planet)
                )

                currentAntardasha?.let { antardasha ->
                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(color = AppTheme.DividerColor)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Antardasha (Bhukti)
                    DashaPeriodRow(
                        label = "Antardasha",
                        planet = antardasha.planet,
                        startDate = antardasha.startDate,
                        endDate = antardasha.endDate,
                        isPrimary = false
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val antardashaProgress = calculateProgress(antardasha.startDate, antardasha.endDate)
                    DashaProgressBar(
                        progress = antardashaProgress,
                        color = getPlanetColor(antardasha.planet),
                        height = 6
                    )

                    // Pratyantardasha if available
                    timeline.currentPratyantardasha?.let { pratyantardasha ->
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Pratyantardasha:",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextMuted
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = pratyantardasha.planet.displayName,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = getPlanetColor(pratyantardasha.planet)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashaPeriodRow(
    label: String,
    planet: Planet,
    startDate: LocalDate,
    endDate: LocalDate,
    isPrimary: Boolean
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM yyyy")

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Planet indicator
        Box(
            modifier = Modifier
                .size(if (isPrimary) 44.dp else 36.dp)
                .clip(CircleShape)
                .background(getPlanetColor(planet).copy(alpha = 0.15f))
                .border(
                    width = 2.dp,
                    color = getPlanetColor(planet),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = planet.symbol,
                style = if (isPrimary) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = getPlanetColor(planet)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$label: ${planet.displayName}",
                style = if (isPrimary) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            Text(
                text = "${startDate.format(dateFormatter)} - ${endDate.format(dateFormatter)}",
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
        }

        // Days remaining
        val daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), endDate)
        if (daysRemaining > 0) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatDuration(daysRemaining),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.AccentPrimary
                )
                Text(
                    text = "remaining",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun DashaProgressBar(
    progress: Float,
    color: Color,
    height: Int = 8
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(height / 2))
            .background(AppTheme.ChipBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .clip(RoundedCornerShape(height / 2))
                .background(color)
        )
    }
}

@Composable
private fun DashaTimelinePreview(timeline: DashaCalculator.DashaTimeline) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Upcoming Periods",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Show next 3 Antardashas
            val currentMahadasha = timeline.currentMahadasha
            val currentAntardasha = timeline.currentAntardasha

            if (currentMahadasha != null && currentAntardasha != null) {
                val currentIndex = currentMahadasha.antardashas.indexOf(currentAntardasha)
                val upcomingAntardashas = currentMahadasha.antardashas
                    .drop(currentIndex + 1)
                    .take(3)

                if (upcomingAntardashas.isEmpty()) {
                    Text(
                        text = "Current Antardasha is the last in this Mahadasha",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted
                    )
                } else {
                    upcomingAntardashas.forEachIndexed { index, antardasha ->
                        UpcomingPeriodItem(
                            planet = antardasha.planet,
                            mahadashaPlanet = currentMahadasha.planet,
                            startDate = antardasha.startDate,
                            isFirst = index == 0
                        )
                        if (index < upcomingAntardashas.lastIndex) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UpcomingPeriodItem(
    planet: Planet,
    mahadashaPlanet: Planet,
    startDate: LocalDate,
    isFirst: Boolean
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    val daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), startDate)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isFirst) AppTheme.ChipBackground else Color.Transparent)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(getPlanetColor(planet).copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = planet.symbol,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = getPlanetColor(planet)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${mahadashaPlanet.displayName}-${planet.displayName}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            Text(
                text = "Starts ${startDate.format(dateFormatter)}",
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
        }

        if (daysUntil > 0) {
            Text(
                text = "in ${formatDuration(daysUntil)}",
                style = MaterialTheme.typography.labelSmall,
                color = if (isFirst) AppTheme.AccentPrimary else AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun PlanetaryTransitsSection(influences: List<HoroscopeCalculator.PlanetaryInfluence>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Current Transits",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Horizontal scrolling transit cards
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(influences.take(6)) { influence ->
                TransitCard(influence)
            }
        }
    }
}

@Composable
private fun TransitCard(influence: HoroscopeCalculator.PlanetaryInfluence) {
    Card(
        modifier = Modifier
            .width(160.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(getPlanetColor(influence.planet).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = influence.planet.symbol,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = getPlanetColor(influence.planet)
                    )
                }

                // Positive/Negative indicator
                Icon(
                    imageVector = if (influence.isPositive) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                    contentDescription = null,
                    tint = if (influence.isPositive) AppTheme.SuccessColor else AppTheme.WarningColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = influence.planet.displayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = influence.influence,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Strength indicator
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) { index ->
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (index < (influence.strength / 2)) {
                                    if (influence.isPositive) AppTheme.SuccessColor else AppTheme.WarningColor
                                } else {
                                    AppTheme.ChipBackground
                                }
                            )
                    )
                    if (index < 4) Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}

@Composable
private fun FeatureGrid(
    features: List<InsightFeature>,
    onFeatureClick: (InsightFeature) -> Unit,
    isDisabled: Boolean = false
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        features.chunked(2).forEach { rowFeatures ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowFeatures.forEach { feature ->
                    FeatureCard(
                        feature = feature,
                        onClick = { onFeatureClick(feature) },
                        isDisabled = isDisabled,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill empty space if odd number of features
                if (rowFeatures.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun FeatureCard(
    feature: InsightFeature,
    onClick: () -> Unit,
    isDisabled: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(enabled = !isDisabled) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isDisabled) AppTheme.CardBackground.copy(alpha = 0.5f) else AppTheme.CardBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isDisabled) AppTheme.TextSubtle.copy(alpha = 0.1f)
                            else feature.color.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = feature.icon,
                        contentDescription = null,
                        tint = if (isDisabled) AppTheme.TextSubtle else feature.color,
                        modifier = Modifier.size(22.dp)
                    )
                }

                if (isDisabled) {
                    Text(
                        text = "Soon",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextSubtle
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = feature.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = if (isDisabled) AppTheme.TextSubtle else AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = feature.description,
                style = MaterialTheme.typography.bodySmall,
                color = if (isDisabled) AppTheme.TextSubtle.copy(alpha = 0.7f) else AppTheme.TextMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )
        }
    }
}

/**
 * Available insight features
 */
enum class InsightFeature(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val isImplemented: Boolean
) {
    // Implemented features
    FULL_CHART(
        title = "Birth Chart",
        description = "View your complete Vedic birth chart",
        icon = Icons.Outlined.GridView,
        color = AppTheme.AccentPrimary,
        isImplemented = true
    ),
    PLANETS(
        title = "Planets",
        description = "Detailed planetary positions",
        icon = Icons.Outlined.Public,
        color = AppTheme.LifeAreaCareer,
        isImplemented = true
    ),
    YOGAS(
        title = "Yogas",
        description = "Planetary combinations & effects",
        icon = Icons.Outlined.AutoAwesome,
        color = AppTheme.AccentGold,
        isImplemented = true
    ),
    DASHAS(
        title = "Dashas",
        description = "Planetary period timeline",
        icon = Icons.Outlined.Timeline,
        color = AppTheme.LifeAreaSpiritual,
        isImplemented = true
    ),
    TRANSITS(
        title = "Transits",
        description = "Current planetary movements",
        icon = Icons.Outlined.Sync,
        color = AppTheme.AccentTeal,
        isImplemented = true
    ),
    ASHTAKAVARGA(
        title = "Ashtakavarga",
        description = "Strength analysis by house",
        icon = Icons.Outlined.BarChart,
        color = AppTheme.SuccessColor,
        isImplemented = true
    ),
    PANCHANGA(
        title = "Panchanga",
        description = "Vedic calendar elements",
        icon = Icons.Outlined.CalendarMonth,
        color = AppTheme.LifeAreaFinance,
        isImplemented = true
    ),

    // Newly implemented features
    MATCHMAKING(
        title = "Matchmaking",
        description = "Kundli Milan compatibility",
        icon = Icons.Outlined.Favorite,
        color = AppTheme.LifeAreaLove,
        isImplemented = true
    ),
    MUHURTA(
        title = "Muhurta",
        description = "Auspicious timing finder",
        icon = Icons.Outlined.AccessTime,
        color = AppTheme.WarningColor,
        isImplemented = true
    ),
    REMEDIES(
        title = "Remedies",
        description = "Personalized remedies",
        icon = Icons.Outlined.Spa,
        color = AppTheme.LifeAreaHealth,
        isImplemented = true
    ),
    VARSHAPHALA(
        title = "Varshaphala",
        description = "Solar return horoscope",
        icon = Icons.Outlined.Cake,
        color = AppTheme.LifeAreaCareer,
        isImplemented = true
    ),

    // Coming soon features
    PRASHNA(
        title = "Prashna",
        description = "Horary astrology",
        icon = Icons.Outlined.HelpOutline,
        color = AppTheme.AccentTeal,
        isImplemented = false
    ),
    CHART_COMPARISON(
        title = "Synastry",
        description = "Chart comparison",
        icon = Icons.Outlined.CompareArrows,
        color = AppTheme.LifeAreaFinance,
        isImplemented = false
    ),
    NAKSHATRA_ANALYSIS(
        title = "Nakshatras",
        description = "Deep nakshatra analysis",
        icon = Icons.Outlined.Stars,
        color = AppTheme.AccentGold,
        isImplemented = false
    ),
    SHADBALA(
        title = "Shadbala",
        description = "Six-fold strength",
        icon = Icons.Outlined.Speed,
        color = AppTheme.SuccessColor,
        isImplemented = false
    );

    companion object {
        val implementedFeatures = entries.filter { it.isImplemented }
        val comingSoonFeatures = entries.filter { !it.isImplemented }
    }
}

@Composable
private fun EmptyInsightsState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Insights,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "No Profile Selected",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Select or create a profile to view your astrological insights",
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Helper functions
private fun calculateProgress(startDate: LocalDate, endDate: LocalDate): Float {
    val today = LocalDate.now()
    val totalDays = ChronoUnit.DAYS.between(startDate, endDate).toFloat()
    val elapsedDays = ChronoUnit.DAYS.between(startDate, today).toFloat()
    return (elapsedDays / totalDays).coerceIn(0f, 1f)
}

private fun formatDuration(days: Long): String {
    return when {
        days < 30 -> "$days days"
        days < 365 -> "${days / 30}m ${days % 30}d"
        else -> {
            val years = days / 365
            val months = (days % 365) / 30
            if (months > 0) "${years}y ${months}m" else "${years}y"
        }
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
