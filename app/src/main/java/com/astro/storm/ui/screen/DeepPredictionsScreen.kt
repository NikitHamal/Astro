package com.astro.storm.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyUIExtra
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.StringKeyNative
import com.astro.storm.core.common.StringKeyDeepPrediction
import com.astro.storm.core.common.StringKeyDeepRelationship
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.localization.localizedName
import com.astro.storm.data.localization.localized
import com.astro.storm.data.localization.formatLocalized
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.deepanalysis.predictions.*
import com.astro.storm.ui.components.deepanalysis.*
import com.astro.storm.ui.viewmodel.DeepPredictionsUiState
import com.astro.storm.ui.viewmodel.DeepPredictionsViewModel
import java.time.format.DateTimeFormatter

import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.TabItem
import com.astro.storm.ui.theme.AppTheme

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
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(StringKeyDeepPrediction.SECTION_PREDICTIONS),
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(StringKey.BTN_BACK),
                            tint = AppTheme.TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.ScreenBackground
                )
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { padding ->
        DeepPredictionsBody(
            chart = chart,
            viewModel = viewModel,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun DeepPredictionsBody(
    chart: VedicChart,
    viewModel: DeepPredictionsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    
    LaunchedEffect(chart) {
        viewModel.calculatePredictions(chart)
    }
    
    val tabs = listOf(
        stringResource(StringKeyAnalysis.ANALYSIS_TAB_DASHAS),
        stringResource(StringKeyAnalysis.ANALYSIS_TAB_TRANSITS),
        stringResource(StringKeyDeepPrediction.SECTION_CRITICAL),
        stringResource(StringKeyDeepPrediction.SECTION_YEARLY),
        stringResource(StringKeyDeepPrediction.SECTION_REMEDIES)
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Tab row
        val tabItems = tabs.map { TabItem(title = it, accentColor = AppTheme.AccentPrimary) }
        
        ModernPillTabRow(
            tabs = tabItems,
            selectedIndex = selectedTab,
            onTabSelected = viewModel::selectTab,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.3f))
        
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
                    2 -> PredictionsOverviewTab(state.predictions)
                    3 -> YearlyTab(state.predictions)
                    4 -> RemediesTab(state.predictions.remedialMeasures)
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
                    title = stringResource(StringKeyDeepPrediction.MAHADASHA_TITLE),
                    icon = Icons.Default.Star
                )
            }
            
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = AppTheme.AccentPrimary.copy(alpha = 0.08f),
                            spotColor = AppTheme.AccentPrimary.copy(alpha = 0.08f)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    color = AppTheme.CardBackground
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${mahadasha.planet.localizedName()} ${stringResource(StringKeyAnalysis.ANALYSIS_TAB_DASHAS)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.TextPrimary
                            )
                        }
                        
                        Text(
                            text = mahadasha.startDate.formatLocalized(com.astro.storm.data.localization.DateFormat.MONTH_YEAR) + stringResource(StringKeyUIExtra.DASH_SPACE) + mahadasha.endDate.formatLocalized(com.astro.storm.data.localization.DateFormat.MONTH_YEAR),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        LocalizedParagraphText(paragraph = mahadasha.overallTheme)
                        
                        if (mahadasha.strengths.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(stringResource(StringKeyNative.LABEL_STRENGTHS), fontWeight = FontWeight.Medium)
                            TraitsList(traits = mahadasha.strengths)
                        }
                        
                        if (mahadasha.challenges.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(stringResource(StringKeyNative.LABEL_CHALLENGES), fontWeight = FontWeight.Medium)
                            TraitsList(traits = mahadasha.challenges)
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(stringResource(StringKeyDeepPrediction.MAHADASHA_ADVICE), fontWeight = FontWeight.Medium)
                        LocalizedParagraphText(paragraph = mahadasha.advice)
                    }
                }
            }
        }
        
        // Current Antardasha
        dasha.currentAntardasha?.let { antardasha ->
            item {
                DeepSectionHeader(
                    title = stringResource(StringKeyDeepPrediction.ANTARDASHA_TITLE),
                    icon = Icons.Default.Timeline
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.CardBackground
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${antardasha.planet.localizedName()} ${stringResource(StringKeyDeepPrediction.ANTARDASHA_TITLE)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        
                        Text(
                            text = antardasha.startDate.formatLocalized(com.astro.storm.data.localization.DateFormat.MONTH_YEAR) + stringResource(StringKeyUIExtra.DASH_SPACE) + antardasha.endDate.formatLocalized(com.astro.storm.data.localization.DateFormat.MONTH_YEAR),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        LocalizedParagraphText(paragraph = antardasha.refinedTheme)
                        
                        if (antardasha.activatedYogas.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = stringResource(StringKeyDeepPrediction.ACTIVATED_YOGAS),
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.TextPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                for (yoga in antardasha.activatedYogas) {
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        color = AppTheme.CardBackgroundElevated,
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = yoga.yogaName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = AppTheme.TextPrimary,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            StrengthBadge(strength = yoga.activationLevel)
                                        }
                                    }
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
                    title = stringResource(StringKeyDeepPrediction.UPCOMING_DASHAS),
                    icon = Icons.Default.Schedule
                )
            }
            
            items(dasha.upcomingDashas) { upcoming ->
                TimelinePeriodCard(
                    title = upcoming.planet.localizedName() + " " + stringResource(StringKeyAnalysis.ANALYSIS_TAB_DASHAS),
                    dateRange = upcoming.startDate.formatLocalized(com.astro.storm.data.localization.DateFormat.MONTH_YEAR) + stringResource(StringKeyUIExtra.DASH_SPACE) + upcoming.endDate.formatLocalized(com.astro.storm.data.localization.DateFormat.MONTH_YEAR),
                    description = upcoming.briefPreview,
                    strength = com.astro.storm.ephemeris.deepanalysis.StrengthLevel.MODERATE
                )
            }
        }
    }
}

@Composable
private fun PredictionsOverviewTab(predictions: DeepPredictions) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Critical Periods
        if (predictions.criticalPeriods.isNotEmpty()) {
            item {
                DeepSectionHeader(
                    title = stringResource(StringKeyDeepPrediction.SECTION_CRITICAL),
                    icon = Icons.Default.Warning
                )
            }
            
            items(predictions.criticalPeriods) { period ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.ErrorColor.copy(alpha = 0.05f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LocalizedParagraphText(
                                paragraph = period.periodName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.ErrorColor
                                )
                            )
                            StrengthBadge(strength = period.intensity)
                        }
                        
                        Text(
                            text = period.startDate.formatLocalized(com.astro.storm.data.localization.DateFormat.SHORT) + stringResource(StringKeyUIExtra.DASH_SPACE) + period.endDate.formatLocalized(com.astro.storm.data.localization.DateFormat.SHORT),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        LocalizedParagraphText(paragraph = period.advice)
                    }
                }
            }
        }
        
        // Opportunity Windows
        if (predictions.opportunityWindows.isNotEmpty()) {
            item {
                DeepSectionHeader(
                    title = stringResource(StringKeyDeepPrediction.SECTION_OPPORTUNITIES),
                    icon = Icons.Default.Lightbulb
                )
            }
            
            items(predictions.opportunityWindows) { window ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.AccentPrimary.copy(alpha = 0.05f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LocalizedParagraphText(
                                paragraph = window.windowName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.AccentPrimary
                                )
                            )
                            StrengthBadge(strength = window.intensity)
                        }
                        
                        LocalizedParagraphText(
                            paragraph = window.opportunityType,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        
                        Text(
                            text = window.startDate.formatLocalized(com.astro.storm.data.localization.DateFormat.SHORT) + stringResource(StringKeyUIExtra.DASH_SPACE) + window.endDate.formatLocalized(com.astro.storm.data.localization.DateFormat.SHORT),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        LocalizedParagraphText(paragraph = window.advice)
                    }
                }
            }
        }
    }
}

@Composable
private fun TransitTab(transit: TransitDeepAnalysis) {
    val language = com.astro.storm.data.localization.LocalLanguage.current
    
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
                        containerColor = AppTheme.ErrorColor.copy(alpha = 0.1f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(StringKeyDeepPrediction.SADE_SATI_ACTIVE),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.ErrorColor
                            )
                            StrengthBadge(
                                strength = com.astro.storm.ephemeris.deepanalysis.StrengthLevel.AFFLICTED
                            )
                        }
                        
                        Text(
                            text = stringResource(StringKeyDeepPrediction.SADE_SATI_PHASE) + stringResource(StringKeyUIExtra.COLON_SPACE) +
                                when(transit.saturnSadeSati.phase) {
                                    SadeSatiPhase.RISING -> stringResource(StringKeyDeepPrediction.SADE_SATI_RISING)
                                    SadeSatiPhase.PEAK -> stringResource(StringKeyDeepPrediction.SADE_SATI_PEAK)
                                    SadeSatiPhase.SETTING -> stringResource(StringKeyDeepPrediction.SADE_SATI_SETTING)
                                    else -> transit.saturnSadeSati.phase.name
                                },
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        LocalizedParagraphText(paragraph = transit.saturnSadeSati.effects)
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(StringKeyDeepPrediction.SECTION_REMEDIES), fontWeight = FontWeight.Medium)
                        LocalizedParagraphText(paragraph = transit.saturnSadeSati.remedies)
                    }
                }
            }
        }
        
        // Major Transits
        item {
            DeepSectionHeader(
                title = stringResource(StringKeyDeepPrediction.MAJOR_TRANSITS),
                icon = Icons.Default.Public
            )
        }
        
        items(transit.majorTransits) { majorTransit ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.CardBackground
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${majorTransit.planet.localizedName()} ${stringResource(StringKeyNative.LABEL_IN_HOUSE).lowercase()} ${majorTransit.currentSign.localizedName()}",
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
                title = stringResource(StringKeyDeepPrediction.JUPITER_TRANSIT),
                icon = Icons.Default.Whatshot
            )
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.CardBackground
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${stringResource(StringKey.PLANET_JUPITER)} ${stringResource(StringKeyNative.LABEL_IN_HOUSE).lowercase()} ${transit.jupiterTransit.currentTransitSign.localizedName()} (${stringResource(StringKeyAnalysis.HOUSE)} ${transit.jupiterTransit.transitHouse.localized()})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    LocalizedParagraphText(paragraph = transit.jupiterTransit.effects)
                    
                    if (transit.jupiterTransit.favorableForAreas.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(StringKeyDeepPrediction.JUPITER_FAVORABLE_AREAS) + stringResource(StringKeyUIExtra.COLON_SPACE), fontWeight = FontWeight.Medium)
                        Text(
                            text = transit.jupiterTransit.favorableForAreas.joinToString(stringResource(StringKeyUIExtra.COMMA_SPACE)) {
                                val key = when(it) {
                                    LifeArea.GENERAL -> StringKeyDeepPrediction.AREA_GENERAL
                                    LifeArea.CAREER -> StringKeyDeepPrediction.AREA_CAREER
                                    LifeArea.RELATIONSHIP -> StringKeyDeepPrediction.AREA_RELATIONSHIP
                                    LifeArea.HEALTH -> StringKeyDeepPrediction.AREA_HEALTH
                                    LifeArea.WEALTH -> StringKeyDeepPrediction.AREA_WEALTH
                                    LifeArea.EDUCATION -> StringKeyDeepPrediction.AREA_EDUCATION
                                    LifeArea.SPIRITUAL -> StringKeyDeepPrediction.AREA_SPIRITUAL
                                }
                                StringResources.get(key, language)
                            },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
        
        // Nodal Transit
        item {
            DeepSectionHeader(
                title = stringResource(StringKeyDeepPrediction.RAHU_KETU_TRANSIT),
                icon = Icons.Default.Loop
            )
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.CardBackground
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(StringKey.PLANET_RAHU) + " " + stringResource(StringKeyNative.LABEL_IN_HOUSE) + " " + transit.rahuKetuTransit.rahuTransitHouse.localized() + stringResource(StringKeyUIExtra.PIPE_SPACE) + stringResource(StringKey.PLANET_KETU) + " " + stringResource(StringKeyNative.LABEL_IN_HOUSE) + " " + transit.rahuKetuTransit.ketuTransitHouse.localized(),
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
                    containerColor = AppTheme.CardBackground
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${stringResource(StringKeyAnalysis.CHART_DATE)} ${yearly.year.localized()}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
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
                title = stringResource(StringKeyDeepPrediction.SECTION_LIFE_AREAS),
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
                    label = stringResource(StringKeyNative.SECTION_CAREER)
                )
                ScoreIndicator(
                    score = when (yearly.relationshipOutlook.rating) {
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.EXCELLENT -> 90.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.STRONG -> 75.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.MODERATE -> 55.0
                        else -> 35.0
                    },
                    label = stringResource(StringKeyDeepRelationship.SECTION_RELATIONSHIP)
                )
                ScoreIndicator(
                    score = when (yearly.healthOutlook.rating) {
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.EXCELLENT -> 90.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.STRONG -> 75.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.MODERATE -> 55.0
                        else -> 35.0
                    },
                    label = stringResource(StringKeyNative.SECTION_HEALTH)
                )
                ScoreIndicator(
                    score = when (yearly.wealthOutlook.rating) {
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.EXCELLENT -> 90.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.STRONG -> 75.0
                        com.astro.storm.ephemeris.deepanalysis.StrengthLevel.MODERATE -> 55.0
                        else -> 35.0
                    },
                    label = stringResource(StringKeyNative.SECTION_WEALTH)
                )
            }
        }
        
        // Key Months
        if (yearly.keyMonths.isNotEmpty()) {
            item {
                DeepSectionHeader(
                    title = stringResource(StringKeyDeepPrediction.KEY_MONTHS),
                    icon = Icons.Default.Event
                )
            }
            
            items(yearly.keyMonths) { keyMonth ->
                TimelinePeriodCard(
                    title = "${stringResource(StringKeyAnalysis.PANCHANGA_NUMBER)} ${keyMonth.month.localized()}",
                    dateRange = "",
                    description = keyMonth.significance,
                    strength = keyMonth.rating
                )
            }
        }
        
        // Yearly Advice
        item {
            DeepSectionHeader(
                title = stringResource(StringKeyDeepPrediction.YEARLY_ADVICE),
                icon = Icons.Default.Lightbulb
            )
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.CardBackground
                )
            ) {
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
                    containerColor = AppTheme.CardBackground
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(StringKeyDeepPrediction.PREDICTION_SUMMARY),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
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
                    title = stringResource(StringKeyDeepPrediction.GEMSTONE_REMEDIES),
                    icon = Icons.Default.Diamond
                )
            }
            
            items(remedies.gemstoneRemedies) { gemstone ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.CardBackground
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        LocalizedParagraphText(
                            paragraph = gemstone.primaryGemstone,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        
                        Row {
                            Text(
                            text = stringResource(StringKeyDeepPrediction.ALTERNATIVE_GEMSTONE) + stringResource(StringKeyUIExtra.COLON_SPACE),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            LocalizedParagraphText(
                                paragraph = gemstone.alternativeGemstone,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        
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
                    title = stringResource(StringKeyDeepPrediction.MANTRA_REMEDIES),
                    icon = Icons.Default.RecordVoiceOver
                )
            }
            
            items(remedies.mantraRemedies) { mantra ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.CardBackground
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = mantra.planet.localizedName() + " " + stringResource(StringKeyDeepPrediction.MANTRA_REMEDIES),
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
                            text = stringResource(StringKeyDeepPrediction.CHANT_COUNT) + stringResource(StringKeyUIExtra.COLON_SPACE) + mantra.chantCount.localized(),
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
                    title = stringResource(StringKeyDeepPrediction.CHARITABLE_REMEDIES),
                    icon = Icons.Default.VolunteerActivism
                )
            }
            
            items(remedies.charitableRemedies) { charity ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.CardBackground
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${stringResource(StringKeyDeepPrediction.TARGET_PLANET)} ${charity.planet.localizedName()}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Row {
                            Text(
                                text = stringResource(StringKeyDeepPrediction.DONATION_ITEMS) + stringResource(StringKeyUIExtra.COLON_SPACE),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            LocalizedParagraphText(
                                paragraph = charity.donationItems,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
                        Row {
                            Text(
                                text = stringResource(StringKeyDeepPrediction.BEST_DAY) + stringResource(StringKeyUIExtra.COLON_SPACE),
                                style = MaterialTheme.typography.bodySmall
                            )
                            LocalizedParagraphText(
                                paragraph = charity.bestDay,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        
                        LocalizedParagraphText(paragraph = charity.guidelines)
                    }
                }
            }
        }
        
        // Fasting
        if (remedies.fastingRemedies.isNotEmpty()) {
            item {
                DeepSectionHeader(
                    title = stringResource(StringKeyDeepPrediction.FASTING_REMEDIES),
                    icon = Icons.Default.NoMeals
                )
            }
            
            items(remedies.fastingRemedies) { fast ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.CardBackground
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${stringResource(StringKeyDeepPrediction.TARGET_PLANET)} ${fast.planet.localizedName()}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Row {
                            Text(
                                text = stringResource(StringKeyDeepPrediction.FASTING_DAY) + stringResource(StringKeyUIExtra.COLON_SPACE),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            LocalizedParagraphText(
                                paragraph = fast.fastingDay,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
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
                    title = stringResource(StringKeyDeepPrediction.YOGIC_REMEDIES),
                    icon = Icons.Default.SelfImprovement
                )
            }
            
            items(remedies.yogicRemedies) { yoga ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.CardBackground
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        LocalizedParagraphText(
                            paragraph = yoga.practiceName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        
                        Text(
                            text = stringResource(StringKeyDeepPrediction.TARGET_PLANET) + stringResource(StringKeyUIExtra.COLON_SPACE) + yoga.targetPlanet.localizedName(),
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
                    containerColor = AppTheme.AccentPrimary.copy(alpha = 0.1f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(StringKeyDeepPrediction.OVERALL_ADVICE),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LocalizedParagraphText(paragraph = remedies.overallRemedialAdvice)
                }
            }
        }
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
