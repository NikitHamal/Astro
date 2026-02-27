package com.astro.storm.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.yoga.Yoga
import com.astro.storm.ui.screen.ArgalaScreen
import com.astro.storm.ui.screen.ashtakavarga.AshtakavargaScreenRedesigned
import com.astro.storm.ui.screen.BhriguBinduScreen
import com.astro.storm.ui.screen.BirthChartScreen
import com.astro.storm.ui.screen.CharaDashaScreen
import com.astro.storm.ui.screen.ChartAnalysisScreen
import com.astro.storm.ui.screen.ChartInputScreen
import com.astro.storm.ui.screen.VimsottariDashaScreen
import com.astro.storm.ui.screen.matchmaking.MatchmakingScreen
import com.astro.storm.ui.screen.MuhurtaScreen
import com.astro.storm.ui.screen.NakshatraScreen
import com.astro.storm.ui.screen.panchanga.PanchangaScreenRedesigned
import com.astro.storm.ui.screen.PlanetsScreen
import com.astro.storm.ui.screen.NadiAmshaScreen
import com.astro.storm.ui.screen.PrashnaScreen
import com.astro.storm.ui.screen.PredictionsScreen
import com.astro.storm.ui.screen.RemediesScreen
import com.astro.storm.ui.screen.ShadbalaScreen
import com.astro.storm.ui.screen.ShodashvargaScreen
import com.astro.storm.ui.screen.SynastryScreen
import com.astro.storm.ui.screen.transits.TransitsScreenRedesigned
import com.astro.storm.ui.screen.VarshaphalaScreen
import com.astro.storm.ui.screen.yogas.YogasScreenRedesigned
import com.astro.storm.ui.screen.YoginiDashaScreen
import com.astro.storm.ui.screen.AshtottariDashaScreen
import com.astro.storm.ui.screen.SudarshanaChakraScreen
import com.astro.storm.ui.screen.MrityuBhagaScreen
import com.astro.storm.ui.screen.LalKitabRemediesScreen
import com.astro.storm.ui.screen.DivisionalChartsScreen
import com.astro.storm.ui.screen.UpachayaTransitScreen
import com.astro.storm.ui.screen.KalachakraDashaScreen
import com.astro.storm.ui.screen.tarabala.TarabalaScreen
import com.astro.storm.ui.screen.ArudhaPadaScreen
import com.astro.storm.ui.screen.GrahaYuddhaScreen
import com.astro.storm.ui.screen.DashaSandhiScreen
import com.astro.storm.ui.screen.GocharaVedhaScreen
import com.astro.storm.ui.screen.KemadrumaYogaScreen
import com.astro.storm.ui.screen.PanchMahapurushaScreen
import com.astro.storm.ui.screen.NityaYogaScreen
import com.astro.storm.ui.screen.AvasthaScreen
import com.astro.storm.ui.screen.MarakaScreen
import com.astro.storm.ui.screen.BadhakaScreen
import com.astro.storm.ui.screen.VipareetaRajaYogaScreen
import com.astro.storm.ui.screen.IshtaKashtaPhalaScreen
import com.astro.storm.ui.screen.ShoolaDashaScreen
import com.astro.storm.ui.screen.AshtavargaTransitScreen
import com.astro.storm.ui.screen.KakshaTransitScreen
import com.astro.storm.ui.screen.SarvatobhadraChakraScreen
import com.astro.storm.ui.screen.DrigBalaScreen
import com.astro.storm.ui.screen.SthanaBalaScreen
import com.astro.storm.ui.screen.KalaBalaScreen
import com.astro.storm.ui.screen.SahamScreen
import com.astro.storm.ui.screen.NativeAnalysisScreen
import com.astro.storm.ui.screen.JaiminiKarakaScreen
import com.astro.storm.ui.screen.DrigDashaScreen
import com.astro.storm.ui.screen.SaptamsaScreen
import com.astro.storm.ui.screen.main.ExportFormat
import com.astro.storm.ui.screen.main.InsightFeature
import com.astro.storm.ui.screen.main.MainScreen
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.ChartViewModel
import com.astro.storm.data.preferences.ThemeManager
import com.astro.storm.ephemeris.GocharaVedhaCalculator
import com.astro.storm.ephemeris.TarabalaCalculator
import com.astro.storm.ephemeris.TransitAnalyzer

/**
 * Navigation routes
 */
sealed class Screen(val route: String) {
    object Main : Screen("main")
    object ChartInput : Screen("chart_input")
    object ChartAnalysis : Screen("chart_analysis/{chartId}/{feature}") {
        fun createRoute(chartId: Long, feature: InsightFeature = InsightFeature.FULL_CHART) =
            "chart_analysis/$chartId/${feature.name}"
    }

    // New feature screens
    object Matchmaking : Screen("matchmaking")
    object Muhurta : Screen("muhurta")
    object Remedies : Screen("remedies/{chartId}") {
        fun createRoute(chartId: Long) = "remedies/$chartId"
    }
    object Varshaphala : Screen("varshaphala/{chartId}") {
        fun createRoute(chartId: Long) = "varshaphala/$chartId"
    }
    object Prashna : Screen("prashna")

    // New advanced feature screens
    object Synastry : Screen("synastry")
    object Nakshatra : Screen("nakshatra/{chartId}") {
        fun createRoute(chartId: Long) = "nakshatra/$chartId"
    }
    object Shadbala : Screen("shadbala/{chartId}") {
        fun createRoute(chartId: Long) = "shadbala/$chartId"
    }
    
    // New Shadbala & Chakra Screens
    object SarvatobhadraChakra : Screen("sarvatobhadra_chakra/{chartId}") {
        fun createRoute(chartId: Long) = "sarvatobhadra_chakra/$chartId"
    }
    object DrigBala : Screen("drig_bala/{chartId}") {
        fun createRoute(chartId: Long) = "drig_bala/$chartId"
    }
    object SthanaBala : Screen("sthana_bala/{chartId}") {
        fun createRoute(chartId: Long) = "sthana_bala/$chartId"
    }
    object KalaBala : Screen("kala_bala/{chartId}") {
        fun createRoute(chartId: Long) = "kala_bala/$chartId"
    }
    object Saham : Screen("saham/{chartId}") {
        fun createRoute(chartId: Long) = "saham/$chartId"
    }
    object NativeAnalysis : Screen("native_analysis/{chartId}") {
        fun createRoute(chartId: Long) = "native_analysis/$chartId"
    }

    // Individual chart analysis screens
    object BirthChart : Screen("birth_chart/{chartId}") {
        fun createRoute(chartId: Long) = "birth_chart/$chartId"
    }
    object Planets : Screen("planets/{chartId}") {
        fun createRoute(chartId: Long) = "planets/$chartId"
    }
    object Yogas : Screen("yogas/{chartId}") {
        fun createRoute(chartId: Long) = "yogas/$chartId"
    }
    object Dashas : Screen("dashas/{chartId}") {
        fun createRoute(chartId: Long) = "dashas/$chartId"
    }
    object Transits : Screen("transits/{chartId}") {
        fun createRoute(chartId: Long) = "transits/$chartId"
    }
    object Ashtakavarga : Screen("ashtakavarga/{chartId}") {
        fun createRoute(chartId: Long) = "ashtakavarga/$chartId"
    }
    object Panchanga : Screen("panchanga/{chartId}") {
        fun createRoute(chartId: Long) = "panchanga/$chartId"
    }
    object ProfileEdit : Screen("profile_edit/{chartId}") {
        fun createRoute(chartId: Long) = "profile_edit/$chartId"
    }

    // Advanced Calculator Screens
    object Shodashvarga : Screen("shodashvarga/{chartId}") {
        fun createRoute(chartId: Long) = "shodashvarga/$chartId"
    }
    object YoginiDasha : Screen("yogini_dasha/{chartId}") {
        fun createRoute(chartId: Long) = "yogini_dasha/$chartId"
    }
    object Argala : Screen("argala/{chartId}") {
        fun createRoute(chartId: Long) = "argala/$chartId"
    }
    object CharaDasha : Screen("chara_dasha/{chartId}") {
        fun createRoute(chartId: Long) = "chara_dasha/$chartId"
    }
    object BhriguBindu : Screen("bhrigu_bindu/{chartId}") {
        fun createRoute(chartId: Long) = "bhrigu_bindu/$chartId"
    }
    object Predictions : Screen("predictions/{chartId}") {
        fun createRoute(chartId: Long) = "predictions/$chartId"
    }

    // New Advanced Feature Screens
    object AshtottariDasha : Screen("ashtottari_dasha/{chartId}") {
        fun createRoute(chartId: Long) = "ashtottari_dasha/$chartId"
    }
    object SudarshanaChakra : Screen("sudarshana_chakra/{chartId}") {
        fun createRoute(chartId: Long) = "sudarshana_chakra/$chartId"
    }
    object MrityuBhaga : Screen("mrityu_bhaga/{chartId}") {
        fun createRoute(chartId: Long) = "mrityu_bhaga/$chartId"
    }
    object LalKitabRemedies : Screen("lal_kitab/{chartId}") {
        fun createRoute(chartId: Long) = "lal_kitab/$chartId"
    }
    object DivisionalCharts : Screen("divisional_charts/{chartId}") {
        fun createRoute(chartId: Long) = "divisional_charts/$chartId"
    }
    object UpachayaTransit : Screen("upachaya_transit/{chartId}") {
        fun createRoute(chartId: Long) = "upachaya_transit/$chartId"
    }
    object KalachakraDasha : Screen("kalachakra_dasha/{chartId}") {
        fun createRoute(chartId: Long) = "kalachakra_dasha/$chartId"
    }
    object Tarabala : Screen("tarabala/{chartId}") {
        fun createRoute(chartId: Long) = "tarabala/$chartId"
    }

    // Arudha Pada (Jaimini) screen
    object ArudhaPada : Screen("arudha_pada/{chartId}") {
        fun createRoute(chartId: Long) = "arudha_pada/$chartId"
    }

    // Graha Yuddha (Planetary War) screen
    object GrahaYuddha : Screen("graha_yuddha/{chartId}") {
        fun createRoute(chartId: Long) = "graha_yuddha/$chartId"
    }

    // Dasha Sandhi (Period Transitions) screen
    object DashaSandhi : Screen("dasha_sandhi/{chartId}") {
        fun createRoute(chartId: Long) = "dasha_sandhi/$chartId"
    }

    // Gochara Vedha (Transit Obstructions) screen
    object GocharaVedha : Screen("gochara_vedha/{chartId}") {
        fun createRoute(chartId: Long) = "gochara_vedha/$chartId"
    }

    // Kemadruma Yoga (Moon Isolation) screen
    object KemadrumaYoga : Screen("kemadruma_yoga/{chartId}") {
        fun createRoute(chartId: Long) = "kemadruma_yoga/$chartId"
    }

    // Panch Mahapurusha Yoga (Five Great Person Yogas) screen
    object PanchMahapurusha : Screen("panch_mahapurusha/{chartId}") {
        fun createRoute(chartId: Long) = "panch_mahapurusha/$chartId"
    }

    // Nitya Yoga (27 Daily Yogas) screen
    object NityaYoga : Screen("nitya_yoga/{chartId}") {
        fun createRoute(chartId: Long) = "nitya_yoga/$chartId"
    }

    // Avastha (Planetary States) screen
    object Avastha : Screen("avastha/{chartId}") {
        fun createRoute(chartId: Long) = "avastha/$chartId"
    }

    // Maraka (Death-Inflicting Planets) screen
    object Maraka : Screen("maraka/{chartId}") {
        fun createRoute(chartId: Long) = "maraka/$chartId"
    }

    // Badhaka (Obstructing Planets) screen
    object Badhaka : Screen("badhaka/{chartId}") {
        fun createRoute(chartId: Long) = "badhaka/$chartId"
    }

    // Vipareeta Raja Yoga (Reverse Raja Yogas) screen
    object VipareetaRajaYoga : Screen("vipareeta_raja_yoga/{chartId}") {
        fun createRoute(chartId: Long) = "vipareeta_raja_yoga/$chartId"
    }

    // Ishta Kashta Phala (Benefic/Malefic Results) screen
    object IshtaKashtaPhala : Screen("ishta_kashta_phala/{chartId}") {
        fun createRoute(chartId: Long) = "ishta_kashta_phala/$chartId"
    }

    // Shoola Dasha (Jaimini Health/Accident Timing) screen
    object ShoolaDasha : Screen("shoola_dasha/{chartId}") {
        fun createRoute(chartId: Long) = "shoola_dasha/$chartId"
    }

    // Ashtavarga Transit Predictions screen
    object AshtavargaTransit : Screen("ashtavarga_transit/{chartId}") {
        fun createRoute(chartId: Long) = "ashtavarga_transit/$chartId"
    }

    object NadiAmsha : Screen("nadi_amsha/{chartId}") {
        fun createRoute(chartId: Long) = "nadi_amsha/$chartId"
    }

    // Kakshya Transit (8-fold division) screen
    object KakshaTransit : Screen("kaksha_transit/{chartId}") {
        fun createRoute(chartId: Long) = "kaksha_transit/$chartId"
    }

    // Jaimini Karaka (Chara Karaka Analysis) screen
    object JaiminiKaraka : Screen("jaimini_karaka/{chartId}") {
        fun createRoute(chartId: Long) = "jaimini_karaka/$chartId"
    }

    // Drig Dasha (Jaimini Longevity system) screen
    object DrigDasha : Screen("drig_dasha/{chartId}") {
        fun createRoute(chartId: Long) = "drig_dasha/$chartId"
    }

    // Saptamsa (D7 Children/Progeny Analysis) screen
    object Saptamsa : Screen("saptamsa/{chartId}") {
        fun createRoute(chartId: Long) = "saptamsa/$chartId"
    }

}

/**
 * Main navigation graph - Redesigned
 *
 * The new navigation structure:
 * - Main: Primary screen with Home, Insights, Settings tabs
 * - ChartInput: Birth data input (unchanged)
 * - ChartAnalysis: Detailed chart analysis with horizontal tabs
 */
@Composable
fun AstroStormNavigation(
    navController: NavHostController,
    themeManager: ThemeManager,
    transitAnalyzer: TransitAnalyzer,
    vedhaCalculator: GocharaVedhaCalculator,
    tarabalaCalculator: TarabalaCalculator,
    viewModel: ChartViewModel = hiltViewModel()
) {
    val savedCharts by viewModel.savedCharts.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val density = LocalDensity.current
    val selectedChartId by viewModel.selectedChartId.collectAsState()

    var currentChart by remember { mutableStateOf<VedicChart?>(null) }

    // Update current chart from UI state
    LaunchedEffect(uiState) {
        if (uiState is com.astro.storm.ui.viewmodel.ChartUiState.Success) {
            currentChart = (uiState as com.astro.storm.ui.viewmodel.ChartUiState.Success).chart
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        // Main screen with bottom navigation
        composable(Screen.Main.route) {
            fun navigateToFeature(route: String) {
                navController.navigate(route)
            }

            fun navigateWithId(screen: Screen, chartId: Long? = selectedChartId) {
                chartId?.let { id ->
                    navController.navigate(screen.route.replace("{chartId}", id.toString()))
                }
            }

            MainScreen(
                viewModel = viewModel,
                themeManager = themeManager,
                savedCharts = savedCharts,
                currentChart = currentChart,
                selectedChartId = selectedChartId,
                onChartSelected = viewModel::loadChart,
                onAddNewChart = { navigateToFeature(Screen.ChartInput.route) },
                onNavigateToChartAnalysis = { feature ->
                    selectedChartId?.let { chartId ->
                        navController.navigate(Screen.ChartAnalysis.createRoute(chartId, feature))
                    }
                },
                onNavigateToMatchmaking = { navigateToFeature(Screen.Matchmaking.route) },
                onNavigateToMuhurta = { navigateToFeature(Screen.Muhurta.route) },
                onNavigateToRemedies = { navigateWithId(Screen.Remedies) },
                onNavigateToVarshaphala = { navigateWithId(Screen.Varshaphala) },
                onNavigateToPrashna = { navigateToFeature(Screen.Prashna.route) },
                onNavigateToBirthChart = { navigateWithId(Screen.BirthChart) },
                onNavigateToPlanets = { navigateWithId(Screen.Planets) },
                onNavigateToYogas = { navigateWithId(Screen.Yogas) },
                onNavigateToDashas = { navigateWithId(Screen.Dashas) },
                onNavigateToTransits = { navigateWithId(Screen.Transits) },
                onNavigateToAshtakavarga = { navigateWithId(Screen.Ashtakavarga) },
                onNavigateToPanchanga = { navigateWithId(Screen.Panchanga) },
                onNavigateToProfileEdit = { chartId -> navigateWithId(Screen.ProfileEdit, chartId) },
                onNavigateToKakshaTransit = { chartId -> navigateWithId(Screen.KakshaTransit, chartId) },
                onNavigateToNadiAmsha = { chartId -> navigateWithId(Screen.NadiAmsha, chartId) },
                onNavigateToSynastry = { navigateToFeature(Screen.Synastry.route) },
                onNavigateToNakshatra = { navigateWithId(Screen.Nakshatra) },
                onNavigateToShadbala = { navigateWithId(Screen.Shadbala) },
                onNavigateToShodashvarga = { navigateWithId(Screen.Shodashvarga) },
                onNavigateToYoginiDasha = { navigateWithId(Screen.YoginiDasha) },
                onNavigateToArgala = { navigateWithId(Screen.Argala) },
                onNavigateToCharaDasha = { navigateWithId(Screen.CharaDasha) },
                onNavigateToBhriguBindu = { navigateWithId(Screen.BhriguBindu) },
                onNavigateToPredictions = { navigateWithId(Screen.Predictions) },
                onNavigateToAshtottariDasha = { navigateWithId(Screen.AshtottariDasha) },
                onNavigateToSudarshanaChakra = { navigateWithId(Screen.SudarshanaChakra) },
                onNavigateToMrityuBhaga = { navigateWithId(Screen.MrityuBhaga) },
                onNavigateToLalKitab = { navigateWithId(Screen.LalKitabRemedies) },
                onNavigateToDivisionalCharts = { navigateWithId(Screen.DivisionalCharts) },
                onNavigateToUpachayaTransit = { navigateWithId(Screen.UpachayaTransit) },
                onNavigateToKalachakraDasha = { navigateWithId(Screen.KalachakraDasha) },
                onNavigateToTarabala = { navigateWithId(Screen.Tarabala) },
                onNavigateToArudhaPada = { navigateWithId(Screen.ArudhaPada) },
                onNavigateToGrahaYuddha = { navigateWithId(Screen.GrahaYuddha) },
                onNavigateToDashaSandhi = { navigateWithId(Screen.DashaSandhi) },
                onNavigateToGocharaVedha = { navigateWithId(Screen.GocharaVedha) },
                onNavigateToKemadrumaYoga = { navigateWithId(Screen.KemadrumaYoga) },
                onNavigateToPanchMahapurusha = { navigateWithId(Screen.PanchMahapurusha) },
                onNavigateToNityaYoga = { navigateWithId(Screen.NityaYoga) },
                onNavigateToAvastha = { navigateWithId(Screen.Avastha) },
                onNavigateToMaraka = { navigateWithId(Screen.Maraka) },
                onNavigateToBadhaka = { navigateWithId(Screen.Badhaka) },
                onNavigateToVipareetaRajaYoga = { navigateWithId(Screen.VipareetaRajaYoga) },
                onNavigateToIshtaKashtaPhala = { navigateWithId(Screen.IshtaKashtaPhala) },
                onNavigateToShoolaDasha = { navigateWithId(Screen.ShoolaDasha) },
                onNavigateToAshtavargaTransit = { navigateWithId(Screen.AshtavargaTransit) },
                onNavigateToSarvatobhadraChakra = { navigateWithId(Screen.SarvatobhadraChakra) },
                onNavigateToDrigBala = { navigateWithId(Screen.DrigBala) },
                onNavigateToSthanaBala = { navigateWithId(Screen.SthanaBala) },
                onNavigateToKalaBala = { navigateWithId(Screen.KalaBala) },
                onNavigateToSaham = { navigateWithId(Screen.Saham) },
                onNavigateToNativeAnalysis = { navigateWithId(Screen.NativeAnalysis) },
                onNavigateToJaiminiKaraka = { navigateWithId(Screen.JaiminiKaraka) },
                onNavigateToDrigDasha = { navigateWithId(Screen.DrigDasha) },
                onNavigateToSaptamsa = { navigateWithId(Screen.Saptamsa) },
                onExportChart = { format ->
                    currentChart?.let { chart ->
                        when (format) {
                            ExportFormat.PDF -> viewModel.exportChartToPdf(chart, density)
                            ExportFormat.IMAGE -> viewModel.exportChartToImage(chart, density)
                            ExportFormat.JSON -> viewModel.exportChartToJson(chart)
                            ExportFormat.CSV -> viewModel.exportChartToCsv(chart)
                            ExportFormat.TEXT -> viewModel.exportChartToText(chart)
                        }
                    }
                }
            )
        }

        // Chart input screen (unchanged functionality, same screen)
        composable(Screen.ChartInput.route) {
            ChartInputScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    viewModel.resetState()
                    navController.popBackStack()
                },
                onChartCalculated = {
                    // After chart is saved, navigate back and it will auto-select
                    viewModel.resetState()
                    navController.popBackStack()
                }
            )
        }

        // Chart analysis screen with feature parameter
        composable(
            route = Screen.ChartAnalysis.route,
            arguments = listOf(
                navArgument("chartId") { type = NavType.LongType },
                navArgument("feature") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable
            val featureName = backStackEntry.arguments?.getString("feature") ?: InsightFeature.FULL_CHART.name
            val feature = try {
                InsightFeature.valueOf(featureName)
            } catch (e: Exception) {
                InsightFeature.FULL_CHART
            }

            // Load chart if not already loaded
            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            currentChart?.let { chart ->
                ChartAnalysisScreen(
                    chart = chart,
                    initialFeature = feature,
                    viewModel = viewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onNavigateToDetailedYoga = { yoga ->
                        when {
                            yoga.detailedResult is com.astro.storm.ephemeris.KemadrumaYogaCalculator.KemadrumaAnalysis -> {
                                navController.navigate(Screen.KemadrumaYoga.createRoute(chartId))
                            }
                            yoga.detailedResult is com.astro.storm.ephemeris.PanchMahapurushaYogaCalculator.MahapurushaYoga -> {
                                navController.navigate(Screen.PanchMahapurusha.createRoute(chartId))
                            }
                            yoga.detailedResult is com.astro.storm.ephemeris.VipareetaRajaYogaCalculator.VipareetaYoga -> {
                                navController.navigate(Screen.VipareetaRajaYoga.createRoute(chartId))
                            }
                        }
                    }
                )
            }
        }

        // Matchmaking screen
        composable(Screen.Matchmaking.route) {
            MatchmakingScreen(
                savedCharts = savedCharts,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Muhurta screen
        composable(Screen.Muhurta.route) {
            MuhurtaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Remedies screen
        composable(
            route = Screen.Remedies.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            RemediesScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Varshaphala screen
        composable(
            route = Screen.Varshaphala.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            VarshaphalaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Prashna (Horary) screen
        composable(Screen.Prashna.route) {
            PrashnaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Birth Chart screen
        composable(
            route = Screen.BirthChart.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            // Use theme-aware chart renderer
            val isDarkTheme = AppTheme.current.isDark
            val themeAwareChartRenderer = remember(isDarkTheme) {
                viewModel.getChartRenderer(isDarkTheme)
            }

            BirthChartScreen(
                chart = currentChart,
                chartRenderer = themeAwareChartRenderer,
                onBack = { navController.popBackStack() }
            )
        }

        // Planets screen
        composable(
            route = Screen.Planets.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            PlanetsScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Yogas screen
        composable(
            route = Screen.Yogas.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            YogasScreenRedesigned(
                chart = currentChart,
                onBack = { navController.popBackStack() },
                onNavigateToDetailedYoga = { yoga ->
                    val chartIdLong = backStackEntry.arguments?.getLong("chartId")
                    chartIdLong?.let { id ->
                        when {
                            yoga.detailedResult is com.astro.storm.ephemeris.KemadrumaYogaCalculator.KemadrumaAnalysis -> {
                                navController.navigate(Screen.KemadrumaYoga.createRoute(id))
                            }
                            yoga.detailedResult is com.astro.storm.ephemeris.PanchMahapurushaYogaCalculator.MahapurushaYoga -> {
                                navController.navigate(Screen.PanchMahapurusha.createRoute(id))
                            }
                            yoga.detailedResult is com.astro.storm.ephemeris.VipareetaRajaYogaCalculator.VipareetaYoga -> {
                                navController.navigate(Screen.VipareetaRajaYoga.createRoute(id))
                            }
                        }
                    }
                }
            )
        }

        // Dashas screen
        composable(
            route = Screen.Dashas.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            VimsottariDashaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Transits screen
        composable(
            route = Screen.Transits.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            TransitsScreenRedesigned(
                chart = currentChart,
                transitAnalyzer = transitAnalyzer,
                onBack = { navController.popBackStack() }
            )
        }

        // Ashtakavarga screen
        composable(
            route = Screen.Ashtakavarga.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            AshtakavargaScreenRedesigned(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Panchanga screen
        composable(
            route = Screen.Panchanga.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            PanchangaScreenRedesigned(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Profile Edit screen - uses ChartInputScreen in edit mode
        composable(
            route = Screen.ProfileEdit.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            ChartInputScreen(
                viewModel = viewModel,
                editChartId = chartId,
                onNavigateBack = {
                    viewModel.resetState()
                    navController.popBackStack()
                },
                onChartCalculated = {
                    viewModel.resetState()
                    navController.popBackStack()
                }
            )
        }

        // Synastry (Chart Comparison) screen
        composable(Screen.Synastry.route) {
            SynastryScreen(
                savedCharts = savedCharts,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Nakshatra Analysis screen
        composable(
            route = Screen.Nakshatra.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            NakshatraScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Shadbala Analysis screen
        composable(
            route = Screen.Shadbala.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            ShadbalaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Shodashvarga (16-divisional charts) screen
        composable(
            route = Screen.Shodashvarga.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            ShodashvargaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Yogini Dasha screen
        composable(
            route = Screen.YoginiDasha.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            YoginiDashaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Argala (Jaimini Intervention) screen
        composable(
            route = Screen.Argala.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            ArgalaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Chara Dasha (Jaimini Sign-based) screen
        composable(
            route = Screen.CharaDasha.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            CharaDashaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Bhrigu Bindu (Karmic Destiny Point) screen
        composable(
            route = Screen.BhriguBindu.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            BhriguBinduScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Predictions (Comprehensive Life Analysis) screen
        composable(
            route = Screen.Predictions.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            PredictionsScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Ashtottari Dasha screen
        composable(
            route = Screen.AshtottariDasha.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            AshtottariDashaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Sudarshana Chakra Dasha screen
        composable(
            route = Screen.SudarshanaChakra.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            SudarshanaChakraScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Mrityu Bhaga Analysis screen
        composable(
            route = Screen.MrityuBhaga.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            MrityuBhagaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Lal Kitab Remedies screen
        composable(
            route = Screen.LalKitabRemedies.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            LalKitabRemediesScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Divisional Charts Analysis screen
        composable(
            route = Screen.DivisionalCharts.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            // Use theme-aware chart renderer
            val isDarkTheme = AppTheme.current.isDark
            val themeAwareChartRenderer = remember(isDarkTheme) {
                viewModel.getChartRenderer(isDarkTheme)
            }

            DivisionalChartsScreen(
                chart = currentChart,
                chartRenderer = themeAwareChartRenderer,
                onBack = { navController.popBackStack() }
            )
        }

        // Upachaya Transit screen
        composable(
            route = Screen.UpachayaTransit.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            UpachayaTransitScreen(
                chart = currentChart,
                transitPositions = currentChart?.planetPositions ?: emptyList(),
                onBack = { navController.popBackStack() }
            )
        }

        // Kalachakra Dasha screen
        composable(
            route = Screen.KalachakraDasha.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            KalachakraDashaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Tarabala & Chandrabala Analysis screen
        composable(
            route = Screen.Tarabala.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            TarabalaScreen(
                chart = currentChart,
                tarabalaCalculator = tarabalaCalculator,
                onBack = { navController.popBackStack() }
            )
        }

        // Arudha Pada (Jaimini) screen
        composable(
            route = Screen.ArudhaPada.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            ArudhaPadaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Graha Yuddha (Planetary War) screen
        composable(
            route = Screen.GrahaYuddha.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            GrahaYuddhaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Dasha Sandhi (Period Transitions) screen
        composable(
            route = Screen.DashaSandhi.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            DashaSandhiScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Gochara Vedha (Transit Obstructions) screen
        composable(
            route = Screen.GocharaVedha.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            GocharaVedhaScreen(
                chart = currentChart,
                vedhaCalculator = vedhaCalculator,
                onBack = { navController.popBackStack() }
            )
        }

        // Kemadruma Yoga (Moon Isolation) screen
        composable(
            route = Screen.KemadrumaYoga.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            KemadrumaYogaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Panch Mahapurusha Yoga (Five Great Person Yogas) screen
        composable(
            route = Screen.PanchMahapurusha.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            PanchMahapurushaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Nitya Yoga (27 Daily Yogas) screen
        composable(
            route = Screen.NityaYoga.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            NityaYogaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Avastha (Planetary States) screen
        composable(
            route = Screen.Avastha.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            AvasthaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Maraka (Death-Inflicting Planets) screen
        composable(
            route = Screen.Maraka.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            MarakaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Badhaka (Obstructing Planets) screen
        composable(
            route = Screen.Badhaka.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            BadhakaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Vipareeta Raja Yoga (Reverse Raja Yogas) screen
        composable(
            route = Screen.VipareetaRajaYoga.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            VipareetaRajaYogaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Ishta Kashta Phala (Benefic/Malefic Results) screen
        composable(
            route = Screen.IshtaKashtaPhala.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            IshtaKashtaPhalaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Shoola Dasha (Jaimini Health/Accident Timing) screen
        composable(
            route = Screen.ShoolaDasha.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            ShoolaDashaScreen(
                chart = currentChart,
                onBack = { navController.popBackStack() }
            )
        }

        // Ashtavarga Transit Predictions screen
        composable(
            route = Screen.AshtavargaTransit.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable

            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }

            AshtavargaTransitScreen(
                chart = currentChart,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Kakshya Transit screen
        composable(
            route = Screen.KakshaTransit.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable
            var chart by remember { mutableStateOf<VedicChart?>(null) }
            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }
            KakshaTransitScreen(chart = currentChart, onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.NadiAmsha.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable
            var chart by remember { mutableStateOf<VedicChart?>(null) }
            LaunchedEffect(chartId) {
                if (selectedChartId != chartId) {
                    viewModel.loadChart(chartId)
                }
            }
            NadiAmshaScreen(chart = currentChart, onBack = { navController.popBackStack() })
        }

        // ===================================
        // NEW SHADBALA SCREENS
        // ===================================

        // Sarvatobhadra Chakra screen
        composable(
            route = Screen.SarvatobhadraChakra.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable
            LaunchedEffect(chartId) { if (selectedChartId != chartId) viewModel.loadChart(chartId) }
            SarvatobhadraChakraScreen(chart = currentChart, onBack = { navController.popBackStack() })
        }

        // Drig Bala screen
        composable(
            route = Screen.DrigBala.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable
            LaunchedEffect(chartId) { if (selectedChartId != chartId) viewModel.loadChart(chartId) }
            DrigBalaScreen(chart = currentChart, onBack = { navController.popBackStack() })
        }

        // Sthana Bala screen
        composable(
            route = Screen.SthanaBala.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable
            LaunchedEffect(chartId) { if (selectedChartId != chartId) viewModel.loadChart(chartId) }
            SthanaBalaScreen(chart = currentChart, onBack = { navController.popBackStack() })
        }

        // Kala Bala screen
        composable(
            route = Screen.KalaBala.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable
            LaunchedEffect(chartId) { if (selectedChartId != chartId) viewModel.loadChart(chartId) }
            KalaBalaScreen(chart = currentChart, onBack = { navController.popBackStack() })
        }

        // Saham (Arabic Parts) screen
        composable(
            route = Screen.Saham.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable
            LaunchedEffect(chartId) { if (selectedChartId != chartId) viewModel.loadChart(chartId) }
            SahamScreen(chart = currentChart, onBack = { navController.popBackStack() })
        }

        // Native Analysis (Comprehensive Profile) screen
        composable(
            route = Screen.NativeAnalysis.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable
            LaunchedEffect(chartId) { if (selectedChartId != chartId) viewModel.loadChart(chartId) }
            NativeAnalysisScreen(chart = currentChart, onBack = { navController.popBackStack() })
        }

        // Jaimini Karaka (Chara Karaka Analysis) screen
        composable(
            route = Screen.JaiminiKaraka.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable
            LaunchedEffect(chartId) { if (selectedChartId != chartId) viewModel.loadChart(chartId) }
            JaiminiKarakaScreen(chart = currentChart, onBack = { navController.popBackStack() })
        }

        // Drig Dasha (Jaimini Longevity system) screen
        composable(
            route = Screen.DrigDasha.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable
            LaunchedEffect(chartId) { if (selectedChartId != chartId) viewModel.loadChart(chartId) }
            DrigDashaScreen(chart = currentChart, onBack = { navController.popBackStack() })
        }

        // Saptamsa (D7 Children/Progeny Analysis) screen
        composable(
            route = Screen.Saptamsa.route,
            arguments = listOf(navArgument("chartId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getLong("chartId") ?: return@composable
            LaunchedEffect(chartId) { if (selectedChartId != chartId) viewModel.loadChart(chartId) }
            SaptamsaScreen(chart = currentChart, onBack = { navController.popBackStack() })
        }

    }
}
