package com.astro.storm.ui.components.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.CinzelDecorativeFamily
import com.astro.storm.ui.theme.NeoVedicTokens
import com.astro.storm.ui.theme.SpaceGroteskFamily

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

// =============================================================================
// NEO-VEDIC SCAFFOLD - Standard screen wrapper for all screens
// =============================================================================

/**
 * Standard Neo-Vedic screen scaffold with consistent top bar, background, and content padding.
 * Use this as the root composable for all feature screens to ensure design consistency.
 */
@Composable
fun NeoVedicScreen(
    title: String,
    onBack: () -> Unit,
    subtitle: String? = null,
    actionIcon: ImageVector? = null,
    onAction: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = title,
                subtitle = subtitle,
                onBack = onBack,
                actionIcon = actionIcon,
                onAction = onAction
            )
        },
        content = content
    )
}

// =============================================================================
// NEO-VEDIC SECTION LABEL - Uppercase tech-style section header
// =============================================================================

/**
 * Space Grotesk uppercase section label, matching Home/Insights tab pattern.
 * Used for labeling sections within cards and content areas.
 */
@Composable
fun NeoVedicSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.TextMuted
) {
    Text(
        text = text.uppercase(),
        modifier = modifier,
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        letterSpacing = 2.sp,
        color = color
    )
}

// =============================================================================
// NEO-VEDIC DISPLAY TITLE - Cinzel Decorative brand-moment headers
// =============================================================================

/**
 * Cinzel Decorative display title for hero moments and screen feature titles.
 */
@Composable
fun NeoVedicDisplayTitle(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Float = 19f,
    color: Color = AppTheme.TextPrimary
) {
    Text(
        text = text,
        modifier = modifier,
        fontFamily = CinzelDecorativeFamily,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize.sp,
        color = color
    )
}

// =============================================================================
// NEO-VEDIC CARD - Standard card wrapper with proper tokens
// =============================================================================

/**
 * Standard Neo-Vedic card with proper border, corner radius, and zero elevation.
 */
@Composable
fun NeoVedicCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppTheme.CardBackground,
    borderColor: Color = AppTheme.BorderColor,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier
                        .clip(RoundedCornerShape(NeoVedicTokens.CardCornerRadius))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = AppTheme.AccentPrimary),
                            onClick = onClick
                        )
                } else Modifier
            ),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        color = backgroundColor,
        border = BorderStroke(NeoVedicTokens.BorderWidth, borderColor),
        tonalElevation = NeoVedicTokens.CardElevation,
        shadowElevation = NeoVedicTokens.CardElevation
    ) {
        content()
    }
}

// =============================================================================
// NEO-VEDIC EXPANDABLE SECTION - Animated expand/collapse with accent line
// =============================================================================

/**
 * Expandable section card with animated expand/collapse, matching the Insights tab pattern.
 */
@Composable
fun NeoVedicExpandableSection(
    title: String,
    icon: ImageVector? = null,
    iconTint: Color = AppTheme.AccentPrimary,
    subtitle: String? = null,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    headerTrailing: @Composable (() -> Unit)? = null,
    expandedContent: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(250),
        label = "expandRotation"
    )

    NeoVedicCard(
        modifier = modifier,
        onClick = onToggle
    ) {
        Column(
            modifier = Modifier
                .padding(NeoVedicTokens.ScreenPadding)
                .animateContentSize(animationSpec = tween(300))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    icon?.let {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(iconTint.copy(alpha = 0.12f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        subtitle?.let {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                headerTrailing?.invoke()

                Spacer(modifier = Modifier.width(NeoVedicTokens.SpaceXS))

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(tween(300)) + fadeIn(tween(250)),
                exit = shrinkVertically(tween(200)) + fadeOut(tween(150))
            ) {
                Column(modifier = Modifier.padding(top = NeoVedicTokens.ScreenPadding)) {
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(NeoVedicTokens.ScreenPadding))
                    expandedContent()
                }
            }
        }
    }
}

// =============================================================================
// NEO-VEDIC STRENGTH BAR - Horizontal progress with label
// =============================================================================

/**
 * Horizontal strength/progress indicator with label and value, matching Neo-Vedic style.
 */
@Composable
fun NeoVedicStrengthBar(
    label: String,
    value: String,
    progress: Float,
    progressColor: Color = AppTheme.AccentGold,
    modifier: Modifier = Modifier
) {
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
                color = AppTheme.TextMuted,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = progressColor
            )
        }
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius)),
            color = progressColor,
            trackColor = AppTheme.DividerColor
        )
    }
}

// =============================================================================
// NEO-VEDIC PLANET BADGE - Circular planet indicator with color
// =============================================================================

/**
 * Small circular badge for planet identification, using traditional Vedic color coding.
 */
@Composable
fun NeoVedicPlanetBadge(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .background(color.copy(alpha = 0.12f), CircleShape)
            .border(NeoVedicTokens.ThinBorderWidth, color.copy(alpha = 0.3f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontFamily = SpaceGroteskFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = if (size <= 36.dp) 10.sp else 12.sp,
            color = color,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

// =============================================================================
// NEO-VEDIC INFO PAIR - Compact label-value display
// =============================================================================

/**
 * Compact info pair used inside cards for displaying labeled data.
 */
@Composable
fun NeoVedicInfoPair(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    labelColor: Color = AppTheme.TextMuted,
    valueColor: Color = AppTheme.TextPrimary,
    valueWeight: FontWeight = FontWeight.Medium
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label.uppercase(),
            fontFamily = SpaceGroteskFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 9.sp,
            letterSpacing = 1.5.sp,
            color = labelColor
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = valueWeight,
            color = valueColor
        )
    }
}

// =============================================================================
// NEO-VEDIC LOADING STATE - Consistent loading indicator
// =============================================================================

/**
 * Loading state with spinner, matching the Neo-Vedic design.
 */
@Composable
fun NeoVedicLoadingState(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.ScreenPadding)
        ) {
            CircularProgressIndicator(
                color = AppTheme.AccentGold,
                strokeWidth = 2.dp,
                modifier = Modifier.size(36.dp)
            )
            message?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

// =============================================================================
// NEO-VEDIC DIVIDER - Thin divider with proper color
// =============================================================================

/**
 * Thin divider line matching the Neo-Vedic palette.
 */
@Composable
fun NeoVedicDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = NeoVedicTokens.ThinBorderWidth,
        color = AppTheme.DividerColor
    )
}

// =============================================================================
// NEO-VEDIC SCORE INDICATOR - Circular score display
// =============================================================================

/**
 * Circular score indicator for strength values, compatibility scores, etc.
 */
@Composable
fun NeoVedicScoreIndicator(
    score: Float,
    maxScore: Float = 100f,
    label: String,
    scoreColor: Color = AppTheme.AccentGold,
    modifier: Modifier = Modifier,
    size: Dp = 72.dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceXXS)
    ) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { (score / maxScore).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxSize(),
                color = scoreColor,
                trackColor = AppTheme.DividerColor,
                strokeWidth = 3.dp
            )
            Text(
                text = "${score.toInt()}",
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.Bold,
                fontSize = if (size >= 72.dp) 21.sp else 15.sp,
                color = scoreColor
            )
        }
        Text(
            text = label.uppercase(),
            fontFamily = SpaceGroteskFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 9.sp,
            letterSpacing = 1.5.sp,
            color = AppTheme.TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

// =============================================================================
// NEO-VEDIC ACCENT LINE - Vertical colored accent indicator
// =============================================================================

/**
 * Vertical accent line for card/row decoration, similar to the transit timeline highlight.
 */
@Composable
fun NeoVedicAccentLine(
    color: Color,
    modifier: Modifier = Modifier,
    width: Dp = 3.dp,
    height: Dp = 48.dp
) {
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .background(color, RoundedCornerShape(NeoVedicTokens.ChipCornerRadius))
    )
}
