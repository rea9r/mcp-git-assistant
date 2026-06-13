package com.example.mcpgitassistant

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "git-assistant")
data class GitAssistantProperties(
	val repoPath: String = ".",
)
