package com.astro.storm.ui.screen.main

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import com.astro.storm.ui.components.ScreenTopBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.VedicChart
import com.astro.storm.data.repository.SavedChart
import com.astro.storm.data.preferences.ThemeManager
import com.astro.storm.ui.components.ProfileHeaderRow
import com.astro.storm.ui.components.ProfileSwitcherBottomSheet
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.CinzelDecorativeFamily
import com.astro.storm.ui.theme.NeoVedicTokens
import com.astro.storm.ui.theme.SpaceGroteskFamily
import com.astro.storm.ui.viewmodel.ChartUiState
import com.astro.storm.ui.viewmodel.ChartViewModel
import kotlinx.coroutines.launch

/**
 * Main Screen with Bottom Navigation
 *
 * Neo-Vedic "Ethereal Vedic Grid" design:
 * - Flat parchment top bar with ASTROSTORM logotype in Cinzel Decorative
 * - Minimal bottom navigation with hairline top border
 * - Space Grotesk labels, Vedic Gold active indicator
 */
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
    val language = LocalLanguage.current
    val colors = AppTheme.current

    // Observe UI state for export feedback
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Track the last export state for proper snackbar styling
    var lastExportState by remember { mutableStateOf<ChartUiState?>(null) }

    // Show snackbar for export states
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is ChartUiState.Exporting -> {
                lastExportState = state
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Indefinite
                )
            }
            is ChartUiState.Exported -> {
                lastExportState = state
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Short
                )
                kotlinx.coroutines.delay(100)
                viewModel.resetState()
            }
            is ChartUiState.Error -> {
                lastExportState = state
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Long
                )
            }
            else -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                if (uiState is ChartUiState.Success || uiState is ChartUiState.Initial) {
                    lastExportState = null
                }
            }
        }
    }

    // Find the current SavedChart for display
    val currentSavedChart = savedCharts.find { it.id == selectedChartId }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                val effectiveState = lastExportState ?: uiState
                Snackbar(
                    snackbarData = data,
                    containerColor = when (effectiveState) {
                        is ChartUiState.Error -> colors.ErrorColor
                        is ChartUiState.Exported -> colors.SuccessColor
                        else -> colors.CardBackground
                    },
                    contentColor = when (effectiveState) {
                        is ChartUiState.Error, is ChartUiState.Exported -> colors.TextPrimary
                        else -> colors.TextSecondary
                    },
                    actionColor = colors.AccentGold
                )
            }
        },
        containerColor = colors.ScreenBackground,
        topBar = {
            NeoVedicTopBar(
                currentChart = currentSavedChart,
                onProfileClick = { showProfileSwitcher = true }
            )
        },
        bottomBar = {
            NeoVedicBottomNavigation(
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
            Crossfade(
                targetState = selectedTab,
                label = "tab_crossfade"
            ) { tab ->
                when (tab) {
                    MainTab.HOME -> {
                        HomeTab(
                            chart = currentChart,
                            onAddNewChart = onAddNewChart,
                            onFeatureClick = { feature ->
                                if (feature.isImplemented) {
                                    when (feature) {
                                        InsightFeature.MATCHMAKING -> onNavigateToMatchmaking()
                                        InsightFeature.MUHURTA -> onNavigateToMuhurta()
                                        InsightFeature.PRASHNA -> selectedChartId?.let { onNavigateToPrashna(it) } ?: onNavigateToPrashna(0L)
                                        InsightFeature.CHART_COMPARISON -> onNavigateToSynastry()
                                        InsightFeature.KAKSHYA_TRANSIT -> selectedChartId?.let { onNavigateToKakshaTransit(it) }
                                        InsightFeature.NADI_AMSHA -> selectedChartId?.let { onNavigateToNadiAmsha(it) }
                                        InsightFeature.FULL_CHART -> if (currentChart != null) onNavigateToBirthChart()
                                        InsightFeature.PLANETS -> if (currentChart != null) onNavigateToPlanets()
                                        InsightFeature.YOGAS -> if (currentChart != null) onNavigateToYogas()
                                        InsightFeature.DASHAS -> if (currentChart != null) onNavigateToDashas()
                                        InsightFeature.TRANSITS -> if (currentChart != null) onNavigateToTransits()
                                        InsightFeature.ASHTAKAVARGA -> if (currentChart != null) onNavigateToAshtakavarga()
                                        InsightFeature.PANCHANGA -> if (currentChart != null) onNavigateToPanchanga()
                                        InsightFeature.REMEDIES -> if (currentChart != null) onNavigateToRemedies()
                                        InsightFeature.VARSHAPHALA -> if (currentChart != null) onNavigateToVarshaphala()
                                        InsightFeature.NAKSHATRA_ANALYSIS -> if (currentChart != null) onNavigateToNakshatra()
                                        InsightFeature.SHADBALA -> if (currentChart != null) onNavigateToShadbala()
                                        InsightFeature.SHODASHVARGA -> if (currentChart != null) onNavigateToShodashvarga()
                                        InsightFeature.YOGINI_DASHA -> if (currentChart != null) onNavigateToYoginiDasha()
                                        InsightFeature.ARGALA -> if (currentChart != null) onNavigateToArgala()
                                        InsightFeature.CHARA_DASHA -> if (currentChart != null) onNavigateToCharaDasha()
                                        InsightFeature.BHRIGU_BINDU -> if (currentChart != null) onNavigateToBhriguBindu()
                                        InsightFeature.PREDICTIONS -> if (currentChart != null) onNavigateToPredictions()
                                        InsightFeature.ASHTOTTARI_DASHA -> if (currentChart != null) onNavigateToAshtottariDasha()
                                        InsightFeature.SUDARSHANA_CHAKRA -> if (currentChart != null) onNavigateToSudarshanaChakra()
                                        InsightFeature.MRITYU_BHAGA -> if (currentChart != null) onNavigateToMrityuBhaga()
                                        InsightFeature.LAL_KITAB -> if (currentChart != null) onNavigateToLalKitab()
                                        InsightFeature.DIVISIONAL_CHARTS -> if (currentChart != null) onNavigateToDivisionalCharts()
                                        InsightFeature.UPACHAYA_TRANSIT -> if (currentChart != null) onNavigateToUpachayaTransit()
                                        InsightFeature.KALACHAKRA_DASHA -> if (currentChart != null) onNavigateToKalachakraDasha()
                                        InsightFeature.TARABALA -> if (currentChart != null) onNavigateToTarabala()
                                        InsightFeature.ARUDHA_PADA -> if (currentChart != null) onNavigateToArudhaPada()
                                        InsightFeature.GRAHA_YUDDHA -> if (currentChart != null) onNavigateToGrahaYuddha()
                                        InsightFeature.DASHA_SANDHI -> if (currentChart != null) onNavigateToDashaSandhi()
                                        InsightFeature.GOCHARA_VEDHA -> if (currentChart != null) onNavigateToGocharaVedha()
                                        InsightFeature.KEMADRUMA_YOGA -> if (currentChart != null) onNavigateToKemadrumaYoga()
                                        InsightFeature.PANCH_MAHAPURUSHA -> if (currentChart != null) onNavigateToPanchMahapurusha()
                                        InsightFeature.NITYA_YOGA -> if (currentChart != null) onNavigateToNityaYoga()
                                        InsightFeature.AVASTHA -> if (currentChart != null) onNavigateToAvastha()
                                        InsightFeature.MARAKA -> if (currentChart != null) onNavigateToMaraka()
                                        InsightFeature.BADHAKA -> if (currentChart != null) onNavigateToBadhaka()
                                        InsightFeature.VIPAREETA_RAJA_YOGA -> if (currentChart != null) onNavigateToVipareetaRajaYoga()
                                        InsightFeature.ISHTA_KASHTA_PHALA -> if (currentChart != null) onNavigateToIshtaKashtaPhala()
                                        InsightFeature.SHOOLA_DASHA -> if (currentChart != null) onNavigateToShoolaDasha()
                                        InsightFeature.ASHTAVARGA_TRANSIT -> if (currentChart != null) onNavigateToAshtavargaTransit()
                                        InsightFeature.SARVATOBHADRA_CHAKRA -> if (currentChart != null) onNavigateToSarvatobhadraChakra()
                                        InsightFeature.DRIG_BALA -> if (currentChart != null) onNavigateToDrigBala()
                                        InsightFeature.STHANA_BALA -> if (currentChart != null) onNavigateToSthanaBala()
                                        InsightFeature.KALA_BALA -> if (currentChart != null) onNavigateToKalaBala()
                                        InsightFeature.SAHAM -> if (currentChart != null) onNavigateToSaham()
                                        InsightFeature.NATIVE_ANALYSIS -> if (currentChart != null) onNavigateToNativeAnalysis()
                                        InsightFeature.JAIMINI_KARAKA -> if (currentChart != null) onNavigateToJaiminiKaraka()
                                        InsightFeature.DRIG_DASHA -> if (currentChart != null) onNavigateToDrigDasha()
                                        InsightFeature.SAPTAMSA -> if (currentChart != null) onNavigateToSaptamsa()
                                        else -> if (currentChart != null) onNavigateToChartAnalysis(feature)
                                    }
                                }
                            }
                        )
                    }
                    MainTab.INSIGHTS -> {
                        InsightsTab(
                            chart = currentChart,
                            onCreateChart = onAddNewChart
                        )
                    }
                    MainTab.SETTINGS -> {
                        SettingsTab(
                            currentChart = currentChart,
                            savedCharts = savedCharts,
                            selectedChartId = selectedChartId,
                            themeManager = themeManager,
                            onEditProfile = onNavigateToProfileEdit,
                            onDeleteProfile = { chartId ->
                                viewModel.deleteChart(chartId)
                            },
                            onExportChart = onExportChart,
                            onManageProfiles = { showProfileSwitcher = true }
                        )
                    }
                }
            }
        }
    }

    // Profile Switcher Bottom Sheet
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

// ============================================================================
// NEO-VEDIC TOP BAR
// Flat parchment surface with ASTROSTORM logotype + profile switcher
// Hairline bottom border, no elevation shadows
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NeoVedicTopBar(
    currentChart: SavedChart?,
    onProfileClick: () -> Unit
) {
    val colors = AppTheme.current
    val borderColor = colors.BorderColor

    Surface(
        color = colors.ScreenBackground,
        tonalElevation = NeoVedicTokens.SurfaceElevation,
        shadowElevation = NeoVedicTokens.SurfaceElevation,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                // Hairline bottom border
                drawLine(
                    color = borderColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = NeoVedicTokens.BorderWidth.toPx()
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = NeoVedicTokens.ScreenPadding, vertical = NeoVedicTokens.SpaceSM + NeoVedicTokens.SpaceXXS / 2),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // ASTROSTORM logotype - Cinzel Decorative
            Text(
                text = "ASTROSTORM",
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = 2.sp,
                color = colors.TextPrimary
            )

            // Profile switcher
            ProfileHeaderRow(
                currentChart = currentChart,
                onProfileClick = onProfileClick
            )
        }
    }
}

// ============================================================================
// NEO-VEDIC BOTTOM NAVIGATION
// Flat surface, hairline top border, Space Grotesk labels
// Vedic Gold active indicator, no rounded corners
// ============================================================================

@Composable
private fun NeoVedicBottomNavigation(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    val language = LocalLanguage.current
    val colors = AppTheme.current
    val borderColor = colors.BorderColor

    NavigationBar(
        containerColor = colors.NavBarBackground,
        contentColor = colors.TextPrimary,
        tonalElevation = NeoVedicTokens.SurfaceElevation,
        modifier = Modifier
            .drawBehind {
                // Hairline top border
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = NeoVedicTokens.BorderWidth.toPx()
                )
            }
    ) {
        MainTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            val localizedTitle = tab.getLocalizedTitle(language)

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = localizedTitle,
                        modifier = Modifier.size(NeoVedicTokens.SpaceLG - NeoVedicTokens.SpaceXXS / 2)
                    )
                },
                label = {
                    Text(
                        text = localizedTitle.uppercase(),
                        fontFamily = SpaceGroteskFamily,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 10.sp,
                        letterSpacing = 1.5.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.NavItemSelected,
                    selectedTextColor = colors.NavItemSelected,
                    unselectedIconColor = colors.NavItemUnselected,
                    unselectedTextColor = colors.NavItemUnselected,
                    indicatorColor = colors.NavIndicator
                )
            )
        }
    }
}

/**
 * Main navigation tabs
 */
enum class MainTab(
    val titleKey: StringKey,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME(
        titleKey = StringKey.TAB_HOME,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    INSIGHTS(
        titleKey = StringKey.TAB_INSIGHTS,
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome
    ),
    SETTINGS(
        titleKey = StringKey.TAB_SETTINGS,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    );

    fun getLocalizedTitle(language: com.astro.storm.core.common.Language): String {
        return StringResources.get(titleKey, language)
    }
}


