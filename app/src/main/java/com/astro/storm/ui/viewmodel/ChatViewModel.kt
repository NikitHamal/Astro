package com.astro.storm.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.astro.storm.core.common.Language
import com.astro.storm.core.model.AgentStreamState
import com.astro.storm.core.model.FlashyPart
import com.astro.storm.core.model.ToolState
import com.astro.storm.core.model.VedicChart
import com.astro.storm.data.ai.agent.StormyAgent
import com.astro.storm.data.ai.provider.AiModel
import com.astro.storm.data.ai.provider.AiProviderRegistry
import com.astro.storm.data.ai.provider.ChatMessage
import com.astro.storm.data.local.chat.ChatConversation
import com.astro.storm.data.local.chat.ChatMessageModel
import com.astro.storm.data.repository.ChatRepository
import com.astro.storm.data.repository.SavedChart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AiStatus {
    object Idle : AiStatus()
    object Thinking : AiStatus()
    object Streaming : AiStatus()
}

@HiltViewModel
class ChatViewModel @Inject constructor(
    application: Application,
    private val chatRepository: ChatRepository,
    private val providerRegistry: AiProviderRegistry,
    private val stormyAgent: StormyAgent
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Idle)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _currentConversationId = MutableStateFlow<Long?>(null)
    val currentConversationId: StateFlow<Long?> = _currentConversationId.asStateFlow()

    private val _flashyParts = MutableStateFlow<List<FlashyPart>>(emptyList())
    val flashyParts: StateFlow<List<FlashyPart>> = _flashyParts.asStateFlow()

    private val _aiStatus = MutableStateFlow<AiStatus>(AiStatus.Idle)
    val aiStatus: StateFlow<AiStatus> = _aiStatus.asStateFlow()

    private val _selectedModel = MutableStateFlow<AiModel?>(null)
    val selectedModel: StateFlow<AiModel?> = _selectedModel.asStateFlow()

    private val _streamingMessageId = MutableStateFlow<Long?>(null)
    val streamingMessageId: StateFlow<Long?> = _streamingMessageId.asStateFlow()

    private var streamingJob: Job? = null
    private var pendingConversationContext: PendingConversationContext? = null

    data class PendingConversationContext(
        val currentChart: VedicChart?,
        val savedCharts: List<SavedChart>,
        val selectedChartId: Long?
    )

    val conversations: StateFlow<List<ChatConversation>> = chatRepository
        .getAllConversations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentMessages: StateFlow<List<ChatMessageModel>> = _currentConversationId
        .flatMapLatest { id ->
            if (id != null) chatRepository.getMessagesForConversation(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableModels: StateFlow<List<AiModel>> = providerRegistry.enabledModels
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            providerRegistry.initialize()
            _selectedModel.value = providerRegistry.getDefaultModel()
        }
    }

    fun createConversation(currentChart: VedicChart?, savedCharts: List<SavedChart>, selectedChartId: Long?) {
        viewModelScope.launch {
            pendingConversationContext = PendingConversationContext(currentChart, savedCharts, selectedChartId)
            _currentConversationId.value = null
            _flashyParts.value = emptyList()
        }
    }

    fun openConversation(conversationId: Long, currentChart: VedicChart?, savedCharts: List<SavedChart>, selectedChartId: Long?) {
        viewModelScope.launch {
            _currentConversationId.value = conversationId
            _flashyParts.value = emptyList() // Reset streaming parts
            
            // Restore model selection
            chatRepository.getConversationEntityById(conversationId)?.let { conv ->
                availableModels.value.find { it.id == conv.modelId }?.let { _selectedModel.value = it }
            }
        }
    }

    fun deleteConversation(id: Long) {
        viewModelScope.launch {
            if (_currentConversationId.value == id) _currentConversationId.value = null
            chatRepository.deleteConversation(id)
        }
    }
    
    fun archiveConversation(id: Long) {
        viewModelScope.launch { chatRepository.archiveConversation(id) }
    }

    fun selectModel(model: AiModel) {
        _selectedModel.value = model
        _currentConversationId.value?.let { id ->
            viewModelScope.launch { chatRepository.updateConversationModel(id, model.id, model.providerId) }
        }
    }

    fun sendMessage(
        content: String,
        currentChart: VedicChart?,
        savedCharts: List<SavedChart>,
        selectedChartId: Long?,
        language: Language = Language.ENGLISH
    ) {
        val model = _selectedModel.value ?: return
        cancelStreaming()

        viewModelScope.launch {
            try {
                _uiState.value = ChatUiState.Sending
                _aiStatus.value = AiStatus.Thinking
                _flashyParts.value = emptyList()

                val conversationId = _currentConversationId.value ?: run {
                    val newId = chatRepository.createConversation(
                        title = "New Chat",
                        modelId = model.id,
                        providerId = model.providerId,
                        profileId = selectedChartId
                    )
                    _currentConversationId.value = newId
                    newId
                }

                chatRepository.addUserMessage(conversationId, content)
                val assistantMsgId = chatRepository.addAssistantMessagePlaceholder(conversationId, model.id)
                _streamingMessageId.value = assistantMsgId

                val history = chatRepository.getMessagesForConversationSync(conversationId)
                    .dropLast(1) // exclude placeholder
                    .map { ChatMessage(it.role, it.content) }

                val currentProfile = savedCharts.find { it.id == selectedChartId }

                streamingJob = launch {
                    stormyAgent.processMessage(
                        messages = history,
                        model = model,
                        currentProfile = currentProfile,
                        allProfiles = savedCharts,
                        currentChart = currentChart,
                        language = language
                    ).collect { state ->
                        when (state) {
                            is AgentStreamState.Update -> {
                                _flashyParts.value = state.parts
                                _aiStatus.value = AiStatus.Streaming
                                _uiState.value = ChatUiState.Streaming
                            }
                            is AgentStreamState.Finished -> {
                                _flashyParts.value = state.parts
                                finalizeMessage(assistantMsgId, state.parts)
                            }
                            is AgentStreamState.Error -> {
                                _uiState.value = ChatUiState.Error(state.message)
                                chatRepository.setMessageError(assistantMsgId, state.message)
                            }
                            is AgentStreamState.Interrupt -> {
                                // Handle ask_user (TODO: Implement UI for this if needed, for now just log/finish)
                                // In Flashy UI, this would show input fields. 
                                // For MVP revamp, we'll stop here or auto-continue if possible.
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ChatUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun finalizeMessage(msgId: Long, parts: List<FlashyPart>) {
        val content = parts.filterIsInstance<FlashyPart.Text>().joinToString("\n\n") { it.content }
        val reasoning = parts.filterIsInstance<FlashyPart.Thought>().joinToString("\n\n") { it.content }
        
        chatRepository.finalizeAssistantMessage(
            messageId = msgId,
            content = content,
            reasoningContent = reasoning.takeIf { it.isNotEmpty() },
            toolsUsed = parts.filterIsInstance<FlashyPart.ToolCall>().map { it.name },
            sectionsJson = null // We can serialize parts if we want to persist section structure later
        )
        
        _streamingMessageId.value = null
        _aiStatus.value = AiStatus.Idle
        _uiState.value = ChatUiState.Idle
    }

    fun cancelStreaming() {
        streamingJob?.cancel()
        _streamingMessageId.value = null
        _aiStatus.value = AiStatus.Idle
        _uiState.value = ChatUiState.Idle
    }
}

sealed class ChatUiState {
    object Idle : ChatUiState()
    object Sending : ChatUiState()
    object Streaming : ChatUiState()
    data class Error(val message: String) : ChatUiState()
}
