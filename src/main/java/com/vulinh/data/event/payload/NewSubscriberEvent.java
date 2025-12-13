package com.vulinh.data.event.payload;

import com.vulinh.data.UuidIdentifiable;
import java.util.Objects;
import java.util.UUID;

public record NewSubscriberEvent(UUID subscribedUserId, String subscribedUsername)
    implements UuidIdentifiable {

  public static NewSubscriberEventBuilder builder() {
    return new NewSubscriberEventBuilder();
  }

  public NewSubscriberEvent withSubscribedUserId(UUID subscribedUserId) {
    return Objects.equals(this.subscribedUserId, subscribedUserId)
        ? this
        : new NewSubscriberEvent(subscribedUserId, subscribedUsername);
  }

  public NewSubscriberEvent withSubscribedUsername(String subscribedUsername) {
    return Objects.equals(this.subscribedUsername, subscribedUsername)
        ? this
        : new NewSubscriberEvent(subscribedUserId, subscribedUsername);
  }

  @Override
  public UUID getId() {
    return subscribedUserId;
  }

  public static class NewSubscriberEventBuilder {

    private UUID subscribedUserId;
    private String subscribedUsername;

    NewSubscriberEventBuilder() {}

    public NewSubscriberEventBuilder subscribedUserId(UUID subscribedUserId) {
      this.subscribedUserId = subscribedUserId;
      return this;
    }

    public NewSubscriberEventBuilder subscribedUsername(String subscribedUsername) {
      this.subscribedUsername = subscribedUsername;
      return this;
    }

    public NewSubscriberEvent build() {
      return new NewSubscriberEvent(subscribedUserId, subscribedUsername);
    }
  }
}
