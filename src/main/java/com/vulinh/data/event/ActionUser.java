package com.vulinh.data.event;

import com.vulinh.data.base.UuidIdentifiable;
import java.util.Objects;
import java.util.UUID;

public record ActionUser(UUID id, String username) implements UuidIdentifiable {

  /**
   * Sentinel {@link ActionUser} for events emitted by the system rather than a real end-user, that
   * carried on {@link EventMessageWrapper#actionUser()} so the field is never {@code null} on the
   * wire. Producers of system-triggered events should use this constant; consumers can recognize it
   * by comparing {@link #id()} against the all-{@code f} UUID.
   */
  public static final ActionUser SYSTEM =
      new ActionUser(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"), "spring-base-auth");

  public static ActionUserBuilder builder() {
    return new ActionUserBuilder();
  }

  public ActionUser withId(UUID id) {
    return Objects.equals(this.id, id) ? this : new ActionUser(id, username);
  }

  public ActionUser withUsername(String username) {
    return Objects.equals(this.username, username) ? this : new ActionUser(id, username);
  }

  @Override
  public UUID getId() {
    return id;
  }

  public static class ActionUserBuilder {

    private UUID id;
    private String username;

    ActionUserBuilder() {}

    public ActionUserBuilder id(UUID id) {
      this.id = id;
      return this;
    }

    public ActionUserBuilder username(String username) {
      this.username = username;
      return this;
    }

    public ActionUser build() {
      return new ActionUser(id, username);
    }
  }
}
