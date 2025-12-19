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
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.StringKey
import com.astro.storm.data.localization.StringKeyInterface
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.model.VedicChart
import java.time.LocalDateTime
import com.astro.storm.data.model.Nakshatra
import com.astro.storm.data.model.Planet
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

enum class DashaSystemType(
    val displayNameKey: StringKeyInterface,
    val shortNameKey: StringKeyInterface,
    val cycleDurationKey: StringKeyInterface,
    val descriptionKey: StringKeyInterface
) {
    VIMSOTTARI(
        displayNameKey = StringKey.DASHA_SYSTEM_VIMSOTTARI,
        shortNameKey = StringKey.DASHA_SYSTEM_VIMSOTTARI_SHORT,
        cycleDurationKey = StringKey.DASHA_CYCLE_VIMSOTTARI,
        descriptionKey = StringKey.DASHA_DESC_VIMSOTTARI
    ),
    YOGINI(
        displayNameKey = StringKey.DASHA_SYSTEM_YOGINI,
        shortNameKey = StringKey.DASHA_SYSTEM_YOGINI_SHORT,
        cycleDurationKey = StringKey.DASHA_CYCLE_YOGINI,
        descriptionKey = StringKey.DASHA_DESC_YOGINI
    ),
    ASHTOTTARI(
        displayNameKey = StringKey.DASHA_SYSTEM_ASHTOTTARI,
        shortNameKey = StringKey.DASHA_SYSTEM_ASHTOTTARI_SHORT,
        cycleDurationKey = StringKey.DASHA_CYCLE_ASHTOTTARI,
        descriptionKey = StringKey.DASHA_DESC_ASHTOTTARI
    ),
    SUDARSHANA(
        displayNameKey = StringKey.DASHA_SYSTEM_SUDARSHANA,
        shortNameKey = StringKey.DASHA_SYSTEM_SUDARSHANA_SHORT,
        cycleDurationKey = StringKey.DASHA_CYCLE_SUDARSHANA,
        descriptionKey = StringKey.DASHA_DESC_SUDARSHANA
    ),
    CHARA(
        displayNameKey = StringKey.DASHA_SYSTEM_CHARA,
        shortNameKey = StringKey.DASHA_SYSTEM_CHARA_SHORT,
        cycleDurationKey = StringKey.DASHA_CYCLE_CHARA,
        descriptionKey = StringKey.DASHA_DESC_CHARA
    )
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
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val chartKey = remember(chart) {
        chart?.let {
            "${it.birthData.dateTime}|${it.birthData.latitude}|${it.birthData.longitude}"
        }
    }

    LaunchedEffect(chartKey) {
        viewModel.loadDashaTimeline(chart)
    }

    val vimsottariState by viewModel.uiState.collectAsStateWithLifecycle()

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

    val accentPrimary = AppTheme.AccentPrimary
    val lifeAreaLove = AppTheme.LifeAreaLove
    val accentGold = AppTheme.AccentGold
    val accentTeal = AppTheme.AccentTeal
    val lifeAreaSpiritual = AppTheme.LifeAreaSpiritual

    val tabs = remember(accentPrimary, lifeAreaLove, accentGold, accentTeal, lifeAreaSpiritual) {
        DashaSystemType.entries.map { system ->
            TabItem(
                title = stringResource(system.displayNameKey),
                accentColor = when (system) {
                    DashaSystemType.VIMSOTTARI -> accentPrimary
                    DashaSystemType.YOGINI -> lifeAreaLove
                    DashaSystemType.ASHTOTTARI -> accentGold
                    DashaSystemType.SUDARSHANA -> accentTeal
                    DashaSystemType.CHARA -> lifeAreaSpiritual
                }
            )
        }
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
            ModernPillTabRow(
                tabs = tabs,
                selectedIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

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
                            systemNameKey = StringKey.DASHA_SYSTEM_YOGINI,
                            descriptionKey = StringKey.YOGINI_PROMPT_DESC,
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
                            systemNameKey = StringKey.DASHA_SYSTEM_CHARA,
                            descriptionKey = StringKey.CHARA_PROMPT_DESC,
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
                            contentDescription = stringResource(StringKey.DASHA_JUMP_TO_TODAY),
                            tint = AppTheme.AccentPrimary
                        )
                    }
                }
                IconButton(onClick = onInfoClick) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = stringResource(StringKey.DASHA_INFO),
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
            LoadingContent(message = stringResource(StringKey.DASHA_CALCULATING_VIMSOTTARI))
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
            EmptyContent(message = stringResource(StringKey.DASHA_NO_CHART_MESSAGE))
        }
    }
}

@Composable
private fun AshtottariDashaContent(
    data: AshtottariTimeline?
) {
    if (data == null) {
        EmptyContent(message = stringResource(StringKey.DASHA_ASHTOTTARI_UNAVAILABLE))
        return
    }

    AshtottariDashaTabContent(timeline = data)
}

@Composable
private fun SudarshanaChakraContent(
    data: SudarshanaTimeline?
) {
    if (data == null) {
        EmptyContent(message = stringResource(StringKey.DASHA_SUDARSHANA_UNAVAILABLE))
        return
    }

    SudarshanaChakraTabContent(timeline = data)
}

@Composable
private fun NavigationPromptContent(
    systemNameKey: StringKeyInterface,
    descriptionKey: StringKeyInterface,
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
                text = stringResource(systemNameKey),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(descriptionKey),
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
                    text = stringResource(StringKey.DASHA_VIEW_SYSTEM, stringResource(systemNameKey)),
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
                text = stringResource(StringKey.DASHA_ERROR),
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
                    text = stringResource(StringKey.BTN_TRY_AGAIN),
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
                    text = stringResource(StringKey.DASHA_DIALOG_CYCLE_DURATION, stringResource(system.cycleDurationKey)),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.AccentGold
                )
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(StringKey.YOGA_GOT_IT),
                    color = AppTheme.AccentPrimary
                )
            }
        },
        containerColor = AppTheme.CardBackground,
        shape = RoundedCornerShape(16.dp)
    )
}
