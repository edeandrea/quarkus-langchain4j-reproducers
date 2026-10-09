package io.github.edeandrea.ql4j.repro.issue2950;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.arc.Unremovable;

/**
 * Injects the root in {@code src/main}, so the build treats its parameters ({@code question}, {@code orderId}) as
 * caller-provided inputs, as it does for any real application.
 */
@ApplicationScoped
@Unremovable
public class OrderSupportEntryPoint {
	final OrderSupportAgent agent;

	OrderSupportEntryPoint(OrderSupportAgent agent) {
		this.agent = agent;
	}
}
