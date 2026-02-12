package com.astro.storm.ui.screen.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.model.VedicChart
import com.astro.storm.data.preferences.ThemeManager
import com.astro.storm.data.repository.SavedChart
import com.astro.storm.ui.components.ProfileSwitcherBottomSheet
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.BorderSubtle
import com.astro.storm.ui.theme.CinzelDecorativeFontFamily
import com.astro.storm.ui.theme.CosmicIndigo
import com.astro.storm.ui.theme.Paper
import com.astro.storm.ui.theme.SpaceGroteskFontFamily
import com.astro.storm.ui.theme.Vellum
import com.astro.storm.ui.theme.VedicGold
import com.astro.storm.ui.viewmodel.ChartViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: ChartViewModel,
    themeManager: ThemeManager,
    savedCharts: List<SavedChart>,
    currentChart: VedicChart?,
    selectedChartId: Long?,
    onChartSelected: (Long) -> Unit,
    onAddNewChart: () -> Unit,
    onNavigateToChartAnalysis: (InsightFeature) -> Unit,
    onNavigateToMatchmaking: () -> Unit = {},
    onNavigateToMuhurta: () -> Unit = {},
    onNavigateToRemedies: () -> Unit = {},
    onNavigateToVarshaphala: () -> Unit = {},
    onNavigateToPrashna: (Long) -> Unit = {},
    onNavigateToBirthChart: () -> Unit = {},
    onNavigateToPlanets: () -> Unit = {},
    onNavigateToYogas: () -> Unit = {},
    onNavigateToDashas: () -> Unit = {},
    onNavigateToTransits: () -> Unit = {},
    onNavigateToAshtakavarga: () -> Unit = {},
    onNavigateToPanchanga: () -> Unit = {},
    onNavigateToProfileEdit: (Long) -> Unit = {},
    onNavigateToSynastry: () -> Unit = {},
    onNavigateToNakshatra: () -> Unit = {},
    onNavigateToShadbala: () -> Unit = {},
    onNavigateToShodashvarga: () -> Unit = {},
    onNavigateToYoginiDasha: () -> Unit = {},
    onNavigateToArgala: () -> Unit = {},
    onNavigateToCharaDasha: () -> Unit = {},
    onNavigateToBhriguBindu: () -> Unit = {},
    onNavigateToPredictions: () -> Unit = {},
    onNavigateToAshtottariDasha: () -> Unit = {},
    onNavigateToSudarshanaChakra: () -> Unit = {},
    onNavigateToMrityuBhaga: () -> Unit = {},
    onNavigateToLalKitab: () -> Unit = {},
    onNavigateToDivisionalCharts: () -> Unit = {},
    onNavigateToUpachayaTransit: () -> Unit = {},
    onNavigateToKalachakraDasha: () -> Unit = {},
    onNavigateToTarabala: () -> Unit = {},
    onNavigateToArudhaPada: () -> Unit = {},
    onNavigateToGrahaYuddha: () -> Unit = {},
    onNavigateToDashaSandhi: () -> Unit = {},
    onNavigateToGocharaVedha: () -> Unit = {},
    onNavigateToKemadrumaYoga: () -> Unit = {},
    onNavigateToPanchMahapurusha: () -> Unit = {},
    onNavigateToNityaYoga: () -> Unit = {},
    onNavigateToAvastha: () -> Unit = {},
    onNavigateToMaraka: () -> Unit = {},
    onNavigateToBadhaka: () -> Unit = {},
    onNavigateToVipareetaRajaYoga: () -> Unit = {},
    onNavigateToIshtaKashtaPhala: () -> Unit = {},
    onNavigateToShoolaDasha: () -> Unit = {},
    onNavigateToAshtavargaTransit: () -> Unit = {},
    onNavigateToSarvatobhadraChakra: () -> Unit = {},
    onNavigateToDrigBala: () -> Unit = {},
    onNavigateToSthanaBala: () -> Unit = {},
    onNavigateToKalaBala: () -> Unit = {},
    onNavigateToSaham: () -> Unit = {},
    onNavigateToNativeAnalysis: () -> Unit = {},
    onNavigateToKakshaTransit: (Long) -> Unit = {},
    onNavigateToNadiAmsha: (Long) -> Unit = {},
    onNavigateToJaiminiKaraka: () -> Unit = {},
    onNavigateToDrigDasha: () -> Unit = {},
    onNavigateToSaptamsa: () -> Unit = {},
    onExportChart: (ExportFormat) -> Unit
) {
    val scope = rememberCoroutineScope()
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.HOME) }
    var showProfileSwitcher by remember { mutableStateOf(false) }
    val profileSheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Vellum,
        topBar = {
            MainTopBar(
                currentChart = savedCharts.find { it.id == selectedChartId },
                onProfileClick = { showProfileSwitcher = true }
            )
        },
        bottomBar = {
            MainBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GrainTextureOverlay()

            Crossfade(targetState = selectedTab, label = "tab_fade") { tab ->
                when (tab) {
                    MainTab.HOME -> {
                        HomeTab(
                            chart = currentChart,
                            onAddNewChart = onAddNewChart,
                            onFeatureClick = { feature ->
                                when (feature) {
                                    InsightFeature.FULL_CHART -> onNavigateToBirthChart()
                                    InsightFeature.YOGAS -> onNavigateToYogas()
                                    InsightFeature.PREDICTIONS -> onNavigateToPredictions()
                                    InsightFeature.MATCHMAKING -> onNavigateToMatchmaking()
                                    InsightFeature.PANCHANGA -> onNavigateToPanchanga()
                                    InsightFeature.TRANSITS -> onNavigateToTransits()
                                    InsightFeature.PLANETS -> onNavigateToPlanets()
                                    InsightFeature.NAKSHATRA_ANALYSIS -> onNavigateToNakshatra()
                                    InsightFeature.DIVISIONAL_CHARTS -> onNavigateToDivisionalCharts()
                                    InsightFeature.SHODASHVARGA -> onNavigateToShodashvarga()
                                    InsightFeature.ASHTAKAVARGA -> onNavigateToAshtakavarga()
                                    InsightFeature.DASHAS -> onNavigateToDashas()
                                    InsightFeature.NATIVE_ANALYSIS -> onNavigateToNativeAnalysis()
                                }
                            }
                        )
                    }
                    MainTab.CHART -> {
                        if (currentChart != null) {
                            onNavigateToBirthChart()
                        }
                    }
                    MainTab.TRANSITS -> {
                        onNavigateToTransits()
                    }
                    MainTab.ORACLE -> {
                        onNavigateToPredictions()
                    }
                }
            }
        }
    }

    if (showProfileSwitcher) {
        ProfileSwitcherBottomSheet(
            savedCharts = savedCharts,
            selectedChartId = selectedChartId,
            onChartSelected = { chart ->
                onChartSelected(chart.id)
                scope.launch {
                    profileSheetState.hide()
                    showProfileSwitcher = false
                }
            },
            onAddNewChart = {
                scope.launch {
                    profileSheetState.hide()
                    showProfileSwitcher = false
                }
                onAddNewChart()
            },
            onEditChart = { chart ->
                scope.launch {
                    profileSheetState.hide()
                    showProfileSwitcher = false
                }
                onNavigateToProfileEdit(chart.id)
            },
            onDeleteChart = { chart ->
                viewModel.deleteChart(chart.id)
            },
            onDismiss = { showProfileSwitcher = false },
            sheetState = profileSheetState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainTopBar(
    currentChart: SavedChart?,
    onProfileClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "ASTROSTORM",
                style = MaterialTheme.typography.titleLarge,
                color = CosmicIndigo,
                fontFamily = CinzelDecorativeFontFamily,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        },
        actions = {
            Surface(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(32.dp)
                    .clickable { onProfileClick() },
                shape = CircleShape,
                color = Paper,
                border = BorderStroke(1.dp, VedicGold)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = currentChart?.name?.take(2)?.uppercase() ?: "NH",
                        style = MaterialTheme.typography.labelSmall,
                        color = CosmicIndigo,
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 10.sp
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Vellum.copy(alpha = 0.9f)
        )
    )
}

@Composable
private fun MainBottomNavigation(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    NavigationBar(
        containerColor = Vellum.copy(alpha = 0.95f),
        contentColor = CosmicIndigo,
        tonalElevation = 0.dp,
        modifier = Modifier.border(width = 0.5.dp, color = BorderSubtle, shape = RoundedCornerShape(0.dp))
    ) {
        MainTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = tab.name,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = tab.name,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = SpaceGroteskFontFamily,
                        letterSpacing = 1.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CosmicIndigo,
                    selectedTextColor = CosmicIndigo,
                    unselectedIconColor = CosmicIndigo.copy(alpha = 0.4f),
                    unselectedTextColor = CosmicIndigo.copy(alpha = 0.4f),
                    indicatorColor = VedicGold.copy(alpha = 0.1f)
                )
            )
        }
    }
}

enum class MainTab(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME(Icons.Filled.Home, Icons.Outlined.Home),
    CHART(Icons.Filled.Star, Icons.Outlined.Star),
    TRANSITS(Icons.Filled.Update, Icons.Outlined.Update),
    ORACLE(Icons.Filled.MenuBook, Icons.Outlined.MenuBook);
}
