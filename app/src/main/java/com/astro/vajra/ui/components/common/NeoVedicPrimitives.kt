package com.astro.vajra.ui.components.common

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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.ColumnScope
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.NeoVedicTokens

@Composable
fun NeoVedicPageHeader(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    actionIcon: ImageVector? = null,
    onAction: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    subtitleContent: @Composable (ColumnScope.() -> Unit)? = null,
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
                    if (subtitleContent != null) {
                        subtitleContent()
                    } else if (!subtitle.isNullOrBlank()) {
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

            actions()
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

/**
 * Ephemeris-style date section header with TODAY/TOMORROW badge support.
 * Matches the reference design for transit screens.
 */
@Composable
fun NeoVedicEphemerisDateHeader(
    dateLabel: String,
    badgeText: String? = null,
    badgeColor: Color = AppTheme.AccentGold,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.CardBackground)
            .padding(horizontal = NeoVedicTokens.ScreenPadding, vertical = NeoVedicTokens.SpaceSM),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = dateLabel,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary
        )
        if (!badgeText.isNullOrBlank()) {
            NeoVedicStatusPill(
                text = badgeText,
                textColor = badgeColor,
                containerColor = badgeColor.copy(alpha = 0.12f)
            )
        }
    }
}

/**
 * Enhanced timeline item for Ephemeris/Transit display with aspect glyphs
 * and planet symbols. Supports retrograde/direct indicators and dignities.
 */
@Composable
fun NeoVedicEphemerisTransitItem(
    timeLabel: String,
    planetGlyph: String,
    aspectGlyph: String? = null,
    targetPlanetGlyph: String? = null,
    title: String,
    positionText: String,
    statusText: String? = null,
    statusColor: Color = AppTheme.AccentGold,
    isHighlighted: Boolean = false,
    showConnector: Boolean = true,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.background(
                    if (isHighlighted) statusColor.copy(alpha = 0.06f) else Color.Transparent
                ) else Modifier
            )
            .padding(horizontal = NeoVedicTokens.ScreenPadding, vertical = NeoVedicTokens.SpaceXS),
        verticalAlignment = Alignment.Top
    ) {
        // Timeline connector
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = NeoVedicTokens.SpaceXS)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        if (isHighlighted) statusColor else AppTheme.BorderColor,
                        CircleShape
                    )
            )
            if (showConnector) {
                Spacer(
                    modifier = Modifier
                        .height(48.dp)
                        .width(1.dp)
                        .background(AppTheme.BorderColor.copy(alpha = 0.4f))
                )
            }
        }

        Spacer(modifier = Modifier.width(NeoVedicTokens.SpaceSM))

        // Content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceXXS)
        ) {
            // Time and aspect glyphs row
            Row(
                horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = timeLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextMuted
                )
                // Planet and aspect glyphs
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = planetGlyph,
                        style = MaterialTheme.typography.titleMedium,
                        color = AppTheme.TextPrimary
                    )
                    if (!aspectGlyph.isNullOrBlank()) {
                        Text(
                            text = aspectGlyph,
                            style = MaterialTheme.typography.labelLarge,
                            color = statusColor
                        )
                    }
                    if (!targetPlanetGlyph.isNullOrBlank()) {
                        Text(
                            text = targetPlanetGlyph,
                            style = MaterialTheme.typography.titleMedium,
                            color = AppTheme.TextPrimary
                        )
                    }
                }
            }

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Position and status row
            Row(
                horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = positionText,
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.TextMuted
                )
                if (!statusText.isNullOrBlank()) {
                    NeoVedicStatusPill(
                        text = statusText,
                        textColor = statusColor,
                        containerColor = statusColor.copy(alpha = 0.12f)
                    )
                }
            }
        }

        // Highlighted indicator bar
        if (isHighlighted) {
            Spacer(modifier = Modifier.width(NeoVedicTokens.SpaceXS))
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(56.dp)
                    .background(statusColor, RoundedCornerShape(NeoVedicTokens.ElementCornerRadius))
            )
        }
    }
}

/**
 * Compact strength indicator with progress bar for Bala screens.
 */
@Composable
fun NeoVedicStrengthIndicator(
    label: String,
    value: Float,
    maxValue: Float = 100f,
    valueText: String? = null,
    accentColor: Color = AppTheme.AccentPrimary,
    modifier: Modifier = Modifier
) {
    val progress = (value / maxValue).coerceIn(0f, 1f)
    val displayColor = when {
        progress >= 0.7f -> AppTheme.SuccessColor
        progress >= 0.5f -> AppTheme.AccentGold
        progress >= 0.3f -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceXXS)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
            Text(
                text = valueText ?: String.format("%.0f%%", progress * 100),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = displayColor
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(AppTheme.DividerColor, RoundedCornerShape(NeoVedicTokens.ElementCornerRadius))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(6.dp)
                    .background(displayColor, RoundedCornerShape(NeoVedicTokens.ElementCornerRadius))
            )
        }
    }
}

/**
 * Planet card for strength/analysis screens with glyph, name, and key metrics.
 */
@Composable
fun NeoVedicPlanetCard(
    planetGlyph: String,
    planetName: String,
    primaryValue: String,
    primaryLabel: String,
    secondaryValue: String? = null,
    secondaryLabel: String? = null,
    accentColor: Color = AppTheme.AccentPrimary,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(NeoVedicTokens.ScreenPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Planet glyph and name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceSM)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(accentColor.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = planetGlyph,
                            style = MaterialTheme.typography.titleLarge,
                            color = accentColor
                        )
                    }
                    Column {
                        Text(
                            text = planetName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        if (!secondaryLabel.isNullOrBlank() && !secondaryValue.isNullOrBlank()) {
                            Text(
                                text = "$secondaryLabel: $secondaryValue",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                        }
                    }
                }

                // Primary metric
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = primaryValue,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                    Text(
                        text = primaryLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            // Additional content if provided
            content?.invoke()
        }
    }
}

/**
 * Summary stat card for overview sections.
 */
@Composable
fun NeoVedicSummaryStatCard(
    value: String,
    label: String,
    icon: ImageVector? = null,
    accentColor: Color = AppTheme.AccentPrimary,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = accentColor.copy(alpha = 0.08f),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(NeoVedicTokens.ThinBorderWidth, accentColor.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(NeoVedicTokens.SpaceMD),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceXXS)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(NeoVedicTokens.SpaceXXS))
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted
            )
        }
    }
}

/**
 * Section divider with optional title.
 */
@Composable
fun NeoVedicSectionDivider(
    title: String? = null,
    modifier: Modifier = Modifier
) {
    if (title != null) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = NeoVedicTokens.SpaceSM),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceXS)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(AppTheme.DividerColor)
            )
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextMuted
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(AppTheme.DividerColor)
            )
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(AppTheme.DividerColor)
        )
    }
}

fun Modifier.vedicCornerMarkers(
    color: Color,
    cornerLength: Dp = 12.dp,
    strokeWidth: Dp = 1.dp
) = this.drawWithContent {
    drawContent()
    val len = cornerLength.toPx()
    val sw = strokeWidth.toPx()

    // Top-left
    drawLine(color, Offset(0f, sw / 2), Offset(len, sw / 2), sw)
    drawLine(color, Offset(sw / 2, 0f), Offset(sw / 2, len), sw)

    // Top-right
    drawLine(color, Offset(size.width - len, sw / 2), Offset(size.width, sw / 2), sw)
    drawLine(color, Offset(size.width - sw / 2, 0f), Offset(size.width - sw / 2, len), sw)

    // Bottom-left
    drawLine(color, Offset(0f, size.height - sw / 2), Offset(len, size.height - sw / 2), sw)
    drawLine(color, Offset(sw / 2, size.height - len), Offset(sw / 2, size.height), sw)

    // Bottom-right
    drawLine(color, Offset(size.width - len, size.height - sw / 2), Offset(size.width, size.height - sw / 2), sw)
    drawLine(color, Offset(size.width - sw / 2, size.height - len), Offset(size.width - sw / 2, size.height), sw)
}
