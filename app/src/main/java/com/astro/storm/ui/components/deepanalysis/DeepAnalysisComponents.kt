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
 * Section header with icon
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
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.AccentPrimary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AppTheme.TextPrimary
        )
    }
}

/**
 * Expandable analysis card
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.CardBackground
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
                        color = AppTheme.TextPrimary
                    )
                    subtitle?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
                
                strength?.let {
                    StrengthBadge(strength = it)
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = AppTheme.TextSubtle
                )
            }
            
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp), color = AppTheme.DividerColor)
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
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = DeepAnalysisColors.forStrength(strength).copy(alpha = 0.2f)
    ) {
        Text(
            text = strength.displayName,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = DeepAnalysisColors.forStrength(strength),
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Circular score indicator
 */
@Composable
fun ScoreIndicator(
    score: Double,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    when {
                        score >= 80 -> DeepAnalysisColors.excellent
                        score >= 60 -> DeepAnalysisColors.strong
                        score >= 40 -> DeepAnalysisColors.moderate
                        score >= 20 -> DeepAnalysisColors.weak
                        else -> DeepAnalysisColors.afflicted
                    }.copy(alpha = 0.2f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${score.toInt()}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = when {
                    score >= 80 -> DeepAnalysisColors.excellent
                    score >= 60 -> DeepAnalysisColors.strong
                    score >= 40 -> DeepAnalysisColors.moderate
                    score >= 20 -> DeepAnalysisColors.weak
                    else -> DeepAnalysisColors.afflicted
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            color = AppTheme.TextMuted
        )
    }
}

/**
 * Localized paragraph display
 */
@Composable
fun LocalizedParagraphText(
    paragraph: LocalizedParagraph,
    useNepali: Boolean = false,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium
) {
    Text(
        text = if (useNepali) paragraph.ne else paragraph.en,
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
    useNepali: Boolean = false,
    modifier: Modifier = Modifier
) {
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
                    text = if (useNepali) trait.nameNe else trait.name,
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
 * Timeline period card
 */
@Composable
fun TimelinePeriodCard(
    title: String,
    dateRange: String,
    description: LocalizedParagraph,
    strength: StrengthLevel,
    useNepali: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DeepAnalysisColors.forStrength(strength).copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(DeepAnalysisColors.forStrength(strength))
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = dateRange,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                LocalizedParagraphText(
                    paragraph = description,
                    useNepali = useNepali,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            StrengthBadge(strength = strength)
        }
    }
}

/**
 * Analysis overview card with scores
 */
@Composable
fun AnalysisOverviewCard(
    title: String,
    scores: List<Pair<String, Double>>,
    summary: LocalizedParagraph,
    useNepali: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.CardBackground
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(scores) { (label, score) ->
                    ScoreIndicator(score = score, label = label)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LocalizedParagraphText(
                paragraph = summary,
                useNepali = useNepali
            )
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
                text = "Analyzing your chart deeply...",
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
