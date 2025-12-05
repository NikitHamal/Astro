package com.astro.storm.ui.screen.chartdetail.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.astro.storm.ephemeris.AshtakavargaCalculator
import com.astro.storm.ephemeris.TransitAnalyzer
import com.astro.storm.ui.screen.chartdetail.ChartDetailColors
import com.astro.storm.ui.screen.chartdetail.ChartDetailUtils
import java.time.format.DateTimeFormatter

@Composable
fun TransitsTabContent(chart: VedicChart) {
    val context = LocalContext.current
    val transitAnalysis = remember(chart) {
        val analyzer = TransitAnalyzer(context)
        try {
            analyzer.analyzeTransits(chart)
        } finally {
            analyzer.close()
        }
    }

    val expandedSections = remember { mutableStateListOf<String>() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { TransitHeader(transitAnalysis) }

        item { TransitQualityCard(transitAnalysis) }

        transitAnalysis.sadeSatiStatus?.let { sadeSati ->
            item { SadeSatiCard(sadeSati) }
        }

        transitAnalysis.specialSaturnTransit?.let { saturnTransit ->
            item { SpecialSaturnCard(saturnTransit) }
        }

        item {
            CurrentTransitsCard(
                analysis = transitAnalysis,
                isExpanded = "CurrentTransits" in expandedSections,
                onToggleExpand = { expanded ->
                    if (expanded) expandedSections.add("CurrentTransits")
                    else expandedSections.remove("CurrentTransits")
                }
            )
        }

        item {
            GocharaResultsCard(
                analysis = transitAnalysis,
                isExpanded = "GocharaResults" in expandedSections,
                onToggleExpand = { expanded ->
                    if (expanded) expandedSections.add("GocharaResults")
                    else expandedSections.remove("GocharaResults")
                }
            )
        }

        if (transitAnalysis.vedicDrishtiResults.isNotEmpty()) {
            item {
                VedicDrishtiCard(
                    analysis = transitAnalysis,
                    isExpanded = "VedicDrishti" in expandedSections,
                    onToggleExpand = { expanded ->
                        if (expanded) expandedSections.add("VedicDrishti")
                        else expandedSections.remove("VedicDrishti")
                    }
                )
            }
        }

        if (transitAnalysis.transitAspects.isNotEmpty()) {
            item {
                TransitAspectsCard(
                    analysis = transitAnalysis,
                    isExpanded = "TransitAspects" in expandedSections,
                    onToggleExpand = { expanded ->
                        if (expanded) expandedSections.add("TransitAspects")
                        else expandedSections.remove("TransitAspects")
                    }
                )
            }
        }

        if (transitAnalysis.ashtakavargaScores.isNotEmpty()) {
            item {
                AshtakavargaScoresCard(
                    scores = transitAnalysis.ashtakavargaScores,
                    isExpanded = "Ashtakavarga" in expandedSections,
                    onToggleExpand = { expanded ->
                        if (expanded) expandedSections.add("Ashtakavarga")
                        else expandedSections.remove("Ashtakavarga")
                    }
                )
            }
        }

        item { FocusAreasCard(transitAnalysis.overallAssessment) }

        item { RecommendationsCard(transitAnalysis.overallAssessment) }

        if (transitAnalysis.significantPeriods.isNotEmpty()) {
            item { SignificantPeriodsCard(transitAnalysis) }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun TransitHeader(analysis: TransitAnalyzer.TransitAnalysis) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Current Transits",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ChartDetailColors.TextPrimary
            )
            Text(
                text = analysis.transitDateTime.format(dateFormatter),
                fontSize = 13.sp,
                color = ChartDetailColors.TextMuted
            )
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = ChartDetailColors.AccentBlue.copy(alpha = 0.12f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = ChartDetailColors.AccentBlue,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = analysis.transitDateTime.format(timeFormatter),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = ChartDetailColors.AccentBlue
                )
            }
        }
    }
}

@Composable
private fun TransitQualityCard(analysis: TransitAnalyzer.TransitAnalysis) {
    val assessment = analysis.overallAssessment
    val score = assessment.score
    val progress = (score / 100.0).coerceIn(0.0, 1.0).toFloat()

    val (qualityColor, qualityGradient) = when (assessment.quality) {
        TransitAnalyzer.TransitQuality.EXCELLENT -> ChartDetailColors.SuccessColor to
                Brush.horizontalGradient(listOf(Color(0xFF10B981), Color(0xFF34D399)))
        TransitAnalyzer.TransitQuality.GOOD -> ChartDetailColors.AccentTeal to
                Brush.horizontalGradient(listOf(Color(0xFF14B8A6), Color(0xFF2DD4BF)))
        TransitAnalyzer.TransitQuality.MODERATE -> ChartDetailColors.AccentBlue to
                Brush.horizontalGradient(listOf(Color(0xFF3B82F6), Color(0xFF60A5FA)))
        TransitAnalyzer.TransitQuality.MIXED -> ChartDetailColors.AccentGold to
                Brush.horizontalGradient(listOf(Color(0xFFF59E0B), Color(0xFFFBBF24)))
        TransitAnalyzer.TransitQuality.CHALLENGING -> ChartDetailColors.WarningColor to
                Brush.horizontalGradient(listOf(Color(0xFFF97316), Color(0xFFFB923C)))
        TransitAnalyzer.TransitQuality.DIFFICULT -> ChartDetailColors.ErrorColor to
                Brush.horizontalGradient(listOf(Color(0xFFEF4444), Color(0xFFF87171)))
    }

    val qualityIcon = when (assessment.quality) {
        TransitAnalyzer.TransitQuality.EXCELLENT,
        TransitAnalyzer.TransitQuality.GOOD -> Icons.Outlined.TrendingUp
        TransitAnalyzer.TransitQuality.MODERATE,
        TransitAnalyzer.TransitQuality.MIXED -> Icons.Outlined.Schedule
        TransitAnalyzer.TransitQuality.CHALLENGING,
        TransitAnalyzer.TransitQuality.DIFFICULT -> Icons.Outlined.TrendingDown
    }

    val favorableCount = analysis.gocharaResults.count {
        it.effect in listOf(TransitAnalyzer.TransitEffect.EXCELLENT, TransitAnalyzer.TransitEffect.GOOD)
    }
    val challengingCount = analysis.gocharaResults.count {
        it.effect in listOf(TransitAnalyzer.TransitEffect.CHALLENGING, TransitAnalyzer.TransitEffect.DIFFICULT)
    }
    val neutralCount = analysis.gocharaResults.size - favorableCount - challengingCount

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ChartDetailColors.CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(qualityGradient, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                qualityIcon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = assessment.quality.displayName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = qualityColor
                            )
                            Text(
                                text = "Transit Period Quality",
                                fontSize = 12.sp,
                                color = ChartDetailColors.TextMuted
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = qualityColor.copy(alpha = 0.12f)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = String.format("%.0f", score),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = qualityColor
                        )
                        Text(
                            text = "Score",
                            fontSize = 10.sp,
                            color = ChartDetailColors.TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = qualityColor,
                trackColor = ChartDetailColors.DividerColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TransitStatBadge(
                    count = favorableCount,
                    label = "Favorable",
                    color = ChartDetailColors.SuccessColor,
                    icon = Icons.Outlined.TrendingUp
                )
                TransitStatBadge(
                    count = neutralCount,
                    label = "Neutral",
                    color = ChartDetailColors.TextSecondary,
                    icon = Icons.Outlined.Schedule
                )
                TransitStatBadge(
                    count = challengingCount,
                    label = "Challenging",
                    color = ChartDetailColors.WarningColor,
                    icon = Icons.Outlined.TrendingDown
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(color = ChartDetailColors.DividerColor)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = assessment.summary,
                fontSize = 13.sp,
                color = ChartDetailColors.TextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun TransitStatBadge(
    count: Int,
    label: String,
    color: Color,
    icon: ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(color.copy(alpha = 0.12f), CircleShape)
                .border(1.dp, color.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = ChartDetailColors.TextMuted
        )
    }
}

@Composable
private fun SadeSatiCard(sadeSati: TransitAnalyzer.SadeSatiStatus) {
    val phaseColor = when (sadeSati.phase) {
        TransitAnalyzer.SadeSatiPhase.PEAK -> ChartDetailColors.ErrorColor
        TransitAnalyzer.SadeSatiPhase.RISING -> ChartDetailColors.WarningColor
        TransitAnalyzer.SadeSatiPhase.SETTING -> ChartDetailColors.AccentGold
        else -> ChartDetailColors.TextSecondary
    }

    val phaseIcon = when (sadeSati.phase) {
        TransitAnalyzer.SadeSatiPhase.RISING -> "↗"
        TransitAnalyzer.SadeSatiPhase.PEAK -> "●"
        TransitAnalyzer.SadeSatiPhase.SETTING -> "↘"
        else -> "○"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = phaseColor.copy(alpha = 0.08f)
        )
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
                            .size(36.dp)
                            .background(phaseColor.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "♄",
                            fontSize = 18.sp,
                            color = phaseColor
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Sade Sati Active",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = phaseColor
                        )
                        Text(
                            text = sadeSati.phase.displayName,
                            fontSize = 12.sp,
                            color = ChartDetailColors.TextSecondary
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = phaseIcon,
                            fontSize = 16.sp,
                            color = phaseColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${String.format("%.0f", sadeSati.intensity * 100)}%",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = phaseColor
                        )
                    }
                    Text(
                        text = "Intensity",
                        fontSize = 10.sp,
                        color = ChartDetailColors.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { sadeSati.intensity.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = phaseColor,
                trackColor = phaseColor.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip(
                    label = "Saturn in ${sadeSati.saturnSign.displayName}",
                    color = phaseColor
                )
                InfoChip(
                    label = "~${String.format("%.1f", sadeSati.yearsRemaining)} years left",
                    color = ChartDetailColors.TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = sadeSati.description,
                fontSize = 12.sp,
                color = ChartDetailColors.TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun InfoChip(label: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun SpecialSaturnCard(saturnTransit: TransitAnalyzer.SpecialSaturnTransit) {
    val transitColor = when (saturnTransit.type) {
        TransitAnalyzer.SpecialSaturnType.ASHTAMA_SHANI -> ChartDetailColors.ErrorColor
        TransitAnalyzer.SpecialSaturnType.KANTAK_SHANI -> ChartDetailColors.WarningColor
        TransitAnalyzer.SpecialSaturnType.SATURN_RETURN -> ChartDetailColors.AccentPurple
        else -> ChartDetailColors.AccentGold
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = transitColor.copy(alpha = 0.08f)
        )
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
                            .size(36.dp)
                            .background(transitColor.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Warning,
                            contentDescription = null,
                            tint = transitColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = saturnTransit.type.displayName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = transitColor
                        )
                        Text(
                            text = "Special Saturn Transit",
                            fontSize = 11.sp,
                            color = ChartDetailColors.TextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = transitColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "${String.format("%.0f", saturnTransit.intensity * 100)}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = transitColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = saturnTransit.interpretation,
                fontSize = 12.sp,
                color = ChartDetailColors.TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun CurrentTransitsCard(
    analysis: TransitAnalyzer.TransitAnalysis,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = ChartDetailColors.AccentBlue)
            ) { onToggleExpand(!isExpanded) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChartDetailColors.CardBackground)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize(spring(stiffness = Spring.StiffnessMedium))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = ChartDetailColors.AccentBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Current Planetary Positions",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ChartDetailColors.TextPrimary
                        )
                        Text(
                            text = "Nirayana Longitudes",
                            fontSize = 11.sp,
                            color = ChartDetailColors.TextMuted
                        )
                    }
                }
                Icon(
                    Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = ChartDetailColors.TextMuted,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation)
                )
            }

            if (!isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(analysis.transitPositions.filter { it.planet in Planet.MAIN_PLANETS }) { position ->
                        CompactPlanetChip(position)
                    }
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(spring(stiffness = Spring.StiffnessMedium)) + fadeIn(),
                exit = shrinkVertically(spring(stiffness = Spring.StiffnessMedium)) + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    analysis.transitPositions.filter { it.planet in Planet.MAIN_PLANETS }.forEach { position ->
                        PlanetPositionRow(position)
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactPlanetChip(position: com.astro.storm.data.model.PlanetPosition) {
    val planetColor = ChartDetailColors.getPlanetColor(position.planet)

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = planetColor.copy(alpha = 0.12f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = position.planet.symbol,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = planetColor
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = position.sign.abbreviation,
                fontSize = 11.sp,
                color = ChartDetailColors.TextSecondary
            )
            if (position.isRetrograde) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "℞",
                    fontSize = 10.sp,
                    color = ChartDetailColors.WarningColor
                )
            }
        }
    }
}

@Composable
private fun PlanetPositionRow(position: com.astro.storm.data.model.PlanetPosition) {
    val planetColor = ChartDetailColors.getPlanetColor(position.planet)
    val nakshatra = getNakshatraInfo(position.longitude)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(planetColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = position.planet.symbol,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = position.planet.displayName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = ChartDetailColors.TextPrimary
                    )
                    if (position.isRetrograde) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = ChartDetailColors.WarningColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "℞",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChartDetailColors.WarningColor,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "${position.sign.displayName} ${ChartDetailUtils.formatDegreeInSign(position.longitude)}",
                    fontSize = 12.sp,
                    color = ChartDetailColors.TextSecondary
                )
                Text(
                    text = nakshatra,
                    fontSize = 11.sp,
                    color = ChartDetailColors.TextMuted
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = position.sign.abbreviation,
                fontSize = 18.sp,
                color = planetColor
            )
        }
    }
}

private fun getNakshatraInfo(longitude: Double): String {
    val nakshatras = listOf(
        "Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashira", "Ardra",
        "Punarvasu", "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni",
        "Hasta", "Chitra", "Swati", "Vishakha", "Anuradha", "Jyeshtha",
        "Mula", "Purva Ashadha", "Uttara Ashadha", "Shravana", "Dhanishtha", "Shatabhisha",
        "Purva Bhadrapada", "Uttara Bhadrapada", "Revati"
    )
    val nakshatraSpan = 360.0 / 27.0
    val index = (longitude / nakshatraSpan).toInt() % 27
    val pada = ((longitude % nakshatraSpan) / (nakshatraSpan / 4)).toInt() + 1
    return "${nakshatras[index]} (Pada $pada)"
}

@Composable
private fun GocharaResultsCard(
    analysis: TransitAnalyzer.TransitAnalysis,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    ExpandableCard(
        title = "Gochara Analysis",
        subtitle = "Transit from Moon Sign",
        icon = Icons.Outlined.RemoveRedEye,
        iconTint = ChartDetailColors.AccentPurple,
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        previewContent = {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(analysis.gocharaResults.take(5)) { result ->
                    GocharaPreviewChip(result)
                }
            }
        }
    ) {
        Column(modifier = Modifier.padding(top = 12.dp)) {
            analysis.gocharaResults.forEach { result ->
                GocharaResultRow(result = result)
                if (result != analysis.gocharaResults.last()) {
                    HorizontalDivider(
                        color = ChartDetailColors.DividerColor.copy(alpha = 0.5f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun GocharaPreviewChip(result: TransitAnalyzer.GocharaResult) {
    val effectColor = getEffectColor(result.effect)

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = effectColor.copy(alpha = 0.12f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = result.planet.symbol,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ChartDetailColors.getPlanetColor(result.planet)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "H${result.houseFromMoon}",
                fontSize = 10.sp,
                color = effectColor
            )
        }
    }
}

@Composable
private fun GocharaResultRow(result: TransitAnalyzer.GocharaResult) {
    val planetColor = ChartDetailColors.getPlanetColor(result.planet)
    val effectColor = getEffectColor(result.effect)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(planetColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = result.planet.symbol,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = result.planet.displayName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ChartDetailColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = ChartDetailColors.AccentBlue.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "House ${result.houseFromMoon}",
                            fontSize = 10.sp,
                            color = ChartDetailColors.AccentBlue,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    if (result.isRetrograde) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "℞",
                            fontSize = 12.sp,
                            color = ChartDetailColors.WarningColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (result.isVedhaAffected) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = ChartDetailColors.WarningColor.copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Outlined.Warning,
                                    contentDescription = null,
                                    tint = ChartDetailColors.WarningColor,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Vedha: ${result.vedhaSource?.symbol ?: ""}",
                                    fontSize = 9.sp,
                                    color = ChartDetailColors.WarningColor
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = ChartDetailColors.AccentGold.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "BAV: ${result.ashtakavargaBindu}/8",
                            fontSize = 9.sp,
                            color = ChartDetailColors.AccentGold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(6.dp),
            color = effectColor.copy(alpha = 0.12f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    when (result.effect) {
                        TransitAnalyzer.TransitEffect.EXCELLENT,
                        TransitAnalyzer.TransitEffect.GOOD,
                        TransitAnalyzer.TransitEffect.MODERATE -> Icons.Outlined.CheckCircle
                        else -> Icons.Outlined.Warning
                    },
                    contentDescription = null,
                    tint = effectColor,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = result.effect.displayName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = effectColor
                )
            }
        }
    }
}

@Composable
private fun VedicDrishtiCard(
    analysis: TransitAnalyzer.TransitAnalysis,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    ExpandableCard(
        title = "Vedic Planetary Aspects",
        subtitle = "Drishti to Natal Planets",
        icon = Icons.Outlined.RemoveRedEye,
        iconTint = ChartDetailColors.AccentTeal,
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        previewContent = {
            Text(
                text = "${analysis.vedicDrishtiResults.size} active aspects",
                fontSize = 12.sp,
                color = ChartDetailColors.TextMuted,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    ) {
        Column(modifier = Modifier.padding(top = 12.dp)) {
            analysis.vedicDrishtiResults.take(8).forEach { drishti ->
                VedicDrishtiRow(drishti)
                if (drishti != analysis.vedicDrishtiResults.take(8).last()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun VedicDrishtiRow(drishti: TransitAnalyzer.VedicDrishtiResult) {
    val transitColor = ChartDetailColors.getPlanetColor(drishti.transitingPlanet)
    val natalColor = ChartDetailColors.getPlanetColor(drishti.natalPlanet)

    val natureColor = when {
        drishti.nature.contains("Benefic") -> ChartDetailColors.SuccessColor
        drishti.nature.contains("Challenging") -> ChartDetailColors.WarningColor
        else -> ChartDetailColors.TextSecondary
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(transitColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = drishti.transitingPlanet.symbol,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Icon(
            Icons.Outlined.RemoveRedEye,
            contentDescription = null,
            tint = ChartDetailColors.TextMuted,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(16.dp)
        )

        Box(
            modifier = Modifier
                .size(24.dp)
                .background(natalColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = drishti.natalPlanet.symbol,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${drishti.aspectHouse}th house aspect",
                fontSize = 12.sp,
                color = ChartDetailColors.TextPrimary
            )
            Text(
                text = drishti.nature,
                fontSize = 10.sp,
                color = natureColor
            )
        }

        Surface(
            shape = RoundedCornerShape(4.dp),
            color = ChartDetailColors.AccentBlue.copy(alpha = 0.12f)
        ) {
            Text(
                text = "${String.format("%.0f", drishti.strength * 100)}%",
                fontSize = 10.sp,
                color = ChartDetailColors.AccentBlue,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TransitAspectsCard(
    analysis: TransitAnalyzer.TransitAnalysis,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    ExpandableCard(
        title = "Degree-Based Aspects",
        subtitle = "Conjunctions & Oppositions",
        icon = Icons.Outlined.Autorenew,
        iconTint = ChartDetailColors.AccentGold,
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        previewContent = {
            Text(
                text = "${analysis.transitAspects.size} active",
                fontSize = 12.sp,
                color = ChartDetailColors.TextMuted,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    ) {
        Column(modifier = Modifier.padding(top = 12.dp)) {
            analysis.transitAspects.take(8).forEach { aspect ->
                TransitAspectRow(aspect = aspect)
                if (aspect != analysis.transitAspects.take(8).last()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun TransitAspectRow(aspect: TransitAnalyzer.TransitAspect) {
    val transitPlanetColor = ChartDetailColors.getPlanetColor(aspect.transitingPlanet)
    val natalPlanetColor = ChartDetailColors.getPlanetColor(aspect.natalPlanet)
    val applyingColor = if (aspect.isApplying) ChartDetailColors.AccentTeal else ChartDetailColors.TextMuted

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
                    .size(24.dp)
                    .background(transitPlanetColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = aspect.transitingPlanet.symbol,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Surface(
                modifier = Modifier.padding(horizontal = 6.dp),
                shape = RoundedCornerShape(4.dp),
                color = ChartDetailColors.DividerColor
            ) {
                Text(
                    text = if (aspect.aspectType == "Conjunction") "☌" else "☍",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(natalPlanetColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = aspect.natalPlanet.symbol,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = aspect.aspectType,
                    fontSize = 12.sp,
                    color = ChartDetailColors.TextPrimary
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${String.format("%.1f", aspect.orb)}°",
                    fontSize = 11.sp,
                    color = ChartDetailColors.TextSecondary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = applyingColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = if (aspect.isApplying) "→" else "←",
                        fontSize = 10.sp,
                        color = applyingColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Text(
                text = "${String.format("%.0f", aspect.strength * 100)}% strength",
                fontSize = 10.sp,
                color = ChartDetailColors.TextMuted
            )
        }
    }
}

@Composable
private fun AshtakavargaScoresCard(
    scores: Map<Planet, AshtakavargaCalculator.TransitScore>,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    ExpandableCard(
        title = "Ashtakavarga Transit Scores",
        subtitle = "Bindu Points in Current Signs",
        icon = Icons.Outlined.Star,
        iconTint = ChartDetailColors.AccentGold,
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        previewContent = {
            val avgBindu = scores.values.map { it.binduScore }.average()
            Text(
                text = "Avg: ${String.format("%.1f", avgBindu)}/8",
                fontSize = 12.sp,
                color = ChartDetailColors.TextMuted,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    ) {
        Column(modifier = Modifier.padding(top = 12.dp)) {
            scores.entries.sortedByDescending { it.value.binduScore }.forEach { (planet, score) ->
                AshtakavargaScoreRow(planet, score)
                if (planet != scores.entries.last().key) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun AshtakavargaScoreRow(planet: Planet, score: AshtakavargaCalculator.TransitScore) {
    val planetColor = ChartDetailColors.getPlanetColor(planet)
    val binduColor = when {
        score.binduScore >= 5 -> ChartDetailColors.SuccessColor
        score.binduScore >= 4 -> ChartDetailColors.AccentTeal
        score.binduScore >= 3 -> ChartDetailColors.AccentGold
        else -> ChartDetailColors.WarningColor
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(planetColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = planet.symbol,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = planet.displayName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = ChartDetailColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { score.binduScore / 8f },
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = binduColor,
                    trackColor = ChartDetailColors.DividerColor
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${score.binduScore}/8",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = binduColor
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FocusAreasCard(assessment: TransitAnalyzer.OverallTransitAssessment) {
    if (assessment.focusAreas.isEmpty()) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChartDetailColors.CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = ChartDetailColors.AccentGold,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Key Focus Areas",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ChartDetailColors.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            assessment.focusAreas.forEachIndexed { index, area ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                ChartDetailColors.AccentGold.copy(alpha = 0.15f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChartDetailColors.AccentGold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = area,
                        fontSize = 13.sp,
                        color = ChartDetailColors.TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RecommendationsCard(assessment: TransitAnalyzer.OverallTransitAssessment) {
    if (assessment.recommendations.isEmpty()) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ChartDetailColors.AccentTeal.copy(alpha = 0.08f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = ChartDetailColors.AccentTeal,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Recommendations",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ChartDetailColors.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            assessment.recommendations.forEach { recommendation ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        fontSize = 14.sp,
                        color = ChartDetailColors.AccentTeal,
                        modifier = Modifier.padding(end = 8.dp, top = 2.dp)
                    )
                    Text(
                        text = recommendation,
                        fontSize = 13.sp,
                        color = ChartDetailColors.TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SignificantPeriodsCard(analysis: TransitAnalyzer.TransitAnalysis) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChartDetailColors.CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Event,
                    contentDescription = null,
                    tint = ChartDetailColors.AccentBlue,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Upcoming Significant Periods",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ChartDetailColors.TextPrimary
                    )
                    Text(
                        text = "Next 30 days",
                        fontSize = 11.sp,
                        color = ChartDetailColors.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            analysis.significantPeriods.forEach { period ->
                SignificantPeriodRow(period, dateFormatter)
                if (period != analysis.significantPeriods.last()) {
                    HorizontalDivider(
                        color = ChartDetailColors.DividerColor.copy(alpha = 0.5f),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SignificantPeriodRow(
    period: TransitAnalyzer.SignificantPeriod,
    dateFormatter: DateTimeFormatter
) {
    val intensityColor = when (period.intensity) {
        5 -> ChartDetailColors.ErrorColor
        4 -> ChartDetailColors.WarningColor
        3 -> ChartDetailColors.AccentGold
        2 -> ChartDetailColors.AccentTeal
        else -> ChartDetailColors.TextSecondary
    }

    val eventTypeLabel = when (period.eventType) {
        TransitAnalyzer.TransitEventType.SADE_SATI_PHASE_CHANGE -> "Sade Sati"
        TransitAnalyzer.TransitEventType.SATURN_SPECIAL_TRANSIT -> "Saturn"
        TransitAnalyzer.TransitEventType.JUPITER_SIGN_CHANGE -> "Jupiter"
        TransitAnalyzer.TransitEventType.RAHU_KETU_AXIS_SHIFT -> "Nodes"
        TransitAnalyzer.TransitEventType.RETROGRADE_STATION -> "Station ℞"
        TransitAnalyzer.TransitEventType.DIRECT_STATION -> "Station D"
        TransitAnalyzer.TransitEventType.PLANETARY_WAR -> "War"
        TransitAnalyzer.TransitEventType.ECLIPSE_PERIOD -> "Eclipse"
        TransitAnalyzer.TransitEventType.GENERAL_TRANSIT -> "Transit"
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = period.description,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = ChartDetailColors.TextPrimary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Event,
                        contentDescription = null,
                        tint = ChartDetailColors.TextMuted,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${period.startDate.format(dateFormatter)} - ${period.endDate.format(dateFormatter)}",
                        fontSize = 11.sp,
                        color = ChartDetailColors.TextMuted
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = intensityColor.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(period.intensity) {
                            Text(
                                text = "★",
                                fontSize = 8.sp,
                                color = intensityColor
                            )
                        }
                        repeat(5 - period.intensity) {
                            Text(
                                text = "☆",
                                fontSize = 8.sp,
                                color = intensityColor.copy(alpha = 0.4f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = ChartDetailColors.AccentPurple.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = eventTypeLabel,
                        fontSize = 9.sp,
                        color = ChartDetailColors.AccentPurple,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            period.planets.forEach { planet ->
                val planetColor = ChartDetailColors.getPlanetColor(planet)
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(planetColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = planet.symbol,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpandableCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit,
    previewContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = iconTint)
            ) { onToggleExpand(!isExpanded) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChartDetailColors.CardBackground)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize(spring(stiffness = Spring.StiffnessMedium))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ChartDetailColors.TextPrimary
                        )
                        Text(
                            text = subtitle,
                            fontSize = 11.sp,
                            color = ChartDetailColors.TextMuted
                        )
                    }
                }
                Icon(
                    Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = ChartDetailColors.TextMuted,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation)
                )
            }

            if (!isExpanded) {
                previewContent()
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(spring(stiffness = Spring.StiffnessMedium)) + fadeIn(),
                exit = shrinkVertically(spring(stiffness = Spring.StiffnessMedium)) + fadeOut()
            ) {
                expandedContent()
            }
        }
    }
}

private fun getEffectColor(effect: TransitAnalyzer.TransitEffect): Color {
    return when (effect) {
        TransitAnalyzer.TransitEffect.EXCELLENT -> ChartDetailColors.SuccessColor
        TransitAnalyzer.TransitEffect.GOOD -> ChartDetailColors.AccentTeal
        TransitAnalyzer.TransitEffect.MODERATE -> ChartDetailColors.AccentBlue
        TransitAnalyzer.TransitEffect.NEUTRAL -> ChartDetailColors.TextSecondary
        TransitAnalyzer.TransitEffect.CHALLENGING -> ChartDetailColors.WarningColor
        TransitAnalyzer.TransitEffect.DIFFICULT -> ChartDetailColors.ErrorColor
    }
}