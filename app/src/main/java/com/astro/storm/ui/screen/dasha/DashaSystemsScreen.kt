package com.astro.storm.ui.screen.dasha

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.astro.storm.core.common.LocalLanguage
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.stringResource
import com.astro.storm.core.model.VedicChart
import java.time.LocalDateTime
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.ephemeris.AshtottariDashaCalculator
import com.astro.storm.ephemeris.AshtottariTimeline
import com.astro.storm.ephemeris.SudarshanaChakraDashaCalculator
import com.astro.storm.ephemeris.SudarshanaTimeline
import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.TabItem
import com.astro.storm.ui.screen.chartdetail.tabs.DashasTabContent
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.DashaUiState
import com.astro.storm.ui.viewmodel.DashaViewModel

/**
 * Unified Dasha Systems Screen
 *
 * Displays all major dasha systems in a tabbed interface:
 * - Vimsottari (120-year cycle) - Most common system
 * - Yogini (36-year cycle) - Alternative system
 * - Ashtottari (108-year cycle) - For night births
 * - Sudarshana Chakra (Triple reference)
 * - Chara (Jaimini sign-based)
 */

enum class DashaSystemType(
    val displayNameKey: StringKeyDosha,
    val shortNameKey: StringKeyDosha,
    val cycleDurationKey: StringKeyDosha,
    val descriptionKey: StringKeyDosha
) {
    VIMSOTTARI(
        displayNameKey = StringKeyDosha.DASHA_VIMSOTTARI_NAME,
        shortNameKey = StringKeyDosha.DASHA_SHORT_VIM,
        cycleDurationKey = StringKeyDosha.DASHA_VIMSOTTARI_DURATION,
        descriptionKey = StringKeyDosha.DASHA_VIMSOTTARI_DESC
    ),
    YOGINI(
        displayNameKey = StringKeyDosha.DASHA_YOGINI,
        shortNameKey = StringKeyDosha.DASHA_SHORT_YOG,
        cycleDurationKey = StringKeyDosha.DASHA_YOGINI_DURATION,
        descriptionKey = StringKeyDosha.DASHA_YOGINI_DESC
    ),
    ASHTOTTARI(
        displayNameKey = StringKeyDosha.DASHA_ASHTOTTARI,
        shortNameKey = StringKeyDosha.DASHA_SHORT_ASH,
        cycleDurationKey = StringKeyDosha.DASHA_ASHTOTTARI_DURATION,
        descriptionKey = StringKeyDosha.DASHA_ASHTOTTARI_DESC
    ),
    SUDARSHANA(
        displayNameKey = StringKeyDosha.DASHA_SUDARSHANA,
        shortNameKey = StringKeyDosha.DASHA_SHORT_SUD,
        cycleDurationKey = StringKeyDosha.DASHA_SUDARSHANA_DURATION,
        descriptionKey = StringKeyDosha.DASHA_SUDARSHANA_DESC
    ),
    CHARA(
        displayNameKey = StringKeyDosha.DASHA_CHARA,
        shortNameKey = StringKeyDosha.DASHA_SHORT_CHA,
        cycleDurationKey = StringKeyDosha.DASHA_CHARA_DURATION,
        descriptionKey = StringKeyDosha.DASHA_CHARA_DESC
    );

    fun getShortName(language: com.astro.storm.core.common.Language): String =
        com.astro.storm.core.common.StringResources.get(shortNameKey, language)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashaSystemsScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    onNavigateToYoginiDasha: () -> Unit,
    onNavigateToCharaDasha: () -> Unit,
    viewModel: DashaViewModel = viewModel()
) {
    val context = LocalContext.current
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val chartKey = remember(chart) {
        chart?.let {
            "${it.birthData.dateTime}|${it.birthData.latitude}|${it.birthData.longitude}"
        }
    }

    // Load Vimsottari dasha
    LaunchedEffect(chartKey) {
        viewModel.loadDashaTimeline(chart)
    }

    val vimsottariState by viewModel.uiState.collectAsStateWithLifecycle()

    // Calculate other dasha systems
    val ashtottariData = remember(chart) {
        chart?.let {
            try {
                val result = AshtottariDashaCalculator.calculateAshtottariDasha(it)
                val moonPosition = it.planetPositions.find { pos -> pos.planet == Planet.MOON }
                val moonLongitude = moonPosition?.longitude ?: 0.0
                val nakshatraResult = Nakshatra.fromLongitude(moonLongitude)
                val birthNakshatra = nakshatraResult.first
                val birthNakshatraPada = nakshatraResult.second
                AshtottariTimeline(
                    mahadashas = result.mahadashas,
                    currentMahadasha = result.currentMahadasha,
                    currentAntardasha = result.currentAntardasha,
                    natalChart = it,
                    startDate = LocalDateTime.now().minusYears(50),
                    endDate = LocalDateTime.now().plusYears(50),
                    applicability = result.applicability,
                    interpretation = result.interpretation,
                    birthNakshatra = birthNakshatra,
                    birthNakshatraLord = result.startingLord,
                    birthNakshatraPada = birthNakshatraPada
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    val sudarshanaData = remember(chart) {
        chart?.let {
            try {
                val result = SudarshanaChakraDashaCalculator.calculateSudarshana(it)
                SudarshanaTimeline(
                    result = result,
                    natalChart = it,
                    currentAge = 30 // Default age - should be calculated from birth date
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    // Read colors outside remember
    val accentPrimary = AppTheme.AccentPrimary
    val lifeAreaLove = AppTheme.LifeAreaLove
    val accentGold = AppTheme.AccentGold
    val accentTeal = AppTheme.AccentTeal
    val lifeAreaSpiritual = AppTheme.LifeAreaSpiritual

    // Pre-resolve localized tab names outside remember
    val vimsottariName = stringResource(DashaSystemType.VIMSOTTARI.displayNameKey)
    val yoginiName = stringResource(DashaSystemType.YOGINI.displayNameKey)
    val ashtottariName = stringResource(DashaSystemType.ASHTOTTARI.displayNameKey)
    val sudarshanaName = stringResource(DashaSystemType.SUDARSHANA.displayNameKey)
    val charaName = stringResource(DashaSystemType.CHARA.displayNameKey)

    val tabs = remember(accentPrimary, lifeAreaLove, accentGold, accentTeal, lifeAreaSpiritual, vimsottariName, yoginiName, ashtottariName, sudarshanaName, charaName) {
        listOf(
            TabItem(title = vimsottariName, accentColor = accentPrimary),
            TabItem(title = yoginiName, accentColor = lifeAreaLove),
            TabItem(title = ashtottariName, accentColor = accentGold),
            TabItem(title = sudarshanaName, accentColor = accentTeal),
            TabItem(title = charaName, accentColor = lifeAreaSpiritual)
        )
    }


    if (showInfoDialog) {
        DashaSystemInfoDialog(
            system = DashaSystemType.entries[selectedTabIndex],
            onDismiss = { showInfoDialog = false }
        )
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            DashaSystemsTopBar(
                chartName = chart?.birthData?.name ?: "",
                systemName = stringResource(DashaSystemType.entries[selectedTabIndex].displayNameKey),
                onBack = onBack,
                onInfoClick = { showInfoDialog = true },
                onJumpToToday = {
                    if (selectedTabIndex == 0) {
                        viewModel.requestScrollToToday()
                    }
                },
                showJumpToToday = selectedTabIndex == 0 && vimsottariState is DashaUiState.Success
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppTheme.ScreenBackground)
        ) {
            // Tab row
            ModernPillTabRow(
                tabs = tabs,
                selectedIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            // Content
            AnimatedContent(
                targetState = selectedTabIndex,
                transitionSpec = {
                    fadeIn(tween(300)) togetherWith fadeOut(tween(200))
                },
                label = "DashaTabContent"
            ) { tabIndex ->
                when (DashaSystemType.entries[tabIndex]) {
                    DashaSystemType.VIMSOTTARI -> {
                        VimsottariDashaContent(
                            uiState = vimsottariState,
                            viewModel = viewModel,
                            chart = chart
                        )
                    }
                    DashaSystemType.YOGINI -> {
                        NavigationPromptContent(
                            systemName = stringResource(StringKeyDosha.DASHA_TITLE_YOGINI),
                            description = stringResource(StringKeyDosha.DASHA_YOGINI_DESC),
                            onNavigate = onNavigateToYoginiDasha
                        )
                    }
                    DashaSystemType.ASHTOTTARI -> {
                        AshtottariDashaContent(data = ashtottariData)
                    }
                    DashaSystemType.SUDARSHANA -> {
                        SudarshanaChakraContent(data = sudarshanaData)
                    }
                    DashaSystemType.CHARA -> {
                        NavigationPromptContent(
                            systemName = stringResource(StringKeyDosha.DASHA_TITLE_CHARA),
                            description = stringResource(StringKeyDosha.DASHA_CHARA_DESC),
                            onNavigate = onNavigateToCharaDasha
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashaSystemsTopBar(
    chartName: String,
    systemName: String,
    onBack: () -> Unit,
    onInfoClick: () -> Unit,
    onJumpToToday: () -> Unit,
    showJumpToToday: Boolean
) {
    Surface(
        color = AppTheme.ScreenBackground,
        shadowElevation = 2.dp
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = stringResource(StringKey.FEATURE_DASHAS),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    if (chartName.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$systemName - $chartName",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(StringKey.BTN_BACK),
                        tint = AppTheme.TextPrimary
                    )
                }
            },
            actions = {
                if (showJumpToToday) {
                    IconButton(onClick = onJumpToToday) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = stringResource(StringKey.BTN_JUMP_TODAY),
                            tint = AppTheme.AccentPrimary
                        )
                    }
                }
                IconButton(onClick = onInfoClick) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = stringResource(StringKey.INFO_DASHA),
                        tint = AppTheme.TextPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = AppTheme.ScreenBackground
            )
        )
    }
}

@Composable
private fun VimsottariDashaContent(
    uiState: DashaUiState,
    viewModel: DashaViewModel,
    chart: VedicChart?
) {
    when (uiState) {
        is DashaUiState.Loading -> {
            LoadingContent(message = stringResource(StringKeyDosha.DASHA_CALC_LOADING, stringResource(StringKeyDosha.DASHA_VIMSOTTARI_NAME)))
        }
        is DashaUiState.Success -> {
            DashasTabContent(
                timeline = uiState.timeline,
                scrollToTodayEvent = viewModel.scrollToTodayEvent
            )
        }
        is DashaUiState.Error -> {
            ErrorContent(
                message = uiState.message,
                onRetry = { viewModel.loadDashaTimeline(chart) }
            )
        }
        is DashaUiState.Idle -> {
            EmptyContent(message = stringResource(StringKeyDosha.DASHA_NO_CHART))
        }
    }
}

@Composable
private fun AshtottariDashaContent(
    data: AshtottariTimeline?
) {
    if (data == null) {
        EmptyContent(message = stringResource(StringKeyDosha.DASHA_NOT_AVAILABLE, stringResource(StringKeyDosha.DASHA_ASHTOTTARI)))
        return
    }

    AshtottariDashaTabContent(timeline = data)
}

@Composable
private fun SudarshanaChakraContent(
    data: SudarshanaTimeline?
) {
    if (data == null) {
        EmptyContent(message = stringResource(StringKeyDosha.DASHA_NOT_AVAILABLE, stringResource(StringKeyDosha.DASHA_SUDARSHANA)))
        return
    }

    SudarshanaChakraTabContent(timeline = data)
}

@Composable
private fun NavigationPromptContent(
    systemName: String,
    description: String,
    onNavigate: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Timeline,
                contentDescription = null,
                tint = AppTheme.AccentPrimary,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = systemName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                onClick = onNavigate,
                color = AppTheme.AccentPrimary,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(StringKeyDosha.DASHA_VIEW_SYSTEM, systemName),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.ButtonText,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            androidx.compose.material3.CircularProgressIndicator(
                color = AppTheme.AccentPrimary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(StringKey.ERROR_CALCULATION),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.ErrorColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                onClick = onRetry,
                color = AppTheme.AccentPrimary,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(StringKeyDosha.AI_MODELS_RETRY),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.ButtonText,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyContent(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun DashaSystemInfoDialog(
    system: DashaSystemType,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(system.displayNameKey),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(system.descriptionKey),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyDosha.DASHA_CYCLE_DURATION, stringResource(system.cycleDurationKey)),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.AccentGold
                )
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(StringKeyDosha.BTN_GOT_IT),
                    color = AppTheme.AccentPrimary
                )
            }
        },
        containerColor = AppTheme.CardBackground,
        shape = RoundedCornerShape(16.dp)
    )
}
