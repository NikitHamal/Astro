package com.astro.storm.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.localization.Language
import com.astro.storm.data.preferences.ThemeMode
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.DarkAppThemeColors
import com.astro.storm.ui.theme.LightAppThemeColors

/**
 * Onboarding Screen for AstroStorm
 *
 * A clean, minimal, and professional one-time onboarding experience.
 * Features:
 * - Language selection (English/Nepali)
 * - Theme selection (Light/Dark/System)
 * - Brief app introduction
 * - Fully responsive to all screen sizes
 */
@Composable
fun OnboardingScreen(
    selectedLanguage: Language,
    selectedTheme: ThemeMode,
    onLanguageSelected: (Language) -> Unit,
    onThemeSelected: (ThemeMode) -> Unit,
    onComplete: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isCompactScreen = configuration.screenWidthDp < 400
    val scrollState = rememberScrollState()

    // Use theme colors based on selection
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (selectedTheme) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemDark
    }
    val colors = if (isDark) DarkAppThemeColors else LightAppThemeColors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.ScreenBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = if (isCompactScreen) 20.dp else 32.dp)
                .padding(top = 48.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo/Icon Section
            OnboardingHeader(colors = colors)

            Spacer(modifier = Modifier.height(40.dp))

            // Welcome Message
            WelcomeSection(
                language = selectedLanguage,
                colors = colors,
                isCompactScreen = isCompactScreen
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Language Selection
            LanguageSelectionSection(
                selectedLanguage = selectedLanguage,
                onLanguageSelected = onLanguageSelected,
                colors = colors
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Theme Selection
            ThemeSelectionSection(
                selectedTheme = selectedTheme,
                language = selectedLanguage,
                onThemeSelected = onThemeSelected,
                colors = colors
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            // Continue Button
            ContinueButton(
                language = selectedLanguage,
                colors = colors,
                onClick = onComplete
            )
        }
    }
}

@Composable
private fun OnboardingHeader(colors: com.astro.storm.ui.theme.AppThemeColors) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colors.AccentGold,
                            colors.AccentPrimary
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = if (colors.isDark) Color.Black else Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "AstroStorm",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = colors.TextPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (colors.isDark) "Vedic Astrology" else "5H&? M/K$?7",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.AccentGold,
            letterSpacing = 2.sp
        )
    }
}

@Composable
private fun WelcomeSection(
    language: Language,
    colors: com.astro.storm.ui.theme.AppThemeColors,
    isCompactScreen: Boolean
) {
    val (title, subtitle) = when (language) {
        Language.ENGLISH -> Pair(
            "Welcome",
            "Your personal guide to Vedic astrology. Get accurate birth charts, dashas, yogas, and personalized predictions."
        )
        Language.NEPALI -> Pair(
            "8M5>$ ",
            "5H&? M/K$?7K 2>? $*>K 5M/M$?$ >!d 89@ (M.*$M0?>, &6>, /K 0 5M/M$?$ -5?7M/5>#@ *M0>*M$ 0M(A9K8M$"
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = if (isCompactScreen) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = colors.TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.widthIn(max = 340.dp)
        )
    }
}

@Composable
private fun LanguageSelectionSection(
    selectedLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    colors: com.astro.storm.ui.theme.AppThemeColors
) {
    val title = when (selectedLanguage) {
        Language.ENGLISH -> "Select Language"
        Language.NEPALI -> "->7> >(M(A9K8M"
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = colors.TextPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Language.entries.forEach { language ->
                LanguageOptionCard(
                    language = language,
                    isSelected = language == selectedLanguage,
                    colors = colors,
                    onClick = { onLanguageSelected(language) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun LanguageOptionCard(
    language: Language,
    isSelected: Boolean,
    colors: com.astro.storm.ui.theme.AppThemeColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) colors.AccentGold else colors.BorderColor
    val backgroundColor = if (isSelected) colors.AccentGold.copy(alpha = 0.1f) else colors.CardBackground

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Language icon
            Icon(
                imageVector = Icons.Outlined.Language,
                contentDescription = null,
                tint = if (isSelected) colors.AccentGold else colors.TextMuted,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = language.nativeName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) colors.AccentGold else colors.TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = language.englishName,
                style = MaterialTheme.typography.bodySmall,
                color = colors.TextMuted
            )

            if (isSelected) {
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = colors.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ThemeSelectionSection(
    selectedTheme: ThemeMode,
    language: Language,
    onThemeSelected: (ThemeMode) -> Unit,
    colors: com.astro.storm.ui.theme.AppThemeColors
) {
    val title = when (language) {
        Language.ENGLISH -> "Choose Theme"
        Language.NEPALI -> "%?. >(M(A9K8M"
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = colors.TextPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ThemeMode.entries.forEach { theme ->
                ThemeOptionCard(
                    theme = theme,
                    language = language,
                    isSelected = theme == selectedTheme,
                    colors = colors,
                    onClick = { onThemeSelected(theme) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ThemeOptionCard(
    theme: ThemeMode,
    language: Language,
    isSelected: Boolean,
    colors: com.astro.storm.ui.theme.AppThemeColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = when (theme) {
        ThemeMode.LIGHT -> Icons.Outlined.LightMode
        ThemeMode.DARK -> Icons.Outlined.DarkMode
        ThemeMode.SYSTEM -> Icons.Outlined.SettingsBrightness
    }

    val borderColor = if (isSelected) colors.AccentGold else colors.BorderColor
    val backgroundColor = if (isSelected) colors.AccentGold.copy(alpha = 0.1f) else colors.CardBackground

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) colors.AccentGold else colors.TextMuted,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = theme.getDisplayName(language),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) colors.AccentGold else colors.TextPrimary,
                textAlign = TextAlign.Center
            )

            if (isSelected) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(colors.AccentGold)
                )
            }
        }
    }
}

@Composable
private fun ContinueButton(
    language: Language,
    colors: com.astro.storm.ui.theme.AppThemeColors,
    onClick: () -> Unit
) {
    val buttonText = when (language) {
        Language.ENGLISH -> "Get Started"
        Language.NEPALI -> "8A0A 0M(A9K8M"
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.AccentGold,
            contentColor = if (colors.isDark) Color.Black else Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        )
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
}
