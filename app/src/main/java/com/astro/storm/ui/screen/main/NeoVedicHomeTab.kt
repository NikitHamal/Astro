package com.astro.storm.ui.screen.main

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AllInclusive
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ui.theme.AppTheme

@Composable
fun NeoVedicHomeTab(
    chart: VedicChart?,
    onFeatureClick: (InsightFeature) -> Unit,
    onAddNewChart: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(bottom = 100.dp)
) {
    if (chart == null) {
        EmptyHomeState(onCreateProfile = onAddNewChart, modifier = modifier)
        return
    }

    val timeline = remember(chart) {
        runCatching { DashaCalculator.calculateDashaTimeline(chart) }.getOrNull()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground)
            .padding(horizontal = 16.dp),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            NeoDashaCard(
                dashaName = timeline?.currentMahadasha?.planet?.name ?: "Saturn",
                dateRange = timeline?.currentMahadasha?.let { "${it.startDate.year} — ${it.endDate.year}" } ?: "Current cycle",
                onClick = { onFeatureClick(InsightFeature.DASHAS) }
            )
        }
        item {
            Text(
                text = "QUICK ACTIONS",
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 1.8.sp,
                color = AppTheme.TextMuted,
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            )
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NeoActionCard("Birth Chart", Icons.Outlined.GridView, Modifier.weight(1f)) { onFeatureClick(InsightFeature.FULL_CHART) }
                    NeoActionCard("Yogas", Icons.Outlined.AllInclusive, Modifier.weight(1f)) { onFeatureClick(InsightFeature.YOGAS) }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NeoActionCard("Predictions", Icons.Outlined.Visibility, Modifier.weight(1f)) { onFeatureClick(InsightFeature.PREDICTIONS) }
                    NeoActionCard("Match", Icons.Outlined.FavoriteBorder, Modifier.weight(1f)) { onFeatureClick(InsightFeature.MATCHMAKING) }
                }
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, AppTheme.BorderColor, RoundedCornerShape(2.dp))
                    .background(AppTheme.CardBackgroundElevated)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("TITHI", style = MaterialTheme.typography.labelSmall, letterSpacing = 1.6.sp, color = AppTheme.TextMuted)
                    Text("Ekadashi", style = MaterialTheme.typography.titleMedium, color = AppTheme.TextPrimary)
                }
                Box(modifier = Modifier.size(width = 1.dp, height = 30.dp).background(AppTheme.AccentGold.copy(alpha = 0.5f)))
                Column(horizontalAlignment = Alignment.End) {
                    Text("NAKSHATRA", style = MaterialTheme.typography.labelSmall, letterSpacing = 1.6.sp, color = AppTheme.TextMuted)
                    Text((chart.planetPositions.find { it.planet.name == "MOON" }?.nakshatra?.name ?: "Rohini"), style = MaterialTheme.typography.titleMedium, color = AppTheme.TextPrimary)
                }
            }
        }
    }
}

@Composable
private fun NeoDashaCard(dashaName: String, dateRange: String, onClick: () -> Unit) {
    val infinite = rememberInfiniteTransition(label = "dasha_orbit")
    val rotation by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(60000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "ring_rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(1.dp, AppTheme.BorderColor, RoundedCornerShape(2.dp))
            .background(AppTheme.CardBackgroundElevated)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(236.dp)
                .border(1.dp, AppTheme.AccentGold.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(208.dp)
                    .rotate(rotation)
                    .border(1.dp, AppTheme.TextMuted.copy(alpha = 0.35f), CircleShape)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("MAHA DASHA", style = MaterialTheme.typography.labelSmall, letterSpacing = 1.8.sp, color = AppTheme.AccentGold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(dashaName.uppercase(), style = MaterialTheme.typography.headlineMedium, color = AppTheme.TextPrimary, fontWeight = FontWeight.Bold)
                Text(dateRange, style = MaterialTheme.typography.bodySmall, color = AppTheme.TextMuted)
            }
        }
    }
}

@Composable
private fun NeoActionCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .border(1.dp, AppTheme.BorderColor, RoundedCornerShape(2.dp))
            .background(AppTheme.CardBackgroundElevated)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Icon(icon, contentDescription = title, tint = AppTheme.TextPrimary, modifier = Modifier.size(20.dp))
        }
        Column {
            Box(modifier = Modifier.size(width = 12.dp, height = 1.dp).background(AppTheme.AccentGold))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title.uppercase(), style = MaterialTheme.typography.labelSmall, letterSpacing = 1.2.sp, color = AppTheme.TextPrimary)
        }
    }
}
