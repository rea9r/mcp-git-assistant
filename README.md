# mcp-git-assistant

A self-hostable MCP server that exposes Git repository context (commit history, diffs, branches, file contents, and more) to AI assistants, with a built-in chat endpoint to talk to the assistant directly. Built with Spring AI + Kotlin on Spring Boot.

> Status: work in progress.

## Running

Set your OpenAI API key:

```bash
export OPENAI_API_KEY=sk-...
```

Start the server:

```bash
./gradlew bootRun
```

### Try the chat endpoint

```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{"prompt": "hello"}'
```
