package com.vulinh.data.event;

import com.vulinh.data.UuidIdentifiable;
import java.util.Objects;
import java.util.UUID;

public record ActionUser(UUID id, String username) implements UuidIdentifiable {

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
