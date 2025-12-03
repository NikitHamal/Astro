package com.astro.storm.ui.screen.main

import android.app.Application
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ephemeris.HoroscopeCalculator
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Home Tab - Daily Horoscope & Predictions
 *
 * Displays personalized horoscope predictions based on the user's Vedic chart:
 * - Today's horoscope with detailed life area predictions
 * - Tomorrow's preview
 * - Weekly overview
 *
 * All predictions are calculated using Vedic astrological principles.
 */
@Composable
fun HomeTab(
    chart: VedicChart?,
    onNavigateToInsights: () -> Unit
) {
    val context = LocalContext.current
    var selectedPeriod by remember { mutableStateOf(HoroscopePeriod.TODAY) }
    var dailyHoroscope by remember { mutableStateOf<HoroscopeCalculator.DailyHoroscope?>(null) }
    var tomorrowHoroscope by remember { mutableStateOf<HoroscopeCalculator.DailyHoroscope?>(null) }
    var weeklyHoroscope by remember { mutableStateOf<HoroscopeCalculator.WeeklyHoroscope?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Calculate horoscopes when chart changes
    LaunchedEffect(chart) {
        if (chart != null) {
            isLoading = true
            withContext(Dispatchers.Default) {
                val calculator = HoroscopeCalculator(context)
                try {
                    dailyHoroscope = calculator.calculateDailyHoroscope(chart, LocalDate.now())
                    tomorrowHoroscope = calculator.calculateDailyHoroscope(chart, LocalDate.now().plusDays(1))
                    weeklyHoroscope = calculator.calculateWeeklyHoroscope(chart, LocalDate.now())
                } finally {
                    calculator.close()
                }
            }
            isLoading = false
        }
    }

    if (chart == null) {
        EmptyHomeState()
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Period Selector
        item {
            PeriodSelector(
                selectedPeriod = selectedPeriod,
                onPeriodSelected = { selectedPeriod = it }
            )
        }

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

        // Content based on selected period
        when (selectedPeriod) {
            HoroscopePeriod.TODAY -> {
                dailyHoroscope?.let { horoscope ->
                    item {
                        DailyHoroscopeHeader(horoscope)
                    }
                    item {
                        EnergyCard(horoscope)
                    }
                    item {
                        LifeAreasSection(horoscope.lifeAreas)
                    }
                    item {
                        LuckyElementsCard(horoscope.luckyElements)
                    }
                    item {
                        RecommendationsCard(
                            recommendations = horoscope.recommendations,
                            cautions = horoscope.cautions
                        )
                    }
                    item {
                        AffirmationCard(horoscope.affirmation)
                    }
                }
            }
            HoroscopePeriod.TOMORROW -> {
                tomorrowHoroscope?.let { horoscope ->
                    item {
                        DailyHoroscopeHeader(horoscope, isTomorrow = true)
                    }
                    item {
                        EnergyCard(horoscope)
                    }
                    item {
                        LifeAreasSection(horoscope.lifeAreas)
                    }
                    item {
                        LuckyElementsCard(horoscope.luckyElements)
                    }
                }
            }
            HoroscopePeriod.WEEKLY -> {
                weeklyHoroscope?.let { weekly ->
                    item {
                        WeeklyOverviewHeader(weekly)
                    }
                    item {
                        WeeklyEnergyChart(weekly.dailyHighlights)
                    }
                    item {
                        KeyDatesSection(weekly.keyDates)
                    }
                    item {
                        WeeklyPredictionsSection(weekly.weeklyPredictions)
                    }
                    item {
                        WeeklyAdviceCard(weekly.weeklyAdvice)
                    }
                }
            }
        }
    }
}

enum class HoroscopePeriod(val displayName: String) {
    TODAY("Today"),
    TOMORROW("Tomorrow"),
    WEEKLY("Weekly")
}

@Composable
private fun PeriodSelector(
    selectedPeriod: HoroscopePeriod,
    onPeriodSelected: (HoroscopePeriod) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AppTheme.CardBackground)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        HoroscopePeriod.entries.forEach { period ->
            val isSelected = period == selectedPeriod
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) AppTheme.AccentPrimary else Color.Transparent)
                    .clickable { onPeriodSelected(period) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = period.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) AppTheme.ButtonText else AppTheme.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun DailyHoroscopeHeader(
    horoscope: HoroscopeCalculator.DailyHoroscope,
    isTomorrow: Boolean = false
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")

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
            // Date
            Text(
                text = horoscope.date.format(dateFormatter),
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.TextMuted
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Theme
            Text(
                text = horoscope.theme,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Theme description
            Text(
                text = horoscope.themeDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Moon & Dasha info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip(
                    icon = Icons.Outlined.NightlightRound,
                    label = "Moon in ${horoscope.moonSign.displayName}"
                )
                InfoChip(
                    icon = Icons.Outlined.Schedule,
                    label = horoscope.activeDasha
                )
            }
        }
    }
}

@Composable
private fun InfoChip(
    icon: ImageVector,
    label: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AppTheme.ChipBackground)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.AccentPrimary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextSecondary
        )
    }
}

@Composable
private fun EnergyCard(horoscope: HoroscopeCalculator.DailyHoroscope) {
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
                text = "Overall Energy",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Energy bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Energy dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(10) { index ->
                        val isActive = index < horoscope.overallEnergy
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isActive) {
                                        when {
                                            index < 3 -> AppTheme.ErrorColor
                                            index < 6 -> AppTheme.WarningColor
                                            else -> AppTheme.SuccessColor
                                        }
                                    } else {
                                        AppTheme.ChipBackground
                                    }
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "${horoscope.overallEnergy}/10",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        horoscope.overallEnergy < 4 -> AppTheme.ErrorColor
                        horoscope.overallEnergy < 7 -> AppTheme.WarningColor
                        else -> AppTheme.SuccessColor
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when {
                    horoscope.overallEnergy >= 8 -> "Excellent day ahead!"
                    horoscope.overallEnergy >= 6 -> "Favorable energy today"
                    horoscope.overallEnergy >= 4 -> "Balanced energy - stay steady"
                    else -> "Take it easy today"
                },
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun LifeAreasSection(lifeAreas: List<HoroscopeCalculator.LifeAreaPrediction>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Life Areas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        lifeAreas.forEach { prediction ->
            LifeAreaCard(prediction)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LifeAreaCard(prediction: HoroscopeCalculator.LifeAreaPrediction) {
    var expanded by remember { mutableStateOf(false) }

    val areaColor = when (prediction.area) {
        HoroscopeCalculator.LifeArea.CAREER -> AppTheme.LifeAreaCareer
        HoroscopeCalculator.LifeArea.LOVE -> AppTheme.LifeAreaLove
        HoroscopeCalculator.LifeArea.HEALTH -> AppTheme.LifeAreaHealth
        HoroscopeCalculator.LifeArea.FINANCE -> AppTheme.LifeAreaFinance
        HoroscopeCalculator.LifeArea.FAMILY -> AppTheme.AccentTeal
        HoroscopeCalculator.LifeArea.SPIRITUALITY -> AppTheme.LifeAreaSpiritual
    }

    val areaIcon = when (prediction.area) {
        HoroscopeCalculator.LifeArea.CAREER -> Icons.Outlined.Work
        HoroscopeCalculator.LifeArea.LOVE -> Icons.Outlined.Favorite
        HoroscopeCalculator.LifeArea.HEALTH -> Icons.Outlined.FavoriteBorder
        HoroscopeCalculator.LifeArea.FINANCE -> Icons.Outlined.AccountBalance
        HoroscopeCalculator.LifeArea.FAMILY -> Icons.Outlined.Home
        HoroscopeCalculator.LifeArea.SPIRITUALITY -> Icons.Outlined.Star
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(areaColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = areaIcon,
                        contentDescription = null,
                        tint = areaColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title and rating
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = prediction.area.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Row {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < prediction.rating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = if (index < prediction.rating) areaColor else AppTheme.TextSubtle,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // Expand indicator
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text(
                        text = prediction.prediction,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(areaColor.copy(alpha = 0.1f))
                            .padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Lightbulb,
                            contentDescription = null,
                            tint = areaColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = prediction.advice,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LuckyElementsCard(lucky: HoroscopeCalculator.LuckyElements) {
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
                text = "Lucky Elements",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LuckyElement(
                    icon = Icons.Outlined.Numbers,
                    label = "Number",
                    value = lucky.number.toString()
                )
                LuckyElement(
                    icon = Icons.Outlined.Palette,
                    label = "Color",
                    value = lucky.color.split(",").first().trim()
                )
                LuckyElement(
                    icon = Icons.Outlined.Explore,
                    label = "Direction",
                    value = lucky.direction
                )
                LuckyElement(
                    icon = Icons.Outlined.Diamond,
                    label = "Gemstone",
                    value = lucky.gemstone
                )
            }
        }
    }
}

@Composable
private fun LuckyElement(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AppTheme.ChipBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.AccentGold,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextPrimary,
            maxLines = 1
        )
    }
}

@Composable
private fun RecommendationsCard(
    recommendations: List<String>,
    cautions: List<String>
) {
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
            // Recommendations
            if (recommendations.isNotEmpty()) {
                Text(
                    text = "Recommendations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.SuccessColor
                )

                Spacer(modifier = Modifier.height(12.dp))

                recommendations.forEach { rec ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = AppTheme.SuccessColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = rec,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }

            // Cautions
            if (cautions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Cautions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.WarningColor
                )

                Spacer(modifier = Modifier.height(12.dp))

                cautions.forEach { caution ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = null,
                            tint = AppTheme.WarningColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = caution,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AffirmationCard(affirmation: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.AccentPrimary.copy(alpha = 0.15f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.FormatQuote,
                contentDescription = null,
                tint = AppTheme.AccentPrimary,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = affirmation,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Today's Affirmation",
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.AccentPrimary
            )
        }
    }
}

// Weekly components
@Composable
private fun WeeklyOverviewHeader(weekly: HoroscopeCalculator.WeeklyHoroscope) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d")

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
                text = "${weekly.startDate.format(dateFormatter)} - ${weekly.endDate.format(dateFormatter)}",
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.TextMuted
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = weekly.weeklyTheme,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = weekly.weeklyOverview,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun WeeklyEnergyChart(dailyHighlights: List<HoroscopeCalculator.DailyHighlight>) {
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
                text = "Weekly Energy Flow",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dailyHighlights.forEach { highlight ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Energy bar
                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .height(80.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(AppTheme.ChipBackground),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(highlight.energy / 10f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when {
                                            highlight.energy < 4 -> AppTheme.ErrorColor
                                            highlight.energy < 7 -> AppTheme.WarningColor
                                            else -> AppTheme.SuccessColor
                                        }
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = highlight.dayOfWeek.take(3),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyDatesSection(keyDates: List<HoroscopeCalculator.KeyDate>) {
    if (keyDates.isEmpty()) return

    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Key Dates",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        keyDates.forEach { keyDate ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (keyDate.isPositive) AppTheme.SuccessColor.copy(alpha = 0.15f)
                                else AppTheme.WarningColor.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = keyDate.date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (keyDate.isPositive) AppTheme.SuccessColor else AppTheme.WarningColor
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = keyDate.event,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = keyDate.significance,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeeklyPredictionsSection(predictions: Map<HoroscopeCalculator.LifeArea, String>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Weekly Overview by Area",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        predictions.entries.forEach { (area, prediction) ->
            var expanded by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { expanded = !expanded },
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = area.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = AppTheme.TextMuted
                        )
                    }

                    AnimatedVisibility(visible = expanded) {
                        Text(
                            text = prediction,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextSecondary,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeeklyAdviceCard(advice: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.AccentPrimary.copy(alpha = 0.15f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Weekly Advice",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.AccentPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = advice,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextPrimary,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun EmptyHomeState() {
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
                imageVector = Icons.Outlined.PersonAddAlt,
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
                text = "Select or create a profile to view your personalized horoscope predictions",
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}
