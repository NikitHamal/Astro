package com.astro.storm.ui.components.deepanalysis

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.StringKey
import com.astro.storm.data.localization.stringResource
import com.astro.storm.ephemeris.deepanalysis.*
import com.astro.storm.ui.theme.AppTheme

/**
 * Reusable UI components for Deep Analysis screens
 */

// Strength level colors
object DeepAnalysisColors {
    val extremelyStrong = Color(0xFF1B5E20)
    val veryStrong = Color(0xFF2E7D32)
    val excellent = Color(0xFF4CAF50)
    val strong = Color(0xFF8BC34A)
    val moderate = Color(0xFFFFEB3B)
    val weak = Color(0xFFFF9800)
    val afflicted = Color(0xFFF44336)
    
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
 * Section header with icon and subtle background
 */
@Composable
fun DeepSectionHeader(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    accentColor: Color = AppTheme.AccentPrimary
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(accentColor.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AppTheme.TextPrimary
        )
    }
}

/**
 * Premium Expandable analysis card with better hierarchy and styling
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
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isExpanded) AppTheme.AccentPrimary.copy(alpha = 0.2f) else AppTheme.BorderColor.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isExpanded) AppTheme.AccentPrimary else AppTheme.TextPrimary
                    )
                    subtitle?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    strength?.let {
                        StrengthBadge(strength = it)
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = if (isExpanded) AppTheme.AccentPrimary else AppTheme.TextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 16.dp), 
                        color = AppTheme.DividerColor.copy(alpha = 0.5f)
                    )
                    content()
                }
            }
        }
    }
}

/**
 * Strength indicator badge
 */
@Composable
fun StrengthBadge(
    strength: StrengthLevel,
    modifier: Modifier = Modifier
) {
    val language = com.astro.storm.data.localization.LocalLanguage.current
    val localizedName = if (language == com.astro.storm.core.common.Language.NEPALI) strength.displayNameNe else strength.displayName
    
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = DeepAnalysisColors.forStrength(strength).copy(alpha = 0.2f)
    ) {
        Text(
            text = localizedName,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = DeepAnalysisColors.forStrength(strength),
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Premium circular score indicator with a progress ring
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
            modifier = Modifier.size(90.dp),
            contentAlignment = Alignment.Center
        ) {
            // Track
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = AppTheme.DividerColor.copy(alpha = 0.5f),
                strokeWidth = 6.dp,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            
            // Progress
            CircularProgressIndicator(
                progress = { (score / 100f).toFloat() },
                modifier = Modifier.fillMaxSize(),
                color = scoreColor,
                strokeWidth = 6.dp,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = localizedScore,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = "/100",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            color = AppTheme.TextSecondary,
            lineHeight = 16.sp,
            maxLines = 2,
            modifier = Modifier.width(100.dp)
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
        color = AppTheme.TextSecondary
    )
}

/**
 * Trait list display
 */
@Composable
fun TraitsList(
    traits: List<LocalizedTrait>,
    useNepali: Boolean? = null,
    modifier: Modifier = Modifier
) {
    val language = com.astro.storm.data.localization.LocalLanguage.current
    val shouldShowNepali = useNepali ?: (language == com.astro.storm.core.common.Language.NEPALI)

    Column(modifier = modifier) {
        traits.forEach { trait ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (shouldShowNepali) trait.nameNe else trait.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    color = AppTheme.TextSecondary
                )
                StrengthBadge(strength = trait.strength)
            }
        }
    }
}



/**
 * Enhanced timeline period card with vertical timeline line
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
    val strengthColor = DeepAnalysisColors.forStrength(strength)
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Timeline indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(strengthColor.copy(alpha = 0.2f), CircleShape)
                        .border(2.dp, strengthColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(strengthColor, CircleShape)
                    )
                }
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(strengthColor.copy(alpha = 0.5f), Color.Transparent)
                            )
                        )
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = dateRange,
                            style = MaterialTheme.typography.labelMedium,
                            color = AppTheme.AccentPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    StrengthBadge(strength = strength)
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LocalizedParagraphText(
                    paragraph = description,
                    useNepali = useNepali,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 22.sp,
                        color = AppTheme.TextSecondary
                    )
                )
            }
        }
    }
}

/**
 * Revamped analysis overview card with horizontal scrollable scores
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
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(24.dp),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.BorderColor.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(vertical = 24.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(scores) { (label, score) ->
                    ScoreIndicator(score = score, label = label)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppTheme.ScreenBackground.copy(alpha = 0.5f))
                    .padding(16.dp)
            ) {
                LocalizedParagraphText(
                    paragraph = summary,
                    useNepali = useNepali,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 22.sp,
                        color = AppTheme.TextSecondary
                    )
                )
            }
        }
    }
}

/**
 * Loading state
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
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                strokeWidth = 4.dp,
                color = AppTheme.AccentPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = loadingText,
                style = MaterialTheme.typography.bodyLarge,
                color = AppTheme.TextPrimary
            )
            Text(
                text = stringResource(StringKey.MSG_MAY_TAKE_MOMENT),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
        }
    }
}

/**
 * Error state
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = AppTheme.ErrorColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKey.ERROR_ANALYSIS_FAILED),
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.ErrorColor
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.AccentPrimary)
            ) {
                Text(stringResource(StringKey.BTN_RETRY))
            }
        }
    }
}
