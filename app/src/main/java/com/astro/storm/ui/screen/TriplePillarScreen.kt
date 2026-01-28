package com.astro.storm.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.width
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.SwissEphemerisEngine
import com.astro.storm.ephemeris.prediction.TriplePillarEngine
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TriplePillarScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val ephemeris = remember { SwissEphemerisEngine.getInstance(context) }
    val formatter = remember { DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault()) }

    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now().plusMonths(12)) }
    var result by remember { mutableStateOf<TriplePillarEngine.TriplePillarSynthesisResult?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(chart) {
        if (chart != null) {
            isLoading = true
            error = null
            result = runCatching {
                withContext(Dispatchers.Default) {
                    TriplePillarEngine.generateSuccessTimeline(chart, ephemeris, startDate, endDate)
                }
            }.onFailure { throwable ->
                error = throwable.message ?: "Unable to generate synthesis"
            }.getOrNull()
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Triple-Pillar Synthesis", color = AppTheme.TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = AppTheme.TextPrimary)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (chart != null) {
                                isLoading = true
                                error = null
                                result = runCatching {
                                    TriplePillarEngine.generateSuccessTimeline(chart, ephemeris, startDate, endDate)
                                }.onFailure { throwable ->
                                    error = throwable.message ?: "Unable to generate synthesis"
                                }.getOrNull()
                                isLoading = false
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Refresh", tint = AppTheme.TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppTheme.ScreenBackground)
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { padding ->
        when {
            chart == null -> {
                EmptyState(modifier = Modifier.padding(padding))
            }
            error != null -> {
                ErrorState(
                    message = error ?: "Unable to generate synthesis",
                    onRetry = {
                        if (chart != null) {
                            isLoading = true
                            error = null
                            result = runCatching {
                                TriplePillarEngine.generateSuccessTimeline(chart, ephemeris, startDate, endDate)
                            }.onFailure { throwable ->
                                error = throwable.message ?: "Unable to generate synthesis"
                            }.getOrNull()
                            isLoading = false
                        }
                    },
                    modifier = Modifier.padding(padding)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item(key = "range") {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Outlined.Timeline, contentDescription = null, tint = AppTheme.AccentPrimary)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        "Timeline Window",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = AppTheme.TextPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "${startDate.format(formatter)} - ${endDate.format(formatter)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AppTheme.TextSecondary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(onClick = { startDate = startDate.minusMonths(6); endDate = endDate.minusMonths(6) }) {
                                        Text("-6 Months")
                                    }
                                    Button(onClick = { startDate = startDate.plusMonths(6); endDate = endDate.plusMonths(6) }) {
                                        Text("+6 Months")
                                    }
                                }
                            }
                        }
                    }

                    item(key = "loading") {
                        AnimatedVisibility(visible = isLoading, enter = fadeIn(tween(200)), exit = fadeOut(tween(200))) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
                        }
                    }

                    result?.let { synthesis ->
                        item(key = "summary") {
                            SummaryCard(text = synthesis.summary)
                        }

                        if (synthesis.peaks.isNotEmpty()) {
                            item(key = "peaks_header") {
                                SectionHeader(title = "Peak Windows")
                            }
                            items(synthesis.peaks) { peak ->
                                PeakCard(peak = peak, formatter = formatter)
                            }
                        }

                        item(key = "timeline_header") {
                            SectionHeader(title = "Success Probability Timeline")
                        }

                        items(synthesis.points) { point ->
                            TimelinePointCard(point = point)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Synthesis Summary",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium, color = AppTheme.TextSecondary)
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = AppTheme.TextPrimary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun PeakCard(
    peak: TriplePillarEngine.TriplePillarPeak,
    formatter: DateTimeFormatter
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                "${peak.start.toLocalDate().format(formatter)} - ${peak.end.toLocalDate().format(formatter)}",
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Max Probability ${peak.maxProbability}% • ${peak.mahaLord.displayName}${peak.antarLord?.let { "/${it.displayName}" } ?: ""}",
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary
            )
        }
    }
}

@Composable
private fun TimelinePointCard(point: TriplePillarEngine.TriplePillarTimelinePoint) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = point.dateTime.toLocalDate().toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${point.mahaLord.displayName}${point.antarLord?.let { "/${it.displayName}" } ?: ""} • ${point.transitSign.displayName}",
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = point.successProbability / 100f,
                color = if (point.peakConditionMet) AppTheme.SuccessColor else AppTheme.AccentPrimary,
                trackColor = AppTheme.DividerColor.copy(alpha = 0.2f),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Success ${point.successProbability}% • BAV ${point.ashtakavargaBav} SAV ${point.ashtakavargaSav}",
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select a chart to view Triple-Pillar synthesis.",
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextSecondary
        )
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextSecondary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}
