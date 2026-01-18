package com.astro.storm.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ui.screen.dasha.DashaSystemsScreen
import com.astro.storm.ui.viewmodel.DashaViewModel

/**
 * Dashas Screen - Unified screen for all dasha systems
 *
 * This screen now serves as a wrapper that delegates to the new
 * DashaSystemsScreen which provides tabbed navigation for:
 * - Vimsottari Dasha (120-year cycle)
 * - Yogini Dasha (36-year cycle)
 * - Ashtottari Dasha (108-year cycle)
 * - Sudarshana Chakra (Triple reference)
 * - Chara Dasha (Jaimini sign-based)
 *
 * For a cleaner, more modern UI/UX with all dasha systems
 * accessible from a single unified interface.
 */
@Composable
fun DashasScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    onNavigateToYoginiDasha: () -> Unit = {},
    onNavigateToCharaDasha: () -> Unit = {},
    viewModel: DashaViewModel = hiltViewModel()
) {
    DashaSystemsScreen(
        chart = chart,
        onBack = onBack,
        onNavigateToYoginiDasha = onNavigateToYoginiDasha,
        onNavigateToCharaDasha = onNavigateToCharaDasha,
        viewModel = viewModel
    )
}
