package com.astro.vajra.ui.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import com.astro.vajra.MainActivity
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyAnalysis
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.ui.theme.AstroVajraTheme
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.NeoVedicTokens

class DebugActivity : ComponentActivity() {

    companion object {
        const val EXTRA_STACK_TRACE = "extra_stack_trace"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val stackTrace = intent.getStringExtra(EXTRA_STACK_TRACE) ?: "No stack trace available."
        val activity = this

        setContent {
            AstroVajraTheme {
                DebugScreen(
                    stackTrace = stackTrace,
                    onRestart = {
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        activity.startActivity(intent)
                        activity.finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugScreen(
    stackTrace: String,
    onRestart: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyAnalysis.DEBUG_UNHANDLED_EXCEPTION)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(NeoVedicTokens.SpaceMD),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(StringKeyAnalysis.DEBUG_ERROR_OCCURRED),
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(NeoVedicTokens.SpaceMD))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(AppTheme.CardBackgroundElevated, shape = MaterialTheme.shapes.medium)
                    .padding(NeoVedicTokens.SpaceXS)
            ) {
                Text(
                    text = stackTrace,
                    color = AppTheme.TextSecondary,
                    fontFamily = FontFamily.Monospace,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                )
            }
            Spacer(modifier = Modifier.height(NeoVedicTokens.SpaceMD))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(NeoVedicTokens.SpaceMD)
            ) {
                OutlinedButton(
                    onClick = { clipboardManager.setText(AnnotatedString(stackTrace)) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AppTheme.AccentPrimary),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(AppTheme.AccentPrimary)
                    )
                ) {
                    Text(stringResource(StringKeyAnalysis.DEBUG_COPY_LOG))
                }

                Button(
                    onClick = onRestart,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.AccentSecondary)
                ) {
                    Text(
                        text = stringResource(StringKeyAnalysis.DEBUG_RESTART_APP),
                        color = Color.White
                    )
                }
            }
        }
    }
}





