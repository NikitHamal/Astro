package com.astro.storm.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.model.FlashyPart
import com.astro.storm.core.model.ToolState
import com.astro.storm.ui.components.MarkdownText
import com.astro.storm.ui.theme.AppTheme

// ==========================================
// UI Components
// ==========================================

@Composable
fun FlashyAiMessage(
    parts: List<FlashyPart>,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.current
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        parts.forEach { part ->
            when (part) {
                is FlashyPart.Thought -> ThoughtBlock(part)
                is FlashyPart.ToolCall -> ToolPill(part)
                is FlashyPart.ToolResult -> ToolResultBlock(part)
                is FlashyPart.Text -> MarkdownText(
                    markdown = part.content,
                    modifier = Modifier.fillMaxWidth(),
                    color = colors.TextPrimary,
                    fontSize = 15.sp,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

@Composable
fun ThoughtBlock(thought: FlashyPart.Thought) {
    val colors = AppTheme.current
    var isExpanded by remember(thought.isCollapsed) { mutableStateOf(!thought.isCollapsed) }
    val rotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.BorderColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(colors.CardBackground.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Psychology,
                contentDescription = null,
                tint = colors.TextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Thought Process",
                style = MaterialTheme.typography.labelMedium,
                color = colors.TextSecondary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                tint = colors.TextSecondary,
                modifier = Modifier.size(16.dp).rotate(rotation)
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .padding(top = 0.dp)
            ) {
                Text(
                    text = thought.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.TextMuted,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ToolPill(tool: FlashyPart.ToolCall) {
    val colors = AppTheme.current
    val isExecuting = tool.state == ToolState.Executing
    var isExpanded by remember { mutableStateOf(tool.isExpanded) }
    
    // Map tool names to icons
    val icon = when {
        "file" in tool.name -> Icons.Outlined.Description
        "web" in tool.name || "search" in tool.name -> Icons.Outlined.Public
        "calculate" in tool.name -> Icons.Outlined.Calculate
        "chart" in tool.name -> Icons.Outlined.InsertChart
        else -> Icons.Outlined.Code
    }

    Surface(
        color = if (isExecuting) colors.AccentPrimary.copy(alpha = 0.1f) else colors.ChipBackground,
        shape = RoundedCornerShape(16.dp),
        border = if (isExecuting) BorderStroke(1.dp, colors.AccentPrimary.copy(alpha = 0.3f)) else null,
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isExecuting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        strokeWidth = 2.dp,
                        color = colors.AccentPrimary
                    )
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (tool.state == ToolState.Failed) colors.ErrorColor else colors.AccentPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = tool.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = tool.args.take(50) + if (tool.args.length > 50) "..." else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.TextMuted,
                    maxLines = 1,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.weight(1f)
                )
                
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = colors.TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
            
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .background(colors.ScreenBackground.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Arguments:",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.TextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = tool.args,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.TextMuted,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ToolResultBlock(result: FlashyPart.ToolResult) {
    val colors = AppTheme.current
    var isExpanded by remember { mutableStateOf(false) }
    
    Surface(
        color = if (result.isError) colors.ErrorColor.copy(alpha = 0.05f) else colors.SuccessColor.copy(alpha = 0.05f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, if (result.isError) colors.ErrorColor.copy(alpha = 0.2f) else colors.SuccessColor.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth().padding(start = 24.dp, bottom = 8.dp) // Indent to show connection to tool
    ) {
        Column {
            Row(
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (result.isError) Icons.Outlined.Error else Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = if (result.isError) colors.ErrorColor else colors.SuccessColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (result.isError) "Error" else "Result",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (result.isError) colors.ErrorColor else colors.SuccessColor,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = colors.TextMuted,
                    modifier = Modifier.size(14.dp)
                )
            }
            
            AnimatedVisibility(visible = isExpanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .padding(top = 0.dp)
                ) {
                    Text(
                        text = result.result,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.TextPrimary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
