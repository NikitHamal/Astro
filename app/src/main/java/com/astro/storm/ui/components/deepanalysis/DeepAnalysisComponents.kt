package com.astro.storm.ui.components.deepanalysis

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.StringKey
import com.astro.storm.data.localization.stringResource
import com.astro.storm.ephemeris.deepanalysis.*
import com.astro.storm.ui.theme.AppTheme

/**
 * Reusable UI components for Deep Analysis screens
 * Revamped for consistency with app design system
 */

// Strength level colors - harmonized with app theme
object DeepAnalysisColors {
    val extremelyStrong = Color(0xFF1B5E20)
    val veryStrong = Color(0xFF2E7D32)
    val excellent = Color(0xFF4CAF50)
    val strong = Color(0xFF8BC34A)
    val moderate = Color(0xFFD4A537)  // Warmer gold instead of harsh yellow
    val weak = Color(0xFFFF9800)
    val afflicted = Color(0xFFE57373)  // Softer red
    
    fun forStrength(level: StrengthLevel): Color = when (level) {
        StrengthLevel.EXTREMELY_STRONG -> extremelyStrong
        StrengthLevel.VERY_STRONG -> veryStrong
        StrengthLevel.EXCELLENT -> excellent
        StrengthLevel.STRONG -> strong
        StrengthLevel.MODERATE -> moderate
        StrengthLevel.WEAK -> weak
        StrengthLevel.AFFLICTED -> afflicted
    }
}

/**
 * Section header with icon - Modern style matching app design
 */
@Composable
fun DeepSectionHeader(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    AppTheme.AccentPrimary.copy(alpha = 0.15f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.AccentPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary
        )
    }
}

/**
 * Expandable analysis card - Premium style with animations
 */
@Composable
fun ExpandableAnalysisCard(
    title: String,
    subtitle: String? = null,
    strength: StrengthLevel? = null,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(250),
        label = "expandRotation"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = AppTheme.AccentPrimary.copy(alpha = 0.08f),
                spotColor = AppTheme.AccentPrimary.copy(alpha = 0.08f)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        color = AppTheme.CardBackground
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize(animationSpec = tween(250))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left indicator line
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(40.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            strength?.let { DeepAnalysisColors.forStrength(it) }
                                ?: AppTheme.AccentPrimary
                        )
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
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
                
                Spacer(modifier = Modifier.width(8.dp))
                
                strength?.let {
                    StrengthBadge(strength = it)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = AppTheme.TextMuted,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation)
                )
            }
            
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(tween(250)) + fadeIn(tween(250)),
                exit = shrinkVertically(tween(200)) + fadeOut(tween(150))
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(
                        color = AppTheme.DividerColor,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    content()
                }
            }
        }
    }
}

/**
 * Strength indicator badge - Horizontal layout with proper sizing
 */
@Composable
fun StrengthBadge(
    strength: StrengthLevel,
    modifier: Modifier = Modifier
) {
    val language = com.astro.storm.data.localization.LocalLanguage.current
    val localizedName = if (language == com.astro.storm.core.common.Language.NEPALI) 
        strength.displayNameNe 
    else 
        strength.displayName
    
    val color = DeepAnalysisColors.forStrength(strength)
    
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = localizedName,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Circular score indicator - Enhanced with gradient ring
 */
@Composable
fun ScoreIndicator(
    score: Double,
    label: String,
    modifier: Modifier = Modifier
) {
    val language = com.astro.storm.data.localization.LocalLanguage.current
    val localizedScore = if (language == com.astro.storm.core.common.Language.NEPALI) {
        com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(score.toInt())
    } else {
        "${score.toInt()}"
    }
    
    val scoreColor = when {
        score >= 80 -> DeepAnalysisColors.excellent
        score >= 60 -> DeepAnalysisColors.strong
        score >= 40 -> DeepAnalysisColors.moderate
        score >= 20 -> DeepAnalysisColors.weak
        else -> DeepAnalysisColors.afflicted
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .border(
                    width = 4.dp,
                    brush = Brush.sweepGradient(
                        listOf(
                            scoreColor,
                            scoreColor.copy(alpha = 0.3f),
                            scoreColor
                        )
                    ),
                    shape = CircleShape
                )
                .padding(4.dp)
                .background(
                    scoreColor.copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = localizedScore,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = scoreColor
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = AppTheme.TextMuted,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Localized paragraph display
 */
@Composable
fun LocalizedParagraphText(
    paragraph: LocalizedParagraph,
    useNepali: Boolean? = null,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium
) {
    val language = com.astro.storm.data.localization.LocalLanguage.current
    val shouldShowNepali = useNepali ?: (language == com.astro.storm.core.common.Language.NEPALI)
    
    Text(
        text = if (shouldShowNepali) paragraph.ne else paragraph.en,
        modifier = modifier,
        style = style,
        color = AppTheme.TextSecondary,
        lineHeight = 22.sp
    )
}

/**
 * Trait list display - Premium style with colored indicators
 */
@Composable
fun TraitsList(
    traits: List<LocalizedTrait>,
    useNepali: Boolean? = null,
    modifier: Modifier = Modifier
) {
    val language = com.astro.storm.data.localization.LocalLanguage.current
    val shouldShowNepali = useNepali ?: (language == com.astro.storm.core.common.Language.NEPALI)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        traits.forEach { trait ->
            val strengthColor = DeepAnalysisColors.forStrength(trait.strength)
            
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = strengthColor.copy(alpha = 0.08f),
                        spotColor = strengthColor.copy(alpha = 0.08f)
                    ),
                color = AppTheme.CardBackground,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left colored indicator
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(strengthColor)
                    )
                    
                    Spacer(modifier = Modifier.width(14.dp))
                    
                    Text(
                        text = if (shouldShowNepali) trait.nameNe else trait.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                        color = AppTheme.TextPrimary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    StrengthBadge(strength = trait.strength)
                }
            }
        }
    }
}

/**
 * Timeline period card - Modern style with colored indicator
 */
@Composable
fun TimelinePeriodCard(
    title: String,
    dateRange: String,
    description: LocalizedParagraph,
    strength: StrengthLevel,
    useNepali: Boolean? = null,
    modifier: Modifier = Modifier
) {
    val color = DeepAnalysisColors.forStrength(strength)
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = color.copy(alpha = 0.1f),
                spotColor = color.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(14.dp),
        color = AppTheme.CardBackground
    ) {
        Row(modifier = Modifier.padding(14.dp)) {
            // Colored indicator dot with pulse animation effect
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            
            Spacer(modifier = Modifier.width(14.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                if (dateRange.isNotEmpty()) {
                    Text(
                        text = dateRange,
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LocalizedParagraphText(
                    paragraph = description,
                    useNepali = useNepali,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            StrengthBadge(strength = strength)
        }
    }
}

/**
 * Analysis overview card with scores - Premium design
 */
@Composable
fun AnalysisOverviewCard(
    title: String,
    scores: List<Pair<String, Double>>,
    summary: LocalizedParagraph,
    useNepali: Boolean? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = AppTheme.AccentPrimary.copy(alpha = 0.1f),
                spotColor = AppTheme.AccentPrimary.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(20.dp),
        color = AppTheme.CardBackground
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            AppTheme.AccentPrimary.copy(alpha = 0.15f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Score row with proper spacing
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(scores.take(4)) { (label, score) ->
                    ScoreIndicator(score = score, label = label)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = AppTheme.DividerColor)
            Spacer(modifier = Modifier.height(16.dp))
            
            LocalizedParagraphText(
                paragraph = summary,
                useNepali = useNepali
            )
        }
    }
}

/**
 * Loading state - Modern spinner
 */
@Composable
fun DeepAnalysisLoading(
    modifier: Modifier = Modifier
) {
    val language = com.astro.storm.data.localization.LocalLanguage.current
    val loadingText = if (language == com.astro.storm.core.common.Language.NEPALI) {
        "तपाईंको कुण्डलीको गहन विश्लेषण गर्दै..."
    } else {
        "Analyzing your chart deeply..."
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        AppTheme.AccentPrimary.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 4.dp,
                    color = AppTheme.AccentPrimary,
                    strokeCap = StrokeCap.Round
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = loadingText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(StringKeyPart1.MSG_MAY_TAKE_MOMENT),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
        }
    }
}

/**
 * Error state - Modern error display
 */
@Composable
fun DeepAnalysisError(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            shape = RoundedCornerShape(20.dp),
            color = AppTheme.ErrorColor.copy(alpha = 0.08f)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            AppTheme.ErrorColor.copy(alpha = 0.15f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = AppTheme.ErrorColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(StringKeyError.ERROR_ANALYSIS_FAILED),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.ErrorColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.AccentPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(StringKeyUI.BTN_RETRY),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * Modern section card for consistent styling across deep analysis screens
 */
@Composable
fun DeepAnalysisSectionCard(
    modifier: Modifier = Modifier,
    accentColor: Color = AppTheme.AccentPrimary,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = accentColor.copy(alpha = 0.08f),
                spotColor = accentColor.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(16.dp),
        color = AppTheme.CardBackground
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

/**
 * Info row for consistent key-value display
 */
@Composable
fun DeepAnalysisInfoRow(
    label: String,
    value: String,
    valueColor: Color = AppTheme.TextPrimary,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextMuted,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
    }
}
