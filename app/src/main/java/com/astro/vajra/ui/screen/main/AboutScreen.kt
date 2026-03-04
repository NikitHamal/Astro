package com.astro.vajra.ui.screen.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.BuildConfig
import com.astro.vajra.R
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyMatch
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import com.astro.vajra.ui.components.common.vedicCornerMarkers
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.NeoVedicFontSizes
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.theme.SpaceGroteskFamily

@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val socialActions = listOf(
        SocialAction(
            label = "Instagram",
            icon = Icons.Outlined.PhotoCamera,
            accent = AppTheme.LifeAreaLove,
            url = "https://instagram.com/nikithamal"
        ),
        SocialAction(
            label = "Facebook",
            icon = Icons.Outlined.ThumbUp,
            accent = AppTheme.AccentPrimary,
            url = "https://facebook.com/thenikithamal"
        ),
        SocialAction(
            label = "TikTok",
            icon = Icons.Outlined.MusicNote,
            accent = AppTheme.AccentGold,
            url = "https://tiktok.com/@nikithamal"
        ),
        SocialAction(
            label = "Email",
            icon = Icons.Outlined.Email,
            accent = AppTheme.AccentTeal,
            url = "mailto:nikithamalofficial@gmail.com"
        ),
        SocialAction(
            label = "WhatsApp",
            icon = Icons.Outlined.Chat,
            accent = AppTheme.SuccessColor,
            url = "https://wa.me/9779765324034"
        )
    )

    val openSocial: (SocialAction) -> Unit = { action ->
        runCatching {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(action.url)))
        }
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKey.SETTINGS_ABOUT_APP),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = NeoVedicTokens.ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(108.dp)
                    .clip(CircleShape)
                    .background(AppTheme.CardBackgroundElevated)
                    .border(
                        width = 1.5.dp,
                        color = AppTheme.AccentGold.copy(alpha = 0.75f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher_round),
                    contentDescription = "AstroVajra Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(92.dp).clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "AstroVajra",
                fontFamily = CinzelDecorativeFamily,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )
            Text(
                text = stringResource(StringKey.SETTINGS_APP_TAGLINE),
                fontFamily = SpaceGroteskFamily,
                fontSize = NeoVedicFontSizes.S13,
                color = AppTheme.AccentGold,
                letterSpacing = 1.6.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .vedicCornerMarkers(color = AppTheme.AccentPrimary),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                border = androidx.compose.foundation.BorderStroke(
                    NeoVedicTokens.BorderWidth,
                    AppTheme.BorderColor
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "About The App",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S11,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp,
                        color = AppTheme.AccentPrimary
                    )
                    Text(
                        text = "AstroVajra is a modern Vedic astrology workspace focused on practical clarity. It combines classical systems like Dashas, Yogas, Panchanga, transit layers and divisional analysis into one clean, high-speed interface.",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S13,
                        lineHeight = 21.sp,
                        color = AppTheme.TextSecondary
                    )
                    Text(
                        text = "The design language follows a Neo-Vedic aesthetic: precise structure, balanced density, and calm visual hierarchy for daily interpretation and deeper study.",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S13,
                        lineHeight = 21.sp,
                        color = AppTheme.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                border = androidx.compose.foundation.BorderStroke(
                    NeoVedicTokens.BorderWidth,
                    AppTheme.BorderColor
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Built By",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S11,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = "Nikit Hamal",
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = NeoVedicFontSizes.S21,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = "Vibe Coder",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S13,
                        color = AppTheme.AccentGold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .border(
                                width = 1.5.dp,
                                color = AppTheme.AccentGold.copy(alpha = 0.7f),
                                shape = CircleShape
                            )
                            .padding(3.dp)
                            .clip(CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.developer),
                            contentDescription = "Developer",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                border = androidx.compose.foundation.BorderStroke(
                    NeoVedicTokens.BorderWidth,
                    AppTheme.BorderColor
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Connect",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S11,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp,
                        color = AppTheme.TextMuted
                    )
                    socialActions.chunked(3).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            row.forEach { action ->
                                SocialActionCard(
                                    action = action,
                                    onClick = { openSocial(action) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            repeat((3 - row.size).coerceAtLeast(0)) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(StringKeyMatch.SETTINGS_VERSION).replace("{0}", BuildConfig.VERSION_NAME),
                fontFamily = SpaceGroteskFamily,
                fontSize = NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun SocialActionCard(
    action: SocialAction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackgroundElevated),
        border = androidx.compose.foundation.BorderStroke(
            NeoVedicTokens.ThinBorderWidth,
            AppTheme.BorderColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(action.accent.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.label,
                    tint = action.accent,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = action.label,
                fontFamily = SpaceGroteskFamily,
                fontSize = NeoVedicFontSizes.S11,
                color = AppTheme.TextPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}

private data class SocialAction(
    val label: String,
    val icon: ImageVector,
    val accent: Color,
    val url: String
)
