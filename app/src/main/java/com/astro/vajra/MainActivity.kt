package com.astro.vajra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.astro.vajra.data.localization.LocalizationManager
import com.astro.vajra.data.localization.LocalizationProvider
import com.astro.vajra.data.preferences.AstrologySettingsManager
import com.astro.vajra.data.preferences.OnboardingManager
import com.astro.vajra.data.preferences.ThemeManager
import com.astro.vajra.data.preferences.ThemeMode
import com.astro.vajra.ephemeris.GocharaVedhaCalculator
import com.astro.vajra.ephemeris.TarabalaCalculator
import com.astro.vajra.ephemeris.TransitAnalyzer
import com.astro.vajra.ui.navigation.AstroVajraNavigation
import com.astro.vajra.ui.screen.OnboardingScreen
import com.astro.vajra.ui.theme.AstroVajraTheme
import com.astro.vajra.ui.theme.LocalAppThemeColors
import com.astro.vajra.ui.viewmodel.ChartViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var onboardingManager: OnboardingManager

    @Inject
    lateinit var localizationManager: LocalizationManager

    @Inject
    lateinit var transitAnalyzer: TransitAnalyzer

    @Inject
    lateinit var vedhaCalculator: GocharaVedhaCalculator

    @Inject
    lateinit var tarabalaCalculator: TarabalaCalculator

    @Inject
    lateinit var astrologySettingsManager: AstrologySettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Observe theme mode
            val themeMode by themeManager.themeMode.collectAsState()
            val isSystemInDarkTheme = isSystemInDarkTheme()

            // Determine if dark theme should be used
            val useDarkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme
            }

            // Observe onboarding completion
            val hasCompletedOnboarding by onboardingManager.hasCompletedOnboarding.collectAsState()
            var showOnboarding by remember { mutableStateOf(!hasCompletedOnboarding) }

            // Track if we should navigate to chart input after onboarding
            var navigateToChartInputAfterOnboarding by remember { mutableStateOf(false) }

            LocalizationProvider {
                AstroVajraTheme(darkTheme = useDarkTheme) {
                    val colors = LocalAppThemeColors.current
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = colors.ScreenBackground
                    ) {
                        if (showOnboarding) {
                            OnboardingScreen(
                                themeManager = themeManager,
                                localizationManager = localizationManager,
                                onboardingManager = onboardingManager,
                                onComplete = { shouldNavigateToChartInput ->
                                    onboardingManager.completeOnboarding()
                                    navigateToChartInputAfterOnboarding = shouldNavigateToChartInput
                                    showOnboarding = false
                                }
                            )
                        } else {
                            val navController = rememberNavController()
                            val viewModel: ChartViewModel = hiltViewModel()

                            // Navigate to chart input if coming from onboarding
                            LaunchedEffect(navigateToChartInputAfterOnboarding) {
                                if (navigateToChartInputAfterOnboarding) {
                                    navController.navigate("chart_input") {
                                        launchSingleTop = true
                                    }
                                    navigateToChartInputAfterOnboarding = false
                                }
                            }

                            AstroVajraNavigation(
                                navController = navController,
                                themeManager = themeManager,
                                transitAnalyzer = transitAnalyzer,
                                vedhaCalculator = vedhaCalculator,
                                tarabalaCalculator = tarabalaCalculator,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
