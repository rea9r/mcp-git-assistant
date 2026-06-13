package com.example.mcpgitassistant

import org.springframework.ai.chat.client.ChatClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class IntentController(builder: ChatClient.Builder) {
	private val chatClient = builder.build()

	@PostMapping("/intent")
	fun classify(@RequestBody request: IntentRequest): IntentClassification {
		return chatClient.prompt()
			.system(SYSTEM_PROMPT)
			.user(request.prompt)
			.call()
			.entity(IntentClassification::class.java)
			?: IntentClassification(intent = GitIntent.OTHER)
	}

	private companion object {
		private val SYSTEM_PROMPT = """
			You classify user requests into Git command intents.

			Choose one intent from the enum and extract the relevant parameters:
			- GIT_LOG: show commit history (optional: limit as positive integer)
			- GIT_DIFF: show diff between two refs (required: base, head)
			- GIT_BRANCH: list branches (no parameters)
			- GIT_FILE: show file contents (required: path; optional: branch)
			- OTHER: any request that doesn't fit the above

			Set unused parameters to null. Do not invent values that the user did not provide.
		""".trimIndent()
	}
}

data class IntentRequest(val prompt: String)
