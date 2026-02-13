package com.astro.storm.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.NeoVedicTokens

@Composable
fun NeoVedicPageHeader(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    actionIcon: ImageVector? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AppTheme.ScreenBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        tonalElevation = NeoVedicTokens.SurfaceElevation,
        shadowElevation = NeoVedicTokens.SurfaceElevation,
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = NeoVedicTokens.ScreenPadding, vertical = NeoVedicTokens.SpaceSM),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = NeoVedicTokens.SpaceSM),
                verticalAlignment = Alignment.Top
            ) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = AppTheme.TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.size(NeoVedicTokens.SpaceXS))
                }
                Column(modifier = Modifier.padding(top = NeoVedicTokens.SpaceXXS)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = AppTheme.TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(NeoVedicTokens.SpaceXXS))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = AppTheme.AccentGold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            if (actionIcon != null && onAction != null) {
                Surface(
                    shape = CircleShape,
                    color = AppTheme.CardBackground,
                    border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
                ) {
                    IconButton(onClick = onAction) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = null,
                            tint = AppTheme.TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NeoVedicTimelineSectionHeader(
    title: String,
    trailingLabel: String? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = NeoVedicTokens.ScreenPadding, vertical = NeoVedicTokens.SpaceSM),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.TextPrimary
            )
            if (!trailingLabel.isNullOrBlank()) {
                Text(
                    text = trailingLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = AppTheme.AccentGold
                )
            }
        }
    }
}

@Composable
fun NeoVedicStatusPill(
    text: String,
    textColor: Color,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        shape = RoundedCornerShape(NeoVedicTokens.ChipCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, textColor.copy(alpha = 0.35f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = NeoVedicTokens.SpaceXS, vertical = NeoVedicTokens.SpaceXXS),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
fun NeoVedicStatRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = AppTheme.TextPrimary
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.TextMuted
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
    }
}

@Composable
fun NeoVedicTimelineItem(
    timeLabel: String,
    glyphText: String,
    title: String,
    subtitle: String,
    severityColor: Color,
    isHighlighted: Boolean,
    showConnector: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = NeoVedicTokens.ScreenPadding),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = NeoVedicTokens.SpaceXXS)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(AppTheme.ScreenBackground, CircleShape)
                    .border(NeoVedicTokens.BorderWidth, severityColor, CircleShape)
            )
            if (showConnector) {
                Spacer(
                    modifier = Modifier
                        .height(56.dp)
                        .width(NeoVedicTokens.ThinBorderWidth)
                        .background(AppTheme.BorderColor.copy(alpha = 0.45f))
                )
            }
        }

        Spacer(modifier = Modifier.size(NeoVedicTokens.SpaceSM))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = NeoVedicTokens.SpaceXS),
            color = if (isHighlighted) severityColor.copy(alpha = 0.08f) else Color.Transparent,
            shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(NeoVedicTokens.SpaceSM),
                verticalAlignment = Alignment.Top
            ) {
                if (isHighlighted) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(56.dp)
                            .background(severityColor, RoundedCornerShape(NeoVedicTokens.ChipCornerRadius))
                    )
                    Spacer(modifier = Modifier.width(NeoVedicTokens.SpaceXS))
                }
                Column(verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceXXS)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceSM),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = timeLabel,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isHighlighted) severityColor else AppTheme.TextMuted
                        )
                        Text(
                            text = glyphText,
                            style = MaterialTheme.typography.titleSmall,
                            color = AppTheme.TextPrimary
                        )
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = MaterialTheme.typography.headlineSmall.fontSize * 1.35f
                        ),
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelLarge,
                        color = AppTheme.TextMuted
                    )
                }
            }
        }
    }
}

@Composable
fun NeoVedicEmptyState(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceSM)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(56.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = AppTheme.TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}
