package com.astro.storm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.LocalLocalizationManager
import com.astro.storm.data.localization.LocalizationManager
import com.astro.storm.data.localization.LocalizationProvider
import com.astro.storm.data.preferences.OnboardingManager
import com.astro.storm.data.preferences.ThemeManager
import com.astro.storm.data.preferences.ThemeMode
import com.astro.storm.ui.navigation.AstroStormNavigation
import com.astro.storm.ui.screen.OnboardingScreen
import com.astro.storm.ui.theme.AstroStormTheme
import com.astro.storm.ui.viewmodel.ChartViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeManager = remember { ThemeManager.getInstance(this) }
            val onboardingManager = remember { OnboardingManager.getInstance(this) }
            val localizationManager = remember { LocalizationManager.getInstance(this) }

            val themeMode by themeManager.themeMode.collectAsState()
            val onboardingCompleted by onboardingManager.onboardingCompleted.collectAsState()
            val currentLanguage by localizationManager.language.collectAsState()

            val isSystemDark = isSystemInDarkTheme()
            val isDarkTheme = themeManager.isDarkMode(isSystemDark)

            LocalizationProvider {
                AstroStormTheme(darkTheme = isDarkTheme) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        if (!onboardingCompleted) {
                            // Show onboarding for first-time users
                            OnboardingScreen(
                                selectedLanguage = currentLanguage,
                                selectedTheme = themeMode,
                                onLanguageSelected = { language ->
                                    localizationManager.setLanguage(language)
                                },
                                onThemeSelected = { theme ->
                                    themeManager.setThemeMode(theme)
                                },
                                onComplete = {
                                    onboardingManager.completeOnboarding()
                                }
                            )
                        } else {
                            // Show main app
                            val navController = rememberNavController()
                            val viewModel: ChartViewModel = viewModel()
                            AstroStormNavigation(
                                navController = navController,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
