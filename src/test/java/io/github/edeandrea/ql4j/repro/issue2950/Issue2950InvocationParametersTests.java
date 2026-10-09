package io.github.edeandrea.ql4j.repro.issue2950;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.edeandrea.ql4j.repro.support.FakeOpenAiResource;
import io.quarkus.test.junit.QuarkusTest;

/**
 * On an affected version this class never runs: the build fails in
 * {@code AgenticProcessor#validateAgenticParameterTypes} with
 * {@code No agent provides an output key named 'invocationParameters'}, because the check skips {@code AgenticScope}
 * and {@code @MemoryId} parameters but not {@code InvocationParameters}.
 */
@QuarkusTest
class Issue2950InvocationParametersTests {
	@Inject
	OrderSupportAgent agent;

	@BeforeEach
	void reset() {
		FakeOpenAiResource.REQUESTS.clear();
	}

	@Test
	void aSubAgentGetsTheInvocationParametersTheRootPutInTheScope() {
		var answer = this.agent.answer("Where is my order?", "A-42");

		System.out.printf("### answer=%s%n", answer);
		FakeOpenAiResource.REQUESTS.forEach(body -> System.out.printf("### request=%s%n", body));

		assertThat(answer).isEqualTo("ok");
		assertThat(FakeOpenAiResource.REQUESTS)
			.as("the tool ran with the root's InvocationParameters, and its result went back to the model")
			.hasSize(2)
			.last()
			.asString()
			.contains("Order A-42 has shipped");
	}
}
