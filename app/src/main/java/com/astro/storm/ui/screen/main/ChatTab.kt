package com.astro.storm.ui.screen.main

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.model.FlashyPart
import com.astro.storm.core.model.VedicChart
import com.astro.storm.data.ai.provider.AiModel
import com.astro.storm.data.ai.provider.MessageRole
import com.astro.storm.data.local.chat.ChatConversation
import com.astro.storm.data.local.chat.ChatMessageModel
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.repository.SavedChart
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.AiStatus
import com.astro.storm.ui.viewmodel.ChatUiState
import com.astro.storm.ui.viewmodel.ChatViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatTab(
    viewModel: ChatViewModel,
    currentChart: VedicChart?,
    savedCharts: List<SavedChart>,
    selectedChartId: Long?,
    onNavigateToModels: () -> Unit,
    onNavigateToChat: (Long?) -> Unit,
    isFullScreen: Boolean = false
) {
    val conversations by viewModel.conversations.collectAsState()
    val availableModels by viewModel.availableModels.collectAsState()
    val selectedModel by viewModel.selectedModel.collectAsState()

    ConversationsListScreen(
        conversations = conversations,
        onConversationClick = { onNavigateToChat(it.id) },
        onNewChat = { onNavigateToChat(null) },
        onDeleteConversation = { viewModel.deleteConversation(it.id) },
        onArchiveConversation = { viewModel.archiveConversation(it.id) },
        selectedModel = selectedModel,
        availableModels = availableModels,
        onSelectModel = { viewModel.selectModel(it) },
        onNavigateToModels = onNavigateToModels,
        hasModels = availableModels.isNotEmpty()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    messages: List<ChatMessageModel>,
    flashyParts: List<FlashyPart>, // Live parts from VM
    isStreaming: Boolean,
    aiStatus: AiStatus,
    uiState: ChatUiState,
    selectedModel: AiModel?,
    availableModels: List<AiModel>,
    onSendMessage: (String) -> Unit,
    onCancelStreaming: () -> Unit,
    onSelectModel: (AiModel) -> Unit,
    onBack: () -> Unit,
    onClearChat: () -> Unit,
    onNavigateToModels: () -> Unit
) {
    val colors = AppTheme.current
    val listState = rememberLazyListState()
    var messageText by remember { mutableStateOf("") }
    var showModelSelector by remember { mutableStateOf(false) }
    var showClearConfirm by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Streaming Message ID logic moved to VM, here we just check isStreaming
    LaunchedEffect(messages.size, flashyParts.size, isStreaming) {
        if (messages.isNotEmpty() || isStreaming) {
            listState.animateScrollToItem((messages.size + if (isStreaming) 1 else 0).coerceAtLeast(0))
        }
    }

    Scaffold(
        containerColor = colors.ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(StringKeyDosha.STORMY_TITLE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.TextPrimary
                        )
                        selectedModel?.let {
                            Text(
                                text = it.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.TextMuted
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(StringKeyDosha.CHAT_BACK),
                            tint = colors.TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showModelSelector = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Psychology,
                            contentDescription = stringResource(StringKeyDosha.CHAT_CHANGE_MODEL),
                            tint = colors.TextSecondary
                        )
                    }
                    IconButton(onClick = { showClearConfirm = true }) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteSweep,
                            contentDescription = stringResource(StringKeyDosha.CHAT_CLEAR),
                            tint = colors.TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.ScreenBackground)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (messages.isEmpty() && !isStreaming) {
                    item { WelcomeMessage { messageText = it } }
                }

                // Render history
                items(items = messages, key = { it.id }) { message ->
                    if (message.role == MessageRole.USER) {
                        UserMessageBubble(message)
                    } else {
                        // Map historical message to FlashyParts
                        val parts = remember(message) {
                            buildList {
                                if (!message.reasoningContent.isNullOrBlank()) {
                                    add(FlashyPart.Thought(message.reasoningContent))
                                }
                                add(FlashyPart.Text(message.content))
                            }
                        }
                        FlashyAiMessage(parts = parts)
                    }
                }

                // Render streaming message
                if (isStreaming || flashyParts.isNotEmpty()) {
                    item(key = "streaming_message") {
                        FlashyAiMessage(parts = flashyParts)
                        
                        // Status indicator below
                        if (aiStatus != AiStatus.Idle && aiStatus != AiStatus.Streaming) {
                             AiStatusIndicator(aiStatus)
                        }
                    }
                }
            }

            ChatInputArea(
                messageText = messageText,
                onMessageTextChange = { messageText = it },
                onSend = {
                    if (messageText.isNotBlank()) {
                        onSendMessage(messageText.trim())
                        messageText = ""
                        focusManager.clearFocus()
                    }
                },
                onCancel = onCancelStreaming,
                isStreaming = isStreaming,
                isSending = uiState == ChatUiState.Sending,
                enabled = selectedModel != null
            )
        }
    }

    if (showModelSelector) {
        ModelSelectorBottomSheet(
            models = availableModels,
            selectedModel = selectedModel,
            onSelectModel = { onSelectModel(it); showModelSelector = false },
            onDismiss = { showModelSelector = false },
            onNavigateToModels = { showModelSelector = false; onNavigateToModels() }
        )
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text(stringResource(StringKeyDosha.CHAT_CLEAR)) },
            text = { Text(stringResource(StringKeyDosha.CHAT_CLEAR_CONFIRM)) },
            confirmButton = {
                TextButton(onClick = { onClearChat(); showClearConfirm = false }) {
                    Text(stringResource(StringKeyDosha.CHAT_CLEAR_BTN), color = colors.ErrorColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) {
                    Text(stringResource(StringKeyDosha.CHAT_CANCEL_BTN))
                }
            },
            containerColor = colors.CardBackground
        )
    }
}

// Reuse existing UserMessageBubble, AiStatusIndicator, etc. or keep simplified versions here
// For brevity, I'll keep the ones from previous implementation that were robust

@Composable
private fun UserMessageBubble(message: ChatMessageModel) {
    val colors = AppTheme.current
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
        Surface(
            color = colors.AccentPrimary,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp),
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.ScreenBackground,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
private fun AiStatusIndicator(aiStatus: AiStatus) {
    val colors = AppTheme.current
    val text = when(aiStatus) {
        is AiStatus.Thinking -> "Thinking..."
        else -> "Processing..."
    }
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 2.dp, color = colors.TextMuted)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.labelSmall, color = colors.TextMuted)
    }
}

@Composable
private fun ChatInputArea(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onCancel: () -> Unit,
    isStreaming: Boolean,
    isSending: Boolean,
    enabled: Boolean
) {
    val colors = AppTheme.current
    Surface(color = colors.CardBackground, tonalElevation = 2.dp, modifier = Modifier.imePadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text(stringResource(StringKeyDosha.STORMY_ASK_PLACEHOLDER), color = colors.TextSubtle, fontSize = 14.sp) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp, color = colors.TextPrimary),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.AccentPrimary,
                    unfocusedBorderColor = colors.BorderColor,
                    focusedContainerColor = colors.InputBackground,
                    unfocusedContainerColor = colors.InputBackground,
                    cursorColor = colors.AccentPrimary
                ),
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { if (messageText.isNotBlank() && enabled && !isStreaming) onSend() }),
                maxLines = 4,
                enabled = enabled && !isSending
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (isStreaming) {
                IconButton(onClick = onCancel, modifier = Modifier.size(48.dp).clip(CircleShape).background(colors.ErrorColor)) {
                    Icon(Icons.Default.Stop, contentDescription = null, tint = Color.White)
                }
            } else {
                IconButton(
                    onClick = onSend,
                    enabled = messageText.isNotBlank() && enabled && !isSending,
                    modifier = Modifier.size(48.dp).clip(CircleShape).background(if (messageText.isNotBlank() && enabled) colors.AccentPrimary else colors.ChipBackground)
                ) {
                    if (isSending) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = colors.TextMuted)
                    else Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = if (messageText.isNotBlank() && enabled) colors.ScreenBackground else colors.TextMuted)
                }
            }
        }
    }
}

// Keep the ConversationsListScreen and helpers as they were (mostly unchanged structure)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConversationsListScreen(
    conversations: List<ChatConversation>,
    onConversationClick: (ChatConversation) -> Unit,
    onNewChat: () -> Unit,
    onDeleteConversation: (ChatConversation) -> Unit,
    onArchiveConversation: (ChatConversation) -> Unit,
    selectedModel: AiModel?,
    availableModels: List<AiModel>,
    onSelectModel: (AiModel) -> Unit,
    onNavigateToModels: () -> Unit,
    hasModels: Boolean
) {
    val colors = AppTheme.current
    var conversationToDelete by remember { mutableStateOf<ChatConversation?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(colors.ScreenBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (conversations.isEmpty()) {
                EmptyChatState(hasModels, onNewChat, onNavigateToModels)
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = conversations, key = { it.id }) { conversation ->
                        ConversationCard(conversation, { onConversationClick(conversation) }, { conversationToDelete = conversation }, { onArchiveConversation(conversation) })
                    }
                }
            }
        }
        if (hasModels && conversations.isNotEmpty()) {
            FloatingActionButton(
                onClick = onNewChat,
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                containerColor = colors.AccentPrimary,
                contentColor = colors.ScreenBackground
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(StringKeyDosha.CHAT_NEW))
            }
        }
    }
    conversationToDelete?.let { conversation ->
        AlertDialog(
            onDismissRequest = { conversationToDelete = null },
            title = { Text(stringResource(StringKeyDosha.CHAT_DELETE)) },
            text = { Text(stringResource(StringKeyDosha.CHAT_DELETE_CONFIRM, conversation.title)) },
            confirmButton = { TextButton(onClick = { onDeleteConversation(conversation); conversationToDelete = null }) { Text(stringResource(StringKeyDosha.CHAT_DELETE_BTN), color = colors.ErrorColor) } },
            dismissButton = { TextButton(onClick = { conversationToDelete = null }) { Text(stringResource(StringKeyDosha.CHAT_CANCEL_BTN)) } },
            containerColor = colors.CardBackground
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConversationCard(conversation: ChatConversation, onClick: () -> Unit, onDelete: () -> Unit, onArchive: () -> Unit) {
    val colors = AppTheme.current
    var showMenu by remember { mutableStateOf(false) }
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = colors.CardBackground), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(colors.ChipBackground), contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.Chat, contentDescription = null, tint = colors.AccentPrimary, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(conversation.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium, color = colors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                conversation.lastMessagePreview?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = colors.TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(formatTimestamp(conversation.updatedAt), style = MaterialTheme.typography.labelSmall, color = colors.TextSubtle)
                    Text("•", style = MaterialTheme.typography.labelSmall, color = colors.TextSubtle)
                    Text(stringResource(StringKeyDosha.CHAT_MESSAGES_COUNT, conversation.messageCount), style = MaterialTheme.typography.labelSmall, color = colors.TextSubtle)
                }
            }
            Box {
                IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, contentDescription = null, tint = colors.TextMuted) }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(text = { Text(stringResource(StringKeyDosha.CHAT_ARCHIVE)) }, onClick = { showMenu = false; onArchive() }, leadingIcon = { Icon(Icons.Outlined.Archive, contentDescription = null) })
                    DropdownMenuItem(text = { Text(stringResource(StringKeyDosha.CHAT_DELETE_BTN), color = colors.ErrorColor) }, onClick = { showMenu = false; onDelete() }, leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null, tint = colors.ErrorColor) })
                }
            }
        }
    }
}

@Composable
private fun EmptyChatState(hasModels: Boolean, onNewChat: () -> Unit, onNavigateToModels: () -> Unit) {
    val colors = AppTheme.current
    Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Box(modifier = Modifier.size(120.dp).clip(CircleShape).background(Brush.radialGradient(listOf(colors.AccentGold.copy(alpha = 0.3f), colors.AccentPrimary.copy(alpha = 0.1f)))), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.AutoAwesome, contentDescription = null, modifier = Modifier.size(56.dp), tint = colors.AccentGold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(stringResource(StringKeyDosha.STORMY_MEET), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = colors.TextPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(StringKeyDosha.STORMY_SUBTITLE), style = MaterialTheme.typography.bodyLarge, color = colors.TextSecondary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(StringKeyDosha.STORMY_INTRO), style = MaterialTheme.typography.bodyMedium, color = colors.TextMuted, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(32.dp))
        if (hasModels) {
            Button(onClick = onNewChat, colors = ButtonDefaults.buttonColors(containerColor = colors.AccentPrimary, contentColor = colors.ScreenBackground), modifier = Modifier.fillMaxWidth(0.7f)) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(StringKeyDosha.STORMY_START_CHAT))
            }
        } else {
            OutlinedButton(onClick = onNavigateToModels, colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.AccentPrimary), modifier = Modifier.fillMaxWidth(0.7f)) {
                Icon(Icons.Outlined.Settings, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(StringKeyDosha.STORMY_CONFIGURE_MODELS), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(StringKeyDosha.STORMY_ENABLE_MODELS), style = MaterialTheme.typography.bodySmall, color = colors.TextMuted)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModelSelectorBottomSheet(models: List<AiModel>, selectedModel: AiModel?, onSelectModel: (AiModel) -> Unit, onDismiss: () -> Unit, onNavigateToModels: () -> Unit) {
    val colors = AppTheme.current
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState, containerColor = colors.BottomSheetBackground, dragHandle = { Surface(modifier = Modifier.padding(vertical = 12.dp).width(32.dp).height(4.dp), color = colors.BottomSheetHandle, shape = RoundedCornerShape(2.dp)) {} }) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 32.dp)) {
            Text(stringResource(StringKeyDosha.MODEL_SELECT_TITLE), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = colors.TextPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            if (models.isEmpty()) {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.Psychology, contentDescription = null, tint = colors.TextMuted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(stringResource(StringKeyDosha.MODEL_NONE_AVAILABLE), style = MaterialTheme.typography.bodyMedium, color = colors.TextMuted)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onNavigateToModels) { Text(stringResource(StringKeyDosha.MODEL_CONFIGURE)) }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val groupedModels = models.groupBy { it.providerId }
                    groupedModels.forEach { (providerId, providerModels) ->
                        item { Text(providerId.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.labelMedium, color = colors.TextMuted, modifier = Modifier.padding(vertical = 4.dp)) }
                        items(providerModels) { model ->
                            val isSelected = model.id == selectedModel?.id && model.providerId == selectedModel?.providerId
                            Surface(onClick = { onSelectModel(model) }, color = if (isSelected) colors.ChipBackgroundSelected else colors.CardBackground, shape = RoundedCornerShape(12.dp)) {
                                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(model.displayName, style = MaterialTheme.typography.bodyMedium, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal, color = colors.TextPrimary)
                                        model.description?.let { desc -> Text(desc, style = MaterialTheme.typography.labelSmall, color = colors.TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                                    }
                                    if (isSelected) Icon(Icons.Default.Check, contentDescription = null, tint = colors.AccentPrimary)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = onNavigateToModels, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Outlined.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(StringKeyDosha.MODEL_MANAGE))
                }
            }
        }
    }
}

@Composable
private fun WelcomeMessage(onSuggestionClick: (String) -> Unit) {
    val colors = AppTheme.current
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Brush.radialGradient(listOf(colors.AccentGold.copy(alpha = 0.3f), colors.AccentPrimary.copy(alpha = 0.1f)))), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.AutoAwesome, contentDescription = null, modifier = Modifier.size(40.dp), tint = colors.AccentGold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(StringKeyDosha.STORMY_HELLO), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = colors.TextPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(StringKeyDosha.STORMY_HELLO_DESC), style = MaterialTheme.typography.bodyMedium, color = colors.TextMuted, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 32.dp))
        Spacer(modifier = Modifier.height(16.dp))
        val suggestionDasha = stringResource(StringKeyDosha.CHAT_SUGGESTION_DASHA)
        val suggestionChart = stringResource(StringKeyDosha.CHAT_SUGGESTION_CHART)
        val suggestionYogas = stringResource(StringKeyDosha.CHAT_SUGGESTION_YOGAS)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SuggestionChip(suggestionDasha) { onSuggestionClick(suggestionDasha) }
            SuggestionChip(suggestionChart) { onSuggestionClick(suggestionChart) }
            SuggestionChip(suggestionYogas) { onSuggestionClick(suggestionYogas) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuggestionChip(text: String, onClick: () -> Unit) {
    val colors = AppTheme.current
    SuggestionChip(onClick = onClick, label = { Text(text, style = MaterialTheme.typography.bodySmall) }, colors = SuggestionChipDefaults.suggestionChipColors(containerColor = colors.ChipBackground, labelColor = colors.TextSecondary), border = null)
}

@Composable
@androidx.compose.runtime.ReadOnlyComposable
private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60_000 -> stringResource(StringKeyDosha.CHAT_JUST_NOW)
        diff < 3600_000 -> stringResource(StringKeyDosha.CHAT_MINUTES_AGO, (diff / 60_000).toInt())
        diff < 86400_000 -> stringResource(StringKeyDosha.CHAT_HOURS_AGO, (diff / 3600_000).toInt())
        diff < 604800_000 -> stringResource(StringKeyDosha.CHAT_DAYS_AGO, (diff / 86400_000).toInt())
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timestamp))
    }
}