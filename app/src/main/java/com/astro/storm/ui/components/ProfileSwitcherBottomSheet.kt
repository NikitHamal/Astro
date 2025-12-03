package com.astro.storm.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.repository.SavedChart
import com.astro.storm.ui.theme.AppTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * GitHub-style profile/chart switcher bottom sheet
 *
 * Allows users to switch between saved charts (profiles) or add a new one.
 * Design inspired by GitHub's account switcher but adapted for astrology app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSwitcherBottomSheet(
    savedCharts: List<SavedChart>,
    selectedChartId: Long?,
    onChartSelected: (SavedChart) -> Unit,
    onAddNewChart: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppTheme.BottomSheetBackground,
        contentColor = AppTheme.TextPrimary,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(32.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(AppTheme.BottomSheetHandle)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Header
            Text(
                text = "Switch Profile",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )

            HorizontalDivider(
                color = AppTheme.DividerColor,
                thickness = 1.dp
            )

            // Charts List
            if (savedCharts.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = AppTheme.TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No saved charts",
                            style = MaterialTheme.typography.bodyLarge,
                            color = AppTheme.TextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add your first chart to get started",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextSubtle
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {
                    items(savedCharts) { chart ->
                        ProfileItem(
                            chart = chart,
                            isSelected = chart.id == selectedChartId,
                            onClick = { onChartSelected(chart) }
                        )
                    }
                }
            }

            HorizontalDivider(
                color = AppTheme.DividerColor,
                thickness = 1.dp
            )

            // Add New Chart Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAddNewChart() }
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = AppTheme.AccentPrimary,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new chart",
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Add new chart",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.AccentPrimary
                )
            }
        }
    }
}

@Composable
private fun ProfileItem(
    chart: SavedChart,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) AppTheme.CardBackgroundElevated else AppTheme.BottomSheetBackground,
        label = "background"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar with initials
        ProfileAvatar(
            name = chart.name,
            isSelected = isSelected
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Profile details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chart.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = AppTheme.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = formatChartDetails(chart),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Selection indicator
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = AppTheme.AccentPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ProfileAvatar(
    name: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    size: Int = 44
) {
    val initials = name
        .split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { "?" }

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) AppTheme.AccentPrimary else AppTheme.BorderColor,
        label = "border"
    )

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(AppTheme.CardBackground)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) AppTheme.AccentPrimary else AppTheme.TextSecondary,
            fontSize = (size / 2.5).sp
        )
    }
}

private fun formatChartDetails(chart: SavedChart): String {
    return try {
        val dateTime = LocalDateTime.parse(chart.dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val formattedDate = dateTime.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
        if (chart.location.isNotEmpty()) {
            "$formattedDate • ${chart.location}"
        } else {
            formattedDate
        }
    } catch (e: Exception) {
        chart.location.ifEmpty { "Birth chart" }
    }
}

/**
 * Profile header row that shows current profile with dropdown arrow
 * Used in the top bar of each screen
 */
@Composable
fun ProfileHeaderRow(
    currentChart: SavedChart?,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onProfileClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Current profile name with dropdown indicator
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            currentChart?.let { chart ->
                ProfileAvatar(
                    name = chart.name,
                    isSelected = true,
                    size = 28
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = chart.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } ?: run {
                Text(
                    text = "Select Profile",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextMuted
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Dropdown arrow
            Text(
                text = "▼",
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted,
                fontSize = 10.sp
            )
        }
    }
}
