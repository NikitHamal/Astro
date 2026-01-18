package com.astro.storm.core.model

/**
 * Represents a distinct part of an agent's response stream.
 * Used for the "Flashy" sectioned chat UI.
 */
sealed class FlashyPart {
    data class Text(
        val content: String, 
        val isStreaming: Boolean = false
    ) : FlashyPart()

    data class Thought(
        val content: String, 
        val isStreaming: Boolean = false,
        val isCollapsed: Boolean = true
    ) : FlashyPart()

    data class ToolCall(
        val id: String,
        val name: String,
        val args: String,
        val state: ToolState = ToolState.Pending,
        val isExpanded: Boolean = false
    ) : FlashyPart()

    data class ToolResult(
        val id: String,
        val result: String,
        val isError: Boolean = false
    ) : FlashyPart()
}

enum class ToolState {
    Pending, Executing, Completed, Failed
}

/**
 * State of the agent's streaming process.
 */
sealed class AgentStreamState {
    data class Update(val parts: List<FlashyPart>) : AgentStreamState()
    data class Interrupt(
        val question: String,
        val options: List<Map<String, String>>, // simplified options
        val allowCustomInput: Boolean,
        val context: String?
    ) : AgentStreamState()
    data class Error(val message: String) : AgentStreamState()
    data class Finished(val parts: List<FlashyPart>) : AgentStreamState()
}
