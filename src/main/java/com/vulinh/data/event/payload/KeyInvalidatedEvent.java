package com.vulinh.data.event.payload;

import java.util.Objects;

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
public record KeyInvalidatedEvent(String kid, String issuer, Reason reason) {

  public enum Reason {
    STARTUP,
    CONTEXT_REFRESHED
  }

  public static Builder builder() {
    return new Builder();
  }

  public KeyInvalidatedEvent withKid(String kid) {
    return Objects.equals(this.kid, kid) ? this : new KeyInvalidatedEvent(kid, issuer, reason);
  }

  public KeyInvalidatedEvent withIssuer(String issuer) {
    return Objects.equals(this.issuer, issuer)
        ? this
        : new KeyInvalidatedEvent(kid, issuer, reason);
  }

  public KeyInvalidatedEvent withReason(Reason reason) {
    return Objects.equals(this.reason, reason)
        ? this
        : new KeyInvalidatedEvent(kid, issuer, reason);
  }

  public Builder toBuilder() {
    return new Builder().kid(kid).issuer(issuer).reason(reason);
  }

  public static final class Builder {

    private String kid;
    private String issuer;
    private Reason reason;

    private Builder() {
      // Hidden constructor
    }

    public Builder kid(String kid) {
      this.kid = kid;
      return this;
    }

    public Builder issuer(String issuer) {
      this.issuer = issuer;
      return this;
    }

    public Builder reason(Reason reason) {
      this.reason = reason;
      return this;
    }

    public KeyInvalidatedEvent build() {
      return new KeyInvalidatedEvent(kid, issuer, reason);
    }
  }
}
