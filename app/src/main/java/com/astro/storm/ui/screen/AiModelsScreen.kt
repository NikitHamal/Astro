package com.astro.storm.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.astro.storm.data.ai.provider.AiModel
import com.astro.storm.data.ai.provider.AiProviderRegistry
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.data.localization.stringResource
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.launch

/**
 * AI Models Settings Screen
 *
 * Allows users to:
 * - View available AI providers and models
 * - Enable/disable models
 * - Set default model
 * - Refresh model list
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiModelsScreen(
    providerRegistry: AiProviderRegistry,
    onBack: () -> Unit
) {
    val colors = AppTheme.current
    val scope = rememberCoroutineScope()

    // Initialize provider registry if not already loaded
    LaunchedEffect(Unit) {
        if (providerRegistry.allModels.value.isEmpty()) {
            providerRegistry.initialize()
        }
    }

    // Collect states
    val allModels by providerRegistry.allModels.collectAsState()
    val enabledModels by providerRegistry.enabledModels.collectAsState()
    val defaultModel = providerRegistry.getDefaultModel()

    var isRefreshing by remember { mutableStateOf(false) }
    var selectedProvider by remember { mutableStateOf<String?>(null) }
    var nvidiaApiKeyInput by remember { mutableStateOf(providerRegistry.getProviderApiKey("nvidia").orEmpty()) }
    var isSavingNvidiaKey by remember { mutableStateOf(false) }

    val enabledModelKeys = remember(enabledModels) {
        enabledModels.asSequence()
            .map { "${it.providerId}::${it.id}" }
            .toSet()
    }
    // Group models by provider once per model list update.
    val modelsByProvider = remember(allModels) {
        allModels.groupBy { it.providerId }
    }

    Scaffold(
        containerColor = colors.ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(StringKeyDosha.AI_MODELS_TITLE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.TextPrimary
                        )
                        Text(
                            text = stringResource(StringKeyDosha.AI_MODELS_ENABLED_COUNT, enabledModels.size),
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(StringKeyDosha.AI_MODELS_BACK),
                            tint = colors.TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                isRefreshing = true
                                providerRegistry.refreshModels()
                                isRefreshing = false
                            }
                        },
                        enabled = !isRefreshing
                    ) {
                        if (isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = colors.AccentPrimary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(StringKeyDosha.AI_MODELS_REFRESH),
                                tint = colors.TextSecondary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.ScreenBackground
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Info card
            item {
                InfoCard()
            }

            // NVIDIA API key settings
            item {
                ProviderApiKeySection(
                    providerName = "NVIDIA NIM",
                    apiKey = nvidiaApiKeyInput,
                    onApiKeyChange = { nvidiaApiKeyInput = it },
                    isSaving = isSavingNvidiaKey,
                    onSave = {
                        scope.launch {
                            isSavingNvidiaKey = true
                            providerRegistry.setProviderApiKey("nvidia", nvidiaApiKeyInput)
                            isSavingNvidiaKey = false
                        }
                    },
                    onClear = {
                        scope.launch {
                            isSavingNvidiaKey = true
                            nvidiaApiKeyInput = ""
                            providerRegistry.setProviderApiKey("nvidia", null)
                            isSavingNvidiaKey = false
                        }
                    }
                )
            }

            // Default model selector
            if (enabledModels.isNotEmpty()) {
                item {
                    DefaultModelSection(
                        defaultModel = defaultModel,
                        enabledModels = enabledModels,
                        onSetDefault = { /* Default model is always the first enabled one */ }
                    )
                }
            }

            // Providers
            modelsByProvider.forEach { (providerId, models) ->
                item {
                    ProviderSection(
                        providerId = providerId,
                        models = models,
                        enabledModelKeys = enabledModelKeys,
                        isExpanded = selectedProvider == providerId,
                        onToggleExpanded = {
                            selectedProvider = if (selectedProvider == providerId) null else providerId
                        },
                        onToggleModel = { model, enabled ->
                            scope.launch {
                                providerRegistry.setModelEnabled(model.providerId, model.id, enabled)
                            }
                        },
                        onEnableAll = {
                            scope.launch {
                                providerRegistry.enableAllModels(providerId)
                            }
                        },
                        onDisableAll = {
                            scope.launch {
                                providerRegistry.disableAllModels(providerId)
                            }
                        }
                    )
                }
            }

            // Empty state
            if (allModels.isEmpty()) {
                item {
                    EmptyModelsState(
                        onRefresh = {
                            scope.launch {
                                isRefreshing = true
                                providerRegistry.refreshModels()
                                isRefreshing = false
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProviderApiKeySection(
    providerName: String,
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    isSaving: Boolean,
    onSave: () -> Unit,
    onClear: () -> Unit
) {
    val colors = AppTheme.current
    var showKey by remember { mutableStateOf(false) }

    Surface(
        color = colors.CardBackground,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = colors.AccentPrimary
                )
                Text(
                    text = "$providerName API Key",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )
            }

            Text(
                text = "Paste your NVIDIA key from build.nvidia.com to enable NVIDIA-hosted free models.",
                style = MaterialTheme.typography.bodySmall,
                color = colors.TextMuted
            )

            OutlinedTextField(
                value = apiKey,
                onValueChange = onApiKeyChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("nvapi-...") },
                label = { Text("NVIDIA API Key") },
                visualTransformation = if (showKey) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showKey = !showKey }) {
                        Icon(
                            imageVector = if (showKey) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (showKey) "Hide key" else "Show key"
                        )
                    }
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onSave,
                    enabled = !isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Save Key")
                    }
                }
                OutlinedButton(
                    onClick = onClear,
                    enabled = !isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Clear Key")
                }
            }
        }
    }
}

@Composable
private fun InfoCard() {
    val colors = AppTheme.current

    Surface(
        color = colors.CardBackground,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = colors.InfoColor,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = stringResource(StringKeyDosha.AI_MODELS_FREE_TITLE),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(StringKeyDosha.AI_MODELS_FREE_DESC),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.TextMuted
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DefaultModelSection(
    defaultModel: AiModel?,
    enabledModels: List<AiModel>,
    onSetDefault: (AiModel) -> Unit
) {
    val colors = AppTheme.current
    var showSelector by remember { mutableStateOf(false) }

    Surface(
        onClick = { showSelector = true },
        color = colors.CardBackground,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(colors.AccentGold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = colors.AccentGold,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(StringKeyDosha.AI_MODELS_DEFAULT),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = colors.TextPrimary
                )
                Text(
                    text = defaultModel?.displayName ?: stringResource(StringKeyDosha.AI_MODELS_NOT_SET),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.TextMuted
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = colors.TextMuted
            )
        }
    }

    // Selector dialog
    if (showSelector) {
        AlertDialog(
            onDismissRequest = { showSelector = false },
            title = { Text(stringResource(StringKeyDosha.AI_MODELS_SELECT_DEFAULT)) },
            text = {
                LazyColumn {
                    items(
                        items = enabledModels,
                        key = { model -> "${model.providerId}::${model.id}" }
                    ) { model ->
                        val isSelected = model.id == defaultModel?.id &&
                                model.providerId == defaultModel?.providerId

                        Surface(
                            onClick = {
                                onSetDefault(model)
                                showSelector = false
                            },
                            color = if (isSelected) colors.ChipBackgroundSelected else colors.CardBackground,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = model.displayName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                    Text(
                                        text = model.providerId,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colors.TextMuted
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = colors.AccentPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSelector = false }) {
                    Text(stringResource(StringKeyDosha.AI_MODELS_CANCEL))
                }
            },
            containerColor = colors.CardBackground
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProviderSection(
    providerId: String,
    models: List<AiModel>,
    enabledModelKeys: Set<String>,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    onToggleModel: (AiModel, Boolean) -> Unit,
    onEnableAll: () -> Unit = {},
    onDisableAll: () -> Unit = {}
) {
    val colors = AppTheme.current
    val enabledCount = models.count { model ->
        enabledModelKeys.contains("${model.providerId}::${model.id}")
    }
    val allEnabled = enabledCount == models.size
    val noneEnabled = enabledCount == 0

    val providerInfo = getProviderInfo(providerId)

    Surface(
        color = colors.CardBackground,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Header
            Surface(
                onClick = onToggleExpanded,
                color = colors.CardBackground
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(providerInfo.color.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = providerInfo.icon,
                            contentDescription = null,
                            tint = providerInfo.color,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = providerInfo.displayName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.TextPrimary
                        )
                        Text(
                            text = stringResource(StringKeyDosha.AI_MODELS_MODELS_ENABLED, enabledCount, models.size),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.TextMuted
                        )
                    }

                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = colors.TextMuted
                    )
                }
            }

            // Models list
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    HorizontalDivider(color = colors.DividerColor)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Enable/Disable All buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onEnableAll,
                            enabled = !allEnabled,
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = colors.AccentPrimary,
                                disabledContentColor = colors.TextMuted
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(StringKeyDosha.AI_MODELS_ENABLE_ALL),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        OutlinedButton(
                            onClick = onDisableAll,
                            enabled = !noneEnabled,
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = colors.TextSecondary,
                                disabledContentColor = colors.TextMuted
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(StringKeyDosha.AI_MODELS_DISABLE_ALL),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    HorizontalDivider(color = colors.DividerColor.copy(alpha = 0.5f))

                    Spacer(modifier = Modifier.height(8.dp))

                    models.forEach { model ->
                        val isEnabled = enabledModelKeys.contains("${model.providerId}::${model.id}")

                        ModelItem(
                            model = model,
                            isEnabled = isEnabled,
                            onToggle = { onToggleModel(model, it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModelItem(
    model: AiModel,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val colors = AppTheme.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!isEnabled) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = model.displayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isEnabled) FontWeight.Medium else FontWeight.Normal,
                color = if (isEnabled) colors.TextPrimary else colors.TextSecondary
            )

            model.description?.let { desc ->
                Text(
                    text = desc,
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Capabilities badges
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                if (model.supportsTools) {
                    CapabilityBadge(stringResource(StringKeyDosha.AI_MODELS_TOOLS))
                }
                if (model.supportsReasoning) {
                    CapabilityBadge(stringResource(StringKeyDosha.AI_MODELS_REASONING))
                }
                if (model.supportsVision) {
                    CapabilityBadge(stringResource(StringKeyDosha.AI_MODELS_VISION))
                }
            }
        }

        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = colors.ScreenBackground,
                checkedTrackColor = colors.AccentPrimary,
                uncheckedThumbColor = colors.TextMuted,
                uncheckedTrackColor = colors.ChipBackground
            )
        )
    }
}

@Composable
private fun CapabilityBadge(text: String) {
    val colors = AppTheme.current

    Surface(
        color = colors.ChipBackground,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = colors.TextMuted,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun EmptyModelsState(onRefresh: () -> Unit) {
    val colors = AppTheme.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.CloudOff,
            contentDescription = null,
            tint = colors.TextMuted,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(StringKeyDosha.AI_MODELS_NONE),
            style = MaterialTheme.typography.titleMedium,
            color = colors.TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(StringKeyDosha.AI_MODELS_NONE_DESC),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.TextMuted,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = onRefresh) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(StringKeyDosha.AI_MODELS_RETRY))
        }
    }
}

// Provider info data
private data class ProviderInfo(
    val displayName: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: androidx.compose.ui.graphics.Color
)

@Composable
private fun getProviderInfo(providerId: String): ProviderInfo {
    return when (providerId.lowercase()) {
        "deepinfra" -> ProviderInfo(
            displayName = stringResource(StringKeyDosha.AI_PROVIDER_DEEPINFRA),
            icon = Icons.Outlined.Speed,
            color = androidx.compose.ui.graphics.Color(0xFF7C3AED)
        )
        "nvidia" -> ProviderInfo(
            displayName = "NVIDIA NIM",
            icon = Icons.Outlined.Speed,
            color = androidx.compose.ui.graphics.Color(0xFF76B900)
        )
        "qwen" -> ProviderInfo(
            displayName = stringResource(StringKeyDosha.AI_PROVIDER_QWEN),
            icon = Icons.Outlined.Psychology,
            color = androidx.compose.ui.graphics.Color(0xFF2563EB)
        )
        "blackbox" -> ProviderInfo(
            displayName = stringResource(StringKeyDosha.AI_PROVIDER_BLACKBOX),
            icon = Icons.Outlined.Code,
            color = androidx.compose.ui.graphics.Color(0xFF1A1A1A)
        )
        "ddg" -> ProviderInfo(
            displayName = stringResource(StringKeyDosha.AI_PROVIDER_DDG),
            icon = Icons.Outlined.Shield,
            color = androidx.compose.ui.graphics.Color(0xFFDE5833)
        )
        else -> ProviderInfo(
            displayName = providerId.replaceFirstChar { it.uppercase() },
            icon = Icons.Outlined.AutoAwesome,
            color = androidx.compose.ui.graphics.Color(0xFF6B7280)
        )
    }
}

