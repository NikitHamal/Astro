package com.astro.storm.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.kp.KpSystemEngine
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KpSystemScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    var cusps by remember { mutableStateOf<List<KpSystemEngine.KpCuspInfo>>(emptyList()) }
    var selectedHouse by remember { mutableIntStateOf(1) }
    var selectedEvent by remember { mutableStateOf(KpSystemEngine.KpEventType.GENERAL) }
    var evaluation by remember { mutableStateOf<KpSystemEngine.KpEventEvaluation?>(null) }

    LaunchedEffect(chart) {
        if (chart != null) {
            cusps = withContext(Dispatchers.Default) { KpSystemEngine.calculateCusps(chart) }
            val cuspInfo = cusps.firstOrNull { it.house == selectedHouse }
            evaluation = cuspInfo?.let { KpSystemEngine.evaluateEvent(chart, it, selectedEvent) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KP Sub-Lord Navigator", color = AppTheme.TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = AppTheme.TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppTheme.ScreenBackground)
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { padding ->
        if (chart == null) {
            EmptyState(modifier = Modifier.padding(padding))
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item(key = "event_type") {
                EventTypeSelector(selected = selectedEvent, onSelected = {
                    selectedEvent = it
                    val cuspInfo = cusps.firstOrNull { cusp -> cusp.house == selectedHouse }
                    evaluation = cuspInfo?.let { info -> KpSystemEngine.evaluateEvent(chart, info, selectedEvent) }
                })
            }

            item(key = "cusp_selector") {
                CuspSelector(
                    cusps = cusps,
                    selectedHouse = selectedHouse,
                    onSelected = { house ->
                        selectedHouse = house
                        val cuspInfo = cusps.firstOrNull { it.house == house }
                        evaluation = cuspInfo?.let { info -> KpSystemEngine.evaluateEvent(chart, info, selectedEvent) }
                    }
                )
            }

            evaluation?.let { eval ->
                item(key = "cusp_summary") { CuspSummaryCard(eval.cuspInfo) }
                item(key = "evaluation") { EvaluationCard(eval) }
            }
        }
    }
}

@Composable
private fun EventTypeSelector(
    selected: KpSystemEngine.KpEventType,
    onSelected: (KpSystemEngine.KpEventType) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Report, contentDescription = null, tint = AppTheme.AccentPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Event Verification", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = AppTheme.TextPrimary)
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(KpSystemEngine.KpEventType.entries) { event ->
                    FilterChip(
                        selected = event == selected,
                        onClick = { onSelected(event) },
                        label = { Text(event.displayName, style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.15f),
                            selectedLabelColor = AppTheme.AccentPrimary
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CuspSelector(
    cusps: List<KpSystemEngine.KpCuspInfo>,
    selectedHouse: Int,
    onSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Info, contentDescription = null, tint = AppTheme.AccentPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cusp Sub-Lord Navigator", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = AppTheme.TextPrimary)
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(cusps) { cusp ->
                    FilterChip(
                        selected = cusp.house == selectedHouse,
                        onClick = { onSelected(cusp.house) },
                        label = { Text("H${cusp.house} ${cusp.subLord.displayName}", style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CuspSummaryCard(cusp: KpSystemEngine.KpCuspInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text("House ${cusp.house}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = AppTheme.TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Sign: ${cusp.sign.displayName} (Lord ${cusp.signLord.displayName})", style = MaterialTheme.typography.bodySmall, color = AppTheme.TextSecondary)
            Text("Star Lord: ${cusp.starLord.displayName}", style = MaterialTheme.typography.bodySmall, color = AppTheme.TextSecondary)
            Text("Sub Lord: ${cusp.subLord.displayName}", style = MaterialTheme.typography.bodySmall, color = AppTheme.TextSecondary)
            Text("Sub-Sub Lord: ${cusp.subSubLord.displayName}", style = MaterialTheme.typography.bodySmall, color = AppTheme.TextSecondary)
        }
    }
}

@Composable
private fun EvaluationCard(evaluation: KpSystemEngine.KpEventEvaluation) {
    val verdict = if (evaluation.isFavorable) "YES" else "NO"
    val verdictColor = if (evaluation.isFavorable) AppTheme.SuccessColor else AppTheme.ErrorColor

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = verdictColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text("4-Step Verdict: $verdict", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = verdictColor)
            }
            Spacer(modifier = Modifier.height(12.dp))
            evaluation.steps.forEach { step ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(step.step, style = MaterialTheme.typography.bodyMedium, color = AppTheme.TextPrimary)
                        Text(
                            "${step.planet.displayName} signifies ${step.planetHouses.sorted().joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                    Text(
                        if (step.isPass) "Pass" else "Fail",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (step.isPass) AppTheme.SuccessColor else AppTheme.ErrorColor
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
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
            text = "Select a chart to view KP analysis.",
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextSecondary
        )
    }
}
