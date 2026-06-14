# mcp-git-assistant

A self-hostable MCP server that exposes Git repository context (commit history, diffs, branches, file contents) to AI assistants, with a built-in chat endpoint to talk to the assistant directly. Built with Spring AI + Kotlin on Spring Boot.

> Status: work in progress.

## What it provides

Over the MCP `/sse` endpoint, the server exposes:

- `gitLog` — recent commits (`limit` defaults to 20, capped at 200)
- `gitBranch` — local branches
- `gitDiff` — unified diff between two refs (output truncated at 50,000 chars)
- `gitFileContent` — file contents at a given ref (defaults to `HEAD`, output truncated at 50,000 chars)

A built-in `POST /chat` endpoint uses the same tools internally to answer natural-language questions about the repo.

## Configuration

Set via environment variables:

| Variable | Required | Default | Description |
|---|---|---|---|
| `OPENAI_API_KEY` | yes | — | OpenAI API key used by the chat endpoint. |
| `MCP_GIT_REPO_PATH` | no | `.` | Path to the Git repository the server exposes. |

## Running

```bash
export OPENAI_API_KEY=sk-...
export MCP_GIT_REPO_PATH=/path/to/your/repo  # optional, defaults to the working directory
./gradlew bootRun
```

## Talking to it

### Via MCP

The MCP server is reachable at `http://localhost:8080/sse` (SSE transport). For ad-hoc exploration, the MCP inspector works well:

```bash
npx @modelcontextprotocol/inspector
```

Then connect with Transport `SSE` and URL `http://localhost:8080/sse`.

### Via the chat endpoint

```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Show the latest 3 commits."}'
```

Pass a `conversationId` to maintain context across turns:

```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Show the latest 3 commits.", "conversationId": "alice"}'

curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Which of those touched README?", "conversationId": "alice"}'
```
