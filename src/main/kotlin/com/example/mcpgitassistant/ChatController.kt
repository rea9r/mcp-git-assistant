package com.example.mcpgitassistant

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
	builder: ChatClient.Builder,
	private val toolCallbackProvider: ToolCallbackProvider,
) {
	private val chatClient = builder.build()

	@PostMapping("/chat")
	fun chat(@RequestBody request: ChatRequest): ChatResponse {
		val content = chatClient.prompt()
			.system(SYSTEM_PROMPT)
			.toolCallbacks(toolCallbackProvider)
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

data class ChatRequest(val prompt: String)

data class ChatResponse(val response: String)
