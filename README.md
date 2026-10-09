# quarkus-langchain4j reproducers

Runnable reproducers for [quarkiverse/quarkus-langchain4j](https://github.com/quarkiverse/quarkus-langchain4j) bugs:

| Issue | Package | Test |
|---|---|---|
| [#2950](https://github.com/quarkiverse/quarkus-langchain4j/issues/2950): an agent's `InvocationParameters` parameter fails the build with `No agent provides an output key named 'invocationParameters'` | `issue2950` | `Issue2950InvocationParametersTests` |

Each test asserts the **expected** behaviour, so **on an affected version the tests fail** (here the build itself fails,
so `Tests run: 0`). That's the reproduction.

## Running

Needs Java 21+ only. No API keys, no network and no containers: the LangChain4j AI services call `FakeOpenAiResource`, a
stand-in OpenAI chat-completions endpoint served by the app itself on test port 18089, so they make real AI-service calls.

```bash
./mvnw test                                       # quarkus-langchain4j 1.14.1
./mvnw test -Dtest=Issue2950InvocationParametersTests
```

Versions: Quarkus 3.40.1, quarkus-langchain4j 1.14.1 (see `pom.xml`). **To try another quarkus-langchain4j version (e.g. a
local `999-SNAPSHOT`), change `quarkus.langchain4j.version` in `pom.xml`**: `@QuarkusTest`'s bootstrap re-resolves the
extension's deployment artifacts from the pom, so a `-Dquarkus.langchain4j.version=…` override changes the runtime jars
but still builds with the old deployment jar.

## What you should see on an affected version

**#2950**: the build fails in `AgenticProcessor#validateAgenticParameterTypes` with
`No agent provides an output key named 'invocationParameters'. This means that parameter no.1 of method '…OrderStatusAgent' … cannot be resolved`.
With the check fixed, the test passes: `OrderTools.lookUpOrder` runs with the `InvocationParameters` the root put into
the scope (`### request=` shows the tool result `Order A-42 has shipped` sent back to the model).
