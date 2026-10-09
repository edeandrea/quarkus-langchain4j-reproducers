package io.github.edeandrea.ql4j.repro.issue2950;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;

/**
 * A sub-agent with an {@link InvocationParameters} parameter, which langchain4j-agentic fills from the agentic scope's
 * execution context ({@code AgentUtil.agentInvocationArguments}), and which the AI service hands to its tools.
 */
@RegisterAiService
public interface OrderStatusAgent {
	@Agent(value = "Answers an order status question", outputKey = "answer")
	@ToolBox(OrderTools.class)
	@UserMessage("Answer the customer: {question}")
	String answerOrderQuestion(String question, InvocationParameters invocationParameters);
}
