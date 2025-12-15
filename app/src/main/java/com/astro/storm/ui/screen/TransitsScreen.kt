package com.astro.storm.ui.screen

import androidx.compose.runtime.Composable
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ui.screen.transits.TransitsScreenRedesigned

/**
 * Transits Screen - Displays current planetary transits analysis
 *
 * This screen now delegates to the redesigned TransitsScreenRedesigned
 * which provides a modern, clean UI with:
 * - Summary card with transit overview
 * - Tab navigation (Current Positions, By House, Upcoming, Aspects)
 * - Expandable planet cards with transit details
 * - House-by-house transit analysis
 * - Upcoming significant transits
 * - Transit to natal aspects
 * - Visual strength indicators
 * - Smooth animations throughout
 */
@Composable
fun TransitsScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    TransitsScreenRedesigned(
        chart = chart,
        onBack = onBack
    )
}
