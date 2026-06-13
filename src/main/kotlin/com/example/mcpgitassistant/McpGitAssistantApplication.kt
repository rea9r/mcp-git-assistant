package com.example.mcpgitassistant

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class McpGitAssistantApplication

fun main(args: Array<String>) {
	runApplication<McpGitAssistantApplication>(*args)
}
