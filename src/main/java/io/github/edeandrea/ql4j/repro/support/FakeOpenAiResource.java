package io.github.edeandrea.ql4j.repro.support;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * A stand-in for the OpenAI chat completions endpoint, served by the app itself, so the reproducers make real
 * LangChain4j AI-service calls with no API key, no network and no containers. See
 * {@code quarkus.langchain4j.openai.base-url} in {@code application.properties}.
 * <p>
 * The first request with tools asks for the first tool; any later request (one that carries a tool result) gets a
 * text answer. Every request body is recorded, so a test can read what was sent back to the model.
 */
@Path("/fake-openai/v1/chat/completions")
public class FakeOpenAiResource {
	public static final List<String> REQUESTS = new CopyOnWriteArrayList<>();
	private static final Pattern TOOLS = Pattern.compile("\"tools\"\\s*:");
	private static final Pattern TOOL_RESULT = Pattern.compile("\"role\"\\s*:\\s*\"tool\"");

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String chatCompletion(String body) {
		REQUESTS.add(body);
		var asksForTool = TOOLS.matcher(body).find() && !TOOL_RESULT.matcher(body).find();
		return asksForTool ? toolCall() : answer();
	}

	private static String toolCall() {
		return """
			{
				"id": "chatcmpl-fake", "object": "chat.completion", "created": 1234567890, "model": "fake",
				"choices": [ { "index": 0, "finish_reason": "tool_calls", "message": { "role": "assistant", "content": null,
					"tool_calls": [ { "id": "call_1", "type": "function", "function": { "name": "lookUpOrder", "arguments": "{}" } } ] } } ],
				"usage": { "prompt_tokens": 1, "completion_tokens": 1, "total_tokens": 2 }
			}
			""";
	}

	private static String answer() {
		return """
			{
				"id": "chatcmpl-fake", "object": "chat.completion", "created": 1234567890, "model": "fake",
				"choices": [ { "index": 0, "message": { "role": "assistant", "content": "ok" }, "finish_reason": "stop" } ],
				"usage": { "prompt_tokens": 1, "completion_tokens": 1, "total_tokens": 2 }
			}
			""";
	}
}
