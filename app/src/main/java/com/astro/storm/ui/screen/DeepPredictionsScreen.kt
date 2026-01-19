package com.astro.storm.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.deepanalysis.predictions.*
import com.astro.storm.ui.components.deepanalysis.*
import com.astro.storm.ui.viewmodel.DeepPredictionsUiState
import com.astro.storm.ui.viewmodel.DeepPredictionsViewModel
import java.time.format.DateTimeFormatter

/**
 * Deep Predictions Screen
 * 
 * Comprehensive predictions display with Dasha, Transit,
 * yearly/monthly predictions, and remedial measures.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepPredictionsScreen(
    chart: VedicChart,
    onBack: () -> Unit,
    viewModel: DeepPredictionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    
    LaunchedEffect(chart) {
        viewModel.calculatePredictions(chart)
    }
    
    val tabs = listOf(
        "Dasha" to Icons.Default.Timeline,
        "Transit" to Icons.Default.Public,
        "Yearly" to Icons.Default.CalendarMonth,
        "Remedies" to Icons.Default.Healing
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Deep Predictions", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tab row
            SectionTabRow(
                sections = tabs,
                selectedIndex = selectedTab,
                onSelect = viewModel::selectTab,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            when (val state = uiState) {
                is DeepPredictionsUiState.Initial,
                is DeepPredictionsUiState.Loading -> {
                    DeepAnalysisLoading()
                }
                is DeepPredictionsUiState.Error -> {
                    DeepAnalysisError(
                        message = state.message,
                        onRetry = { viewModel.calculatePredictions(chart) }
                    )
                }
                is DeepPredictionsUiState.Success -> {
                    when (selectedTab) {
                        0 -> DashaTab(state.predictions.dashaAnalysis)
                        1 -> TransitTab(state.predictions.transitAnalysis)
                        2 -> YearlyTab(state.predictions)
                        3 -> RemediesTab(state.predictions.remedialMeasures)
                    }
                }
            }
        }
    }
}

@Composable
private fun DashaTab(dasha: DashaDeepAnalysis) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Current Mahadasha
        dasha.currentMahadasha?.let { mahadasha ->
            item {
                DeepSectionHeader(
                    title = "Current Mahadasha",
                    icon = Icons.Default.Star
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${mahadasha.planet.displayName} Mahadasha",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Text(
                            text = "${mahadasha.startDate.format(dateFormatter)} - ${mahadasha.endDate.format(dateFormatter)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        LocalizedParagraphText(paragraph = mahadasha.overallTheme)
                        
                        if (mahadasha.strengths.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Strengths", fontWeight = FontWeight.Medium)
                            TraitsList(traits = mahadasha.strengths)
                        }
                        
                        if (mahadasha.challenges.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Challenges", fontWeight = FontWeight.Medium)
                            TraitsList(traits = mahadasha.challenges)
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Advice", fontWeight = FontWeight.Medium)
                        LocalizedParagraphText(paragraph = mahadasha.advice)
                    }
                }
            }
        }
        
        // Current Antardasha
        dasha.currentAntardasha?.let { antardasha ->
            item {
                DeepSectionHeader(
                    title = "Current Antardasha",
                    icon = Icons.Default.Timeline
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${antardasha.planet.displayName} Antardasha",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = "${antardasha.startDate.format(dateFormatter)} - ${antardasha.endDate.format(dateFormatter)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        LocalizedParagraphText(paragraph = antardasha.refinedTheme)
                        
                        if (antardasha.activatedYogas.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Activated Yogas", fontWeight = FontWeight.Medium)
                            antardasha.activatedYogas.forEach { yoga ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(yoga.yogaName)
                                    StrengthBadge(strength = yoga.activationLevel)
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Upcoming Dashas
        if (dasha.upcomingDashas.isNotEmpty()) {
            item {
                DeepSectionHeader(
                    title = "Upcoming Periods",
                    icon = Icons.Default.Schedule
                )
            }
            
            items(dasha.upcomingDashas) { upcoming ->
                TimelinePeriodCard(
                    title = "${upcoming.planet.displayName} Mahadasha",
                    dateRange = "${upcoming.startDate.format(dateFormatter)} - ${upcoming.endDate.format(dateFormatter)}",
                    description = upcoming.briefPreview,
                    strength = com.astro.storm.ephemeris.deepanalysis.StrengthLevel.MODERATE
                )
            }
        }
    }
}

@Composable
private fun TransitTab(transit: TransitDeepAnalysis) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Sade Sati
        if (transit.saturnSadeSati.isActive) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Sade Sati Active",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                            StrengthBadge(
                                strength = com.astro.storm.ephemeris.deepanalysis.StrengthLevel.AFFLICTED
                            )
                        }
                        
                        Text(
                            text = "Phase: ${transit.saturnSadeSati.phase.name}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        LocalizedParagraphText(paragraph = transit.saturnSadeSati.effects)
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Remedies", fontWeight = FontWeight.Medium)
                        LocalizedParagraphText(paragraph = transit.saturnSadeSati.remedies)
                    }
                }
            }
        }
        
        // Major Transits
        item {
            DeepSectionHeader(
                title = "Major Transits",
                icon = Icons.Default.Public
            )
        }
        
        items(transit.majorTransits) { majorTransit ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${majorTransit.planet.displayName} in ${majorTransit.currentSign.displayName}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        StrengthBadge(strength = majorTransit.intensity)
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    LocalizedParagraphText(paragraph = majorTransit.effectOnNative)
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    LocalizedParagraphText(
                        paragraph = majorTransit.duration,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        
        // Jupiter Transit
        item {
            DeepSectionHeader(
                title = "Jupiter Transit",
                icon = Icons.Default.Whatshot
            )
        }
        
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Jupiter in ${transit.jupiterTransit.currentTransitSign.displayName} (House ${transit.jupiterTransit.transitHouse})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    LocalizedParagraphText(paragraph = transit.jupiterTransit.effects)
                    
                    if (transit.jupiterTransit.favorableForAreas.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Favorable for: ", fontWeight = FontWeight.Medium)
                        Text(
                            text = transit.jupiterTransit.favorableForAreas.joinToString(", ") { it.name },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
        
        // Nodal Transit
        item {
            DeepSectionHeader(
                title = "Rahu-Ketu Transit",
                icon = Icons.Default.Loop
            )
        }
        
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Rahu in House ${transit.rahuKetuTransit.rahuTransitHouse} | Ketu in House ${transit.rahuKetuTransit.ketuTransitHouse}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    LocalizedParagraphText(paragraph = transit.rahuKetuTransit.nodalAxisEffects)
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    LocalizedParagraphText(
                        paragraph = transit.rahuKetuTransit.duration,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun YearlyTab(predictions: DeepPredictions) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Yearly Overview
        val yearly = predictions.yearlyPrediction
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Year ${yearly.year}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        StrengthBadge(strength = yearly.overallRating)
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    LocalizedParagraphText(paragraph = yearly.overallTheme)
                }
            }
        }
        
        // Life Area Outlooks
        item {
            DeepSectionHeader(
                title = "Life Area Outlook",
                icon = Icons.Default.Category
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ScoreIndicator(
                    score = when (yearly.careerOutlook.rating) {
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.EXCELLENT -> 90.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.STRONG -> 75.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.MODERATE -> 55.0
                        else -> 35.0
                    },
                    label = "Career"
                )
                ScoreIndicator(
                    score = when (yearly.relationshipOutlook.rating) {
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.EXCELLENT -> 90.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.STRONG -> 75.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.MODERATE -> 55.0
                        else -> 35.0
                    },
                    label = "Relationship"
                )
                ScoreIndicator(
                    score = when (yearly.healthOutlook.rating) {
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.EXCELLENT -> 90.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.STRONG -> 75.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.MODERATE -> 55.0
                        else -> 35.0
                    },
                    label = "Health"
                )
                ScoreIndicator(
                    score = when (yearly.wealthOutlook.rating) {
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.EXCELLENT -> 90.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.STRONG -> 75.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.MODERATE -> 55.0
                        else -> 35.0
                    },
                    label = "Wealth"
                )
            }
        }
        
        // Key Months
        if (yearly.keyMonths.isNotEmpty()) {
            item {
                DeepSectionHeader(
                    title = "Key Months",
                    icon = Icons.Default.Event
                )
            }
            
            items(yearly.keyMonths) { keyMonth ->
                TimelinePeriodCard(
                    title = "Month ${keyMonth.month}",
                    dateRange = "",
                    description = keyMonth.significance,
                    strength = keyMonth.rating
                )
            }
        }
        
        // Yearly Advice
        item {
            DeepSectionHeader(
                title = "Yearly Guidance",
                icon = Icons.Default.Lightbulb
            )
        }
        
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    LocalizedParagraphText(paragraph = yearly.yearlyAdvice)
                }
            }
        }
        
        // Overall Summary
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Overall Prediction Summary",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LocalizedParagraphText(paragraph = predictions.overallPredictionSummary)
                }
            }
        }
    }
}

@Composable
private fun RemediesTab(remedies: RemedialProfile) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Gemstones
        if (remedies.gemstoneRemedies.isNotEmpty()) {
            item {
                DeepSectionHeader(
                    title = "Gemstone Remedies",
                    icon = Icons.Default.Diamond
                )
            }
            
            items(remedies.gemstoneRemedies) { gemstone ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${gemstone.planet.displayName}: ${gemstone.primaryGemstone}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = "Alternative: ${gemstone.alternativeGemstone}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        LocalizedParagraphText(paragraph = gemstone.wearingGuidelines)
                        
                        if (gemstone.cautions.en.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "⚠️ ",
                                style = MaterialTheme.typography.bodySmall
                            )
                            LocalizedParagraphText(
                                paragraph = gemstone.cautions,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
        
        // Mantras
        if (remedies.mantraRemedies.isNotEmpty()) {
            item {
                DeepSectionHeader(
                    title = "Mantra Remedies",
                    icon = Icons.Default.RecordVoiceOver
                )
            }
            
            items(remedies.mantraRemedies) { mantra ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${mantra.planet.displayName} Mantra",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = mantra.beejaMantra,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "Chant count: ${mantra.chantCount}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        
                        LocalizedParagraphText(
                            paragraph = mantra.bestTime,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
        
        // Charitable
        if (remedies.charitableRemedies.isNotEmpty()) {
            item {
                DeepSectionHeader(
                    title = "Charitable Remedies",
                    icon = Icons.Default.VolunteerActivism
                )
            }
            
            items(remedies.charitableRemedies) { charity ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "For ${charity.planet.displayName}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = "Donate: ${charity.donationItems}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Text(
                            text = "Best Day: ${charity.bestDay}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        
                        LocalizedParagraphText(paragraph = charity.guidelines)
                    }
                }
            }
        }
        
        // Fasting
        if (remedies.fastingRemedies.isNotEmpty()) {
            item {
                DeepSectionHeader(
                    title = "Fasting Remedies",
                    icon = Icons.Default.NoMeals
                )
            }
            
            items(remedies.fastingRemedies) { fast ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "For ${fast.planet.displayName}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = "Day: ${fast.fastingDay}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        LocalizedParagraphText(paragraph = fast.fastingType)
                        LocalizedParagraphText(paragraph = fast.guidelines)
                    }
                }
            }
        }
        
        // Yogic
        if (remedies.yogicRemedies.isNotEmpty()) {
            item {
                DeepSectionHeader(
                    title = "Yogic Practices",
                    icon = Icons.Default.SelfImprovement
                )
            }
            
            items(remedies.yogicRemedies) { yoga ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = yoga.practiceName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = "Strengthens: ${yoga.targetPlanet.displayName}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        LocalizedParagraphText(paragraph = yoga.guidelines)
                        LocalizedParagraphText(paragraph = yoga.benefits)
                    }
                }
            }
        }
        
        // Overall Advice
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Overall Remedial Guidance",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LocalizedParagraphText(paragraph = remedies.overallRemedialAdvice)
                }
            }
        }
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
