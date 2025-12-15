package com.astro.storm.ui.screen

import androidx.compose.runtime.Composable
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ui.screen.panchanga.PanchangaScreenRedesigned

/**
 * Panchanga Screen - Displays Hindu calendar analysis
 *
 * This screen now delegates to the redesigned PanchangaScreenRedesigned
 * which provides a modern, clean UI with:
 * - Summary card with all five limbs
 * - Tab navigation (Today, Birth Day, Elements)
 * - Visual representations of each element
 * - Auspicious/inauspicious timing
 * - Detailed element breakdowns
 * - Smooth animations throughout
 */
@Composable
fun PanchangaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    PanchangaScreenRedesigned(
        chart = chart,
        onBack = onBack
    )
}
