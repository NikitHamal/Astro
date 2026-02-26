package com.astro.storm.ui.components.dialogs

import androidx.compose.foundation.BorderStroke
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.CinzelDecorativeFamily
import com.astro.storm.ui.theme.SpaceGroteskFamily
import com.astro.storm.ui.theme.NeoVedicTokens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Reusable dialog card with icon and title.
 */
@Composable
fun DialogCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.CardBackground,
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = DialogColors.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S16,
                    fontWeight = FontWeight.SemiBold,
                    color = DialogColors.TextPrimary,
                    fontFamily = CinzelDecorativeFamily
                )
            }
            content()
        }
    }
}

/**
 * Detail row displaying a label-value pair.
 */
@Composable
fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = DialogColors.TextPrimary
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
            color = DialogColors.TextMuted,
            fontFamily = SpaceGroteskFamily
        )
        Text(
            text = value,
            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
            fontWeight = FontWeight.Medium,
            color = valueColor,
            fontFamily = SpaceGroteskFamily
        )
    }
}

/**
 * Status chip for displaying conditions.
 */
@Composable
fun StatusChip(
    label: String,
    value: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
            color = DialogColors.TextSecondary,
            fontFamily = SpaceGroteskFamily
        )
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = color.copy(alpha = 0.15f)
        ) {
            Text(
                text = value,
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
                fontWeight = FontWeight.SemiBold,
                color = color,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                fontFamily = SpaceGroteskFamily
            )
        }
    }
}

/**
 * Strength progress row with label, bar, and value.
 */
@Composable
fun StrengthRow(
    label: String,
    value: Double,
    maxValue: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
            color = DialogColors.TextSecondary,
            modifier = Modifier.weight(1f)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { (value / maxValue).coerceIn(0.0, 1.0).toFloat() },
                modifier = Modifier
                    .width(60.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = DialogColors.AccentTeal,
                trackColor = DialogColors.DividerColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = String.format("%.1f", value),
                fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
                fontWeight = FontWeight.Medium,
                color = DialogColors.TextPrimary,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.End
            )
        }
    }
}

/**
 * Summary badge for statistics display.
 */
@Composable
fun SummaryBadge(
    label: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S18,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12,
            color = DialogColors.TextMuted
        )
    }
}

/**
 * Styled divider for dialog sections.
 */
@Composable
fun DialogDivider() {
    HorizontalDivider(
        color = DialogColors.DividerColor,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}
