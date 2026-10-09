package io.github.edeandrea.ql4j.repro.issue2950;

import jakarta.enterprise.context.ApplicationScoped;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.invocation.InvocationParameters;

/**
 * A tool scoped by the caller through {@link InvocationParameters} rather than by an argument the model chooses.
 */
@ApplicationScoped
public class OrderTools {
	@Tool("Looks up the customer's order")
	public String lookUpOrder(InvocationParameters parameters) {
		return "Order " + parameters.get("orderId") + " has shipped";
	}
}
