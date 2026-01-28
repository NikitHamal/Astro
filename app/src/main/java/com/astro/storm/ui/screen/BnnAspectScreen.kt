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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.width
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.bnn.BnnAspectEngine
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun BnnAspectScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    var result by remember { mutableStateOf<BnnAspectEngine.BnnAnalysisResult?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(chart) {
        if (chart != null) {
            error = null
            result = runCatching {
                withContext(Dispatchers.Default) { BnnAspectEngine.analyze(chart) }
            }.onFailure { throwable ->
                error = throwable.message ?: "Unable to compute BNN links"
            }.getOrNull()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BNN Aspect Engine", color = AppTheme.TextPrimary) },
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
        when {
            chart == null -> EmptyState(modifier = Modifier.padding(padding))
            error != null -> ErrorState(
                message = error ?: "Unable to compute BNN links",
                onRetry = {
                    if (chart != null) {
                        error = null
                        result = BnnAspectEngine.analyze(chart)
                    }
                },
                modifier = Modifier.padding(padding)
            )
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    result?.let { analysis ->
                        if (analysis.signatures.isNotEmpty()) {
                            item(key = "signature_header") { SectionHeader(title = "Planetary Signatures") }
                            items(analysis.signatures) { signature ->
                                SignatureCard(signature)
                            }
                        }

                        item(key = "links_header") { SectionHeader(title = "Planetary Links") }
                        items(analysis.links) { link ->
                            LinkCard(link)
                        }

                        if (analysis.chains.isNotEmpty()) {
                            item(key = "chains_header") { SectionHeader(title = "Link Chains") }
                            items(analysis.chains) { chain ->
                                ChainCard(chain)
                            }
                        }
                    }
                }
            }
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
private fun SignatureCard(signature: BnnAspectEngine.BnnSignature) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(signature.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = AppTheme.TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Text(signature.description, style = MaterialTheme.typography.bodySmall, color = AppTheme.TextSecondary)
        }
    }
}

@Composable
private fun LinkCard(link: BnnAspectEngine.BnnPlanetaryLink) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Link, contentDescription = null, tint = AppTheme.AccentPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${link.from.displayName} → ${link.to.displayName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextPrimary
                )
            }
            Text(
                text = link.aspectType.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun ChainCard(chain: BnnAspectEngine.BnnLinkChain) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = chain.planets.joinToString(" → ") { it.displayName },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(chain.description, style = MaterialTheme.typography.bodySmall, color = AppTheme.TextSecondary)
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
            text = "Select a chart to view BNN planetary links.",
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
        Text(text = message, style = MaterialTheme.typography.bodyMedium, color = AppTheme.TextSecondary)
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onRetry) { Text(text = "Retry") }
    }
}
