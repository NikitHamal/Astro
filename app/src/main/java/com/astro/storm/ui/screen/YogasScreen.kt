package com.astro.storm.ui.screen

import androidx.compose.runtime.Composable
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ui.screen.yogas.YogasScreenRedesigned

/**
 * Yogas Screen - Displays planetary combinations (yogas) in the chart
 *
 * This screen now delegates to the redesigned YogasScreenRedesigned
 * which provides a modern, clean UI with:
 * - Summary statistics card with overall strength
 * - Category filter chips for easy navigation
 * - Expandable yoga cards with full details
 * - Visual strength indicators
 * - Planets and houses involved
 * - Effects and activation periods
 * - Cancellation factors for negative yogas
 * - Smooth animations throughout
 */
@Composable
fun YogasScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    YogasScreenRedesigned(
        chart = chart,
        onBack = onBack
    )
}
