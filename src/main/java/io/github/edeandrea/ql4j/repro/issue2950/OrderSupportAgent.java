package io.github.edeandrea.ql4j.repro.issue2950;

import dev.langchain4j.agentic.declarative.BeforeCall;
import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.invocation.InvocationParameters;

/**
 * The root. It has no {@code InvocationParameters} parameter: it puts them into the scope's execution context itself, in
 * a {@code @BeforeCall} hook, which is where langchain4j-agentic reads them from for {@link OrderStatusAgent}.
 */
public interface OrderSupportAgent {
	@SequenceAgent(outputKey = "answer", subAgents = OrderStatusAgent.class)
	String answer(String question, String orderId);

	@BeforeCall
	static void scopeToolsToTheOrder(AgenticScope agenticScope) {
		agenticScope.writeExecutionContext(InvocationParameters.class, InvocationParameters.from("orderId", agenticScope.readState("orderId")));
	}
}
