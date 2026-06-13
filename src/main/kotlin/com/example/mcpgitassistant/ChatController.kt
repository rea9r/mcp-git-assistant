package com.example.mcpgitassistant

import org.springframework.ai.chat.client.ChatClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(builder: ChatClient.Builder) {
	private val chatClient = builder.build()

	@PostMapping("/chat")
	fun chat(@RequestBody request: ChatRequest): ChatResponse {
		val content = chatClient.prompt()
			.user(request.prompt)
			.call()
			.content()
		return ChatResponse(content.orEmpty())
	}
}

data class ChatRequest(val prompt: String)

data class ChatResponse(val response: String)
