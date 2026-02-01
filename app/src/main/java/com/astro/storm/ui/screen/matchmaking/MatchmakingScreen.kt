package com.astro.storm.ui.screen.matchmaking

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.repository.SavedChart
import com.astro.storm.core.common.Language
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringResources
import com.astro.storm.data.localization.currentLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.*
import com.astro.storm.ephemeris.MatchmakingCalculator
import com.astro.storm.ephemeris.VedicAstrologyUtils
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.ChartViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.EntryPointAccessors
import com.astro.storm.data.ai.agent.AgentResponse
import com.astro.storm.data.ai.agent.StormyAgent
import com.astro.storm.data.ai.provider.AiProviderRegistry
import com.astro.storm.data.ai.provider.ChatMessage
import com.astro.storm.data.ai.provider.MessageRole
import com.astro.storm.ui.components.ContentCleaner
import com.astro.storm.ui.components.MarkdownText

/**
 * Entry point for getting dependencies in MatchmakingScreen
 */
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
@dagger.hilt.EntryPoint
interface MatchmakingScreenEntryPoint {
    fun aiProviderRegistry(): com.astro.storm.data.ai.provider.AiProviderRegistry
    fun stormyAgent(): com.astro.storm.data.ai.agent.StormyAgent
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchmakingScreen(
    savedCharts: List<SavedChart>,
    viewModel: ChartViewModel,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedBrideId by remember { mutableStateOf<Long?>(null) }
    var selectedGroomId by remember { mutableStateOf<Long?>(null) }
    var brideChart by remember { mutableStateOf<VedicChart?>(null) }
    var groomChart by remember { mutableStateOf<VedicChart?>(null) }

    var matchingResult by remember { mutableStateOf<MatchmakingResult?>(null) }
    var isCalculating by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var showBrideSelector by remember { mutableStateOf(false) }
    var showGroomSelector by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(StringKeyMatchmaking.MATCH_OVERVIEW),
        stringResource(StringKeyMatchmaking.MATCH_GUNAS),
        stringResource(StringKeyMatchmaking.MATCH_DOSHAS),
        stringResource(StringKeyMatchmaking.MATCH_NAKSHATRAS),
        stringResource(StringKeyMatchmaking.MATCH_REMEDIES)
    )

    val animatedProgress by animateFloatAsState(
        targetValue = matchingResult?.let { (it.totalPoints / it.maxPoints).toFloat() } ?: 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "progress"
    )

    LaunchedEffect(selectedBrideId) {
        selectedBrideId?.let { id ->
            brideChart = withContext(Dispatchers.IO) { viewModel.getChartById(id) }
        } ?: run { brideChart = null }
    }

    LaunchedEffect(selectedGroomId) {
        selectedGroomId?.let { id ->
            groomChart = withContext(Dispatchers.IO) { viewModel.getChartById(id) }
        } ?: run { groomChart = null }
    }

    // Pre-fetch localized strings and language for use in LaunchedEffect and scope.launch (stringResource is @Composable)
    val errorCalculationFailedText = stringResource(StringKeyError.ERROR_CALCULATION_FAILED)
    val language = currentLanguage()

    LaunchedEffect(brideChart, groomChart) {
        if (brideChart != null && groomChart != null) {
            isCalculating = true
            errorMessage = null
            delay(300)
            try {
                matchingResult = withContext(Dispatchers.Default) {
                    MatchmakingCalculator.calculateMatchmaking(brideChart!!, groomChart!!)
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: errorCalculationFailedText
            }
            isCalculating = false
        } else {
            matchingResult = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_TITLE),
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary,
                            fontSize = 18.sp
                        )
                        AnimatedVisibility(visible = matchingResult != null) {
                            Text(
                                stringResource(StringKeyMatchmaking.MATCH_ASHTAKOOTA),
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextMuted
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onBack()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(StringKeyUI.BTN_BACK),
                            tint = AppTheme.TextPrimary
                        )
                    }
                },
                actions = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.ScreenBackground
                )
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = AppTheme.CardBackground,
                    contentColor = AppTheme.TextPrimary,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        containerColor = AppTheme.ScreenBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                EnhancedProfileSelectionSection(
                    savedCharts = savedCharts,
                    selectedBrideId = selectedBrideId,
                    selectedGroomId = selectedGroomId,
                    brideChart = brideChart,
                    groomChart = groomChart,
                    onSelectBride = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showBrideSelector = true
                    },
                    onSelectGroom = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showGroomSelector = true
                    },
                    onSwapProfiles = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        val tempId = selectedBrideId
                        selectedBrideId = selectedGroomId
                        selectedGroomId = tempId
                    },
                    onClearSelection = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        selectedBrideId = null
                        selectedGroomId = null
                        brideChart = null
                        groomChart = null
                        matchingResult = null
                    }
                )
            }

            if (isCalculating) {
                item {
                    CalculatingState()
                }
            }

            errorMessage?.let { error ->
                item {
                    ErrorCard(error) {
                        errorMessage = null
                        scope.launch {
                            delay(100)
                            if (brideChart != null && groomChart != null) {
                                isCalculating = true
                                try {
                                    matchingResult = withContext(Dispatchers.Default) {
                                        MatchmakingCalculator.calculateMatchmaking(brideChart!!, groomChart!!)
                                    }
                                } catch (e: Exception) {
                                    errorMessage = e.message
                                }
                                isCalculating = false
                            }
                        }
                    }
                }
            }

            matchingResult?.let { result ->
                item {
                    EnhancedCompatibilityScoreCard(result, animatedProgress)
                }

                item {
                    QuickInsightsRow(result)
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    TabSelector(
                        tabs = tabs,
                        selectedTab = selectedTab,
                        onTabSelected = { index ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            selectedTab = index
                        }
                    )
                }

                when (selectedTab) {
                    0 -> {
                        item {
                            OverviewSection(result, brideChart, groomChart)
                        }
                    }
                    1 -> {
                        item {
                            GunaSummaryHeader(result)
                        }
                        itemsIndexed(result.gunaAnalyses) { index, guna ->
                            AnimatedGunaCard(guna, index)
                        }
                    }
                    2 -> {
                        item {
                            DoshaSection(result)
                        }
                    }
                    3 -> {
                        item {
                            NakshatraSection(result, brideChart, groomChart)
                        }
                    }
                    4 -> {
                        item {
                            EnhancedRemediesSection(result)
                        }
                        item {
                            MatchmakingAiInsightCard(
                                result = result,
                                brideChart = brideChart,
                                groomChart = groomChart
                            )
                        }
                    }
                }
            }

            if (matchingResult == null && !isCalculating && errorMessage == null) {
                item {
                    EmptyMatchingState(
                        hasBride = brideChart != null,
                        hasGroom = groomChart != null,
                        hasCharts = savedCharts.isNotEmpty()
                    )
                }
            }
        }
    }

    if (showBrideSelector) {
        EnhancedProfileSelectorBottomSheet(
            title = stringResource(StringKeyMatchmaking.MATCH_SELECT_BRIDE),
            icon = Icons.Filled.Female,
            accentColor = AppTheme.LifeAreaLove,
            charts = savedCharts,
            selectedId = selectedBrideId,
            excludeId = selectedGroomId,
            onSelect = { id ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                selectedBrideId = id
                showBrideSelector = false
            },
            onDismiss = { showBrideSelector = false }
        )
    }

    if (showGroomSelector) {
        EnhancedProfileSelectorBottomSheet(
            title = stringResource(StringKeyMatchmaking.MATCH_SELECT_GROOM),
            icon = Icons.Filled.Male,
            accentColor = AppTheme.AccentTeal,
            charts = savedCharts,
            selectedId = selectedGroomId,
            excludeId = selectedBrideId,
            onSelect = { id ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                selectedGroomId = id
                showGroomSelector = false
            },
            onDismiss = { showGroomSelector = false }
        )
    }
}

@Composable
private fun EnhancedProfileSelectionSection(
    savedCharts: List<SavedChart>,
    selectedBrideId: Long?,
    selectedGroomId: Long?,
    brideChart: VedicChart?,
    groomChart: VedicChart?,
    onSelectBride: () -> Unit,
    onSelectGroom: () -> Unit,
    onSwapProfiles: () -> Unit,
    onClearSelection: () -> Unit
) {
    val hasSelection = brideChart != null || groomChart != null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(StringKeyMatchmaking.MATCH_SELECT_PROFILES),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                
                AnimatedVisibility(visible = hasSelection) {
                    Row {
                        if (brideChart != null && groomChart != null) {
                            IconButton(
                                onClick = onSwapProfiles,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Filled.SwapHoriz,
                                    contentDescription = stringResource(StringKeyMatchmaking.MATCH_SWAP_PROFILES),
                                    tint = AppTheme.AccentPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        IconButton(
                            onClick = onClearSelection,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = stringResource(StringKeyMatchmaking.MATCH_CLEAR_SELECTION),
                                tint = AppTheme.TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EnhancedProfileCard(
                    label = stringResource(StringKeyMatchmaking.MATCH_BRIDE),
                    chart = brideChart,
                    icon = Icons.Filled.Female,
                    color = AppTheme.LifeAreaLove,
                    onClick = onSelectBride,
                    modifier = Modifier.weight(1f)
                )

                ConnectionIndicator(
                    isConnected = brideChart != null && groomChart != null,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                EnhancedProfileCard(
                    label = stringResource(StringKeyMatchmaking.MATCH_GROOM),
                    chart = groomChart,
                    icon = Icons.Filled.Male,
                    color = AppTheme.AccentTeal,
                    onClick = onSelectGroom,
                    modifier = Modifier.weight(1f)
                )
            }

            if (savedCharts.isEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = AppTheme.InfoColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            tint = AppTheme.InfoColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_CREATE_CHARTS_FIRST),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.InfoColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EnhancedProfileCard(
    label: String,
    chart: VedicChart?,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (chart != null) 1f else 0.98f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "scale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (chart != null) color.copy(alpha = 0.08f) else AppTheme.ChipBackground
        ),
        shape = RoundedCornerShape(16.dp),
        border = if (chart != null) null else androidx.compose.foundation.BorderStroke(
            1.dp, AppTheme.BorderColor.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        if (chart != null) color.copy(alpha = 0.15f) else AppTheme.ChipBackground
                    )
                    .border(
                        width = 2.dp,
                        color = if (chart != null) color else AppTheme.BorderColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (chart != null) color else AppTheme.TextMuted,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = if (chart != null) color else AppTheme.TextMuted,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                chart?.birthData?.name ?: stringResource(StringKeyMatchmaking.MATCH_TAP_TO_SELECT),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (chart != null) FontWeight.SemiBold else FontWeight.Normal,
                color = if (chart != null) AppTheme.TextPrimary else AppTheme.TextSubtle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            chart?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    getNakshatraName(it),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun EnhancedCompatibilityScoreCard(
    result: MatchmakingResult,
    animatedProgress: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            getRatingColor(result.rating).copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 14.dp,
                        color = AppTheme.ChipBackground,
                        strokeCap = StrokeCap.Round
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 14.dp,
                        color = getRatingColor(result.rating),
                        strokeCap = StrokeCap.Round
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format("%.1f", result.totalPoints),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = stringResource(StringKeyMatchmaking.MATCH_OUT_OF, result.maxPoints.toInt()),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    color = getRatingColor(result.rating).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            getRatingIcon(result.rating),
                            contentDescription = null,
                            tint = getRatingColor(result.rating),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            result.rating.getLocalizedName(LocalLanguage.current),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = getRatingColor(result.rating)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = getRatingColor(result.rating)
                        )) {
                            append(String.format("%.1f%%", result.percentage))
                        }
                        append(" ${stringResource(StringKeyMatchmaking.MATCH_COMPATIBILITY)}")
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppTheme.TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = result.rating.getLocalizedDescription(LocalLanguage.current),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickInsightsRow(result: MatchmakingResult) {
    val language = LocalLanguage.current
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val hasNadiDosha = result.gunaAnalyses.find { it.gunaType == GunaType.NADI }?.hasDosha == true
        val hasBhakootDosha = result.gunaAnalyses.find { it.gunaType == GunaType.BHAKOOT }?.hasDosha == true

        item {
            QuickInsightChip(
                label = stringResource(StringKeyMatchmaking.MATCH_MANGLIK),
                value = result.getManglikQuickStatus(language),
                color = getManglikStatusColor(result.manglikCompatibilityLevel)
            )
        }

        if (hasNadiDosha) {
            item {
                QuickInsightChip(
                    label = stringResource(StringKeyMatchmaking.MATCH_NADI),
                    value = stringResource(StringKeyMatchmaking.MATCH_DOSHA_PRESENT),
                    color = AppTheme.ErrorColor
                )
            }
        }

        if (hasBhakootDosha) {
            item {
                QuickInsightChip(
                    label = stringResource(StringKeyMatchmaking.MATCH_BHAKOOT),
                    value = stringResource(StringKeyMatchmaking.MATCH_NEEDS_ATTENTION),
                    color = AppTheme.WarningColor
                )
            }
        }

        item {
            QuickInsightChip(
                label = stringResource(StringKeyMatchmaking.MATCH_GUNAS),
                value = "${result.totalPoints.toInt()}/${result.maxPoints.toInt()}",
                color = if (result.totalPoints >= 18) AppTheme.SuccessColor else AppTheme.WarningColor
            )
        }
    }
}

@Composable
private fun TabSelector(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(tabs) { index, title ->
            val isSelected = selectedTab == index
            FilterChip(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                label = {
                    Text(
                        tabs[index],
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.15f),
                    selectedLabelColor = AppTheme.AccentPrimary,
                    containerColor = AppTheme.ChipBackground,
                    labelColor = AppTheme.TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = Color.Transparent,
                    selectedBorderColor = AppTheme.AccentPrimary.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
private fun OverviewSection(
    result: MatchmakingResult,
    brideChart: VedicChart?,
    groomChart: VedicChart?
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.BarChart,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(StringKeyMatchmaking.MATCH_GUNA_DISTRIBUTION),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                result.gunaAnalyses.forEachIndexed { index, guna ->
                    EnhancedGunaScoreBar(guna)
                    if (index != result.gunaAnalyses.lastIndex) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        if (brideChart != null && groomChart != null) {
            Spacer(modifier = Modifier.height(8.dp))
            ProfileComparisonCard(brideChart, groomChart)
        }

        if (result.specialConsiderations.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            SpecialConsiderationsCard(result.specialConsiderations)
        }
    }
}

@Composable
private fun EnhancedGunaScoreBar(guna: GunaAnalysis) {
    val animatedProgress by animateFloatAsState(
        targetValue = (guna.obtainedPoints / guna.maxPoints).toFloat(),
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "guna_progress"
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Text(
                    guna.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "(${guna.description})",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                color = if (guna.isPositive) AppTheme.SuccessColor.copy(alpha = 0.1f) 
                       else AppTheme.WarningColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    "${guna.obtainedPoints.toInt()}/${guna.maxPoints.toInt()}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (guna.isPositive) AppTheme.SuccessColor else AppTheme.WarningColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(AppTheme.ChipBackground)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = if (guna.isPositive) 
                                listOf(AppTheme.SuccessColor.copy(alpha = 0.7f), AppTheme.SuccessColor)
                            else 
                                listOf(AppTheme.WarningColor.copy(alpha = 0.7f), AppTheme.WarningColor)
                        )
                    )
            )
        }
    }
}

@Composable
private fun ProfileComparisonCard(
    brideChart: VedicChart,
    groomChart: VedicChart
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Compare,
                    contentDescription = null,
                    tint = AppTheme.AccentSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(StringKeyMatchmaking.MATCH_PROFILE_COMPARISON),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        brideChart.birthData.name ?: stringResource(StringKeyMatchmaking.MATCH_BRIDE),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.LifeAreaLove
                    )
                }
                Text(stringResource(StringKeyMatchmaking.MATCH_VS), color = AppTheme.TextMuted)
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        groomChart.birthData.name ?: stringResource(StringKeyMatchmaking.MATCH_GROOM),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.AccentTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = AppTheme.DividerColor)
            Spacer(modifier = Modifier.height(12.dp))

            val language = LocalLanguage.current
            ComparisonRow(stringResource(StringKeyMatchmaking.MATCH_MOON_SIGN), MatchmakingReportUtils.getRashiName(brideChart, language), MatchmakingReportUtils.getRashiName(groomChart, language))
            ComparisonRow(stringResource(StringKeyMatchmaking.MATCH_NAKSHATRA), MatchmakingReportUtils.getNakshatraName(brideChart, language), MatchmakingReportUtils.getNakshatraName(groomChart, language))
            ComparisonRow(stringResource(StringKeyMatchmaking.MATCH_PADA), MatchmakingReportUtils.getPada(brideChart, language), MatchmakingReportUtils.getPada(groomChart, language))
        }
    }
}

@Composable
private fun SpecialConsiderationsCard(considerations: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = AppTheme.InfoColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(StringKeyMatchmaking.MATCH_KEY_CONSIDERATIONS),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            considerations.forEach { consideration ->
                Row(
                    modifier = Modifier.padding(vertical = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(AppTheme.AccentPrimary)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        consideration,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun GunaSummaryHeader(result: MatchmakingResult) {
    val positiveCount = result.gunaAnalyses.count { it.isPositive }
    val totalCount = result.gunaAnalyses.size

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.AccentPrimary.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "$positiveCount",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.SuccessColor
                )
                Text(stringResource(StringKeyMatchmaking.MATCH_FAVORABLE), style = MaterialTheme.typography.labelSmall, color = AppTheme.TextMuted)
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(AppTheme.DividerColor)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "${totalCount - positiveCount}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.WarningColor
                )
                Text(stringResource(StringKeyMatchmaking.MATCH_NEEDS_ATTENTION), style = MaterialTheme.typography.labelSmall, color = AppTheme.TextMuted)
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(AppTheme.DividerColor)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    String.format("%.1f", result.totalPoints),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.AccentPrimary
                )
                Text(stringResource(StringKeyMatchmaking.MATCH_TOTAL_SCORE), style = MaterialTheme.typography.labelSmall, color = AppTheme.TextMuted)
            }
        }
    }
}

@Composable
private fun AnimatedGunaCard(
    guna: GunaAnalysis,
    index: Int,
    language: Language = currentLanguage()
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 50L)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 3 }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (guna.isPositive) AppTheme.SuccessColor.copy(alpha = 0.12f)
                                    else AppTheme.WarningColor.copy(alpha = 0.12f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (guna.isPositive) Icons.Filled.CheckCircle else Icons.Outlined.Warning,
                                contentDescription = null,
                                tint = if (guna.isPositive) AppTheme.SuccessColor else AppTheme.WarningColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                guna.gunaType.getLocalizedName(language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = AppTheme.TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                com.astro.storm.ephemeris.MatchmakingCalculator.getGunaDescription(guna.gunaType.displayName, language),
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = if (guna.isPositive) AppTheme.SuccessColor.copy(alpha = 0.12f)
                        else AppTheme.WarningColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            "${guna.obtainedPoints.toInt()}/${guna.maxPoints.toInt()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (guna.isPositive) AppTheme.SuccessColor else AppTheme.WarningColor,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = AppTheme.DividerColor)
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(AppTheme.LifeAreaLove)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                stringResource(StringKeyMatchmaking.MATCH_BRIDE),
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                        }
                        Text(
                            guna.brideValue,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.LifeAreaLove
                        )
                    }
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                stringResource(StringKeyMatchmaking.MATCH_GROOM),
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(AppTheme.AccentTeal)
                            )
                        }
                        Text(
                            guna.groomValue,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.AccentTeal,
                            textAlign = TextAlign.End
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    color = AppTheme.ChipBackground,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        guna.analysis,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DoshaSection(result: MatchmakingResult) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = AppTheme.PlanetMars,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_MANGLIK_DOSHA_ANALYSIS),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_MARS_PLACEMENT),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    color = getManglikStatusColor(result.manglikCompatibilityLevel).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            getManglikStatusIcon(result.manglikCompatibilityLevel),
                            contentDescription = null,
                            tint = getManglikStatusColor(result.manglikCompatibilityLevel),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            result.manglikCompatibilityRecommendation,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = getManglikStatusColor(result.manglikCompatibilityLevel),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        EnhancedManglikPersonCard(result.brideManglik, stringResource(StringKeyMatchmaking.MATCH_BRIDE), AppTheme.LifeAreaLove)
        EnhancedManglikPersonCard(result.groomManglik, stringResource(StringKeyMatchmaking.MATCH_GROOM), AppTheme.AccentTeal)

        NadiDoshaCard(result)
        BhakootDoshaCard(result)
    }
}

@Composable
private fun EnhancedManglikPersonCard(
    analysis: ManglikAnalysis,
    label: String,
    accentColor: Color
) {
    val language = LocalLanguage.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (label == stringResource(StringKeyMatchmaking.MATCH_BRIDE)) Icons.Filled.Female else Icons.Filled.Male,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            analysis.person,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = accentColor
                        )
                        if (analysis.marsHouse > 0) {
                            Text(
                                stringResource(StringKeyMatchmaking.MATCH_MARS_IN_HOUSE, analysis.marsHouse),
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextMuted
                            )
                        }
                    }
                }

                Surface(
                    color = getManglikSeverityColor(analysis.effectiveDosha).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        analysis.effectiveDosha.getLocalizedName(language),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = getManglikSeverityColor(analysis.effectiveDosha),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            if (analysis.factors.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    color = AppTheme.WarningColor.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_CONTRIBUTING_FACTORS),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.WarningColor
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        analysis.factors.forEach { factor ->
                            Text(
                                "• $factor",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.WarningColor.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            if (analysis.cancellations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = AppTheme.SuccessColor.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_CANCELLATION_FACTORS),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.SuccessColor
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        analysis.cancellations.forEach { cancellation ->
                            Text(
                                "✓ $cancellation",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.SuccessColor.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NadiDoshaCard(result: MatchmakingResult) {
    val nadiGuna = result.gunaAnalyses.find { it.gunaType == GunaType.NADI }
    val hasNadiDosha = nadiGuna?.obtainedPoints == 0.0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (hasNadiDosha) AppTheme.ErrorColor.copy(alpha = 0.06f) 
                           else AppTheme.CardBackground
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Bloodtype,
                        contentDescription = null,
                        tint = if (hasNadiDosha) AppTheme.ErrorColor else AppTheme.SuccessColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_NADI_DOSHA),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_HEALTH_PROGENY),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }
                Surface(
                    color = if (hasNadiDosha) AppTheme.ErrorColor.copy(alpha = 0.12f) 
                           else AppTheme.SuccessColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (hasNadiDosha) stringResource(StringKeyMatchmaking.MATCH_PRESENT) else stringResource(StringKeyMatchmaking.MATCH_ABSENT),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (hasNadiDosha) AppTheme.ErrorColor else AppTheme.SuccessColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            nadiGuna?.let { guna ->
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(StringKeyMatchmaking.MATCH_BRIDE), style = MaterialTheme.typography.labelSmall, color = AppTheme.TextMuted)
                        Text(
                            guna.brideValue,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.LifeAreaLove
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(StringKeyMatchmaking.MATCH_GROOM), style = MaterialTheme.typography.labelSmall, color = AppTheme.TextMuted)
                        Text(
                            guna.groomValue,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.AccentTeal
                        )
                    }
                }

                if (hasNadiDosha) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = AppTheme.InfoColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_NADI_WARNING),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.InfoColor,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BhakootDoshaCard(result: MatchmakingResult) {
    val bhakootGuna = result.gunaAnalyses.find { it.gunaType == GunaType.BHAKOOT }
    val hasBhakootDosha = bhakootGuna?.obtainedPoints == 0.0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (hasBhakootDosha) AppTheme.WarningColor.copy(alpha = 0.06f) 
                           else AppTheme.CardBackground
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.CurrencyExchange,
                        contentDescription = null,
                        tint = if (hasBhakootDosha) AppTheme.WarningColor else AppTheme.SuccessColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_BHAKOOT_DOSHA),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_FINANCIAL_HARMONY),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }
                Surface(
                    color = if (hasBhakootDosha) AppTheme.WarningColor.copy(alpha = 0.12f) 
                           else AppTheme.SuccessColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (hasBhakootDosha) stringResource(StringKeyMatchmaking.MATCH_PRESENT) else stringResource(StringKeyMatchmaking.MATCH_ABSENT),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (hasBhakootDosha) AppTheme.WarningColor else AppTheme.SuccessColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            bhakootGuna?.let { guna ->
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(StringKeyMatchmaking.MATCH_BRIDE_RASHI), style = MaterialTheme.typography.labelSmall, color = AppTheme.TextMuted)
                        Text(
                            guna.brideValue,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.LifeAreaLove
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(StringKeyMatchmaking.MATCH_GROOM_RASHI), style = MaterialTheme.typography.labelSmall, color = AppTheme.TextMuted)
                        Text(
                            guna.groomValue,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.AccentTeal
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NakshatraSection(
    result: MatchmakingResult,
    brideChart: VedicChart?,
    groomChart: VedicChart?
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.AutoAwesome,
                        contentDescription = null,
                        tint = AppTheme.AccentSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_NAKSHATRA_COMPATIBILITY),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_BIRTH_STAR),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                if (brideChart != null && groomChart != null) {
                    Spacer(modifier = Modifier.height(20.dp))

                    NakshatraComparisonRow(
                        label = stringResource(StringKeyMatchmaking.MATCH_BIRTH_NAKSHATRA),
                        brideValue = getNakshatraName(brideChart),
                        groomValue = getNakshatraName(groomChart)
                    )

                    NakshatraComparisonRow(
                        label = stringResource(StringKeyMatchmaking.MATCH_NAKSHATRA_LORD),
                        brideValue = getNakshatraLord(brideChart),
                        groomValue = getNakshatraLord(groomChart)
                    )

                    NakshatraComparisonRow(
                        label = stringResource(StringKeyMatchmaking.MATCH_PADA),
                        brideValue = getPada(brideChart),
                        groomValue = getPada(groomChart)
                    )

                    NakshatraComparisonRow(
                        label = stringResource(StringKeyMatchmaking.MATCH_GANA),
                        brideValue = getGana(brideChart),
                        groomValue = getGana(groomChart)
                    )

                    NakshatraComparisonRow(
                        label = stringResource(StringKeyMatchmaking.MATCH_YONI),
                        brideValue = getYoni(brideChart),
                        groomValue = getYoni(groomChart)
                    )
                }
            }
        }

        RajjuAnalysisCard(result)
        VedhaAnalysisCard(result)
        StreeDeerghCard(result, brideChart, groomChart)
    }
}

@Composable
private fun RajjuAnalysisCard(result: MatchmakingResult) {
    val hasRajjuDosha = !result.additionalFactors.rajjuCompatible

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (hasRajjuDosha) AppTheme.ErrorColor.copy(alpha = 0.06f) 
                           else AppTheme.CardBackground
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Link,
                        contentDescription = null,
                        tint = if (hasRajjuDosha) AppTheme.ErrorColor else AppTheme.SuccessColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_RAJJU_MATCHING),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_LONGEVITY),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }
                Surface(
                    color = if (hasRajjuDosha) AppTheme.ErrorColor.copy(alpha = 0.12f) 
                           else AppTheme.SuccessColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (hasRajjuDosha) stringResource(StringKeyMatchmaking.MATCH_CONFLICT) else stringResource(StringKeyMatchmaking.MATCH_COMPATIBLE),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (hasRajjuDosha) AppTheme.ErrorColor else AppTheme.SuccessColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                stringResource(StringKeyMatchmaking.MATCH_RAJJU_DESCRIPTION),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun VedhaAnalysisCard(result: MatchmakingResult) {
    val hasVedha = result.additionalFactors.vedhaPresent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (hasVedha) AppTheme.WarningColor.copy(alpha = 0.06f) 
                           else AppTheme.CardBackground
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Block,
                        contentDescription = null,
                        tint = if (hasVedha) AppTheme.WarningColor else AppTheme.SuccessColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_VEDHA_ANALYSIS),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_OBSTRUCTION_CHECK),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }
                Surface(
                    color = if (hasVedha) AppTheme.WarningColor.copy(alpha = 0.12f) 
                           else AppTheme.SuccessColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (hasVedha) stringResource(StringKeyMatchmaking.MATCH_PRESENT) else stringResource(StringKeyMatchmaking.MATCH_NONE),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (hasVedha) AppTheme.WarningColor else AppTheme.SuccessColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                stringResource(StringKeyMatchmaking.MATCH_VEDHA_DESCRIPTION),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun StreeDeerghCard(
    result: MatchmakingResult,
    brideChart: VedicChart?,
    groomChart: VedicChart?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.WbTwilight,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        stringResource(StringKeyMatchmaking.MATCH_STREE_DEERGHA),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        stringResource(StringKeyMatchmaking.MATCH_PROSPERITY_FACTORS),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        stringResource(StringKeyMatchmaking.MATCH_STREE_DEERGHA_LABEL),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        stringResource(StringKeyMatchmaking.MATCH_FAVORABLE),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.SuccessColor
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(50.dp)
                        .background(AppTheme.DividerColor)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Outlined.TrendingUp,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        stringResource(StringKeyMatchmaking.MATCH_MAHENDRA),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        stringResource(StringKeyMatchmaking.MATCH_BENEFICIAL),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.SuccessColor
                    )
                }
            }
        }
    }
}

@Composable
private fun EnhancedRemediesSection(result: MatchmakingResult) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Spa,
                        contentDescription = null,
                        tint = AppTheme.LifeAreaHealth,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_SUGGESTED_REMEDIES),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_VEDIC_RECOMMENDATIONS),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                result.remedies.forEachIndexed { index, remedy ->
                    RemedyItem(index + 1, remedy)
                    if (index != result.remedies.lastIndex) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = AppTheme.InfoColor.copy(alpha = 0.08f),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = null,
                    tint = AppTheme.InfoColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    stringResource(StringKeyMatchmaking.MATCH_REMEDIES_DISCLAIMER),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.InfoColor,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun EmptyMatchingState(
    hasBride: Boolean,
    hasGroom: Boolean,
    hasCharts: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(AppTheme.ChipBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (hasCharts) Icons.Outlined.FavoriteBorder else Icons.Outlined.PersonAddAlt,
                    contentDescription = null,
                    tint = AppTheme.TextSubtle,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(
                    when {
                        !hasCharts -> StringKeyMatchmaking.MATCH_NO_CHARTS
                        !hasBride && !hasGroom -> StringKeyMatchmaking.MATCH_SELECT_BOTH
                        !hasBride -> StringKeyMatchmaking.MATCH_SELECT_BRIDE_PROFILE
                        !hasGroom -> StringKeyMatchmaking.MATCH_SELECT_GROOM_PROFILE
                        else -> StringKeyMatchmaking.MATCH_PREPARING_ANALYSIS
                    }
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(
                    when {
                        !hasCharts -> StringKeyMatchmaking.MATCH_CREATE_CHARTS
                        !hasBride && !hasGroom -> StringKeyMatchmaking.MATCH_SELECT_TAP_CARDS
                        !hasBride -> StringKeyMatchmaking.MATCH_TAP_BRIDE_CARD
                        !hasGroom -> StringKeyMatchmaking.MATCH_TAP_GROOM_CARD
                        else -> StringKeyMatchmaking.MATCH_CALCULATING
                    }
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 48.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnhancedProfileSelectorBottomSheet(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    charts: List<SavedChart>,
    selectedId: Long?,
    excludeId: Long?,
    onSelect: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val availableCharts = charts.filter { it.id != excludeId }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppTheme.CardBackground,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(AppTheme.TextSubtle.copy(alpha = 0.4f))
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        stringResource(StringKeyMatchmaking.MATCH_CHARTS_AVAILABLE, availableCharts.size),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            if (availableCharts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.SearchOff,
                            contentDescription = null,
                            tint = AppTheme.TextSubtle,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(StringKeyMatchmaking.MATCH_NO_CHARTS_AVAILABLE),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableCharts, key = { it.id }) { chart ->
                        val isSelected = chart.id == selectedId

                        Surface(
                            onClick = { onSelect(chart.id) },
                            color = if (isSelected) accentColor.copy(alpha = 0.1f) else Color.Transparent,
                            shape = RoundedCornerShape(14.dp),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(
                                1.5.dp, accentColor
                            ) else androidx.compose.foundation.BorderStroke(
                                1.dp, AppTheme.BorderColor.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) accentColor.copy(alpha = 0.15f) 
                                            else AppTheme.ChipBackground
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        chart.name.firstOrNull()?.uppercase() ?: "?",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) accentColor else AppTheme.TextMuted
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        chart.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = AppTheme.TextPrimary
                                    )
                                    Text(
                                        chart.location,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = AppTheme.TextMuted,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        Icons.Filled.CheckCircle,
                                        contentDescription = stringResource(StringKeyMatchmaking.MATCH_SELECTED),
                                        tint = accentColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Report generation functions moved to MatchmakingReportUtils.kt
 */

private fun getMoonPosition(chart: VedicChart) = chart.planetPositions.find {
    it.planet == Planet.MOON
}

@Composable
private fun getNakshatraName(chart: VedicChart): String {
    val language = LocalLanguage.current
    return getMoonPosition(chart)?.nakshatra?.getLocalizedName(language) ?: stringResource(StringKeyMatchPart1.MISC_UNKNOWN)
}

@Composable
private fun getRashiName(chart: VedicChart): String {
    val unknownText = stringResource(StringKeyMatchPart1.MISC_UNKNOWN)
    val language = LocalLanguage.current
    val moonPosition = getMoonPosition(chart) ?: return unknownText
    return moonPosition.sign.getLocalizedName(language)
}

@Composable
private fun getPada(chart: VedicChart): String {
    val unknownText = stringResource(StringKeyMatchPart1.MISC_UNKNOWN)
    val moonPosition = getMoonPosition(chart) ?: return unknownText
    return stringResource(StringKeyMatchmaking.MATCH_PADA_NUMBER, moonPosition.nakshatraPada)
}

@Composable
private fun getNakshatraLord(chart: VedicChart): String {
    val unknownText = stringResource(StringKeyMatchPart1.MISC_UNKNOWN)
    val language = LocalLanguage.current
    val moonPosition = getMoonPosition(chart) ?: return unknownText
    return moonPosition.nakshatra.ruler.getLocalizedName(language)
}

@Composable
private fun getGana(chart: VedicChart): String {
    val unknownText = stringResource(StringKeyMatchPart1.MISC_UNKNOWN)
    val language = LocalLanguage.current
    val moonPosition = getMoonPosition(chart) ?: return unknownText
    // Use centralized VedicAstrologyUtils for consistent Gana lookup
    return VedicAstrologyUtils.getGanaDisplayName(moonPosition.nakshatra, language)
}

@Composable
private fun getYoni(chart: VedicChart): String {
    val unknownText = stringResource(StringKeyMatchPart1.MISC_UNKNOWN)
    val language = LocalLanguage.current
    val moonPosition = getMoonPosition(chart) ?: return unknownText
    // Use centralized VedicAstrologyUtils for consistent Yoni lookup
    return VedicAstrologyUtils.getYoniDisplayName(moonPosition.nakshatra, language)
}

/**
 * State for AI insight generation in Matchmaking
 */
private sealed interface MatchmakingAiInsightState {
    data object Initial : MatchmakingAiInsightState
    data object Loading : MatchmakingAiInsightState
    data class Streaming(val content: String) : MatchmakingAiInsightState
    data class Success(val content: String) : MatchmakingAiInsightState
    data class Error(val message: String) : MatchmakingAiInsightState
}

/**
 * AI Insight Card for Matchmaking - Provides AI-powered interpretation of compatibility results
 */
@Composable
private fun MatchmakingAiInsightCard(
    result: MatchmakingResult,
    brideChart: VedicChart?,
    groomChart: VedicChart?
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val languageForAi = LocalLanguage.current

    // AI state management
    var aiState by remember { mutableStateOf<MatchmakingAiInsightState>(MatchmakingAiInsightState.Initial) }
    var streamingContent by remember { mutableStateOf("") }
    var displayContent by remember { mutableStateOf("") }
    var streamingJob by remember { mutableStateOf<Job?>(null) }
    var lastUpdateTime by remember { mutableStateOf(0L) }

    // Get AI dependencies using EntryPoint
    val entryPoint = remember(context) {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            MatchmakingScreenEntryPoint::class.java
        )
    }
    val agent = remember(entryPoint) { entryPoint.stormyAgent() }
    val providerRegistry = remember(entryPoint) { entryPoint.aiProviderRegistry() }

    // Generate matchmaking context for the AI
    val matchmakingContext = remember(result, brideChart, groomChart) {
        formatMatchmakingForAi(result, brideChart, groomChart)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.AccentPrimary.copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AppTheme.AccentPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.AutoAwesome,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(StringKeyMatchmaking.MATCH_AI_INSIGHT),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        stringResource(StringKeyMatchmaking.MATCH_AI_INSIGHT_SUBTITLE),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content based on state
            AnimatedContent(
                targetState = aiState,
                label = "matchmaking_ai_insight_state"
            ) { state ->
                when (state) {
                    is MatchmakingAiInsightState.Initial -> {
                        // Show generate button
                        Button(
                            onClick = {
                                scope.launch {
                                    val model = providerRegistry.getDefaultModel()
                                    if (model == null) {
                                        aiState = MatchmakingAiInsightState.Error(com.astro.storm.core.common.StringResources.get(com.astro.storm.core.common.StringKeyUI.UI_NO_AI_MODEL, languageForAi))
                                        return@launch
                                    }

                                    aiState = MatchmakingAiInsightState.Loading
                                    streamingContent = ""

                                    streamingJob = launch {
                                        try {
                                            val messages = listOf(
                                                ChatMessage(
                                                    role = MessageRole.USER,
                                                    content = matchmakingContext
                                                )
                                            )

                                            agent.processMessage(
                                                messages = messages,
                                                model = model,
                                                currentProfile = null,
                                                allProfiles = emptyList(),
                                                currentChart = brideChart,
                                                language = languageForAi
                                            ).collect { response ->
                                                when (response) {
                                                    is AgentResponse.ContentChunk -> {
                                                        streamingContent += response.text
                                                        // Throttle UI updates to reduce recompositions
                                                        val currentTime = System.currentTimeMillis()
                                                        if (currentTime - lastUpdateTime > 100) { // Max 10 updates/second
                                                            lastUpdateTime = currentTime
                                                            // Use raw content during streaming (skip expensive cleaning)
                                                            displayContent = streamingContent.trim()
                                                            aiState = MatchmakingAiInsightState.Streaming(displayContent)
                                                        }
                                                    }
                                                    is AgentResponse.Complete -> {
                                                        // Only apply full cleaning when content is complete
                                                        val finalContent = withContext(Dispatchers.Default) {
                                                            ContentCleaner.cleanForDisplay(response.content)
                                                        }
                                                        aiState = MatchmakingAiInsightState.Success(finalContent)
                                                    }
                                                    is AgentResponse.Error -> {
                                                        aiState = MatchmakingAiInsightState.Error(response.message)
                                                    }
                                                    else -> { /* Ignore other responses */ }
                                                }
                                            }
                                        } catch (e: CancellationException) {
                                            throw e
                                        } catch (e: Exception) {
                                            aiState = MatchmakingAiInsightState.Error(e.message ?: com.astro.storm.core.common.StringResources.get(com.astro.storm.core.common.StringKeyUI.UI_FAILED_GENERATE_INSIGHT, languageForAi))
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppTheme.AccentPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                Icons.Outlined.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = AppTheme.ButtonText
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(StringKeyMatchmaking.MATCH_GENERATE_AI_INSIGHT),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.ButtonText
                            )
                        }
                    }

                    is MatchmakingAiInsightState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = AppTheme.AccentPrimary,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                stringResource(StringKeyDoshaPart3.STORMY_ANALYZING_COMPATIBILITY),
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextMuted
                            )
                        }
                    }

                    is MatchmakingAiInsightState.Streaming -> {
                        Column {
                            Surface(
                                color = AppTheme.CardBackgroundElevated,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    MarkdownText(
                                        markdown = state.content,
                                        textColor = AppTheme.TextSecondary,
                                        linkColor = AppTheme.AccentPrimary,
                                        textSize = 13f
                                    )
                                    // Typing indicator
                                    Row(
                                        modifier = Modifier.padding(top = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(
                                            color = AppTheme.AccentPrimary,
                                            strokeWidth = 2.dp,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            stringResource(StringKeyDoshaPart3.AI_GENERATING),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AppTheme.TextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }

                    is MatchmakingAiInsightState.Success -> {
                        Column {
                            Surface(
                                color = AppTheme.CardBackgroundElevated,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                MarkdownText(
                                    markdown = state.content,
                                    modifier = Modifier.padding(12.dp),
                                    textColor = AppTheme.TextSecondary,
                                    linkColor = AppTheme.AccentPrimary,
                                    textSize = 13f
                                )
                            }
                            // Regenerate button
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Surface(
                                    onClick = {
                                        aiState = MatchmakingAiInsightState.Initial
                                    },
                                    color = AppTheme.AccentPrimary.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Filled.Refresh,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = AppTheme.AccentPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            stringResource(StringKeyUI.BTN_REGENERATE),
                                            style = MaterialTheme.typography.labelMedium,
                                            color = AppTheme.AccentPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    is MatchmakingAiInsightState.Error -> {
                        Column {
                            Surface(
                                color = AppTheme.ErrorColor.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Outlined.ErrorOutline,
                                        contentDescription = null,
                                        tint = AppTheme.ErrorColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        state.message,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = AppTheme.ErrorColor,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { aiState = MatchmakingAiInsightState.Initial },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppTheme.AccentPrimary
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = AppTheme.ButtonText
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    stringResource(StringKeyUI.BTN_TRY_AGAIN),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = AppTheme.ButtonText
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Cleanup on disposal
    DisposableEffect(Unit) {
        onDispose {
            streamingJob?.cancel()
        }
    }
}

/**
 * Format Matchmaking result as context for AI interpretation
 */
private fun formatMatchmakingForAi(
    result: MatchmakingResult,
    brideChart: VedicChart?,
    groomChart: VedicChart?
): String {
    return buildString {
        appendLine("Please provide a deeper Vedic astrology interpretation of this Kundli Milan (matchmaking/compatibility) analysis. Be insightful, practical, and compassionate in your guidance. Focus on helping the couple understand their compatibility and how to strengthen their relationship.")
        appendLine()
        appendLine("## ${StringResources.get(com.astro.storm.core.common.StringKeyReport.REPORT_MATCHMAKING_TITLE, Language.ENGLISH)}")
        appendLine("${StringResources.get(com.astro.storm.core.common.StringKeyMatchmaking.MATCH_TOTAL_SCORE, Language.ENGLISH)}: ${result.totalPoints} / ${result.maxPoints}")
        appendLine("${StringResources.get(com.astro.storm.core.common.StringKeyMatchPart1.SUMMARY_RATING, Language.ENGLISH)}: ${result.rating.getLocalizedName(Language.ENGLISH)}")
        appendLine()

        // Bride's details
        brideChart?.let { chart ->
            appendLine("## ${StringResources.get(StringKeyMatchmaking.MATCH_BRIDE, Language.ENGLISH)}'s ${StringResources.get(StringKeyMatchPart1.DETAILED_TITLE, Language.ENGLISH)}")
            val brideMoon = chart.planetPositions.find { it.planet == Planet.MOON }
            brideMoon?.let { moon ->
                appendLine("- ${StringResources.get(StringKeyMatchPart1.MOON_SIGN, Language.ENGLISH)} (Rashi): ${moon.sign.displayName}")
                appendLine("- ${StringResources.get(StringKeyNakshatra.NAKSHATRA_LABEL, Language.ENGLISH)}: ${moon.nakshatra.displayName} (Pada ${moon.nakshatraPada})")
                appendLine("- ${StringResources.get(StringKeyMatchmaking.MATCH_NAKSHATRA_LORD, Language.ENGLISH)}: ${moon.nakshatra.ruler.displayName}")
            }
            appendLine()
        }

        // Groom's details
        groomChart?.let { chart ->
            appendLine("## ${StringResources.get(StringKeyMatchmaking.MATCH_GROOM, Language.ENGLISH)}'s ${StringResources.get(StringKeyMatchPart1.DETAILED_TITLE, Language.ENGLISH)}")
            val groomMoon = chart.planetPositions.find { it.planet == Planet.MOON }
            groomMoon?.let { moon ->
                appendLine("- ${StringResources.get(StringKeyMatchPart1.MOON_SIGN, Language.ENGLISH)} (Rashi): ${moon.sign.displayName}")
                appendLine("- ${StringResources.get(StringKeyNakshatra.NAKSHATRA_LABEL, Language.ENGLISH)}: ${moon.nakshatra.displayName} (Pada ${moon.nakshatraPada})")
                appendLine("- ${StringResources.get(StringKeyMatchmaking.MATCH_NAKSHATRA_LORD, Language.ENGLISH)}: ${moon.nakshatra.ruler.displayName}")
            }
            appendLine()
        }

        appendLine("## Ashtakoota (8-fold) Guna Analysis")
        result.gunaAnalyses.forEach { guna ->
            appendLine("### ${guna.name} (${guna.obtainedPoints}/${guna.maxPoints} points)")
            appendLine("- Bride: ${guna.brideValue}")
            appendLine("- Groom: ${guna.groomValue}")
            appendLine("- Significance: ${guna.description}")
            if (guna.analysis.isNotEmpty()) {
                appendLine("- Analysis: ${guna.analysis}")
            }
            appendLine()
        }

        appendLine("## Manglik (Kuja Dosha) Analysis")
        appendLine("- Bride Manglik Status: ${if (result.brideManglik.effectiveDosha != ManglikDosha.NONE) "Yes (Manglik)" else "No"}")
        appendLine("- Groom Manglik Status: ${if (result.groomManglik.effectiveDosha != ManglikDosha.NONE) "Yes (Manglik)" else "No"}")
        appendLine("- Compatibility: ${result.manglikCompatibilityLevel}")
        appendLine()

        if (result.specialConsiderations.isNotEmpty()) {
            appendLine("## Special Considerations")
            result.specialConsiderations.forEach { consideration ->
                appendLine("- $consideration")
            }
            appendLine()
        }

        if (result.remedies.isNotEmpty()) {
            appendLine("## Suggested Remedies")
            result.remedies.forEach { remedy ->
                appendLine("- $remedy")
            }
            appendLine()
        }

        appendLine("Please provide:")
        appendLine("1. Your overall interpretation of this compatibility match")
        appendLine("2. Key strengths of this union based on the Guna scores")
        appendLine("3. Areas that may need attention or conscious effort")
        appendLine("4. How the Nakshatra pairing influences their emotional connection")
        appendLine("5. Practical advice for building a harmonious relationship")
        appendLine("6. If there are any doshas, explain their impact and effective remedies")
    }
}


