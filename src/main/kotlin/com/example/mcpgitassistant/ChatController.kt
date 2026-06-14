package com.example.mcpgitassistant

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
	builder: ChatClient.Builder,
	chatMemory: ChatMemory,
	private val toolCallbackProvider: ToolCallbackProvider,
) {
	private val chatClient = builder
		.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
		.build()

	@PostMapping("/chat")
	fun chat(@RequestBody request: ChatRequest): ChatResponse {
		val content = chatClient.prompt()
			.system(SYSTEM_PROMPT)
			.toolCallbacks(toolCallbackProvider)
			.advisors { it.param(ChatMemory.CONVERSATION_ID, request.conversationId ?: "default") }
			.user(request.prompt)
			.call()
			.content()
		return ChatResponse(content.orEmpty())
	}

	private companion object {
		private val SYSTEM_PROMPT = """
			You are an assistant that answers questions about a local Git repository.

			Use the available tools (gitLog, gitBranch, gitDiff, gitFileContent) when the
			question requires repository data. Quote command output as code blocks when useful.
			For path / ref arguments, only use values that appear in the user's question or
			in previous tool output -- do not invent paths or refs.
		""".trimIndent()
	}
}

data class ChatRequest(val prompt: String, val conversationId: String? = null)

data class ChatResponse(val response: String)
