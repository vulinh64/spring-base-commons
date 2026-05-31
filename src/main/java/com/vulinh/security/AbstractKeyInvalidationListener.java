package com.vulinh.security;

import com.vulinh.data.event.EventMessageWrapper;
import com.vulinh.data.event.payload.KeyInvalidatedEvent;
import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;

/**
 * Base class for services that subscribe to {@link KeyInvalidatedEvent} broadcasts from the auth
 * server, mirroring the dormant-until-extended idiom established by {@link
 * com.vulinh.exception.CommonExceptionHandler}.
 *
 * <h2>Usage</h2>
 *
 * <p>This class is intentionally <em>not</em> annotated with {@link
 * org.springframework.context.annotation.Configuration} or {@link
 * org.springframework.stereotype.Component}, so it does nothing on its own: it is dormant unless a
 * consuming service explicitly extends it. The expected usage is:
 *
 * <pre>{@code
 * @Configuration
 * public class JwtKeyInvalidationConfig extends AbstractKeyInvalidationListener {
 *
 *   @Override
 *   protected void onKeyInvalidated(EventMessageWrapper<KeyInvalidatedEvent> event) {
 *     // refresh JWKS cache, or no-op if relying on lazy-on-unknown-kid
 *   }
 * }
 * }</pre>
 *
 * <p>Pair with a Spring Cloud Stream inbound binding named {@code keyInvalidated-in-0}. The
 * destination should be configured from {@value #KEY_INVALIDATED_TOPIC_NAME_PROPERTY}, whose
 * default value is declared in {@code application.properties}.
 *
 * <pre>{@code
 * spring.cloud.function.definition: keyInvalidated
 * spring.cloud.stream.bindings.keyInvalidated-in-0.destination: ${application-properties.message-topic.key-invalidated.topic-name}
 * spring.cloud.stream.bindings.keyInvalidated-in-0.group: ${spring.application.name}
 * }</pre>
 *
 * <h2>Idempotency</h2>
 *
 * <p>{@link #onKeyInvalidated(EventMessageWrapper)} <strong>may be invoked more than once</strong>
 * for the same logical event (RabbitMQ redelivery on ack failure). Implementations must be
 * idempotent: refreshing the JWKS cache twice in succession is the canonical safe operation. The
 * wrapper's {@link EventMessageWrapper#eventId()} is a stable dedup key if a service needs to
 * detect repeats explicitly.
 *
 * <h2>Delivery</h2>
 *
 * <p>Delivery is best-effort. If RabbitMQ is unreachable when the producer publishes, or if the
 * consumer's queue isn't durable, the event may be dropped. Spring's lazy-on-unknown-{@code kid}
 * JWKS refresh remains the correctness backstop; this listener exists to shorten the recovery
 * window, not to be the sole mechanism.
 */
public abstract class AbstractKeyInvalidationListener {

  public static final String KEY_INVALIDATED_TOPIC_NAME_PROPERTY =
      "application-properties.message-topic.key-invalidated.topic-name";

  /**
   * Spring Cloud Stream {@link Consumer} bean that funnels the inbound event through {@link
   * #onKeyInvalidated(EventMessageWrapper)}. The bean name {@code keyInvalidated} is load-bearing,
   * as it must match the binding name {@code keyInvalidated-in-0} in the consuming service's
   * configuration.
   *
   * <p>⚠️ DO NOT rename in subclasses!!!
   */
  @Bean
  public Consumer<EventMessageWrapper<KeyInvalidatedEvent>> keyInvalidated() {
    return this::onKeyInvalidated;
  }

  /**
   * Hook for the consuming service to act on the event: typically forces a JWKS cache refresh.
   * Maybe a no-op if the service relies on Spring's lazy-on-unknown-{@code kid} JWKS refresh.
   *
   * <p>Implementations must be idempotent (see class-level javadoc).
   *
   * @param event the wrapped key-invalidation event
   */
  protected abstract void onKeyInvalidated(EventMessageWrapper<KeyInvalidatedEvent> event);
}
