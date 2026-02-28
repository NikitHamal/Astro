package com.astro.storm.ui.screen.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.R
import com.astro.storm.core.common.Language
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.ui.components.common.NeoVedicPageHeader
import com.astro.storm.ui.components.common.vedicCornerMarkers
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.CinzelDecorativeFamily
import com.astro.storm.ui.theme.NeoVedicFontSizes
import com.astro.storm.ui.theme.NeoVedicTokens
import com.astro.storm.ui.theme.SpaceGroteskFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    val context = LocalContext.current
    
    val openUrl = { url: String ->
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // App Branding
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                AppTheme.AccentGold.copy(alpha = 0.3f),
                                AppTheme.AccentGold.copy(alpha = 0.05f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(48.dp)
                )
            }
            
            Text(
                text = "AstroStorm",
                fontFamily = CinzelDecorativeFamily,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )
            
            Text(
                text = stringResource(StringKey.SETTINGS_APP_TAGLINE),
                fontFamily = SpaceGroteskFamily,
                fontSize = NeoVedicFontSizes.S14,
                color = AppTheme.AccentGold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )
            
            // Developer Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .vedicCornerMarkers(color = AppTheme.AccentPrimary),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (language == Language.ENGLISH) "DEVELOPED BY" else "विकासकर्ता",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S12,
                        color = AppTheme.TextMuted,
                        letterSpacing = 2.sp
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Circular shaped image using clip
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .border(2.dp, AppTheme.AccentGold, CircleShape)
                            .padding(4.dp)
                            .clip(CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.developer),
                            contentDescription = "Nikit Hamal",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Nikit Hamal",
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                    
                    Text(
                        text = "Software Engineer",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = NeoVedicFontSizes.S14,
                        color = AppTheme.TextSecondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                    )
                    
                    HorizontalDivider(color = AppTheme.DividerColor)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Social Links Grid
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SocialLink(
                            icon = "IG", 
                            label = "Instagram", 
                            handle = "@nikithamal",
                            onClick = { openUrl("https://instagram.com/nikithamal") }
                        )
                        SocialLink(
                            icon = "FB", 
                            label = "Facebook", 
                            handle = "thenikithamal",
                            onClick = { openUrl("https://facebook.com/thenikithamal") }
                        )
                        SocialLink(
                            icon = "TT", 
                            label = "TikTok", 
                            handle = "@nikithamal",
                            onClick = { openUrl("https://tiktok.com/@nikithamal") }
                        )
                        ContactLink(
                            icon = Icons.Default.Email, 
                            label = "Email", 
                            value = "nikithamalofficial@gmail.com",
                            onClick = { openUrl("mailto:nikithamalofficial@gmail.com") }
                        )
                        ContactLink(
                            icon = Icons.Default.Phone, 
                            label = "WhatsApp", 
                            value = "+977 9765324034",
                            onClick = { openUrl("https://wa.me/9779765324034") }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = stringResource(StringKeyMatch.SETTINGS_VERSION).replace("{0}", "1.0.0"),
                fontFamily = SpaceGroteskFamily,
                fontSize = NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SocialLink(
    icon: String,
    label: String,
    handle: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius))
            .clickable(onClick = onClick),
        color = AppTheme.CardBackgroundElevated,
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(AppTheme.AccentPrimary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = AppTheme.AccentPrimary
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontFamily = SpaceGroteskFamily,
                    fontSize = NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = handle,
                    fontSize = NeoVedicFontSizes.S14,
                    color = AppTheme.TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ContactLink(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius))
            .clickable(onClick = onClick),
        color = AppTheme.CardBackgroundElevated,
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(AppTheme.AccentGold.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(18.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontFamily = SpaceGroteskFamily,
                    fontSize = NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = value,
                    fontSize = NeoVedicFontSizes.S14,
                    color = AppTheme.TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
