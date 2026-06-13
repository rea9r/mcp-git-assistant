package com.example.mcpgitassistant

import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service

@Service
class GitTools(private val gitCli: GitCli) {
	@Tool(description = "Show recent commits as 'sha subject (author, relative time)' lines, newest first.")
	fun gitLog(
		@ToolParam(description = "Maximum number of commits to return. Defaults to 20, capped at 200.", required = false) limit: Int? = null,
	): String {
		val effectiveLimit = (limit ?: 20).coerceIn(1, 200)
		return gitCli.run("log", "--max-count=$effectiveLimit", "--pretty=format:%h %s (%an, %ar)")
	}

	@Tool(description = "List all local branches, with the current branch marked by an asterisk.")
	fun gitBranch(): String = gitCli.run("branch")

	@Tool(description = "Show the unified diff between two refs (commit / branch / tag). Truncated if very large.")
	fun gitDiff(
		@ToolParam(description = "Base ref to compare from (e.g. 'main', 'HEAD~3', a commit SHA).") base: String,
		@ToolParam(description = "Head ref to compare to.") head: String,
	): String {
		requireValidRef(base, "base")
		requireValidRef(head, "head")
		return gitCli.run("diff", base, head, "--").truncate(MAX_OUTPUT_CHARS)
	}

	@Tool(description = "Show the contents of a file at a given ref (defaults to HEAD). Truncated if very large.")
	fun gitFileContent(
		@ToolParam(description = "Repo-relative file path.") path: String,
		@ToolParam(description = "Ref to read from. Defaults to HEAD.", required = false) ref: String? = null,
	): String {
		val effectiveRef = ref ?: "HEAD"
		requireValidRef(effectiveRef, "ref")
		requireValidPath(path)
		return gitCli.run("show", "$effectiveRef:$path").truncate(MAX_OUTPUT_CHARS)
	}

	private fun requireValidRef(value: String, name: String) {
		require(value.isNotBlank() && !value.startsWith("-") && value.none { it in FORBIDDEN_CHARS }) {
			"Invalid $name ref: '$value'"
		}
	}

	private fun requireValidPath(path: String) {
		require(
			path.isNotBlank() &&
				!path.startsWith("-") &&
				!path.contains("..") &&
				path.none { it in FORBIDDEN_CHARS },
		) {
			"Invalid path: '$path'"
		}
	}

	private fun String.truncate(maxChars: Int): String =
		if (length <= maxChars) this
		else take(maxChars) + "\n\n(... truncated, original $length characters)"

	private companion object {
		private val FORBIDDEN_CHARS = setOf('\t', '\n', ';', '&', '|', '$', '`', '<', '>')
		private const val MAX_OUTPUT_CHARS = 50_000
	}
}
