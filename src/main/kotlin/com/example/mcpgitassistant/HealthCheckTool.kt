package com.example.mcpgitassistant

import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service

@Service
class HealthCheckTool {
	@Tool(description = "Returns 'OK' to confirm the MCP server is alive.")
	fun ping(): String = "OK"

	@Tool(description = "Echoes the input message back, useful for verifying round-trip communication.")
	fun echo(
		@ToolParam(description = "Message to echo back to the caller.") message: String,
	): String = message
}
