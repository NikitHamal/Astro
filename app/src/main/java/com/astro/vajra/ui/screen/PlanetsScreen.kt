package com.astro.vajra.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.ui.components.NakshatraDetailDialog
import com.astro.vajra.ui.components.dialogs.PlanetDetailDialog
import com.astro.vajra.ui.screen.chartdetail.tabs.PlanetsTabContent
import com.astro.vajra.ui.theme.AppTheme

/**
 * Planets Screen - Standalone screen for detailed planetary analysis
 *
 * Features:
 * - Detailed planetary positions and states
 * - Retrograde, combust, and planetary war indicators
 * - Dignity status (exaltation, debilitation, own sign)
 * - Nakshatra and pada information
 * - Interactive planet cards with detailed dialog views
 *
 * Note: Shadbala analysis is available in the dedicated ShadbalaScreen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanetsScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    if (chart == null) {
        EmptyChartScreen(
            title = stringResource(StringKey.FEATURE_PLANETS),
            message = stringResource(StringKey.NO_PROFILE_MESSAGE),
            onBack = onBack
        )
        return
    }

    // Dialog states
    var selectedPlanetPosition by remember { mutableStateOf<PlanetPosition?>(null) }
    var selectedNakshatra by remember { mutableStateOf<Pair<Nakshatra, Int>?>(null) }

    // Render dialogs
    selectedPlanetPosition?.let { position ->
        PlanetDetailDialog(
            planetPosition = position,
            chart = chart,
            onDismiss = { selectedPlanetPosition = null }
        )
    }

    selectedNakshatra?.let { (nakshatra, pada) ->
        NakshatraDetailDialog(
            nakshatra = nakshatra,
            pada = pada,
            onDismiss = { selectedNakshatra = null }
        )
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            PlanetsTopBar(
                chartName = chart.birthData.name,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppTheme.ScreenBackground)
        ) {
            PlanetsTabContent(
                chart = chart,
                onPlanetClick = { selectedPlanetPosition = it },
                onNakshatraClick = { nakshatra, pada ->
                    selectedNakshatra = nakshatra to pada
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlanetsTopBar(
    chartName: String,
    onBack: () -> Unit
) {
    NeoVedicPageHeader(
                title = stringResource(StringKey.FEATURE_PLANETS),
                subtitle = chartName,
                onBack = onBack
            )
}




