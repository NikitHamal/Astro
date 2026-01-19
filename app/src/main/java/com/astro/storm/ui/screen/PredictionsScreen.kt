package com.astro.storm.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringResources
import com.astro.storm.data.localization.currentLanguage
import com.astro.storm.core.model.LifeArea
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.prediction.*
import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.TabItem
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter

enum class PredictionsTab(val stringKey: StringKey) {
    OVERVIEW(StringKey.PREDICTIONS_TAB_OVERVIEW),
    LIFE_AREAS(StringKey.PREDICTIONS_TAB_LIFE_AREAS),
    TIMING(StringKey.PREDICTIONS_TAB_TIMING),
    REMEDIES(StringKey.PREDICTIONS_TAB_REMEDIES);

    fun getLocalizedTitle(language: Language): String = StringResources.get(stringKey, language)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictionsScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val language = currentLanguage()

    var selectedTab by remember { mutableStateOf(PredictionsTab.OVERVIEW) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var predictionData by remember { mutableStateOf<PredictionData?>(null) }

    LaunchedEffect(chart) {
        if (chart != null) {
            isLoading = true
            errorMessage = null
            try {
                predictionData = withContext(Dispatchers.Default) {
                    PredictionEngine.calculatePredictions(chart, language)
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: StringResources.get(StringKey.PREDICTIONS_CALC_FAILED, language)
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        StringResources.get(StringKey.FEATURE_PREDICTIONS, language),
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = StringResources.get(StringKey.BTN_BACK, language),
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
    ) { paddingValues ->
        when {
            chart == null -> {
                EmptyPredictionsState(modifier = Modifier.padding(paddingValues))
            }
            errorMessage != null -> {
                ErrorState(
                    message = errorMessage!!,
                    onRetry = {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            try {
                                predictionData = withContext(Dispatchers.Default) {
                                    PredictionEngine.calculatePredictions(chart, language)
                                }
                            } catch (e: Exception) {
                                errorMessage = e.message ?: StringResources.get(StringKey.PREDICTIONS_CALC_FAILED, language)
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            isLoading && predictionData == null -> {
                LoadingState(modifier = Modifier.padding(paddingValues))
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Tab Row
                    val tabItems = PredictionsTab.entries.map { tab ->
                        TabItem(title = tab.getLocalizedTitle(language), accentColor = AppTheme.AccentPrimary)
                    }
                    
                    ModernPillTabRow(
                        tabs = tabItems,
                        selectedIndex = selectedTab.ordinal,
                        onTabSelected = { selectedTab = PredictionsTab.entries[it] },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    
                    HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.5f))

                    // Tab Content
                    predictionData?.let { data ->
                        AnimatedContent(
                            targetState = selectedTab,
                            transitionSpec = {
                                val direction = if (targetState.ordinal > initialState.ordinal) {
                                    AnimatedContentTransitionScope.SlideDirection.Left
                                } else {
                                    AnimatedContentTransitionScope.SlideDirection.Right
                                }
                                slideIntoContainer(direction, tween(300)) togetherWith
                                        slideOutOfContainer(direction, tween(300))
                            },
                            label = "TabContent"
                        ) { tab ->
                            when (tab) {
                                PredictionsTab.OVERVIEW -> OverviewTabContent(data)
                                PredictionsTab.LIFE_AREAS -> LifeAreasTabContent(data)
                                PredictionsTab.TIMING -> TimingTabContent(data)
                                PredictionsTab.REMEDIES -> RemediesTabContent(data)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Tab Content Composables
@Composable
private fun OverviewTabContent(data: PredictionData) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Life Path Overview
        item {
            LifePathCard(data.lifeOverview)
        }

        // Current Period Analysis
        item {
            CurrentPeriodCard(data.currentPeriod)
        }

        // Active Yogas Summary
        if (data.activeYogas.isNotEmpty()) {
            item {
                ActiveYogasSummaryCard(data.activeYogas.take(3))
            }
        }

        // Challenges & Opportunities Overview
        item {
            ChallengesOpportunitiesCard(data.challengesOpportunities)
        }

        // Quick Life Areas Summary
        item {
            QuickLifeAreasSummary(data.lifeAreas)
        }
    }
}

@Composable
private fun LifeAreasTabContent(data: PredictionData) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        items(data.lifeAreas, key = { it.area }) { area ->
            LifeAreaDetailCard(area)
        }
    }
}

@Composable
private fun TimingTabContent(data: PredictionData) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Favorable Periods
        if (data.timing.favorablePeriods.isNotEmpty()) {
            item {
                FavorablePeriodsCard(data.timing.favorablePeriods)
            }
        }

        // Unfavorable Periods
        if (data.timing.unfavorablePeriods.isNotEmpty()) {
            item {
                UnfavorablePeriodsCard(data.timing.unfavorablePeriods)
            }
        }

        // Key Dates
        if (data.timing.keyDates.isNotEmpty()) {
            item {
                KeyDatesCard(data.timing.keyDates)
            }
        }
    }
}

@Composable
private fun RemediesTabContent(data: PredictionData) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        RemedialSuggestionsCard(data.remedies, data.currentPeriod.dashaInfo)
    }
}

// Card Composables
@Composable
private fun LifePathCard(overview: LifeOverview) {
    val language = currentLanguage()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    Icons.Outlined.TrendingUp,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    StringResources.get(StringKey.PREDICTIONS_YOUR_LIFE_PATH, language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Text(
                overview.lifeTheme,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AppTheme.AccentPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                overview.overallPath,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                overview.coreNarrative,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = AppTheme.DividerColor)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                StringResources.get(StringKey.PREDICTIONS_KEY_STRENGTHS, language),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            overview.keyStrengths.forEach { strength ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        strength,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = AppTheme.LifeAreaSpiritual.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Outlined.Star,
                        contentDescription = null,
                        tint = AppTheme.LifeAreaSpiritual,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            StringResources.get(StringKey.PREDICTIONS_SPIRITUAL_PATH, language),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.LifeAreaSpiritual
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            overview.spiritualPath,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentPeriodCard(period: CurrentPeriodAnalysis) {
    val language = currentLanguage()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        StringResources.get(StringKey.PREDICTIONS_CURRENT_PERIOD, language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }

                // Energy Indicator
                Surface(
                    color = getEnergyColor(period.overallEnergy).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${period.overallEnergy}/10",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = getEnergyColor(period.overallEnergy)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                period.period,
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.TextMuted
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                period.dashaInfo,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = AppTheme.AccentPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                period.dashaEffect,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp
            )

            if (period.keyFocusAreas.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    StringResources.get(StringKey.PREDICTIONS_FOCUS_AREAS, language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                period.keyFocusAreas.forEach { focus ->
                    Row(verticalAlignment = Alignment.Top) {
                        Text("• ", color = AppTheme.TextMuted)
                        Text(
                            focus,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }

            if (period.transitHighlights.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = AppTheme.DividerColor)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    StringResources.get(StringKey.PREDICTIONS_ACTIVE_TRANSITS, language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(period.transitHighlights, key = { it.planet.name }) { transit ->
                        TransitHighlightChip(transit)
                    }
                }
            }
        }
    }
}

@Composable
private fun TransitHighlightChip(transit: TransitHighlight) {
    val language = currentLanguage()
    val planetColor = getPlanetColor(transit.planet)

    Surface(
        color = planetColor.copy(alpha = 0.15f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(140.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    transit.planet.symbol,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = planetColor
                )
                Icon(
                    if (transit.isPositive) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                    contentDescription = null,
                    tint = if (transit.isPositive) AppTheme.SuccessColor else AppTheme.WarningColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                transit.planet.getLocalizedName(language),
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                transit.description,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun ActiveYogasSummaryCard(yogas: List<ActiveYoga>) {
    val language = currentLanguage()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    StringResources.get(StringKey.PREDICTIONS_ACTIVE_YOGAS, language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            yogas.forEach { yoga ->
                YogaItem(yoga)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun YogaItem(yoga: ActiveYoga) {
    Surface(
        color = AppTheme.AccentGold.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    yoga.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(yoga.strength) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = AppTheme.AccentGold,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                yoga.description,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )

            if (yoga.planets.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    yoga.planets.forEach { planet ->
                        Text(
                            planet.symbol,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = getPlanetColor(planet)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChallengesOpportunitiesCard(data: ChallengesOpportunities) {
    val language = currentLanguage()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Opportunities
            if (data.opportunities.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Lightbulb,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        StringResources.get(StringKey.PREDICTIONS_OPPORTUNITIES, language),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.SuccessColor
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                data.opportunities.take(2).forEach { opportunity ->
                    OpportunityItem(opportunity)
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // Challenges
            if (data.currentChallenges.isNotEmpty()) {
                if (data.opportunities.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null,
                        tint = AppTheme.WarningColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        StringResources.get(StringKey.PREDICTIONS_CURRENT_CHALLENGES, language),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.WarningColor
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                data.currentChallenges.take(2).forEach { challenge ->
                    ChallengeItem(challenge)
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
private fun OpportunityItem(opportunity: Opportunity) {
    Surface(
        color = AppTheme.SuccessColor.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = AppTheme.SuccessColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    opportunity.area,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.SuccessColor
                )
                Text(
                    opportunity.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun ChallengeItem(challenge: Challenge) {
    Surface(
        color = AppTheme.WarningColor.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Filled.Warning,
                contentDescription = null,
                tint = AppTheme.WarningColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    challenge.area,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.WarningColor
                )
                Text(
                    challenge.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun QuickLifeAreasSummary(areas: List<LifeAreaPrediction>) {
    val language = currentLanguage()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                StringResources.get(StringKey.PREDICTIONS_LIFE_AREAS_GLANCE, language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            areas.forEach { area ->
                QuickLifeAreaRow(area)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun QuickLifeAreaRow(area: LifeAreaPrediction) {
    val language = currentLanguage()
    val areaColor = area.area.color

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            area.area.icon,
            contentDescription = null,
            tint = areaColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            area.area.getLocalizedName(language),
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            repeat(5) { index ->
                Icon(
                    if (index < area.rating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = null,
                    tint = if (index < area.rating) areaColor else AppTheme.TextSubtle,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun LifeAreaDetailCard(area: LifeAreaPrediction) {
    val language = currentLanguage()
    var expanded by remember { mutableStateOf(false) }
    val areaColor = area.area.color

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(areaColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        area.area.icon,
                        contentDescription = null,
                        tint = areaColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        area.area.getLocalizedName(language),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        repeat(5) { index ->
                            Icon(
                                if (index < area.rating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = if (index < area.rating) areaColor else AppTheme.TextSubtle,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                Icon(
                    if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    PredictionTimeframe(
                        StringResources.get(StringKey.PREDICTIONS_SHORT_TERM, language),
                        area.shortTerm,
                        areaColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PredictionTimeframe(
                        StringResources.get(StringKey.PREDICTIONS_MEDIUM_TERM, language),
                        area.mediumTerm,
                        areaColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PredictionTimeframe(
                        StringResources.get(StringKey.PREDICTIONS_LONG_TERM, language),
                        area.longTerm,
                        areaColor
                    )

                    if (area.keyFactors.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = AppTheme.DividerColor)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            StringResources.get(StringKeyPrediction.PRED_LABEL_KEY_FACTORS, language),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = areaColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        area.keyFactors.forEach { factor ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("• ", color = areaColor)
                                Text(
                                    factor,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }

                    if (area.supportingPlanets.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = AppTheme.DividerColor)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            StringResources.get(StringKeyPrediction.PRED_LABEL_SUPPORTING_PLANETS, language),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = areaColor
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            area.supportingPlanets.forEach { planet ->
                                AssistChip(
                                    onClick = {},
                                    label = {
                                        Text(
                                            planet.getLocalizedName(language),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = areaColor.copy(alpha = 0.12f),
                                        labelColor = areaColor
                                    ),
                                    border = null
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = areaColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Outlined.Lightbulb,
                                contentDescription = null,
                                tint = areaColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    StringResources.get(StringKeyPrediction.PRED_LABEL_ADVICE, language),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = areaColor
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    area.advice,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PredictionTimeframe(title: String, prediction: String, color: Color) {
    Column {
        Text(
            title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            prediction,
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.TextSecondary,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun FavorablePeriodsCard(periods: List<FavorablePeriod>) {
    val language = currentLanguage()
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.TrendingUp,
                    contentDescription = null,
                    tint = AppTheme.SuccessColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    StringResources.get(StringKey.PREDICTIONS_FAVORABLE_PERIODS, language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            periods.forEach { period ->
                Surface(
                    color = AppTheme.SuccessColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "${period.startDate.format(dateFormatter)} - ${period.endDate.format(dateFormatter)}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.SuccessColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            period.reason,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                        if (period.bestFor.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "${StringResources.get(StringKey.PREDICTIONS_BEST_FOR, language)}:",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                            period.bestFor.forEach { item ->
                                Row {
                                    Text("• ", color = AppTheme.SuccessColor)
                                    Text(
                                        item,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = AppTheme.TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun UnfavorablePeriodsCard(periods: List<UnfavorablePeriod>) {
    val language = currentLanguage()
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.TrendingDown,
                    contentDescription = null,
                    tint = AppTheme.WarningColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    StringResources.get(StringKey.PREDICTIONS_CAUTION_PERIODS, language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            periods.forEach { period ->
                Surface(
                    color = AppTheme.WarningColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "${period.startDate.format(dateFormatter)} - ${period.endDate.format(dateFormatter)}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.WarningColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            period.reason,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                        if (period.avoid.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "${StringResources.get(StringKey.PREDICTIONS_CAUTION_FOR, language)}:",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                            period.avoid.forEach { item ->
                                Row {
                                    Text("• ", color = AppTheme.WarningColor)
                                    Text(
                                        item,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = AppTheme.TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun KeyDatesCard(dates: List<KeyDate>) {
    val language = currentLanguage()
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM dd")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.CalendarToday,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    StringResources.get(StringKey.PREDICTIONS_IMPORTANT_DATES, language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            dates.forEach { keyDate ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (keyDate.isPositive) AppTheme.SuccessColor.copy(alpha = 0.15f)
                                else AppTheme.WarningColor.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            keyDate.date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (keyDate.isPositive) AppTheme.SuccessColor else AppTheme.WarningColor
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            keyDate.date.format(dateFormatter),
                            style = MaterialTheme.typography.labelMedium,
                            color = AppTheme.TextMuted
                        )
                        Text(
                            keyDate.event,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            keyDate.significance,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
                if (dates.last() != keyDate) {
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(color = AppTheme.DividerColor)
                }
            }
        }
    }
}

@Composable
private fun RemedialSuggestionsCard(remedies: List<RemedySuggestion>, currentPeriod: String) {
    val language = currentLanguage()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Spa,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    StringResources.get(StringKey.PREDICTIONS_REMEDIAL_SUGGESTIONS, language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                StringResources.get(StringKeyPrediction.PRED_REMEDY_FOR_PERIOD, language, currentPeriod),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

            remedies.forEachIndexed { index, remedy ->
                Surface(
                    color = AppTheme.AccentPrimary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(AppTheme.AccentPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                (index + 1).toString(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.AccentPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                remedy.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = AppTheme.TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                remedy.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextSecondary,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                remedy.method,
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextSecondary,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "${remedy.timing} • ${remedy.duration}",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                        }
                    }
                }
                if (index < remedies.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

// Empty and Error States
@Composable
private fun EmptyPredictionsState(modifier: Modifier = Modifier) {
    val language = currentLanguage()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.InsertChart,
                contentDescription = null,
                tint = AppTheme.TextSubtle,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                StringResources.get(StringKey.PREDICTIONS_NO_CHART_SELECTED, language),
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.TextMuted
            )
            Text(
                StringResources.get(StringKey.PREDICTIONS_SELECT_CHART_MESSAGE, language),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSubtle
            )
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    val language = currentLanguage()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = AppTheme.AccentPrimary,
                strokeWidth = 3.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                StringResources.get(StringKey.PREDICTIONS_CALCULATING, language),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language = currentLanguage()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = AppTheme.ErrorColor,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                StringResources.get(StringKey.PREDICTIONS_ERROR_LOADING, language),
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                message,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.AccentPrimary
                )
            ) {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(StringResources.get(StringKey.BTN_RETRY, language))
            }
        }
    }
}

@Composable
private fun getEnergyColor(energy: Int): Color = when {
    energy >= 8 -> AppTheme.SuccessColor
    energy >= 6 -> AppTheme.AccentGold
    energy >= 4 -> AppTheme.WarningColor
    else -> AppTheme.ErrorColor
}

@Composable
private fun getPlanetColor(planet: Planet): Color = AppTheme.getPlanetColor(planet)
