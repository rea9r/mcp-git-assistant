package com.example.mcpgitassistant

import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class McpToolsConfig {
	@Bean
	fun toolCallbackProvider(
		healthCheckTool: HealthCheckTool,
		gitTools: GitTools,
	): ToolCallbackProvider =
		MethodToolCallbackProvider.builder()
			.toolObjects(healthCheckTool, gitTools)
			.build()
}
