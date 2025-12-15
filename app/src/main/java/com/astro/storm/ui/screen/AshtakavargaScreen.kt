package com.astro.storm.ui.screen

import androidx.compose.runtime.Composable
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ui.screen.ashtakavarga.AshtakavargaScreenRedesigned

/**
 * Ashtakavarga Screen - Displays bindu strength analysis
 *
 * This screen now delegates to the redesigned AshtakavargaScreenRedesigned
 * which provides a modern, clean UI with:
 * - Summary card with total bindus and strength
 * - Tab navigation (Overview, Sarvashtakavarga, By Planet, By House)
 * - Visual house strength distribution chart
 * - Key insights (strongest/weakest houses)
 * - Planet-wise bindu details
 * - House-wise bindu details with planet contributions
 * - Smooth animations throughout
 */
@Composable
fun AshtakavargaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    AshtakavargaScreenRedesigned(
        chart = chart,
        onBack = onBack
    )
}
