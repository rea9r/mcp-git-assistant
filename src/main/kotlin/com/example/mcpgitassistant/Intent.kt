package com.example.mcpgitassistant

enum class GitIntent {
	GIT_LOG,
	GIT_DIFF,
	GIT_BRANCH,
	GIT_FILE,
	OTHER,
}

/**
 * LLM が user prompt から抽出した Git 操作 intent + 必要な parameter。
 *
 * intent によって使われる field は異なる。例:
 * - GIT_LOG: limit
 * - GIT_DIFF: base, head
 * - GIT_BRANCH: (なし)
 * - GIT_FILE: path, branch
 * - OTHER: (なし)
 */
data class IntentClassification(
	val intent: GitIntent,
	val limit: Int? = null,
	val base: String? = null,
	val head: String? = null,
	val path: String? = null,
	val branch: String? = null,
)
