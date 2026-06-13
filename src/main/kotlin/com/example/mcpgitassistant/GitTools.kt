package com.example.mcpgitassistant

import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service

@Service
class GitTools(private val gitCli: GitCli) {
	@Tool(description = "Show recent commits as 'sha subject (author, relative time)' lines, newest first.")
	fun gitLog(
		@ToolParam(description = "Maximum number of commits to return. Defaults to 20, capped at 200.") limit: Int? = null,
	): String {
		val effectiveLimit = (limit ?: 20).coerceIn(1, 200)
		return gitCli.run("log", "--max-count=$effectiveLimit", "--pretty=format:%h %s (%an, %ar)")
	}

	@Tool(description = "List all local branches, with the current branch marked by an asterisk.")
	fun gitBranch(): String = gitCli.run("branch")
}
