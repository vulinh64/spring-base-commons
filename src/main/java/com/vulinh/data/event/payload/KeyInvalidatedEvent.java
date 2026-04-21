package com.vulinh.data.event.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Payload of a {@link com.vulinh.data.event.EventType#KEY_INVALIDATED} event, published by the auth
 * server whenever its in-memory signing keypair changes (typically a JVM restart or context
 * refresh). Consumers are expected to refresh their JWKS cache so they can verify tokens minted
 * with the new key.
 *
 * <p>The {@link #kid} carried here is the <strong>new active kid</strong>: the one the auth
 * server's JWKS now exposes for signing. The previous kid is not included; consumers who cache JWKS
 * by kid should fetch the JWKS again and adopt whatever appears.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyInvalidatedEvent(String kid, String issuer, Reason reason) {

  public enum Reason {
    STARTUP,
    CONTEXT_REFRESHED
  }
}
