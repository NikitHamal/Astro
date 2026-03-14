package com.astro.vajra.ui.screen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.ephemeris.VedicOracleEngine
import com.astro.vajra.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private sealed interface OracleUiState {
    data object Loading : OracleUiState
    data class Success(val analysis: VedicOracleEngine.OracleAnalysis) : OracleUiState
    data class Error(val message: String) : OracleUiState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VedicOracleScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(
            title = "Vedic Oracle",
            message = stringResource(StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    val context = LocalContext.current
    var uiState by remember(chart.id, chart.calculationTime) { mutableStateOf<OracleUiState>(OracleUiState.Loading) }

    LaunchedEffect(chart.id, chart.calculationTime) {
        uiState = OracleUiState.Loading
        uiState = try {
            val customDsl = withContext(Dispatchers.IO) {
                context.assets.open("yoga_rules.json").bufferedReader().use { it.readText() }
            }
            val analysis = withContext(Dispatchers.Default) {
                VedicOracleEngine.analyze(context, chart, customYogaDsl = customDsl)
            }
            OracleUiState.Success(analysis)
        } catch (error: Exception) {
            OracleUiState.Error(error.message ?: "Oracle analysis failed")
        }
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            TopAppBar(
                title = { Text("Vedic Oracle") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            OracleUiState.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Synthesizing Dashas, transits, KP, Muhurta, and karmic layers…")
                }
            }

            is OracleUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }

            is OracleUiState.Success -> {
                OracleContent(
                    analysis = state.analysis,
                    paddingValues = paddingValues
                )
            }
        }
    }
}

@Composable
private fun OracleContent(
    analysis: VedicOracleEngine.OracleAnalysis,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionCard(
                title = "Triple-Pillar Predictive Engine",
                subtitle = analysis.triplePillar.summary
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = {}, label = { Text("Maha: ${analysis.triplePillar.currentMahadasha?.displayName ?: "—"}") })
                    AssistChip(onClick = {}, label = { Text("Antar: ${analysis.triplePillar.currentAntardasha?.displayName ?: "—"}") })
                }
                Spacer(modifier = Modifier.height(10.dp))
                analysis.triplePillar.peakWindows.take(5).forEach { point ->
                    BulletLine("${point.date}: ${point.successProbability}% • ${point.summary}")
                }
            }
        }

        item {
            SectionCard(
                title = "Bhrigu Nandi Nadi",
                subtitle = "Planetary link graph across nadi aspects and sign handshakes."
            ) {
                analysis.bhriguNandiNadi.chains.take(4).forEach { chain ->
                    BulletLine("${chain.planets.joinToString(" → ") { it.displayName }} • ${chain.cumulativeWeight.toInt()} strength")
                }
            }
        }

        item {
            SectionCard(
                title = "KP 4-Step Theory",
                subtitle = "Cusp sign lord → star lord → sub-lord → sub-sub-lord gatekeeping."
            ) {
                analysis.kpSystem.houseGatekeepers.take(6).forEach { gatekeeper ->
                    BulletLine("House ${gatekeeper.house}: ${gatekeeper.decision}")
                }
            }
        }

        item {
            SectionCard(
                title = "Muhurta Optimization",
                subtitle = "Highest-scoring 5-minute search windows for vehicle-related undertakings."
            ) {
                analysis.muhurtaOptimization.bestWindows.take(5).forEach { window ->
                    BulletLine("${window.dateTime}: ${window.score} • ${window.nakshatra.displayName} • ${window.tithi}")
                }
            }
        }

        item {
            SectionCard(
                title = "Ishta Devata & Beeja",
                subtitle = analysis.ishtaDevata.rationale
            ) {
                BulletLine("Atmakaraka: ${analysis.ishtaDevata.atmakaraka.displayName}")
                BulletLine("Ishta Devata: ${analysis.ishtaDevata.ishtaDevata}")
                BulletLine("Birth Akshara: ${analysis.ishtaDevata.birthAkshara}")
                BulletLine("Generated mantra: ${analysis.ishtaDevata.generatedMantra}")
            }
        }

        item {
            SectionCard(
                title = "Sarvatobhadra Vedha",
                subtitle = "Current chakra score ${analysis.sarvatobhadraVedha.overallScore}/100"
            ) {
                analysis.sarvatobhadraVedha.strongestVedhas.take(4).forEach { BulletLine(it) }
            }
        }

        item {
            SectionCard(
                title = "Panchapakshi",
                subtitle = analysis.panchapakshi.interpretation
            ) {
                BulletLine("Birth bird: ${analysis.panchapakshi.birthBird.name}")
                BulletLine("Current activity: ${analysis.panchapakshi.currentActivity.name} • ${analysis.panchapakshi.activityStrength}/100")
                analysis.panchapakshi.nextTransitions.take(3).forEach { transition ->
                    BulletLine("${transition.startsAt}: ${transition.activity.name}")
                }
            }
        }

        item {
            SectionCard(
                title = "Varga Deities & Shakti",
                subtitle = analysis.vargaDeities.summary
            ) {
                analysis.vargaDeities.highlights.take(6).forEach { placement ->
                    BulletLine("${placement.chartType.shortName} ${placement.planet.displayName}: ${placement.deity} • ${placement.shakti}")
                }
            }
        }

        item {
            SectionCard(
                title = "Chakra Health",
                subtitle = "Strongest: ${analysis.chakraHealth.strongest.name} • Weakest: ${analysis.chakraHealth.weakest.name}"
            ) {
                analysis.chakraHealth.chakras.forEach { chakra ->
                    BulletLine("${chakra.name}: ${chakra.score}/100 • ${chakra.status}")
                }
            }
        }

        item {
            SectionCard(
                title = "Astro DSL",
                subtitle = "Local rule interpreter matches user-extensible yoga definitions."
            ) {
                if (analysis.customYogas.isEmpty()) {
                    BulletLine("No custom yoga rules matched the current chart.")
                } else {
                    analysis.customYogas.forEach { yoga ->
                        BulletLine("${yoga.name}: ${yoga.matchedConditions.joinToString("; ")}")
                    }
                }
            }
        }

        item {
            SectionCard(
                title = "Bhrigu Bindu & Karmic Nodes",
                subtitle = "Karmic weight ${analysis.karmicNode.karmicWeight}/100"
            ) {
                analysis.karmicNode.activatedFactors.forEach { BulletLine(it) }
                analysis.karmicNode.divisionalEchoes.forEach { BulletLine(it) }
            }
        }

        item {
            SectionCard(
                title = "Local Sidereal Sky",
                subtitle = "LST ${"%.2f".format(analysis.skyView.localSiderealTimeHours)}h"
            ) {
                analysis.skyView.bodies.take(7).forEach { body ->
                    BulletLine("${body.planet.displayName}: alt ${"%.1f".format(body.altitude)}°, az ${"%.1f".format(body.azimuth)}°")
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    subtitle: String,
    content: @Composable Column.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.AutoGraph, contentDescription = null, tint = AppTheme.AccentGold)
                Text(title, fontWeight = FontWeight.Bold, color = AppTheme.TextPrimary)
            }
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = AppTheme.TextSecondary)
            content()
        }
    }
}

@Composable
private fun BulletLine(text: String) {
    Text(
        text = "• $text",
        style = MaterialTheme.typography.bodyMedium,
        color = AppTheme.TextPrimary
    )
}
