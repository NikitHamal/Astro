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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.SpaceGroteskFamily

private data class AdvancedEngineInfo(
    val title: String,
    val description: String
)

@Composable
fun AdvancedEnginesScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val engines = remember {
        listOf(
            AdvancedEngineInfo(
                title = "Karmic Node Transformation Analyzer",
                description = "Rahu-Ketu axis analysis across D1/D9/D10/D12/D60 with karmic weight scoring."
            ),
            AdvancedEngineInfo(
                title = "Sarvatobhadra Chakra Calculator",
                description = "Transit vedha classification with cell state tracking and impact scoring."
            ),
            AdvancedEngineInfo(
                title = "Chakra Mapping Calculator",
                description = "Planet-to-chakra mapping with health scoring and dosha balance profiling."
            ),
            AdvancedEngineInfo(
                title = "Astro DSL",
                description = "Custom yoga definition language with lexer, parser, and evaluator."
            ),
            AdvancedEngineInfo(
                title = "KP System Calculator",
                description = "Krishnamurti sub-lord system with 249 sub-divisions and 4-step verification."
            ),
            AdvancedEngineInfo(
                title = "Muhurta Search Engine",
                description = "Multi-layer muhurta scoring with Tarabala and Chandrabala."
            ),
            AdvancedEngineInfo(
                title = "Bhrigu Nandi Nadi Engine",
                description = "Lagna-independent planetary graph analysis with chain discovery."
            ),
            AdvancedEngineInfo(
                title = "Panchapakshi Calculator",
                description = "Five-bird timing cycles with activity and strength profiling."
            ),
            AdvancedEngineInfo(
                title = "Triple Pillar Predictive Engine",
                description = "Dasha-Transit-Ashtakavarga synthesis with probability scoring."
            ),
            AdvancedEngineInfo(
                title = "Ishta Devata Calculator",
                description = "Atmakaraka-based deity determination from Karakamsha."
            ),
            AdvancedEngineInfo(
                title = "Sidereal Sky Calculator",
                description = "Ecliptic-equatorial-horizontal transforms with rise/set timing."
            ),
            AdvancedEngineInfo(
                title = "Varga Deity Calculator",
                description = "D3/D7/D9/D10/D60 deity mappings with balance analysis."
            )
        )
    }

    Scaffold(
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKey.FEATURE_ADVANCED_ENGINES),
                subtitle = stringResource(StringKey.FEATURE_ADVANCED_ENGINES_DESC),
                onBack = onBack
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                val statusText = if (chart == null) {
                    "Select a chart to wire engine outputs. This list confirms the modules are in the codebase."
                } else {
                    "These engines are implemented in the codebase. Dedicated UI views are being wired here."
                }
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall.copy(color = AppTheme.TextMuted),
                    fontFamily = SpaceGroteskFamily
                )
            }

            items(engines) { engine ->
                AdvancedEngineCard(engine)
            }
        }
    }
}

@Composable
private fun AdvancedEngineCard(engine: AdvancedEngineInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = AppTheme.AccentPrimary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = engine.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = AppTheme.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = engine.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}
