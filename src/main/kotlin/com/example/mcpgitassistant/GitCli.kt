package com.example.mcpgitassistant

import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Paths

@Service
class GitCli(properties: GitAssistantProperties) {
	private val workingDir: File = Paths.get(properties.repoPath).toAbsolutePath().normalize().toFile()

	fun run(vararg args: String): String {
		val process = ProcessBuilder(listOf("git") + args.toList())
			.directory(workingDir)
			.redirectErrorStream(true)
			.start()
		val output = process.inputStream.bufferedReader().readText()
		val exitCode = process.waitFor()
		check(exitCode == 0) { "git ${args.joinToString(" ")} failed (exit=$exitCode): $output" }
		return output
	}
}
